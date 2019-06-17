/*
 *     Copyright (C) 2016 psygate (https://github.com/psygate)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 */

package com.psygate.minecraft.spigot.sovereignty.ruby.data;

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.WorkerPool;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubySnitchesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordPersistenceManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordRenderer;
import com.psygate.spatial.primitives.IntAABB3D;
import com.psygate.spatial.primitives.IntBoundable3D;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables.*;

/**
 * Created by psygate (https://github.com/psygate) on 22.03.2016.
 */
public class Snitch implements IntBoundable3D {
    private final static Logger LOG = Ruby.getLogger(Snitch.class.getName());
    private final BlockKey source;
    private final IntAABB3D bounds;
    private final RubySnitchesRecord record;

    public Snitch(RubySnitchesRecord rubySnitchesRecord) {
        if (rubySnitchesRecord == null) {
            throw new NullPointerException("Record cannot be null.");
        } else if (rubySnitchesRecord.getSnitchId() == null) {
            throw new IllegalStateException("Snitch ID cannot be null.");
        }

        final int radius;
        switch (rubySnitchesRecord.getSnitchType()) {
            case SNITCH:
                radius = Ruby.getConf().getSnitches().getRadius();
                break;
            case ALERT:
                radius = Ruby.getConf().getAlerts().getRadius();
                break;
            default:
                radius = 1;
        }
//        int radius = Ruby.getConf().getSnitches().getRadius();

        if (radius <= 0) {
            throw new IllegalStateException("Snitch radius cannot be <= 0.");
        }

        source = new BlockKey(rubySnitchesRecord.getX(), rubySnitchesRecord.getY(), rubySnitchesRecord.getZ(), rubySnitchesRecord.getWorldUuid());
        bounds = new IntAABB3D(
                source.getX() - radius,
                source.getY() - radius,
                source.getZ() - radius,
                source.getX() + radius,
                source.getY() + radius,
                source.getZ() + radius
        );

        this.record = rubySnitchesRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snitch snitch = (Snitch) o;

        return source != null ? source.equals(snitch.source) : snitch.source == null;

    }

    @Override
    public int hashCode() {
        return source != null ? source.hashCode() : 0;
    }

    public RubySnitchesRecord getRecord() {
        return record;
    }

    public BlockKey getSource() {
        return source;
    }

    @Override
    public IntAABB3D getBounds() {
        return bounds;
    }

    @Override
    public String toString() {
        return record.getSnitchName();
    }

    public void presentInventory(Player player) {
        if (getSnitchType() == SnitchType.SNITCH) {
            int spaces = 9;

            DummyHolder holder = new DummyHolder();
            Inventory inventory = Bukkit.createInventory(holder, spaces, record.getSnitchName());
            holder.setInv(inventory);
            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {
                player.openInventory(inventory);
            });

            for (int i = 0; i < spaces; i++) {

                int bookID = i;
                RecordPersistenceManager.getInstance().loadRecords(this, i * Ruby.PAGE_SIZE, i * Ruby.PAGE_SIZE + Ruby.PAGE_SIZE, (list) -> {
                    if (!list.isEmpty()) {
                        String[] values = RecordRenderer.getInstance().renderBookMessages(list);
                        WorkerPool.submit(() -> {
                            ItemStack book = RecordRenderer.getInstance().renderBook(values, bookID);
                            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {
                                inventory.setItem(bookID, book);
                            });
                        });
                    }
                });
            }
        }
    }

    public void setX(Integer value) {
        record.setX(value);
    }

    public Integer getX() {
        return record.getX();
    }

    public void setY(Integer value) {
        record.setY(value);
    }

    public Integer getY() {
        return record.getY();
    }

    public void setZ(Integer value) {
        record.setZ(value);
    }

    public Integer getZ() {
        return record.getZ();
    }

    public void setWorldUuid(UUID value) {
        record.setWorldUuid(value);
    }

    public UUID getWorldUuid() {
        return record.getWorldUuid();
    }

    public void setSnitchId(Long value) {
        record.setSnitchId(value);
    }

    public Long getSnitchId() {
        return record.getSnitchId();
    }

    public void setCreator(UUID value) {
        record.setCreator(value);
    }

    public UUID getCreator() {
        return record.getCreator();
    }

    public void setCreation(Timestamp value) {
        record.setCreation(value);
    }

    public Timestamp getCreation() {
        return record.getCreation();
    }

    public void setGroupId(Long value) {
        record.setGroupId(value);
    }

    public Long getGroupId() {
        return record.getGroupId();
    }

    public void setSnitchName(String value) {
        record.setSnitchName(value);
    }

    public String getSnitchName() {
        return record.getSnitchName();
    }

    public void setSnitchType(SnitchType value) {
        record.setSnitchType(value);
    }

    public SnitchType getSnitchType() {
        return record.getSnitchType();
    }

    public Block getBlock() {
        return source.getLocation().getBlock();
    }

    private class DummyHolder implements InventoryHolder {
        private Inventory inv;

        public void setInv(Inventory inv) {
            this.inv = inv;
        }

        @Override
        public Inventory getInventory() {
            return inv;
        }
    }
}

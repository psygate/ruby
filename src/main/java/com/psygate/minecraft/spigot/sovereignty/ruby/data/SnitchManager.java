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

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyRecordsRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubySnitchesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordPersistenceManager;
import com.psygate.spatial.primitives.IntAABB3D;
import com.psygate.spatial.trees.AdaptiveIntAreaOcTree;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables.*;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class SnitchManager {
    private static SnitchManager instance;
    private final static Logger LOG = Ruby.getLogger(SnitchManager.class.getName());

    private SnitchManager() {

    }

    public static SnitchManager getInstance() {
        if (instance == null) {
            instance = new SnitchManager();
        }

        return instance;
    }

    private final static AdaptiveIntAreaOcTree<Snitch> EMPTY_TREE = new AdaptiveIntAreaOcTree<>(new IntAABB3D(0, 0, 0, 1, 1, 1), 8);
    private final Map<UUID, AdaptiveIntAreaOcTree<Snitch>> snitchMap = new HashMap<>();
    private final Map<BlockKey, Snitch> snitchBlockMap = new HashMap<>();

    public void loadAll(World world) {
        LOG.fine("Loading world: " + world.getName() + " (" + world.getUID() + ")");
        if (snitchMap.containsKey(world.getUID())) {
            LOG.fine("World " + world.getName() + " (" + world.getUID() + ") already loaded.");
            return;
        }
        UUID wuuid = world.getUID();
        Ruby.DBI().asyncSubmit((conf) -> {
            List<Snitch> snitches = DSL.using(conf).selectFrom(RUBY_SNITCHES)
                    .where(RUBY_SNITCHES.WORLD_UUID.eq(wuuid))
                    .fetch(record -> new Snitch(record));
            IntAABB3D completebounds = snitches.stream().map(Snitch::getBounds).reduce(new IntAABB3D(0, 0, 0, 1, 1, 1), (a, b) -> a.merge(b));
            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {

                for (Snitch snitch : snitches) {
                    boolean cull = false;

                    switch (snitch.getSnitchType()) {
                        case ALERT:
                            if (snitch.getBlock().getType() != Ruby.getConf().getAlerts().getType()) {
                                cull = true;
                            }
                            break;
                        case SNITCH:
                            if (snitch.getBlock().getType() != Ruby.getConf().getSnitches().getType()) {
                                cull = true;
                            }
                            break;
                    }

                    if (cull) {
                        LOG.info("Culling snitch: " + snitch + ", block type mismatch.");
                        destroySnitch(snitch.getSource());
                        continue;
                    }

                    snitchMap.compute(snitch.getSource().getUuid(), (uuid, tree) -> {
                        if (tree == null) {
                            tree = new AdaptiveIntAreaOcTree<>(completebounds, 8);
                        }

                        tree.add(snitch);
                        return tree;
                    });

                    snitchBlockMap.put(snitch.getSource(), snitch);
                }
                LOG.info("Loaded " + snitches.size() + " snitches for " + world.getName() + " (" + world.getUID() + ")");
            });
        });
    }

    public Collection<Snitch> getValuesIntersecting(UUID uid, IntAABB3D chunkAABB) {
        if (snitchMap.containsKey(uid)) {
            return snitchMap.get(uid).getValuesIntersecting(chunkAABB);
        } else {
            return Collections.emptyList();
        }
    }

    public void createSnitch(int chunkx, int chunkz, BlockKey coords, long groupid, UUID creator, Runnable failure, Runnable success, SnitchType type) {
        IntAABB3D chunkAABB = new IntAABB3D(chunkx, 0, chunkz, chunkx + 16, 256, chunkz + 16);
        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesIntersecting(coords.getUuid(), chunkAABB);
        //snitchMap.getOrDefault(ev.getTarget().getWorld().getUID(), EMPTY_TREE).getValuesIntersecting(chunkAABB);
        if (snitches.size() > Ruby.getConf().getLimits().getChunkLimit()) {
            failure.run();
        } else {
            String prename = (type.name() + "(" + coords.getX() + "," + coords.getY() + "," + coords.getZ() + "," + coords.getWorld().getName() + ")");
            String snitchName = prename.substring(0, Math.min(64, prename.length()));
//                Snitch snitch = new Snitch(ev.getTarget(), Ruby.getConf().getSnitches().getRadius());
            UUID wuuid = coords.getUuid();
            UUID puuid = creator;

            Ruby.DBI().asyncSubmit((conf) -> {
                RubySnitchesRecord rec = DSL.using(conf).insertInto(RUBY_SNITCHES).set(
                        new RubySnitchesRecord(
                                coords.getX(),
                                coords.getY(),
                                coords.getZ(),
                                wuuid,
                                null,
                                puuid,
                                new Timestamp(System.currentTimeMillis()),
                                groupid,
                                snitchName,
                                type
                        )
                ).returning().fetchOne();

//                    snitch.setRecord(rec);
                Snitch snitch = new Snitch(rec);
                Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {
                    snitchMap.compute(wuuid, (uuid, snitchIntAdaptiveTree) -> {
                        if (snitchIntAdaptiveTree == null) {
                            snitchIntAdaptiveTree = new AdaptiveIntAreaOcTree<>(snitch.getBounds(), 8);
                        }

                        snitchIntAdaptiveTree.add(snitch);

                        return snitchIntAdaptiveTree;
                    });

                    snitchBlockMap.put(snitch.getSource(), snitch);
                    success.run();
                });
            });
        }
    }

    public boolean hasValuesContaining(Block block) {
        return hasValuesContaining(block.getLocation());
    }

    public Collection<Snitch> getValuesContaining(Block block) {
        return getValuesContaining(block.getLocation());
    }

    public Collection<Snitch> getValuesContaining(Location location) {
        if (snitchMap.containsKey(location.getWorld().getUID())) {
            return snitchMap.get(location.getWorld().getUID()).getValuesContaining(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean destroySnitch(BlockKey target) {
        if (snitchBlockMap.containsKey(target)) {
            Snitch broken = snitchBlockMap.get(target);
            snitchMap.get(target.getUuid()).remove(broken);
            LOG.fine("Removed snitch " + target);
            Ruby.DBI().asyncSubmit((conf) -> {
                DSLContext ctx = DSL.using(conf);

                RecordPersistenceManager.getInstance().clearRecords(broken.getSnitchId(), ctx);
                int out = DSL.using(conf).deleteFrom(RUBY_SNITCHES).where(RUBY_SNITCHES.SNITCH_ID.eq(broken.getRecord().getSnitchId())).execute();
                LOG.fine("Database cleared snitch " + broken);
            });
            snitchBlockMap.remove(target);
            return true;
        }
        return false;
    }

    public boolean hasValuesContaining(Location block) {
        if (snitchMap.containsKey(block.getWorld().getUID())) {
            return snitchMap.get(block.getWorld().getUID()).hasValuesContaining(block.getBlockX(), block.getBlockY(), block.getBlockZ());
        } else {
            return false;
        }
    }


    public Collection<Snitch> getValuesContaining(BlockKey target) {
        if (snitchBlockMap.containsKey(target)) {
            return snitchMap.get(target.getUuid()).getValuesContaining(target.getX(), target.getY(), target.getZ());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean hasValuesContaining(BlockKey target) {
        if (snitchMap.containsKey(target.getUuid())) {
            return snitchMap.get(target.getUuid()).hasValuesContaining(target.getX(), target.getY(), target.getZ());
        } else {
            return false;
        }
    }

    public Optional<Snitch> getSnitch(Block clickedBlock) {
        BlockKey key = new BlockKey(clickedBlock);
        if (snitchBlockMap.containsKey(key)) {
            return Optional.of(snitchBlockMap.get(key));
        } else {
            return Optional.empty();
        }
    }

    public void flush() {
        Ruby.DBI().submit((conf) -> {
            persistSnitches(snitchBlockMap.values(), DSL.using(conf));
        });
    }

    private void persistSnitches(Collection<Snitch> values, DSLContext ctx) {
        try {
            int[] exec = ctx.batchUpdate(snitchBlockMap.values().stream().map(Snitch::getRecord).collect(Collectors.toList())).execute();
        } catch (Exception e) {
            LOG.warning("Failed to batch update snitches, falling back to single row mode.");

            for (Snitch snitch : snitchBlockMap.values()) {
                try {
                    int exec = ctx.executeUpdate(snitch.getRecord());
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Cannot persist snitch: " + snitch, e);
                }
            }
        }
    }

    public Optional<Snitch> getSnitch(int x, int y, int z, UUID world) {
        if (!snitchMap.containsKey(world)) {
            loadAll(Bukkit.getWorld(world));
        }

        BlockKey key = new BlockKey(x, y, z, world);
        if (!snitchBlockMap.containsKey(key)) {
            return Optional.empty();
        } else {
            return Optional.of(snitchBlockMap.get(new BlockKey(x, y, z, world)));
        }
    }

    public void reload(List<World> worlds) {
        snitchMap.clear();
        snitchBlockMap.clear();
        worlds.forEach(this::loadAll);
    }
}

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

package com.psygate.minecraft.spigot.sovereignty.ruby.events;

import com.psygate.collections.Pair;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RubyLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jooq.Record;
import org.stringtemplate.v4.ST;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by psygate (https://github.com/psygate) on 05.04.2016.
 */
public class SnitchRecord {
    private final static Logger LOG = Ruby.getLogger(SnitchRecord.class.getName());
    private final SnitchData snitchData;
    private final long timeOfEvent;
    private final LogType eventType;
    private final Map<RecordLocationType, RubyLocation> locations = new HashMap<>();
    private final Map<RecordPlayerType, Pair<UUID, String>> players = new HashMap<>();
    private final Map<RecordEntityType, Pair<EntityType, String>> entities = new HashMap<>();
    private final List<Material> materials = new ArrayList<>();

    public SnitchRecord(Snitch snitch, LogType eventType) {
        this(snitch, System.currentTimeMillis(), eventType);
    }

    public SnitchRecord(Snitch snitch, long timeOfEvent, LogType eventType) {
        this(
                snitch.getX(),
                snitch.getY(),
                snitch.getZ(),
                snitch.getWorldUuid(),
                snitch.getSnitchId(),
                snitch.getSnitchName(),
                timeOfEvent,
                snitch.getSnitchType(),
                eventType
        );
    }


    public SnitchRecord(int snitchX, int snitchY, int snitchZ, UUID snitchWorld, long snitchID, String snitchName, long timeOfEvent, SnitchType snitchType, LogType eventType) {
        this.snitchData = new SnitchData(snitchX, snitchY, snitchZ, snitchWorld, snitchID, snitchName, snitchType);
        this.timeOfEvent = requireGE(timeOfEvent, 0);
        this.eventType = Objects.requireNonNull(eventType);
    }

    public void add(RecordLocationType ltype, RubyLocation location) {
        if (locations.containsKey(ltype)) {
            throw new IllegalArgumentException("Duplicate location definition. (" + ltype + ")");
        }
        locations.put(Objects.requireNonNull(ltype), Objects.requireNonNull(location));
    }

    public void add(RecordPlayerType ptype, Player player) {
        Objects.requireNonNull(player);
        add(ptype, player.getUniqueId(), player.getName());
    }

    public void add(RecordPlayerType ptype, UUID playeruuid, String playername) {
        Objects.requireNonNull(ptype);
        if (players.containsKey(ptype)) {
            throw new IllegalArgumentException("Duplicate player definition. (" + ptype + ")");
        }

        players.put(Objects.requireNonNull(ptype), new Pair<>(playeruuid, playername));
    }

    public void addMaterial(Material mat) {
        materials.add(mat);
    }

    public void add(RecordEntityType etype, Entity entity) {
        Objects.requireNonNull(entity);
        add(etype, entity.getType(), entity.getCustomName());
    }

    public void add(RecordEntityType etype, EntityType type, String name) {
        if (players.containsKey(etype)) {
            throw new IllegalArgumentException("Duplicate player definition. (" + etype + ")");
        }
        Objects.requireNonNull(etype);
        entities.put(etype, new Pair<>(type, name));
    }

    private long requireGE(long timeOfEvent, int comp) {
        if (timeOfEvent < comp) {
            throw new IllegalArgumentException("Argument cannot be smaller than " + comp);
        }

        return timeOfEvent;
    }

    public SnitchData getSnitchData() {
        return snitchData;
    }

    public long getTimeOfEvent() {
        return timeOfEvent;
    }

    public LogType getEventType() {
        return eventType;
    }

//    public final String render(String template) {
//        ST st = new ST(template);
//        bindVariables(st);
//        return st.render();
//    }

    public Map<RecordLocationType, RubyLocation> getLocations() {
        return locations;
    }

    public Map<RecordPlayerType, Pair<UUID, String>> getPlayers() {
        return players;
    }

    public Map<RecordEntityType, Pair<EntityType, String>> getEntities() {
        return entities;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void add(Material type) {
        materials.add(type);
    }

    @Override
    public String toString() {
        return "SnitchRecord{" +
                "snitchData=" + snitchData +
                ", timeOfEvent=" + timeOfEvent +
                ", eventType=" + eventType +
                ", locations=" + locations +
                ", players=" + players +
                ", entities=" + entities +
                ", materials=" + materials +
                '}';
    }
}

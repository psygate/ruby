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

package com.psygate.minecraft.spigot.sovereignty.ruby.configuration;

import com.psygate.collections.Pair;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by psygate (https://github.com/psygate) on 21.03.2016.
 */
public class Configuration {
    private Limits limits;
    private Snitches snitches;
    private Alerts alerts;
    private Redstone redstone;
    private Map<LogType, String> broadcastTemplates = new HashMap<>();
    private Map<LogType, String> persistedTemplates = new HashMap<>();
    private Map<LogType, String> bookTemplates = new HashMap<>();


    public Configuration(FileConfiguration conf) {
        limits = new Limits(conf.getConfigurationSection("limits"));
        snitches = new Snitches(conf.getConfigurationSection("snitches"));
        alerts = new Alerts(conf.getConfigurationSection("alerts"));

        ConfigurationSection bts = conf.getConfigurationSection("record_renderer_settings.templates.broadcast");
        for (String key : bts.getKeys(false)) {
            broadcastTemplates.put(LogType.valueOf(key.toUpperCase().trim()), bts.getString(key));
        }

        ConfigurationSection pts = conf.getConfigurationSection("record_renderer_settings.templates.persisted");
        for (String key : pts.getKeys(false)) {
            persistedTemplates.put(LogType.valueOf(key.toUpperCase().trim()), pts.getString(key));
        }

        ConfigurationSection bots = conf.getConfigurationSection("record_renderer_settings.templates.book");
        for (String key : bots.getKeys(false)) {
            bookTemplates.put(LogType.valueOf(key.toUpperCase().trim()), bots.getString(key));
        }

        for (LogType type : LogType.values()) {
            if (!broadcastTemplates.containsKey(type)) {
                LogType[] loaded = broadcastTemplates.keySet().toArray(new LogType[0]);
                Arrays.sort(loaded, (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2)));
                throw new IllegalStateException("Missing broadcast template for " + type + " Loaded: " + Arrays.toString(loaded));
            }
            if (!persistedTemplates.containsKey(type)) {
                LogType[] loaded = persistedTemplates.keySet().toArray(new LogType[0]);
                Arrays.sort(loaded, (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2)));
                throw new IllegalStateException("Missing persisted template for " + type + " Loaded: " + Arrays.toString(loaded));
            }
            if (!bookTemplates.containsKey(type)) {
                LogType[] loaded = persistedTemplates.keySet().toArray(new LogType[0]);
                Arrays.sort(loaded, (o1, o2) -> String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2)));
                throw new IllegalStateException("Missing persisted template for " + type + " Loaded: " + Arrays.toString(loaded));
            }
        }

        redstone = new Redstone(conf.getConfigurationSection("redstone_events"));
    }

    public Limits getLimits() {
        return limits;
    }

    public Snitches getSnitches() {
        return snitches;
    }

    public Alerts getAlerts() {
        return alerts;
    }

    public Redstone getRedstone() {
        return redstone;
    }

    public String getBroadcastTemplate(LogType type) {
        return broadcastTemplates.get(Objects.requireNonNull(type));
    }

    public String getPersistedTemplate(LogType type) {
        return persistedTemplates.get(Objects.requireNonNull(type));
    }

    public String getBookMessages(LogType eventType) {
        return bookTemplates.get(Objects.requireNonNull(eventType));
    }

    public boolean isEmitRedstone(SnitchType snitchType) {
        switch (snitchType) {
            case SNITCH:
                return snitches.isRedstoneEmit();
            case ALERT:
                return alerts.isRedstoneEmit();
            default:
                return false;
        }
    }

    public byte getRedstoneEmissionEnter(Rank rank) {
        return redstone.getEmissionEnter(rank);
    }

    public byte getRedstoneEmissionExit(Rank rank) {
        return redstone.getEmissionExit(rank);
    }
}

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

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psygate on 27.05.2016.
 */
public class Redstone {
    private Map<Rank, Byte> enterEmission = new HashMap<>();
    private byte defaultEnterEmission;
    private Map<Rank, Byte> exitEmission = new HashMap<>();
    private byte defaultExitEmission;

    public Redstone(ConfigurationSection sec) {
        for (Rank rank : Rank.values()) {
            enterEmission.put(rank, (byte) sec.getInt("enter." + rank.name().toLowerCase()));
        }
        defaultEnterEmission = (byte) sec.getInt("enter.no_rank");

        for (Rank rank : Rank.values()) {
            exitEmission.put(rank, (byte) sec.getInt("exit." + rank.name().toLowerCase()));
        }
        defaultExitEmission = (byte) sec.getInt("exit.no_rank");
    }

    public byte getEmissionEnter(Rank rank) {
        if (enterEmission.containsKey(rank)) {
            return enterEmission.get(rank);
        }

        return defaultEnterEmission;
    }

    public byte getEmissionExit(Rank rank) {
        if (exitEmission.containsKey(rank)) {
            return exitEmission.get(rank);
        }

        return defaultExitEmission;
    }
}

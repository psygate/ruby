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

import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by psygate (https://github.com/psygate) on 21.03.2016.
 */
public class Limits {
    private int chunkLimit;

    public Limits(ConfigurationSection section) {
        chunkLimit = section.getInt("chunk_limit");
    }

    public int getChunkLimit() {
        return chunkLimit;
    }
}

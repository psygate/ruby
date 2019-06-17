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

import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;

import java.util.UUID;

/**
 * Created by psygate on 18.05.2016.
 */
public class SnitchData {
    private final int x, y, z;
    private final UUID worlduuid;
    private final long id;
    private final String name;
    private final SnitchType type;

    public SnitchData(int snitchX, int snitchY, int snitchZ, UUID snitchWorld, long snitchID, String snitchName, SnitchType type) {
        this.x = snitchX;
        this.y = snitchY;
        this.z = snitchZ;
        this.worlduuid = snitchWorld;
        this.id = snitchID;
        this.name = snitchName;
        this.type = type;
    }

    public SnitchData(Snitch snitch) {
        this.x = snitch.getX();
        this.y = snitch.getY();
        this.z = snitch.getZ();
        this.worlduuid = snitch.getWorldUuid();
        this.id = snitch.getSnitchId();
        this.name = snitch.getSnitchName();
        this.type = snitch.getSnitchType();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public UUID getWorlduuid() {
        return worlduuid;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SnitchType getType() {
        return type;
    }
}

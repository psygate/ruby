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

package com.psygate.minecraft.spigot.sovereignty.ruby.util.redstone;

import com.psygate.collections.Pair;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.Map;

/**
 * Created by psygate on 27.05.2016.
 */
public class RedstoneLockListener implements Listener {
    private Map<Block, Pair<Byte, MutInt>> emissionList;

    public RedstoneLockListener(Map<Block, Pair<Byte, MutInt>> emissionList) {
        this.emissionList = emissionList;
    }

    @EventHandler
    public void preventChange(BlockRedstoneEvent ev) {
        if (emissionList.containsKey(ev.getBlock())) {
            ev.setNewCurrent(emissionList.get(ev.getBlock()).getKey());
        }
    }
}

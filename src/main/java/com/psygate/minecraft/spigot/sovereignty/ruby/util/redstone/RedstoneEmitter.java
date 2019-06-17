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
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Member;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by psygate on 27.05.2016.
 */
public class RedstoneEmitter {
    private final static BlockFace[] search = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
    private final static Map<Material, BiConsumer<Block, Byte>> appliers = new HashMap<>();
    private static RedstoneEmitter instance;
    private Map<Block, Pair<Byte, MutInt>> emissionList = new HashMap<>();

    static {
        appliers.put(Material.REDSTONE_WIRE, (bl, b) -> {
            BlockState state = bl.getState();
            state.setRawData(b);
            state.update(true, true);
        });

    }

    private RedstoneEmitter() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Ruby.getInstance(),
                () -> {
                    // Make this time and not tick based.
                    if (!emissionList.isEmpty()) {
                        Iterator<Map.Entry<Block, Pair<Byte, MutInt>>> it = emissionList.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<Block, Pair<Byte, MutInt>> next = it.next();

                            if (next.getValue().getValue().isZeroOrLess()) {
                                it.remove();
                                appliers.get(next.getKey().getType()).accept(next.getKey(), (byte) 0);

                            }

                            next.getValue().getValue().dec();
                        }
                    }
                },
                1, 1
        );
    }

    public static RedstoneEmitter getInstance() {
        if (instance == null) {
            instance = new RedstoneEmitter();
            Bukkit.getPluginManager().registerEvents(new RedstoneLockListener(instance.emissionList), Ruby.getInstance());
        }

        return instance;
    }

    public void emitEnter(Snitch snitch, Player player) {
        Group group = GroupManager.getInstance().getGroup(snitch.getGroupId()).get();
        Rank rank = group.getMember(player.getUniqueId()).map(Member::getRank).orElseGet(() -> null);

        byte val = Ruby.getConf().getRedstoneEmissionEnter(rank);
        applyRedstoneCurrent(val, snitch.getBlock());
    }

    private void applyRedstoneCurrent(byte val, Block block) {
        if (val > 0) {
            for (int i = 0; i < search.length; i++) {
                Block rel = block.getRelative(search[i]);
                if (!emissionList.containsKey(rel) && appliers.containsKey(rel.getType())) {
                    emissionList.put(rel, new Pair<>(val, new MutInt(20)));
                    appliers.get(rel.getType()).accept(rel, val);
                }
            }
        }
    }

    public void emitExit(Snitch snitch, Player player) {
        Group group = GroupManager.getInstance().getGroup(snitch.getGroupId()).get();
        Rank rank = group.getMember(player.getUniqueId()).map(Member::getRank).orElseGet(() -> null);

        byte val = Ruby.getConf().getRedstoneEmissionExit(rank);
        applyRedstoneCurrent(val, snitch.getBlock());
    }
}

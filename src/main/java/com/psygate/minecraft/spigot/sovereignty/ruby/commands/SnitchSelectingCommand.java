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

package com.psygate.minecraft.spigot.sovereignty.ruby.commands;

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchManager;
import com.psygate.spatial.primitives.IntAABB3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by psygate (https://github.com/psygate) on 09.04.2016.
 */
public abstract class SnitchSelectingCommand extends NucleusPlayerCommand {
    public SnitchSelectingCommand(int minargs, int maxargs) {
        super(minargs, maxargs);
    }

    protected Snitch selectSnitch(Player player) {
        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(player.getLocation());
//        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesIntersecting(
//                player.getWorld().getUID(),
//                IntAABB3D.fromCenter(
//                        player.getLocation().getBlockX(),
//                        player.getLocation().getBlockY(),
//                        player.getLocation().getBlockZ(),
//                        Ruby.getConf().getSnitches().getRadius()
//                )
//        );

        if (player.isOp()) {
            player.sendMessage("Searching candidates: " + snitches.size() + " (" + snitches.stream().map(s -> s.toString() + "access[" + canAccess(s, player) + "]").collect(Collectors.toList()) + ")");
        }

        boolean found = false;
        Snitch candidate = null;
        double dist = Double.MAX_VALUE;
        if (!snitches.isEmpty()) {
            for (Snitch snitch : snitches) {
                if (candidate == null && canAccess(snitch, player)) {
                    candidate = snitch;
                    dist = distance(candidate.getSource(), player.getLocation());
                    found = true;
                } else {
                    double ldist = distance(snitch.getSource(), player.getLocation());
                    if (ldist < dist && canAccess(snitch, player)) {
                        candidate = snitch;
                        dist = ldist;
                    }
                    found = true;
                }
            }
        }
        if (!found) {
            throw new CommandException("No snitches found in your proximity.");
        } else {
            return candidate;
        }
    }

    private double distance(BlockKey source, Location location) {
        return (source.getX() - location.getBlockX()) * (source.getX() - location.getBlockX())
                + (source.getY() - location.getBlockY()) * (source.getY() - location.getBlockY())
                + (source.getZ() - location.getBlockZ()) * (source.getZ() - location.getBlockZ());
    }

    private boolean canAccess(Snitch snitch, Player player) {
        return player.isOp() || GroupManager.getInstance()
                .getGroup(snitch.getRecord().getGroupId())
                .map(g -> g.hasMemberWithRankGE(player.getUniqueId(), Rank.MODERATOR))
                .orElse(Boolean.FALSE);
    }

}

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

package com.psygate.minecraft.spigot.sovereignty.ruby.util;

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishPlugin;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by psygate (https://github.com/psygate) on 09.04.2016.
 */
public class RecordFilterUtil {
    public static boolean isBroadcastAble(Player player, Snitch snitch) {
        Optional<? extends Group> groupopt = GroupManager.getInstance().getGroup(snitch.getRecord().getGroupId());
        if (!groupopt.isPresent()) {
            return true;
        } else {
            return !groupopt.get().hasMemberWithRankGE(player.getUniqueId(), Rank.GUEST);
        }
    }

    public static boolean isLoggable(Player player, Snitch snitch) {
        if (snitch.getRecord().getSnitchType() == SnitchType.ALERT) {
            return false;
        } else if (!isBroadcastAble(player, snitch)) {
            return false;
        } else if (GroupManager.getInstance().getGroup(snitch.getRecord().getGroupId()).map(g -> g.hasMemberWithRankGE(player.getUniqueId(), Rank.MEMBER)).orElse(false)) {
            return false;
        }

        return true;
    }

    public static boolean isRedstoneEmitable(Player player, Snitch snitch) {
        return (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                && !isVanished(player);
    }

    private static Function<Player, Boolean> vanishHandler = null;

    public static boolean isVanished(Player player) {
        if (vanishHandler == null) {
            if (Bukkit.getPluginManager().getPlugin("VanishNoPacket") != null) {
                vanishHandler = (pl) -> VanishPlugin.getPlugin(VanishPlugin.class).getManager().isVanished(pl);
            } else {
                vanishHandler = (pl) -> Boolean.FALSE;
            }
        }

        return vanishHandler.apply(player);
    }
}

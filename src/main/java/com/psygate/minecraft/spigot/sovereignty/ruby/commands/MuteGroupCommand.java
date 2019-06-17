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

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.BroadcastManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class MuteGroupCommand extends SnitchSelectingCommand {
    private final static Pattern namePattern = Pattern.compile("[A-Za-z0-9+-_\\.,]+");

    public MuteGroupCommand() {
        super(1, 100);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        Ruby.DBI().asyncSubmit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            LinkedList<String> output = new LinkedList<>();
            Map<Long, String> mutes = new HashMap<>();

            for (String name : strings) {
                Optional<Long> groupid = GroupManager.getInstance().getGroup(name).map(Group::getGroupID);

                if (!groupid.isPresent()) {
                    output.add(ChatColor.RED + "Failed to mute or unmute " + name + ", group not found.");
                } else {
                    mutes.put(groupid.get(), name);
                }
            }

            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {
                for (Map.Entry<Long, String> id : mutes.entrySet()) {
                    if (BroadcastManager.getInstance().toggleGroupMute(player.getUniqueId(), id.getKey())) {
                        player.sendMessage(ChatColor.YELLOW + id.getValue() + ChatColor.RED + " muted.");
                    } else {
                        player.sendMessage(ChatColor.YELLOW + id.getValue() + ChatColor.GREEN + " unmuted.");
                    }
                }
            });
        });
    }


    @Override
    protected String[] getName() {
        return new String[]{"mutesnitchgroup"};
    }
}

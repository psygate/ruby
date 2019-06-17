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

import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.BroadcastManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class MuteSnitchCommand extends SnitchSelectingCommand {
    private final static Pattern namePattern = Pattern.compile("[A-Za-z0-9+-_\\.,]+");

    public MuteSnitchCommand() {
        super(1, 100);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        Ruby.DBI().asyncSubmit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            LinkedList<String> output = new LinkedList<>();
            Map<Long, String> mutes = new HashMap<>();

            for (String name : strings) {
                Optional<Long> snitchid = ctx.select(Tables.RUBY_SNITCHES.SNITCH_ID)
                        .from(Tables.RUBY_SNITCHES)
                        .where(Tables.RUBY_SNITCHES.SNITCH_NAME.eq(name))
                        .fetchOptional(Record1<Long>::value1);

                if (!snitchid.isPresent()) {
                    output.add(ChatColor.RED + "Failed to mute or unmute " + name + ", snitch not found.");
                } else {
                    mutes.put(snitchid.get(), name);
                }
            }

            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> {
                for (Map.Entry<Long, String> id : mutes.entrySet()) {
                    if (BroadcastManager.getInstance().toggleSnitchMute(player.getUniqueId(), id.getKey())) {
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
        return new String[]{"mutesnitch"};
    }
}

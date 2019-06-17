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

import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jooq.impl.DSL;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class NameSnitchCommand extends SnitchSelectingCommand {
    private final static Pattern namePattern = Pattern.compile("[A-Za-z0-9+-_\\.,]+");

    public NameSnitchCommand() {
        super(1, 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        if (!namePattern.matcher(strings[0]).matches()) {
            throw new CommandException("Illegal name.");
        } else if (strings[0].length() > 64) {
            throw new CommandException("Name too long.");
        } else {
            Snitch candidate = selectSnitch(player);
            if (candidate == null) {
                throw new CommandException(ChatColor.RED + "You don't own any snitches nearby.");
            } else {
                assert candidate.getRecord() != null : "Snitch has no attached record.";
                String name = candidate.getRecord().getSnitchName();
                candidate.getRecord().setSnitchName(strings[0]);
                Ruby.DBI().asyncSubmit((conf) -> {
                    DSL.using(conf).executeUpdate(candidate.getRecord());
                });
                player.sendMessage(ChatColor.GREEN + "Renamed \"" + name + "\" to \"" + candidate.getRecord().getSnitchName() + "\".");
            }
        }
    }


    @Override
    protected String[] getName() {
        return new String[]{"namesnitch"};
    }
}

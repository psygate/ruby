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
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordPersistenceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jooq.impl.DSL;

import java.util.regex.Pattern;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class ClearSnitchCommand extends SnitchSelectingCommand {
    private final static Pattern namePattern = Pattern.compile("[A-Za-z0-9+-_\\.,]+");

    public ClearSnitchCommand() {
        super(0, 0);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        Snitch candidate = selectSnitch(player);
        if (candidate == null) {
            throw new CommandException("No snitches found in your proximity.");
        } else {
            assert candidate.getRecord() != null : "Snitch has no attached record.";
            RecordPersistenceManager.getInstance().clearRecords(candidate);
            player.sendMessage(ChatColor.GREEN + "Cleared \"" + candidate.getRecord().getSnitchName() + ".");
        }
    }


    @Override
    protected String[] getName() {
        return new String[]{"clearsnitch"};
    }
}

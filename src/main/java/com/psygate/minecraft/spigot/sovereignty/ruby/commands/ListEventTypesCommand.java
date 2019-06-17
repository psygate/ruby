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

import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class ListEventTypesCommand extends NucleusPlayerCommand {
    private final static String[] output = renderOutput();

    private static String[] renderOutput() {
        String[] output = new String[1 + LogType.values().length];
        output[0] = ChatColor.YELLOW + "Event types:";
        LogType[] vals = LogType.values();

        for (int i = 0; i < vals.length; i++) {
            output[i + 1] = "- " + vals[i].name();
        }

        return output;
    }

    public ListEventTypesCommand() {
        super(0, 0);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        player.sendMessage(output);
    }

    @Override
    protected String[] getName() {
        return new String[]{"listeventtypes"};
    }
}

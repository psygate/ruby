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

import com.codahale.metrics.Timer;
import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.WorkerPool;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.SnitchRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.BroadcastManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordPersistenceManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables.*;

/**
 * Created by psygate (https://github.com/psygate) on 02.04.2016.
 */
public class ListEntriesCommand extends SnitchSelectingCommand {

    public ListEntriesCommand() {
        super(0, LogType.values().length + 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        int page = 0;
        int genindex = 0;
        Snitch snitch = selectSnitch(player);
        if (snitch == null) {
            throw new CommandException(ChatColor.RED + "No snitches found in your proximity.");
        } else if (snitch.getSnitchType() == SnitchType.ALERT) {
            throw new CommandException(ChatColor.RED + "Alerts do not record actions.");
        }

        if (strings.length > 0) {
            try {
                page = Integer.parseInt(strings[0]) - 1;
                genindex = 1;
                if (page < 0) {
                    throw new CommandException("Illegal page number.");
                }
            } catch (NumberFormatException e) {
//                throw new CommandException("Invalid page number: " + strings);
            }
        }

        String[] eventTypeNameArray;
        if (strings.length > genindex) {
            eventTypeNameArray = Arrays.copyOfRange(strings, genindex, strings.length);
        } else {
            eventTypeNameArray = new String[0];
        }

        player.sendMessage(ChatColor.YELLOW + "Records are being rendered. Please be patient.");
        int finalPage = page;
        WorkerPool.submit(() -> {
            Ruby.DBI().submit((conf) -> {
                int amount = DSL.using(conf).selectCount().from(RUBY_RECORDS).where(RUBY_RECORDS.SNITCH_ID.eq(snitch.getSnitchId())).fetchOne().value1();
                player.sendMessage(ChatColor.YELLOW + "Page " + (finalPage + 1) + " of " + (amount / Ruby.PAGE_SIZE + 1) + " pages.");
            });
            RecordPersistenceManager.getInstance().loadRecords(
                    snitch,
                    finalPage * Ruby.PAGE_SIZE,
                    finalPage * Ruby.PAGE_SIZE + Ruby.PAGE_SIZE,
                    (recs) -> {
                        if (recs.isEmpty()) {
                            String msg = ChatColor.YELLOW + "No more entries.";
                            Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> player.sendMessage(msg));
                        } else {
                            Timer.Context rendertimer = Nucleus.getMetricRegistry().timer("ruby-record-rendering").time();
                            try {
                                String[] msg = RecordRenderer.getInstance().renderPersistedMessages(recs);
                                Bukkit.getScheduler().runTask(Ruby.getInstance(), () -> player.sendMessage(BroadcastManager.sanitize(msg)));
                            } finally {
                                rendertimer.stop();
                            }
                        }
                    });
        });
    }

    @Override
    protected String[] getName() {
        return new String[]{"listentries"};
    }
}

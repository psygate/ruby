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

import com.psygate.collections.Pair;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.stringtemplate.v4.ST;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by psygate (https://github.com/psygate) on 03.04.2016.
 */
public class RecordRenderer {
    private final static Logger LOG = Ruby.getLogger(RecordRenderer.class.getName());
    private static RecordRenderer instance;

    private RecordRenderer() {

    }

    public static RecordRenderer getInstance() {
        if (instance == null) {
            instance = new RecordRenderer();
        }

        return instance;
    }

    public String renderChatMessage(SnitchRecord ev) {
        String template = Ruby.getConf().getBroadcastTemplate(ev.getEventType());
        return renderEvent(template, ev);
    }

    public String[] renderPersistedMessages(List<SnitchRecord> list) {
        String[] values = new String[list.size()];
        int idx = 0;

        for (SnitchRecord ev : list) {
            String template = Ruby.getConf().getPersistedTemplate(ev.getEventType());
            ST st = new ST(template);
            bind(st, ev, new Date(ev.getTimeOfEvent()));
            String persisted = st.render();
            values[idx] = persisted;
            idx++;
        }

        return values;
    }

    public String[] renderBookMessages(List<SnitchRecord> list) {
        String[] values = new String[list.size()];
        int idx = 0;

        for (SnitchRecord ev : list) {
            String template = Ruby.getConf().getBookMessages(ev.getEventType());
            ST st = new ST(template);
            bind(st, ev, new Date(ev.getTimeOfEvent()));
            values[idx] = st.render();
            idx++;
        }

        return values;
    }

    private String renderEvent(String template, SnitchRecord ev) {
        ST st = new ST(template);
        bind(st, ev, new Date());

        return st.render();
    }

    private void bind(ST st, SnitchRecord rec, Date date) {
        st.add("snitch_name", rec.getSnitchData().getName());
        st.add("snitch_x", String.valueOf(rec.getSnitchData().getX()));
        st.add("snitch_y", String.valueOf(rec.getSnitchData().getY()));
        st.add("snitch_z", String.valueOf(rec.getSnitchData().getZ()));
        st.add("snitch_worlduuid", String.valueOf(rec.getSnitchData().getWorlduuid()));
        st.add("snitch_worldname", Bukkit.getWorld(rec.getSnitchData().getWorlduuid()).getName());

        for (Map.Entry<RecordPlayerType, Pair<UUID, String>> player : rec.getPlayers().entrySet()) {
            st.add(player.getKey().name().toLowerCase() + "_uuid", String.valueOf(player.getValue().getKey()));
            st.add(player.getKey().name().toLowerCase() + "_name", String.valueOf(player.getValue().getValue()));
        }

        for (Map.Entry<RecordEntityType, Pair<EntityType, String>> entity : rec.getEntities().entrySet()) {
            st.add(entity.getKey().name().toLowerCase() + "_type", String.valueOf(entity.getValue().getKey()));
            st.add(entity.getKey().name().toLowerCase() + "_name", String.valueOf(entity.getValue().getValue()));
        }

        for (Map.Entry<RecordLocationType, RubyLocation> en : rec.getLocations().entrySet()) {
            LOG.info("Binding: " + en.getKey() + " as " + en.getValue());
            st.add(en.getKey().name().toLowerCase() + "_x", String.valueOf(en.getValue().getX()));
            st.add(en.getKey().name().toLowerCase() + "_y", String.valueOf(en.getValue().getY()));
            st.add(en.getKey().name().toLowerCase() + "_z", String.valueOf(en.getValue().getZ()));
            st.add(en.getKey().name().toLowerCase() + "_worldname", Bukkit.getWorld(en.getValue().getWorld()).getName());
            st.add(en.getKey().name().toLowerCase() + "_worlduuid", String.valueOf(en.getValue().getWorld()));
        }

        int idx = 0;
        for (Material mat : rec.getMaterials()) {
            if (idx >= 1) {
                st.add("material_" + idx, mat.name());
            } else {
                st.add("material", mat.name());
            }
        }

        for (ChatColor color : ChatColor.values()) {
            st.add(color.name().toLowerCase(), color.toString());
        }

        st.add("br", "\n");

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        st.add("date", format.format(date));
    }

    public ItemStack renderBook(String[] values, int bookID) {
        String[] rvals = Arrays.stream(values)
                .map(m -> m.replaceAll("\r\n", "\n").replaceAll("\n\r", "\n").replaceAll("\r", "\n"))
                .flatMap(m -> Arrays.stream(m.split("\n")))
                .map(m -> m.replaceAll("\n", ""))
                .toArray(String[]::new);

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor(ChatColor.GOLD + "Ruby");
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        meta.setTitle(ChatColor.GOLD + "[" + bookID + "] Ruby " + format.format(new Date()).substring(0, 16));

        ArrayList<StringBuilder> pages = new ArrayList<>();

        for (int i = 0; i < rvals.length / 13 + 1; i++) {
            pages.add(new StringBuilder());
        }

        int pagesptr = 0;
        for (int i = 0; i < rvals.length; i++) {
            if (i != 0 && i % 13 == 0) {
                pagesptr++;
            }

            pages.get(pagesptr).append(rvals[i]).append("\n");
        }

        meta.setPages(
                pages.stream().filter(b -> b.length() != 0).map(StringBuilder::toString).collect(Collectors.toList())
        );
        book.setItemMeta(meta);
        book.setAmount(book.getMaxStackSize());

        return book;
    }
}

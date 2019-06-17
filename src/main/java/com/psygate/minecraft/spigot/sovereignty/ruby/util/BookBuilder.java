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

import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by psygate (https://github.com/psygate) on 09.04.2016.
 */
public class BookBuilder {
    private final static Logger LOG = Ruby.getLogger(BookBuilder.class.getName());
    private final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    private final BookMeta meta = (BookMeta) book.getItemMeta();
    private final StringBuilder pagebuilder = new StringBuilder();
    private final LinkedList<String> pages = new LinkedList<>();
    private boolean title = false;
    private boolean author = false;
    private int lines = 0;
    private int chars = 0;

    public BookBuilder() {
        meta.setTitle("Book");
        meta.setAuthor("Author");
        meta.setPages(new ArrayList<>());
    }

    public void setTitle(ChatColor color, String title) {
        meta.setTitle(color + title.substring(0, Math.min(title.length(), 16 - color.toString().length())));
        if (title.length() + color.toString().length() > 16) {
            meta.setDisplayName(color + title);
        }
        this.title = true;
    }

    public void setAuthor(ChatColor color, String author) {
        meta.setAuthor(color + author.substring(0, Math.min(author.length(), 16 - color.toString().length())));
        this.author = true;
    }

    public boolean hasTitle() {
        return title;
    }

    public boolean hasAuthor() {
        return author;
    }

    public void nextPage() {
        pages.add(pagebuilder.toString());
        lines = 0;
        chars = 0;
        pagebuilder.setLength(0);
    }

    public void addLine(String line) {
        if (lines >= 14 || chars + line.length() >= 256) {
            pages.add(pagebuilder.toString());
            pagebuilder.setLength(0);
            lines = 0;
            chars = 0;
        }

        if (pagebuilder.length() > 0) {
            chars += 2;
            pagebuilder.append("\n");
        }
        pagebuilder.append(line);
        chars += line.length();
        chars += countLineBreaks(line) * 2;

        lines++;
    }

    public ItemStack toBook(List<String> emptyPageDropIn) {
        if (pagebuilder.length() > 0) {
            this.pages.add(pagebuilder.toString());
            pagebuilder.setLength(0);
            lines = 0;
            chars = 0;
        }

        if (this.pages.isEmpty()) {
            meta.setPages(emptyPageDropIn);
        } else {
            meta.setPages(this.pages);
        }
        book.setItemMeta(meta);
        return book;
    }

    public int pageCount() {
        return pages.size();
    }

    private int countLineBreaks(String line) {
        int lb = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\n') {
                lb++;
            }
        }

        return lb;
    }
}

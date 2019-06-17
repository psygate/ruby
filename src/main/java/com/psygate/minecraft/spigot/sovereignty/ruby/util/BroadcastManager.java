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

import com.google.common.cache.*;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Member;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyGroupMutesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyMutesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.SnitchRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordFilterUtil.isBroadcastAble;

/**
 * Created by psygate (https://github.com/psygate) on 25.03.2016.
 */
public class BroadcastManager {
    private static Logger LOG = Ruby.getLogger(BroadcastManager.class.getName());
    private static BroadcastManager ourInstance = new BroadcastManager();

    public static BroadcastManager getInstance() {
        return ourInstance;
    }

    private LoadingCache<UUID, Set<Long>> snitchMuteCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(100)
            .maximumSize(1000)
            .removalListener(new RemovalListener<UUID, Set<Long>>() {
                @Override
                public void onRemoval(RemovalNotification<UUID, Set<Long>> removalNotification) {
                    Ruby.DBI().asyncSubmit((conf) -> {
                        DSLContext ctx = DSL.using(conf);
                        ctx.deleteFrom(Tables.RUBY_MUTES)
                                .where(Tables.RUBY_MUTES.PUUID.eq(removalNotification.getKey()))
                                .execute();
                        for (Long id : removalNotification.getValue()) {
                            ctx.insertInto(Tables.RUBY_MUTES)
                                    .set(new RubyMutesRecord(removalNotification.getKey(), id))
                                    .onDuplicateKeyIgnore()
                                    .execute();
                        }
                    });
                }
            })
            .build(new CacheLoader<UUID, Set<Long>>() {
                @Override
                public Set<Long> load(UUID uuid) throws Exception {
                    return new HashSet<>(
                            Ruby.DBI().submit((conf) -> {
                                return DSL.using(conf).selectDistinct(Tables.RUBY_MUTES.SNITCH_ID)
                                        .from(Tables.RUBY_MUTES)
                                        .where(Tables.RUBY_MUTES.PUUID.eq(uuid))
                                        .fetch(Record1::value1);
                            })
                    );
                }
            });
    private LoadingCache<UUID, Set<Long>> groupMuteCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(100)
            .maximumSize(1000)
            .removalListener(new RemovalListener<UUID, Set<Long>>() {
                @Override
                public void onRemoval(RemovalNotification<UUID, Set<Long>> removalNotification) {
                    Ruby.DBI().asyncSubmit((conf) -> {
                        DSLContext ctx = DSL.using(conf);
                        ctx.deleteFrom(Tables.RUBY_GROUP_MUTES)
                                .where(Tables.RUBY_GROUP_MUTES.PUUID.eq(removalNotification.getKey()))
                                .execute();
                        for (Long id : removalNotification.getValue()) {
                            ctx.insertInto(Tables.RUBY_GROUP_MUTES)
                                    .set(new RubyGroupMutesRecord(removalNotification.getKey(), id))
                                    .onDuplicateKeyIgnore()
                                    .execute();
                        }
                    });
                }
            })
            .build(new CacheLoader<UUID, Set<Long>>() {
                @Override
                public Set<Long> load(UUID uuid) throws Exception {
                    return new HashSet<>(
                            Ruby.DBI().submit((conf) -> {
                                return DSL.using(conf).selectDistinct(Tables.RUBY_MUTES.SNITCH_ID)
                                        .from(Tables.RUBY_MUTES)
                                        .where(Tables.RUBY_MUTES.PUUID.eq(uuid))
                                        .fetch(Record1::value1);
                            })
                    );
                }
            });

    private LoadingCache<Long, Map<LogType, Long>> rateLimitCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(100)
            .maximumSize(1000)
            .build(new CacheLoader<Long, Map<LogType, Long>>() {
                @Override
                public Map<LogType, Long> load(Long aLong) throws Exception {
                    HashMap<LogType, Long> map = new HashMap<>();
                    for (LogType type : LogType.values()) {
                        map.put(type, 0L);
                    }

                    return map;
                }
            });

    private BroadcastManager() {
    }

    /**
     * Returns true if the snitch is now muted, else false.
     *
     * @param player
     * @param id
     * @return
     */
    public boolean toggleSnitchMute(UUID player, long id) {
        Set<Long> values = snitchMuteCache.getUnchecked(player);
        if (values.contains(id)) {
            values.remove(id);
            return false;
        } else {
            values.add(id);
            return true;
        }
    }

    private void broadcastMsg(Long snitchid, LogType type, Long groupid, String... msg) {
        long now = System.currentTimeMillis();
        Map<LogType, Long> map = rateLimitCache.getUnchecked(snitchid);
        if (now - map.get(type) > TimeUnit.SECONDS.toMillis(3)) {
            map.put(type, now);
            GroupManager.getInstance().getGroup(groupid).ifPresent(group -> {
                for (Member mem : group.getMembers().values()) {
                    if (
                            !GroupManager.getInstance().getGroupMutes(mem.getUUID()).contains(group.getGroupID())
                                    && !snitchMuteCache.getUnchecked(mem.getUUID()).contains(snitchid)
                                    && !groupMuteCache.getUnchecked(mem.getUUID()).contains(groupid)
                            ) {
                        Optional.ofNullable(Bukkit.getPlayer(mem.getUUID())).ifPresent(p -> p.sendMessage(msg));
                    }
                }
            });
        }
    }

    public void broadcast(SnitchRecord ev, Player player, Snitch snitch) {
        if (isBroadcastAble(player, snitch)) {
            broadcastMsg(snitch.getSnitchId(), ev.getEventType(), snitch.getGroupId(), sanitize(RecordRenderer.getInstance().renderChatMessage(ev)));
        }
    }

    public static String[] sanitize(String split) {
        return sanitize(new String[]{split});
    }

    public static String[] sanitize(String[] split) {
        return Arrays.stream(split)
                .flatMap(m -> Arrays.stream(m.replaceAll("\r\n", "\n").replaceAll("\n\r", "\n").replaceAll("\r", "\n").split("\n")))
                .map(m -> m.replaceAll("\n", ""))
                .toArray(String[]::new);
    }

    public boolean toggleGroupMute(UUID player, Long id) {
        Set<Long> values = groupMuteCache.getUnchecked(player);
        if (values.contains(id)) {
            values.remove(id);
            return false;
        } else {
            values.add(id);
            return true;
        }
    }
}
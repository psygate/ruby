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
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.WorkerPool;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.player.PlayerManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.*;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Tables.*;

/**
 * Created by psygate (https://github.com/psygate) on 24.03.2016.
 */
public class RecordPersistenceManager {
    private final static Logger LOG = Ruby.getLogger(RecordPersistenceManager.class.getName());
    private static RecordPersistenceManager instance;

    public static RecordPersistenceManager getInstance() {
        if (instance == null) {
            instance = new RecordPersistenceManager();
        }
        return instance;
    }

    public void loadRecords(Snitch snitch, int recordstart, int recordend, Consumer<List<SnitchRecord>> call) {
        Ruby.DBI().asyncSubmit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            List<RubyRecordsRecord> baserecs = ctx.selectFrom(RUBY_RECORDS)
                    .where(RUBY_RECORDS.SNITCH_ID.eq(snitch.getSnitchId()))
                    .orderBy(RUBY_RECORDS.TIME_OF_EVENT.desc())
                    .limit(recordstart, recordend)
                    .fetch();

            List<Long> ids = baserecs.stream().map(RubyRecordsRecord::getRecordId).collect(Collectors.toList());

            List<RubyLocationRecord> locrecs = ctx.selectFrom(RUBY_LOCATION)
                    .where(RUBY_LOCATION.RECORD_ID.in(ids))
                    .orderBy(RUBY_LOCATION.ID.asc())
                    .fetch();

            List<RubyPlayerRecord> playerrecs = ctx.selectFrom(RUBY_PLAYER)
                    .where(RUBY_PLAYER.RECORD_ID.in(ids))
                    .orderBy(RUBY_PLAYER.ID.asc())
                    .fetch();

            List<RubyEntityRecord> entityrecs = ctx.selectFrom(RUBY_ENTITY)
                    .where(RUBY_ENTITY.RECORD_ID.in(ids))
                    .orderBy(RUBY_ENTITY.ID.asc())
                    .fetch();

            List<RubyMaterialRecord> matrecs = ctx.selectFrom(RUBY_MATERIAL)
                    .where(RUBY_MATERIAL.RECORD_ID.in(ids))
                    .orderBy(RUBY_MATERIAL.ID.asc())
                    .fetch();

            WorkerPool.submit(() -> call.accept(map(snitch, baserecs, locrecs, entityrecs, playerrecs, matrecs)));
        });
    }

    public void persist(SnitchRecord rec) {
        if (rec.getSnitchData().getType() == SnitchType.SNITCH) {
            Ruby.DBI().asyncSubmit((conf) -> {
                DSLContext ctx = DSL.using(conf);
                Long id = ctx.insertInto(RUBY_RECORDS)
                        .set(new RubyRecordsRecord(
                                rec.getSnitchData().getId(),
                                null,
                                new Timestamp(rec.getTimeOfEvent()),
                                rec.getEventType()
                        )).returning(RUBY_RECORDS.RECORD_ID)
                        .fetchOne().getRecordId();

                for (Map.Entry<RecordLocationType, RubyLocation> en : rec.getLocations().entrySet()) {
                    ctx.insertInto(RUBY_LOCATION).set(new RubyLocationRecord(
                            null,
                            en.getValue().getX(),
                            en.getValue().getY(),
                            en.getValue().getZ(),
                            en.getValue().getWorld(),
                            id,
                            en.getKey()
                    ))
                            .execute();
                }

                for (Map.Entry<RecordPlayerType, Pair<UUID, String>> en : rec.getPlayers().entrySet()) {
                    ctx.insertInto(RUBY_PLAYER).set(new RubyPlayerRecord(
                            null,
                            id,
                            en.getValue().getKey(),
                            en.getKey()
                    )).execute();
                }

                for (Map.Entry<RecordEntityType, Pair<EntityType, String>> en : rec.getEntities().entrySet()) {
                    ctx.insertInto(RUBY_ENTITY).set(new RubyEntityRecord(
                            null,
                            id,
                            en.getValue().getKey(),
                            en.getValue().getValue(),
                            en.getKey()
                    )).execute();
                }

                for (Material m : rec.getMaterials()) {
                    ctx.insertInto(RUBY_MATERIAL).set(new RubyMaterialRecord(
                            null,
                            id,
                            m
                    ))
                            .execute();
                }
            });
        }
    }

    private List<SnitchRecord> map(Snitch snitch, List<RubyRecordsRecord> baserecs, List<RubyLocationRecord> locrecs, List<RubyEntityRecord> entityrecs, List<RubyPlayerRecord> playerrecs, List<RubyMaterialRecord> matrecs) {
        Map<Long, SnitchRecord> records = new HashMap<>();

        for (RubyRecordsRecord rec : baserecs) {
            records.put(rec.getRecordId(), new SnitchRecord(snitch.getX(), snitch.getY(), snitch.getZ(), snitch.getWorldUuid(), snitch.getSnitchId(), snitch.getSnitchName(), rec.getTimeOfEvent().getTime(), snitch.getSnitchType(), rec.getLogType()));
        }

        for (RubyLocationRecord rec : locrecs) {
            records.get(rec.getRecordId()).add(rec.getRecordLocationType(), new RubyLocation(rec.getX(), rec.getY(), rec.getZ(), rec.getWorldUuid()));
        }

        for (RubyEntityRecord rec : entityrecs) {
            records.get(rec.getRecordId()).add(rec.getRecordEntityType(), rec.getEntityType(), rec.getEntityName());
        }

        for (RubyPlayerRecord rec : playerrecs) {
            records.get(rec.getRecordId()).add(rec.getRecordPlayerType(), rec.getPuuid(), PlayerManager.getInstance().toName(rec.getPuuid()));
        }

        for (RubyMaterialRecord rec : matrecs) {
            records.get(rec.getRecordId()).add(rec.getMaterialType());
        }

        ArrayList<SnitchRecord> sorted = new ArrayList<>(records.values());
//        sorted.sort((a, b) -> Long.compare(a.getTimeOfEvent(), b.getTimeOfEvent()));
        Collections.sort(sorted, (a, b) -> Long.compare(a.getTimeOfEvent(), b.getTimeOfEvent()));
//        Collections.sort(sorted, );

        return sorted;
    }

    public void clearRecords(Snitch candidate) {
        Ruby.DBI().asyncSubmit((conf) -> {
            DSLContext ctx = DSL.using(conf);

            clearRecords(candidate.getSnitchId(), ctx);
        });
    }

    public void clearRecords(Long snitchId, DSLContext ctx) {

        try (Cursor<RubyRecordsRecord> cur = ctx.selectFrom(RUBY_RECORDS).where(RUBY_RECORDS.SNITCH_ID.eq(snitchId)).fetchLazy()) {
            while (cur.hasNext()) {
                long id = cur.fetchOne().getRecordId();
                ctx.deleteFrom(RUBY_PLAYER).where(RUBY_PLAYER.RECORD_ID.eq(id)).execute();
                ctx.deleteFrom(RUBY_ENTITY).where(RUBY_ENTITY.RECORD_ID.eq(id)).execute();
                ctx.deleteFrom(RUBY_LOCATION).where(RUBY_LOCATION.RECORD_ID.eq(id)).execute();
            }
        }

        ctx.deleteFrom(RUBY_RECORDS).where(RUBY_RECORDS.SNITCH_ID.eq(snitchId)).execute();
    }
}

/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyEntity;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyGroupMutes;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyLocation;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyMaterial;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyMutes;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyPlayer;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyRecords;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubySnitches;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyEntityRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyGroupMutesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyLocationRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyMaterialRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyMutesRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyPlayerRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyRecordsRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubySnitchesRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>nucleus</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<RubyEntityRecord, Long> IDENTITY_RUBY_ENTITY = Identities0.IDENTITY_RUBY_ENTITY;
	public static final Identity<RubyLocationRecord, Long> IDENTITY_RUBY_LOCATION = Identities0.IDENTITY_RUBY_LOCATION;
	public static final Identity<RubyMaterialRecord, Long> IDENTITY_RUBY_MATERIAL = Identities0.IDENTITY_RUBY_MATERIAL;
	public static final Identity<RubyPlayerRecord, Long> IDENTITY_RUBY_PLAYER = Identities0.IDENTITY_RUBY_PLAYER;
	public static final Identity<RubyRecordsRecord, Long> IDENTITY_RUBY_RECORDS = Identities0.IDENTITY_RUBY_RECORDS;
	public static final Identity<RubySnitchesRecord, Long> IDENTITY_RUBY_SNITCHES = Identities0.IDENTITY_RUBY_SNITCHES;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<RubyEntityRecord> KEY_RUBY_ENTITY_PRIMARY = UniqueKeys0.KEY_RUBY_ENTITY_PRIMARY;
	public static final UniqueKey<RubyGroupMutesRecord> KEY_RUBY_GROUP_MUTES_PRIMARY = UniqueKeys0.KEY_RUBY_GROUP_MUTES_PRIMARY;
	public static final UniqueKey<RubyLocationRecord> KEY_RUBY_LOCATION_PRIMARY = UniqueKeys0.KEY_RUBY_LOCATION_PRIMARY;
	public static final UniqueKey<RubyMaterialRecord> KEY_RUBY_MATERIAL_PRIMARY = UniqueKeys0.KEY_RUBY_MATERIAL_PRIMARY;
	public static final UniqueKey<RubyMutesRecord> KEY_RUBY_MUTES_PRIMARY = UniqueKeys0.KEY_RUBY_MUTES_PRIMARY;
	public static final UniqueKey<RubyPlayerRecord> KEY_RUBY_PLAYER_PRIMARY = UniqueKeys0.KEY_RUBY_PLAYER_PRIMARY;
	public static final UniqueKey<RubyRecordsRecord> KEY_RUBY_RECORDS_PRIMARY = UniqueKeys0.KEY_RUBY_RECORDS_PRIMARY;
	public static final UniqueKey<RubySnitchesRecord> KEY_RUBY_SNITCHES_PRIMARY = UniqueKeys0.KEY_RUBY_SNITCHES_PRIMARY;
	public static final UniqueKey<RubySnitchesRecord> KEY_RUBY_SNITCHES_SNITCH_ID = UniqueKeys0.KEY_RUBY_SNITCHES_SNITCH_ID;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<RubyEntityRecord, RubyRecordsRecord> RUBY_ENTITY_IBFK_1 = ForeignKeys0.RUBY_ENTITY_IBFK_1;
	public static final ForeignKey<RubyLocationRecord, RubyRecordsRecord> RUBY_LOCATION_IBFK_1 = ForeignKeys0.RUBY_LOCATION_IBFK_1;
	public static final ForeignKey<RubyMaterialRecord, RubyRecordsRecord> RUBY_MATERIAL_IBFK_1 = ForeignKeys0.RUBY_MATERIAL_IBFK_1;
	public static final ForeignKey<RubyMutesRecord, RubySnitchesRecord> RUBY_MUTES_IBFK_1 = ForeignKeys0.RUBY_MUTES_IBFK_1;
	public static final ForeignKey<RubyPlayerRecord, RubyRecordsRecord> RUBY_PLAYER_IBFK_1 = ForeignKeys0.RUBY_PLAYER_IBFK_1;
	public static final ForeignKey<RubyRecordsRecord, RubySnitchesRecord> RUBY_RECORDS_IBFK_1 = ForeignKeys0.RUBY_RECORDS_IBFK_1;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<RubyEntityRecord, Long> IDENTITY_RUBY_ENTITY = createIdentity(RubyEntity.RUBY_ENTITY, RubyEntity.RUBY_ENTITY.ID);
		public static Identity<RubyLocationRecord, Long> IDENTITY_RUBY_LOCATION = createIdentity(RubyLocation.RUBY_LOCATION, RubyLocation.RUBY_LOCATION.ID);
		public static Identity<RubyMaterialRecord, Long> IDENTITY_RUBY_MATERIAL = createIdentity(RubyMaterial.RUBY_MATERIAL, RubyMaterial.RUBY_MATERIAL.ID);
		public static Identity<RubyPlayerRecord, Long> IDENTITY_RUBY_PLAYER = createIdentity(RubyPlayer.RUBY_PLAYER, RubyPlayer.RUBY_PLAYER.ID);
		public static Identity<RubyRecordsRecord, Long> IDENTITY_RUBY_RECORDS = createIdentity(RubyRecords.RUBY_RECORDS, RubyRecords.RUBY_RECORDS.RECORD_ID);
		public static Identity<RubySnitchesRecord, Long> IDENTITY_RUBY_SNITCHES = createIdentity(RubySnitches.RUBY_SNITCHES, RubySnitches.RUBY_SNITCHES.SNITCH_ID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<RubyEntityRecord> KEY_RUBY_ENTITY_PRIMARY = createUniqueKey(RubyEntity.RUBY_ENTITY, RubyEntity.RUBY_ENTITY.ID);
		public static final UniqueKey<RubyGroupMutesRecord> KEY_RUBY_GROUP_MUTES_PRIMARY = createUniqueKey(RubyGroupMutes.RUBY_GROUP_MUTES, RubyGroupMutes.RUBY_GROUP_MUTES.PUUID);
		public static final UniqueKey<RubyLocationRecord> KEY_RUBY_LOCATION_PRIMARY = createUniqueKey(RubyLocation.RUBY_LOCATION, RubyLocation.RUBY_LOCATION.ID);
		public static final UniqueKey<RubyMaterialRecord> KEY_RUBY_MATERIAL_PRIMARY = createUniqueKey(RubyMaterial.RUBY_MATERIAL, RubyMaterial.RUBY_MATERIAL.ID);
		public static final UniqueKey<RubyMutesRecord> KEY_RUBY_MUTES_PRIMARY = createUniqueKey(RubyMutes.RUBY_MUTES, RubyMutes.RUBY_MUTES.PUUID);
		public static final UniqueKey<RubyPlayerRecord> KEY_RUBY_PLAYER_PRIMARY = createUniqueKey(RubyPlayer.RUBY_PLAYER, RubyPlayer.RUBY_PLAYER.ID);
		public static final UniqueKey<RubyRecordsRecord> KEY_RUBY_RECORDS_PRIMARY = createUniqueKey(RubyRecords.RUBY_RECORDS, RubyRecords.RUBY_RECORDS.RECORD_ID);
		public static final UniqueKey<RubySnitchesRecord> KEY_RUBY_SNITCHES_PRIMARY = createUniqueKey(RubySnitches.RUBY_SNITCHES, RubySnitches.RUBY_SNITCHES.X, RubySnitches.RUBY_SNITCHES.Y, RubySnitches.RUBY_SNITCHES.Z, RubySnitches.RUBY_SNITCHES.WORLD_UUID);
		public static final UniqueKey<RubySnitchesRecord> KEY_RUBY_SNITCHES_SNITCH_ID = createUniqueKey(RubySnitches.RUBY_SNITCHES, RubySnitches.RUBY_SNITCHES.SNITCH_ID);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<RubyEntityRecord, RubyRecordsRecord> RUBY_ENTITY_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_RECORDS_PRIMARY, RubyEntity.RUBY_ENTITY, RubyEntity.RUBY_ENTITY.RECORD_ID);
		public static final ForeignKey<RubyLocationRecord, RubyRecordsRecord> RUBY_LOCATION_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_RECORDS_PRIMARY, RubyLocation.RUBY_LOCATION, RubyLocation.RUBY_LOCATION.RECORD_ID);
		public static final ForeignKey<RubyMaterialRecord, RubyRecordsRecord> RUBY_MATERIAL_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_RECORDS_PRIMARY, RubyMaterial.RUBY_MATERIAL, RubyMaterial.RUBY_MATERIAL.RECORD_ID);
		public static final ForeignKey<RubyMutesRecord, RubySnitchesRecord> RUBY_MUTES_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_SNITCHES_SNITCH_ID, RubyMutes.RUBY_MUTES, RubyMutes.RUBY_MUTES.SNITCH_ID);
		public static final ForeignKey<RubyPlayerRecord, RubyRecordsRecord> RUBY_PLAYER_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_RECORDS_PRIMARY, RubyPlayer.RUBY_PLAYER, RubyPlayer.RUBY_PLAYER.RECORD_ID);
		public static final ForeignKey<RubyRecordsRecord, RubySnitchesRecord> RUBY_RECORDS_IBFK_1 = createForeignKey(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys.KEY_RUBY_SNITCHES_SNITCH_ID, RubyRecords.RUBY_RECORDS, RubyRecords.RUBY_RECORDS.SNITCH_ID);
	}
}
/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables;


import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.UUIDByteConverter;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Keys;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyLocationRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordLocationType;
import com.psygate.minecraft.spigot.sovereignty.ruby.sql.util.RecordLocationTypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RubyLocation extends TableImpl<RubyLocationRecord> {

	private static final long serialVersionUID = -1925816948;

	/**
	 * The reference instance of <code>nucleus.ruby_location</code>
	 */
	public static final RubyLocation RUBY_LOCATION = new RubyLocation();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<RubyLocationRecord> getRecordType() {
		return RubyLocationRecord.class;
	}

	/**
	 * The column <code>nucleus.ruby_location.id</code>.
	 */
	public final TableField<RubyLocationRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>nucleus.ruby_location.x</code>.
	 */
	public final TableField<RubyLocationRecord, Integer> X = createField("x", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.ruby_location.y</code>.
	 */
	public final TableField<RubyLocationRecord, Integer> Y = createField("y", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.ruby_location.z</code>.
	 */
	public final TableField<RubyLocationRecord, Integer> Z = createField("z", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.ruby_location.world_uuid</code>.
	 */
	public final TableField<RubyLocationRecord, UUID> WORLD_UUID = createField("world_uuid", org.jooq.impl.SQLDataType.BINARY.length(16).nullable(false), this, "", new UUIDByteConverter());

	/**
	 * The column <code>nucleus.ruby_location.record_id</code>.
	 */
	public final TableField<RubyLocationRecord, Long> RECORD_ID = createField("record_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>nucleus.ruby_location.record_location_type</code>.
	 */
	public final TableField<RubyLocationRecord, RecordLocationType> RECORD_LOCATION_TYPE = createField("record_location_type", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "", new RecordLocationTypeConverter());

	/**
	 * Create a <code>nucleus.ruby_location</code> table reference
	 */
	public RubyLocation() {
		this("ruby_location", null);
	}

	/**
	 * Create an aliased <code>nucleus.ruby_location</code> table reference
	 */
	public RubyLocation(String alias) {
		this(alias, RUBY_LOCATION);
	}

	private RubyLocation(String alias, Table<RubyLocationRecord> aliased) {
		this(alias, aliased, null);
	}

	private RubyLocation(String alias, Table<RubyLocationRecord> aliased, Field<?>[] parameters) {
		super(alias, Nucleus.NUCLEUS, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<RubyLocationRecord, Long> getIdentity() {
		return Keys.IDENTITY_RUBY_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<RubyLocationRecord> getPrimaryKey() {
		return Keys.KEY_RUBY_LOCATION_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<RubyLocationRecord>> getKeys() {
		return Arrays.<UniqueKey<RubyLocationRecord>>asList(Keys.KEY_RUBY_LOCATION_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<RubyLocationRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<RubyLocationRecord, ?>>asList(Keys.RUBY_LOCATION_IBFK_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyLocation as(String alias) {
		return new RubyLocation(alias, this);
	}

	/**
	 * Rename this table
	 */
	public RubyLocation rename(String name) {
		return new RubyLocation(name, null);
	}
}

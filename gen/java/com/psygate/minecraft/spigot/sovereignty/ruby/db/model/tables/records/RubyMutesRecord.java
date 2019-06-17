/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyMutes;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyMutes;

import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


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
public class RubyMutesRecord extends UpdatableRecordImpl<RubyMutesRecord> implements Record2<UUID, Long>, IRubyMutes {

	private static final long serialVersionUID = 581739928;

	/**
	 * Setter for <code>nucleus.ruby_mutes.puuid</code>.
	 */
	public void setPuuid(UUID value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_mutes.puuid</code>.
	 */
	@Override
	public UUID getPuuid() {
		return (UUID) getValue(0);
	}

	/**
	 * Setter for <code>nucleus.ruby_mutes.snitch_id</code>.
	 */
	public void setSnitchId(Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_mutes.snitch_id</code>.
	 */
	@Override
	public Long getSnitchId() {
		return (Long) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<UUID> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<UUID, Long> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<UUID, Long> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<UUID> field1() {
		return RubyMutes.RUBY_MUTES.PUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return RubyMutes.RUBY_MUTES.SNITCH_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID value1() {
		return getPuuid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value2() {
		return getSnitchId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMutesRecord value1(UUID value) {
		setPuuid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMutesRecord value2(Long value) {
		setSnitchId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMutesRecord values(UUID value1, Long value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached RubyMutesRecord
	 */
	public RubyMutesRecord() {
		super(RubyMutes.RUBY_MUTES);
	}

	/**
	 * Create a detached, initialised RubyMutesRecord
	 */
	public RubyMutesRecord(UUID puuid, Long snitchId) {
		super(RubyMutes.RUBY_MUTES);

		setValue(0, puuid);
		setValue(1, snitchId);
	}
}
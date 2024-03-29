/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyPlayer;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyPlayer;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordPlayerType;

import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class RubyPlayerRecord extends UpdatableRecordImpl<RubyPlayerRecord> implements Record4<Long, Long, UUID, RecordPlayerType>, IRubyPlayer {

	private static final long serialVersionUID = 2114871470;

	/**
	 * Setter for <code>nucleus.ruby_player.id</code>.
	 */
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_player.id</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>nucleus.ruby_player.record_id</code>.
	 */
	public void setRecordId(Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_player.record_id</code>.
	 */
	@Override
	public Long getRecordId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>nucleus.ruby_player.puuid</code>.
	 */
	public void setPuuid(UUID value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_player.puuid</code>.
	 */
	@Override
	public UUID getPuuid() {
		return (UUID) getValue(2);
	}

	/**
	 * Setter for <code>nucleus.ruby_player.record_player_type</code>.
	 */
	public void setRecordPlayerType(RecordPlayerType value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_player.record_player_type</code>.
	 */
	@Override
	public RecordPlayerType getRecordPlayerType() {
		return (RecordPlayerType) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Long, Long, UUID, RecordPlayerType> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Long, Long, UUID, RecordPlayerType> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return RubyPlayer.RUBY_PLAYER.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return RubyPlayer.RUBY_PLAYER.RECORD_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<UUID> field3() {
		return RubyPlayer.RUBY_PLAYER.PUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<RecordPlayerType> field4() {
		return RubyPlayer.RUBY_PLAYER.RECORD_PLAYER_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value2() {
		return getRecordId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID value3() {
		return getPuuid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordPlayerType value4() {
		return getRecordPlayerType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyPlayerRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyPlayerRecord value2(Long value) {
		setRecordId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyPlayerRecord value3(UUID value) {
		setPuuid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyPlayerRecord value4(RecordPlayerType value) {
		setRecordPlayerType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyPlayerRecord values(Long value1, Long value2, UUID value3, RecordPlayerType value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached RubyPlayerRecord
	 */
	public RubyPlayerRecord() {
		super(RubyPlayer.RUBY_PLAYER);
	}

	/**
	 * Create a detached, initialised RubyPlayerRecord
	 */
	public RubyPlayerRecord(Long id, Long recordId, UUID puuid, RecordPlayerType recordPlayerType) {
		super(RubyPlayer.RUBY_PLAYER);

		setValue(0, id);
		setValue(1, recordId);
		setValue(2, puuid);
		setValue(3, recordPlayerType);
	}
}

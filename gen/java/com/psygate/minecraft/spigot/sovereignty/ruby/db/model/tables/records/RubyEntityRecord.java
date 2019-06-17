/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyEntity;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyEntity;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordEntityType;

import javax.annotation.Generated;

import org.bukkit.entity.EntityType;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class RubyEntityRecord extends UpdatableRecordImpl<RubyEntityRecord> implements Record5<Long, Long, EntityType, String, RecordEntityType>, IRubyEntity {

	private static final long serialVersionUID = 1320526134;

	/**
	 * Setter for <code>nucleus.ruby_entity.id</code>.
	 */
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_entity.id</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>nucleus.ruby_entity.record_id</code>.
	 */
	public void setRecordId(Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_entity.record_id</code>.
	 */
	@Override
	public Long getRecordId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>nucleus.ruby_entity.entity_type</code>.
	 */
	public void setEntityType(EntityType value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_entity.entity_type</code>.
	 */
	@Override
	public EntityType getEntityType() {
		return (EntityType) getValue(2);
	}

	/**
	 * Setter for <code>nucleus.ruby_entity.entity_name</code>.
	 */
	public void setEntityName(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_entity.entity_name</code>.
	 */
	@Override
	public String getEntityName() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>nucleus.ruby_entity.record_entity_type</code>.
	 */
	public void setRecordEntityType(RecordEntityType value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_entity.record_entity_type</code>.
	 */
	@Override
	public RecordEntityType getRecordEntityType() {
		return (RecordEntityType) getValue(4);
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
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Long, Long, EntityType, String, RecordEntityType> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Long, Long, EntityType, String, RecordEntityType> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return RubyEntity.RUBY_ENTITY.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return RubyEntity.RUBY_ENTITY.RECORD_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<EntityType> field3() {
		return RubyEntity.RUBY_ENTITY.ENTITY_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return RubyEntity.RUBY_ENTITY.ENTITY_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<RecordEntityType> field5() {
		return RubyEntity.RUBY_ENTITY.RECORD_ENTITY_TYPE;
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
	public EntityType value3() {
		return getEntityType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getEntityName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RecordEntityType value5() {
		return getRecordEntityType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord value2(Long value) {
		setRecordId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord value3(EntityType value) {
		setEntityType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord value4(String value) {
		setEntityName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord value5(RecordEntityType value) {
		setRecordEntityType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyEntityRecord values(Long value1, Long value2, EntityType value3, String value4, RecordEntityType value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached RubyEntityRecord
	 */
	public RubyEntityRecord() {
		super(RubyEntity.RUBY_ENTITY);
	}

	/**
	 * Create a detached, initialised RubyEntityRecord
	 */
	public RubyEntityRecord(Long id, Long recordId, EntityType entityType, String entityName, RecordEntityType recordEntityType) {
		super(RubyEntity.RUBY_ENTITY);

		setValue(0, id);
		setValue(1, recordId);
		setValue(2, entityType);
		setValue(3, entityName);
		setValue(4, recordEntityType);
	}
}
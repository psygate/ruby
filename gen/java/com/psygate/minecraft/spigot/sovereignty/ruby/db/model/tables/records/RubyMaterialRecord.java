/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyMaterial;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyMaterial;

import javax.annotation.Generated;

import org.bukkit.Material;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
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
public class RubyMaterialRecord extends UpdatableRecordImpl<RubyMaterialRecord> implements Record3<Long, Long, Material>, IRubyMaterial {

	private static final long serialVersionUID = -300029247;

	/**
	 * Setter for <code>nucleus.ruby_material.id</code>.
	 */
	public void setId(Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_material.id</code>.
	 */
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>nucleus.ruby_material.record_id</code>.
	 */
	public void setRecordId(Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_material.record_id</code>.
	 */
	@Override
	public Long getRecordId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>nucleus.ruby_material.material_type</code>.
	 */
	public void setMaterialType(Material value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>nucleus.ruby_material.material_type</code>.
	 */
	@Override
	public Material getMaterialType() {
		return (Material) getValue(2);
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
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, Material> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, Material> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return RubyMaterial.RUBY_MATERIAL.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return RubyMaterial.RUBY_MATERIAL.RECORD_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Material> field3() {
		return RubyMaterial.RUBY_MATERIAL.MATERIAL_TYPE;
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
	public Material value3() {
		return getMaterialType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMaterialRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMaterialRecord value2(Long value) {
		setRecordId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMaterialRecord value3(Material value) {
		setMaterialType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RubyMaterialRecord values(Long value1, Long value2, Material value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached RubyMaterialRecord
	 */
	public RubyMaterialRecord() {
		super(RubyMaterial.RUBY_MATERIAL);
	}

	/**
	 * Create a detached, initialised RubyMaterialRecord
	 */
	public RubyMaterialRecord(Long id, Long recordId, Material materialType) {
		super(RubyMaterial.RUBY_MATERIAL);

		setValue(0, id);
		setValue(1, recordId);
		setValue(2, materialType);
	}
}
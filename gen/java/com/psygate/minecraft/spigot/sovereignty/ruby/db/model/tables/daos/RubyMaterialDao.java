/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.daos;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyMaterial;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyMaterialRecord;

import java.util.List;

import javax.annotation.Generated;

import org.bukkit.Material;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


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
public class RubyMaterialDao extends DAOImpl<RubyMaterialRecord, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial, Long> {

	/**
	 * Create a new RubyMaterialDao without any configuration
	 */
	public RubyMaterialDao() {
		super(RubyMaterial.RUBY_MATERIAL, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial.class);
	}

	/**
	 * Create a new RubyMaterialDao with an attached configuration
	 */
	public RubyMaterialDao(Configuration configuration) {
		super(RubyMaterial.RUBY_MATERIAL, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial> fetchById(Long... values) {
		return fetch(RubyMaterial.RUBY_MATERIAL.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial fetchOneById(Long value) {
		return fetchOne(RubyMaterial.RUBY_MATERIAL.ID, value);
	}

	/**
	 * Fetch records that have <code>record_id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial> fetchByRecordId(Long... values) {
		return fetch(RubyMaterial.RUBY_MATERIAL.RECORD_ID, values);
	}

	/**
	 * Fetch records that have <code>material_type IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyMaterial> fetchByMaterialType(Material... values) {
		return fetch(RubyMaterial.RUBY_MATERIAL.MATERIAL_TYPE, values);
	}
}

/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;

import org.bukkit.Material;


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
public interface IRubyMaterial extends Serializable {

	/**
	 * Getter for <code>nucleus.ruby_material.id</code>.
	 */
	public Long getId();

	/**
	 * Getter for <code>nucleus.ruby_material.record_id</code>.
	 */
	public Long getRecordId();

	/**
	 * Getter for <code>nucleus.ruby_material.material_type</code>.
	 */
	public Material getMaterialType();
}
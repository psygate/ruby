/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces;


import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordEntityType;

import java.io.Serializable;

import javax.annotation.Generated;

import org.bukkit.entity.EntityType;


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
public interface IRubyEntity extends Serializable {

	/**
	 * Getter for <code>nucleus.ruby_entity.id</code>.
	 */
	public Long getId();

	/**
	 * Getter for <code>nucleus.ruby_entity.record_id</code>.
	 */
	public Long getRecordId();

	/**
	 * Getter for <code>nucleus.ruby_entity.entity_type</code>.
	 */
	public EntityType getEntityType();

	/**
	 * Getter for <code>nucleus.ruby_entity.entity_name</code>.
	 */
	public String getEntityName();

	/**
	 * Getter for <code>nucleus.ruby_entity.record_entity_type</code>.
	 */
	public RecordEntityType getRecordEntityType();
}

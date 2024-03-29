/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces;


import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;


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
public interface IRubySnitches extends Serializable {

	/**
	 * Getter for <code>nucleus.ruby_snitches.x</code>.
	 */
	public Integer getX();

	/**
	 * Getter for <code>nucleus.ruby_snitches.y</code>.
	 */
	public Integer getY();

	/**
	 * Getter for <code>nucleus.ruby_snitches.z</code>.
	 */
	public Integer getZ();

	/**
	 * Getter for <code>nucleus.ruby_snitches.world_uuid</code>.
	 */
	public UUID getWorldUuid();

	/**
	 * Getter for <code>nucleus.ruby_snitches.snitch_id</code>.
	 */
	public Long getSnitchId();

	/**
	 * Getter for <code>nucleus.ruby_snitches.creator</code>.
	 */
	public UUID getCreator();

	/**
	 * Getter for <code>nucleus.ruby_snitches.creation</code>.
	 */
	public Timestamp getCreation();

	/**
	 * Getter for <code>nucleus.ruby_snitches.group_id</code>.
	 */
	public Long getGroupId();

	/**
	 * Getter for <code>nucleus.ruby_snitches.snitch_name</code>.
	 */
	public String getSnitchName();

	/**
	 * Getter for <code>nucleus.ruby_snitches.snitch_type</code>.
	 */
	public SnitchType getSnitchType();
}

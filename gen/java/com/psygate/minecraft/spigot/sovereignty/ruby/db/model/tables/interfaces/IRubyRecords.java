/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces;


import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;

import java.io.Serializable;
import java.sql.Timestamp;

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
public interface IRubyRecords extends Serializable {

	/**
	 * Getter for <code>nucleus.ruby_records.snitch_id</code>.
	 */
	public Long getSnitchId();

	/**
	 * Getter for <code>nucleus.ruby_records.record_id</code>.
	 */
	public Long getRecordId();

	/**
	 * Getter for <code>nucleus.ruby_records.time_of_event</code>.
	 */
	public Timestamp getTimeOfEvent();

	/**
	 * Getter for <code>nucleus.ruby_records.log_type</code>.
	 */
	public LogType getLogType();
}

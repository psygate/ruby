/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.daos;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.RubyPlayer;
import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.records.RubyPlayerRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordPlayerType;

import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

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
public class RubyPlayerDao extends DAOImpl<RubyPlayerRecord, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer, Long> {

	/**
	 * Create a new RubyPlayerDao without any configuration
	 */
	public RubyPlayerDao() {
		super(RubyPlayer.RUBY_PLAYER, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer.class);
	}

	/**
	 * Create a new RubyPlayerDao with an attached configuration
	 */
	public RubyPlayerDao(Configuration configuration) {
		super(RubyPlayer.RUBY_PLAYER, com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer> fetchById(Long... values) {
		return fetch(RubyPlayer.RUBY_PLAYER.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer fetchOneById(Long value) {
		return fetchOne(RubyPlayer.RUBY_PLAYER.ID, value);
	}

	/**
	 * Fetch records that have <code>record_id IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer> fetchByRecordId(Long... values) {
		return fetch(RubyPlayer.RUBY_PLAYER.RECORD_ID, values);
	}

	/**
	 * Fetch records that have <code>puuid IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer> fetchByPuuid(UUID... values) {
		return fetch(RubyPlayer.RUBY_PLAYER.PUUID, values);
	}

	/**
	 * Fetch records that have <code>record_player_type IN (values)</code>
	 */
	public List<com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos.RubyPlayer> fetchByRecordPlayerType(RecordPlayerType... values) {
		return fetch(RubyPlayer.RUBY_PLAYER.RECORD_PLAYER_TYPE, values);
	}
}
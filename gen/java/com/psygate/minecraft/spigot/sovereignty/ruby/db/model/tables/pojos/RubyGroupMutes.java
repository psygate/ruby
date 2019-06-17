/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyGroupMutes;

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
public class RubyGroupMutes implements IRubyGroupMutes {

	private static final long serialVersionUID = -1754052580;

	private final UUID puuid;
	private final Long groupId;

	public RubyGroupMutes(RubyGroupMutes value) {
		this.puuid = value.puuid;
		this.groupId = value.groupId;
	}

	public RubyGroupMutes(
		UUID puuid,
		Long groupId
	) {
		this.puuid = puuid;
		this.groupId = groupId;
	}

	@Override
	public UUID getPuuid() {
		return this.puuid;
	}

	@Override
	public Long getGroupId() {
		return this.groupId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RubyGroupMutes (");

		sb.append(puuid);
		sb.append(", ").append(groupId);

		sb.append(")");
		return sb.toString();
	}
}

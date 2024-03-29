/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyEntity;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordEntityType;

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
public class RubyEntity implements IRubyEntity {

	private static final long serialVersionUID = 2060442386;

	private final Long             id;
	private final Long             recordId;
	private final EntityType       entityType;
	private final String           entityName;
	private final RecordEntityType recordEntityType;

	public RubyEntity(RubyEntity value) {
		this.id = value.id;
		this.recordId = value.recordId;
		this.entityType = value.entityType;
		this.entityName = value.entityName;
		this.recordEntityType = value.recordEntityType;
	}

	public RubyEntity(
		Long             id,
		Long             recordId,
		EntityType       entityType,
		String           entityName,
		RecordEntityType recordEntityType
	) {
		this.id = id;
		this.recordId = recordId;
		this.entityType = entityType;
		this.entityName = entityName;
		this.recordEntityType = recordEntityType;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Long getRecordId() {
		return this.recordId;
	}

	@Override
	public EntityType getEntityType() {
		return this.entityType;
	}

	@Override
	public String getEntityName() {
		return this.entityName;
	}

	@Override
	public RecordEntityType getRecordEntityType() {
		return this.recordEntityType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RubyEntity (");

		sb.append(id);
		sb.append(", ").append(recordId);
		sb.append(", ").append(entityType);
		sb.append(", ").append(entityName);
		sb.append(", ").append(recordEntityType);

		sb.append(")");
		return sb.toString();
	}
}

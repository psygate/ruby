/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.pojos;


import com.psygate.minecraft.spigot.sovereignty.ruby.db.model.tables.interfaces.IRubyMaterial;

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
public class RubyMaterial implements IRubyMaterial {

	private static final long serialVersionUID = -1344504171;

	private final Long     id;
	private final Long     recordId;
	private final Material materialType;

	public RubyMaterial(RubyMaterial value) {
		this.id = value.id;
		this.recordId = value.recordId;
		this.materialType = value.materialType;
	}

	public RubyMaterial(
		Long     id,
		Long     recordId,
		Material materialType
	) {
		this.id = id;
		this.recordId = recordId;
		this.materialType = materialType;
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
	public Material getMaterialType() {
		return this.materialType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RubyMaterial (");

		sb.append(id);
		sb.append(", ").append(recordId);
		sb.append(", ").append(materialType);

		sb.append(")");
		return sb.toString();
	}
}

package com.bit.community.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
	private EventType type;
	private int actorId;
	private int entityType;
	private int entityId;
	private int entityOwnerId;
	
	private Map<String, String> exts = new HashMap<>();
	
	public EventModel setExt(String key, String value) {
		exts.put(key, value);
		return this;
	}
	
	public String getExt(String key) {
		return exts.get(key); 
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getActorId() {
		return actorId;
	}

	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;
	}

	public int getEntityType() {
		return entityType;
	}

	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}

	public int getEntityId() {
		return entityId;
	}

	public EventModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}

	public int getEntityOwnerId() {
		return entityOwnerId;
	}

	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;
	}

	public Map<String, String> getExts() {
		return exts;
	}

	public void setExts(Map<String, String> exts) {
		this.exts = exts;
	}

	public EventModel(EventType type) {
		this.type = type;
	}
	
	
}

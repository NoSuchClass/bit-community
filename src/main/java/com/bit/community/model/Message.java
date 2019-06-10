package com.bit.community.model;

import java.util.Date;

public class Message {
	private int id;
	private int toId;
	private int fromId;
	private int hasRead;
	private Date createdDate;
	private String content;
	private String conversationId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public int getHasRead() {
		return hasRead;
	}
	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getConversationId() {
		if (fromId > toId) {
			return conversationId = String.format("%d_%d", toId, fromId);
		}else {
			return conversationId = String.format("%d_%d", fromId, toId);
		}
	}
//	public void setConverstionId(String converstionId) {
//		this.converstionId = converstionId;
//	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", toId=" + toId + ", fromId=" + fromId + ", hasRead=" + hasRead + ", createdDate="
				+ createdDate + ", content=" + content + ", conversationId=" + conversationId + "]";
	}
}

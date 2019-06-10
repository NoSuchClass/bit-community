package com.bit.community.async;

import java.util.List;

public interface EventHandler {
	public void doHandle(EventModel model) ;
	public List<EventType> getSupportEventTypes();
}

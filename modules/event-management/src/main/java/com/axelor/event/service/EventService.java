package com.axelor.event.service;

import com.axelor.event.db.Event;

public interface EventService {
		public Event calculateEventSummaryFields(Event event);
		public Event checkMailSend(Event event);
}

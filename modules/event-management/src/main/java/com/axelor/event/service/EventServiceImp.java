package com.axelor.event.service;

import java.math.BigDecimal;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventServiceImp implements EventService {

	@Override
	public Event calculateEventSummaryFields(Event event) {	
		BigDecimal totalAmount=null;
		for(EventRegistration eventRegistration : event.getEventRegistrationList()) {
			totalAmount=totalAmount.add(eventRegistration.getAmount());
			System.out.println(totalAmount);
		}
		return event;
	}
}

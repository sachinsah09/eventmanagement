package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventController {

	@Inject
	EventService eventService;

	private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public void calculateEventSummaryFields(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		try {
			event = eventService.calculateEventSummaryFields(event);
			response.setValues(event);
		} catch (Exception e) {
			response.setError("Something went wrong ! please try again");
		}
	}
	
}

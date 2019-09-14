package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.service.EventService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventController {

	@Inject
	EventService eventService;

	public void calculateEventSummaryFields(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		try {
			event = eventService.calculateEventSummaryFields(event);
			response.setValues(event);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

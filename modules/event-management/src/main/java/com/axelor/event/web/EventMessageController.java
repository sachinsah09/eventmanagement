package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.service.EventMessagServiceImp;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventMessageController {

	@Inject
	EventMessagServiceImp service;

	public void sendMessageAll(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		try {
			service.sendMessageAll(event);
			response.setNotify("Send Successfully");
		} catch (AxelorException e) {
			response.setNotify("Already send to all registered email");
		}
	}
}

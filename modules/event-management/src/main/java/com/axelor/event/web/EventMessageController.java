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
	
	public void sendMessageAll(ActionRequest request, ActionResponse response){
		Event event = request.getContext().asType(Event.class);
		try {
			System.out.println(event);
			service.sendMessageAll(event);
		} catch (AxelorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

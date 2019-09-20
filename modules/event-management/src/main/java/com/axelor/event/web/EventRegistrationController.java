package com.axelor.event.web;

import java.math.BigDecimal;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {

	@Inject
	EventRegistrationService service;

	public void calculateAmount(ActionRequest request, ActionResponse response) {
		EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
		try {
			BigDecimal amount = service.calculateAmount(eventRegistration);
			response.setValue("amount", amount);
		} catch (Exception e) {
			response.setError("Something went wrong ! please try again");
		}
	}
}

package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.service.EventService;
import com.axelor.i18n.I18n;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.axelor.app.AppSettings;
import java.io.File;
import java.lang.invoke.MethodHandles;
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

	public void generateInvoiceReport(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().asType(Event.class);
		String name = "Event";

		String fileLink = ReportFactory.createReport(name + "-${date}")
				.addParam("eventId", event.getId()).addParam("Locale", ReportSettings.getPrintingLocale(null))
				.generate().getFileLink();

		logger.debug("Printing " + name);
		response.setView(ActionView.define(name).add("html", fileLink).map());
	}
}

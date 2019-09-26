package com.axelor.event.service;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.event.db.EventRegistration;
import com.google.inject.persist.Transactional;

public class ImportRegistration {

	private final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Transactional
	public Object importRegistration(Object bean, Map<String, Object> values) {
		assert bean instanceof EventRegistration;
		EventRegistration eventRegistration = (EventRegistration) bean;
		try {
			if (eventRegistration.getEvent().getTotalEntry() < eventRegistration.getEvent().getCapacity()) {
				Integer totalEntry=eventRegistration.getEvent().getTotalEntry();
				totalEntry++;
				eventRegistration.getEvent().setTotalEntry(totalEntry);;
				return eventRegistration;
			}
		} catch (Exception e) {
			LOG.error("Error when importing registering : {}", e);
		}
		return null;
	}

}

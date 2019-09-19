package com.axelor.event.service;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.repo.EmailAddressRepository;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class EventServiceImp implements EventService {

	@Inject
	Provider<EntityManager> em;

	@Override
	public Event calculateEventSummaryFields(Event event) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalDiscount = BigDecimal.ZERO;
		if (event.getEventRegistrationList() != null) {
			for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
				totalAmount = totalAmount.add(eventRegistration.getAmount());
			}
			event.setAmountCollected(totalAmount);
		}

		if (event.getDiscountList() != null) {
			if (event.getEventFees() != null && event.getEventRegistrationList() != null) {
				for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
					BigDecimal amount = eventRegistration.getAmount();
					BigDecimal diff = event.getEventFees().subtract(amount);
					totalDiscount = totalDiscount.add(diff);
				}
			}
			event.setTotatDiscount(totalDiscount);
		}
		return event;
	}
}

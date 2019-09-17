package com.axelor.event.service;

import java.math.BigDecimal;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventServiceImp implements EventService {

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

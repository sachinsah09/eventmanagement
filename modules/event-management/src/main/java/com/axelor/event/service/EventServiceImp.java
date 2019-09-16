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
			System.out.println(totalAmount);
			event.setAmountCollected(totalAmount);
		}

		if (event.getDiscountList() != null) {
			for (Discount discount : event.getDiscountList()) {
				totalDiscount = totalDiscount.add(discount.getDiscountAmount());
			}
			System.out.println(totalDiscount);
			event.setTotatDiscount(totalDiscount);
		}

		return event;
	}
}

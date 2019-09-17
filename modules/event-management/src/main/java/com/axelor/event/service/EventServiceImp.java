package com.axelor.event.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAddressRepository;
import com.axelor.apps.message.db.repo.MessageRepository;
import com.axelor.db.JPA;
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

	@Transactional
	@Override
	public Event checkMailSend(Event event) {
		for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
			Boolean isSend = false;
			String email = eventRegistration.getEmail();
			EmailAddress emailAddress = Beans.get(EmailAddressRepository.class).all().filter("self.address = ?", email)
					.fetchOne();
			if (emailAddress != null) {
				isSend = true;
			} else {
				isSend = false;
			}
			eventRegistration.setIsMailSend(isSend);
		}
		return event;
	}
}

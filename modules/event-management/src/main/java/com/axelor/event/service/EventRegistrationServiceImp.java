package com.axelor.event.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.axelor.apps.tool.date.Period;
import com.axelor.event.db.Discount;
import com.axelor.event.db.EventRegistration;
import com.google.inject.Inject;

public class EventRegistrationServiceImp implements EventRegistrationService {

	@Inject
	Period dateTool;
	
	@Override
	public BigDecimal calculateAmount(EventRegistration eventRegistration) {
		BigDecimal amount = BigDecimal.ZERO;
		if (eventRegistration.getEvent() != null && eventRegistration.getEvent().getRegistrationClose() != null
				&& eventRegistration.getRegistrationDate() != null
				&& eventRegistration.getEvent().getDiscountList() != null) {
			LocalDateTime registrationCloseDate = eventRegistration.getEvent().getRegistrationClose();
			LocalDateTime registrationDate = eventRegistration.getRegistrationDate();
		
			System.out.println(new Period(registrationDate.toLocalDate(),registrationCloseDate.toLocalDate()));
			
			BigDecimal temp = BigDecimal.ZERO;
			for (Discount discount : eventRegistration.getEvent().getDiscountList()) {
				LocalDateTime discountDate = registrationCloseDate.minusDays(discount.getBeforeDays());
				if (registrationDate.isBefore(discountDate)) {
					BigDecimal discountAmount = discount.getDiscountAmount();
					if (discountAmount.max(temp) != null) {
						temp = discountAmount;
						amount = eventRegistration.getEvent().getEventFees().subtract(discount.getDiscountAmount());
					}
				}
			}
		}
		if (amount == BigDecimal.ZERO) {
			amount = eventRegistration.getEvent().getEventFees();
		}
		return amount;
	}

}

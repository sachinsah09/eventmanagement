package com.axelor.event.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.axelor.event.db.Discount;
import com.axelor.event.db.EventRegistration;

public class EventRegistrationServiceImp implements EventRegistrationService {

	@Override
	public BigDecimal calculateAmount(EventRegistration eventRegistration) {
		BigDecimal amount = BigDecimal.ZERO;
		if (eventRegistration.getEvent() != null) {
			LocalDateTime registrationCloseDate = eventRegistration.getEvent().getRegistrationClose();
			LocalDateTime registrationDate = eventRegistration.getRegistrationDate();
			if (registrationCloseDate != null && registrationDate != null) {
				if (eventRegistration.getEvent().getDiscountList() != null) {
					BigDecimal temp2 = BigDecimal.ZERO;
					for (Discount discount : eventRegistration.getEvent().getDiscountList()) {
						LocalDateTime discountDate = registrationCloseDate.minusDays(discount.getBeforeDays());
						if (registrationDate.isBefore(discountDate)) {
							BigDecimal temp = discount.getDiscountAmount();
							if (temp.max(temp2) != null) {
								temp2 = temp;
								amount = eventRegistration.getEvent().getEventFees()
										.subtract(discount.getDiscountAmount());
							}
						}
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

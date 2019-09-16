package com.axelor.event.service;

import java.math.BigDecimal;
import com.axelor.event.db.EventRegistration;

public interface EventRegistrationService {
	
	public BigDecimal calculateAmount(EventRegistration eventRegistration);
}

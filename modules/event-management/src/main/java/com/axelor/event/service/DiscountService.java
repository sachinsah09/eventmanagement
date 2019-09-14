package com.axelor.event.service;

import java.math.BigDecimal;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;

public interface DiscountService {
	public BigDecimal calculateDiscountAmount(Discount discount,Event event);
}

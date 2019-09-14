package com.axelor.event.service;

import java.math.BigDecimal;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;

public class DiscountServiceImp  implements DiscountService {

	@Override
	public BigDecimal calculateDiscountAmount(Discount discount,Event event) {
		BigDecimal discountAmount = null;
		if(event.getEventFees() != null) {
			discountAmount=(event.getEventFees().multiply(discount.getDiscountPercent())).divide(new BigDecimal(100));
		}
		return discountAmount;
	}
}
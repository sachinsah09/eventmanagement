package com.axelor.event.web;

import java.math.BigDecimal;

import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.service.DiscountService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class DiscountController {

	@Inject
	DiscountService discountService;
	
	public void calculateDiscountAmount(ActionRequest request, ActionResponse response) {
		Event event = request.getContext().getParent().asType(Event.class);
		Discount discount = request.getContext().asType(Discount.class);
		BigDecimal discountAmount = null;
		try {
			discountAmount= discountService.calculateDiscountAmount(discount,event);
			response.setValue("discountAmount", discountAmount);
		} catch (Exception e) {
			response.setError("Error in computing Discount Amount");
		}
	}
	
}

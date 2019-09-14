package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.service.DiscountService;
import com.axelor.event.service.DiscountServiceImp;

public class Module extends AxelorModule {
	protected void configure() {
		bind(DiscountService.class).to(DiscountServiceImp.class);
	}
}
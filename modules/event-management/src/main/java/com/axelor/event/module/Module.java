package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.service.DiscountService;
import com.axelor.event.service.DiscountServiceImp;
import com.axelor.event.service.EventService;
import com.axelor.event.service.EventServiceImp;

public class Module extends AxelorModule {
	protected void configure() {
		bind(DiscountService.class).to(DiscountServiceImp.class);
		bind(EventService.class).to(EventServiceImp.class);
	}
}
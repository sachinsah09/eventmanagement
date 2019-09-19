package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.message.service.MessageServiceImpl;
import com.axelor.event.service.DiscountService;
import com.axelor.event.service.DiscountServiceImp;
import com.axelor.event.service.EventMessagServiceImp;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventRegistrationServiceImp;
import com.axelor.event.service.EventService;
import com.axelor.event.service.EventServiceImp;
import com.axelor.event.service.ImportEventRegistrationService;
import com.axelor.event.service.ImportEventRegistrationServiceImp;

public class Module extends AxelorModule {
	protected void configure() {
		bind(DiscountService.class).to(DiscountServiceImp.class);
		bind(EventService.class).to(EventServiceImp.class);
		bind(EventRegistrationService.class).to(EventRegistrationServiceImp.class);
		bind(MessageServiceImpl.class).to(EventMessagServiceImp.class);
		bind(ImportEventRegistrationService.class).to(ImportEventRegistrationServiceImp.class);
	}
}
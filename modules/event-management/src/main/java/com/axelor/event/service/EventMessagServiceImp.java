package com.axelor.event.service;

import java.util.HashSet;
import java.util.Set;
import com.axelor.apps.message.db.EmailAccount;
import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAccountRepository;
import com.axelor.apps.message.db.repo.EmailAddressRepository;
import com.axelor.apps.message.db.repo.MessageRepository;
import com.axelor.apps.message.service.MessageServiceImpl;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.meta.db.repo.MetaAttachmentRepository;
import com.google.inject.Inject;

public class EventMessagServiceImp extends MessageServiceImpl {

	@Inject
	public EventMessagServiceImp(MetaAttachmentRepository metaAttachmentRepository,
			MessageRepository messageRepository) {
		super(metaAttachmentRepository, messageRepository);
	}

	public void sendMessageAll(Event event) throws AxelorException {
		Boolean sentByEmail = true;
		Set<EmailAddress> emailAddressSet = new HashSet<EmailAddress>();
		for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
			if (eventRegistration.getIsMailSend() != true) {
				EmailAddress emailAddress = new EmailAddress(eventRegistration.getEmail());
				emailAddressSet.add(emailAddress);
				eventRegistration.setIsMailSend(sentByEmail);
			}
		}
		EmailAccount mailAccount = Beans.get(EmailAccountRepository.class).all().filter("self.isDefault=true")
				.fetchOne();
		EmailAddress fromEmailAddress = Beans.get(EmailAddressRepository.class).all().filter("self.id=38").fetchOne();
		Message message = new Message(2, "you are registered", "thank you for the registration", 2, 2,
				"ssa.axelor@gmail.com", fromEmailAddress, emailAddressSet, emailAddressSet, null, null, sentByEmail,
				mailAccount);
		sendMessage(message);
	}
}
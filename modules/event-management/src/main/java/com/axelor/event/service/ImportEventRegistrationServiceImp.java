package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;
import com.google.inject.Inject;

public class ImportEventRegistrationServiceImp implements ImportEventRegistrationService {

	@Inject
	EventRegistrationService service;
	@Inject
	EventRegistrationService eventRegistrationService;
	@Inject
	EventService eventService;

	public void importRegistrationCsv(MetaFile dataFile, Integer id) {
		File configXmlFile = this.getConfigXmlFile();
		Map<String, Object> context = new HashMap<String, Object>();
		Event event;
		event = Beans.get(EventRepository.class).all().filter("self.id = ?", id).fetchOne();
		context.put("eventPass", event);

		if (event.getTotalEntry() < event.getCapacity()) {
			File csvFile = getDataCsvFile(dataFile);
			Importer importer = new CSVImporter(configXmlFile.getAbsolutePath(), csvFile.getParent().toString());
			importer.setContext(context);
			importer.run();
		}
		event = Beans.get(EventRepository.class).all().filter("self.id = ?", id).fetchOne();
		for (EventRegistration eventRegistration : event.getEventRegistrationList()) {
			BigDecimal amount = eventRegistrationService.calculateAmount(eventRegistration);
			eventRegistration.setAmount(amount);
		}
		event.setTotalEntry(event.getEventRegistrationList().size());
		event = eventService.calculateEventSummaryFields(event);
	}

	private File getConfigXmlFile() {
		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/data/" + "input-config.xml");
			if (bindFileInputStream == null) {
			}
			FileOutputStream outputStream = new FileOutputStream(configFile);
			IOUtils.copy(bindFileInputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	private File getDataCsvFile(MetaFile dataFile) {
		File csvFile = null;
		try {
			File tempDir = Files.createTempDir();
			csvFile = new File(tempDir, "registration.csv");
			Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvFile;
	}
}

package com.axelor.event.web;

import java.util.LinkedHashMap;
import com.axelor.event.service.ImportEventRegistrationService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ImportController {

	@Inject
	ImportEventRegistrationService service;
	
	@SuppressWarnings("unchecked")
	public void importRegistration(ActionRequest request, ActionResponse response) {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
		MetaFile dataFile = Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
		Integer id = (Integer) request.getContext().get("_id");
		if (!Files.getFileExtension(dataFile.getFileName()).equals("csv")) {
			response.setError("Please select CSV File Type");
		} else {
			service.importRegistrationCsv(dataFile, id);
			response.setNotify("Registrations imported  successfully!!");
		}
		
	}
}

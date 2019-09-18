package com.axelor.event.web;

import java.util.LinkedHashMap;
import com.axelor.data.csv.CSVImporter;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.io.Files;

public class ImportController {

	@SuppressWarnings("unchecked")
	public void importRegistration(ActionRequest request, ActionResponse response) {
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
		MetaFile dataFile = Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());
		if (!Files.getFileExtension(dataFile.getFileName()).equals("csv")) {
			response.setError("Please select CSV File Type");
		} else {
			System.out.println(dataFile.getFileName());
			CSVImporter importer = new CSVImporter("*/data-init/registration-config.xml", "*/data-init/input");
			importer.run();
		}
	}
}

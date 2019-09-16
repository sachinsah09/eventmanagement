package com.axelor.event.imports;

import java.io.File;

import com.axelor.event.db.ImportConfiguration;
import com.axelor.inject.Beans;

public class FactoryImporter {

	public Importer createImporter(ImportConfiguration importConfiguration) {
		Importer importer;

		importer = Beans.get(ImporterCSV.class);
		return importer.init(importConfiguration);
	}

	public Importer createImporter(ImportConfiguration importConfiguration, File workspace) {

		Importer importer;
		importer = Beans.get(ImporterCSV.class);
		return importer.init(importConfiguration, workspace);
	}
}

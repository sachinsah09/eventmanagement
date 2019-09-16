package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;

import com.axelor.common.FileUtils;
import com.axelor.event.db.ImportConfiguration;
import com.axelor.event.db.ImportHistory;
import com.axelor.event.imports.FactoryImporter;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;
import com.google.inject.Inject;

public class ImportServiceImp implements ImportService {

	  @Inject private FactoryImporter factoryImporter;

	  @Inject private MetaFiles metaFiles;
	
	@Override
	public ImportHistory importRegistration(MetaFile dataFile) {
		// TODO Auto-generated method stub
		ImportHistory importHistory = null;
		try {
			File configXmlFile = this.getConfigXmlFile();
			File dataCsvFile = this.getDataCsvFile(dataFile);

			importHistory = importRegistrationData(dataCsvFile);
			this.deleteTempFiles(configXmlFile, dataCsvFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return importHistory;
	}

	private File getConfigXmlFile() {
		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass()
					.getResourceAsStream("/import-configs/registration-config.xml");
			if (bindFileInputStream == null) {
				System.out.println("NULL");
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

	private ImportHistory importRegistrationData(File dataCsvFile) {

		ImportHistory importHistory = null;
		try {
			ImportConfiguration importConfiguration = new ImportConfiguration();
			importConfiguration.setDataMetaFile(metaFiles.upload(dataCsvFile));

			importHistory = factoryImporter.createImporter(importConfiguration).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return importHistory;
	}

	private void deleteTempFiles(File configXmlFile, File dataCsvFile) {
		try {
			if (configXmlFile.isDirectory() && dataCsvFile.isDirectory()) {
				FileUtils.deleteDirectory(dataCsvFile);
			} else {
				configXmlFile.delete();
				dataCsvFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

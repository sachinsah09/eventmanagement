package com.axelor.event.service;

import com.axelor.meta.db.MetaFile;

public interface ImportEventRegistrationService {
	public void importRegistrationCsv(MetaFile dataFile, Integer id);
}

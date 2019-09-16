package com.axelor.event.service;

import com.axelor.event.db.ImportHistory;
import com.axelor.meta.db.MetaFile;

public interface ImportService {
	public ImportHistory importRegistration(MetaFile dataFile);
}

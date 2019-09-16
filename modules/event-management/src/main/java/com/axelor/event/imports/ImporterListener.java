package com.axelor.event.imports;

import com.axelor.data.Listener;
import com.axelor.db.Model;
import com.axelor.i18n.I18n;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImporterListener implements Listener {

	protected Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String name, importLog = "";
	private int totalRecord, successRecord, notNull, anomaly;

	public ImporterListener(String name) {
		this.name = name;
	}

	public String getImportLog() {

		String log = importLog;
		return log;
	}

	@Override
	public void imported(Model bean) {
		if (bean != null) {
			++notNull;
		}
	}

	@Override
	public void imported(Integer total, Integer success) {
		totalRecord += total;
		successRecord += success;
	}

	@Override
	public void handle(Model bean, Exception e) {
		anomaly++;
		importLog += "\n" + e;

	}
}

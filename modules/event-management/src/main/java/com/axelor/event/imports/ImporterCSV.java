package com.axelor.event.imports;

import com.axelor.data.csv.CSVImporter;
import com.axelor.event.db.ImportHistory;

import java.io.IOException;
import java.util.Map;

class ImporterCSV extends Importer {

  @Override
  protected ImportHistory process(String bind, String data, Map<String, Object> importContext)
      throws IOException {

    CSVImporter importer = new CSVImporter(bind, data);

    ImporterListener listener = new ImporterListener(getConfiguration().getName());
    importer.addListener(listener);
    importer.setContext(importContext);
    importer.run();

    return addHistory(listener);
  }

  @Override
  protected ImportHistory process(String bind, String data) throws IOException {
    return process(bind, data, null);
  }
}
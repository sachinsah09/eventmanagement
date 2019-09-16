package com.axelor.event.web;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;

import com.axelor.event.db.ImportHistory;
import com.axelor.event.service.ImportService;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ImportController {
	  @Inject private ImportService importService;

	  /**
	   * Import cities
	   *
	   * @param request
	   * @param response
	   */
	  @SuppressWarnings("unchecked")
	  public void importRegistration(ActionRequest request, ActionResponse response) {
	    LinkedHashMap<String, Object> map =
	        (LinkedHashMap<String, Object>) request.getContext().get("metaFile");
	    MetaFile dataFile =
	        Beans.get(MetaFileRepository.class).find(((Integer) map.get("id")).longValue());

	    try {
	      ImportHistory importHistory = importService.importRegistration(dataFile);
	      response.setAttr("importHistoryList", "value:add", importHistory);
	      File readFile = MetaFiles.getPath(importHistory.getLogMetaFile()).toFile();
	      response.setNotify(
	          FileUtils.readFileToString(readFile, StandardCharsets.UTF_8)
	              .replaceAll("(\r\n|\n\r|\r|\n)", "<br />"));

	    } catch (Exception e) {
	      System.out.println(e);
	    }
	  }
	}


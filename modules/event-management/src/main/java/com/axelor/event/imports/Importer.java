package com.axelor.event.imports;

import java.io.IOException;

import com.axelor.auth.AuthUtils;
import com.axelor.event.db.ImportConfiguration;
import com.axelor.event.db.ImportHistory;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Importer {

	 private static final File DEFAULT_WORKSPACE = createDefaultWorkspace();

	  protected Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	  private ImportConfiguration configuration;
	  private File workspace;

	  @Inject ExcelToCSV excelToCSV;

	  @Inject MetaFiles metaFiles;

	  public void setConfiguration(ImportConfiguration configuration) {
	    this.configuration = configuration;
	  }

	  public void setWorkspace(File workspace) {
	    Preconditions.checkArgument(workspace.exists() && workspace.isDirectory());
	    this.workspace = workspace;
	  }

	  public ImportConfiguration getConfiguration() {
	    return this.configuration;
	  }

	  public File getWorkspace() {
	    return this.workspace;
	  }

	  public Importer init(ImportConfiguration configuration) {
	    return init(configuration, DEFAULT_WORKSPACE);
	  }

	  public Importer init(ImportConfiguration configuration, File workspace) {
	    setConfiguration(configuration);
	    setWorkspace(workspace);
	    log.debug("Initialisation de l'import pour la configuration {}", configuration.getName());
	    return this;
	  }

	  public ImportHistory run(Map<String, Object> importContext) throws IOException {

	    File bind = MetaFiles.getPath(configuration.getBindMetaFile()).toFile(),
	        data = MetaFiles.getPath(configuration.getDataMetaFile()).toFile();

	    if (!bind.exists()) {
	      throw new IOException();
	            }
	    if (!data.exists()) {
	      throw new IOException();   }

	    File workspace = createFinalWorkspace(configuration.getDataMetaFile());
	    ImportHistory importHistory =
	        process(bind.getAbsolutePath(), workspace.getAbsolutePath(), importContext);
	    deleteFinalWorkspace(workspace);

	    return importHistory;
	  }

	  public ImportHistory run() throws IOException {
	    return run(null);
	  }

	  protected abstract ImportHistory process(
	      String bind, String data, Map<String, Object> importContext) throws IOException;

	  protected abstract ImportHistory process(String bind, String data) throws IOException;

	  protected void deleteFinalWorkspace(File workspace) throws IOException {

	    if (workspace.isDirectory()) {
	      FileUtils.deleteDirectory(workspace);
	    } else {
	      workspace.delete();
	    }
	  }

	  protected File createFinalWorkspace(MetaFile metaFile) throws IOException {

	    File data = MetaFiles.getPath(metaFile).toFile();
	    File finalWorkspace = new File(workspace, computeFinalWorkspaceName(data));
	    finalWorkspace.mkdir();

	    if (isZip(data)) {
	      unZip(data, finalWorkspace);
	    } else {
	      FileUtils.copyFile(data, new File(finalWorkspace, metaFile.getFileName()));
	    }

	    if (Files.getFileExtension(data.getName()).equals("xlsx"))
	      importExcel(new File(finalWorkspace, metaFile.getFileName()));

	    return finalWorkspace;
	  }

	  protected String computeFinalWorkspaceName(File data) {
	    return String.format(
	        "%s-%s",
	        Files.getNameWithoutExtension(data.getName()),
	        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
	  }

	  protected boolean isZip(File file) {
	    return Files.getFileExtension(file.getName()).equals("zip");
	  }

	  protected void unZip(File file, File directory) throws ZipException, IOException {

	    File extractFile = null;
	    FileOutputStream fileOutputStream = null;
	    ZipFile zipFile = new ZipFile(file);
	    Enumeration<? extends ZipEntry> entries = zipFile.entries();

	    while (entries.hasMoreElements()) {
	      try {
	        ZipEntry entry = entries.nextElement();
	        InputStream entryInputStream = zipFile.getInputStream(entry);
	        byte[] buffer = new byte[1024];
	        int bytesRead = 0;

	        extractFile = new File(directory, entry.getName());
	        if (entry.isDirectory()) {
	          extractFile.mkdirs();
	          continue;
	        } else {
	          extractFile.getParentFile().mkdirs();
	          extractFile.createNewFile();
	        }

	        fileOutputStream = new FileOutputStream(extractFile);
	        while ((bytesRead = entryInputStream.read(buffer)) != -1) {
	          fileOutputStream.write(buffer, 0, bytesRead);
	        }

	        if (Files.getFileExtension(extractFile.getName()).equals("xlsx")) {
	          importExcel(extractFile);
	        }
	      } catch (IOException ioException) {
	        log.error(ioException.getMessage());
	        continue;
	      } finally {
	        if (fileOutputStream == null) {
	          continue;
	        }
	        try {
	          fileOutputStream.close();
	        } catch (IOException e) {
	        }
	      }
	    }

	    zipFile.close();
	  }

	  
	  protected ImportHistory addHistory(ImporterListener listener) throws IOException {

		    ImportHistory importHistory =
		        new ImportHistory(AuthUtils.getUser(), configuration.getDataMetaFile());
		    File logFile = File.createTempFile("importLog", ".log");
		    FileWriter writer = null;
		    try {
		      writer = new FileWriter(logFile);
		      writer.write(listener.getImportLog());
		    } finally {
		      writer.close();
		    }
		    MetaFile logMetaFile =
		        metaFiles.upload(
		            new FileInputStream(logFile),
		            "importLog-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".log");
		    importHistory.setLogMetaFile(logMetaFile);
		    importHistory.setImportConfiguration(configuration);

		    return importHistory;
		  }
	  /**
	   * Ajout d'un nouveau log dans la table des historiques pour la configuration données.
	   *
	   * @param listener
	   * @return
	   * @throws IOException
	   */
	

	  private static File createDefaultWorkspace() {

	    File file = Files.createTempDir();
	    file.deleteOnExit();
	    return file;
	  }

	  public void importExcel(File excelFile) throws IOException {
	    List<Map> sheetList = excelToCSV.generateExcelSheets(excelFile);
	    FileInputStream inputStream = new FileInputStream(excelFile);
	    Workbook workBook = new XSSFWorkbook(inputStream);

	    try {
	      for (int i = 0; i < sheetList.size(); i++) {
	        Sheet sheet = workBook.getSheet(sheetList.get(i).get("name").toString());
	        File sheetFile =
	            new File(
	                excelFile.getParent() + "/" + sheetList.get(i).get("name").toString() + ".csv");
	        excelToCSV.writeTOCSV(sheetFile, sheet, 0, 0);
	      }

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	}


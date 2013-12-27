package edu.common.dynamicextensions.nui.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.nutility.FormDefinitionExporter;
import edu.common.dynamicextensions.nutility.IoUtil;

public class ExportFormAction  extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static AtomicInteger formCnt = new AtomicInteger();
	
	private static Logger logger = Logger.getLogger(ExportFormAction.class);

	public void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) {
		String tmpDir = getTmpDirName();
		String zipFileName = null;
		
		try {
			Long containerId = Long.parseLong(httpReq.getParameter("containerId"));
			Container container = Container.getContainer(containerId);
			
			if (container == null) {
				logger.info("No container found with given id: " + containerId);
				return;
			}
						
			FormDefinitionExporter export = new FormDefinitionExporter();
			export.export(container, tmpDir);
				
			zipFileName = zipFiles(tmpDir);
			sendFile(httpResp, zipFileName);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred when exporting form", e);
		} finally {
			IoUtil.delete(tmpDir);
			IoUtil.delete(zipFileName);
		}
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) { 
		doPost(request, response);
	}
	
	private String zipFiles(String dir) {
		String zipFile = new StringBuilder(System.getProperty("java.io.tmpdir"))
			.append(File.separator).append("export-form-")
			.append(formCnt.incrementAndGet()).append(".zip")
			.toString();
		
		IoUtil.zipFiles(dir, zipFile);
		return zipFile;
	}
		
	private void sendFile(HttpServletResponse response, String filePath) 
	throws Exception {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"exportedForm.zip\"");
		
		OutputStream out = response.getOutputStream();
		FileInputStream fin = null;
		
		try {
			fin = new FileInputStream(filePath);
			IoUtil.copy(fin, out);
		} finally {
			IoUtil.close(fin);
		}
	}
		
	private String getTmpDirName() {
		return new StringBuilder()
			.append(System.getProperty("java.io.tmpdir"))
			.append(File.separator)
			.append(System.currentTimeMillis())
			.append(formCnt.incrementAndGet())
			.append("export")
			.toString();
	}
}
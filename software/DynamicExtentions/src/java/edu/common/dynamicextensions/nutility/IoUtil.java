package edu.common.dynamicextensions.nutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class IoUtil {
	private static final Logger logger = Logger.getLogger(IoUtil.class);
	
	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of writer", e);
			}
		}
	}

	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of output stream", e);
			}
		}
	}
	
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (Exception e) {
				logger.warn("Error closing an instance of input stream", e);
			}
		}
	}
	
	public static void delete(String file) {
		if (file != null) {
			new File(file).delete();
		}
	}
	
	public static void copy(InputStream in, OutputStream out) 
	throws IOException {
		byte[] bytes = new byte[1024];
		int numRead = 0;
		
		while ((numRead = in.read(bytes)) >= 0) {
			out.write(bytes, 0, numRead);
		}
		
		out.flush();		
	}
	
	public static void zipFiles(String inputDir, String outFilePath) {
		FileOutputStream fout = null;
		ZipOutputStream zout = null;
		
		try {
			fout = new FileOutputStream(outFilePath);
			zout = new ZipOutputStream(fout);

			File inputDirFile = new File(inputDir);
			URI baseDir = inputDirFile.toURI();
			
			List<File> dirs = new ArrayList<File>();
			dirs.add(inputDirFile);
			
			while (!dirs.isEmpty()) {
				File dir = dirs.remove(0);			
			
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						dirs.add(file);
						continue;
					}
					
					zipFile(file, baseDir.relativize(file.toURI()).getPath(), zout);
				}
			}
			
			zout.flush();
		} catch (Exception e) {
			throw new RuntimeException("Error zipping input directory:" + inputDir, e);
		} finally {
			IoUtil.close(zout);
			IoUtil.close(fout);
		}
	}
	
	private static void zipFile(File file, String entry, ZipOutputStream zout) 
	throws Exception {		
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			
			zout.putNextEntry(new ZipEntry(entry));
			copy(fin, zout);
			zout.closeEntry();
		} finally {
			close(fin);
		}
	}	
}

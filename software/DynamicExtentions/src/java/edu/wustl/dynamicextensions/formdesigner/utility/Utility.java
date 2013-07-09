
package edu.wustl.dynamicextensions.formdesigner.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class Utility {

	public static void saveStreamToFileInTemp(InputStream uploadedInputStream, String uploadedFileLocation)
			throws IOException {
		// write code to save file using fileutils
		File pvFile = new File(uploadedFileLocation);
		pvFile.createNewFile();
		FileUtils.copyInputStreamToFile(uploadedInputStream, pvFile);
	}

}

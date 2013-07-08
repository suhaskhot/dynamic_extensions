
package edu.common.dynamicextensions.util;

import java.io.File;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author kunal_kamble
 *
 */
public class FileReader
{

	/** The file path. */
	private final String filePath;

	/** The base dir. */
	private final String baseDir;

	/**
	 * Overloaded constructor with base directory
	 * @param filePath file path
	 * @param baseDirectory base directory
	 * @throws DynamicExtensionsSystemException exception
	 */
	public FileReader(String filePath, String baseDirectory)
			throws DynamicExtensionsSystemException
	{
		baseDir = baseDirectory;
		this.filePath = getSystemIndependantFilePath(filePath);
	}

	/**
	 * @return file path by replacing %20 by space
	 * sinces spaces are replaced by %20
	 */
	public String getFilePath()
	{
		return filePath.replace("%20", " ");
	}

	/**
	 * @return relative file path from base directory by replacing %20 by space
	 * sinces spaces are replaced by %20
	 */

	public String getRelativeFilePath()
	{
		return getFilePath().replace(baseDir, "");
	}

	/**
	 * This method creates the system independent file path
	 * @param path
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public String getSystemIndependantFilePath(String path) throws DynamicExtensionsSystemException
	{
		String relativePath = path;
		if (baseDir != null && !"".equals(baseDir))
		{
			relativePath = baseDir + "/" + path;
		}
		File file = new File(relativePath.replace(" ", "%20"));
		return file.getAbsolutePath();
	}
}

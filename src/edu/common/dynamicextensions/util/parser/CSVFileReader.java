package edu.common.dynamicextensions.util.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

import au.com.bytecode.opencsv.CSVReader;

public class CSVFileReader
{
	protected String fileName;

	protected CSVReader reader;

	protected long lineNumber = 0;
	
	/**
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 */
	public CSVFileReader(String filePath) throws DynamicExtensionsSystemException
	{
		this.fileName = filePath;
		this.fileName = this.fileName.replace(" ", "%20");
		try
		{
			reader = new CSVReader(new FileReader(getSystemIndependantFilePath(this.fileName)));
		}
		catch (FileNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig file " + filePath, e);
		}
	}

	/**
	 * @return
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return
	 */
	public long getLineNumber()
	{
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 */
	public void setLineNumber(long lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	/**
	 * @return
	 */
	public CSVReader getReader()
	{
		return reader;
	}

	/**
	 * @param reader
	 */
	public void setReader(CSVReader reader)
	{
		this.reader = reader;
	}
	
	/**
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	protected String[] getNextLine(CSVReader reader) throws IOException
	{
		String[] nextLine;
		//To skip the blank lines
		while((nextLine =reader.readNext()) != null){
			lineNumber++;
			if(nextLine[0].length() != 0){
				break;
			}
		}
		
		return nextLine ;
	}
	
	/**
	 * @param path
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected String getSystemIndependantFilePath(String path) throws DynamicExtensionsSystemException
	{
		URI uri = null;
		try
		{
			uri = new URI("file:///" + path);
		}
		catch (URISyntaxException e)
		{
			throw new DynamicExtensionsSystemException("Error while openig CSV file  " + path, e);
		}
		return uri.getPath();
	}



}

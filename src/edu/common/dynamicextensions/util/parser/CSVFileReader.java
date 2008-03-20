package edu.common.dynamicextensions.util.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

public class CSVFileReader extends edu.common.dynamicextensions.util.FileReader
{
	

	protected CSVReader reader;

	protected long lineNumber = 0;
	
	/**
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 * @throws FileNotFoundException 
	 */
	public CSVFileReader(String filePath) throws DynamicExtensionsSystemException, FileNotFoundException
	{
		super(filePath);
		reader =  new CSVReader(new FileReader(filePath));
		
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
	public String[] getNextLine() throws IOException
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
	


}

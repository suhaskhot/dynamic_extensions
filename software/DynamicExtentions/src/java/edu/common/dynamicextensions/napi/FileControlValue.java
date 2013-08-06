
package edu.common.dynamicextensions.napi;

import java.io.InputStream;

public class FileControlValue
{

	/**
	 * this is the name of the file 
	 */
	private String fileName;

	/**
	 * file content e.g. MIME text etc.
	 */
	private String contentType;

	private Long recordId;
	
	private String filePath;
	
	private InputStream in;

	public FileControlValue() {
		
	}
	
	public FileControlValue(String fileName, String contentType, Long recordId) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.recordId = recordId;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public Long getRecordId()
	{
		return recordId;
	}

	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
}

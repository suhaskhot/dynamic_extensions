/*
 * Created on Nov 3, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;

/**
 * @author preeti_munot
 * @hibernate.joined-subclass table="DYEXTN_FILE_UPLOAD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class FileUploadControl extends Control implements FileUploadInterface
{
	

	Integer columns = null;
	/** 
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateHTML()
	 */
	public String generateHTML()
	{
		String htmlString = "<input type= \"file\" " +  "name = \"value(" + getHTMLComponentName() + ")\" " + "id = \""
		+ getHTMLComponentName() + "\"/>";
		return htmlString;
	}

	/**
	 * @hibernate.property name="columns" type="integer" column="NO_OF_COLUMNS" 
	 * @return Returns the columns.
	 */
	public Integer getColumns()
	{
		return columns;
	}

	/**
	 * 
	 */
	public void setColumns(Integer columns)
	{
		this.columns = columns;
	}

}
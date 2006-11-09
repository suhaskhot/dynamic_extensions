/*
 * Created on Nov 3, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FileUploadControl extends Control implements FileUploadInterface
{
	/**
	 * Size of the text field to be shown on UI.
	 */
	protected Integer columns;

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateHTML()
	 */
	@Override
	public String generateHTML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return Number of columns (size of text box shown on UI)
	 */
	public Integer getColumns()
	{
		return this.columns;
	}
	/**
	 * 
	 * @param columns  Number of columns (size of text box shown on UI)
	 */
	public void setColumns(Integer columns)
	{
		this.columns = columns;
	}

}

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

	Integer columns = null;
	/** 
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateHTML()
	 */
	public String generateHTML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface#getColumns()
	 */
	public Integer getColumns()
	{
		return columns;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface#setColumns(java.lang.Integer)
	 */
	public void setColumns(Integer columns)
	{
		this.columns = columns;
	}

}

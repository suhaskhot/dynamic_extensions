/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;


/**
 * @author preeti_lodha
 *
 * Interface for XMI Export
 */
public interface XMIExportInterface
{
	public void exportXMI(String filename, javax.jmi.reflect.RefPackage extent, String xmiVersion);
	
}

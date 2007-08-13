/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.importer;

import java.util.Collection;

/**
 * @author preeti_lodha
 *
 * This is interface for XMI Import
 */
public interface XMIImportInterface
{
	public Collection importXMI(String filename, javax.jmi.reflect.RefPackage extent); 

}

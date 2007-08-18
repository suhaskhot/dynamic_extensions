
package edu.common.dynamicextensions.xmi.importer;

import edu.wustl.cab2b.server.path.DomainModelParser;

public class ImportTest
{

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args)
	{		
		try
		{
			DomainModelParser parser = new DomainModelParser("C://Documents and Settings//ashish_gupta//Desktop//XMLs//catissue-domainModel.xml");
			new DynamicExtensionsDomainModelProcessor(parser, "Ashish");
			System.out.println("--------------- Done ------------");
		}
		catch(Exception e)
		{			
			e.printStackTrace();
		}
	}
}

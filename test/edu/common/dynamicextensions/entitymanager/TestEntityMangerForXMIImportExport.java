
package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestEntityMangerForXMIImportExport extends DynamicExtensionsBaseTestCase
{

	public void testXMIExport()
	{
		try
		{
			EntityGroupInterface entityGroup = new MockEntityManager()
					.initializeEntityGroupForInheritanceAndAssociation();

			XMIBuilder xmiBuilder = new XMIBuilder();
			xmiBuilder.exportMetadataToXMI(entityGroup);
		}
		catch (DynamicExtensionsApplicationException dynamicExtensionsApplicationException)
		{
			System.out.print(dynamicExtensionsApplicationException.getMessage());
			System.exit(1);
		}
	}

}

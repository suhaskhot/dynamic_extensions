
package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.client.DownloadPVVersionClient;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

/**
 *
 * @author mandar_shidhore
 *
 */
public class TestReadPermissibleValueProcessor extends DynamicExtensionsBaseTestCase
{

	/**
	 *
	 */
	public void testDownLoadPermissibleValues()
	{
		try
		{
			String[] parameters = {"CPUML/TestModels/Ant_Task_For_Downloading_PV's/",APPLICATIONURL,"CPUML/TestModels/Ant_Task_For_Downloading_PV's/","allInstanceOfAllAttributeOfAllVersion.xml"};
			DownloadPVVersionClient.main(parameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

}

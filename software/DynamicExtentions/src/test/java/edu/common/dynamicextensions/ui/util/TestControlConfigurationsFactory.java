
package edu.common.dynamicextensions.ui.util;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestControlConfigurationsFactory extends DynamicExtensionsBaseTestCase
{

	public void testGetControlsDataTypes()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			List dataTypeList = controlsConfigfactory
					.getControlsDataTypes(ProcessorConstants.TEXT_CONTROL);
			if (dataTypeList.isEmpty())
			{
				fail("testGetControlsDataTypes-->failed. dataTypeList is empty for textControl.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetControlsDataTypes--> failed. exception occured");
		}
	}

	public void testGetControlJspName()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			String jspPageName = controlsConfigfactory
					.getControlJspName(ProcessorConstants.TEXT_CONTROL);
			if (jspPageName == null || jspPageName.trim().equals(""))
			{
				fail("testGetControlJspName-->failed. jspPage name for text control is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetControlJspName--> failed. exception occured");
		}
	}

	public void testGetControlImagePath()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			String imagePath = controlsConfigfactory
					.getControlImagePath(ProcessorConstants.TEXT_CONTROL);
			if (imagePath == null || imagePath.trim().equals(""))
			{
				fail("testGetControlImagePath-->failed. imagePath for text control is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetControlImagePath--> failed. exception occured");
		}
	}

	public void testGetImplicitRules()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			List implicitRules = controlsConfigfactory.getImplicitRules(
					ProcessorConstants.TEXT_CONTROL, ProcessorConstants.DATATYPE_NUMBER);
			if (implicitRules == null || implicitRules.isEmpty())
			{
				fail("testGetImplicitRules-->failed. Implicite rule for text control of number data type is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetImplicitRules--> failed. exception occured");
		}
	}

	public void testGetExplicitRules()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			List explicitRules = controlsConfigfactory.getExplicitRules(
					ProcessorConstants.TEXT_CONTROL, ProcessorConstants.DATATYPE_NUMBER);
			if (explicitRules == null || explicitRules.isEmpty())
			{
				fail("testGetExplicitRules-->failed. Explicite rule for text control of number data type is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetExplicitRules--> failed. exception occured");
		}
	}

	public void testGetListOfControls()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			List controlsList = controlsConfigfactory.getListOfControls();
			if (controlsList == null || controlsList.isEmpty())
			{
				fail("testGetListOfControls-->failed. Explicite rule for text control of number data type is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetListOfControls--> failed. exception occured");
		}
	}

	public void testGetRuleDisplayLabels()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			List explicitRules = controlsConfigfactory.getExplicitRules(
					ProcessorConstants.TEXT_CONTROL, ProcessorConstants.DATATYPE_NUMBER);
			List controlsList = controlsConfigfactory.getRuleDisplayLabels(explicitRules);
			if (controlsList == null || controlsList.size() != explicitRules.size())
			{
				fail("testGetRuleDisplayLabels-->failed. display labels for Explicite rule for text control of number data type is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetRuleDisplayLabels--> failed. exception occured");
		}
	}

	public void testGetRulesMap()
	{
		try
		{
			ControlConfigurationsFactory controlsConfigfactory = ControlConfigurationsFactory
					.getInstance();
			Map rulesMap = controlsConfigfactory.getRulesMap(ProcessorConstants.TEXT_CONTROL);
			if (rulesMap == null || rulesMap.isEmpty())
			{
				fail("testGetRulesMap-->failed. Rules map for text control is empty.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("testGetRulesMap--> failed. exception occured");
		}
	}
}

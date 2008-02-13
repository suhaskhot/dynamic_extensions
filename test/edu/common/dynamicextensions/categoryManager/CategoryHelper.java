package edu.common.dynamicextensions.categoryManager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;

/**
 * @author kunal_kamble
 * @author mandar_shidhore
 *
 */
public class CategoryHelper implements CategoryHelperInterface
{

	public void addControl(String attributeName, ContainerInterface container, ControlEnum controlValue,
			List<PermissibleValueInterface>... permissibleValueList)
	{
		// TODO Auto-generated method stub

	}

	public CategoryEntityInterface createCategoryEntity(String entityName, CategoryInterface... category)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public CategoryInterface createCtaegory(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

}

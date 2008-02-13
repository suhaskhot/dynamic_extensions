
package edu.common.dynamicextensions.categoryManager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;

/**
 * 
 * @author mandar_shidhore
 * @author kunal_kamble
 *
 */
public interface CategoryHelperInterface
{
	public int textFieldControlNumber = 1;

	public enum ControlEnum {
		textFieldControlNumber(1);

		Integer value;

		ControlEnum(Integer value)
		{
			this.value = value;
		}

		public Integer getValue()
		{
			return value;
		}

		public static ControlEnum get(Integer value)
		{
			ControlEnum [] controlValues = ControlEnum.values();

			for (ControlEnum controlNumber : controlValues)
			{
				if (controlNumber.getValue().equals(value))
				{
					return controlNumber;
				}
			}
			return null;
		}
	};

	public CategoryInterface createCtaegory(String name);

	public CategoryEntityInterface createCategoryEntity(String entityName, CategoryInterface... category);

	public void addControl(String attributeName, ContainerInterface container, ControlEnum controlValue,
			List<PermissibleValueInterface>... permissibleValueList);

}


package edu.common.dynamicextensions.categoryManager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * 
 * @author mandar_shidhore
 * @author kunal_kamble
 *
 */
public interface CategoryHelperInterface
{
//	public int textFieldControl = 1;
//	public int listBoxControl = 2;
//	public int datePickerControl = 3;
//	public int fileControl = 4;
//	public int radioButtonControl = 5;

	public enum ControlEnum {
		textFieldControl, listBoxControl, datePickerControl, fileControl, radioButtonControl, TEXTAREA;

		Integer value;

//		ControlEnum(Integer value)
//		{
//			this.value = value;
//		}

//		public Integer getValue()
//		{
//			return value;
//		}
//
//		public static ControlEnum get(Integer value)
//		{
//			ControlEnum[] controlValues = ControlEnum.values();
//
//			for (ControlEnum controlNumber : controlValues)
//			{
//				if (controlNumber.getValue().equals(value))
//				{
//					return controlNumber;
//				}
//			}
//			return null;
//		}
	};

	/**
	 * @param name
	 * @return
	 */
	public CategoryInterface createCtaegory(String name);

	/**
	 * @param entity
	 * @param category
	 * @return
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, CategoryInterface... category);

	/**
	 * @param entity
	 * @param attributeName
	 * @param container
	 * @param controlValue
	 * @param permissibleValueList
	 */
	public void addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			List<PermissibleValueInterface>... permissibleValueList);

	/**
	 * @param parentContainer
	 * @param childContainer
	 */
	public void setChildCategoryEntity(ContainerInterface parentContainer, ContainerInterface childContainer);
	
	/**
	 * @param parentContainer
	 * @param childContainer
	 */
	public void setParent(ContainerInterface parentContainer, ContainerInterface childContainer);
	
	/**
	 * @param sourceContainer
	 * @param targetContainer
	 * @param sourceRoleList
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public void associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer, List<String> sourceRoleList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

}

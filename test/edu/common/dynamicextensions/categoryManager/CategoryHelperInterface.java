package edu.common.dynamicextensions.categoryManager;

import java.util.List;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
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
	public enum ControlEnum {
		TEXT_FIELD_CONTROL, LIST_BOX_CONTROL, DATE_PICKER_CONTROL, FILE_UPLOAD_CONTROL, RADIO_BUTTON_CONTROL, 
		TEXT_AREA_CONTROL,CHECK_BOX_CONTROL;
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
			String controlCaption, List<String>... permissibleValueList);

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
	 * @param noOfEntries
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAssociationControlInterface associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer, 
			List<String> sourceRoleList,int noOfEntries)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

}

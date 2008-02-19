
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
		TEXT_FIELD_CONTROL, LIST_BOX_CONTROL, DATE_PICKER_CONTROL, FILE_UPLOAD_CONTROL, RADIO_BUTTON_CONTROL, TEXT_AREA_CONTROL, CHECK_BOX_CONTROL;
	};

	/**
	 * Create category with the given name.
	 * @param name
	 * @return
	 */
	public CategoryInterface createCategory(String name);

	/**
	 * Create Category entity and category container from given entity. Category name should  
	 * be passed if we want to make this category entity as root category entity.
	 * @param entity
	 * @param category
	 * @return
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, CategoryInterface... category);

	/**
	 * Add a controls to category containers. Attribute name is used to get the attribute of the entity. 
	 * controlValue is used to select the type of control desired. controlCaption is used to modify the 
	 * UI property i.e. label of the attribute. Lastly a list of permissible values is passed if any permissible
	 * values subset exist for an attribute.
	 * @param entity
	 * @param attributeName
	 * @param container
	 * @param controlValue
	 * @param permissibleValueList
	 */
	public void addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			String controlCaption, List<String>... permissibleValueList);

	/**
	 * Set parent container.
	 * @param parentContainer
	 * @param childContainer
	 */
	public void setParentContainer(ContainerInterface parentContainer, ContainerInterface childContainer);

	/**
	 * Associate category containers with each other.
	 * E.G. User (1) ----> (*) Study. Here user category entity's container will be sourceContainer,
	 * study category entity's container will be targetContainer, sourceRoleList will contain source roles
	 * for all associations between the containers. noOfEntries indicates multiplicity.
	 * @param sourceContainer
	 * @param targetContainer
	 * @param associationNamesList
	 * @param noOfEntries
	 * @return CategoryAssociationControlInterface 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAssociationControlInterface associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer,
			List<String> associationNamesList, int noOfEntries) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

}

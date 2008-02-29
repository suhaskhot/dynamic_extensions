
package edu.common.dynamicextensions.util;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This is an helper API for creating category.
 * @author mandar_shidhore
 * @author kunal_kamble
 */
public interface CategoryHelperInterface
{
	/**
	 * These are constants to be used while creating controls.
	 */
	public enum ControlEnum {
		TEXT_FIELD_CONTROL("textField"), LIST_BOX_CONTROL("listBox"), DATE_PICKER_CONTROL("datePicker"), FILE_UPLOAD_CONTROL("fileUpload"), RADIO_BUTTON_CONTROL(
				"radioButton"), TEXT_AREA_CONTROL("textArea"), CHECK_BOX_CONTROL("checkBox"), COMBO_BOX_CONTROL("comboBox");

		String value;

		ControlEnum()
		{

		}

		ControlEnum(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}

		public static ControlEnum get(String value)
		{
			ControlEnum[] ControlEnumValues = ControlEnum.values();

			for (ControlEnum ce : ControlEnumValues)
			{
				if (ce.getValue().equalsIgnoreCase(value))
				{
					return ce;
				}
			}
			return null;
		}
	};

	/**
	 * Create category with the given name.
	 * @param name name by which we wish to create the category.
	 * @return category
	 */
	public CategoryInterface createCategory(String name);

	/**
	 * Saves a category.
	 * @param category category object to be saved.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public void saveCategory(CategoryInterface category) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Create category container and category entity from given entity.
	 * @param entity entity used to create a category entity and category container.
	 * @param containerCaption container name on UI.
	 * @return container.
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, String containerCaption);

	/**
	 * Set the root category entity of this category.
	 * @param container root category entity's container.
	 * @param category
	 */
	public void setRootCategoryEntity(ContainerInterface container, CategoryInterface category);

	/**
	 * Add a controls to category containers. Attribute name is used to get the attribute of the entity. 
	 * controlValue is used to select the type of control desired. controlCaption is used to modify the 
	 * UI property i.e. label of the attribute. Lastly a list of permissible values is passed if any permissible
	 * values subset exist for an attribute.
	 * @param entity used to create a category entity.
	 * @param attributeName name of the attribute belonging to the entity.
	 * @param container the container created for category entity to which we wish to add a control.
	 * @param controlValue type of control to be created. (e.g. ControlEnum.TEXT_FIELD_CONTROL)
	 * @param permissibleValues in case of radio buttons, lists and combo boxes, the list of permissible values is required, optional otherwise.
	 * @throws DynamicExtensionsApplicationException
	 */
	public void addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			String controlCaption, List<String>... permissibleValues) throws DynamicExtensionsApplicationException;

	/**
	 * This method is used when there is inheritance between two entities.
	 * @param parentContainer parentForm
	 * @param childContainer childForm
	 */
	public void setParentContainer(ContainerInterface parentContainer, ContainerInterface childContainer);

	/**
	 * Associate category containers with each other.
	 * E.G. User (1) ------userstudyassociation------> (*) Study. Here user category entity's container will be sourceContainer,
	 * study category entity's container will be targetContainer, associationNameList will contain source roles
	 * for all associations between the containers. noOfEntries indicates multiplicity.
	 * @param sourceContainer equivalent to main form
	 * @param targetContainer equivalent to sub form
	 * @param associationNameList names of the association(s) present between two entities involved.
	 * e.g 'userstudyassociation' as depicted above
	 * @param noOfEntries indicates multiplicity. e.g. one-to-one (1) or one-to-many (-1) etc.
	 * @return CategoryAssociationControlInterface 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAssociationControlInterface associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer,
			List<String> associationNameList, int noOfEntries) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Method returns the next sequenceNumber
	 * @param container category entity container
	 * @return next sequence number
	 */
	public int getNextSequenceNumber(ContainerInterface container);

}

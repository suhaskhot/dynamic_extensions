
package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;

/**
 * @author kunal_kamble
 * @author mandar_shidhore
 *
 */
public class CategoryHelper implements CategoryHelperInterface
{
	DomainObjectFactory factory = DomainObjectFactory.getInstance();

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCtaegory(java.lang.String)
	 */
	public CategoryInterface createCtaegory(String name)
	{
		CategoryInterface category = factory.createCategory();
		category.setName(name);
		return category;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCategoryEntity(java.lang.String, edu.common.dynamicextensions.domaininterface.CategoryInterface[])
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, CategoryInterface... category)
	{
		CategoryEntityInterface categoryEntity = factory.createCategoryEntity();
		categoryEntity.setName(entity.getName() + " Category Entity");
		categoryEntity.setEntity(entity);

		if (category != null && category.length != 0)
		{
			for (CategoryInterface c : category)
			{
				categoryEntity.setCategory(c);
				c.setRootCategoryElement(categoryEntity);
			}
		}

		ContainerInterface container = createContainer(categoryEntity);
		return container;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#addControl(edu.common.dynamicextensions.domaininterface.AttributeInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.categoryManager.CategoryHelperInterface.ControlEnum, java.util.List<edu.common.dynamicextensions.domaininterface.PermissibleValueInterface>[])
	 */
	public void addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			String controlCaption, List<String>... permissibleValueList)
	{
		CategoryAttributeInterface categoryAttribute = factory.createCategoryAttribute();
		categoryAttribute.setName(attributeName + " Category Attribute");
		categoryAttribute.setAttribute(entity.getAttributeByName(attributeName));

		CategoryEntity categoryEntity = (CategoryEntity) container.getAbstractEntity();
		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);

		ControlInterface controlInterface = null;
		switch (controlValue)
		{
			case TEXT_FIELD_CONTROL :
				controlInterface = createTextFieldControl(container, categoryAttribute);
				break;
			case LIST_BOX_CONTROL :
				controlInterface = createListBoxControl(container, categoryAttribute, createPermissibleValueList(permissibleValueList[0]));
				break;
			case DATE_PICKER_CONTROL :
				controlInterface = createDatePickerControl(container, categoryAttribute);
				break;
			case FILE_UPLOAD_CONTROL :
				controlInterface = createFileUploadControl(container, categoryAttribute);
				break;
			case TEXT_AREA_CONTROL :
				controlInterface = createTextAreaControl(container, categoryAttribute);
				break;
			case RADIO_BUTTON_CONTROL :
				controlInterface = createRadioButtonControl(container, categoryAttribute, createPermissibleValueList(permissibleValueList[0]));
				break;
			case CHECK_BOX_CONTROL :
				controlInterface = createCheckBoxControl(container, categoryAttribute);
				break;

		}

		controlInterface.setCaption(controlCaption);

		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setChildCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setChildCategoryEntity(ContainerInterface parentContainer, ContainerInterface childContainer)
	{
		CategoryEntityInterface parentCategoryEntity = (CategoryEntity) parentContainer.getAbstractEntity();
		CategoryEntityInterface childCategoryEntity = (CategoryEntity) childContainer.getAbstractEntity();
		parentCategoryEntity.addChildCategory(childCategoryEntity);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setParentCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setParent(ContainerInterface parentContainer, ContainerInterface childContainer)
	{
		CategoryEntityInterface parentCategoryEntity = (CategoryEntity) parentContainer.getAbstractEntity();
		CategoryEntityInterface childCategoryEntity = (CategoryEntity) childContainer.getAbstractEntity();
		childCategoryEntity.setParentCategoryEntity(parentCategoryEntity);

		childContainer.setBaseContainer(parentContainer);
	}

	/**
	 * @param sourceContainer
	 * @param targetContainer
	 * @param sourceRoleList
	 * @param noOfEntries
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryAssociationControlInterface associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer,
			List<String> sourceRoleList, int noOfEntries) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		PathInterface path = factory.createPath();

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		int pathSequenceNumber = 1;

		for (String sourceRole : sourceRoleList)
		{
			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setPathSequenceNumber(pathSequenceNumber++);

			EntityInterface sourceEntity = ((CategoryEntityInterface) sourceContainer.getAbstractEntity()).getEntity();
			EntityInterface tatgetEntity = ((CategoryEntityInterface) targetContainer.getAbstractEntity()).getEntity();

			System.out.println("\tassociation name " + sourceRole);
			pathAssociationRelationInterface.setAssociation(entityManagerInterface.getAssociationByName(sourceRole));

			pathAssociationRelationInterface.setPath(path);
			path.addPathAssociationRelation(pathAssociationRelationInterface);
		}

		CategoryEntityInterface sourceCategoryEntity = (CategoryEntityInterface) sourceContainer.getAbstractEntity();
		CategoryEntityInterface targetCategoryEntity = (CategoryEntityInterface) targetContainer.getAbstractEntity();

		addPathBetweenCategoryEntities(sourceCategoryEntity, targetCategoryEntity, path);
		targetCategoryEntity.setNumberOfEntries(noOfEntries);

		CategoryAssociationInterface categoryAssociation = associateCategoryEntities(sourceCategoryEntity, targetCategoryEntity, sourceCategoryEntity
				.getName()
				+ " to " + targetCategoryEntity.getName() + " category association");

		CategoryAssociationControlInterface categoryAssociationControlInterface = createCategoryAssociationControl(sourceContainer, targetContainer,
				categoryAssociation, sourceCategoryEntity.getName() + "-" + targetCategoryEntity.getName() + " association control");

		return categoryAssociationControlInterface;
	}

	/**
	 * @param sourceCategoryEntity
	 * @param targetCategoryEntity
	 * @param path
	 */
	private void addPathBetweenCategoryEntities(CategoryEntityInterface sourceCategoryEntity, CategoryEntityInterface targetCategoryEntity,
			PathInterface path)
	{
		targetCategoryEntity.setPath(path);
		sourceCategoryEntity.addChildCategory(targetCategoryEntity);
	}

	/**
	 * Method associates the source and the target category entity
	 * @param sourceCategoryEntity
	 * @param targetCategoryEntity
	 * @param name
	 * @return category association object
	 */
	private CategoryAssociationInterface associateCategoryEntities(CategoryEntityInterface sourceCategoryEntity,
			CategoryEntityInterface targetCategoryEntity, String name)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		CategoryAssociationInterface categoryAssociation = factory.createCategoryAssociation();

		categoryAssociation.setName(name);
		categoryAssociation.setTargetCategoryEntity(targetCategoryEntity);
		categoryAssociation.setCategoryEntity(sourceCategoryEntity);

		sourceCategoryEntity.addChildCategory(targetCategoryEntity);

		sourceCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation);

		return categoryAssociation;
	}

	/**
	 * Method creates the association between the given parent and the target container 
	 * @param parentContainer
	 * @param targetContainer
	 * @param categoryAssociation
	 * @param caption
	 */
	private CategoryAssociationControlInterface createCategoryAssociationControl(ContainerInterface parentContainer,
			ContainerInterface targetContainer, CategoryAssociationInterface categoryAssociation, String caption)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		CategoryAssociationControlInterface categoryAssociationControl = factory.createCategoryAssociationControl();

		categoryAssociationControl.setCaption(caption);
		categoryAssociationControl.setContainer(targetContainer);
		categoryAssociationControl.setBaseAbstractAttribute(categoryAssociation);
		categoryAssociationControl.setSequenceNumber(getNextSequenceNumber(parentContainer));

		categoryAssociationControl.setParentContainer((Container) parentContainer);
		parentContainer.addControl(categoryAssociationControl);

		return categoryAssociationControl;
	}

	/**
	 * @param abstractEntity
	 * @return
	 */
	private ContainerInterface createContainer(AbstractEntityInterface abstractEntity)
	{
		ContainerInterface container = DomainObjectFactory.getInstance().createContainer();
		container.setCaption(abstractEntity.getName() + "_container");
		container.setAbstractEntity(abstractEntity);
		container.setMainTableCss("formRequiredLabel");
		container.setRequiredFieldIndicatior("*");
		container.setRequiredFieldWarningMessage("indicates mandatory fields.");
		abstractEntity.addContainer(container);

		return container;
	}

	/**
	 * 
	 * @param container
	 * @param baseAbstractAttribute
	 * @param sequenceNumber
	 * @return
	 */
	private TextFieldInterface createTextFieldControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		TextFieldInterface textField = DomainObjectFactory.getInstance().createTextField();
		textField.setCaption(baseAbstractAttribute.getName());
		textField.setBaseAbstractAttribute(baseAbstractAttribute);
		textField.setColumns(50);
		textField.setSequenceNumber(getNextSequenceNumber(container));

		textField.setParentContainer((Container) container);
		container.addControl(textField);

		return textField;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @param permissibleValueList
	 * @return
	 */
	private ListBoxInterface createListBoxControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute,
			List<PermissibleValueInterface> permissibleValueList)
	{
		ListBoxInterface listBox = DomainObjectFactory.getInstance().createListBox();
		listBox.setCaption(baseAbstractAttribute.getName());
		listBox.setBaseAbstractAttribute(baseAbstractAttribute);
		listBox.setSequenceNumber(getNextSequenceNumber(container));

		listBox.setParentContainer((Container) container);
		container.addControl(listBox);

		UserDefinedDEInterface userDefinedDE = factory.createUserDefinedDE();
		for (PermissibleValueInterface pv : permissibleValueList)
		{
			userDefinedDE.addPermissibleValue(pv);
		}
		((CategoryAttribute) baseAbstractAttribute).setDataElement(userDefinedDE);

		return listBox;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @return
	 */
	private DatePickerInterface createDatePickerControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		DatePickerInterface datePicker = DomainObjectFactory.getInstance().createDatePicker();
		datePicker.setCaption(baseAbstractAttribute.getName());
		datePicker.setBaseAbstractAttribute(baseAbstractAttribute);
		datePicker.setSequenceNumber(getNextSequenceNumber(container));

		datePicker.setParentContainer((Container) container);
		container.addControl(datePicker);
		datePicker.setDateValueType(ProcessorConstants.DATE_ONLY_FORMAT);
		return datePicker;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @return
	 */
	private FileUploadInterface createFileUploadControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		FileUploadInterface fileUpload = DomainObjectFactory.getInstance().createFileUploadControl();
		fileUpload.setCaption(baseAbstractAttribute.getName());
		fileUpload.setBaseAbstractAttribute(baseAbstractAttribute);
		fileUpload.setSequenceNumber(getNextSequenceNumber(container));

		fileUpload.setParentContainer((Container) container);
		container.addControl(fileUpload);

		return fileUpload;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @return
	 */
	private TextAreaInterface createTextAreaControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		TextAreaInterface textArea = DomainObjectFactory.getInstance().createTextArea();
		textArea.setCaption(baseAbstractAttribute.getName());
		textArea.setBaseAbstractAttribute(baseAbstractAttribute);
		textArea.setSequenceNumber(getNextSequenceNumber(container));
		textArea.setColumns(50);
		textArea.setRows(5);

		textArea.setParentContainer((Container) container);
		container.addControl(textArea);

		return textArea;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @param permissibleValueList
	 * @return
	 */
	private RadioButtonInterface createRadioButtonControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute,
			List<PermissibleValueInterface> permissibleValueList)
	{
		RadioButtonInterface radioButton = DomainObjectFactory.getInstance().createRadioButton();
		radioButton.setCaption(baseAbstractAttribute.getName());
		radioButton.setBaseAbstractAttribute(baseAbstractAttribute);
		radioButton.setSequenceNumber(getNextSequenceNumber(container));
		UserDefinedDEInterface userDefinedDE = factory.createUserDefinedDE();

		for (PermissibleValueInterface pv : permissibleValueList)
		{
			userDefinedDE.addPermissibleValue(pv);
		}
		((CategoryAttribute) baseAbstractAttribute).setDataElement(userDefinedDE);

		radioButton.setParentContainer((Container) container);
		container.addControl(radioButton);

		return radioButton;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @return
	 */
	private CheckBoxInterface createCheckBoxControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		CheckBoxInterface checkBox = DomainObjectFactory.getInstance().createCheckBox();
		checkBox.setCaption(baseAbstractAttribute.getName());
		checkBox.setBaseAbstractAttribute(baseAbstractAttribute);
		checkBox.setSequenceNumber(getNextSequenceNumber(container));

		checkBox.setParentContainer((Container) container);
		container.addControl(checkBox);

		return checkBox;
	}

	/**
	 * @param values
	 * @return
	 */
	private List<PermissibleValueInterface> createPermissibleValueList(List<String> values)
	{
		List<PermissibleValueInterface> pvList = new ArrayList<PermissibleValueInterface>();
		for (String permissibleValueString : values)
		{
			PermissibleValueInterface permissibleValue = factory.createStringValue();
			((StringValue) permissibleValue).setValue(permissibleValueString);

			pvList.add(permissibleValue);
		}
		return pvList;
	}

	/**
	 * Method returns the next sequenceNumber
	 * @param container
	 * @return
	 */
	public int getNextSequenceNumber(ContainerInterface container)
	{
		int nextSequenceNumber = 1;

		if (container.getAllControls() != null)
		{
			nextSequenceNumber = container.getAllControls().size() + 1;
		}
		return nextSequenceNumber;
	}

}
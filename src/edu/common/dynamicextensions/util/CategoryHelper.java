
package edu.common.dynamicextensions.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
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
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
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

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCtaegory(java.lang.String)
	 */
	public CategoryInterface createCategory(String name)
	{
		CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
		category.setName(name);
		return category;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#saveCategory(edu.common.dynamicextensions.domaininterface.CategoryInterface)
	 */
	public void saveCategory(CategoryInterface category) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			categoryManager.persistCategory(category);
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
			throw new DynamicExtensionsSystemException("Error while saving a category");
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
			throw new DynamicExtensionsApplicationException("Error while saving a category");
		}
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCategoryEntity(java.lang.String, edu.common.dynamicextensions.domaininterface.CategoryInterface[])
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, String containerCaption)
	{
		CategoryEntityInterface categoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntity.setName(entity.getName() + " Category Entity");
		categoryEntity.setEntity(entity);

		ContainerInterface container = createContainer(categoryEntity, containerCaption);
		return container;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setRootCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.CategoryInterface)
	 */
	public void setRootCategoryEntity(ContainerInterface container, CategoryInterface category)
	{
		category.setRootCategoryElement((CategoryEntityInterface) container.getAbstractEntity());
		((CategoryEntityInterface) container.getAbstractEntity()).setCategory(category);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#addControl(edu.common.dynamicextensions.domaininterface.AttributeInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.categoryManager.CategoryHelperInterface.ControlEnum, java.util.List<edu.common.dynamicextensions.domaininterface.PermissibleValueInterface>[])
	 */
	public ControlInterface addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			String controlCaption, List<String>... permissibleValueList) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		CategoryAttributeInterface categoryAttribute = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute.setName(attributeName + " Category Attribute");
		categoryAttribute.setAttribute(entity.getAttributeByName(attributeName));

		CategoryEntity categoryEntity = (CategoryEntity) container.getAbstractEntity();
		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);

		ControlInterface control = null;
		switch (controlValue)
		{
			case TEXT_FIELD_CONTROL :
				control = createTextFieldControl(container, categoryAttribute);
				break;
			case LIST_BOX_CONTROL :
				control = createListBoxControl(container, categoryAttribute, createPermissibleValuesList(entity, attributeName,
						permissibleValueList[0]));
				break;
			case DATE_PICKER_CONTROL :
				control = createDatePickerControl(container, categoryAttribute);
				break;
			case FILE_UPLOAD_CONTROL :
				control = createFileUploadControl(container, categoryAttribute);
				break;
			case TEXT_AREA_CONTROL :
				control = createTextAreaControl(container, categoryAttribute);
				break;
			case RADIO_BUTTON_CONTROL :
				control = createRadioButtonControl(container, categoryAttribute, createPermissibleValuesList(entity, attributeName,
						permissibleValueList[0]));
				break;
			case CHECK_BOX_CONTROL :
				control = createCheckBoxControl(container, categoryAttribute);
				break;
		}

		control.setCaption(controlCaption);

		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);
		return control;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setParentCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setParentContainer(ContainerInterface parentContainer, ContainerInterface childContainer)
	{
		CategoryEntityInterface parentCategoryEntity = (CategoryEntity) parentContainer.getAbstractEntity();
		CategoryEntityInterface childCategoryEntity = (CategoryEntity) childContainer.getAbstractEntity();
		childCategoryEntity.setParentCategoryEntity(parentCategoryEntity);

		childContainer.setBaseContainer(parentContainer);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.test.CategoryHelperInterface#associateCategoryContainers(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, java.util.List, int)
	 */
	public CategoryAssociationControlInterface associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer,
			List<String> associationNamesList, int noOfEntries) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		PathInterface path = factory.createPath();

		EntityManagerInterface entityManager = EntityManager.getInstance();

		int pathSequenceNumber = 1;

		for (String associationName : associationNamesList)
		{
			PathAssociationRelationInterface pathAssociationRelation = factory.createPathAssociationRelation();
			pathAssociationRelation.setPathSequenceNumber(pathSequenceNumber++);
			pathAssociationRelation.setAssociation(entityManager.getAssociationByName(associationName));

			pathAssociationRelation.setPath(path);
			path.addPathAssociationRelation(pathAssociationRelation);
		}

		CategoryEntityInterface sourceCategoryEntity = (CategoryEntityInterface) sourceContainer.getAbstractEntity();
		CategoryEntityInterface targetCategoryEntity = (CategoryEntityInterface) targetContainer.getAbstractEntity();

		// Add path information.
		addPathBetweenCategoryEntities(sourceCategoryEntity, targetCategoryEntity, path);
		targetCategoryEntity.setNumberOfEntries(noOfEntries);

		CategoryAssociationInterface categoryAssociation = associateCategoryEntities(sourceCategoryEntity, targetCategoryEntity, sourceCategoryEntity
				.getName()
				+ " to " + targetCategoryEntity.getName() + " category association");

		CategoryAssociationControlInterface categoryAssociationControl = createCategoryAssociationControl(sourceContainer, targetContainer,
				categoryAssociation, targetContainer.getCaption());

		return categoryAssociationControl;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.test.CategoryHelperInterface#getNextSequenceNumber(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
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

	/**
	 * @param sourceCategoryEntity source category entity 
	 * @param targetCategoryEntity target category entity 
	 * @param path path information between the category entities
	 */
	private void addPathBetweenCategoryEntities(CategoryEntityInterface sourceCategoryEntity, CategoryEntityInterface targetCategoryEntity,
			PathInterface path)
	{
		targetCategoryEntity.setPath(path);
		sourceCategoryEntity.addChildCategory(targetCategoryEntity);
		targetCategoryEntity.setParentCategoryEntity(sourceCategoryEntity);
	}

	/**
	 * Method associates the source and the target category entity
	 * @param sourceCategoryEntity source category entity 
	 * @param targetCategoryEntity target category entity 
	 * @param name name of the category association
	 * @return CategoryAssociationInterface category association object
	 */
	private CategoryAssociationInterface associateCategoryEntities(CategoryEntityInterface sourceCategoryEntity,
			CategoryEntityInterface targetCategoryEntity, String name)
	{
		CategoryAssociationInterface categoryAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
		categoryAssociation.setName(name);
		categoryAssociation.setTargetCategoryEntity(targetCategoryEntity);
		categoryAssociation.setCategoryEntity(sourceCategoryEntity);

		sourceCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation);

		return categoryAssociation;
	}

	/**
	 * Method creates the association between the given parent and the target container 
	 * @param parentContainer main form
	 * @param targetContainer sub form
	 * @param categoryAssociation association between category entities
	 * @param caption name to be displayed on UI
	 * @return CategoryAssociationControlInterface category association control object
	 */
	private CategoryAssociationControlInterface createCategoryAssociationControl(ContainerInterface parentContainer,
			ContainerInterface targetContainer, CategoryAssociationInterface categoryAssociation, String caption)
	{
		CategoryAssociationControlInterface categoryAssociationControl = DomainObjectFactory.getInstance().createCategoryAssociationControl();
		categoryAssociationControl.setCaption(caption);
		categoryAssociationControl.setContainer(targetContainer);
		categoryAssociationControl.setBaseAbstractAttribute(categoryAssociation);
		categoryAssociationControl.setSequenceNumber(getNextSequenceNumber(parentContainer));
		categoryAssociationControl.setParentContainer((Container) parentContainer);

		parentContainer.addControl(categoryAssociationControl);

		return categoryAssociationControl;
	}

	/**
	 * @param abstractEntity category entity
	 * @return container object for category entity
	 */
	private ContainerInterface createContainer(AbstractEntityInterface abstractEntity, String caption)
	{
		ContainerInterface container = DomainObjectFactory.getInstance().createContainer();
		if(caption == null)
		{
			caption = abstractEntity.getName() + " category container";
		}
		container.setCaption(caption);
		container.setAbstractEntity(abstractEntity);
		container.setMainTableCss("formRequiredLabel");
		container.setRequiredFieldIndicatior("*");
		container.setRequiredFieldWarningMessage("indicates mandatory fields.");

		abstractEntity.addContainer(container);

		return container;
	}

	/**
	 * 
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @return text field object
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
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @param permissibleValues list of permissible values
	 * @return list box object
	 */
	private ListBoxInterface createListBoxControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute,
			List<PermissibleValueInterface> permissibleValues)
	{
		ListBoxInterface listBox = DomainObjectFactory.getInstance().createListBox();
		listBox.setCaption(baseAbstractAttribute.getName());
		listBox.setBaseAbstractAttribute(baseAbstractAttribute);
		listBox.setSequenceNumber(getNextSequenceNumber(container));
		listBox.setParentContainer((Container) container);

		container.addControl(listBox);

		UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
		for (PermissibleValueInterface pv : permissibleValues)
		{
			userDefinedDE.addPermissibleValue(pv);
		}
		((CategoryAttribute) baseAbstractAttribute).setDataElement(userDefinedDE);
		AttributeTypeInformationInterface attributeTypeInformation = ((CategoryAttribute) baseAbstractAttribute).getAttribute()
				.getAttributeTypeInformation();
		((CategoryAttribute) baseAbstractAttribute).setDefaultValue(attributeTypeInformation.getDefaultValue());
		return listBox;
	}

	/**
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @return date picker object
	 */
	private DatePickerInterface createDatePickerControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		DatePickerInterface datePicker = DomainObjectFactory.getInstance().createDatePicker();
		datePicker.setCaption(baseAbstractAttribute.getName());
		datePicker.setBaseAbstractAttribute(baseAbstractAttribute);
		datePicker.setSequenceNumber(getNextSequenceNumber(container));
		datePicker.setParentContainer((Container) container);
		datePicker.setDateValueType(ProcessorConstants.DATE_ONLY_FORMAT);

		container.addControl(datePicker);

		return datePicker;
	}

	/**
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @return file upload object
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
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @return text area object
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
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @param permissibleValues list of permissible values
	 * @return RadioButtonInterface radio button object
	 */
	private RadioButtonInterface createRadioButtonControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute,
			List<PermissibleValueInterface> permissibleValues)
	{
		RadioButtonInterface radioButton = DomainObjectFactory.getInstance().createRadioButton();
		radioButton.setCaption(baseAbstractAttribute.getName());
		radioButton.setBaseAbstractAttribute(baseAbstractAttribute);
		radioButton.setSequenceNumber(getNextSequenceNumber(container));
		radioButton.setParentContainer((Container) container);

		UserDefinedDEInterface userDefinedDE = DomainObjectFactory.getInstance().createUserDefinedDE();
		for (PermissibleValueInterface pv : permissibleValues)
		{
			userDefinedDE.addPermissibleValue(pv);
		}

		((CategoryAttribute) baseAbstractAttribute).setDataElement(userDefinedDE);

		container.addControl(radioButton);

		return radioButton;
	}

	/**
	 * @param container category entity container
	 * @param baseAbstractAttribute category attribute
	 * @return check box object
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
	 * This method creates a list of permissible values for a category attribute
	 * @param entity entity which contains attribute by the given name
	 * @param attributeName name of the attribute
	 * @param desiredPermissibleValues subset of permissible values for this category attribute
	 * @return list of permissible values for category attribute
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public List<PermissibleValueInterface> createPermissibleValuesList(EntityInterface entity, String attributeName,
			List<String> desiredPermissibleValues) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<PermissibleValueInterface> permissibleValues = null;
		
		try
		{
			AttributeInterface attribute = entity.getAttributeByName(attributeName);
			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
			UserDefinedDEInterface userDefinedDE = (UserDefinedDE) attributeTypeInformation.getDataElement();
			
			if (userDefinedDE == null || userDefinedDE.getPermissibleValueCollection() == null || userDefinedDE.getPermissibleValueCollection().size() == 0)
			{
				permissibleValues = addNewPermissibleValues(attributeTypeInformation,desiredPermissibleValues);
			}
			else
			{
				permissibleValues = getSubsetOfPermissibleValues(attribute,desiredPermissibleValues);
			}
		}
		catch(ParseException parseException)
		{
			throw new DynamicExtensionsSystemException("Parse Exception",parseException);
		}
		return permissibleValues;
	}

	/**
	 * 
	 * @param attributeTypeInformation
	 * @param desiredPermissibleValues
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 */
	private List<PermissibleValueInterface> getSubsetOfPermissibleValues(AttributeInterface attributeInterface, List<String> desiredPermissibleValues) throws DynamicExtensionsApplicationException 
	 {
		List<PermissibleValueInterface> permissibleValues = new ArrayList<PermissibleValueInterface>();
		
		AttributeTypeInformationInterface attributeTypeInformation = attributeInterface.getAttributeTypeInformation();
		UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) attributeTypeInformation.getDataElement();
		
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		
		if (categoryManager.isPermissibleValuesSubsetValid(userDefinedDE, desiredPermissibleValues))
		{
			permissibleValues = new ArrayList<PermissibleValueInterface>();
			for (PermissibleValueInterface pv : userDefinedDE.getPermissibleValueCollection())
			{
				if (desiredPermissibleValues.contains(pv.getValueAsObject().toString()))
				{
					permissibleValues.add(pv);
				}
			}
		}
		else
		{
			throw new DynamicExtensionsApplicationException("Invalid subset of persmissible values. Original set of permissible values for the attribute "+ attributeInterface.getName() +" of the entity "+attributeInterface.getEntity().getName() + "is different.");
		}
		return permissibleValues;
		
	}

	/**
	 * 
	 * @param attributeTypeInformation
	 * @param desiredPermissibleValues
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws ParseException 
	 */
	private List<PermissibleValueInterface> addNewPermissibleValues(AttributeTypeInformationInterface attributeTypeInformation, List<String> desiredPermissibleValues) throws DynamicExtensionsSystemException, ParseException {
		
		List<PermissibleValueInterface> permissibleValues = new ArrayList<PermissibleValueInterface>();
		PermissibleValueInterface permissibleValueInterface = null;
			
		for(String value : desiredPermissibleValues)
		{
			permissibleValueInterface = attributeTypeInformation.getPermissibleValueForString(value);
			permissibleValues.add(permissibleValueInterface);
		}
		return permissibleValues;
	}
}

package edu.common.dynamicextensions.entitymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.util.Utility;

/**
 * This Class is a mock class to test EntityManager
 * @author chetan_patil
 */
public class MockEntityManager
{

	int identifier = 1;
	int sequence = 1;

	/**
	 * This method returns the manually created Entities.
	 * 
	 * @return The Collection of Entities
	 * @throws DynamicExtensionsApplicationException If Entities can't be created
	 */
	@SuppressWarnings("unchecked")
	public Collection getAllEntities() throws DynamicExtensionsApplicationException
	{
		/**
		 * This is dummy Entity Class
		 */
		final class DummyEntity
		{

			private String entityName;
			Date createdDate;

			/**
			 * Constructor with arguments
			 * @param entityName Name of an Entity
			 * @param createdDate Date of Entity creation
			 */
			DummyEntity(String entityName, Date createdDate)
			{
				this.entityName = entityName;
				this.createdDate = createdDate;
			}

		}

		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		Collection entityInterfaceCollection = new ArrayList();
		EntityInterface entityInterface = null;

		/* Create dummy entities */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DummyEntity[] dummyEntities = null;
		try
		{
			dummyEntities = new DummyEntity[]{
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-15 14:15:20")),
					new DummyEntity("Health Annotation", dateFormat.parse("2006-10-16 14:16:21")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-17 14:17:22")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-18 14:18:23")),
					new DummyEntity("Pathology Entity", dateFormat.parse("2006-10-19 14:19:24")),};
		}
		catch (ParseException parseException)
		{
			throw new DynamicExtensionsApplicationException("Cannot create Dummy Entities.",
					parseException);
		}

		/* Populate all dummy entities into a Collection */
		for (int i = 0; i < dummyEntities.length; i++)
		{
			entityInterface = domainObjectFactory.createEntity();

			entityInterface.setName(dummyEntities[i].entityName);
			entityInterface.setCreatedDate(dummyEntities[i].createdDate);

			entityInterfaceCollection.add(entityInterface);
		}

		return entityInterfaceCollection;
	}

	/**
	 * This method returns a dummy Container instance populated with dummy
	 * Controls, Entity and its attributes.	 * 
	 * @param containerName Name of the Conatiner
	 * @return Container instance
	 * @throws DynamicExtensionsApplicationException On failure to create Container instance
	 */
	public ContainerInterface getContainer(String containerName)
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		ContainerInterface containerInterface = null;
		ControlInterface controlInterface = null;
		EntityInterface entityInterface = null;

		containerInterface = domainObjectFactory.createContainer();
		containerInterface.setButtonCss("actionButton");
		containerInterface.setCaption("DummyContainer");
		containerInterface.setMainTableCss("formRequiredLabel");
		containerInterface.setRequiredFieldIndicatior("*");
		containerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
		containerInterface.setTitleCss("formTitle");

		entityInterface = initializeEntity();
		containerInterface.setEntity(entityInterface);

		Collection abstractAttributeCollection = entityInterface.getAbstractAttributeCollection();
		Iterator abstractAttributeCollectionIterator = abstractAttributeCollection.iterator();
		while (abstractAttributeCollectionIterator.hasNext())
		{
			AttributeInterface attributeInterface = (AttributeInterface) abstractAttributeCollectionIterator
					.next();
			if (attributeInterface.getName().equals("name"))
			{
				controlInterface = initializeTextField(attributeInterface);
			}
			else if (attributeInterface.getName().equals("description"))
			{
				controlInterface = initializeTextArea(attributeInterface);
			}
			else if (attributeInterface.getName().equals("dateOfJoining"))
			{
				controlInterface = initializeDatePicker(attributeInterface);
			}
			else if (attributeInterface.getName().equals("gender"))
			{
				controlInterface = initializeComboBox(attributeInterface);
			}
			containerInterface.addControl(controlInterface);
		}
		return containerInterface;
	}

	/**
	 * This method creates the populated dummy Entity instance
	 * 
	 * @return Manually created dummy Entity along with its attributes
	 * @throws DynamicExtensionsApplicationException On failure to create Entity 
	 */
	public EntityInterface initializeEntity() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AbstractAttributeInterface abstractAttributeInterface = null;

		EntityInterface dummyEntity = domainObjectFactory.createEntity();

		dummyEntity.setName("Employee");
		dummyEntity.setCreatedDate(new Date());
		dummyEntity.setDescription("This is a dummy entity");
		dummyEntity.setLastUpdated(dummyEntity.getCreatedDate());

		/* Date of Joining attribute */
		abstractAttributeInterface = initializeDateAttribute();
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		/* Gender attribute with its Permissible values */
		abstractAttributeInterface = initializeStringAttribute("gender", "Male");
		((AttributeInterface) abstractAttributeInterface).getAttributeTypeInformation()
				.setDataElement(initializeDataElement());
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		/* Description attribute */
		abstractAttributeInterface = initializeStringAttribute("description",
				"William James Bill Murray is an Academy Award-nominated");
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		return dummyEntity;
	}

	/**
	 * This method creates populated dummy StringAttributeTypeInformation 
	 * @param attributeName Name of the Attribute
	 * @param defaultValue Default value of the Attribute
	 * @return Manually created StringAttributeTypeInformation instance
	 */
	public AttributeInterface initializeStringAttribute(String attributeName,
			String stringDefaultValue)
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AttributeInterface stringAttribute = domainObjectFactory.createStringAttribute();
		StringValueInterface defaultValue = domainObjectFactory.createStringValue();

		defaultValue.setValue(stringDefaultValue);
		stringAttribute.setCreatedDate(new Date());
		stringAttribute.setDescription("This is a dummy StringAttributeTypeInformation");
		stringAttribute.setIsCollection(new Boolean(false));
		stringAttribute.setIsIdentified(new Boolean(true));
		stringAttribute.setIsPrimaryKey(new Boolean(false));
		stringAttribute.setLastUpdated(stringAttribute.getCreatedDate());
		stringAttribute.setName(attributeName);

		StringAttributeTypeInformation stringTypeInfo = (StringAttributeTypeInformation) stringAttribute
				.getAttributeTypeInformation();

		stringTypeInfo.setDefaultValue(defaultValue);
		stringTypeInfo.setSize(new Integer(stringDefaultValue.length()));

		return stringAttribute;
	}

	/**
	 * This method creates populated dummy DateAttributeTypeInformation
	 * 
	 * @return Manually created DateAttributeTypeInformation instance
	 * @throws DynamicExtensionsApplicationException On failure to create Date
	 */
	public AttributeInterface initializeDateAttribute()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AttributeInterface dateAttribute = domainObjectFactory.createDateAttribute();
		DateValueInterface defaultValue = domainObjectFactory.createDateValue();

		Date dateOfJoining = null;
		/*	try
		 {
		 dateOfJoining = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1983-10-21 14:15:20");
		 }
		 catch (ParseException parseException)
		 {
		 throw new DynamicExtensionsApplicationException("Cannot create DateAttributeTypeInformation", parseException);
		 }*/

		dateAttribute.setCreatedDate(new Date());
		dateAttribute.setDescription("This is a dummy DateAttributeTypeInformation");
		dateAttribute.setIsCollection(new Boolean(false));
		dateAttribute.setIsIdentified(new Boolean(true));
		dateAttribute.setIsPrimaryKey(new Boolean(false));
		dateAttribute.setLastUpdated(dateAttribute.getCreatedDate());
		dateAttribute.setName("dateOfJoining");
		//SimpleDateFormat dateFormat = new SimpleDateFormat(Variables.datePattern);

		try
		{
			defaultValue.setValue(Utility
					.parseDate("11-11-1982", Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dateAttribute.getAttributeTypeInformation().setDefaultValue(defaultValue);

		return dateAttribute;
	}

	/**
	 * @param abstractAttributeInterface Assosiated Attribute of the Entity
	 * @return Manually created TextField control instance
	 */
	public ControlInterface initializeTextField(
			AbstractAttributeInterface abstractAttributeInterface)
	{
		final int COLUMNS = 50;
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		TextFieldInterface textFieldInterface = domainObjectFactory.createTextField();

		textFieldInterface.setAbstractAttribute(abstractAttributeInterface);

		textFieldInterface.setColumns(new Integer(COLUMNS));
		textFieldInterface.setIsPassword(new Boolean(false));

		textFieldInterface.setCaption("Employee Name");
		textFieldInterface.setSequenceNumber(new Integer(sequence++));
		textFieldInterface.setIsHidden(new Boolean(false));
		textFieldInterface.setCssClass("formField");
		textFieldInterface.setTooltip("This is Name of the Employee.");

		return textFieldInterface;
	}

	/**
	 * @param abstractAttributeInterface Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeTextArea(AbstractAttributeInterface abstractAttributeInterface)
	{
		final int COLUMNS = 50;
		final int ROWS = 6;
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		TextAreaInterface textAreaInterface = domainObjectFactory.createTextArea();

		textAreaInterface.setAbstractAttribute(abstractAttributeInterface);

		textAreaInterface.setColumns(new Integer(COLUMNS));
		textAreaInterface.setRows(new Integer(ROWS));

		textAreaInterface.setCaption("Description");
		textAreaInterface.setSequenceNumber(new Integer(sequence++));
		textAreaInterface.setIsHidden(new Boolean(false));
		textAreaInterface.setCssClass("formField");
		textAreaInterface.setTooltip("This is Description of the Employee.");

		return textAreaInterface;
	}

	/**
	 * @param abstractAttributeInterface Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeDatePicker(
			AbstractAttributeInterface abstractAttributeInterface)
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		DatePickerInterface datePickerInterface = domainObjectFactory.createDatePicker();

		datePickerInterface.setAbstractAttribute(abstractAttributeInterface);

		datePickerInterface.setCaption("Date of Joining");
		datePickerInterface.setSequenceNumber(new Integer(sequence++));
		datePickerInterface.setIsHidden(new Boolean(false));
		datePickerInterface.setCssClass("formField");
		datePickerInterface.setTooltip("This is Date of Joining of the Employee.");

		return datePickerInterface;
	}

	/**
	 * @param abstractAttributeInterface Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeComboBox(AbstractAttributeInterface abstractAttributeInterface)
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		ComboBoxInterface comboBoxInterface = domainObjectFactory.createComboBox();

		comboBoxInterface.setAbstractAttribute(abstractAttributeInterface);

		comboBoxInterface.setCaption("Gender");
		comboBoxInterface.setSequenceNumber(new Integer(sequence));
		comboBoxInterface.setIsHidden(new Boolean(false));
		comboBoxInterface.setCssClass("formField");
		comboBoxInterface.setTooltip("This is Gender of the Employee.");

		return comboBoxInterface;
	}

	/**
	 * @return Manually created DataElement instance
	 */
	public DataElementInterface initializeDataElement()
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		UserDefinedDEInterface userDefinedDEInterface = domainObjectFactory.createUserDefinedDE();
		//new UserDefinedDE();

		StringValueInterface stringValueInterface = null;

		/* First Value */
		stringValueInterface = domainObjectFactory.createStringValue();
		stringValueInterface.setValue("Male");
		userDefinedDEInterface.addPermissibleValue(stringValueInterface);

		/* Second Value */
		stringValueInterface = domainObjectFactory.createStringValue();
		stringValueInterface.setValue("Female");
		userDefinedDEInterface.addPermissibleValue(stringValueInterface);

		return userDefinedDEInterface;
	}

	/**
	 * This method creates the populated dummy Entity instance
	 * 
	 * @return Manually created dummy Entity along with its attributes
	 * @throws DynamicExtensionsApplicationException On failure to create Entity 
	 */
	public EntityGroupInterface initializeEntityGroup()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroupInterface = domainObjectFactory.createEntityGroup();
		entityGroupInterface.setName("Test group1");
		entityGroupInterface.setLongName("Test long name1");
		entityGroupInterface.setShortName("Test short name1");
		entityGroupInterface.setDescription("Test description1");
		entityGroupInterface.setVersion("1");

		EntityInterface entityInterface = initializeEntity();
		entityGroupInterface.addEntity(entityInterface);

		return entityGroupInterface;
	}

	/**
	 * 
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public SemanticPropertyInterface initializeSemanticProperty()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		SemanticPropertyInterface semanticPropertyInterface = domainObjectFactory
				.createSemanticProperty();
		semanticPropertyInterface.setConceptCode("c1");
		semanticPropertyInterface.setTerm("t1");
		semanticPropertyInterface.setThesaurasName("th1");

		return semanticPropertyInterface;
	}

}

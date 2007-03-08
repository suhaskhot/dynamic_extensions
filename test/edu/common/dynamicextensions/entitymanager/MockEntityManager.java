
package edu.common.dynamicextensions.entitymanager;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.databaseproperties.DatabaseProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.DatabasePropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
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

		dummyEntity.setName("Entity1");
		dummyEntity.setCreatedDate(new Date());
		dummyEntity.setDescription("This is a dummy entity");
		dummyEntity.setLastUpdated(dummyEntity.getCreatedDate());

		// Attribute 1
		abstractAttributeInterface = initializeDateAttribute();
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);

		// Attribute 2
		abstractAttributeInterface = initializeStringAttribute("gender", "Male");
		((AttributeInterface) abstractAttributeInterface).getAttributeTypeInformation()
				.setDataElement(initializeDataElement());
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);

		// Attribute 3
		abstractAttributeInterface = initializeStringAttribute("description",
				"William James Bill Murray is an Academy Award-nominated");
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		
		TablePropertiesInterface  tablePropertiesInterface = new TableProperties();
		tablePropertiesInterface.setName("DE_TABLE1");
		
		dummyEntity.setTableProperties(tablePropertiesInterface);

		return dummyEntity;
	}

	public EntityInterface initializeEntity2() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AbstractAttributeInterface abstractAttributeInterface = null;

		EntityInterface dummyEntity = domainObjectFactory.createEntity();

		dummyEntity.setName("Entity2");
		dummyEntity.setCreatedDate(new Date());
		dummyEntity.setDescription("This is a dummy entity");
		dummyEntity.setLastUpdated(dummyEntity.getCreatedDate());

		// Attribute 1
		abstractAttributeInterface = initializeStringAttribute("name", "");
		((AttributeInterface) abstractAttributeInterface).getAttributeTypeInformation()
				.setDataElement(initializeDataElement());
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);

		// Attribute 2
		abstractAttributeInterface = initializeStringAttribute("address", "");
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		
		TablePropertiesInterface  tablePropertiesInterface = new TableProperties();
		tablePropertiesInterface.setName("DE_TABLE2");
		
		dummyEntity.setTableProperties(tablePropertiesInterface);

		return dummyEntity;
	}

	public EntityInterface initializeEntity3() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AbstractAttributeInterface abstractAttributeInterface = null;

		EntityInterface dummyEntity = domainObjectFactory.createEntity();

		dummyEntity.setName("Entity3");
		dummyEntity.setCreatedDate(new Date());
		dummyEntity.setDescription("This is a dummy entity");
		dummyEntity.setLastUpdated(dummyEntity.getCreatedDate());

		// Attribute 1
		abstractAttributeInterface = initializeLongAttribute();
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);

		// Attribute 2
		abstractAttributeInterface = initializeDoubleAttribute();
		((AttributeInterface) abstractAttributeInterface).getAttributeTypeInformation()
				.setDataElement(initializeDataElement());
		dummyEntity.addAbstractAttribute(abstractAttributeInterface);
		abstractAttributeInterface.setEntity(dummyEntity);
		
		TablePropertiesInterface  tablePropertiesInterface = new TableProperties();
		tablePropertiesInterface.setName("DE_TABLE3");
		
		dummyEntity.setTableProperties(tablePropertiesInterface);

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

		dateAttribute.setCreatedDate(new Date());
		dateAttribute.setDescription("This is a dummy DateAttributeTypeInformation");
		dateAttribute.setIsCollection(new Boolean(false));
		dateAttribute.setIsIdentified(new Boolean(true));
		dateAttribute.setIsPrimaryKey(new Boolean(false));
		dateAttribute.setLastUpdated(dateAttribute.getCreatedDate());
		dateAttribute.setName("dateOfJoining");

		try
		{
			defaultValue.setValue(Utility
					.parseDate("11-11-1982", Constants.DATE_PATTERN_MM_DD_YYYY));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		dateAttribute.getAttributeTypeInformation().setDefaultValue(defaultValue);

		return dateAttribute;
	}

	/**
	 * This method creates populated dummy DateAttributeTypeInformation
	 * 
	 * @return Manually created DateAttributeTypeInformation instance
	 * @throws DynamicExtensionsApplicationException On failure to create Date
	 */
	public AttributeInterface initializeDoubleAttribute()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AttributeInterface doubleAttribute = domainObjectFactory.createDoubleAttribute();

		doubleAttribute.setCreatedDate(new Date());
		doubleAttribute.setDescription("This is a dummy DoubleAttributeTypeInformation");
		doubleAttribute.setIsCollection(new Boolean(false));
		doubleAttribute.setIsIdentified(new Boolean(true));
		doubleAttribute.setIsPrimaryKey(new Boolean(false));
		doubleAttribute.setLastUpdated(doubleAttribute.getCreatedDate());
		doubleAttribute.setName("Height");

		return doubleAttribute;
	}

	/**
	 * This method creates populated dummy DateAttributeTypeInformation
	 * 
	 * @return Manually created DateAttributeTypeInformation instance
	 * @throws DynamicExtensionsApplicationException On failure to create Date
	 */
	public AttributeInterface initializeLongAttribute()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		AttributeInterface longAttribute = domainObjectFactory.createDoubleAttribute();

		longAttribute.setCreatedDate(new Date());
		longAttribute.setDescription("This is a dummy LongAttributeTypeInformation");
		longAttribute.setIsCollection(new Boolean(false));
		longAttribute.setIsIdentified(new Boolean(true));
		longAttribute.setIsPrimaryKey(new Boolean(false));
		longAttribute.setLastUpdated(longAttribute.getCreatedDate());
		longAttribute.setName("Age");

		return longAttribute;
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
		entityGroupInterface.setName("Test Group");
		entityGroupInterface.setLongName("Test long name1");
		entityGroupInterface.setShortName("Test short name1");
		entityGroupInterface.setDescription("Test description1");
		entityGroupInterface.setVersion("1");

		EntityInterface entity1 = initializeEntity();

		entityGroupInterface.addEntity(entity1);

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

	/**
	 * This method creates the populated dummy Entity instance
	 * 
	 * @return Manually created dummy Entity along with its attributes
	 * @throws DynamicExtensionsApplicationException On failure to create Entity 
	 */
	public EntityGroupInterface initializeEntityGroupForInheritance()
			throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroupInterface = domainObjectFactory.createEntityGroup();
		entityGroupInterface.setName("Test Group");
		entityGroupInterface.setLongName("Test long name1");
		entityGroupInterface.setShortName("Test short name1");
		entityGroupInterface.setDescription("Test description1");
		entityGroupInterface.setVersion("1");

		EntityInterface entity1 = initializeEntity();

		EntityInterface entity2 = initializeEntity2();
		entity2.setParentEntity(entity1);

		EntityInterface entity3 = initializeEntity3();
		entity3.setParentEntity(entity2);

		entityGroupInterface.addEntity(entity1);
		entityGroupInterface.addEntity(entity2);
		entityGroupInterface.addEntity(entity3);

		return entityGroupInterface;
	}

}

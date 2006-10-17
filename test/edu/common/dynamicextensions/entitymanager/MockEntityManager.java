package edu.common.dynamicextensions.entitymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DateAttribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.StringAttribute;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domain.userinterface.ComboBox;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domain.userinterface.DatePicker;
import edu.common.dynamicextensions.domain.userinterface.TextArea;
import edu.common.dynamicextensions.domain.userinterface.TextField;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * This Class is a mock class to test EntityManager
 * @author chetan_patil
 * @version 1.0
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
	public Collection getAllEntities()
			throws DynamicExtensionsApplicationException 
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

		Collection entityCollection = new ArrayList();
		Entity entity = null;

		/* Create dummy entities */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DummyEntity[] dummyEntities = null;
		try 
		{
			dummyEntities = new DummyEntity[]{
					new DummyEntity("Pathology Entity", dateFormat
							.parse("2006-10-15 14:15:20")),
					new DummyEntity("Health Annotation", dateFormat
							.parse("2006-10-16 14:16:21")),
					new DummyEntity("Pathology Entity", dateFormat
							.parse("2006-10-17 14:17:22")),
					new DummyEntity("Pathology Entity", dateFormat
							.parse("2006-10-18 14:18:23")),
					new DummyEntity("Pathology Entity", dateFormat
							.parse("2006-10-19 14:19:24")),};
		} 
		catch (ParseException parseException) 
		{
			throw new DynamicExtensionsApplicationException("Cannot create Dummy Entities.", parseException);
		}

		/* Populate all dummy entities into a Collection */
		for (int i = 0; i < dummyEntities.length; i++) 
		{
			entity = new Entity();

			entity.setName(dummyEntities[i].entityName);
			entity.setCreatedDate(dummyEntities[i].createdDate);

			entityCollection.add(entity);
		}

		return entityCollection;
	}

	/**
	 * This method returns a Dummy Entity populated with a Dummy
	 * StringAttribute.
	 * 
	 * @return Entity instance
	 */
	public Entity createEntity() 
	{
		Entity entity = new Entity();
		entity.setId(new Long(1));
		entity.setDescription("Dummy description");
		entity.setName("EntityOne");
		Collection abstractAttributeCollection = new HashSet();
		StringAttribute strAttr = new StringAttribute();
		strAttr.setDescription("description");
		strAttr.setEntity(entity);
		abstractAttributeCollection.add(strAttr);
		entity.setAbstractAttributeCollection(abstractAttributeCollection);
		return entity;
	}

	/**
	 * This method returns a dummy Container instance populated with dummy
	 * Controls, Entity and its attributes.	 * 
	 * @param containerName Name of the Conatiner
	 * @return Container instance
	 * @throws DynamicExtensionsApplicationException On failure to create Container instance
	 */
	public ContainerInterface getContainer(String containerName) throws DynamicExtensionsApplicationException 
	{
		ContainerInterface containerInterface = null;
		ControlInterface controlInterface = null;
		EntityInterface entityInterface = null;
		
		containerInterface = new Container();
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
		while(abstractAttributeCollectionIterator.hasNext())
		{
			Attribute attribute = (Attribute)abstractAttributeCollectionIterator.next();
			if(attribute.getName().equals("name"))
			{
				controlInterface = initializeTextField(attribute);
			}
			else if(attribute.getName().equals("description"))
			{
				controlInterface = initializeTextArea(attribute);
			}
			else if(attribute.getName().equals("dateOfJoining"))
			{
				controlInterface = initializeDatePicker(attribute);
			}
			else if(attribute.getName().equals("gender"))
			{
				controlInterface = initializeComboBox(attribute);
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
		Attribute attribute = null;
		Entity dummyEntity = new Entity();

		dummyEntity.setName("Employee");
		dummyEntity.setId(new Long(1L));
		dummyEntity.setCreatedDate(new Date());
		dummyEntity.setDescription("This is a dummy entity");
		dummyEntity.setLastUpdated(dummyEntity.getCreatedDate());

		/* Name attribute */
		attribute = initializeStringAttribute("name", "Daniel Pearl");
		dummyEntity.addAbstractAttribute(attribute);
	
		/* Date of Joining attribute */
		attribute = initializeDateAttribute();
		dummyEntity.addAbstractAttribute(attribute);

		/* Gender attribute with its Permissible values */
		attribute = initializeStringAttribute("gender", "Male");
		attribute.setDataElement(initializeDataElement());
		dummyEntity.addAbstractAttribute(attribute);
		
		/* Description attribute */
		attribute = initializeStringAttribute("description",
				"William James Bill Murray (born September 21, 1950) is an Academy Award-nominated, "
				+ "Emmy-winning and Golden Globe-winning American comedian and actor. He is most famous "
				+ "for his comedic roles in Groundhog Day, Caddyshack, Ghostbusters, and What About Bob?. "
				+ "He has gained further acclaim for recent dramatic roles, such as in the acclaimed films "
				+ "Lost In Translation and Broken Flowers.");
		dummyEntity.addAbstractAttribute(attribute);
	
		return dummyEntity;
	}

	/**
	 * This method creates populated dummy StringAttribute 
	 * @param attributeName Name of the Attribute
	 * @param defaultValue Default value of the Attribute
	 * @return Manually created StringAttribute instance
	 */
	public Attribute initializeStringAttribute(String attributeName, String defaultValue) 
	{
		StringAttribute stringAttribute = new StringAttribute();

		stringAttribute.setCreatedDate(new Date());
		stringAttribute.setDescription("This is a dummy StringAttribute");
		stringAttribute.setId(new Long(identifier++));
		stringAttribute.setIsCollection(new Boolean(false));
		stringAttribute.setIsIdentified(new Boolean(true));
		stringAttribute.setIsPrimaryKey(new Boolean(false));
		stringAttribute.setLastUpdated(stringAttribute.getCreatedDate());
		stringAttribute.setName(attributeName);

		stringAttribute.setDefaultValue(defaultValue);
		stringAttribute.setSize(new Integer(defaultValue.length()));

		return stringAttribute;
	}

	/**
	 * This method creates populated dummy DateAttribute
	 * 
	 * @return Manually created DateAttribute instance
	 * @throws DynamicExtensionsApplicationException On failure to create Date
	 */
	public Attribute initializeDateAttribute() throws DynamicExtensionsApplicationException
	{
		DateAttribute dateAttribute = new DateAttribute();
		Date dateOfJoining = null;
		try 
		{
			dateOfJoining = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1983-10-21 14:15:20");
		} 
		catch (ParseException parseException) 
		{
			throw new DynamicExtensionsApplicationException("Cannot create DateAttribute", parseException);
		}

		dateAttribute.setCreatedDate(new Date());
		dateAttribute.setDescription("This is a dummy DateAttribute");
		dateAttribute.setId(new Long(identifier++));
		dateAttribute.setIsCollection(new Boolean(false));
		dateAttribute.setIsIdentified(new Boolean(true));
		dateAttribute.setIsPrimaryKey(new Boolean(false));
		dateAttribute.setLastUpdated(dateAttribute.getCreatedDate());
		dateAttribute.setName("dateOfJoining");
		dateAttribute.setDefaultValue(dateOfJoining);

		return dateAttribute;
	}

	/**
	 * @param abstractAttribute Assosiated Attribute of the Entity
	 * @return Manually created TextField control instance
	 */
	public ControlInterface initializeTextField(AbstractAttribute abstractAttribute)
	{
		final int COLUMNS = 50;
		TextField textField = new TextField();
		
		textField.setAbstractAttribute(abstractAttribute);
		
		textField.setColumns(new Integer(COLUMNS));
		textField.setIsPassword(new Boolean(false));
		
		textField.setCaption("Employee Name");
		textField.setSequenceNumber(new Integer(sequence++));
		textField.setIsHidden(new Boolean(false));
		textField.setCssClass("formField");
		textField.setTooltip("This is Name of the Employee.");

		return textField;
	}
	
	/**
	 * @param abstractAttribute Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeTextArea(AbstractAttribute abstractAttribute)
	{
		final int COLUMNS = 50;
		final int ROWS = 6;
		TextArea textArea = new TextArea();
		
		textArea.setAbstractAttribute(abstractAttribute);
		
		textArea.setColumns(new Integer(COLUMNS));
		textArea.setRows(new Integer(ROWS));
		
		textArea.setCaption("Description");
		textArea.setSequenceNumber(new Integer(sequence++));
		textArea.setIsHidden(new Boolean(false));
		textArea.setCssClass("formField");
		textArea.setTooltip("This is Description of the Employee.");

		return textArea;
	}
	
	/**
	 * @param abstractAttribute Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeDatePicker(AbstractAttribute abstractAttribute)
	{
		DatePicker datePicker = new DatePicker();
		
		datePicker.setAbstractAttribute(abstractAttribute);
		
		datePicker.setCaption("Date of Joining");
		datePicker.setSequenceNumber(new Integer(sequence++));
		datePicker.setIsHidden(new Boolean(false));
		datePicker.setCssClass("formField");
		datePicker.setTooltip("This is Date of Joining of the Employee.");

		return datePicker;
	}
	
	/**
	 * @param abstractAttribute Assosiated Attribute of the Entity
	 * @return Manually created TextArea control instance
	 */
	public ControlInterface initializeComboBox(AbstractAttribute abstractAttribute)
	{
		ComboBox comboBox = new ComboBox();
		
		comboBox.setAbstractAttribute(abstractAttribute);
		
		comboBox.setCaption("Gender");
		comboBox.setSequenceNumber(new Integer(sequence));
		comboBox.setIsHidden(new Boolean(false));
		comboBox.setCssClass("formField");
		comboBox.setTooltip("This is Gender of the Employee.");

		return comboBox;
	}
	
	/**
	 * @return Manually created DataElement instance
	 */
	public DataElementInterface initializeDataElement()
	{
		UserDefinedDE userDefinedDE = new UserDefinedDE();
		
		Collection permissibleValueCollection = new HashSet();
		StringValue stringValue = null;
		
		/* First Value */
		stringValue = new StringValue();
		stringValue.setValue("Male");
		permissibleValueCollection.add(stringValue);
		
		/* Second Value */
		stringValue = new StringValue();
		stringValue.setValue("Female");
		permissibleValueCollection.add(stringValue);
		
		userDefinedDE.setPermissibleValueCollection(permissibleValueCollection);
		
		return userDefinedDE;
	}
	
}

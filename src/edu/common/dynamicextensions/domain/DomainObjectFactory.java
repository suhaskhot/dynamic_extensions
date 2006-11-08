
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domain.userinterface.CheckBox;
import edu.common.dynamicextensions.domain.userinterface.ComboBox;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domain.userinterface.DataGrid;
import edu.common.dynamicextensions.domain.userinterface.DatePicker;
import edu.common.dynamicextensions.domain.userinterface.ListBox;
import edu.common.dynamicextensions.domain.userinterface.RadioButton;
import edu.common.dynamicextensions.domain.userinterface.TextArea;
import edu.common.dynamicextensions.domain.userinterface.TextField;
import edu.common.dynamicextensions.domain.userinterface.View;
import edu.common.dynamicextensions.domain.validationrules.Rule;
import edu.common.dynamicextensions.domain.validationrules.RuleParameter;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.ByteArrayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.ByteArrayValueInterface;
import edu.common.dynamicextensions.domaininterface.CaDSRDEInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.FloatAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerAttributeInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongAttributeInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.ShortAttributeInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DataGridInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ViewInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;

/**
 * This is a singleton class which provides methods for generating domain objects.
 * For each domain object a create method is provided. 
 * @author sujay_narkar
 *
 */
public class DomainObjectFactory
{

	/**
	 * Domain Object Factory Instance
	 */
	private static DomainObjectFactory domainObjectFactory;

	/**
	 * Empty Constructor
	 */
	protected DomainObjectFactory()
	{
	}

	/**
	 * This method returns the instance of SegmentationDomainElementFactory.
	 * @return the instance of SegmentationDomainElementFactory.
	 */
	public static synchronized DomainObjectFactory getInstance()
	{
		if (domainObjectFactory == null)
		{
			domainObjectFactory = new DomainObjectFactory();
		}
		return domainObjectFactory;
	}

	/**
	 * This method creates an object of Entity.
	 * @return an instance of Entity.
	 */
	public EntityInterface createEntity()
	{
		Entity entity = new Entity();
		return entity;
	}

	/**
	 * This method creates an object of EntityGroup
	 * @return an instance of EntityGroup.
	 */
	public EntityGroupInterface createEntityGroup()
	{
		EntityGroup entityGroup = new EntityGroup();
		return entityGroup;
	}

	/**
	 * This method creates an object of Association
	 * @return an instance of Association.
	 */
	public AssociationInterface createAssociation()
	{
		Association association = new Association();
		return association;
	}

	/**
	 * This method creates an object of Role
	 * @return an instance of Role.
	 */
	public RoleInterface createRole()
	{
		Role role = new Role();
		return role;
	}

	/**
	 * This method creates an object of SemanticProperty.
	 * @return an instance of SemanticProperty.
	 */
	public SemanticPropertyInterface createSemanticProperty()
	{
		SemanticProperty semanticProperty = new SemanticProperty();
		return semanticProperty;
	}

	/**
	 * This method creates an object of ColunmProperties.
	 * @return an instance of ColumnProperties.
	 */
	public ColumnPropertiesInterface createColumnProperties()
	{
		ColumnProperties columnProperties = new ColumnProperties();
		return columnProperties;
	}

	/**
	 * This method creates an object of TableProperties.
	 * @return an instance of TableProperties.
	 */
	public TablePropertiesInterface createTableProperties()
	{
		TableProperties tableProperties = new TableProperties();
		return tableProperties;
	}

	/**
	 * This method creates an object of ConstraintProperties
	 * @return an instance of ConstraintProperties.
	 */
	public ConstraintPropertiesInterface createConstraintProperties()
	{
		ConstraintProperties constraintProperties = new ConstraintProperties();
		return constraintProperties;
	}

	/**
	 * This method creates an object of BooleanAttribute.
	 * @return an instance of BooleanAttribute.
	 */
	public BooleanAttributeInterface createBooleanAttribute()
	{
		BooleanAttribute booleanAttribute = new BooleanAttribute();
		return booleanAttribute;
	}

	/**
	 * This method creates an object of ByteArrayAttribute.
	 * @return an instance of ByteArrayAttribute.
	 */
	public ByteArrayAttributeInterface createByteArrayAttribute()
	{
		ByteArrayAttribute byteArrayAttribute = new ByteArrayAttribute();
		return byteArrayAttribute;
	}

	/**
	 * This method creates an object of ByteArrayValue.
	 * @return an instance of ByteArrayAttribute.
	 */
	public ByteArrayValueInterface createByteArrayValue()
	{
		ByteArrayValue byteArrayValue = new ByteArrayValue();
		return byteArrayValue;
	}

	/**
	 * This method creates an object of DateAttribute.
	 * @return an instance of DateAttribute.
	 */
	public DateAttributeInterface createDateAttribute()
	{
		DateAttribute dateAttribute = new DateAttribute();
		return dateAttribute;
	}

	/**
	 * This method creates an object of DoubleAttribute.
	 * @return an instance of DoubleAttribute.
	 */
	public DoubleAttributeInterface createDoubleAttribute()
	{
		DoubleAttribute doubleAttribute = new DoubleAttribute();
		return doubleAttribute;
	}

	/**
	 * This method creates an object of FloatAttribute.
	 * @return an instance of FloatAttribute.
	 */
	public FloatAttributeInterface createFloatAttribute()
	{
		FloatAttribute floatAttribute = new FloatAttribute();
		return floatAttribute;
	}

	/**
	 * This method creates an object of IntegerAttribute.
	 * @return an instance of IntegerAttribute.
	 */
	public IntegerAttributeInterface createIntegerAttribute()
	{
		IntegerAttribute integerAttribute = new IntegerAttribute();
		return integerAttribute;
	}

	/**
	 * This method creates an object of LongAttribute.
	 * @return an instance of LongAttribute.
	 */
	public LongAttributeInterface createLongAttribute()
	{
		LongAttribute longAttribute = new LongAttribute();
		return longAttribute;
	}

	/**
	 * This method creates an object of ShortAttribute
	 * @return an instance of ShortAttribute.
	 */
	public ShortAttributeInterface createShortAttribute()
	{
		ShortAttribute shortAttribute = new ShortAttribute();
		return shortAttribute;
	}

	/**
	 * This method creates an object of StringAttribute.
	 * @return an instance of StringAttribute.
	 */
	public StringAttributeInterface createStringAttribute()
	{
		StringAttribute stringAttribute = new StringAttribute();
		return stringAttribute;
	}

	/**
	 * This method creates an object of BooleanValue.
	 * @return an instance of BooleanValue.
	 */
	public BooleanValueInterface createBooleanValue()
	{
		BooleanValue booleanValue = new BooleanValue();
		return booleanValue;
	}

	/**
	 * This method creates an object of DateValue.
	 * @return an instance of DateValue.
	 */
	public DateValueInterface createDateValue()
	{
		DateValue dateValue = new DateValue();
		return dateValue;
	}

	/**
	 * This method creates an object of DoubleValue.
	 * @return an instance of DoubleValue.
	 */
	public DoubleValueInterface createDoubleValue()
	{
		DoubleValue doubleValue = new DoubleValue();
		return doubleValue;
	}

	/**
	 * This This method creates an object of FloatValue.
	 * @return an instance of FloatValue.
	 */
	public FloatValueInterface createFloatValue()
	{
		FloatValue floatValue = new FloatValue();
		return floatValue;
	}

	/**
	 * This method creates an object of IntegerValue.
	 * @return an instance of IntegerValue.
	 */
	public IntegerValueInterface createIntegerValue()
	{
		IntegerValue integerValue = new IntegerValue();
		return integerValue;
	}

	/**
	 * This method creates an object of LongValue.
	 * @return an instance of LongValue.
	 */
	public LongValueInterface createLongValue()
	{
		LongValue longValue = new LongValue();
		return longValue;
	}

	/**
	 * This method creates an object of ShortValue.
	 * @return an instance of ShortValue.
	 */
	public ShortValueInterface createShortValue()
	{
		ShortValue shortValue = new ShortValue();
		return shortValue;
	}

	/**
	 * This method creates an object of StringValue.
	 * @return an instance of StringValue.
	 */
	public StringValueInterface createStringValue()
	{
		StringValue stringValue = new StringValue();
		return stringValue;
	}

	/**
	 * This method creates an object of CheckBox.
	 * @return an instance of CheckBox.
	 */
	public CheckBoxInterface createCheckBox()
	{
		CheckBox checkBox = new CheckBox();
		return checkBox;
	}

	/**
	 * This method creates an object of ComboBox
	 * @return an instance of ComboBox.
	 */
	public ComboBoxInterface createComboBox()
	{
		ComboBox comboBox = new ComboBox();
		return comboBox;
	}

	/**
	 * This method creates an object of Container.
	 * @return an instance of Container.
	 */
	public ContainerInterface createContainer()
	{
		Container container = new Container();
		return container;
	}

	/**
	 * This method creates an object of DataGrid.
	 * @return an instance of DataGrid.
	 */
	public DataGridInterface createDataGrid()
	{
		DataGrid dataGrid = new DataGrid();
		return dataGrid;
	}

	/**
	 * This method creates an object of DatePicker.
	 * @return an instance of DatePicker.
	 */
	public DatePickerInterface createDatePicker()
	{
		DatePicker datePicker = new DatePicker();
		return datePicker;
	}

	/**
	 * This method creates an object of ListBox.
	 * @return an instance of ListBox.
	 */
	public ListBoxInterface createListBox()
	{
		ListBox listBox = new ListBox();
		return listBox;
	}

	/**
	 * This method creates an object of RadioButton.
	 * @return an instance of RadioButton.
	 */
	public RadioButtonInterface createRadioButton()
	{
		RadioButton radioButton = new RadioButton();
		return radioButton;
	}

	/**
	 * This method creates an object of TextArea. 
	 * @return an instance of TextArea.
	 */
	public TextAreaInterface createTextArea()
	{
		TextArea textArea = new TextArea();
		return textArea;
	}

	/**
	 * This method creates an object of TextField.
	 * @return an instance of TextField.
	 */
	public TextFieldInterface createTextField()
	{
		TextField textField = new TextField();
		return textField;
	}

	/**
	 * This method creates an object of View.
	 * @return an instance of View.
	 */
	public ViewInterface createView()
	{
		View view = new View();
		return view;
	}

	/**
	 * This method creates an object of Rule.
	 * @return an instance of Rule.
	 */
	public RuleInterface createRule()
	{
		Rule rule = new Rule();
		return rule;
	}

	/**
	 * This method creates an object of RuleParameter.
	 * @return an instance of RuleParameter.
	 */
	public RuleParameterInterface createRuleParameter()
	{
		RuleParameter ruleParameter = new RuleParameter();
		return ruleParameter;
	}

	/**
	 * This method creates an object of CaDSRDE.
	 * @return an instance of CaDSRDE.
	 */
	public CaDSRDEInterface createCaDSRDE()
	{
		CaDSRDE caDSRDE = new CaDSRDE();
		return caDSRDE;
	}

	/**
	 * This method creates an object of UserDefinedDE.
	 * @return an instance of UserDefinedDE.
	 */
	public UserDefinedDE createUserDefinedDE()
	{
		UserDefinedDE userDefinedDE = new UserDefinedDE();
		return userDefinedDE;
	}

}

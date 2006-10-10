
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
 * @author sujay_narkar
 *
 */
public class DomainObjectFactory {
	
	/**
	 * Domain Object Factory Instance
	 */
	private static DomainObjectFactory domainObjectFactory;
	
	/**
	 * 
	 *
	 */
	protected DomainObjectFactory(){
		
	}
	
	/**
	 * Returns the instance of SegmentationDomainElementFactory
	 * @return SegmentationDomainElementFactory
	 */
	public static synchronized DomainObjectFactory getInstance () {
		if (domainObjectFactory == null) {
			domainObjectFactory= new  DomainObjectFactory();
		}
		return domainObjectFactory ;
	}
	
	/**
	 * Returns an object of Entity.
	 * @return an instance of Entity.
	 */
	public EntityInterface createEntity(){
		Entity entity = new Entity();
		return entity;
	}
	
	/**
	 * 
	 * @return an instance of EntityGroup.
	 */
	public EntityGroupInterface createEntityGroup(){
		EntityGroup entityGroup = new EntityGroup();
		return entityGroup;
	}
	
	/**
	 * 
	 * @return an instance of Association.
	 */
	public AssociationInterface createAssociation(){
		Association association = new Association();
		return association;
	}
	/**
	 * 
	 * @return an instance of Role.
	 */
	public RoleInterface createRole(){
		Role role = new Role();
		return role;
	}
	
	/**
	 * 
	 * @return an instance of SemanticProperty.
	 */
	public SemanticPropertyInterface createSemanticProperty() {
		SemanticProperty semanticProperty = new SemanticProperty();
		return semanticProperty;
	}
	
	/**
	 * 
	 * @return an instance of ColumnProperties.
	 */
	public ColumnPropertiesInterface createColumnProperties(){
		ColumnProperties columnProperties = new ColumnProperties();
		return columnProperties;
	}
	/**
	 * 
	 * @return an instance of TableProperties.
	 */
	public TablePropertiesInterface createTableProperties(){
		TableProperties tableProperties = new TableProperties();
		return tableProperties;
	}
	/**
	 * 
	 * @return an instance of ConstraintProperties.
	 */
	public ConstraintPropertiesInterface createConstraintProperties(){
		ConstraintProperties constraintProperties = new ConstraintProperties();
		return constraintProperties;
	}
	/**
	 * 
	 * @return instance of BooleanAttribute.
	 */
	public BooleanAttributeInterface createBooleanAttribute(){
		BooleanAttribute booleanAttribute = new BooleanAttribute();
		return booleanAttribute;
	}
	/**
	 * 
	 * @return instance of ByteArrayAttribute.
	 */
	public ByteArrayAttributeInterface createByteArrayAttribute(){
		ByteArrayAttribute byteArrayAttribute = new ByteArrayAttribute();
		return byteArrayAttribute;
	}  
    
    
    /**
     * 
     * @return instance of ByteArrayAttribute.
     */
    public ByteArrayValueInterface createByteArrayValue(){
        ByteArrayValue  byteArrayValue  = new ByteArrayValue ();
        return byteArrayValue;
    } 
    
    
	/**
	 * 
	 * @return instance of DateAttribute.
	 */
	public DateAttributeInterface createDateAttribute(){
		DateAttribute dateAttribute = new DateAttribute();
		return dateAttribute;
	}
	/**
	 * 
	 * @return instance of DoubleAttribute.
	 */
	public DoubleAttributeInterface createDoubleAttribute(){
		DoubleAttribute doubleAttribute = new DoubleAttribute();
		return doubleAttribute;
	}
	
	/**
	 * 
	 * @return instance of FloatAttribute.
	 */
	public FloatAttributeInterface createFloatAttribute(){
		FloatAttribute floatAttribute = new FloatAttribute();
		return floatAttribute;
	}
	
	/**
	 * 
	 * @return instance of IntegerAttribute.
	 */
	public IntegerAttributeInterface createIntegerAttribute(){
		IntegerAttribute integerAttribute = new IntegerAttribute();
		return integerAttribute;
	}
	
	/**
	 * 
	 * @return instance of LongAttribute.
	 */
	public LongAttributeInterface createLongAttribute(){
		LongAttribute longAttribute = new LongAttribute();
		return longAttribute;
	}
	
	/**
	 * 
	 * @return instance of ShortAttribute.
	 */
	public ShortAttributeInterface createShortAttribute(){
		ShortAttribute shortAttribute = new ShortAttribute();
		return shortAttribute;
	}
	
	/**
	 * 
	 * @return instance of StringAttribute.
	 */
	public StringAttributeInterface createStringAttribute(){
		StringAttribute stringAttribute = new StringAttribute();
		return stringAttribute;
	}
	/**
	 * 
	 * @return instance of BooleanConceptValue.
	 */
	public BooleanValueInterface createBooleanValue(){
		BooleanValue booleanValue = new BooleanValue();
		return booleanValue;
	}
	
	/**
	 * 
	 * @return instance of DateConceptValue.
	 */
	public DateValueInterface createDateValue(){
		DateValue dateValue = new DateValue();
		return dateValue;
	}
	/**
	 * 
	 * @return instance of DoubleConceptValue.
	 */
	public DoubleValueInterface createDoubleValue(){
		DoubleValue doubleValue = new DoubleValue();
		return doubleValue;
	}
	/**
	 * 
	 * @return instance of FloatConceptValue.
	 */
	public FloatValueInterface createFloatValue(){
		FloatValue floatValue = new FloatValue();
		return floatValue;
	}
	/**
	 * 
	 * @return instance of IntegerConceptValue.
	 */
	public IntegerValueInterface createIntegerValue(){
		IntegerValue integerValue = new IntegerValue();
		return integerValue;
	}
	/**
	 * 
	 * @return instance of LongConceptValue.
	 */
	public LongValueInterface createLongValue(){
		LongValue longValue = new LongValue();
		return longValue;
	}
	/**
	 * 
	 * @return instance of ShortConceptValue.
	 */
	public ShortValueInterface createShortValue(){
		ShortValue shortValue = new ShortValue();
		return shortValue;
	}
	
	/**
	 * 
	 * @return instance of StringConceptValue.
	 */
	public StringValueInterface createStringValue(){
		StringValue stringValue = new StringValue();
		return stringValue;
	}
	
	/**
	 * 
	 * @return instance of CheckBox.
	 */
	public CheckBoxInterface createCheckBox(){
		CheckBox checkBox = new CheckBox();
		return checkBox;
	}
	
	/**
	 * 
	 * @return instance of ComboBox.
	 */
	public ComboBoxInterface createComboBox(){
		ComboBox comboBox = new ComboBox();
		return comboBox;
	}
	
	/**
	 * 
	 * @return instance of Container.
	 */
	public ContainerInterface createContainer(){
		Container container = new Container();
		return container;
	}
	/**
	 * 
	 * @return instance of DataGrid.
	 */
	public DataGridInterface createDataGrid(){
		DataGrid dataGrid = new DataGrid();
		return dataGrid;
	}
	/**
	 * 
	 * @return instance of DatePicker.
	 */
	public DatePickerInterface createDatePicker(){
		DatePicker datePicker = new DatePicker();
		return datePicker;
	}
	/**
	 * 
	 * @return instance of ListBox.
	 */
	public ListBoxInterface createListBox(){
		ListBox listBox = new ListBox();
		return listBox;
	}
	/**
	 * 
	 * @return instance of RadioButton.
	 */
	public RadioButtonInterface createRadioButton(){
		RadioButton radioButton = new RadioButton();
		return radioButton;
	}
	/**
	 * 
	 * @return instance of TextArea.
	 */
	public TextAreaInterface createTextArea(){
		TextArea textArea = new TextArea();
		return textArea;
	}
	/**
	 * 
	 * @return instance of TextField.
	 */
	public TextFieldInterface createTextField(){
		TextField textField = new TextField();
		return textField;
	}
	/**
	 * 
	 * @return instance of View.
	 */
	public ViewInterface createView(){
		View view = new View();
		return view;
	}
	/**
	 * 
	 * @return
	 */
	public RuleInterface createRule(){
		Rule rule = new Rule();
		return rule;
	}
	
	/**
	 * 
	 * @return
	 */
	public RuleParameterInterface createRuleParameter(){
		RuleParameter ruleParameter = new RuleParameter();
		return ruleParameter;
	}
    
    /**
     * 
     * @return
     */
    public CaDSRDEInterface  createCaDSRDE(){
        CaDSRDE caDSRDE = new CaDSRDE();
        return caDSRDE;
    }
    
    /**
     * 
     * @return
     */
    
    public UserDefinedDE createUserDefinedDE(){
        UserDefinedDE userDefinedDE = new UserDefinedDE();
        return userDefinedDE ;
    }
    
   
    
    
   
	
}


package edu.common.dynamicextensions.processor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.BooleanValue;
import edu.common.dynamicextensions.domain.ByteArrayAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateValue;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleValue;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatValue;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerValue;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongValue;
import edu.common.dynamicextensions.domain.PermissibleValue;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortValue;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.RuleConfigurationObject;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.common.dynamicextensions.ui.webui.util.OptionValueObject;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AttributeProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for attribute processor
	 *
	 */
	protected AttributeProcessor()
	{

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static AttributeProcessor getInstance()
	{
		return new AttributeProcessor();
	}

	/**
	 * Creates a new AttributeInterface object based on the Datatype.
	 * If datatype is "DATATYPE_STRING" get a new instance of String attribute from DomainObjectFactory 
	 * and return it.
	 * Similarly for each Datatype a new Attribute object is created and returned back.  
	 * @param attributeUIBeanInformationIntf : UI Bean Information interface object that contains information of
	 * datatype selected by the user on the UI.
	 * @return New (Domain Object) Attribute object based on datatype
	 * @throws DynamicExtensionsApplicationException  : Exception
	 */
	public AttributeInterface createAttribute(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	throws DynamicExtensionsApplicationException
	{
		AttributeInterface attributeInterface = null;
		if (attributeUIBeanInformationIntf != null)
		{
			String attributeType = attributeUIBeanInformationIntf.getDataType();
			if (attributeType != null)
			{
				DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING))
				{
					attributeInterface = domainObjectFactory.createStringAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE))
				{
					attributeInterface = domainObjectFactory.createDateAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN))
				{
					attributeInterface = domainObjectFactory.createBooleanAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BYTEARRAY))
				{
					attributeInterface = domainObjectFactory.createByteArrayAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER))
				{
					int noOfDecimals = DynamicExtensionsUtility.convertStringToInt(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
					attributeInterface = getInterfaceForNumericDataType(noOfDecimals);
				}
			}
		}
		return attributeInterface;

	}

	/**
	 * This method populates the Attribute Interface objects with appropriate information based on its type.
	 * Each attribute object has different relevant information to be filled in based on the interface it implements
	 * This method accepts an AbstractAttributeInterface object and populates required fields.
	 * Information to be filled is available in the  AbstractAttributeUIBeanInterface object which is populated 
	 * in the UI.
	 * @param attributeInterface : Attribute(Domain Object to be populated) 
	 * @param attributeUIBeanInformationIntf : UI Bean object containing the information entered by the end-user on the UI.
	 *  @throws DynamicExtensionsSystemException : Exception
	 *  @throws DynamicExtensionsApplicationException : Excedption
	 */
	public void populateAttribute(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if ((attributeUIBeanInformationIntf != null) && (attributeInterface != null))
		{
			//populate information specific to attribute type
			populateAttributeSpecificInfo(attributeInterface,attributeUIBeanInformationIntf);

			//populate information common to attributes
			populateAttributeCommomInfo(attributeInterface,attributeUIBeanInformationIntf);

			//Set is identified
			populateIsIdentifiedInfo(attributeInterface,attributeUIBeanInformationIntf.getAttributeIdentified());

			//set concept codes
			populateSemanticPropertiesInfo(attributeInterface,attributeUIBeanInformationIntf.getAttributeConceptCode());

			//populate rules
			populateRules(attributeInterface, attributeUIBeanInformationIntf);
		}
		else
		{
			Logger.out.error("Either Attribute interface or attribute information interface is null [" + attributeInterface + "] / ["
					+ attributeUIBeanInformationIntf + "]");
		}

	}

	/**
	 * @param attributeInterface
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateAttributeCommomInfo(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		//Set name of attribute
		attributeInterface.setName(attributeUIBeanInformationIntf.getName());
		//desc of attribute
		attributeInterface.setDescription(attributeUIBeanInformationIntf.getDescription());
	}

	/**
	 * @param attributeInterface
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateIsIdentifiedInfo(AbstractAttributeInterface attributeInterface, String strIsIdentified)
	{
		if (attributeInterface instanceof AttributeInterface)
		{
			Boolean isIdentified = new Boolean(strIsIdentified);
			((AttributeInterface) attributeInterface).setIsIdentified(isIdentified);
		}
	}

	/**
	 * @param attributeInterface
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateSemanticPropertiesInfo(AbstractAttributeInterface attributeInterface,String attributeConceptCode)
	{
		Collection collection = SemanticPropertyBuilderUtil.getSymanticPropertyCollection(attributeConceptCode);
		if (collection != null && !collection.isEmpty())
		{
			Iterator iterator = collection.iterator();
			while (iterator.hasNext())
			{
				attributeInterface.addSemanticProperty((SemanticPropertyInterface) iterator.next());
			}
		}
	}

	/**
	 * @param attributeTypeInformation
	 * @param attributeUIBeanInformationIntf 
	 * @throws DynamicExtensionsApplicationException 
	 */
	private void populateAttributeSpecificInfo(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
	{
		AttributeTypeInformationInterface attributeTypeInformation = DynamicExtensionsUtility.getAttributeTypeInformation(attributeInterface);
		if((attributeTypeInformation!=null)&&(attributeUIBeanInformationIntf!=null))
		{
			if (attributeTypeInformation instanceof StringAttributeTypeInformation)
			{
				populateStringAttributeInterface((StringAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof BooleanAttributeTypeInformation)
			{
				populateBooleanAttributeInterface((BooleanAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
			{
				populateDateAttributeInterface((DateAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof ByteArrayAttributeTypeInformation)
			{
				populateByteArrayAttributeInterface((ByteArrayAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof ShortAttributeTypeInformation)
			{
				populateShortAttributeInterface((ShortAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof LongAttributeTypeInformation)
			{
				populateLongAttributeInterface((LongAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof IntegerAttributeTypeInformation)
			{
				populateIntegerAttributeInterface((IntegerAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof FloatAttributeTypeInformation)
			{
				populateFloatAttributeInterface((FloatAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
			{
				populateDoubleAttributeInterface((DoubleAttributeTypeInformation) attributeTypeInformation, attributeUIBeanInformationIntf);
			}
		}
	}

	/**
	 * @param interface1
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateByteArrayAttributeInterface(ByteArrayAttributeTypeInformation byteArrayAttribute, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		//TODO : Code for byte array attribute initialization 
	}

	/**
	 * @author deepti_shelar
	 * 
	 * Populate validation rules information for the attribute.
	 * There are some validation rules that are applicable to the attributes. These need to be stored along with the attributes
	 *
	 * @param abstractAttributeInterface : attribute interface
	 * @param attributeUIBeanInformationIntf : UI Bean containing rule information specified by the user
	 * @throws DynamicExtensionsSystemException : dynamicExtensionsSystemException
	 */
	public void populateRules(AbstractAttributeInterface abstractAttributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	throws DynamicExtensionsSystemException
	{
		String[] validationRules = attributeUIBeanInformationIntf.getValidationRules();
		ControlConfigurationsFactory configurationsFactory = null;
		if (validationRules != null && validationRules.length != 0)
		{
			String validationRule = "";
			configurationsFactory = ControlConfigurationsFactory.getInstance();
			RuleConfigurationObject ruleConfigurationObject = null;
			
			DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
			RuleInterface ruleInterface = null;
			Collection<RuleParameterInterface> ruleParameterCollection = new HashSet<RuleParameterInterface>();
			
			for (int counter = 0; counter < validationRules.length; counter++)
			{
				validationRule = validationRules[counter];
				ruleConfigurationObject = configurationsFactory.getRuleObject(validationRule);
				ruleInterface = domainObjectFactory.createRule();
				ruleInterface.setName(ruleConfigurationObject.getRuleName());

				ruleParameterCollection = getRuleParameterCollection(ruleConfigurationObject, attributeUIBeanInformationIntf);
				if (ruleParameterCollection != null && !(ruleParameterCollection.isEmpty()))
				{
					ruleInterface.setRuleParameterCollection(ruleParameterCollection);
				}
				abstractAttributeInterface.addRule(ruleInterface);
			}
		}
		
	}

	/**
	 * 
	 * @param ruleConfigurationObject : Rule configuration object
	 * @param abstractAttributeUIBeanInterface : UI Bean for attribute information
	 * @return : Collection of rule parameters
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	private Collection<RuleParameterInterface> getRuleParameterCollection(RuleConfigurationObject ruleConfigurationObject,
			AbstractAttributeUIBeanInterface abstractAttributeUIBeanInterface) throws DynamicExtensionsSystemException
			{
		Collection<RuleParameterInterface> ruleParameterCollection = new HashSet<RuleParameterInterface>();
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		List ruleParametersList = ruleConfigurationObject.getRuleParametersList();
		if (ruleParametersList != null)
		{
			StringBuffer operationNameBuff = null;
			Iterator ruleParametersListIter = ruleParametersList.iterator();
			while (ruleParametersListIter.hasNext())
			{
				NameValueBean param = (NameValueBean) ruleParametersListIter.next();

				String paramName = param.getName();
				operationNameBuff = new StringBuffer(paramName);
				operationNameBuff.setCharAt(0, Character.toUpperCase(operationNameBuff.charAt(0)));
				String methodName = "get" + operationNameBuff.toString();

				try
				{
					Class clas = Class.forName("edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface");
					Class[] types = new Class[]{};

					Method method = clas.getMethod(methodName, types);
					Object result = method.invoke(abstractAttributeUIBeanInterface, new Object[0]);
					RuleParameterInterface ruleParameterInterface = domainObjectFactory.createRuleParameter();
					ruleParameterInterface.setName(paramName);
					if (result != null)
					{
						ruleParameterInterface.setValue(result.toString());
					}
					ruleParameterCollection.add(ruleParameterInterface);

				}
				catch (Exception e)
				{
					throw new DynamicExtensionsSystemException(e.getMessage(), e);
				}

			}
		}

		return ruleParameterCollection;
			}

	/**
	 * @param attributeUIBeanInformationIntf : UI Bean attribute information object
	 * @return : Data Element containing list of permisible values
	 * @throws DynamicExtensionsApplicationException :dynamicExtensionsApplicationException
	 */
	public DataElementInterface getDataElementInterface(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	throws DynamicExtensionsApplicationException
	{
		DataElementInterface dataEltInterface = null;
		PermissibleValueInterface permissibleValue = null;

		if (attributeUIBeanInformationIntf != null)
		{
			String displayChoice = attributeUIBeanInformationIntf.getDisplayChoice();
			if (displayChoice != null)
			{
				if (displayChoice.equalsIgnoreCase(ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED))
				{
					dataEltInterface = DomainObjectFactory.getInstance().createUserDefinedDE();

					String[] optionNames = attributeUIBeanInformationIntf.getOptionNames();
					String[] optionDescriptions = attributeUIBeanInformationIntf.getOptionDescriptions();
					String[] optionConceptCodes = attributeUIBeanInformationIntf.getOptionConceptCodes();
					String optionName = null,optionDesc = null,optionConceptCode = null;
					Collection<SemanticPropertyInterface> semanticPropertiesForOptions = null;

					if(optionNames!=null)
					{
						for(int i=0;i<optionNames.length;i++)
						{
							optionName = optionNames[i];  
							optionDesc = optionDescriptions[i];
							optionConceptCode = optionConceptCodes[i];
							semanticPropertiesForOptions = SemanticPropertyBuilderUtil.getSymanticPropertyCollection(optionConceptCode);
							if ((optionName != null) && (optionName.trim() != null))
							{
								permissibleValue = getPermissibleValue(attributeUIBeanInformationIntf, optionName,optionDesc,semanticPropertiesForOptions);
								((UserDefinedDE) dataEltInterface).addPermissibleValue(permissibleValue);
							}
						}
					}

				}
			}
		}
		return dataEltInterface;
	}

	/**
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information
	 * @param permissibleValue : permissible value  for attribute
	 * @return Permissible value object for given permissible value 
	 * @throws DynamicExtensionsApplicationException  : dynamicExtensionsApplicationException
	 */
	private PermissibleValueInterface getPermissibleValue(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf,
			String permissibleValue,String permissibleValueDesc,Collection permissibleValueSematicPropColln) throws DynamicExtensionsApplicationException
			{
		PermissibleValueInterface permissibleValueIntf = null;
		if (attributeUIBeanInformationIntf != null)
		{
			String attributeType = attributeUIBeanInformationIntf.getDataType();
			if (attributeType != null)
			{
				try
				{
					if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING))
					{
						permissibleValueIntf = DomainObjectFactory.getInstance().createStringValue();
						((StringValue) permissibleValueIntf).setValue(permissibleValue);
					}
					else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE))
					{
						permissibleValueIntf = DomainObjectFactory.getInstance().createDateValue();
						Date value = Utility.parseDate(permissibleValue,ProcessorConstants.DATE_ONLY_FORMAT);
						((DateValue) permissibleValueIntf).setValue(value);
					}
					else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN))
					{
						permissibleValueIntf = DomainObjectFactory.getInstance().createBooleanValue();
						Boolean value = new Boolean(permissibleValue);
						((BooleanValue) permissibleValueIntf).setValue(value);
					}
					else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER))
					{
						permissibleValueIntf = getPermissibleValueInterfaceForNumber(attributeUIBeanInformationIntf, permissibleValue,permissibleValueDesc,permissibleValueSematicPropColln);
					}
					//populate common properties
					if(permissibleValueIntf instanceof PermissibleValue)
					{
						((PermissibleValue) permissibleValueIntf).setDescription(permissibleValueDesc);
						((PermissibleValue) permissibleValueIntf).setSemanticPropertyCollection(permissibleValueSematicPropColln);
					}
				}
				catch (Exception e)
				{
					throw new DynamicExtensionsApplicationException(e.getMessage(), e);
				}

			}
		}

		return permissibleValueIntf;
			}

	/**
	 * @param attributeUIBeanInformationIntf : attribute UI Information
	 * @param permissibleValue : Permissible values
	 * @return PermissibleValueInterface for numberic field
	 * @throws DynamicExtensionsApplicationException :Exception
	 */
	private PermissibleValueInterface getPermissibleValueInterfaceForNumber(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf,
			String permissibleValue,String permissibleValueDesc,Collection permissibleValueSematicPropColln) throws DynamicExtensionsApplicationException
			{

		PermissibleValueInterface permissibleValueIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		int noOfDecimalPlaces = 0;
		//Number of decimal places 
		String strNoOfDecimalPlaces = attributeUIBeanInformationIntf.getAttributeDecimalPlaces();
		if (strNoOfDecimalPlaces != null)
		{
			try
			{
				if (strNoOfDecimalPlaces.trim().equals(""))
				{
					noOfDecimalPlaces = 0;
				}
				else
				{
					noOfDecimalPlaces = Integer.parseInt(strNoOfDecimalPlaces);
				}
			}
			catch (NumberFormatException e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
		}

		if (noOfDecimalPlaces == 0)
		{

			permissibleValueIntf = DomainObjectFactory.getInstance().createLongValue();
			Long value = null;
			try
			{
				value = new Long(permissibleValue);
			}
			catch (Exception e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
			((LongValue) permissibleValueIntf).setValue(value);
		}
		else if (noOfDecimalPlaces > 0)
		{

			permissibleValueIntf = DomainObjectFactory.getInstance().createDoubleValue();
			Double value = null;
			try
			{
				value = new Double(permissibleValue);
			}
			catch (Exception e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
			((DoubleValue) permissibleValueIntf).setValue(value);
		}
		return permissibleValueIntf;

			}

	/**
	 * 
	 * @param attributeUIBeanInformationIntf  :UI Bean containing attribute information entered by user on UI
	 * @return Attribute object populated with all required information
	 * @throws DynamicExtensionsSystemException : Exception
	 * @throws DynamicExtensionsApplicationException : Exception
	 */
	public AttributeInterface createAndPopulateAttribute(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AttributeInterface attributeInterface = createAttribute(attributeUIBeanInformationIntf);
		populateAttribute(attributeInterface, attributeUIBeanInformationIntf);
		return attributeInterface;
	}

	/**
	 * 
	 * @param booleanAttributeIntf Boolean attribute object
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 */
	private void populateBooleanAttributeInterface(BooleanAttributeTypeInformation booleanAttributeIntf,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		BooleanValueInterface booleanValue = DomainObjectFactory.getInstance().createBooleanValue();
		booleanValue.setValue(new Boolean(attributeUIBeanInformationIntf.getAttributeDefaultValue()));
		booleanAttributeIntf.setDefaultValue(booleanValue);
	}

	/**
	 * 
	 * @param dateAttributeIntf : date Attribute ObjectUI Bean containing attribute information entered by user on UI
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 * @throws DynamicExtensionsApplicationException : Exception
	 */
	private void populateDateAttributeInterface(DateAttributeTypeInformation dateAttributeIntf,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
			{
		String dateValueType = attributeUIBeanInformationIntf.getDateValueType();
		Date defaultValue = null;
		if (dateValueType != null)
		{
			try
			{
				if (dateValueType.equalsIgnoreCase(ProcessorConstants.DATE_VALUE_TODAY))
				{
					String todaysDate = Utility.parseDateToString(new Date(), ProcessorConstants.DATE_ONLY_FORMAT);
					defaultValue = Utility.parseDate(todaysDate,ProcessorConstants.DATE_ONLY_FORMAT);
				}
				else if (dateValueType.equalsIgnoreCase(ProcessorConstants.DATE_VALUE_SELECT))
				{
					if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
					{
						defaultValue = Utility.parseDate(attributeUIBeanInformationIntf.getAttributeDefaultValue(),ProcessorConstants.DATE_ONLY_FORMAT);
					}
				}

			}
			catch (Exception e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
		}
		//Set default value
		DateValueInterface dateValue = DomainObjectFactory.getInstance().createDateValue();
		dateValue.setValue(defaultValue);
		dateAttributeIntf.setDefaultValue(dateValue);

		//		Set Date format based on the UI selection : DATE ONLY or DATE And TIME
		if (attributeUIBeanInformationIntf.getFormat() != null)
		{
			if (attributeUIBeanInformationIntf.getFormat().equalsIgnoreCase(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY))
			{
				dateAttributeIntf.setFormat(ProcessorConstants.DATE_ONLY_FORMAT);
			}
			else if (attributeUIBeanInformationIntf.getFormat().equalsIgnoreCase(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME))
			{
				dateAttributeIntf.setFormat(ProcessorConstants.DATE_TIME_FORMAT);
			}
		}
			}

	/**
	 * 
	 * @param stringAttributeIntf : String attribute object
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information entered by user on UI
	 * @throws DynamicExtensionsApplicationException : Exception
	 */
	private void populateStringAttributeInterface(StringAttributeTypeInformation stringAttributeIntf,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
			{
		//Default Value
		StringValueInterface stringValue = DomainObjectFactory.getInstance().createStringValue();
		stringValue.setValue(attributeUIBeanInformationIntf.getAttributeDefaultValue());
		stringAttributeIntf.setDefaultValue(stringValue);

		//Size for string attribute
		Integer size = null;
		try
		{
			if ((attributeUIBeanInformationIntf.getAttributeSize() != null)&&(!attributeUIBeanInformationIntf.getAttributeSize().trim().equals("")))
			{
				size = new Integer(attributeUIBeanInformationIntf.getAttributeSize());
			}
			else
			{
				size = new Integer(0);
			}
			stringAttributeIntf.setSize(size);
		}
		catch (NumberFormatException e)
		{
			throw new DynamicExtensionsApplicationException(e.getMessage(), e);
		}

			}

	/**
	 * 
	 * @param shortAttributeInterface : Short attribute object
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information entered by user on UI
	 */
	private void populateShortAttributeInterface(ShortAttributeTypeInformation shortAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		//Set default value
		if((attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)&&(!attributeUIBeanInformationIntf.getAttributeDefaultValue().trim().equals("")))
		{
			ShortValueInterface shortValue = DomainObjectFactory.getInstance().createShortValue();
			shortValue.setValue(new Short(attributeUIBeanInformationIntf.getAttributeDefaultValue()));
			shortAttributeInterface.setDefaultValue(shortValue);
		}
		shortAttributeInterface.setMeasurementUnits(attributeUIBeanInformationIntf.getAttributeMeasurementUnits());
		shortAttributeInterface.setDecimalPlaces(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
		shortAttributeInterface.setDigits(attributeUIBeanInformationIntf.getAttributeDigits());

	}

	/**
	 * 
	 * @param integerAttributeInterface : Integer Attribute object
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information entered by user on UI
	 * @throws DynamicExtensionsApplicationException : Excpetion
	 */
	private void populateIntegerAttributeInterface(IntegerAttributeTypeInformation integerAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
			{
		//Set default value
		if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
		{
			Integer defaultValue;
			try
			{
				if (attributeUIBeanInformationIntf.getAttributeDefaultValue().trim().equals(""))
				{
					defaultValue = new Integer(0); //Assume 0 for blank fields
				}
				else
				{
					defaultValue = new Integer(attributeUIBeanInformationIntf.getAttributeDefaultValue());
				}
			}
			catch (NumberFormatException e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
			IntegerValueInterface integerValue = DomainObjectFactory.getInstance().createIntegerValue();
			integerValue.setValue(defaultValue);
			integerAttributeInterface.setDefaultValue(integerValue);
		}
		integerAttributeInterface.setMeasurementUnits(attributeUIBeanInformationIntf.getAttributeMeasurementUnits());
		integerAttributeInterface.setDecimalPlaces(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
		integerAttributeInterface.setDigits(attributeUIBeanInformationIntf.getAttributeDigits());

			}

	/**
	 * 
	 * @param longAttributeInterface : Long attribute object
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 */
	private void populateLongAttributeInterface(LongAttributeTypeInformation longAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		//Set Default Value
		if((attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)&&(!attributeUIBeanInformationIntf.getAttributeDefaultValue().trim().equals("")))
		{
			LongValueInterface longValue = DomainObjectFactory.getInstance().createLongValue();
			longValue.setValue(new Long(attributeUIBeanInformationIntf.getAttributeDefaultValue()));
			longAttributeInterface.setDefaultValue(longValue);
		}
		longAttributeInterface.setMeasurementUnits(attributeUIBeanInformationIntf.getAttributeMeasurementUnits());
		longAttributeInterface.setDecimalPlaces(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
		longAttributeInterface.setDigits(attributeUIBeanInformationIntf.getAttributeDigits());

	}

	/**
	 * 
	 * @param floatAttributeInterface : Float attribute
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information entered by user on UI
	 */
	private void populateFloatAttributeInterface(FloatAttributeTypeInformation floatAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if((attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)&&(!attributeUIBeanInformationIntf.getAttributeDefaultValue().trim().equals("")))
		{
			FloatValueInterface floatValue = DomainObjectFactory.getInstance().createFloatValue();
			floatValue.setValue(new Float(attributeUIBeanInformationIntf.getAttributeDefaultValue()));
			floatAttributeInterface.setDefaultValue(floatValue);
		}
		floatAttributeInterface.setMeasurementUnits(attributeUIBeanInformationIntf.getAttributeMeasurementUnits());
		floatAttributeInterface.setDecimalPlaces(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
		floatAttributeInterface.setDigits(attributeUIBeanInformationIntf.getAttributeDigits());

	}

	/**
	 * 
	 * @param doubleAttributeInterface : Double attribute
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 */
	private void populateDoubleAttributeInterface(DoubleAttributeTypeInformation doubleAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if((attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)&&(!attributeUIBeanInformationIntf.getAttributeDefaultValue().trim().equals("")))
		{
			DoubleValueInterface doubleValue = DomainObjectFactory.getInstance().createDoubleValue();
			doubleValue.setValue(new Double(attributeUIBeanInformationIntf.getAttributeDefaultValue()));
			doubleAttributeInterface.setDefaultValue(doubleValue);
		}
		doubleAttributeInterface.setMeasurementUnits(attributeUIBeanInformationIntf.getAttributeMeasurementUnits());
		doubleAttributeInterface.setDecimalPlaces(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
		doubleAttributeInterface.setDigits(attributeUIBeanInformationIntf.getAttributeDigits());
		//doubleAttributeInterface.setValidationRules(attributeInformationIntf.getValidationRules());
	}

	/**
	 * 
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 * @return Attribute for appropriate numeric data type based on number of decimal places and digits 
	 */
	private AttributeInterface getInterfaceForNumericDataType(int noOfDecimalPlaces)
	{
		AttributeInterface numberAttribIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		if (noOfDecimalPlaces == 0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
		}
		if (noOfDecimalPlaces > 0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttribute();
		}
		return numberAttribIntf;
	}

	/**
	 * 
	 * @param attributeInterface : Attribute object
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information entered by user on UI
	 */
	private void populateAttributeValidationRules(AbstractAttributeInterface attributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		String[] ruleNames = null;

		int i = 0;
		if (attributeInterface.getRuleCollection() != null && !attributeInterface.getRuleCollection().isEmpty())
		{
			Iterator rulesIter = attributeInterface.getRuleCollection().iterator();
			ruleNames = new String[attributeInterface.getRuleCollection().size()];
			while (rulesIter.hasNext())
			{
				RuleInterface rule = (RuleInterface) rulesIter.next();
				ruleNames[i++] = rule.getName();
				if (rule.getRuleParameterCollection() != null && !rule.getRuleParameterCollection().isEmpty())
				{
					Iterator paramIter = rule.getRuleParameterCollection().iterator();
					while (paramIter.hasNext())
					{
						RuleParameterInterface param = (RuleParameterInterface) paramIter.next();
						if (param.getName().equalsIgnoreCase("min"))
						{
							attributeUIBeanInformationIntf.setMin(param.getValue());
						}
						else if (param.getName().equalsIgnoreCase("max"))
						{
							attributeUIBeanInformationIntf.setMax(param.getValue());
						}
					}
				}
			}
		}
		attributeUIBeanInformationIntf.setValidationRules(ruleNames);
		attributeUIBeanInformationIntf.setTempValidationRules(ruleNames);
	}

	/**
	 * 
	 * @param attributeInterface :Attribute object
	 * @param attributeUIBeanInformationIntf  : UI Bean containing attribute information to be displayed on UI
	 */
	public void populateAttributeUIBeanInterface(AbstractAttributeInterface attributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if ((attributeUIBeanInformationIntf != null) && (attributeInterface != null))
		{
			attributeUIBeanInformationIntf.setName(attributeInterface.getName());
			attributeUIBeanInformationIntf.setDescription(attributeInterface.getDescription());

			//is Identified
			if (attributeInterface instanceof AttributeInterface)
			{
				Boolean isIdentified = ((AttributeInterface) attributeInterface).getIsIdentified();
				if (isIdentified != null)
				{
					attributeUIBeanInformationIntf.setAttributeIdentified(isIdentified.toString());
				}
			}
			//Concept code
			if (attributeInterface.getSemanticPropertyCollection() != null && !attributeInterface.getSemanticPropertyCollection().isEmpty())
			{
				attributeUIBeanInformationIntf.setAttributeConceptCode(SemanticPropertyBuilderUtil.getConceptCodeString(attributeInterface));
			}
			populateAttributeValidationRules(attributeInterface, attributeUIBeanInformationIntf);
			//Permissible values
			setOptionsInformation(attributeInterface, attributeUIBeanInformationIntf);
			populateAttributeInformationInUIBean(attributeInterface,attributeUIBeanInformationIntf);

		}
	}

	/**
	 * @param attributeInterface
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateAttributeInformationInUIBean(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		AttributeTypeInformationInterface attributeTypeInformationInterface = DynamicExtensionsUtility.getAttributeTypeInformation(attributeInterface);
		if(attributeTypeInformationInterface!=null)
		{
			if (attributeTypeInformationInterface instanceof StringAttributeTypeInformation)
			{
				populateStringAttributeUIBeanInterface((StringAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof DateAttributeTypeInformation)
			{
				populateDateAttributeUIBeanInterface((DateAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof BooleanAttributeTypeInformation)
			{
				populateBooleanAttributeUIBeanInterface((BooleanAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof IntegerAttributeTypeInformation)
			{
				populateIntegerAttributeUIBeanInterface((IntegerAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof ShortAttributeTypeInformation)
			{
				populateShortAttributeUIBeanInterface((ShortAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof LongAttributeTypeInformation)
			{
				populateLongAttributeUIBeanInterface((LongAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);
			}
			else if (attributeTypeInformationInterface instanceof FloatAttributeTypeInformation)
			{
				populateFloatAttributeUIBeanInterface((FloatAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);

			}
			else if (attributeTypeInformationInterface instanceof DoubleAttributeTypeInformation)
			{
				populateDoubleAttributeUIBeanInterface((DoubleAttributeTypeInformation) attributeTypeInformationInterface,attributeUIBeanInformationIntf);

			}
		}
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateDoubleAttributeUIBeanInterface(DoubleAttributeTypeInformation doubleAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
		if (doubleAttributeInformation.getDefaultValue() != null)
		{
			DoubleValue defaultDoubleValue = (DoubleValue)doubleAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(defaultDoubleValue.getValue()+"");
		}
		attributeUIBeanInformationIntf.setAttributeMeasurementUnits(doubleAttributeInformation.getMeasurementUnits());
		attributeUIBeanInformationIntf.setAttributeDecimalPlaces(doubleAttributeInformation.getDecimalPlaces());
		attributeUIBeanInformationIntf.setAttributeDigits(doubleAttributeInformation.getDigits());		
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateFloatAttributeUIBeanInterface(FloatAttributeTypeInformation floatAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
		if (floatAttributeInformation.getDefaultValue() != null)
		{
			FloatValue floatValue = (FloatValue)floatAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(floatValue.getValue()+"");
		}
		attributeUIBeanInformationIntf.setAttributeMeasurementUnits(floatAttributeInformation.getMeasurementUnits());
		attributeUIBeanInformationIntf.setAttributeDecimalPlaces(floatAttributeInformation.getDecimalPlaces());
		attributeUIBeanInformationIntf.setAttributeDigits(floatAttributeInformation.getDigits());
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateLongAttributeUIBeanInterface(LongAttributeTypeInformation longAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
		if (longAttributeInformation.getDefaultValue() != null)
		{
			LongValue longDefaultValue = (LongValue)longAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(longDefaultValue.getValue()+"");
		}
		attributeUIBeanInformationIntf.setAttributeMeasurementUnits(longAttributeInformation.getMeasurementUnits());
		attributeUIBeanInformationIntf.setAttributeDecimalPlaces(longAttributeInformation.getDecimalPlaces());
		attributeUIBeanInformationIntf.setAttributeDigits(longAttributeInformation.getDigits());
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateShortAttributeUIBeanInterface(ShortAttributeTypeInformation shortAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
		if (shortAttributeInformation.getDefaultValue() != null)
		{
			ShortValue shortDefaultValue =(ShortValue)shortAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(shortDefaultValue.getValue()+"");
		}
		attributeUIBeanInformationIntf.setAttributeMeasurementUnits(shortAttributeInformation.getMeasurementUnits());
		attributeUIBeanInformationIntf.setAttributeDecimalPlaces(shortAttributeInformation.getDecimalPlaces());
		attributeUIBeanInformationIntf.setAttributeDigits(shortAttributeInformation.getDigits());
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateIntegerAttributeUIBeanInterface(IntegerAttributeTypeInformation integerAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
		if (integerAttributeInformation.getDefaultValue() != null)
		{
			IntegerValue integerDefaultValue = (IntegerValue)integerAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(integerDefaultValue.getValue()+"");
		}
		attributeUIBeanInformationIntf.setAttributeMeasurementUnits(integerAttributeInformation.getMeasurementUnits());
		attributeUIBeanInformationIntf.setAttributeDecimalPlaces(integerAttributeInformation.getDecimalPlaces());
		attributeUIBeanInformationIntf.setAttributeDigits(integerAttributeInformation.getDigits());		
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateBooleanAttributeUIBeanInterface(BooleanAttributeTypeInformation booleanAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_BOOLEAN);
		if (booleanAttributeInformation.getDefaultValue() != null)
		{
			BooleanValue booleanDefaultValue = (BooleanValue)booleanAttributeInformation.getDefaultValue();
			attributeUIBeanInformationIntf.setAttributeDefaultValue(booleanDefaultValue.getValue()+"");
		}
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateDateAttributeUIBeanInterface(DateAttributeTypeInformation datAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_DATE);
		if (datAttributeInformation.getDefaultValue() != null)
		{
			String defaultValue = Utility.parseDateToString((Date)datAttributeInformation.getDefaultValue().getValueAsObject(),
					ProcessorConstants.DATE_ONLY_FORMAT);
			attributeUIBeanInformationIntf.setAttributeDefaultValue(defaultValue);
		}
		else
		{
			attributeUIBeanInformationIntf.setAttributeDefaultValue("");
		}
		String dateFormat = ((DateAttributeTypeInformation) datAttributeInformation).getFormat();
		if (dateFormat != null)
		{
			if (dateFormat.equalsIgnoreCase(ProcessorConstants.DATE_ONLY_FORMAT))
			{
				attributeUIBeanInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY);
			}
			else if (dateFormat.equalsIgnoreCase(ProcessorConstants.DATE_TIME_FORMAT))
			{
				attributeUIBeanInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME);
			}
		}
		else
			//Default will be date only
		{
			attributeUIBeanInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY);
		}
	}

	/**
	 * @param information
	 * @param attributeUIBeanInformationIntf
	 */
	private void populateStringAttributeUIBeanInterface(StringAttributeTypeInformation stringAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_STRING);
		if(stringAttributeInformation.getDefaultValue()!=null)
		{
			attributeUIBeanInformationIntf.setAttributeDefaultValue((String)stringAttributeInformation.getDefaultValue().getValueAsObject());
		}
		Integer size =  stringAttributeInformation.getSize();
		if (size != null)
		{
			attributeUIBeanInformationIntf.setAttributeSize(size.toString());
		}
	}

	/**
	 * 
	 * @param attributeInterface : Attribute interface
	 * @param attributeUIBeanInformationIntf    : UI Bean containing attribute information to be displayed on UI 
	 * @return Comma separated list of permissible values
	 */
	private void setOptionsInformation(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		ArrayList<OptionValueObject> optionDetails = new ArrayList<OptionValueObject>();


		if ((attributeUIBeanInformationIntf != null) && (attributeInterface != null))
		{
			AttributeTypeInformationInterface attributeTypeInformationInterface = DynamicExtensionsUtility.getAttributeTypeInformation(attributeInterface); 
			if (attributeTypeInformationInterface!=null)
			{
				DataElementInterface dataEltInterface = attributeTypeInformationInterface.getDataElement();
				if (dataEltInterface != null)
				{
					if (dataEltInterface instanceof UserDefinedDEInterface)
					{
						attributeUIBeanInformationIntf.setDisplayChoice(ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED);
						UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) dataEltInterface;
						Collection userDefinedValues = userDefinedDE.getPermissibleValueCollection();
						if (userDefinedValues != null)
						{
							PermissibleValueInterface permissibleValueIntf = null;
							Iterator userDefinedValuesIterator = userDefinedValues.iterator();
							while (userDefinedValuesIterator.hasNext())
							{
								permissibleValueIntf = (PermissibleValueInterface) userDefinedValuesIterator.next();
								optionDetails.add(getOptionDetails(permissibleValueIntf));
							}
						}
						attributeUIBeanInformationIntf.setOptionDetails(optionDetails);
					}
				}
			}
		}
	}

	/**
	 * @param permissibleValueIntf
	 * @return
	 */
	private OptionValueObject getOptionDetails(PermissibleValueInterface permissibleValueIntf)
	{
		if(permissibleValueIntf!=null)
		{
			Object permissibleValueObjectValue = permissibleValueIntf.getValueAsObject();
			if ((permissibleValueObjectValue != null) && (permissibleValueObjectValue.toString() != null)
					&& (permissibleValueObjectValue.toString().trim() != ""))
			{
				OptionValueObject optionDetail = new OptionValueObject();
				optionDetail.setOptionName(permissibleValueObjectValue.toString().trim());
				if(permissibleValueIntf instanceof PermissibleValue)
				{
					populateOptionDetails(optionDetail,((PermissibleValue)permissibleValueIntf));
				}
				return optionDetail;
			}
		}
		return null;
	}

	/**
	 * @param optionValue 
	 * @param permissibleValueIntf
	 */
	private void populateOptionDetails(OptionValueObject optionValue, PermissibleValue permissibleValue)
	{
		if((optionValue!=null)&&(permissibleValue!=null))
		{
			if(permissibleValue.getDescription()!=null)
			{
				optionValue.setOptionDescription(permissibleValue.getDescription());
			}
			else
			{
				optionValue.setOptionDescription("");	
			}
			String optionConceptCode = SemanticPropertyBuilderUtil.getConceptCodeString(permissibleValue.getSemanticPropertyCollection());
			if(optionConceptCode!=null)
			{
				optionValue.setOptionConceptCode(optionConceptCode);
			}
			else
			{
				optionValue.setOptionConceptCode("");	
			}
		}
	}

	/**
	 * @param abstractAttributeInterface
	 * @param controlsForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public void updateAttributeInformation(AbstractAttributeInterface abstractAttributeInformation, AbstractAttributeUIBeanInterface attributeUIBeanInformation) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		if((abstractAttributeInformation!=null)&&(attributeUIBeanInformation!=null))
		{
			if(abstractAttributeInformation instanceof AttributeInterface)
			{
				AttributeInterface attributeInformation = (AttributeInterface)abstractAttributeInformation;
				AttributeTypeInformationInterface attributeTypeInformation = createAttributeTypeInformation(attributeUIBeanInformation);
				attributeInformation.setAttributeTypeInformation(attributeTypeInformation);
				populateAttribute(attributeInformation, attributeUIBeanInformation);
			}
		}
	}

	/**
	 * @param attributeUIBeanInformation
	 * @throws DynamicExtensionsApplicationException 
	 */
	private AttributeTypeInformationInterface createAttributeTypeInformation(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
	{
		AttributeTypeInformationInterface attributeTypeInformation = null;
		if (attributeUIBeanInformationIntf != null)
		{
			String attributeType = attributeUIBeanInformationIntf.getDataType();
			if (attributeType != null)
			{
				DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING))
				{
					attributeTypeInformation = domainObjectFactory.createStringAttributeTypeInformation();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE))
				{
					attributeTypeInformation = domainObjectFactory.createDateAttributeTypeInformation();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN))
				{
					attributeTypeInformation = domainObjectFactory.createBooleanAttributeTypeInformation();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BYTEARRAY))
				{
					attributeTypeInformation = domainObjectFactory.createByteArrayAttributeTypeInformation();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER))
				{
					int noOfDecimals = DynamicExtensionsUtility.convertStringToInt(attributeUIBeanInformationIntf.getAttributeDecimalPlaces());
					attributeTypeInformation = getInterfaceForNumericDataTypeInformation(noOfDecimals);
				}
			}
		}
		return attributeTypeInformation;
	}
	/**
	 * 
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 * @return Attribute for appropriate numeric data type based on number of decimal places and digits 
	 */
	private AttributeTypeInformationInterface getInterfaceForNumericDataTypeInformation(int noOfDecimalPlaces)
	
	{
		AttributeTypeInformationInterface numberAttribIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		if (noOfDecimalPlaces == 0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createLongAttributeTypeInformation();
		}
		if (noOfDecimalPlaces > 0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttributeTypeInformation();
		}
		return numberAttribIntf;
	}
}
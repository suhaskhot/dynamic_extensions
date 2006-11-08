
package edu.common.dynamicextensions.processor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domain.BooleanValue;
import edu.common.dynamicextensions.domain.DateValue;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.DoubleValue;
import edu.common.dynamicextensions.domain.LongValue;
import edu.common.dynamicextensions.domain.StringValue;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DoubleAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FloatAttributeInterface;
import edu.common.dynamicextensions.domaininterface.IntegerAttributeInterface;
import edu.common.dynamicextensions.domaininterface.LongAttributeInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.ShortAttributeInterface;
import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.RuleConfigurationObject;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
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
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING))
				{
					attributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE))
				{
					attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN))
				{
					attributeInterface = DomainObjectFactory.getInstance().createBooleanAttribute();
				}
				else if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER))
				{
					attributeInterface = getInterfaceForNumericDataType(attributeUIBeanInformationIntf);
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
			if (attributeInterface instanceof StringAttributeInterface)
			{
				populateStringAttributeInterface((StringAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof BooleanAttributeInterface)
			{
				populateBooleanAttributeInterface((BooleanAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof DateAttributeInterface)
			{
				populateDateAttributeInterface((DateAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof ShortAttributeInterface)
			{
				populateShortAttributeInterface((ShortAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof LongAttributeInterface)
			{
				populateLongAttributeInterface((LongAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof IntegerAttributeInterface)
			{
				populateIntegerAttributeInterface((IntegerAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof FloatAttributeInterface)
			{
				populateFloatAttributeInterface((FloatAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			else if (attributeInterface instanceof DoubleAttributeInterface)
			{
				populateDoubleAttributeInterface((DoubleAttributeInterface) attributeInterface, attributeUIBeanInformationIntf);
			}
			attributeInterface.setName(attributeUIBeanInformationIntf.getName());
			attributeInterface.setDescription(attributeUIBeanInformationIntf.getDescription());

			//Set is identified
			if (attributeInterface instanceof AttributeInterface)
			{
				Boolean isIdentified = new Boolean(attributeUIBeanInformationIntf.getAttributeIdentified());
				((AttributeInterface) attributeInterface).setIsIdentified(isIdentified);
			}

			Collection collection = SemanticPropertyBuilderUtil.getSymanticPropertyCollection(attributeUIBeanInformationIntf
					.getAttributeConceptCode());
			if (collection != null && !collection.isEmpty())
			{
				Iterator iterator = collection.iterator();
				while (iterator.hasNext())
				{
					attributeInterface.addSemanticProperty((SemanticPropertyInterface) iterator.next());
				}
			}
			populateRules(attributeInterface, attributeUIBeanInformationIntf);
		}
		else
		{
			Logger.out.error("Either Attribute interface or attribute information interface is null [" + attributeInterface + "] / ["
					+ attributeUIBeanInformationIntf + "]");
		}

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
		/*if (validationRules != null && validationRules.length == 0)
		{
			validationRules = attributeUIBeanInformationIntf.getTempValidationRules();
		}*/
		if (validationRules != null && validationRules.length != 0)
		{
			String validationRule = "";
			ControlConfigurationsFactory configurationsFactory = ControlConfigurationsFactory.getInstance();
			RuleConfigurationObject ruleConfigurationObject;
			DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
			RuleInterface ruleInterface;
			Collection ruleParameterCollection = new HashSet();
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
	private Collection getRuleParameterCollection(RuleConfigurationObject ruleConfigurationObject,
			AbstractAttributeUIBeanInterface abstractAttributeUIBeanInterface) throws DynamicExtensionsSystemException
	{
		Collection ruleParameterCollection = new HashSet();
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
		PermissibleValueInterface permissibleValueInterface = null;

		if (attributeUIBeanInformationIntf != null)
		{
			String displayChoice = attributeUIBeanInformationIntf.getDisplayChoice();
			if (displayChoice != null)
			{
				if (displayChoice.equalsIgnoreCase(ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED))
				{
					dataEltInterface = DomainObjectFactory.getInstance().createUserDefinedDE();
					String choiceList = attributeUIBeanInformationIntf.getChoiceList();
					if (choiceList != null)
					{
						StringTokenizer strTokenizer = new StringTokenizer(choiceList, ",");
						if (strTokenizer != null)
						{
							while (strTokenizer.hasMoreElements())
							{
								String choice = strTokenizer.nextToken();
								if ((choice != null) && (choice.trim() != null))
								{
									permissibleValueInterface = getPermissibleValueInterface(attributeUIBeanInformationIntf, choice);
									((UserDefinedDE) dataEltInterface).addPermissibleValue(permissibleValueInterface);
								}
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
	private PermissibleValueInterface getPermissibleValueInterface(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf,
			String permissibleValue) throws DynamicExtensionsApplicationException
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
						Date value = Utility.parseDate(permissibleValue);
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
						permissibleValueIntf = getPermissibleValueInterfaceForNumber(attributeUIBeanInformationIntf, permissibleValue);
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
			String permissibleValue) throws DynamicExtensionsApplicationException
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
		/*//Number of digits
		 String strNoOfDigits = attributeInformationIntf.getAttributeDigits();
		 if(strNoOfDigits!=null){
		 try{
		 noOfDigits = Integer.parseInt(strNoOfDigits);
		 }catch (NumberFormatException e){
		 noOfDigits = 0;
		 } 
		 }


		 * If number of decimal places is 0 AND number of digits <= SHORT_LENGTH : Short attribute
		 * If number of decimal places is 0 AND number of digits <= INT_LENGTH : Integer attribute
		 * If number of decimal places is 0 AND number of digits <= LONG_LENGTH : Long attribute 
		 * If Number of decimal places is > 0  <= FLOAT_LENGTH : Float Attribute
		 * If Number of decimal places is > 0  > FLOAT_LENGTH <DOUBLE_LENGTH : Double Attribute
		 */
		if (noOfDecimalPlaces == 0)
		{
			/*if(noOfDigits > 0){
			 if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_SHORT){
			 permissibleValueIntf = DomainObjectFactory.getInstance().createShortValue();
			 Short value = null;
			 try
			 {
			 value = new Short(permissibleValue);
			 }
			 catch (RuntimeException e)
			 {
			 System.out.println("Exception while storing short value");
			 value = new Short("0");
			 }
			 ((ShortValue)permissibleValueIntf).setValue(value);
			 }else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_INT){
			 permissibleValueIntf = DomainObjectFactory.getInstance().createIntegerValue();
			 Integer value = null;
			 try
			 {
			 value = new Integer(permissibleValue);
			 }
			 catch (RuntimeException e)
			 {
			 System.out.println("Exception while storing integer value");
			 value = new Integer("0");
			 }
			 ((IntegerValue)permissibleValueIntf).setValue(value);
			 }else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_LONG){*/
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
			/*	}
			 }
			 else{
			 System.out.println("Too many digits");
			 }*/
		}
		else if (noOfDecimalPlaces > 0)
		{
			/*if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_FLOAT){
			 permissibleValueIntf = DomainObjectFactory.getInstance().createFloatValue();
			 Float value = null;
			 try
			 {
			 value = new Float(permissibleValue);
			 }
			 catch (RuntimeException e)
			 {
			 System.out.println("Exception while storing long value");
			 value = new Float("0");
			 }
			 ((FloatValue)permissibleValueIntf).setValue(value);
			 }else if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_DOUBLE){*/
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
			/*}
			 else{
			 System.out.println("Too many decimal places");
			 }*/
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
	private void populateBooleanAttributeInterface(BooleanAttributeInterface booleanAttributeIntf,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		Boolean defaultValue = new Boolean(attributeUIBeanInformationIntf.getAttributeDefaultValue());
		booleanAttributeIntf.setDefaultValue(defaultValue);
	}

	/**
	 * 
	 * @param dateAttributeIntf : date Attribute ObjectUI Bean containing attribute information entered by user on UI
	 * @param attributeUIBeanInformationIntf : UI Bean containing attribute information entered by user on UI
	 * @throws DynamicExtensionsApplicationException : Exception
	 */
	private void populateDateAttributeInterface(DateAttributeInterface dateAttributeIntf,
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
					String todaysDate = Utility.parseDateToString(new Date(), Constants.DATE_PATTERN_MM_DD_YYYY);
					defaultValue = Utility.parseDate(todaysDate);
				}
				else if (dateValueType.equalsIgnoreCase(ProcessorConstants.DATE_VALUE_SELECT))
				{
					if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
					{
						defaultValue = Utility.parseDate(attributeUIBeanInformationIntf.getAttributeDefaultValue());
					}
				}

			}
			catch (Exception e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
		}
		dateAttributeIntf.setDefaultValue(defaultValue);
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
	private void populateStringAttributeInterface(StringAttributeInterface stringAttributeIntf,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
	{
		stringAttributeIntf.setDefaultValue(attributeUIBeanInformationIntf.getAttributeDefaultValue());
		Integer size = null;
		try
		{
			if (attributeUIBeanInformationIntf.getAttributeSize() != null)
			{
				if (attributeUIBeanInformationIntf.getAttributeSize().trim().equals(""))
				{
					size = new Integer(0);
				}
				else
				{
					size = new Integer(attributeUIBeanInformationIntf.getAttributeSize());
				}
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
	private void populateShortAttributeInterface(ShortAttributeInterface shortAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
		{
			Short defaultValue = new Short(attributeUIBeanInformationIntf.getAttributeDefaultValue());
			shortAttributeInterface.setDefaultValue(defaultValue);
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
	private void populateIntegerAttributeInterface(IntegerAttributeInterface integerAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf) throws DynamicExtensionsApplicationException
	{
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
			integerAttributeInterface.setDefaultValue(defaultValue);
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
	private void populateLongAttributeInterface(LongAttributeInterface longAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
		{
			Long defaultValue = new Long(attributeUIBeanInformationIntf.getAttributeDefaultValue());
			longAttributeInterface.setDefaultValue(defaultValue);
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
	private void populateFloatAttributeInterface(FloatAttributeInterface floatAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
		{
			Float defaultValue = new Float(attributeUIBeanInformationIntf.getAttributeDefaultValue());
			floatAttributeInterface.setDefaultValue(defaultValue);
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
	private void populateDoubleAttributeInterface(DoubleAttributeInterface doubleAttributeInterface,
			AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		if (attributeUIBeanInformationIntf.getAttributeDefaultValue() != null)
		{
			Double defaultValue = new Double(attributeUIBeanInformationIntf.getAttributeDefaultValue());
			doubleAttributeInterface.setDefaultValue(defaultValue);
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
	 * @throws DynamicExtensionsApplicationException : Exception
	 */
	private AttributeInterface getInterfaceForNumericDataType(AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
			throws DynamicExtensionsApplicationException
	{
		AttributeInterface numberAttribIntf = null;
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
					noOfDecimalPlaces = new Integer(0); //Assume 0 for blank values
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
		/*//Number of digits
		 String strNoOfDigits = attributeInformationIntf.getAttributeDigits();
		 if(strNoOfDigits!=null){
		 try{
		 noOfDigits = Integer.parseInt(strNoOfDigits);
		 }catch (NumberFormatException e){
		 noOfDigits = 0;
		 } 
		 }


		 * If number of decimal places is 0 AND number of digits <= SHORT_LENGTH : Short attribute
		 * If number of decimal places is 0 AND number of digits <= INT_LENGTH : Integer attribute
		 * If number of decimal places is 0 AND number of digits <= LONG_LENGTH : Long attribute 
		 * If Number of decimal places is > 0  <= FLOAT_LENGTH : Float Attribute
		 * If Number of decimal places is > 0  > FLOAT_LENGTH <DOUBLE_LENGTH : Double Attribute

		 if(noOfDecimalPlaces == 0){
		 if(noOfDigits > 0){
		 if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_SHORT){
		 numberAttribIntf = DomainObjectFactory.getInstance().createShortAttribute();
		 }else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_INT){
		 numberAttribIntf = DomainObjectFactory.getInstance().createIntegerAttribute();
		 }else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_LONG){
		 numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
		 }
		 }
		 else{
		 System.out.println("Too many digits");
		 }
		 }*/
		if (noOfDecimalPlaces == 0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
		}
		if (noOfDecimalPlaces > 0)
		{
			/*if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_FLOAT){
			 numberAttribIntf = DomainObjectFactory.getInstance().createFloatAttribute();
			 }else if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_DOUBLE){*/
			numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttribute();
			/*}
			 else{
			 System.out.println("Too many decimal places");
			 }*/
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
			attributeUIBeanInformationIntf.setChoiceList(getChoiceList(attributeInterface, attributeUIBeanInformationIntf));

			if (attributeInterface instanceof StringAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_STRING);
				attributeUIBeanInformationIntf.setAttributeDefaultValue(((StringAttributeInterface) attributeInterface).getDefaultValue());
				Integer size = ((StringAttributeInterface) attributeInterface).getSize();
				if (size != null)
				{
					attributeUIBeanInformationIntf.setAttributeSize(size.toString());
				}
			}
			else if (attributeInterface instanceof DateAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_DATE);
				if (((DateAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					String defaultValue = Utility.parseDateToString(((DateAttributeInterface) attributeInterface).getDefaultValue(),
							Constants.DATE_PATTERN_MM_DD_YYYY);
					attributeUIBeanInformationIntf.setAttributeDefaultValue(defaultValue);
				}
				else
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue("");
				}
				String dateFormat = ((DateAttributeInterface) attributeInterface).getFormat();
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
			else if (attributeInterface instanceof BooleanAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_BOOLEAN);
				if (((BooleanAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((BooleanAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
			}
			else if (attributeInterface instanceof IntegerAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if (((IntegerAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((IntegerAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
				attributeUIBeanInformationIntf.setAttributeMeasurementUnits((((IntegerAttributeInterface) attributeInterface).getMeasurementUnits()));
				attributeUIBeanInformationIntf.setAttributeDecimalPlaces(((IntegerAttributeInterface) attributeInterface).getDecimalPlaces());
				attributeUIBeanInformationIntf.setAttributeDigits((((IntegerAttributeInterface) attributeInterface).getDigits()));

			}
			else if (attributeInterface instanceof ShortAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if (((ShortAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((ShortAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
				attributeUIBeanInformationIntf.setAttributeMeasurementUnits((((ShortAttributeInterface) attributeInterface).getMeasurementUnits()));
				attributeUIBeanInformationIntf.setAttributeDecimalPlaces(((ShortAttributeInterface) attributeInterface).getDecimalPlaces());
				attributeUIBeanInformationIntf.setAttributeDigits((((ShortAttributeInterface) attributeInterface).getDigits()));

			}
			else if (attributeInterface instanceof LongAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if (((LongAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((LongAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
				attributeUIBeanInformationIntf.setAttributeMeasurementUnits((((LongAttributeInterface) attributeInterface).getMeasurementUnits()));
				attributeUIBeanInformationIntf.setAttributeDecimalPlaces(((LongAttributeInterface) attributeInterface).getDecimalPlaces());
				attributeUIBeanInformationIntf.setAttributeDigits((((LongAttributeInterface) attributeInterface).getDigits()));

			}
			else if (attributeInterface instanceof FloatAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if (((FloatAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((FloatAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
				attributeUIBeanInformationIntf.setAttributeMeasurementUnits((((FloatAttributeInterface) attributeInterface).getMeasurementUnits()));
				attributeUIBeanInformationIntf.setAttributeDecimalPlaces(((FloatAttributeInterface) attributeInterface).getDecimalPlaces());
				attributeUIBeanInformationIntf.setAttributeDigits((((FloatAttributeInterface) attributeInterface).getDigits()));
			}
			else if (attributeInterface instanceof DoubleAttributeInterface)
			{
				attributeUIBeanInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if (((DoubleAttributeInterface) attributeInterface).getDefaultValue() != null)
				{
					attributeUIBeanInformationIntf.setAttributeDefaultValue(((DoubleAttributeInterface) attributeInterface).getDefaultValue()
							.toString());
				}
				attributeUIBeanInformationIntf.setAttributeMeasurementUnits((((DoubleAttributeInterface) attributeInterface).getMeasurementUnits()));
				attributeUIBeanInformationIntf.setAttributeDecimalPlaces(((DoubleAttributeInterface) attributeInterface).getDecimalPlaces());
				attributeUIBeanInformationIntf.setAttributeDigits((((DoubleAttributeInterface) attributeInterface).getDigits()));
			}

		}
	}

	/**
	 * 
	 * @param attributeInterface : Attribute interface
	 * @param attributeUIBeanInformationIntf    : UI Bean containing attribute information to be displayed on UI 
	 * @return Comma separated list of permissible values
	 */
	private String getChoiceList(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeUIBeanInformationIntf)
	{
		Object permissibleValueObjectValue = null;
		String choiceList = "";
		if ((attributeUIBeanInformationIntf != null) && (attributeInterface != null))
		{
			if (attributeInterface instanceof AttributeInterface)
			{
				DataElementInterface dataEltInterface = ((AttributeInterface) attributeInterface).getDataElement();
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
								permissibleValueObjectValue = permissibleValueIntf.getValueAsObject();
								if ((permissibleValueObjectValue != null) && (permissibleValueObjectValue.toString() != null)
										&& (permissibleValueObjectValue.toString().trim() != ""))
								{
									choiceList = choiceList + "," + permissibleValueObjectValue.toString().trim();
								}
							}
						}
					}
				}
			}
		}
		return choiceList;
	}
}
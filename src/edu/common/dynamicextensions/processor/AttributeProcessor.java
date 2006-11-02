/*
 * Created on Oct 11, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
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
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.RuleConfigurationObject;
import edu.common.dynamicextensions.ui.util.SemanticPropertyBuilderUtil;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;

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
	protected AttributeProcessor () {

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static AttributeProcessor getInstance () {
		return new AttributeProcessor();
	}

	public AttributeInterface createAttribute(AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		AttributeInterface attributeInterface = null;
		if(attributeInformationIntf!=null)
		{
			String attributeType = attributeInformationIntf.getDataType();
			if(attributeType!=null)
			{
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING)) {
					attributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
				} else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE)) {
					attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN)) {
					attributeInterface = DomainObjectFactory.getInstance().createBooleanAttribute();
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER)){
					attributeInterface = getInterfaceForNumericDataType(attributeInformationIntf);
				}
			}
		}
		return attributeInterface;

	}

	public void populateAttribute(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if((attributeInformationIntf!=null)&&(attributeInterface!=null))
		{
			if(attributeInterface instanceof StringAttributeInterface)
			{
				populateStringAttributeInterface((StringAttributeInterface) attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof BooleanAttributeInterface)
			{
				populateBooleanAttributeInterface((BooleanAttributeInterface) attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof DateAttributeInterface)
			{
				populateDateAttributeInterface((DateAttributeInterface) attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof ShortAttributeInterface)
			{
				populateShortAttributeInterface((ShortAttributeInterface)attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof LongAttributeInterface)
			{
				populateLongAttributeInterface((LongAttributeInterface)attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof IntegerAttributeInterface)
			{
				populateIntegerAttributeInterface((IntegerAttributeInterface)attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof FloatAttributeInterface)
			{
				populateFloatAttributeInterface((FloatAttributeInterface)attributeInterface, attributeInformationIntf);
			}else if(attributeInterface instanceof DoubleAttributeInterface)
			{
				populateDoubleAttributeInterface((DoubleAttributeInterface)attributeInterface, attributeInformationIntf);
			}
			attributeInterface.setName(attributeInformationIntf.getName());
			attributeInterface.setDescription(attributeInformationIntf.getDescription());

			//Set is identified
			if(attributeInterface instanceof AttributeInterface)
			{
				Boolean isIdentified = new Boolean(attributeInformationIntf.getAttributeIdentified());
				((AttributeInterface)attributeInterface).setIsIdentified(isIdentified);
			}

			Collection collection =  SemanticPropertyBuilderUtil.getSymanticPropertyCollection(attributeInformationIntf.getAttributeConceptCode());
			if (collection != null && !collection.isEmpty()) {
				Iterator iterator = collection.iterator();
				while (iterator.hasNext()) {
					attributeInterface.addSemanticProperty((SemanticPropertyInterface) iterator.next());
				}
			}
			populateRules(attributeInterface,attributeInformationIntf);
		}
		else
		{
			System.out.println("Either Attribute interface or attribute information interface is null [" + attributeInterface + "] / [" + attributeInformationIntf + "]");
		}

	}

	/**
	 * 
	 * @param attributeInterface
	 * @param attributeInformationIntf
	 */
	public void populateRules(AbstractAttributeInterface abstractAttributeInterface,AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		String[] validationRules  = attributeInformationIntf.getValidationRules();
		if(validationRules != null)
		{
			String validationRule = "";
			ControlConfigurationsFactory configurationsFactory = ControlConfigurationsFactory.getInstance();
			RuleConfigurationObject ruleConfigurationObject;
			DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
			RuleInterface ruleInterface;
			for (int counter= 0 ; counter < validationRules.length;counter++)
			{
				validationRule = validationRules[counter];
				ruleConfigurationObject = configurationsFactory.getRuleObject(validationRule);
				ruleInterface = domainObjectFactory.createRule();
				ruleInterface.setName(ruleConfigurationObject.getRuleName());
				abstractAttributeInterface.addRule(ruleInterface);

			}
		}
	}

	/**
	 * 
	 * @param ruleConfigurationObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection getRuleParameterCollection(RuleConfigurationObject ruleConfigurationObject,
			AbstractAttributeUIBeanInterface abstractAttributeUIBeanInterface)
	{
		Collection<RuleParameterInterface> RuleParameterCollection = new HashSet<RuleParameterInterface>();
		DomainObjectFactory domainObjectFactory = DomainObjectFactory .getInstance(); 
		List ruleParametersList = ruleConfigurationObject.getRuleParametersList();
		if(ruleParametersList != null )
		{
			StringBuffer operationNameBuff = null;
			Iterator ruleParametersListIter = ruleParametersList.iterator();
			while(ruleParametersListIter.hasNext())
			{
				NameValueBean param = (NameValueBean)ruleParametersListIter.next();

				String paramName = param.getName();
				operationNameBuff = new StringBuffer(paramName);
				operationNameBuff.setCharAt(0, Character.toUpperCase(operationNameBuff.charAt(0)));				
				String methodName = "get" + operationNameBuff.toString();

				try {
					Class clas = Class.forName("edu.common.dynamicextensions.ui.interfaces.AbstractAttributeUIBeanInterface");
					Class[] types = new Class[] {};

					Method method = clas.getMethod(methodName, types);
					Object result = method.invoke(abstractAttributeUIBeanInterface, new Object[0]);
					RuleParameterInterface ruleParameterInterface =   domainObjectFactory.createRuleParameter();
					ruleParameterInterface.setName(paramName);
					ruleParameterInterface.setValue(result.toString());
					RuleParameterCollection.add(ruleParameterInterface);

				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



				//ruleParameter.getValue();

			}
		}
		return RuleParameterCollection;
	}
	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	public DataElementInterface getDataElementInterface(AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		DataElementInterface  dataEltInterface = null;
		PermissibleValueInterface permissibleValueInterface = null;

		if(attributeInformationIntf!=null)
		{
			String displayChoice = attributeInformationIntf.getDisplayChoice();
			if(displayChoice!=null)
			{
				if(displayChoice.equalsIgnoreCase(ProcessorConstants.DISPLAY_CHOICE_USER_DEFINED))
				{
					dataEltInterface = DomainObjectFactory.getInstance().createUserDefinedDE();
					String choiceList = attributeInformationIntf.getChoiceList();
					if(choiceList!=null)
					{
						StringTokenizer strTokenizer = new StringTokenizer(choiceList,",");
						if(strTokenizer!=null)
						{
							while(strTokenizer.hasMoreElements())
							{
								String choice = strTokenizer.nextToken();
								if((choice!=null)&&(choice.trim()!=null))
								{
									permissibleValueInterface  = getPermissibleValueInterface(attributeInformationIntf, choice);
									((UserDefinedDE)dataEltInterface).addPermissibleValue(permissibleValueInterface);
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
	 * @param attributeInformationIntf
	 * @param permissibleValue 
	 * @return
	 */
	private PermissibleValueInterface getPermissibleValueInterface(AbstractAttributeUIBeanInterface attributeInformationIntf, String permissibleValue)
	{
		PermissibleValueInterface permissibleValueIntf = null;
		if(attributeInformationIntf!=null)
		{
			String attributeType = attributeInformationIntf.getDataType();
			if(attributeType!=null)
			{
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING)) {
					permissibleValueIntf = DomainObjectFactory.getInstance().createStringValue();
					((StringValue)permissibleValueIntf).setValue(permissibleValue); 
				} else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE)) {
					permissibleValueIntf = DomainObjectFactory.getInstance().createDateValue();
					Date value = null;
					try
					{
						value = Utility.parseDate(permissibleValue);

					}
					catch (ParseException e)
					{
						System.out.println("Exception while storing date value");
						value = new Date();
					}
					((DateValue)permissibleValueIntf).setValue(value);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN)) {
					permissibleValueIntf = DomainObjectFactory.getInstance().createBooleanValue();
					Boolean value = null;
					try
					{
						value = new Boolean(permissibleValue);
					}
					catch (RuntimeException e)
					{
						System.out.println("Exception while storing boolean value");
						value = new Boolean(false);
					}
					((BooleanValue)permissibleValueIntf).setValue(value);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER)){
					permissibleValueIntf = getPermissibleValueInterfaceForNumber(attributeInformationIntf, permissibleValue);
				}

			}
		}
		return permissibleValueIntf;
	}

	/**
	 * @param attributeInformationIntf
	 * @param permissibleValue TODO
	 * @return
	 */
	private PermissibleValueInterface getPermissibleValueInterfaceForNumber(AbstractAttributeUIBeanInterface attributeInformationIntf, String permissibleValue)
	{

		PermissibleValueInterface permissibleValueIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		int noOfDecimalPlaces = 0  ;
		//Number of decimal places 

		String strNoOfDecimalPlaces = attributeInformationIntf.getAttributeDecimalPlaces();
		if(strNoOfDecimalPlaces!=null){
			try{
				noOfDecimalPlaces = Integer.parseInt(strNoOfDecimalPlaces);
			}catch (NumberFormatException e){
				noOfDecimalPlaces = 0;
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
		if(noOfDecimalPlaces == 0){
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
			catch (RuntimeException e)
			{
				System.out.println("Exception while storing long value");
				value = new Long("0");
			}
			((LongValue)permissibleValueIntf).setValue(value);
			/*	}
			}
			else{
				System.out.println("Too many digits");
			}*/
		}
		else if(noOfDecimalPlaces > 0)
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
			catch (RuntimeException e)
			{
				System.out.println("Exception while storing long value");
				value = new Double("0");
			}
			((DoubleValue)permissibleValueIntf).setValue(value);
			/*}
			else{
				System.out.println("Too many decimal places");
			}*/
		}
		return permissibleValueIntf;

	}

	public AttributeInterface createAndPopulateAttribute(AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		AttributeInterface attributeInterface = createAttribute(attributeInformationIntf);
		populateAttribute(attributeInterface, attributeInformationIntf);
		return attributeInterface;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateBooleanAttributeInterface(BooleanAttributeInterface booleanAttributeIntf, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		Boolean defaultValue = new Boolean(attributeInformationIntf.getAttributeDefaultValue());
		booleanAttributeIntf.setDefaultValue(defaultValue);
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateDateAttributeInterface(DateAttributeInterface dateAttributeIntf, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		String dateValueType = attributeInformationIntf.getDateValueType();
		Date defaultValue = null;
		if(dateValueType!=null)
		{
			try
			{
				if(dateValueType.equalsIgnoreCase(ProcessorConstants.DATE_VALUE_TODAY))
				{
					String todaysDate = Utility.parseDateToString(new Date(), Constants.DATE_PATTERN_MM_DD_YYYY);
					defaultValue = Utility.parseDate(todaysDate);
				}
				else if(dateValueType.equalsIgnoreCase(ProcessorConstants.DATE_VALUE_SELECT))
				{
					if(attributeInformationIntf.getAttributeDefaultValue()!=null)
					{
						defaultValue = Utility.parseDate(attributeInformationIntf.getAttributeDefaultValue());
						System.out.println("Date Def Value = " + defaultValue);
					}
				}

			}
			catch (ParseException e)
			{
				System.out.println("Exception while saving date def value");
				defaultValue = new Date();
			}
		}
		dateAttributeIntf.setDefaultValue(defaultValue);
//		Set Date format based on the UI selection : DATE ONLY or DATE And TIME
		if(attributeInformationIntf.getFormat()!=null)
		{
			if(attributeInformationIntf.getFormat().equalsIgnoreCase(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY))
			{
				dateAttributeIntf.setFormat(ProcessorConstants.DATE_ONLY_FORMAT);
			}
			else if(attributeInformationIntf.getFormat().equalsIgnoreCase(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME))
			{
				dateAttributeIntf.setFormat(ProcessorConstants.DATE_TIME_FORMAT);
			}
		}
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateStringAttributeInterface(StringAttributeInterface stringAttributeIntf, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		stringAttributeIntf.setDefaultValue(attributeInformationIntf.getAttributeDefaultValue());
		Integer size;
		try
		{
			size = new Integer(attributeInformationIntf.getAttributeSize());
		}
		catch (NumberFormatException e)
		{
			size = new Integer(0);
		}
		stringAttributeIntf.setSize(size);
	}
	/**
	 * 
	 * @param shortAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateShortAttributeInterface(ShortAttributeInterface shortAttributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if(attributeInformationIntf.getAttributeDefaultValue()!=null)
		{
			Short defaultValue = new Short(attributeInformationIntf.getAttributeDefaultValue());
			shortAttributeInterface.setDefaultValue(defaultValue);
		}
		shortAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		shortAttributeInterface.setDecimalPlaces(attributeInformationIntf.getAttributeDecimalPlaces());
		shortAttributeInterface.setDigits(attributeInformationIntf.getAttributeDigits());
		shortAttributeInterface.setSize(attributeInformationIntf.getAttributeSize());

	}

	/**
	 * 
	 * @param integerAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateIntegerAttributeInterface(IntegerAttributeInterface integerAttributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if(attributeInformationIntf.getAttributeDefaultValue()!=null)
		{
			Integer defaultValue;
			try
			{
				defaultValue = new Integer(attributeInformationIntf.getAttributeDefaultValue());
			}
			catch (NumberFormatException e)
			{
				defaultValue = new Integer(0);
			}
			integerAttributeInterface.setDefaultValue(defaultValue);
		}
		integerAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		integerAttributeInterface.setDecimalPlaces(attributeInformationIntf.getAttributeDecimalPlaces());
		integerAttributeInterface.setDigits(attributeInformationIntf.getAttributeDigits());
		integerAttributeInterface.setSize(attributeInformationIntf.getAttributeSize());

	}

	/**
	 * 
	 * @param longAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateLongAttributeInterface(LongAttributeInterface longAttributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if(attributeInformationIntf.getAttributeDefaultValue()!=null)
		{
			Long defaultValue = new Long(attributeInformationIntf.getAttributeDefaultValue());
			longAttributeInterface.setDefaultValue(defaultValue);
		}
		longAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		longAttributeInterface.setDecimalPlaces(attributeInformationIntf.getAttributeDecimalPlaces());
		longAttributeInterface.setDigits(attributeInformationIntf.getAttributeDigits());
		longAttributeInterface.setSize(attributeInformationIntf.getAttributeSize());

	}
	/**
	 * 
	 * @param floatAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateFloatAttributeInterface(FloatAttributeInterface floatAttributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if(attributeInformationIntf.getAttributeDefaultValue()!=null)
		{
			Float defaultValue = new Float(attributeInformationIntf.getAttributeDefaultValue());
			floatAttributeInterface.setDefaultValue(defaultValue);
		}
		floatAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		floatAttributeInterface.setDecimalPlaces(attributeInformationIntf.getAttributeDecimalPlaces());
		floatAttributeInterface.setDigits(attributeInformationIntf.getAttributeDigits());
		floatAttributeInterface.setSize(attributeInformationIntf.getAttributeSize());

	}

	private void populateDoubleAttributeInterface(DoubleAttributeInterface doubleAttributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if(attributeInformationIntf.getAttributeDefaultValue()!=null)
		{
			Double defaultValue = new Double(attributeInformationIntf.getAttributeDefaultValue());
			doubleAttributeInterface.setDefaultValue(defaultValue);
		}
		doubleAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		doubleAttributeInterface.setDecimalPlaces(attributeInformationIntf.getAttributeDecimalPlaces());
		doubleAttributeInterface.setDigits(attributeInformationIntf.getAttributeDigits());
		doubleAttributeInterface.setSize(attributeInformationIntf.getAttributeSize());
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForNumericDataType(AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		AttributeInterface numberAttribIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		int noOfDecimalPlaces = 0;
		//Number of decimal places 

		String strNoOfDecimalPlaces = attributeInformationIntf.getAttributeDecimalPlaces();
		if(strNoOfDecimalPlaces!=null){
			try{
				noOfDecimalPlaces = Integer.parseInt(strNoOfDecimalPlaces);
			}catch (NumberFormatException e){
				noOfDecimalPlaces = 0;
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
		if(noOfDecimalPlaces==0)
		{
			numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
		}
		if(noOfDecimalPlaces > 0)
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

	public void populateAttributeUIBeanInterface(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		if((attributeInformationIntf!=null)&&(attributeInterface!=null))
		{
			attributeInformationIntf.setName(attributeInterface.getName());
			attributeInformationIntf.setDescription(attributeInterface.getDescription());

			//is Identified
			if(attributeInterface instanceof AttributeInterface)
			{
				Boolean isIdentified = ((AttributeInterface)attributeInterface).getIsIdentified();
				if(isIdentified!=null)
				{
					attributeInformationIntf.setAttributeIdentified(isIdentified.toString());	
				}
			}
			//Concept code
			if (!attributeInterface.getSemanticPropertyCollection().isEmpty())
			{
				attributeInformationIntf.setAttributeConceptCode(SemanticPropertyBuilderUtil.getConceptCodeString(attributeInterface));
			}

			//Permissible values
			attributeInformationIntf.setChoiceList(getChoiceList(attributeInterface,attributeInformationIntf));

			if(attributeInterface instanceof StringAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_STRING);
				attributeInformationIntf.setAttributeDefaultValue(((StringAttributeInterface)attributeInterface).getDefaultValue());
				Integer size = ((StringAttributeInterface)attributeInterface).getSize();
				if(size!=null){
					attributeInformationIntf.setAttributeSize(size.toString());
				}
			}else if(attributeInterface instanceof DateAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_DATE);
				if(((DateAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					String defaultValue = Utility.parseDateToString(((DateAttributeInterface)attributeInterface).getDefaultValue(),Constants.DATE_PATTERN_MM_DD_YYYY);
					attributeInformationIntf.setAttributeDefaultValue(defaultValue);
				}
				else
				{
					attributeInformationIntf.setAttributeDefaultValue("");
				}
				String dateFormat = ((DateAttributeInterface)attributeInterface).getFormat();
				if(dateFormat!=null)
				{
					if(dateFormat.equalsIgnoreCase(ProcessorConstants.DATE_ONLY_FORMAT))
					{
						attributeInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY);
					}
					else if(dateFormat.equalsIgnoreCase(ProcessorConstants.DATE_TIME_FORMAT))
					{
						attributeInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME);
					} 
				}
				else //Default will be date only
				{
					attributeInformationIntf.setFormat(ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY);
				}
			}
			else if(attributeInterface instanceof BooleanAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_BOOLEAN);
				if(((BooleanAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((BooleanAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
			}
			else if(attributeInterface instanceof IntegerAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if(((IntegerAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((IntegerAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
				attributeInformationIntf.setAttributeMeasurementUnits((((IntegerAttributeInterface)attributeInterface).getMeasurementUnits()));
				attributeInformationIntf.setAttributeDecimalPlaces(((IntegerAttributeInterface)attributeInterface).getDecimalPlaces());
				attributeInformationIntf.setAttributeDigits((((IntegerAttributeInterface)attributeInterface).getDigits()));
				attributeInformationIntf.setAttributeSize((((IntegerAttributeInterface)attributeInterface).getSize()));


			}
			else if(attributeInterface instanceof ShortAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if(((ShortAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((ShortAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
				attributeInformationIntf.setAttributeMeasurementUnits((((ShortAttributeInterface)attributeInterface).getMeasurementUnits()));
				attributeInformationIntf.setAttributeDecimalPlaces(((ShortAttributeInterface)attributeInterface).getDecimalPlaces());
				attributeInformationIntf.setAttributeDigits((((ShortAttributeInterface)attributeInterface).getDigits()));
				attributeInformationIntf.setAttributeSize((((ShortAttributeInterface)attributeInterface).getSize()));

			}
			else if(attributeInterface instanceof LongAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if(((LongAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((LongAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
				attributeInformationIntf.setAttributeMeasurementUnits((((LongAttributeInterface)attributeInterface).getMeasurementUnits()));
				attributeInformationIntf.setAttributeDecimalPlaces(((LongAttributeInterface)attributeInterface).getDecimalPlaces());
				attributeInformationIntf.setAttributeDigits((((LongAttributeInterface)attributeInterface).getDigits()));
				attributeInformationIntf.setAttributeSize((((LongAttributeInterface)attributeInterface).getSize()));

			}
			else if(attributeInterface instanceof FloatAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if(((FloatAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((FloatAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
				attributeInformationIntf.setAttributeMeasurementUnits((((FloatAttributeInterface)attributeInterface).getMeasurementUnits()));
				attributeInformationIntf.setAttributeDecimalPlaces(((FloatAttributeInterface)attributeInterface).getDecimalPlaces());
				attributeInformationIntf.setAttributeDigits((((FloatAttributeInterface)attributeInterface).getDigits()));
				attributeInformationIntf.setAttributeSize((((FloatAttributeInterface)attributeInterface).getSize()));
			}
			else if(attributeInterface instanceof DoubleAttributeInterface)
			{
				attributeInformationIntf.setDataType(ProcessorConstants.DATATYPE_NUMBER);
				if(((DoubleAttributeInterface)attributeInterface).getDefaultValue()!=null)
				{
					attributeInformationIntf.setAttributeDefaultValue(((DoubleAttributeInterface)attributeInterface).getDefaultValue().toString());
				}
				attributeInformationIntf.setAttributeMeasurementUnits((((DoubleAttributeInterface)attributeInterface).getMeasurementUnits()));
				attributeInformationIntf.setAttributeDecimalPlaces(((DoubleAttributeInterface)attributeInterface).getDecimalPlaces());
				attributeInformationIntf.setAttributeDigits((((DoubleAttributeInterface)attributeInterface).getDigits()));
				attributeInformationIntf.setAttributeSize((((DoubleAttributeInterface)attributeInterface).getSize()));
			}

		}
	}


	/**
	 * @param attributeInterface
	 * @param attributeInformationIntf
	 * @return
	 */
	private String getChoiceList(AbstractAttributeInterface attributeInterface, AbstractAttributeUIBeanInterface attributeInformationIntf)
	{
		Object permissibleValueObjectValue = null; 
		String choiceList = "";
		if((attributeInformationIntf!=null)&&(attributeInterface!=null))
		{
			if(attributeInterface instanceof AttributeInterface)
			{
				DataElementInterface dataEltInterface = ((AttributeInterface)attributeInterface).getDataElement();
				if(dataEltInterface!=null)
				{
					if(dataEltInterface instanceof UserDefinedDEInterface)
					{
						attributeInformationIntf.setDisplayChoice("UserDefined");
						UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface)dataEltInterface;
						Collection userDefinedValues = userDefinedDE.getPermissibleValueCollection();
						if(userDefinedValues!=null)
						{
							PermissibleValueInterface permissibleValueIntf = null;
							Iterator userDefinedValuesIterator = userDefinedValues.iterator();
							while(userDefinedValuesIterator.hasNext())
							{
								permissibleValueIntf = (PermissibleValueInterface)userDefinedValuesIterator.next();
								permissibleValueObjectValue = permissibleValueIntf.getValueAsObject();
								if((permissibleValueObjectValue!=null)&&(permissibleValueObjectValue.toString()!=null) && (permissibleValueObjectValue.toString().trim()!=""))
								{
									choiceList =  choiceList + "," + permissibleValueObjectValue.toString().trim();
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
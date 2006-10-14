/*
 * Created on Oct 11, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.util.Date;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DoubleAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FloatAttributeInterface;
import edu.common.dynamicextensions.domaininterface.IntegerAttributeInterface;
import edu.common.dynamicextensions.domaininterface.LongAttributeInterface;
import edu.common.dynamicextensions.domaininterface.ShortAttributeInterface;
import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.common.dynamicextensions.ui.interfaces.AbstractAttributeInformationInterface;

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

	public AttributeInterface createAttribute(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		System.out.println("Creating Attribute ");
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
	
	public void populateAttribute(AttributeInterface attributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		System.out.println("Populating attribute");
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
			System.out.println("Attribute Name = " + attributeInterface.getName());
			attributeInterface.setDescription(attributeInformationIntf.getDescription());
			System.out.println("Attribute Desc = " + attributeInterface.getDescription());
		}
		else
		{
			System.out.println("Either Attribute interface or attribute information interface is null [" + attributeInterface + "] / [" + attributeInformationIntf + "]");
		}
	}
	
	

	public AttributeInterface createAndPopulateAttribute(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		AttributeInterface attributeInterface = createAttribute(attributeInformationIntf);
		populateAttribute(attributeInterface, attributeInformationIntf);
		return attributeInterface;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateBooleanAttributeInterface(BooleanAttributeInterface booleanAttributeIntf, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Boolean defaultValue = new Boolean(attributeInformationIntf.getAttributeDefaultValue());
		booleanAttributeIntf.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + booleanAttributeIntf.getDefaultValue());
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateDateAttributeInterface(DateAttributeInterface dateAttributeIntf, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Date defaultValue  = new Date(attributeInformationIntf.getAttributeDefaultValue());
		dateAttributeIntf.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + dateAttributeIntf.getDefaultValue());
		dateAttributeIntf.setFormat(attributeInformationIntf.getFormat());
		System.out.println("Attribute Format = " + dateAttributeIntf.getFormat());
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private void populateStringAttributeInterface(StringAttributeInterface stringAttributeIntf, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		System.out.println("String Data Tupe");
		stringAttributeIntf.setDefaultValue(attributeInformationIntf.getAttributeDefaultValue());
		System.out.println("Attribute Def Value = " + stringAttributeIntf.getDefaultValue());
		Integer size = new Integer(attributeInformationIntf.getAttributeSize());
		stringAttributeIntf.setSize(size);
		System.out.println("Attribute Size = " + stringAttributeIntf.getSize());
	}
	/**
	 * 
	 * @param shortAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateShortAttributeInterface(ShortAttributeInterface shortAttributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Short defaultValue = new Short(attributeInformationIntf.getAttributeDefaultValue());
		shortAttributeInterface.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + shortAttributeInterface.getDefaultValue());
		shortAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		System.out.println("Attribute Measuremt Attrib = " + shortAttributeInterface.getMeasurementUnits());
	}
	
	/**
	 * 
	 * @param integerAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateIntegerAttributeInterface(IntegerAttributeInterface integerAttributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Integer defaultValue = new Integer(attributeInformationIntf.getAttributeDefaultValue());
		integerAttributeInterface.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + integerAttributeInterface.getDefaultValue());
		integerAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		System.out.println("Attribute Measuremt Attrib = " + integerAttributeInterface.getMeasurementUnits());
	}
	
	/**
	 * 
	 * @param longAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateLongAttributeInterface(LongAttributeInterface longAttributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Long defaultValue = new Long(attributeInformationIntf.getAttributeDefaultValue());
		longAttributeInterface.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + longAttributeInterface.getDefaultValue());
		longAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		System.out.println("Attribute Measuremt Attrib = " + longAttributeInterface.getMeasurementUnits());
	}
	/**
	 * 
	 * @param floatAttributeInterface
	 * @param attributeInformationIntf
	 */
	private void populateFloatAttributeInterface(FloatAttributeInterface floatAttributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Float defaultValue = new Float(attributeInformationIntf.getAttributeDefaultValue());
		floatAttributeInterface.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + floatAttributeInterface.getDefaultValue());
		floatAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		System.out.println("Attribute Measuremt Attrib = " + floatAttributeInterface.getMeasurementUnits());
	}
	
	private void populateDoubleAttributeInterface(DoubleAttributeInterface doubleAttributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		Double defaultValue = new Double(attributeInformationIntf.getAttributeDefaultValue());
		doubleAttributeInterface.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + doubleAttributeInterface.getDefaultValue());
		doubleAttributeInterface.setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
		System.out.println("Attribute Measuremt Attrib = " + doubleAttributeInterface.getMeasurementUnits());
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForNumericDataType(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		AttributeInterface numberAttribIntf = null;
		//If it is numberic it can either be float, simple integer, etc based on number of decimals
		int noOfDecimalPlaces = 0 , noOfDigits = 0;
		//Number of decimal places 

		String strNoOfDecimalPlaces = attributeInformationIntf.getAttributeDecimalPlaces();
		if(strNoOfDecimalPlaces!=null){
			try{
				noOfDecimalPlaces = Integer.parseInt(strNoOfDecimalPlaces);
			}catch (NumberFormatException e){
				noOfDecimalPlaces = 0;
			} 
		}
		//Number of digits
		String strNoOfDigits = attributeInformationIntf.getAttributeDigits();
		if(strNoOfDigits!=null){
			try{
				noOfDigits = Integer.parseInt(strNoOfDigits);
			}catch (NumberFormatException e){
				noOfDigits = 0;
			} 
		}

		System.out.println("No Of Digits " + noOfDigits + " No Of decimals " + noOfDecimalPlaces);
		/*
		 * If number of decimal places is 0 AND number of digits <= SHORT_LENGTH : Short attribute
		 * If number of decimal places is 0 AND number of digits <= INT_LENGTH : Integer attribute
		 * If number of decimal places is 0 AND number of digits <= LONG_LENGTH : Long attribute 
		 * If Number of decimal places is > 0  <= FLOAT_LENGTH : Float Attribute
		 * If Number of decimal places is > 0  > FLOAT_LENGTH <DOUBLE_LENGTH : Double Attribute
		 */
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
		}
		else if(noOfDecimalPlaces > 0)
		{
			if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_FLOAT){
				numberAttribIntf = DomainObjectFactory.getInstance().createFloatAttribute();
			}else if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_DOUBLE){
				numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttribute();
			}
			else{
				System.out.println("Too many decimal places");
			}
		}
		return numberAttribIntf;
	}
	
	public void populateAttributeInformation(AbstractAttributeInterface attributeInterface, AbstractAttributeInformationInterface attributeInformationIntf)
	{
		if((attributeInformationIntf!=null)&&(attributeInterface!=null))
		{
			attributeInformationIntf.setName(attributeInterface.getName());
			attributeInformationIntf.setDescription(attributeInterface.getDescription());
			
			if(attributeInterface instanceof StringAttributeInterface)
			{
				attributeInformationIntf.setAttributeDefaultValue(((StringAttributeInterface)attributeInterface).getDefaultValue());
				//attributeInformationIntf.setAttributeSize(((StringAttributeInterface)attributeInterface).getSize());
			}
		}
	}
}

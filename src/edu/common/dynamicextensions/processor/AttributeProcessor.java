/*
 * Created on Oct 11, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.util.Date;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
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
	/*  
	 *//**
	 * 
	 * @param attributeType
	 * @return
	 *//*
	public AttributeInterface createAttribute(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		AttributeInterface attributeInterface = null;
		if(attributeType!=null)
		{
			if (attributeType.equalsIgnoreCase("String")) {
				attributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
			} else if(attributeType.equalsIgnoreCase("Date")) {
				attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
			}else if(attributeType.equalsIgnoreCase("Date")) {
				attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
			}
		}
		return attributeInterface;
	}*/

	public AttributeInterface createAndPopulateAttribute(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		System.out.println("Creating Attribute ");
		AttributeInterface attributeInterface = null;
		if(attributeInformationIntf!=null)
		{
			String attributeType = attributeInformationIntf.getDataType();
			if(attributeType!=null)
			{
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING)) {
					attributeInterface = getInterfaceForStringDataType(attributeInformationIntf);
				} else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE)) {
					attributeInterface = getInterfaceForDateDataType(attributeInformationIntf);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN)) {
					attributeInterface = getInterfaceForBooleanDataType(attributeInformationIntf);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER)){
					attributeInterface = getInterfaceForNumericDataType(attributeInformationIntf);
				}
				
				//Set common attributes
				if(attributeInterface!=null)
				{
					attributeInterface.setName(attributeInformationIntf.getName());
					System.out.println("Attribute Name = " + attributeInterface.getName());
					attributeInterface.setDescription(attributeInformationIntf.getDescription());
					System.out.println("Attribute Desc = " + attributeInterface.getDescription());
				}
			}
		}

		return attributeInterface;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForBooleanDataType(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		BooleanAttributeInterface booleanAttributeIntf = DomainObjectFactory.getInstance().createBooleanAttribute();
		Boolean defaultValue = new Boolean(attributeInformationIntf.getAttributeDefaultValue());
		booleanAttributeIntf.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + booleanAttributeIntf.getDefaultValue());
		return booleanAttributeIntf;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForDateDataType(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		DateAttributeInterface dateAttributeIntf = DomainObjectFactory.getInstance().createDateAttribute();
		Date defaultValue  = new Date(attributeInformationIntf.getAttributeDefaultValue());
		dateAttributeIntf.setDefaultValue(defaultValue);
		System.out.println("Attribute Def Value = " + dateAttributeIntf.getDefaultValue());
		dateAttributeIntf.setFormat(attributeInformationIntf.getFormat());
		System.out.println("Attribute Format = " + dateAttributeIntf.getFormat());
		return dateAttributeIntf;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForStringDataType(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		System.out.println("String Data Tupe");
		StringAttributeInterface stringAttributeIntf = DomainObjectFactory.getInstance().createStringAttribute();
		stringAttributeIntf.setDefaultValue(attributeInformationIntf.getAttributeDefaultValue());
		System.out.println("Attribute Def Value = " + stringAttributeIntf.getDefaultValue());
		Integer size = new Integer(attributeInformationIntf.getAttributeSize());
		stringAttributeIntf.setSize(size);
		System.out.println("Attribute Size = " + stringAttributeIntf.getSize());
		return stringAttributeIntf;
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
					Short defaultValue = new Short(attributeInformationIntf.getAttributeDefaultValue());
					((ShortAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
					System.out.println("Attribute Def Value = " + ((ShortAttributeInterface)numberAttribIntf).getDefaultValue());
					((ShortAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
					System.out.println("Attribute Measuremt Attrib = " + ((ShortAttributeInterface)numberAttribIntf).getMeasurementUnits());
				}else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_INT){
					numberAttribIntf = DomainObjectFactory.getInstance().createIntegerAttribute();
					Integer defaultValue = new Integer(attributeInformationIntf.getAttributeDefaultValue());
					((IntegerAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
					System.out.println("Attribute Def Value = " + ((IntegerAttributeInterface)numberAttribIntf).getDefaultValue());
					((IntegerAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
					System.out.println("Attribute Measuremt Attrib = " + ((IntegerAttributeInterface)numberAttribIntf).getMeasurementUnits());
				}else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_LONG){
					numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
					Long defaultValue = new Long(attributeInformationIntf.getAttributeDefaultValue());
					((LongAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
					((LongAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
					System.out.println("Attribute Def Value = " + ((LongAttributeInterface)numberAttribIntf).getDefaultValue());
					System.out.println("Attribute Measuremt Attrib = " + ((LongAttributeInterface)numberAttribIntf).getMeasurementUnits());
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
				Float defaultValue = new Float(attributeInformationIntf.getAttributeDefaultValue());
				((FloatAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
				((FloatAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
				
				System.out.println("Attribute Def Value = " + ((FloatAttributeInterface)numberAttribIntf).getDefaultValue());
				System.out.println("Attribute Measuremt Attrib = " + ((FloatAttributeInterface)numberAttribIntf).getMeasurementUnits());
			}else if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_DOUBLE){
				numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttribute();
				Double defaultValue = new Double(attributeInformationIntf.getAttributeDefaultValue());
				((DoubleAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
				((DoubleAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
				
				System.out.println("Attribute Def Value = " + ((DoubleAttributeInterface)numberAttribIntf).getDefaultValue());
				System.out.println("Attribute Measuremt Attrib = " + ((DoubleAttributeInterface)numberAttribIntf).getMeasurementUnits());
			}
			else{
				System.out.println("Too many decimal places");
			}
		}
		return numberAttribIntf;
	}
}

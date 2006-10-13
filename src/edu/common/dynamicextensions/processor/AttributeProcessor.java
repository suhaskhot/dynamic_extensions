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
		AttributeInterface attributeInterface = null;
		if(attributeInformationIntf!=null)
		{
			String attributeType = attributeInformationIntf.getDataType();
			if(attributeType!=null)
			{
				if (attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_STRING)) {
					//attributeInterface = DomainObjectFactory.getInstance().createStringAttribute();
					attributeInterface = getInterfaceForStringDataType(attributeInformationIntf);
				} else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_DATE)) {
					//attributeInterface = DomainObjectFactory.getInstance().createDateAttribute();
					attributeInterface = getInterfaceForDateDataType(attributeInformationIntf);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_BOOLEAN)) {
					//attributeInterface = DomainObjectFactory.getInstance().createBooleanAttribute();
					attributeInterface = getInterfaceForBooleanDataType(attributeInformationIntf);
				}else if(attributeType.equalsIgnoreCase(ProcessorConstants.DATATYPE_NUMBER)){
					attributeInterface = getInterfaceForNumericDataType(attributeInformationIntf);
				}
				if(attributeInterface!=null)
				{
					attributeInterface.setName(attributeInformationIntf.getName());
					attributeInterface.setDescription(attributeInformationIntf.getDescription());
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
		dateAttributeIntf.setFormat(attributeInformationIntf.getFormat());
		return dateAttributeIntf;
	}

	/**
	 * @param attributeInformationIntf
	 * @return
	 */
	private AttributeInterface getInterfaceForStringDataType(AbstractAttributeInformationInterface attributeInformationIntf)
	{
		StringAttributeInterface stringAttributeIntf = DomainObjectFactory.getInstance().createStringAttribute();
		stringAttributeIntf.setDefaultValue(attributeInformationIntf.getAttributeDefaultValue());
		Integer size = new Integer(attributeInformationIntf.getAttributeSize());
		stringAttributeIntf.setSize(size);
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
					((ShortAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
				}else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_INT){
					numberAttribIntf = DomainObjectFactory.getInstance().createIntegerAttribute();
					Integer defaultValue = new Integer(attributeInformationIntf.getAttributeDefaultValue());
					((IntegerAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
					((IntegerAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
				}else if(noOfDigits <= ProcessorConstants.MAX_NO_OF_DIGITS_LONG){
					numberAttribIntf = DomainObjectFactory.getInstance().createLongAttribute();
					Long defaultValue = new Long(attributeInformationIntf.getAttributeDefaultValue());
					((LongAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
					((LongAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
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
			}else if(noOfDecimalPlaces <= ProcessorConstants.MAX_NO_OF_DECIMALS_DOUBLE){
				numberAttribIntf = DomainObjectFactory.getInstance().createDoubleAttribute();
				Double defaultValue = new Double(attributeInformationIntf.getAttributeDefaultValue());
				((DoubleAttributeInterface)numberAttribIntf).setDefaultValue(defaultValue);
				((DoubleAttributeInterface)numberAttribIntf).setMeasurementUnits(attributeInformationIntf.getAttributeMeasurementUnits());
			}
			else{
				System.out.println("Too many decimal places");
			}
		}
		return numberAttribIntf;
	}
}

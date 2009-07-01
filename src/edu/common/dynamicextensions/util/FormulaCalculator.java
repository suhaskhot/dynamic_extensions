package edu.common.dynamicextensions.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domaininterface.AssociationMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.parser.FormulaParser;
import edu.common.dynamicextensions.validation.ValidatorUtil;



/**
 * 
 * @author rajesh_patil
 *
 */
public class FormulaCalculator 
{
	/**
	 * 
	 */
	private FormulaParser formulaParser = null;

	/**
	 * 
	 */
	public FormulaCalculator()
	{
		this.formulaParser = new FormulaParser();
	}
	/**
	 * 
	 */
	public FormulaCalculator(FormulaParser formulaParser)
	{
		this.formulaParser = formulaParser;
	}
	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws ParseException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public Object evaluateFormula(
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap,
			CategoryAttributeInterface categoryAttributeInterface,CategoryInterface category) throws DynamicExtensionsApplicationException, ParseException, DynamicExtensionsSystemException
	{
		Object value = null;
		Map<BaseAbstractAttributeInterface, Object> attributeValueMapForValidation = new HashMap <BaseAbstractAttributeInterface, Object>();
		boolean allCalculatedAttributesPresent = true;
		FormulaParser formulaParser = getFormulaParser();
		formulaParser.parseExpression(categoryAttributeInterface.getFormula().getExpression());
		List<String> symbols = formulaParser.getSymobols();
		for (String symbol : symbols)
		{
			String [] names = symbol.split("_");
			List <Object> values = new ArrayList <Object>();
			List <CategoryAttributeInterface> attributes = new ArrayList<CategoryAttributeInterface>();
			getCategoryAttribute(names[0],names[1],category.getRootCategoryElement(),attributes);
			if (!attributes.isEmpty())
			{
				evaluateFormulaForCalulatedAttribute(attributeValueMap,attributes.get(0),values);
			}
			if (!values.isEmpty())
			{
				attributeValueMapForValidation.put(attributes.get(0), values.get(0));
			}
			else
			{
				allCalculatedAttributesPresent = false;
			}
		}
		List<String> errorList = new ArrayList<String>();
		Set<Map.Entry<BaseAbstractAttributeInterface, Object>> attributeSet = attributeValueMapForValidation
				.entrySet();
		for (Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueNode : attributeSet) 
		{
			errorList.addAll(ValidatorUtil.validateAttributes(
					attributeValueNode, attributeValueNode.getKey().getName()));
		}
		if (allCalculatedAttributesPresent && errorList.isEmpty())
		{
			Set<Entry<BaseAbstractAttributeInterface, Object>> entries = attributeValueMapForValidation.entrySet();
			for (Map.Entry<BaseAbstractAttributeInterface, Object> entry : entries)
			{
				CategoryAttributeInterface attribute = (CategoryAttributeInterface) entry.getKey();
				AttributeMetadataInterface attributeInterface = (AttributeMetadataInterface) attribute.getAbstractAttribute();
				PermissibleValueInterface permissibleValueInterface = attributeInterface.getAttributeTypeInformation().getPermissibleValueForString(entry.getValue().toString());
				formulaParser.setVariableValue(attribute.getCategoryEntity()
						.getEntity().getName()
						+ "_" + attributeInterface.getName(),
						permissibleValueInterface.getValueAsObject());
			}

			value = formulaParser.evaluateExpression();
		}
		return value;
	}
	/**
	 * 
	 * @return
	 */
	private void getCategoryAttribute(String entityName,String attributeName,CategoryEntityInterface rootCategoryEntity,List <CategoryAttributeInterface> attributes)
	{
		if (rootCategoryEntity != null)
		{
			for (CategoryAssociationInterface categoryAssociationInterface : rootCategoryEntity
					.getCategoryAssociationCollection())
			{
				if (categoryAssociationInterface.getTargetCategoryEntity()
						.getEntity().getName().equals(entityName))
				{
					for (CategoryAttributeInterface categoryAttributeInterface : categoryAssociationInterface.getTargetCategoryEntity().getCategoryAttributeCollection())
					{
						if (categoryAttributeInterface.getAbstractAttribute().getName().equals(attributeName))
						{
							attributes.add(categoryAttributeInterface);
							return;
						}
					}
				}
				else
				{
					getCategoryAttribute(entityName,attributeName,categoryAssociationInterface.getTargetCategoryEntity(),attributes);
				}
			}
		}
	}
	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 * @throws ParseException 
	 */
	private void evaluateFormulaForCalulatedAttribute(
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap,
			CategoryAttributeInterface calculatedAttribute,List <Object> values) throws ParseException
	{
		Set<Entry<BaseAbstractAttributeInterface, Object>> entries = attributeValueMap.entrySet();
		for (Map.Entry<BaseAbstractAttributeInterface, Object> entry : entries)
		{
			BaseAbstractAttributeInterface attribute = entry.getKey();
			if (attribute instanceof AttributeMetadataInterface)
			{
				if (attribute.equals(calculatedAttribute))
				{
					String value = (String) entry.getValue();
					if (value != null && value.length() > 0)
					{
						values.add(value);
					}
					return;
				}
			}
			else if (attribute instanceof AssociationMetadataInterface)
			{
				List <Map<BaseAbstractAttributeInterface, Object>> attributeValueMapList = (List<Map<BaseAbstractAttributeInterface, Object>>) entry.getValue();
				for (Map<BaseAbstractAttributeInterface, Object> map : attributeValueMapList)
				{
					evaluateFormulaForCalulatedAttribute(map,calculatedAttribute,values);
				}
			}
		}
	}
	/**
	 * @throws DynamicExtensionsApplicationException 
	 * @throws ParseException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	public String setDefaultValueForCalculatedAttributes(CategoryAttributeInterface categoryAttribute,CategoryInterface category) throws DynamicExtensionsApplicationException, ParseException, DynamicExtensionsSystemException
	{
		StringBuffer message = new StringBuffer("");
		Object value = null;
		Map<BaseAbstractAttributeInterface, Object> attributeValueMap = new HashMap <BaseAbstractAttributeInterface, Object>();
		boolean allCalculatedAttributesPresent = true;
		FormulaParser formulaParser = getFormulaParser();
		formulaParser.parseExpression(categoryAttribute.getFormula().getExpression());
		List<String> symbols = formulaParser.getSymobols();
		for (String symbol : symbols)
		{
			String [] names = symbol.split("_");
			List <CategoryAttributeInterface> attributes = new ArrayList<CategoryAttributeInterface>();
			getCategoryAttribute(names[0],names[1],category.getRootCategoryElement(),attributes);
			if (!attributes.isEmpty())
			{
				if (attributes.get(0).getDefaultValuePermissibleValue() == null)
				{
					allCalculatedAttributesPresent = false;
				}
				else
				{
					attributeValueMap.put(attributes.get(0), attributes.get(0).getDefaultValue());
				}
			}
		}
		List<String> errorList = new ArrayList<String>();
		Set<Map.Entry<BaseAbstractAttributeInterface, Object>> attributeSet = attributeValueMap
				.entrySet();
		for (Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueNode : attributeSet) 
		{
			errorList.addAll(ValidatorUtil.validateAttributes(
					attributeValueNode, attributeValueNode.getKey().getName()));
		}

		if (allCalculatedAttributesPresent && errorList.isEmpty())
		{
			Set<Entry<BaseAbstractAttributeInterface, Object>> entries = attributeValueMap.entrySet();
			for (Map.Entry<BaseAbstractAttributeInterface, Object> entry : entries)
			{
				CategoryAttributeInterface attribute = (CategoryAttributeInterface) entry.getKey();
				AttributeMetadataInterface attributeInterface = (AttributeMetadataInterface) attribute.getAbstractAttribute();
				PermissibleValueInterface permissibleValueInterface = attributeInterface.getAttributeTypeInformation().getPermissibleValueForString(entry.getValue().toString());
				formulaParser.setVariableValue(attribute.getCategoryEntity()
						.getEntity().getName()
						+ "_" + attribute.getAbstractAttribute().getName(),
						permissibleValueInterface.getValueAsObject());
			}
			value = formulaParser.evaluateExpression();
			PermissibleValueInterface defaultValue = ((AttributeMetadataInterface) categoryAttribute)
					.getAttributeTypeInformation()
					.getPermissibleValueForString(value.toString());
			categoryAttribute.setDefaultValue(defaultValue);
		}
		else if (!errorList.isEmpty())
		{
			for (String error : errorList)
			{
				message.append(error);
			}
		}
		return message.toString();
	}
	/**
	 * 
	 * @return
	 */
	public FormulaParser getFormulaParser() 
	{
		return formulaParser;
	}
	/**
	 * 
	 * @param formulaParser
	 */
	public void setFormulaParser(FormulaParser formulaParser) 
	{
		this.formulaParser = formulaParser;
	}
}

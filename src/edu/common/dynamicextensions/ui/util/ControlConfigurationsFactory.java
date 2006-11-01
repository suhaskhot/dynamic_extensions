/*
 * Created on Oct 17, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.validation.ValidatorRuleInterface;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author preeti_munot
 * @author deepti_shelar
 *
 * 
 */
public class ControlConfigurationsFactory
{
	private static ControlConfigurationsFactory m_instance = null;
	private Map controlsConfigurationMap = null;
	private Map rulesConfigurationMap = null;

	private ControlConfigurationsFactory()
	{
		controlsConfigurationMap = new HashMap();
		rulesConfigurationMap = new HashMap();
		parseXML("RuleConfigurations.xml");
		parseXML("ControlConfigurations.xml");
		System.out.println("");
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static ControlConfigurationsFactory getInstance()
	{
		if (m_instance == null)
		{
			m_instance = new ControlConfigurationsFactory();
		}
		return m_instance;
	}

	/**
	 * Parse the XML
	 *
	 */
	private void parseXML(String configurationFileName)
	{
		Document document = null;
		try
		{
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			if (factory != null)
			{
				docBuilder = factory.newDocumentBuilder();
				if (docBuilder != null)
				{
					InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configurationFileName);
					if (inputStream != null)
					{
						document = docBuilder.parse(inputStream);

						if (document != null)
						{
							if (configurationFileName.equalsIgnoreCase("RuleConfigurations.xml"))
							{
								loadRuleConfigurations(document);
							}
							else if (configurationFileName.equalsIgnoreCase("ControlConfigurations.xml"))
							{
								loadControlConfigurations(document);
							}

						}
					}
					else
					{
						System.out.println("InputStream null...Please check");
					}
				}

			}
		}
		catch (FactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param document
	 */
	private void loadRuleConfigurations(Document document)
	{
		if (document != null)
		{
			try
			{
				NodeList validationRulesList = document.getElementsByTagName(Constants.VALIDATION_RULE);
				if (validationRulesList != null)
				{
					int noOfRules = validationRulesList.getLength();
					Node validationRuleNode = null, ruleDisplayLabelNode = null, ruleNameNode = null, ruleClassNode = null;
					Node errorKey = null, errorKeyNameNode = null;
					NamedNodeMap ruleAttributes = null, errorKeysList = null;
					NodeList ruleParameters = null, childNodes = null;
					String ruleName = null, errorKeyValue = null;
					Map ruleParametersMap = new HashMap();
					for (int i = 0; i < noOfRules; i++)
					{
						validationRuleNode = validationRulesList.item(i);
						if (validationRuleNode != null)
						{
							RuleConfigurationObject ruleConfigurationObject = new RuleConfigurationObject();
							ruleAttributes = validationRuleNode.getAttributes();
							if (ruleAttributes != null)
							{
								ruleDisplayLabelNode = ruleAttributes.getNamedItem(Constants.DISPLAY_LABEL);
								ruleNameNode = ruleAttributes.getNamedItem(Constants.RULE_NAME);
								ruleClassNode = ruleAttributes.getNamedItem(Constants.RULE_CLASS);
								if (ruleDisplayLabelNode != null && ruleNameNode != null && ruleClassNode != null)
								{
									ruleName = ruleNameNode.getNodeValue();
									ruleConfigurationObject.setDisplayLabel(ruleDisplayLabelNode.getNodeValue());
									ruleConfigurationObject.setRuleName(ruleName);
									ruleConfigurationObject.setRuleClassName(ruleClassNode.getNodeValue());
								}
							}
							ruleParameters = ((Element) validationRuleNode).getElementsByTagName(Constants.PARAM);
							if (ruleParameters.getLength() != 0)
							{
								ruleParametersMap.put(ruleName, loadRuleParameters(ruleParameters));
								ruleConfigurationObject.setRuleParametersMap(ruleParametersMap);
							}
							childNodes = ((Element) validationRuleNode).getElementsByTagName(Constants.ERROR_KEY);
							errorKey = childNodes.item(0);
							errorKeysList = errorKey.getAttributes();
							if (errorKeysList != null)
							{
								errorKeyNameNode = errorKeysList.getNamedItem(Constants.NAME);
								errorKeyValue = errorKeyNameNode.getNodeValue();
								ruleConfigurationObject.setErrorKey(errorKeyValue);
							}
							rulesConfigurationMap.put(ruleName, ruleConfigurationObject);

						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getStackTrace());

			}
		}
	}

	/**
	 * 
	 */
	private List loadRuleParameters(NodeList ruleParameters)
	{
		ArrayList ruleParametersAttributes = new ArrayList();
		if (ruleParameters != null)
		{
			NameValueBean nameValueBeanAttribute = null;
			NamedNodeMap paramAttributes = null;
			Node paramLabelNode = null, paramNameNode = null, paramNode = null;
			String paramLabel = null, paramName = null;
			int noOfParameters = ruleParameters.getLength();
			for (int i = 0; i < noOfParameters; i++)
			{
				paramNode = ruleParameters.item(i);
				paramAttributes = paramNode.getAttributes();
				if (paramAttributes != null)
				{
					paramLabelNode = paramAttributes.getNamedItem(Constants.PARAM_LABEL);
					paramNameNode = paramAttributes.getNamedItem(Constants.PARAM_NAME);
					if (paramLabelNode != null && paramNameNode != null)
					{
						paramLabel = paramLabelNode.getNodeValue();
						paramName = paramNameNode.getNodeValue();
					}
				}
				if (paramName != null && paramLabel != null)
				{
					nameValueBeanAttribute = new NameValueBean(paramName, paramLabel);
					ruleParametersAttributes.add(nameValueBeanAttribute);
				}
			}
		}
		return (ArrayList) ruleParametersAttributes;
	}

	/**
	 * 
	 * @param document
	 */
	private void loadControlConfigurations(Document document)
	{
		if (document != null)
		{
			NodeList controlsList = document.getElementsByTagName(Constants.CONTROL_TAGNAME);
			if (controlsList != null)
			{
				ControlsConfigurationObject controlsConfigurationObject = null;
				List listOfControls = new ArrayList();
				Node controlNode = null, controlNameNode = null, displayLabelNode = null, jspNameNode = null, dataTypeNode = null;
				NodeList controlDataTypesNodesList = null, controlCommonValidationRules = null;
				NamedNodeMap controlAttributes = null;
				String controlName = null, displayLabel = null;
				NameValueBean nameValueBean = null;
				Map dataTypeRulesMap = null, dataTypeImplicitRulesMap = null, dataTypeExplicitRulesMap = null;
				int noOfControls = controlsList.getLength();
				for (int i = 0; i < noOfControls; i++)
				{
					controlNode = controlsList.item(i);
					if (controlNode != null)
					{
						List commonValidationRulesList = new ArrayList(), dataTypeValidationRulesList = new ArrayList(), dataTypeRuleObjectsList = new ArrayList();
						List implicitRulesList = null, implicitExplicitRulesList = null;
						NodeList dataTypeRulesList = null;
						controlsConfigurationObject = new ControlsConfigurationObject();
						controlAttributes = controlNode.getAttributes();
						if (controlAttributes != null)
						{
							controlNameNode = controlAttributes.getNamedItem(Constants.NAME);
							displayLabelNode = controlAttributes.getNamedItem(Constants.DISPLAY_LABEL);
							jspNameNode = controlAttributes.getNamedItem(Constants.JSP_NAME);
							if (controlNameNode != null && displayLabelNode != null && jspNameNode != null)
							{
								controlName = controlNameNode.getNodeValue();
								displayLabel = displayLabelNode.getNodeValue();
								nameValueBean = new NameValueBean(controlName, displayLabel);
								listOfControls.add(nameValueBean);
								controlsConfigurationObject.setControlName(controlName);
								controlsConfigurationObject.setDisplayLabel(displayLabel);
								controlsConfigurationObject.setJspName(jspNameNode.getNodeValue());
							}
						}
						List commonImplicitRules = new ArrayList();
						List commonExplicitRules = new ArrayList();
						controlCommonValidationRules = ((Element) controlNode).getElementsByTagName(Constants.COMMON_VALIDATION_RULE);
						List commonValidationsList = getChildNodesList(controlCommonValidationRules, Constants.NAME);
						implicitRulesList = getChildNodesList(controlCommonValidationRules, Constants.IS_IMPLICIT);
						for (int j = 0; j < implicitRulesList.size(); j++)
						{
							if (implicitRulesList.get(j).toString().equalsIgnoreCase("false"))
							{
								commonExplicitRules.add(commonValidationsList.get(j));
							}
							else if (implicitRulesList.get(j).toString().equalsIgnoreCase("true"))
							{
								commonImplicitRules.add(commonValidationsList.get(j));
							}
						}

						controlsConfigurationObject.setCommonValidationRules(getRuleObjectsList(commonValidationsList));
						controlsConfigurationObject.setCommonExplicitRules(commonExplicitRules);
						controlsConfigurationObject.setCommonImplicitRules(commonImplicitRules);
						controlDataTypesNodesList = ((Element) controlNode).getElementsByTagName(Constants.DATA_TYPE_TAGNAME);
						List dataTypesList = getChildNodesList(controlDataTypesNodesList, Constants.NAME);
						dataTypeRulesMap = new HashMap();
						dataTypeImplicitRulesMap = new HashMap();
						dataTypeExplicitRulesMap = new HashMap();
						for (int j = 0; j < dataTypesList.size(); j++)
						{
							dataTypeNode = controlDataTypesNodesList.item(j);
							dataTypeRulesList = ((Element) dataTypeNode).getElementsByTagName(Constants.VALIDATION_RULE);
							dataTypeValidationRulesList = getChildNodesList(dataTypeRulesList, Constants.NAME);
							implicitRulesList = getChildNodesList(dataTypeRulesList, Constants.IS_IMPLICIT);
							implicitExplicitRulesList = getImplicitExplicitRules(dataTypeValidationRulesList, implicitRulesList);
							dataTypeImplicitRulesMap.put(dataTypesList.get(j), implicitExplicitRulesList.get(0));
							dataTypeExplicitRulesMap.put(dataTypesList.get(j), implicitExplicitRulesList.get(1));
							dataTypeRuleObjectsList = getRuleObjectsList(dataTypeValidationRulesList);
							dataTypeRulesMap.put(dataTypesList.get(j), dataTypeRuleObjectsList);
						}
						controlsConfigurationObject.setDataTypeValidationRules(dataTypeRulesMap);
						controlsConfigurationObject.setDataTypesList(getNameValueBeansList(dataTypesList));
						controlsConfigurationObject.setDataTypeExplicitRules(dataTypeExplicitRulesMap);
						controlsConfigurationObject.setDataTypeImplicitRules(dataTypeImplicitRulesMap);
					}
					controlsConfigurationMap.put(controlName, controlsConfigurationObject);
					controlsConfigurationMap.put("ListOfControls", listOfControls);

				}
			}
		}
	}

	/**
	 * 
	 * @param rulesList
	 * @param implicitRulesList
	 * @return
	 */
	private List getImplicitExplicitRules(List rulesList, List implicitRulesList)
	{
		List listOfImplicitRules = new ArrayList();
		List listOfExplicitRules = new ArrayList();
		List listOfImplicitExplicitRules = new ArrayList();
		for (int i = 0; i < implicitRulesList.size(); i++)
		{
			if (implicitRulesList.get(i).toString().equalsIgnoreCase("false"))
			{
				listOfExplicitRules.add(rulesList.get(i));
			}
			else if (implicitRulesList.get(i).toString().equalsIgnoreCase("true"))
			{
				listOfImplicitRules.add(rulesList.get(i));
			}
		}
		listOfImplicitExplicitRules.add(listOfImplicitRules);
		listOfImplicitExplicitRules.add(listOfExplicitRules);

		return listOfImplicitExplicitRules;

	}

	/**
	 * 
	 * @param ruleValidationsList
	 * @return
	 */
	private List getRuleObjectsList(List ruleValidationsList)
	{
		List ruleObjectsList = new ArrayList();
		Iterator rulesIterator = ruleValidationsList.iterator();
		RuleConfigurationObject ruleObject = null;
		while (rulesIterator.hasNext())
		{
			String ruleName = (String) rulesIterator.next();
			ruleObject = (RuleConfigurationObject) rulesConfigurationMap.get(ruleName);
			ruleObjectsList.add(ruleObject);
		}
		return ruleObjectsList;
	}

	/**
	 * 
	 * @param dataTypeName
	 * @return
	 */
	private List getDataTypeValidationRules(String dataTypeName, Node controlNode)
	{
		List dataTypeValidations = new ArrayList();
		NodeList rules;
		Node ruleNode = null, ruleAttributeNode = null;
		String attrName = "", attrValue = "", ruleNodeName = "";
		NamedNodeMap ruleAttributes = null;
		if (controlNode != null && dataTypeName != null)
		{
			rules = ((Element) controlNode).getElementsByTagName(Constants.VALIDATION_RULE);
			for (int i = 0; i < rules.getLength(); i++)
			{
				ruleNode = rules.item(i);
				ruleAttributes = ruleNode.getAttributes();
				if (ruleAttributes != null)
				{
					for (int j = 0; j < ruleAttributes.getLength(); j++)
					{
						ruleAttributeNode = ruleAttributes.item(j);
						attrName = ruleAttributeNode.getNodeName();
						attrValue = ruleAttributeNode.getNodeValue();
						if (attrName != null && attrValue != null && attrName.equalsIgnoreCase(Constants.NAME))
						{
							dataTypeValidations.add(attrValue);
						}

					}
				}
			}
		}
		return dataTypeValidations;
	}

	/**
	 * 
	 * @param controlDataTypesNodesList
	 * @return
	 */
	private ArrayList getChildNodesList(NodeList mappedNodesList, String key)
	{
		ArrayList childNodesList = new ArrayList();
		if (mappedNodesList != null)
		{
			NamedNodeMap childNodes = null;
			Node childNameNode = null, childNode = null;
			String childNodeName = null;
			int noOfChildNodes = mappedNodesList.getLength();
			for (int i = 0; i < noOfChildNodes; i++)
			{
				childNode = mappedNodesList.item(i);
				childNodes = childNode.getAttributes();
				if (childNodes != null)
				{
					childNameNode = childNodes.getNamedItem(key);
					if (childNameNode != null)
					{
						childNodeName = childNameNode.getNodeValue();
						childNodesList.add(childNodeName);
					}
				}
			}
		}
		return childNodesList;
	}

	/**
	 * 
	 * @param dataTypesList
	 * @return
	 */
	private List getNameValueBeansList(List dataTypesList)
	{
		List nameValueBeansList = new ArrayList();
		Iterator iter = dataTypesList.iterator();
		NameValueBean nameValueBeanAttribute;
		String nodeName = "";
		while (iter.hasNext())
		{
			nodeName = (String) iter.next();
			nameValueBeanAttribute = new NameValueBean(nodeName, nodeName);
			nameValueBeansList.add(nameValueBeanAttribute);
		}

		return nameValueBeansList;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public List getControlsDataTypes(String controlName)
	{
		ArrayList dataTypesList = null;
		if ((controlName != null) && (controlsConfigurationMap != null))
		{
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);
			if (controlsConfigurationObject != null)
			{
				dataTypesList = (ArrayList) controlsConfigurationObject.getDataTypesList();
			}
		}
		return dataTypesList;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public String getControlJspName(String controlName)
	{
		String jspName = null;
		if ((controlName != null) && (controlsConfigurationMap != null))
		{
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);
			if (controlsConfigurationObject != null)
			{
				jspName = controlsConfigurationObject.getJspName();
			}
		}
		return jspName;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public String getControlDisplayLabel(String controlName)
	{
		String displayLabel = null;
		if ((controlName != null) && (controlsConfigurationMap != null))
		{
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);
			if (controlsConfigurationObject != null)
			{
				displayLabel = controlsConfigurationObject.getDisplayLabel();
			}
		}
		return displayLabel;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public ArrayList getImplicitRules(String controlName, String dataType)
	{
		ArrayList implicitRules = new ArrayList();
		if ((controlName != null) && (controlsConfigurationMap != null))
		{
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);
			if (controlsConfigurationObject != null)
			{
				implicitRules = (ArrayList) getListOfRules(controlsConfigurationObject.getCommonImplicitRules());
				Map map = controlsConfigurationObject.getDataTypeImplicitRules();
				List rulesList = (ArrayList) map.get(dataType);
				Iterator iter = rulesList.iterator();
				while (iter.hasNext())
				{
					String ruleName = iter.next().toString();
					if (!ruleName.equalsIgnoreCase(""))
					{
						implicitRules.add(ruleName);
					}
				}
			}
		}
		return implicitRules;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public ArrayList getExplicitRules(String controlName, String dataType)
	{
		ArrayList explicitRules = new ArrayList();
		if ((controlName != null) && (controlsConfigurationMap != null))
		{
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);

			if (controlsConfigurationObject != null)
			{
				explicitRules = (ArrayList) getListOfRules(controlsConfigurationObject.getCommonExplicitRules());
				Map map = controlsConfigurationObject.getDataTypeExplicitRules();
				List rulesList = (ArrayList) map.get(dataType);
				if(rulesList != null) {
					Iterator iter = rulesList.iterator();
					while (iter.hasNext())
					{
						String ruleName = iter.next().toString();
						if (!ruleName.equalsIgnoreCase(""))
						{
							explicitRules.add(ruleName);
						}
					}
				}
			}
		}
		return explicitRules;
	}

	/**
	 * 
	 * @param rules
	 * @return
	 */
	private List getListOfRules(List rules)
	{
		ArrayList listOfRules = new ArrayList();

		Iterator iter = rules.iterator();
		while (iter.hasNext())
		{
			listOfRules.add(iter.next().toString());
		}
		return listOfRules;
	}

	/**
	 * 
	 */
	public RuleConfigurationObject getRuleObject(String ruleName)
	{
		RuleConfigurationObject ruleConfigurationObject = null;
		if(ruleName != null && rulesConfigurationMap != null)
		{
			ruleConfigurationObject = (RuleConfigurationObject) rulesConfigurationMap.get(ruleName);	
		}

		return ruleConfigurationObject;
	}

	/**
	 * 
	 *
	 */
	public List getListOfControls()
	{
		ArrayList listOfControls = null;
		if (controlsConfigurationMap != null)
		{
			listOfControls = (ArrayList) controlsConfigurationMap.get("ListOfControls");
		}
		return listOfControls;
	}
	public List getRuleDisplayLabels(List ruleNamesList)
	{
		List listOfDisplayLabels = new ArrayList();
		Iterator iter = (Iterator)ruleNamesList.iterator();
		RuleConfigurationObject ruleConfigurationObject = null;
		while(iter.hasNext())
		{
			String ruleName = iter.next().toString();
			ruleConfigurationObject = (RuleConfigurationObject) rulesConfigurationMap.get(ruleName);	
			listOfDisplayLabels.add(ruleConfigurationObject.getDisplayLabel());
		}
		return listOfDisplayLabels;
	}

	/**
	 * This method returns rule instance for given rule name
	 * @param ruleName
	 * @return
	 */
	public ValidatorRuleInterface getValidatorRule(String ruleName) {
		RuleConfigurationObject ruleConfiguration =  (RuleConfigurationObject) rulesConfigurationMap.get(ruleName);
		ValidatorRuleInterface ruleInterface;

		Class ruleClass;

		try
		{
			ruleClass = Class.forName(ruleConfiguration.getRuleClassName());
			ruleInterface = (ValidatorRuleInterface) ruleClass.newInstance();
		}
		catch (Exception e)
		{
			return null;	
		}
		return ruleInterface;
	}

	/**
	 * @param args
	 */
	/*
	 public static void main(String[] args)
	 {
	 ControlConfigurationsFactory cmf = ControlConfigurationsFactory.getInstance();
	 cmf.getImplicitRules("TextControl", "Text");
	 cmf.getExplicitRules("TextControl", "Text");
	 //cmf.getListOfControls();
	 }*/
}
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
				ControlsConfigurationObject controlsConfigurationObject;
				List listOfControls = new ArrayList();
				Node controlNode = null, controlNameNode = null, displayLabelNode = null, jspNameNode = null;
				NodeList controlDataTypesNodesList = null, controlCommonValidationRules = null;
				NamedNodeMap controlAttributes = null;
				String controlName = null, displayLabel = null;
				NameValueBean nameValueBean = null;
				int noOfControls = controlsList.getLength();

				for (int i = 0; i < noOfControls; i++)
				{
					controlNode = controlsList.item(i);
					if (controlNode != null)
					{
						List commonValidationRulesList = new ArrayList();
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
								nameValueBean = new NameValueBean(controlName,displayLabel);
								listOfControls.add(nameValueBean);
								controlsConfigurationObject.setControlName(controlName);
								controlsConfigurationObject.setDisplayLabel(displayLabel);
								controlsConfigurationObject.setJspName(jspNameNode.getNodeValue());
							}
						}
						controlDataTypesNodesList = ((Element) controlNode).getElementsByTagName(Constants.DATA_TYPE_TAGNAME);
						controlDataTypesNodesList.getLength();
						/*for(int j=0;j<controlDataTypesNodesList.getLength();j++) {
							controlDataTypesNodesList.item(j);
						}*/
						List dataTypesList = getChildNodesList(controlDataTypesNodesList);
						/*Iterator dataTypesIterator = dataTypesList.iterator();
						List dataTypeValidations = new ArrayList();
						while (dataTypesIterator.hasNext())
						{
							String dataType = (String) dataTypesIterator.next();
							dataTypeValidations = getDataTypeValidationRules(dataType,controlNode);
						}*/

						controlsConfigurationObject.setDataTypesList(getNameValueBeansList(dataTypesList));
						controlCommonValidationRules = ((Element) controlNode).getElementsByTagName(Constants.COMMON_VALIDATION_RULE);
						List commonValidationsList = getChildNodesList(controlCommonValidationRules);
						Iterator rulesIterator = commonValidationsList.iterator();
						RuleConfigurationObject ruleObject = null;
						while (rulesIterator.hasNext())
						{
							String ruleName = (String) rulesIterator.next();
							ruleObject = (RuleConfigurationObject) rulesConfigurationMap.get(ruleName);
							commonValidationRulesList.add(ruleObject);
						}
						controlsConfigurationObject.setCommonValidationRules(commonValidationRulesList);
						controlsConfigurationMap.put(controlName, controlsConfigurationObject);
						controlsConfigurationMap.put("ListOfControls", listOfControls);
					}
				}
			}
		}
		System.out.println("");
	}

	/**
	 * 
	 * @param dataTypeName
	 * @return
	 */
	private List getDataTypeValidationRules(String dataTypeName,Node controlNode)
	{
		List dataTypeValidations = new ArrayList();
		NodeList rules;
		Node ruleNode = null ,ruleAttributeNode = null;
		String attrName  ="" , attrValue="",ruleNodeName = "";
		NamedNodeMap ruleAttributes = null; 
		if(controlNode != null && dataTypeName != null)
		{
			rules = ((Element)controlNode).getElementsByTagName(Constants.VALIDATION_RULE);
			for(int i=0;i<rules.getLength();i++)
			{
				ruleNode = rules.item(i);
				ruleAttributes = ruleNode.getAttributes();
				if(ruleAttributes != null) {
					for(int j=0;j<ruleAttributes.getLength();j++)
					{
						ruleAttributeNode = ruleAttributes.item(j);
						attrName = ruleAttributeNode.getNodeName();
						attrValue = ruleAttributeNode.getNodeValue();
						if(attrName != null && attrValue != null && attrName.equalsIgnoreCase(Constants.NAME)) {
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
	private ArrayList getChildNodesList(NodeList mappedNodesList)
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
					childNameNode = childNodes.getNamedItem(Constants.NAME);
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
	public ArrayList getControlsDataTypes(String controlName)
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
	public String getControlDisplatLabel(String controlName)
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
	public ArrayList getImplicitRules(String controlName)
	{
		ArrayList implicitRules = null;

		return implicitRules;
	}

	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public ArrayList getExplicitRules(String controlName)
	{
		ArrayList explicitRules = null;

		return explicitRules;
	}

	/**
	 * @param args
	 *//*
	public static void main(String[] args)
	{
		ControlConfigurationsFactory cmf = ControlConfigurationsFactory.getInstance();
		cmf.getListOfControls();
	}*/
	/**
	 * 
	 *
	 */
	public List getListOfControls()
	{
		ArrayList listOfControls = null;
		if (controlsConfigurationMap != null)
		{
			listOfControls = (ArrayList)controlsConfigurationMap.get("ListOfControls");
		}
		return listOfControls;
	}


}

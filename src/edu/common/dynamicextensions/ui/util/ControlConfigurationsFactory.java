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
 *
 * Class which parses the Controls - Attributes mappings 
 */
public class ControlConfigurationsFactory
{
	private static ControlConfigurationsFactory m_instance = null;
	private Map controlsConfigurationMap = null;

	private ControlConfigurationsFactory()
	{
		controlsConfigurationMap = new HashMap();
		parseXML();
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
	private void parseXML()
	{
		String configurationFileName = "ControlConfigurations.xml";
		Document document = null;
		//Load the XML Document
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
							loadControlConfigurations(document);
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
	private void loadControlConfigurations(Document document)
	{
		if (document != null)
		{
			NodeList controlsList = document.getElementsByTagName(Constants.CONTROL_TAGNAME);
			if (controlsList != null)
			{
				ControlsConfigurationObject controlsConfigurationObject;
				Node controlNode = null, controlNameNode = null, displayLabelNode = null, jspNameNode = null;
				NodeList controlDataTypesNodesList = null ,controlCommonValidationRules = null;
				NamedNodeMap controlAttributes = null;
				String controlName = null;
				int noOfControls = controlsList.getLength();
				for (int i = 0; i < noOfControls; i++)
				{
					controlNode = controlsList.item(i);
					if (controlNode != null)
					{
						controlsConfigurationObject = new ControlsConfigurationObject();
						controlAttributes = controlNode.getAttributes();
						if (controlAttributes != null)
						{
							controlNameNode = controlAttributes.getNamedItem(Constants.NAME_ATTRIBUTE);
							displayLabelNode = controlAttributes.getNamedItem(Constants.DISPLAY_LABEL);
							jspNameNode = controlAttributes.getNamedItem(Constants.JSP_NAME);
							if (controlNameNode != null && displayLabelNode != null && jspNameNode != null)
							{
								controlName = controlNameNode.getNodeValue();
								controlsConfigurationObject.setControlName(controlName);
								controlsConfigurationObject.setDisplayLabel(displayLabelNode.getNodeValue());
								controlsConfigurationObject.setJspName(jspNameNode.getNodeValue());
							}
						}
						controlDataTypesNodesList = ((Element) controlNode).getElementsByTagName(Constants.DATA_TYPE_TAGNAME);
						controlsConfigurationObject.setDataTypesList(getControlDataTypes(controlDataTypesNodesList));
						//controlsConfigurationObject.setValidationRules()
						controlsConfigurationMap.put(controlName, controlsConfigurationObject);
						/*controlCommonValidationRules = ((Element) controlNode).getElementsByTagName(Constants.VALIDATION_RULE);
						controlsConfigurationMap.put(arg0, arg1)getControlDataTypes(controlCommonValidationRules);
*/
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param controlDataTypesNodesList
	 * @return
	 */
	private ArrayList getControlDataTypes(NodeList controlDataTypesNodesList)
	{
		ArrayList dataTypesNames = new ArrayList();
		if(controlDataTypesNodesList != null) 
		{
			NameValueBean nameValueBeanAttribute = null;
			NamedNodeMap controlDataTypes = null;
			Node dataTypeNameNode = null , dataTypeNode = null;
			String dataTypeName = null ;
			int noOfDataTypes = controlDataTypesNodesList.getLength();
			for(int i=0;i<noOfDataTypes;i++) {
				dataTypeNode = controlDataTypesNodesList.item(i);
				controlDataTypes = dataTypeNode.getAttributes();
				if (controlDataTypes != null)
				{
					dataTypeNameNode = controlDataTypes.getNamedItem(Constants.NAME_ATTRIBUTE);
					if (dataTypeNameNode != null)
					{
						dataTypeName = dataTypeNameNode.getNodeValue();
					}
				}
			//	dataTypeName = dataTypeNameNode.getNodeValue();
				if (dataTypeName != null)
				{
					nameValueBeanAttribute = new NameValueBean(dataTypeName, dataTypeName);
					dataTypesNames.add(nameValueBeanAttribute);
				}
			}
		}
		return dataTypesNames;
	}
	/**
	 * 
	 * @param controlCommonValidationRules
	 * @return
	 */
	private ArrayList getControlValidationRules(NodeList controlCommonValidationRules)
	{
		ArrayList validationRuleNames = new ArrayList();
		if(controlCommonValidationRules != null) 
		{
			NameValueBean nameValueBeanAttribute = null;
			Node validationRuleNode = null;
			String validationRuleName = null ;
			int noOfValidationRules = controlCommonValidationRules.getLength();
			for(int i=0 ; i<noOfValidationRules ; i++) {
				validationRuleNode = controlCommonValidationRules.item(i);
				validationRuleName = validationRuleNode.getNodeValue();
				if (validationRuleName != null)
				{
					nameValueBeanAttribute = new NameValueBean(validationRuleName, validationRuleName);
					validationRuleNames.add(nameValueBeanAttribute);
				}
			}
		}
		return validationRuleNames;
	}
	/**
	 * 
	 * @param controlName
	 * @return
	 */
	public ArrayList getControlsDataTypes(String controlName) {
		ArrayList dataTypesList = null;
		if ((controlName != null) && (controlsConfigurationMap != null)) {
			ControlsConfigurationObject controlsConfigurationObject = (ControlsConfigurationObject) controlsConfigurationMap.get(controlName);
			if(controlsConfigurationObject != null) {
				dataTypesList = (ArrayList)controlsConfigurationObject.getDataTypesList();
			}
		}
		return dataTypesList;
	}





	/**
	 * @param document
	 *//*
	private void loadControlAttributeMappings(Document document)
	{
		if (document != null)
		{
			NodeList controlsList = document.getElementsByTagName(Constants.CONTROL_TAGNAME);
			if (controlsList != null)
			{
				NodeList dataTypesNodeList = null;
				Node controlNode = null, controlNameNode = null;
				NamedNodeMap controlAttributes = null;
				String controlName = null;
				ArrayList mappedDataTypes = null;

				int noOfControls = controlsList.getLength();
				for (int i = 0; i < noOfControls; i++)
				{
					mappedDataTypes = new ArrayList();
					controlNode = controlsList.item(i);
					if (controlNode != null)
					{
						//Get control attributes
						controlAttributes = controlNode.getAttributes();
						if (controlAttributes != null)
						{
							controlNameNode = controlAttributes.getNamedItem(Constants.NAME_ATTRIBUTE);
							if (controlNameNode != null)
							{
								controlName = controlNameNode.getNodeValue();
								dataTypesNodeList = ((Element) controlNode).getElementsByTagName(Constants.DATA_TYPE_TAGNAME);
								if (controlName != null)
								{
									//Get control attribute mappings
									mappedDataTypes = getMappedDataTypes(dataTypesNodeList);
									if (mappedDataTypes != null)
									{
										System.out.println(mappedDataTypes);
										m_controlAttributeMap.put(controlName, mappedDataTypes);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	  *//**
	  * @param attributeNamesNodeList
	  * @return
	  *//*
	private ArrayList getMappedDataTypes(NodeList dataTypesNodeList)
	{
		ArrayList mappedAttributeNames = new ArrayList();
		Node attributeNameNode = null, attributeNameValueNode = null;
		String attributeName = null;
		NameValueBean nameValueBeanAttribute = null;
		int noOfAttributes = 0;
		if (dataTypesNodeList != null)
		{
			noOfAttributes = dataTypesNodeList.getLength();
			for (int j = 0; j < noOfAttributes; j++)
			{
				attributeNameNode = dataTypesNodeList.item(j);
				if ((attributeNameNode != null) && (attributeNameNode.getNodeType() == Node.ELEMENT_NODE))
				{
					attributeNameValueNode = attributeNameNode.getFirstChild();
					if (attributeNameValueNode != null)
					{
						attributeName = attributeNameValueNode.getNodeValue();
						if (attributeName != null)
						{
							nameValueBeanAttribute = new NameValueBean(attributeName, attributeName);
							mappedAttributeNames.add(nameValueBeanAttribute);
						}
					}
				}
			}
		}
		return mappedAttributeNames;
	}*/

	/*public List getAttributesForControl(String controlName)
	{
		if ((controlName != null) && (m_controlAttributeMap != null))
		{
			return (List) m_controlAttributeMap.get(controlName);
		}
		return null;
	}
	 */
	/**
	 * @param args
	 *//*
	public static void main(String[] args) {
	 ControlConfigurationsFactory cmf = ControlConfigurationsFactory.getInstance();
	 cmf.parseXML();
	 SimpleDateFormat sdf = new SimpleDateFormat(ProcessorConstants.DATE_FORMAT);
	 Date value = null;
	 try
	 {
	 value = sdf.parse("10/03/2006");
	 System.out.println("Here " + value);

	 }
	 catch (ParseException e)
	 {
	 value = new Date();
	 } 
	 }*/

}

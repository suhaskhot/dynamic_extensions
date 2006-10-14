/*
 * Created on Sep 27, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domain.userinterface.Control;
import edu.wustl.common.beans.NameValueBean;



/**
 * 
 * @author preeti_munot
 *
 * Singleton class that will load the UI Controls configuration information 
 * from EAVUIControlsConfigurations.xml.
 * This also maintains the list of attributes associated with each control.
 * Methods of this class will be called to fetch the list of controls and thier
 * associated attributes.
 */
public final class UIControlsConfigurationFactory {

	private static UIControlsConfigurationFactory m_instance = null;
	private LinkedHashMap m_UIControlsMap = null;
	private ResourceBundle rb_displayTypeClassNameMappings = null; 
	private ResourceBundle rb_uicontrolsCaptions = null;

	private UIControlsConfigurationFactory() {
		m_UIControlsMap = new LinkedHashMap();
		rb_displayTypeClassNameMappings = ResourceBundle.getBundle(UIConfigurationConstants.DISPLAYTYPE_CLASSNAME_MAPPING_FILE);
		parseXML();
	}

	public static UIControlsConfigurationFactory getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new UIControlsConfigurationFactory();
		}
		return m_instance;
	}

	/**
	 * Parse the XML
	 *
	 */
	private void parseXML()
	{
		String configurationFileName = "EAVUIControlsConfigurations.xml";
		Document document = null;
		//Load the XML Document
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			if(factory!=null)
			{
				docBuilder = factory.newDocumentBuilder();
				if(docBuilder!=null)
				{
					InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configurationFileName);
					if(inputStream!=null)
					{
						document = docBuilder.parse(inputStream);

						if(document!=null)
						{
							//load properties filename details
							loadPropertiesFile(document.getElementsByTagName(UIConfigurationConstants.PROPERTY_FILENAME_TAGNAME));
							//Load the details for each of the controls
							loadUIControlsDetails(document.getElementsByTagName(UIConfigurationConstants.UICONTROL_TAGNAME));
						}
					}
					else
					{
						System.out.println("InputStream null...Please check");
					}
				}

			}
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadPropertiesFile(NodeList propertiesFileNameNodeList) {
		if((propertiesFileNameNodeList!=null)&&((propertiesFileNameNodeList.getLength()>0)))
		{
			//Consider only the first node of the properties filename node
			Node propertiesFileNameNode = propertiesFileNameNodeList.item(0);
			if(propertiesFileNameNode!=null)
			{
				Node propertyFileName = propertiesFileNameNode.getFirstChild();
				if(propertyFileName!=null)
				{
					String strPropertiesFileName = propertyFileName.getNodeValue();
					System.out.println("Properties FileName = " + strPropertiesFileName);
					if(strPropertiesFileName!=null)
					{
						try {
							rb_uicontrolsCaptions = ResourceBundle
							.getBundle(strPropertiesFileName);
						} catch (Exception e) {
							e.printStackTrace();
						}						
						if(rb_uicontrolsCaptions==null)
						{
							System.out.println("Error Loading properties file..");
						}
					}
				}
			}
		}
	}


	private void loadUIControlsDetails(NodeList uiControlsList) {
		if(uiControlsList!=null)
		{
			String uiControlName = null;
			int noOfControls = uiControlsList.getLength();
			for(int i=0; i<noOfControls;i++)
			{
				Node uiControlNode = uiControlsList.item(i);
				if(uiControlNode!=null)
				{
					if(uiControlNode.getNodeType() == Node.ELEMENT_NODE)
					{
						NamedNodeMap uiControlNodeProperties = uiControlNode.getAttributes();
						if(uiControlNodeProperties!=null)
						{
							Node uiControlNameNode = uiControlNodeProperties.getNamedItem(UIConfigurationConstants.NAME_ATTRIBUTE);
							if(uiControlNameNode!=null)
							{
								uiControlName = uiControlNameNode.getNodeValue();
								ArrayList uiControlAttributesList = loadUIControlAttributes(uiControlNode.getChildNodes());
								m_UIControlsMap.put(uiControlName,uiControlAttributesList);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * @param uiControlAttributes : Attributes for a UI Control node
	 * @return List of Data type objects for each of the attributes
	 */
	private ArrayList loadUIControlAttributes(NodeList uiControlAttributes) {
		ArrayList uiControlAttributesList = new ArrayList();
		if(uiControlAttributes!=null)
		{
			int noOfAttributes = uiControlAttributes.getLength();
			for(int i=0;i<noOfAttributes;i++)
			{
				Node attributeNode = uiControlAttributes.item(i);

				if(attributeNode!=null)
				{
					if(attributeNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Control attributeDataTypeObject = getAttributeDataTypeObject(attributeNode);
						if(attributeDataTypeObject!=null)
						{
							uiControlAttributesList.add(attributeDataTypeObject);
						}
					}
				}
			}
		}
		return uiControlAttributesList;
	}

	private Control getAttributeDataTypeObject(Node attributeNode) {
		Control controlDataType = null;
		NamedNodeMap attributeNodeProperties = null;
		if(attributeNode!=null)
		{
			attributeNodeProperties = attributeNode.getAttributes();

			//If JSP Include code
			if(attributeNode.getNodeName().equalsIgnoreCase(UIConfigurationConstants.JSP_INCLUDE_NODE_TAGNAME))
			{
				if(attributeNodeProperties!=null)
				{
					Node jspIncludeNameNode = attributeNodeProperties.getNamedItem(UIConfigurationConstants.NAME_ATTRIBUTE);
					if(jspIncludeNameNode!=null)
					{
						String jspIncludeName = jspIncludeNameNode.getNodeValue();
						if(jspIncludeName!=null)
						{
							JSPInclude jspIncludeControl = new JSPInclude();
							jspIncludeControl.setJspName(jspIncludeName);
							//Get List of params
							Element jspNode = (Element)attributeNode;
							NodeList paramsList = jspNode.getElementsByTagName(UIConfigurationConstants.PARAM_TAGNAME);
							if(paramsList!=null)
							{
								System.out.println("Length = " + paramsList.getLength());
								Node paramNode = null;
								String paramValue = null,paramName = null;
								ArrayList paramList = new ArrayList();
								for(int i=0;i<paramsList.getLength();i++)
								{
									paramNode = paramsList.item(i);
									if((paramNode!=null)&&(paramNode.getNodeType()==Node.ELEMENT_NODE))
									{
										NamedNodeMap paramAttributes = paramNode.getAttributes();
										if(paramAttributes!=null)
										{
											Node paramNameNode = null,paramValueNode;
											paramNameNode = paramAttributes.getNamedItem(UIConfigurationConstants.NAME_ATTRIBUTE);
											paramValueNode = paramAttributes.getNamedItem(UIConfigurationConstants.VALUE_ATTRIBUTE);
											if(paramNameNode!=null)
											{
												paramName = paramNameNode.getNodeValue();
											}
											if(paramValueNode!=null)
											{
												paramValue = paramValueNode.getNodeValue();
											}
											NameValueBean paramNVB = new NameValueBean(paramName,paramValue);
											System.out.println(paramNVB.toString());
											paramList.add(paramNVB);
										}
									}
								}
								jspIncludeControl.setJspParams(paramList);
								System.out.println("Set JSP Params as "  + paramList);
							}
							//Assign as control data type
							controlDataType = jspIncludeControl;

						}
					}
				}
			}

			//It it is an Attribute Node
			if(attributeNode.getNodeName().equalsIgnoreCase(UIConfigurationConstants.ATTRIBUTE_NODE_TAGNAME))
			{
				String displayType = null;
				if(attributeNodeProperties!=null)
				{
					try {
						Node displayTypeNode = attributeNodeProperties.getNamedItem(UIConfigurationConstants.DISPLAY_TYPE_ATTRIBUTE);
						if(displayTypeNode!=null)
						{
							displayType = displayTypeNode.getNodeValue();
							if(displayType!=null)
							{
								if(rb_displayTypeClassNameMappings!=null)
								{
									String controlDataTypeClassName = rb_displayTypeClassNameMappings.getString(displayType);
									if(controlDataTypeClassName!=null)
									{
										Class controlDataTypeClass = Class.forName(controlDataTypeClassName);
										if(controlDataTypeClass!=null)
										{
											controlDataType = (Control)controlDataTypeClass.newInstance();
											if(controlDataType!=null)
											{
												Map propertiesMap = getPropertiesMap(attributeNodeProperties);

												//Get the caption from the map and replace it with value from properties file
												if(propertiesMap!=null)
												{
													String captionKey = (String)propertiesMap.get(UIConfigurationConstants.CAPTION_ATTRIBUTE);
													String captionValue = null;
													if((captionKey!=null)&&(rb_uicontrolsCaptions!=null))
													{
														try {
															captionValue = rb_uicontrolsCaptions.getString(captionKey);
														} catch (MissingResourceException e) {
															captionValue = "";
														}
													}
													else
													{
														captionValue = "";
													}
													propertiesMap.put(UIConfigurationConstants.CAPTION_ATTRIBUTE,captionValue);
												}
												//Get Values if any and add to map
												if(propertiesMap!=null)
												{
													Element attributeNodeElt = (Element) attributeNode;
													//Load the values for the node
													try {
														NodeList valueNodesList  = attributeNodeElt.getElementsByTagName(UIConfigurationConstants.VALUE_TAGNAME);
														System.out.println("No Of Values = " + valueNodesList.getLength());
														propertiesMap.put(UIConfigurationConstants.VALUES_LIST,getListOfValues(valueNodesList));
													} catch (Exception e) {
														System.out.println("Error while loading the values for control ");
													}
													//Load Event Handlers
													try {
														NodeList eventNodesList  = attributeNodeElt.getElementsByTagName(UIConfigurationConstants.EVENT_TAGNAME);
														System.out.println("No Of events = " + eventNodesList.getLength());
														propertiesMap.put(UIConfigurationConstants.EVENT_HANDLERS, getEventHandlers(eventNodesList));
													} catch (Exception e) {
														System.out.println("Error while loading the values for control ");
													}

												}
												//Populate attributes in the datatype class
												controlDataType.populateAttributes(propertiesMap);	
											}
										}
									}
								}
							}
						}

					} catch (DOMException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return controlDataType;
	}

	private Map getEventHandlers(NodeList eventNodesList)
	{
		HashMap eventHandlers = new HashMap();
		if(eventNodesList!=null)
		{
			int noOfEventNodes = eventNodesList.getLength();
			Node eventNameNode = null,eventHandlerNode=null;
			String eventName = null, eventHandlerName = null;
			for(int i=0;i<noOfEventNodes;i++)
			{
				Node eventNode = eventNodesList.item(i);
				if((eventNode!=null)&&(eventNode.getNodeType()==Node.ELEMENT_NODE))
				{
					NamedNodeMap eventAttributes = eventNode.getAttributes();
					if(eventAttributes!=null)
					{
						eventNameNode = eventAttributes.getNamedItem(UIConfigurationConstants.NAME_ATTRIBUTE);
						eventHandlerNode = eventAttributes.getNamedItem(UIConfigurationConstants.EVENT_HANDLER_ATTRIBUTE);

						if(eventNameNode!=null)
						{
							eventName = eventNameNode.getNodeValue();
						}
						if(eventHandlerNode!=null)
						{
							eventHandlerName = eventHandlerNode.getNodeValue();
						}

						if((eventName!=null)&&(eventHandlerName!=null))
						{
							eventHandlers.put(eventName, eventHandlerName);
							System.out.println("Adding event :" + eventName + " Handler Name :" + eventHandlerName);
						}
					}
				}

			}
		}
		return eventHandlers;

	}
	/**
	 * @param attributeNode
	 * @return String with list of values separated by a separator
	 */
	private ArrayList getListOfValues(NodeList setOfValueNodes) {
		ArrayList valuesListString = new ArrayList();
		String strValue=null;

		for(int j=0;j<setOfValueNodes.getLength();j++)
		{
			Node valueNode =setOfValueNodes.item(j);  	
			if((valueNode!=null)&&(valueNode.getNodeType()==Node.ELEMENT_NODE))
			{
				if(valueNode!=null)
				{
					Node value = valueNode.getFirstChild();
					if(value!=null)
					{
						strValue = value.getNodeValue();
						if(strValue!=null)
						{
							valuesListString.add(strValue);
						}
					}
				}
			}
		}

		return valuesListString;

	}

	private Map getPropertiesMap(NamedNodeMap attributeNodeProperties) {
		HashMap propertiesMap = new HashMap();
		if(attributeNodeProperties!=null)
		{
			int noOfProperties = attributeNodeProperties.getLength();
			for(int i=0;i<noOfProperties;i++)
			{
				Node attributeProperty = attributeNodeProperties.item(i);
				if(attributeProperty!=null)
				{
					if((attributeProperty.getNodeName()!=null)&&((attributeProperty.getNodeValue()!=null)))
					{
						propertiesMap.put(attributeProperty.getNodeName(),attributeProperty.getNodeValue());
					}
				}
			}
		}
		//propertiesMap
		return propertiesMap;
	}

	private void displayData()
	{
		if(m_UIControlsMap!=null)
		{
			Set keySet = m_UIControlsMap.keySet();
			Iterator iter = keySet.iterator();
			while(iter.hasNext())
			{
				String key = (String)iter.next();
				ArrayList list = (ArrayList)m_UIControlsMap.get(key);
				if(list!=null)
				{
					for(int i=0; i<list.size();i++)
					{
						Control dataType = (Control)list.get(i);
						System.out.println(dataType.generateHTML());
					}
				}
			}
		}
	}

	public List getControlNames()
	{
		if(m_UIControlsMap!=null)
		{
			return new ArrayList(m_UIControlsMap.keySet());
		}
		return null;
	}

	public List getConrolAttributesList(String controlName)
	{
		if((controlName!=null)&&(m_UIControlsMap!=null))
		{
			return (List)m_UIControlsMap.get(controlName);
		}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UIControlsConfigurationFactory UCCF = UIControlsConfigurationFactory.getInstance();
		List list = UCCF.getConrolAttributesList("TextControl");
		System.out.println(list);
		//UCCF.displayData();
		System.out.println("Done");
	}

}

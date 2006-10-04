/*
 * Created on Sep 27, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.domain.userinterface;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



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
public class UIControlsConfigurationFactory {
	
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
						System.out.println("Loading prop bundle");
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
								System.out.println("UI Control : " + uiControlName);
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
		String displayType = null;
		NamedNodeMap attributeNodeProperties = attributeNode.getAttributes();
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
												System.out.println("Searchin string "  + captionKey);
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
											System.out.println("Caption Value = " + captionValue);
											propertiesMap.put(UIConfigurationConstants.CAPTION_ATTRIBUTE,captionValue);
										}
										//Get Values if any and add to map
										if(propertiesMap!=null)
										{
											System.out.println(controlDataType);
											propertiesMap.put(UIConfigurationConstants.VALUES_LIST,getListOfValues(attributeNode));
										}
										//Populate attributes in the datatype class
										controlDataType.populateAttribute(propertiesMap);	
									}
								}
							}
						}
					}
				}
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return controlDataType;
	}

	/**
	 * @param attributeNode
	 * @return String with list of values separated by a separator
	 */
	private ArrayList getListOfValues(Node attributeNode) {
		ArrayList valuesListString = new ArrayList();
		String strValue=null;
		if(attributeNode!=null)
		{
			NodeList listOfValues  = attributeNode.getChildNodes();
			if(listOfValues!=null)
			{
				int noOfValues = listOfValues.getLength();
				System.out.println("No Of Value Nodes = " + noOfValues);
				for(int i=0;i<noOfValues;i++)
				{
					Node valuesNode = listOfValues.item(i);
					if(valuesNode!=null)
					{
						NodeList setOfValueNodes = valuesNode.getChildNodes();
						if(setOfValueNodes!=null)
						{
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
											System.out.println("StrValue = " + strValue);
											if(strValue!=null)
											{
												valuesListString.add(strValue);
											}
										}
									}
								}
							}
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
				System.out.println("Control => " + key);
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

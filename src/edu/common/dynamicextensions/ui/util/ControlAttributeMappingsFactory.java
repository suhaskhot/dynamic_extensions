/*
 * Created on Oct 17, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

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

import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author preeti_munot
 *
 * Class which parses the Controls - Attributes mappings 
 */
public class ControlAttributeMappingsFactory
{
	private static ControlAttributeMappingsFactory m_instance = null;
	private Hashtable m_controlAttributeMap = null;

	private ControlAttributeMappingsFactory() {
		m_controlAttributeMap = new Hashtable();
		parseXML();
	}

	public synchronized static ControlAttributeMappingsFactory getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new ControlAttributeMappingsFactory();
		}
		return m_instance;
	}

	/**
	 * Parse the XML
	 *
	 */
	private void parseXML()
	{
		String configurationFileName = "ControlAttributeMapping.xml";
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
							loadControlAttributeMappings(document);
						}
					}
					else
					{
						System.out.println("InputStream null...Please check");
					}
				}

			}
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param document
	 */
	private void loadControlAttributeMappings(Document document)
	{
		if(document!=null)
		{
			NodeList controlsList = document.getElementsByTagName(Constants.CONTROL_TAGNAME);
			if(controlsList!=null)
			{
				NodeList attributeNamesNodeList = null;
				Node controlNode = null,controlNameNode=null;
				NamedNodeMap controlAttributes = null;
				String controlName = null;
				ArrayList mappedAttributeNames = null;


				int noOfControls = controlsList.getLength();
				for(int i=0;i<noOfControls;i++)
				{
					mappedAttributeNames = new ArrayList();
					controlNode = controlsList.item(i);
					if(controlNode!=null)
					{
						//Get control attributes
						controlAttributes  = controlNode.getAttributes();
						if(controlAttributes!=null)
						{
							controlNameNode = controlAttributes.getNamedItem(Constants.NAME_ATTRIBUTE);
							if(controlNameNode!=null)
							{
								controlName = controlNameNode.getNodeValue();
								System.out.println("Conttrol Name = " + controlName);
								attributeNamesNodeList = ((Element)controlNode).getElementsByTagName(Constants.ATTRIBUTE_TAGNAME);
								if(controlName!=null)
								{
									//Get control attribute mappings
									mappedAttributeNames = getMappedAttributeNames(attributeNamesNodeList);
									if(mappedAttributeNames!=null)
									{
										System.out.println(mappedAttributeNames);
										m_controlAttributeMap.put(controlName,mappedAttributeNames);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param attributeNamesNodeList
	 * @return
	 */
	private ArrayList getMappedAttributeNames(NodeList attributeNamesNodeList)
	{
		ArrayList mappedAttributeNames = new ArrayList();
		Node attributeNameNode = null,attributeNameValueNode = null;
		String attributeName=null;
		NameValueBean nameValueBeanAttribute = null;
		int noOfAttributes = 0;
		if(attributeNamesNodeList!=null)
		{
			noOfAttributes = attributeNamesNodeList.getLength();
			for(int j=0;j<noOfAttributes;j++)
			{
				attributeNameNode = attributeNamesNodeList.item(j);
				if((attributeNameNode!=null)&&(attributeNameNode.getNodeType()==Node.ELEMENT_NODE))
				{
					attributeNameValueNode = attributeNameNode.getFirstChild();
					if(attributeNameValueNode!=null)
					{
						attributeName=attributeNameValueNode.getNodeValue();
						if(attributeName!=null)
						{
							nameValueBeanAttribute = new NameValueBean(attributeName,attributeName);
							mappedAttributeNames.add(nameValueBeanAttribute);
						}
					}
				}
			}
		}
		return mappedAttributeNames;
	}
	
	public List getAttributesForControl(String controlName)
	{
		if((controlName!=null)&&(m_controlAttributeMap!=null))
		{
			return (List)m_controlAttributeMap.get(controlName);
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ControlAttributeMappingsFactory cmf = ControlAttributeMappingsFactory.getInstance();
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
	}

}

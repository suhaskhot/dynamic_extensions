/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;
import org.openide.util.Lookup;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author preeti_lodha
 *
 * Utility functions for XMI import/XMI 
 */
public class XMIUtilities
{/*
	//Common Utility functions
	*//**
	 * @return MDRepository object
	 */
	public static MDRepository getRepository()
	{
		return null;
	}

	/**
	 * Get UML Package
	 * @param repository
	 * @param extent
	 * @return
	 */
	public static RefPackage getUMLPackage(MDRepository repository, String extent)
	{
		return null;
	}

	/**
	 * Get MOF Package 
	 * @param repository
	 * @param extent
	 * @return
	 */
	public static RefPackage getMOFPackage(MDRepository repository, String extent)
	{
		return null;
	}
	//XMI Import related
	/**
	 * Given a umlPackage will return a populated EntityGroup object
	 * @param umlPackage : umlPackage containing group information 
	 * @return EntityGroupInterface : Entity Group Domain object
	 */
	public static EntityGroupInterface getEntityGroup(UmlPackage umlPackage)
	{
		return null;
	}


	/**
	 *Given an umlClass object will return  object of Entity Inerface. 
	 *All details for the Entity interface will be populated in 
	 * @param umlClass : UML Class object 
	 * @return EntityInteface : Entity Domain object
	 */ 
	public static EntityInterface getEntity(UmlClass umlClass)
	{
		return null;
	}

	/**
	 * Given a uml Attribute will return a populated Attribute(Domain Object)  
	 * @param umlAttribute : UML Attribute 
	 * @return AttributeInteface : Entity Attribute Domain Object
	 */
	public static AttributeInterface getAttribute(Attribute umlAttribute)
	{
		return null;
	}

	/**
	 * Given a uml Association object will return an AssociationInterface(Domain) object 
	 * @param umlAssociation :  UML Association
	 * @return AssociationInterface : Association Domain Object
	 */
	public static AssociationInterface getAssociation(UmlAssociation umlAssociation)
	{
		return null;
	}


	//XMI Export Related
	/**
	 * 
	 * @param entityGroup
	 * @return
	 */
	public static UmlPackage getUMLPackage(EntityGroupInterface entityGroup)
	{
		return null;
	}


	/**
	 * Return a UML Class object for given Entity Domain Object
	 * @param EntityInterface : entity
	 * @return	 UML Class
	 */
	public static UmlClass getUMLClass(EntityInterface entity)
	{
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public static Attribute getUMLAttribute(AttributeInterface attribute)
	{
		return null;
	}

	/**
	 * Return a UML Class object for given Entity Domain Object
	 * @param association
	 * @return
	 */
	public static UmlAssociation getUMLAssociation(AssociationInterface association)
	{
		return null;
	}

	public static XmiWriter getXMIWriter()
	{
		XmiWriter writer = (XmiWriter) Lookup.getDefault().lookup(XmiWriter.class);
		return writer;
		
	}

	public static String getClassNameForEntity(EntityInterface entity)
	{
		if(entity!=null)
		{
			return entity.getName(); 
		}
		return null;
	}

	/**
	 * @return
	 */
	public static String getAttributeName(AttributeInterface attribute)
	{
		if(attribute!=null)
		{
			return attribute.getName();
		}
		return null;
	}
	/***
	 * Finds and returns the first model element having the given
	 * <code>name</code> in the <code>umlPackage</code>, returns
	 * <code>null</code> if not found.
	 *
	 * @param umlPackage The modelPackage to search
	 * @param name the name to find.
	 * @return the found model element.
	 */
	public static Object find(
			org.omg.uml.modelmanagement.UmlPackage umlPackage,
			final String name)
	{
		System.out.println("here");
		return CollectionUtils.find(
				umlPackage.getOwnedElement(),
				new Predicate()
				{
					public boolean evaluate(Object object)
					{
						return StringUtils.trimToEmpty(((ModelElement)object).getName()).equals(name);
						//return true;
					}
				});
	}
	 /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param modelPackage The modelPackage to search
     * @param name the name to find.
     * @return the found model element.
     */
    public static Object find(
    		org.omg.uml.UmlPackage modelPackage,
        final String name)
    {
        return CollectionUtils.find(
        		
            modelPackage.getModelManagement().getModel().refAllOfType(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (((ModelElement)object).getName()).equals(name);
                }
            });
    }
    public static void transform(File xmlFile) throws TransformerException, FileNotFoundException {
		final String XSLT_FILENAME = "D:\\DynamicExtensions\\abc.xsl";
		final String RSLT_FILENAME = "D:/DynamicExtensions/transformed-abc.xmi";
		File xsltFile = new File(XSLT_FILENAME); 
		Source xmlSource = new StreamSource(xmlFile); 
		Source xsltSource = new StreamSource(xsltFile); 
		FileOutputStream f = new FileOutputStream(RSLT_FILENAME);
		Result result = new StreamResult(f); 


//		 create an instance of TransformerFactory 
		TransformerFactory transFact = TransformerFactory.newInstance(); 

		Transformer trans = transFact.newTransformer(xsltSource); 

		trans.transform(xmlSource, result); 
		} 

}

/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiWriter;

import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;
import org.openide.util.Lookup;

import edu.common.dynamicextensions.domain.userinterface.Container;
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
{
	//Common Utility functions
	/**
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
}

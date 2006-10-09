
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.common.dynamicextensions.domaininterface.ByteArrayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PrimitiveAttributeInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

/**
 * @author sujay_narkar
 *
 */
public class DomainObjectFactory {
	
	/**
	 * Domain Object Factory Instance
	 */
	private static DomainObjectFactory domainObjectFactory;
	
	/**
	 * 
	 *
	 */
	protected DomainObjectFactory(){
		
	}
	
	/**
	 * Returns the instance of SegmentationDomainElementFactory
	 * @return SegmentationDomainElementFactory
	 */
	public static synchronized DomainObjectFactory getInstance () {
		if (domainObjectFactory == null) {
			domainObjectFactory= new  DomainObjectFactory();
		}
		return domainObjectFactory ;
	}
	
	/**
	 * Returns an object of Entity.
	 * @return an instance of Entity.
	 */
	public EntityInterface createEntity(){
	    Entity entity = new Entity();
	    return entity;
	}
	
	/**
	 * 
	 * @return an instance of EntityGroup.
	 */
	public EntityGroupInterface createEntityGroup(){
	    EntityGroup entityGroup = new EntityGroup();
	    return entityGroup;
	}
	
	/**
	 * 
	 * @return an instance of Association.
	 */
	public AssociationInterface createAssociation(){
	     Association association = new Association();
	     return association;
	}
	/**
	 * 
	 * @return an instance of Role.
	 */
	public RoleInterface createRole(){
	    Role role = new Role();
	    return role;
	}
	
	/**
	 * 
	 * @return an instance of SemanticProperty.
	 */
	public SemanticPropertyInterface createSemanticProperty() {
	    SemanticProperty semanticProperty = new SemanticProperty();
	    return semanticProperty;
	}
	
	/**
	 * 
	 * @return an instance of ColumnProperties.
	 */
	public ColumnPropertiesInterface createColumnProperties(){
	    ColumnProperties columnProperties = new ColumnProperties();
	    return columnProperties;
	}
	/**
	 * 
	 * @return an instance of TableProperties.
	 */
	public TablePropertiesInterface createTableProperties(){
	    TableProperties tableProperties = new TableProperties();
	    return tableProperties;
	}
	/**
	 * 
	 * @return an instance of ConstraintProperties.
	 */
	public ConstraintPropertiesInterface createConstraintProperties(){
	    ConstraintProperties constraintProperties = new ConstraintProperties();
	    return constraintProperties;
	}
	/**
	 * 
	 * @return instance of BooleanAttribute.
	 */
	public BooleanAttributeInterface createBooleanAttribute(){
	    BooleanAttribute booleanAttribute = new BooleanAttribute();
	    return booleanAttribute;
	}
	/**
	 * 
	 * @return instance of ByteArrayAttribute.
	 */
	public ByteArrayAttributeInterface createByteArrayAttribute(){
	    ByteArrayAttribute byteArrayAttribute = new ByteArrayAttribute();
	    return byteArrayAttribute;
	}
	/**
	 * 
	 * @return instance of DateAttribute.
	 */
	public DateAttributeInterface createDateAttribute(){
	    DateAttribute dateAttribute = new DateAttribute();
	    return dateAttribute;
	}
	
	
}

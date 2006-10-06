package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * An entity can have multiple associations, where each association is linked
 * to another entity.
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ASSOCIATION" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class Association extends Attribute implements java.io.Serializable {
	
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Direction of the association.
	 */
	protected String direction;
	/**
	 * The source entity of this association.Source entity is linked to target entity through this association object.
	 */
	protected Collection sourceEntityCollection;
	/**
	 * Source role of association.This specifies how the source entity is related to target entity.
	 */
	protected Collection sourceRoleCollection;
	/**
	 * Target role of association.This specifies how the target entity is related to source entity.
	 */
	protected Collection targetRoleCollection;
	/**
	 * The target entity of this association.
	 */
	protected Collection targetEntityCollection;
	
	/**
	 * Constraint properties related to this association.
	 */
	public Collection constraintPropertiesCollection;
	
	/**
	 * Empty Constructor.
	 */
	public Association(){
		
	}

	/**
	 * @hibernate.property name="direction" type="string" column="DIRECTION" 
	 * @return Returns the direction.
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	

	

	
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @hibernate.set name="sourceEntityCollection" table="DYEXTN_ENTITY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Entity" 
	 * @return Returns the sourceEntityCollection.
	 */
	public Collection getSourceEntityCollection() {
		return sourceEntityCollection;
	}
	/**
	 * @param sourceEntityCollection The sourceEntityCollection to set.
	 */
	public void setSourceEntityCollection(Collection sourceEntityCollection) {
		this.sourceEntityCollection = sourceEntityCollection;
	}
	/**
     * @hibernate.set name="targetEntityCollection" table="DYEXTN_ENTITY"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="ASSOCIATION_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Entity" 
	 * @return Returns the targetEntityCollection.
	 */
	public Collection getTargetEntityCollection() {
		return targetEntityCollection;
	}
	/**
	 * @param targetEntityCollection The targetEntityCollection to set.
	 */
	public void setTargetEntityCollection(Collection targetEntityCollection) {
		this.targetEntityCollection = targetEntityCollection;
	}
	/**
     * @hibernate.set name="sourceRoleCollection" table="DYEXTN_ROLE
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="ASSOCIATION_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role"  
     * 
	 * @return Returns the sourceRoleCollection.
	 */
	public Collection getSourceRoleCollection() {
		return sourceRoleCollection;
	}
	/**
	 * @param sourceRoleCollection The sourceRoleCollection to set.
	 */
	public void setSourceRoleCollection(Collection sourceRoleCollection) {
		this.sourceRoleCollection = sourceRoleCollection;
	}
	/**
     * @hibernate.set name="targetRoleCollection" table="DYEXTN_ROLE
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="ASSOCIATION_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role" 
	 * @return Returns the targetRoleCollection.
	 */
	public Collection getTargetRoleCollection() {
		return targetRoleCollection;
	}
	/**
	 * @param targetRoleCollection The targetRoleCollection to set.
	 */
	public void setTargetRoleCollection(Collection targetRoleCollection) {
		this.targetRoleCollection = targetRoleCollection;
	}
	/**
     * @hibernate.set name="constraintPropertiesCollection" table="DYEXTN_CONSTRAINT_PROPERTIES"
     * cascade="save-update" inverse="false" lazy="false"
     * @hibernate.collection-key column="ASSOCIATION_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties" 
	 * @return Returns the constraintPropertiesCollection.
	 */
	public Collection getConstraintPropertiesCollection() {
		return constraintPropertiesCollection;
	}
	/**
	 * @param constraintPropertiesCollection The constraintPropertiesCollection to set.
	 */
	public void setConstraintPropertiesCollection(
			Collection constraintPropertiesCollection) {
		this.constraintPropertiesCollection = constraintPropertiesCollection;
	}
}
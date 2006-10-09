package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
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
	/**
	 * 
	 */	
	
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
	private Collection getSourceEntityCollection() {
		return sourceEntityCollection;
	}
	/**
	 * @param sourceEntityCollection The sourceEntityCollection to set.
	 */
	private void setSourceEntityCollection(Collection sourceEntityCollection) {
		this.sourceEntityCollection = sourceEntityCollection;
	}
	
	/**
	 * 
	 * @return
	 */
	public Entity getSourceEntity(){
		if(sourceEntityCollection != null){
			Iterator sourceEntityIterator = sourceEntityCollection.iterator();
			return (Entity)sourceEntityIterator.next();
		} else {
			return null;   
		}
		
	}
	
	/**
	 * 
	 * @param sourceEntity
	 */
	public void setSourceEntity(Entity sourceEntity){
		if(sourceEntityCollection == null){
			sourceEntityCollection = new HashSet();
		}
		this.sourceEntityCollection.add(sourceEntity);
	}
	/**
	 * @hibernate.set name="targetEntityCollection" table="DYEXTN_ENTITY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Entity" 
	 * @return Returns the targetEntityCollection.
	 */
	private Collection getTargetEntityCollection() {
		return targetEntityCollection;
	}
	/**
	 * @param targetEntityCollection The targetEntityCollection to set.
	 */
	private void setTargetEntityCollection(Collection targetEntityCollection) {
		this.targetEntityCollection = targetEntityCollection;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Entity getTargetEntity(){
		if(targetEntityCollection != null){
			Iterator targetEntityIterator = targetEntityCollection.iterator();
			return (Entity)targetEntityIterator.next();
		} else {
			return null;   
		}
		
	}
	
	/**
	 * 
	 * @param sourceEntity
	 */
	public void setTargetEntity(Entity targetEntity){
		if(targetEntityCollection == null){
			targetEntityCollection = new HashSet();
		}
		this.targetEntityCollection.add(targetEntity);
	}
	/**
	 * @hibernate.set name="sourceRoleCollection" table="DYEXTN_ROLE
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role"  
	 * 
	 * @return Returns the sourceRoleCollection.
	 */
	private Collection getSourceRoleCollection() {
		return sourceRoleCollection;
	}
	/**
	 * @param sourceRoleCollection The sourceRoleCollection to set.
	 */
	private void setSourceRoleCollection(Collection sourceRoleCollection) {
		this.sourceRoleCollection = sourceRoleCollection;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Role getSourceRole(){
		if(sourceRoleCollection != null){
			Iterator sourceRoleIterator = sourceRoleCollection.iterator();
			return (Role)sourceRoleIterator.next();
		} else {
			return null;   
		}
		
	}
	
	/**
	 * 
	 * @param sourceEntity
	 */
	public void setSourceRole(Role sourceRole){
		if(sourceRoleCollection == null){
			sourceRoleCollection = new HashSet();
		}
		this.sourceRoleCollection.add(sourceRole);
	}
	
	
	
	/**
	 * @hibernate.set name="targetRoleCollection" table="DYEXTN_ROLE
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role" 
	 * @return Returns the targetRoleCollection.
	 */
	private Collection getTargetRoleCollection() {
		return targetRoleCollection;
	}
	/**
	 * @param targetRoleCollection The targetRoleCollection to set.
	 */
	public void setTargetRoleCollection(Collection targetRoleCollection) {
		this.targetRoleCollection = targetRoleCollection;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Role getTargetRole(){
		if(targetRoleCollection != null){
			Iterator targetRoleIterator = targetRoleCollection.iterator();
			return (Role)targetRoleIterator.next();
		} else {
			return null;   
		}
		
	}
	
	/**
	 * 
	 * @param sourceEntity
	 */
	public void setTargetRole(Role targetRole){
		if(targetRoleCollection == null){
			targetRoleCollection = new HashSet();
		}
		this.targetRoleCollection.add(targetRole);
	}
	/**
	 * @hibernate.set name="constraintPropertiesCollection" table="DYEXTN_CONSTRAINT_PROPERTIES"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties" 
	 * @return Returns the constraintPropertiesCollection.
	 */
	private Collection getConstraintPropertiesCollection() {
		return constraintPropertiesCollection;
	}
	/**
	 * @param constraintPropertiesCollection The constraintPropertiesCollection to set.
	 */
	private void setConstraintPropertiesCollection(
			Collection constraintPropertiesCollection) {
		this.constraintPropertiesCollection = constraintPropertiesCollection;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public ConstraintProperties getConstraintProperties(){
		if(constraintPropertiesCollection != null){
			Iterator constraintPropertiesIterator = constraintPropertiesCollection.iterator();
			return (ConstraintProperties)constraintPropertiesIterator .next();
		} else {
			return null;   
		}
		
	}
	
    
	/**
	 * 
	 * @param sourceEntity
	 */
	public void setConstraintProperties(ConstraintProperties constraintProperties){
		if(constraintPropertiesCollection == null){
			constraintPropertiesCollection  = new HashSet();
		}
		this.constraintPropertiesCollection .add(constraintProperties);
	}
	
}
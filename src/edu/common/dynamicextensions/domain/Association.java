package edu.common.dynamicextensions.domain;

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
	protected Entity sourceEntity;
    /**
     * Source role of association.This specifies how the source entity is related to target entity.
     */
	protected Role sourceRole;
    /**
     * Target role of association.This specifies how the target entity is related to source entity.
     */
	protected Role targetRole;
    /**
     * The target entity of this association.
     */
	protected Entity targetEntity;
    
    /**
     * Constraint properties related to this association.
     */
	public ConstraintProperties constraintProperties;
	
	/**
     * Empty Constructor.
	 */
	public Association(){

	}
   /**
    * @hibernate.many-to-one column ="CONSTRAINT_PROPERTY_ID" class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties"
    * @return Returns the constraintProperties.
    */
    public ConstraintProperties getConstraintProperties() {
        return constraintProperties;
    }
    /**
     * @param constraintProperties The constraintProperties to set.
     */
    public void setConstraintProperties(
            ConstraintProperties constraintProperties) {
        this.constraintProperties = constraintProperties;
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
     * @hibernate.many-to-one column ="SOURCE_ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity"
     * @return Returns the sourceEntity.
     */
    public Entity getSourceEntity() {
        return sourceEntity;
    }
    /**
     * @param sourceEntity The sourceEntity to set.
     */
    public void setSourceEntity(Entity sourceEntity) {
        this.sourceEntity = sourceEntity;
    }
    /**
     * @hibernate.many-to-one column ="SOURCE_ROLE_ID" class="edu.common.dynamicextensions.domain.Role"
     * @return Returns the sourceRole.
     */
    public Role getSourceRole() {
        return sourceRole;
    }
    /**
     * @param sourceRole The sourceRole to set.
     */
    public void setSourceRole(Role sourceRole) {
        this.sourceRole = sourceRole;
    }
    /**
     *  @hibernate.many-to-one column ="TARGET_ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity"
     * @return Returns the targetEntity.
     */
    public Entity getTargetEntity() {
        return targetEntity;
    }
    /**
     * @param targetEntity The targetEntity to set.
     */
    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
    /**
     * @hibernate.many-to-one column ="TARGET_ROLE_ID" class="edu.common.dynamicextensions.domain.Role"
     * @return Returns the targetRole.
     */
    public Role getTargetRole() {
        return targetRole;
    }
    /**
     * @param targetRole The targetRole to set.
     */
    public void setTargetRole(Role targetRole) {
        this.targetRole = targetRole;
    }

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}
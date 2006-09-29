package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * An entity can have multiple associations, where each association has is linked
 * to another entity.
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.class table="DYEXTN_ASSOCIATION"
 */
public class Association extends Attribute implements java.io.Serializable {

    private static final long serialVersionUID = 1234567890L;

	/**
     * 
	 */
    protected String direction;
    /**
     * 
     */
	protected Entity sourceEntity;
    /**
     * 
     */
	protected Role sourceRole;
    /**
     * 
     */
	protected Role targetRole;
    /**
     * 
     */
	protected Entity targetEntity;
    
    /**
     * 
     */
	public ConstraintProperties constraintProperties;
	/**
     * 
	 *
	 */
	public Association(){

	}
   /**
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
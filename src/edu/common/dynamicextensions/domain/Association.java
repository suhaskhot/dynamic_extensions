
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * An entity can have multiple associations, where each association is linked to another entity.
 * This Class represents the Association between the Entities.
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ASSOCIATION" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class Association extends AbstractAttribute implements java.io.Serializable, AssociationInterface
{

	/**
	 * Serial Version Unique Identifief
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Direction of the association.
	 */
	protected String direction;

	/**
	 * The source entity of this association. Source entity is linked to target entity through this association object.
	 */
	protected Collection<EntityInterface> sourceEntityCollection;

	/**
	 * Source role of association.This specifies how the source entity is related to target entity.
	 */
	protected Collection<RoleInterface> sourceRoleCollection;

	/**
	 * Target role of association.This specifies how the target entity is related to source entity.
	 */
	protected Collection<RoleInterface> targetRoleCollection;

	/**
	 * The target entity of this association.
	 */
	protected Collection<EntityInterface> targetEntityCollection;

	/**
	 * Constraint properties related to this association.
	 */
	public Collection<ConstraintPropertiesInterface> constraintPropertiesCollection;

	/**
	 * Empty Constructor.
	 */
	public Association()
	{
	}

	/**
	 * This method returns the direction of the Association.
	 * @hibernate.property name="direction" type="string" column="DIRECTION" 
	 * @return the direction of the Association
	 */
	public String getDirection()
	{
		return direction;
	}

	/**
	 * This method sets the direction of the Association.
	 * @param direction the direction of the Association to be set.
	 */
	public void setDirection(String direction)
	{
		this.direction = direction;
	}

	/**
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

	/**
	 * This method returns the Collection of source Entities of this Asscociation.
	 * @hibernate.set name="sourceEntityCollection" table="DYEXTN_ENTITY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Entity" 
	 * @return the Collection of source Entities of this Asscociation.
	 */
	private Collection<EntityInterface> getSourceEntityCollection()
	{
		return sourceEntityCollection;
	}

	/**
	 * This method sets the Collection of source Entities of this Asscociation.
	 * @param sourceEntityCollection The sourceEntityCollection to be set.
	 */
	private void setSourceEntityCollection(Collection<EntityInterface> sourceEntityCollection)
	{
		this.sourceEntityCollection = sourceEntityCollection;
	}

	/**
	 * This method returns the source Entity of this Association.
	 * @return the source Entity of this Association.
	 */
	public EntityInterface getSourceEntity()
	{
		EntityInterface sourceEntity = null;
		if (sourceEntityCollection != null)
		{
			Iterator sourceEntityIterator = sourceEntityCollection.iterator();
			sourceEntity = (EntityInterface) sourceEntityIterator.next();
		}
		return sourceEntity;
	}

	/**
	 * This method sets the source Entity of the Association.
	 * @param sourceEntity the Entity to be set as source of the Association.
	 */
	public void setSourceEntity(EntityInterface sourceEntity)
	{
		if (sourceEntityCollection == null)
		{
			sourceEntityCollection = new HashSet<EntityInterface>();
		}
		this.sourceEntityCollection.add(sourceEntity);
	}

	/**
	 * This method returns the collection of the target Entities.
	 * @hibernate.set name="targetEntityCollection" table="DYEXTN_ENTITY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Entity" 
	 * @return the collection of the target Entities.
	 */
	private Collection<EntityInterface> getTargetEntityCollection()
	{
		return targetEntityCollection;
	}

	/**
	 * This method sets the collection of the target entities of the Association to the given target entity collection.
	 * @param targetEntityCollection The targetEntityCollection to be set.
	 */
	private void setTargetEntityCollection(Collection<EntityInterface> targetEntityCollection)
	{
		this.targetEntityCollection = targetEntityCollection;
	}

	/**
	 * This method returns the target Entity of the Association.
	 * @return the target Entity of the Association
	 */
	public EntityInterface getTargetEntity()
	{
		EntityInterface targetEntity = null;
		if (targetEntityCollection != null)
		{
			Iterator targetEntityIterator = targetEntityCollection.iterator();
			targetEntity = (EntityInterface) targetEntityIterator.next();
		}
		return targetEntity;
	}

	/**
	 * This method sets the target Entity of the Association to the given Entity.
	 * @param targetEntity the Entity to be set as target Entity of the Association.
	 */
	public void setTargetEntity(EntityInterface targetEntity)
	{
		if (targetEntityCollection == null)
		{
			targetEntityCollection = new HashSet<EntityInterface>();
		}
		this.targetEntityCollection.add(targetEntity);
	}

	/**
	 * This method returns the Collection of the source Roles of the Association.
	 * @hibernate.set name="sourceRoleCollection" table="DYEXTN_ROLE
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role"
	 * @return the Collection of the source Roles of the Association.
	 */
	private Collection<RoleInterface> getSourceRoleCollection()
	{
		return sourceRoleCollection;
	}

	/**
	 * This method sets the sourceRoleCollection to the given Collection of the source Roles.
	 * @param sourceRoleCollection The Collection of the source Roles to be set.
	 */
	private void setSourceRoleCollection(Collection<RoleInterface> sourceRoleCollection)
	{
		this.sourceRoleCollection = sourceRoleCollection;
	}

	/**
	 * This method returns the source Role of the Association.
	 * @return the source Role of the Association.
	 */
	public RoleInterface getSourceRole()
	{
		RoleInterface sourceRole = null;
		if (sourceRoleCollection != null)
		{
			Iterator sourceRoleIterator = sourceRoleCollection.iterator();
			sourceRole = (RoleInterface) sourceRoleIterator.next();
		}
		return sourceRole;
	}

	/**
	 * This method sets the source Role of the Association.
	 * @param sourceRole the Role to be set as source Role.
	 */
	public void setSourceRole(RoleInterface sourceRole)
	{
		if (sourceRoleCollection == null)
		{
			sourceRoleCollection = new HashSet<RoleInterface>();
		}
		this.sourceRoleCollection.add(sourceRole);
	}

	/**
	 * This method returns the Collection of the target Roles of the Association.
	 * @hibernate.set name="targetRoleCollection" table="DYEXTN_ROLE
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Role" 
	 * @return the Collection of the target Roles of the Association.
	 */
	private Collection<RoleInterface> getTargetRoleCollection()
	{
		return targetRoleCollection;
	}

	/**
	 * This method sets the targetRoleCollection to the given Collection of target Roles.
	 * @param targetRoleCollection The Collection of target Roles to be set.
	 */
	private void setTargetRoleCollection(Collection<RoleInterface> targetRoleCollection)
	{
		this.targetRoleCollection = targetRoleCollection;
	}

	/**
	 * This method returns the targetRole of the Association.
	 * @return the targetRole of the Association.
	 */
	public RoleInterface getTargetRole()
	{
		RoleInterface targetRole = null;
		if (targetRoleCollection != null)
		{
			Iterator targetRoleIterator = targetRoleCollection.iterator();
			targetRole = (RoleInterface) targetRoleIterator.next();
		}
		return targetRole;
	}

	/**
	 * This method sets the target Role of the Association.
	 * @param targetRole the Role to be set as targetRole of the Association.
	 */
	public void setTargetRole(RoleInterface targetRole)
	{
		if (targetRoleCollection == null)
		{
			targetRoleCollection = new HashSet<RoleInterface>();
		}
		this.targetRoleCollection.add(targetRole);
	}

	/**
	 * This method returns the Collection of the ConstraintProperties of the Association.
	 * 
	 * @hibernate.set name="constraintPropertiesCollection" table="DYEXTN_CONSTRAINT_PROPERTIES"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties" 
	 * 
	 * @return the Collection of the ConstraintProperties of the Association.
	 */
	private Collection<ConstraintPropertiesInterface> getConstraintPropertiesCollection()
	{
		return constraintPropertiesCollection;
	}

	/**
	 * This method sets constraintPropertiesCollection to the given Collection of the ConstraintProperties.
	 * @param constraintPropertiesCollection The constraintPropertiesCollection to set.
	 */
	private void setConstraintPropertiesCollection(Collection<ConstraintPropertiesInterface> constraintPropertiesCollection)
	{
		this.constraintPropertiesCollection = constraintPropertiesCollection;
	}

	/**
	 * This method returns the ConstraintProperties of the Association.
	 * @return the ConstraintProperties of the Association. 
	 */
	public ConstraintPropertiesInterface getConstraintProperties()
	{
		ConstraintPropertiesInterface contraintProperties = null;
		if (constraintPropertiesCollection != null)
		{
			Iterator constraintPropertiesIterator = constraintPropertiesCollection.iterator();
			contraintProperties = (ConstraintPropertiesInterface) constraintPropertiesIterator.next();
		}
		return contraintProperties;
	}

	/**
	 * This method sets the constraintProperties to the given ContraintProperties.
	 * @param constraintProperties the constraintProperties to be set.
	 */
	public void setConstraintProperties(ConstraintPropertiesInterface constraintProperties)
	{
		if (constraintPropertiesCollection == null)
		{
			constraintPropertiesCollection = new HashSet<ConstraintPropertiesInterface>();
		}
		this.constraintPropertiesCollection.add(constraintProperties);
	}

}
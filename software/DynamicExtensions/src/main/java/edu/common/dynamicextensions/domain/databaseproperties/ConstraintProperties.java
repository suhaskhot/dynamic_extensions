
package edu.common.dynamicextensions.domain.databaseproperties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintKeyPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_CONSTRAINT_PROPERTIES"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ConstraintProperties extends DatabaseProperties
		implements
			ConstraintPropertiesInterface
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7190506925704368959L;

	/**
	 * The source entity key through which constraint is related.
	 * e.g. Used in case of foreign key constraint in one to many relation.
	 */
	protected Collection<ConstraintKeyPropertiesInterface> srcEntityConstKeyPropColl = new HashSet<ConstraintKeyPropertiesInterface>();
	/**
	 * The target entity key through which constraint is related.
	 * Used in case of many to many relation.Both source and target entity key is entered in the intermediate table.
	 */
	protected Collection<ConstraintKeyPropertiesInterface> tgtEntityConstKeyPropColl = new HashSet<ConstraintKeyPropertiesInterface>();

	/**
	 * The name of the Constraint
	 */
	protected String constraintName;

	/**
	 * It will return the srcEntityConstraintCollection on which the constraint depends
	 * @hibernate.set name="srcEntityConstraintKeyPropertiesCollection" table="DYEXTN_CONSTRAINTKEY_PROP"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SRC_CONSTRAINT_KEY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintKeyProperties"
	 * @return Returns the srcEntityConstraintKeyPropertiesCollection.
	 * @return
	 */
	public Collection<ConstraintKeyPropertiesInterface> getSrcEntityConstraintKeyPropertiesCollection()
	{
		return srcEntityConstKeyPropColl;
	}

	/**
	 * It will set the srcEntityConstraintCollection
	 * @param srcEntityConstraint
	 */
	public void setSrcEntityConstraintKeyPropertiesCollection(
			Collection<ConstraintKeyPropertiesInterface> srcEntityConstKeyPropColl)
	{
		this.srcEntityConstKeyPropColl = srcEntityConstKeyPropColl;
	}

	/**
	 * @hibernate.set name="tgtEntityConstraintKeyPropertiesCollection" table="DYEXTN_CONSTRAINTKEY_PROP"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="TGT_CONSTRAINT_KEY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintKeyProperties"
	 * @return tgtEntityConstraintKeyPropertiesCollection
	 */
	public Collection<ConstraintKeyPropertiesInterface> getTgtEntityConstraintKeyPropertiesCollection()
	{
		return tgtEntityConstKeyPropColl;
	}

	/**
	 * It will set the srcEntityConstraintCollection
	 * @param tgtEntityConstraint
	 */
	public void setTgtEntityConstraintKeyPropertiesCollection(
			Collection<ConstraintKeyPropertiesInterface> tgtEntityConstraintCollection)
	{
		this.tgtEntityConstKeyPropColl = tgtEntityConstraintCollection;
	}

	/**
	 * It will clear the srcEntityConstraintCollection and then add add the srcEntityConstraint properties
	 */
	public void setSrcEntityConstraintKeyProp(ConstraintKeyPropertiesInterface srcEntityConstraint)
	{
		if (srcEntityConstKeyPropColl == null)
		{
			srcEntityConstKeyPropColl = new HashSet();
		}
		else
		{
			srcEntityConstKeyPropColl.clear();
		}
		this.srcEntityConstKeyPropColl.add(srcEntityConstraint);
	}

	/**
	 * It will clear the tgtEntityConstraintCollection and then add add the tgtEntityConstraint properties
	 */
	public void setTgtEntityConstraintKeyProp(ConstraintKeyPropertiesInterface tgtEntityConstraint)
	{
		if (tgtEntityConstKeyPropColl == null)
		{
			tgtEntityConstKeyPropColl = new HashSet();
		}
		else
		{
			tgtEntityConstKeyPropColl.clear();
		}

		this.tgtEntityConstKeyPropColl.add(tgtEntityConstraint);

	}

	/**
	 * It will retrieve the constraintKeyProperties from srcEntityConstraintCollection
	 * @return srcEntityConstraintKeyProperties
	 */
	public ConstraintKeyPropertiesInterface getSrcEntityConstraintKeyProperties()
	{
		ConstraintKeyPropertiesInterface cnstrKeyProp = null;
		if (srcEntityConstKeyPropColl != null
				&& !srcEntityConstKeyPropColl.isEmpty())
		{
			Iterator srcEntityConstraintIterator = srcEntityConstKeyPropColl
					.iterator();
			cnstrKeyProp = (ConstraintKeyPropertiesInterface) srcEntityConstraintIterator.next();
		}
		return cnstrKeyProp;

	}

	/**
	 * it will retrieve the tgtEntityConstraint From tgtEntityConstraintCollection
	 * @return
	 */
	public ConstraintKeyPropertiesInterface getTgtEntityConstraintKeyProperties()
	{
		ConstraintKeyPropertiesInterface cnstrKeyProp = null;
		if (tgtEntityConstKeyPropColl != null
				&& !tgtEntityConstKeyPropColl.isEmpty())
		{
			Iterator tgtEntityConstraintIterator = tgtEntityConstKeyPropColl
					.iterator();
			cnstrKeyProp = (ConstraintKeyPropertiesInterface) tgtEntityConstraintIterator.next();
		}
		return cnstrKeyProp;

	}

	/**
	 * It will set the name of the foreign key constraint
	 * @hibernate.property name="ConstraintName" type="string" column="CONSTRAINT_NAME"
	 * @return
	 */
	public String getConstraintName()
	{
		return constraintName;
	}

	/**
	 * It will return the name of the foreign key constraint
	 */
	public void setConstraintName(String constraintName)
	{
		this.constraintName = constraintName;
	}

	/**
	 * It will add the srcCnstrKeyProp in the srcEntityConstraintCollection
	 * @param srcCnstrKeyProp
	 */
	public void addSrcConstaintKeyProperties(ConstraintKeyPropertiesInterface srcCnstrKeyProp)
	{
		if (srcEntityConstKeyPropColl == null)
		{
			srcEntityConstKeyPropColl = new HashSet();
		}
		srcEntityConstKeyPropColl.add(srcCnstrKeyProp);

	}

	/**
	 * It will add the srcCnstrKeyProp in the srcEntityConstraintCollection
	 * @param srcCnstrKeyProp
	 */
	public void addTgtConstraintKeyProperties(ConstraintKeyPropertiesInterface tgtCnstrKeyProp)
	{
		if (tgtEntityConstKeyPropColl == null)
		{
			tgtEntityConstKeyPropColl = new HashSet();
		}
		tgtEntityConstKeyPropColl.add(tgtCnstrKeyProp);
	}

}
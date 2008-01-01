package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
/**
 * @hibernate.class table="DYEXTN_PATH"
 * @author rajesh_patil
 *
 */
public class Path extends DynamicExtensionBaseDomainObject implements PathInterface
{
	/**
	 * Serial Version Unique Identifier
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 *
	 */
    Collection<AssociationInterface> associationCollection = new HashSet<AssociationInterface>();

    /**
     *
     *
     */
    public Path()
    {
    	super();
    }

    /**

     * @hibernate.set name="associationCollection" table="DYEXTN_PATH_ASSOCIATION_REL"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PATH_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.Association" column="ASSOCIATION_ID"
     */
    public Collection<AssociationInterface> getAssociationCollection() {
        return associationCollection;
    }

    /**
     * @param associationCollection the associationCollection to set
     */
    public void setAssociationCollection(Collection<AssociationInterface> associationCollection) {
        this.associationCollection = associationCollection;
    }

	public void addAssociation(AssociationInterface associationInterface)
	{
		// TODO Auto-generated method stub

	}

	public void removeAssociation(AssociationInterface associationInterface)
	{
		// TODO Auto-generated method stub

	}

	public void removeAllAssociations()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * This method returns the unique identifier of the Path.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_PATH_SEQ"
	 * @return the identifier of the Path.
	 */
	public Long getId()
	{
		return id;
	}

}

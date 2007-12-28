package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
/**
 * @hibernate.class table="DYEXTN_PATH"
 * @author rajesh_patil
 *
 */
public class Path implements PathInterface
{

	/**
	 *
	 */
    Collection<Association> associationCollection = new HashSet<Association>();

    /**
     *
     *
     */
    public Path()
    {
    	super();
    }

    /**

     * @hibernate.set name="associationCollection" table="DYEXTN_PATH_ASSOCTION_REL"
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PATH_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.Association" column="ASSOCTION_ID"
     */
    public Collection<Association> getAssociationCollection() {
        return associationCollection;
    }

    /**
     * @param associationCollection the associationCollection to set
     */
    public void setAssociationCollection(Collection<Association> associationCollection) {
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

}

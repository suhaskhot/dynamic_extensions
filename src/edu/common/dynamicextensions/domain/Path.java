package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
/**
 * @hibernate.class table="DYEXTN_PATH"
 * @author rajesh_patil
 *
 */
public class Path {

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
    private Collection<Association> getAssociationCollection() {
        return associationCollection;
    }

    /**
     * @param associationCollection the associationCollection to set
     */
    private void setAssociationCollection(Collection<Association> associationCollection) {
        this.associationCollection = associationCollection;
    }

}

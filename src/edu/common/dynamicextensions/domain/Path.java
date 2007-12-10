package edu.common.dynamicextensions.domain;

import java.util.HashSet;
import java.util.Set;

public class Path {
    
    protected Set<Association> associationCollection = new HashSet<Association>();
    
    public Path()
    {
        
    }

    /**
     * @return the associationCollection
     */
    public Set<Association> getAssociationCollection() {
        return associationCollection;
    }

    /**
     * @param associationCollection the associationCollection to set
     */
    public void setAssociationCollection(Set<Association> associationCollection) {
        this.associationCollection = associationCollection;
    }

}

package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class Role {

	protected String associationType;
	protected Integer maxCardinality;
	protected Integer minCardinality;
	protected String name;

	public Role(){

	}

	public void finalize() throws Throwable {

	}

	
    /**
     * @return Returns the associationType.
     */
    public String getAssociationType() {
        return associationType;
    }
    /**
     * @param associationType The associationType to set.
     */
    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }
    /**
     * @return Returns the maxCardinality.
     */
    public Integer getMaxCardinality() {
        return maxCardinality;
    }
    /**
     * @param maxCardinality The maxCardinality to set.
     */
    public void setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
    }
    /**
     * @return Returns the minCardinality.
     */
    public Integer getMinCardinality() {
        return minCardinality;
    }
    /**
     * @param minCardinality The minCardinality to set.
     */
    public void setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
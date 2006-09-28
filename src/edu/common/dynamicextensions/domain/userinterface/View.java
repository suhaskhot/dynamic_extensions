package edu.common.dynamicextensions.domain.userinterface;

import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 */
public class View {

	protected Long id;
	protected String name;
	protected Collection containerCollection;

	public View(){

	}

	public void finalize() throws Throwable {

	}
	

    /**
     * @return Returns the containerCollection.
     */
    public Collection getContainerCollection() {
        return containerCollection;
    }
    /**
     * @param containerCollection The containerCollection to set.
     */
    public void setContainerCollection(Collection containerCollection) {
        this.containerCollection = containerCollection;
    }
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
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
package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;
/**
 * ViewInterface stores necessary information for generating view on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface ViewInterface {
    
    /**
	 * @return Returns the id.
	 */
	public Long getId();
	
	/**
	 * @return Returns the name.
	 */
	public String getName();
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) ;
		
	/**
	 * @return Returns the containerCollection.
	 */
	public Collection getContainerCollection();
	/**
	 * @param containerCollection The containerCollection to set.
	 */
	public void addContainer(ContainerInterface containerInterface);
	
}

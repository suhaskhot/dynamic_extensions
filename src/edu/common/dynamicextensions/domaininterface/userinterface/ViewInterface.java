package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface ViewInterface {
    
    /**
	 * @return Returns the id.
	 */
	public Long getId();
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id);
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
	public void setContainerCollection(Collection containerCollection);
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
	 */
	public Long getSystemIdentifier();
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
	 */
	public void setSystemIdentifier(Long id);

}

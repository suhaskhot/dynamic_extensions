package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * 
 * @author mandar_shidhore
 *
 */
public interface NewEntityManagerInterface {
    
    public void saveEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

}

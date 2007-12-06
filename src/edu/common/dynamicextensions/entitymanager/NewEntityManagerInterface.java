package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.dbManager.DAOException;

public interface NewEntityManagerInterface {
    
    public void saveEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException, DAOException;

}

package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * 
 * @author mandar_shidhore
 *
 */
public class AbstractManager {
    
    /**
     * This method takes the class name , criteria for the object and returns the object. 
     * @param className class name
     * @param objectName objectName
     * @return DynamicExtensionBaseDomainObjectInterface
     */
    private DynamicExtensionBaseDomainObjectInterface getObjectByName(String className, String objectName) throws DynamicExtensionsSystemException
    {
        DynamicExtensionBaseDomainObjectInterface object = null;
        
        if (objectName == null || objectName.equals(""))
        {
            return object;
        }
        
        //Getting the instance of the default biz logic.
        DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
        List objectList = new ArrayList();
        
        try
        {
            objectList = defaultBizLogic.retrieve(className, "name", objectName);
        }
        catch (DAOException e)
        {
            throw new DynamicExtensionsSystemException(e.getMessage(), e);
        }

        if (objectList != null && objectList.size() > 0)
        {
            object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
        }

        return object;
    }

}
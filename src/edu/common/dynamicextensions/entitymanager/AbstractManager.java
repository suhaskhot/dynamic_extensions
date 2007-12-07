package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author mandar_shidhore
 *
 */
public abstract class AbstractManager {

    /**
     * This method takes the class name , criteria for the object and returns the object.
     * @param className class name
     * @param objectName objectName
     * @return DynamicExtensionBaseDomainObjectInterface
     */
    protected DynamicExtensionBaseDomainObjectInterface getObjectByName(String className, String objectName) throws DynamicExtensionsSystemException
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

    /**
     * Returns all instances in the whole system for a given type of the object
     * @return Collection of instances of given class
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    protected Collection getAllObjects(String objectName) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
        Collection objectList = new HashSet();

        try
        {
            objectList = bizLogic.retrieve(objectName);
            if (objectList == null)
            {
                objectList = new HashSet();
            }
        }
        catch (DAOException e)
        {
            throw new DynamicExtensionsSystemException(e.getMessage(), e);
        }
        return objectList;
    }
    /**
     * This method returns object for a given class name and identifer
     * @param objectName  name of the class of the object
     * @param identifier identifier of the object
     * @return  obejct
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    protected DynamicExtensionBaseDomainObject getObjectByIdentifier(String objectName,
            String identifier) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
        DynamicExtensionBaseDomainObject object;
        try
        {
            List objectList = bizLogic.retrieve(objectName, Constants.ID, identifier);

            if (objectList == null || objectList.size() == 0)
            {
                Logger.out.debug("Required Obejct not found: Object Name*" + objectName
                        + "*   identifier  *" + identifier + "*");
                System.out.println("Required Obejct not found: Object Name*" + objectName
                        + "*   identifier  *" + identifier + "*");
                throw new DynamicExtensionsApplicationException("OBJECT_NOT_FOUND");
            }

            object = (DynamicExtensionBaseDomainObject) objectList.get(0);
        }
        catch (DAOException e)
        {
            throw new DynamicExtensionsSystemException(e.getMessage(), e);
        }
        return object;
    }
}
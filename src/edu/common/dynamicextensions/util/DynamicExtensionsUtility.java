/**
 * 
 */

package edu.common.dynamicextensions.util;

import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author chetan_patil
 *
 */
public class DynamicExtensionsUtility
{

	/**
	 * This method fetches the Container instance from the Database given the corresponding Container Identifier.
	 * @param containerIdentifier The Idetifier of the Container.
	 * @return the ContainerInterface
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	public static ContainerInterface getContainerByIdentifier(String containerIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		ContainerInterface containerInterface = null;
		try
		{
			containerInterface = (ContainerInterface) getObjectByIdentifier(ContainerInterface.class.getName(), containerIdentifier);			
		}
		catch (DynamicExtensionsApplicationException dynamicExtensionsApplicationException)
		{
			throw new DynamicExtensionsApplicationException("CONTAINER_NOT_FOUND", dynamicExtensionsApplicationException);
		}
		return containerInterface;
	}

	/**
	 * This method returns object for a given class name and identifer 
	 * @param objectName  name of the class of the object
	 * @param identifier identifier of the object
	 * @return  obejct
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	private static Object getObjectByIdentifier(String objectName, String identifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		Object object = null;
		try
		{
			List objectList = bizLogic.retrieve(objectName, Constants.ID, identifier);

			if (objectList == null || objectList.isEmpty())
			{
				throw new DynamicExtensionsApplicationException("OBJECT_NOT_FOUND");
			}

			object = objectList.get(0);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return object;
	}
}

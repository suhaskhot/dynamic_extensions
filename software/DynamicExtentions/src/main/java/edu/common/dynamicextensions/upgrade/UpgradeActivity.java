
package edu.common.dynamicextensions.upgrade;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;

public class UpgradeActivity
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(UpgradeActivity.class);

	/**
	 * The main method.
	 * @param args the arguments
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DAOException the DAO exception
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, DAOException
	{
		EntityCache.getInstance();

		// 1. Upgrade Old Skip Logic.
		LOGGER.info("*******************************************************");
		LOGGER.info("Upgrading Old Skip Logic Started...");
		LOGGER.info("*******************************************************");
		UpgradeOldSkipLogic.main(null);
		LOGGER.info("*******************************************************");
		LOGGER.info("Upgrading Old Skip Logic completed...");
		LOGGER.info("*******************************************************");

		// 2. Upgrade PV References.
		LOGGER.info("*******************************************************");
		LOGGER.info("Upgrading Permissible Value Reference Started...");
		LOGGER.info("*******************************************************");
		CategoryPVReferenceUpgrade.main(null);
		LOGGER.info("*******************************************************");
		LOGGER.info("Upgrading Permissible Value Reference completed...");
		LOGGER.info("*******************************************************");
	}

}

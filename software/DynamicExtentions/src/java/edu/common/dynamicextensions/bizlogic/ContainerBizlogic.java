package edu.common.dynamicextensions.bizlogic;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.skiplogic.SkipLogic;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;


/**
 * @author kunal_kamble
 * This class has all the necessary methods related to the container.
 */
public class ContainerBizlogic
{
	private static final String FILTER_DISABLED_CONTROLS = "filterDisabledControls";

	/**
	 * This method returns the skip logic object for a given container object
	 * @param container
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public SkipLogic getSkipLogic(ContainerInterface container) throws DynamicExtensionsSystemException
	{
		HibernateDAO hibernateDAO = DynamicExtensionsUtility.getHibernateDAO();
		List<ColumnValueBean> valBeanList = new ArrayList<ColumnValueBean>();
		ColumnValueBean colValueBean = new ColumnValueBean("containerIdentifier",
				container.getId());
		valBeanList.add(colValueBean);

		List objects = null;
		try
		{
			objects = hibernateDAO.retrieve(SkipLogic.class.getName(), colValueBean);
			DynamicExtensionsUtility.closeDAO(hibernateDAO);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while retiring skip logic for container ",e);
		}
		SkipLogic skipLogic = null;
		if (objects != null && !objects.isEmpty())
		{
			skipLogic = (SkipLogic) objects.get(0);
		}
		return skipLogic;
	}


	/**
	 * This method used to fetch container with filtered control collection
	 * @param identifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public ContainerInterface getContainerById(Long identifier) throws DynamicExtensionsSystemException
	{
		ContainerInterface dyExtBsDmnObj = null;
		HibernateDAO hibernateDAO  = null;
		try
		{
			hibernateDAO  = DynamicExtensionsUtility.getHibernateDAO();
			hibernateDAO.enableFilter(FILTER_DISABLED_CONTROLS);
			List<ColumnValueBean> valBeanList = new ArrayList<ColumnValueBean>();
			ColumnValueBean colValueBean = new ColumnValueBean(DEConstants.OBJ_IDENTIFIER,
					identifier);
			valBeanList.add(colValueBean);


			List objects = hibernateDAO.retrieve(ContainerInterface.class.getName(), colValueBean);
			if (objects != null && !objects.isEmpty())
			{
				dyExtBsDmnObj = (ContainerInterface) objects.get(0);
			}
		}
	
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		finally
		{
			DynamicExtensionsUtility.closeDAO(hibernateDAO);
		}
		return dyExtBsDmnObj;

	}
}

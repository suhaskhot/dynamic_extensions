/**
 * <p>Title: BizLogicFactory Class>
 * <p>Description:  BizLogicFactory is a factory for DAO instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.common.dynamicextensions.bizlogic;

import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * BizLogicFactory is a factory for DAO instances of various domain objects.
 * @author gautam_shetty
 * Kapil: test
 */
public class BizLogicFactory
{

//	/**
//	 * Returns DAO instance according to the form bean type.
//	 * @param FORM_TYPE The form bean type.
//	 * @return An AbstractDAO object.
//	 */
//	public static AbstractBizLogic getBizLogic(int FORM_TYPE)
//	{
//		AbstractBizLogic abstractBizLogic;
//
//		if (FORM_TYPE == Constants.QUERY_INTERFACE_ID)
//		{
//			abstractBizLogic = new QueryBizLogic();
//		}
//		else
//		{
//			abstractBizLogic = new DefaultBizLogic(DynamicExtensionDAO.getInstance().getAppName());
//		}
//		return abstractBizLogic;
//	}

	/**
	 * Returns the BizLogic instance
	 * @return the DefaultBizLogic instance
	 */
	public static DefaultBizLogic getDefaultBizLogic()
	{
		return new DefaultBizLogic(DynamicExtensionDAO.getInstance().getAppName());
	}
}
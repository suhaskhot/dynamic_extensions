/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.util;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;



public class DynamicExtensionsBaseTestCase extends TestCase
{

    public DynamicExtensionsBaseTestCase()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public DynamicExtensionsBaseTestCase(String arg0)
    {
        super(arg0);
    }

    protected void setUp()
    {
        // TODO Auto-generated method stub
        Logger.out = org.apache.log4j.Logger.getLogger("dynamicExtensions.logger");
        edu.common.dynamicextensions.util.global.Variables.containerFlag = false;
    }
    
    protected void tearDown()
    {
        // TODO Auto-generated method stub
        //super.tearDown();
        Variables.hibernateCfgFileName = "hibernate.cfg.xml";
    }
    
    protected void executeQuery(String query)
    {
//      Checking whether the data table is created properly or not.
        Connection conn = null;
        try
        {
            conn = DBUtil.getConnection();
        }
        catch (HibernateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        java.sql.PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(query);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
        
    }

    
   
}

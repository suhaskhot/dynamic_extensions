
package edu.wustl.cab2b.server.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;

/**
 * Test class to test {@link SQLQueryUtil}
 * To Run this test case a started server even though it is JUnit
 * @author Chandrakant Talele
 */
public class SQLQueryUtilTest extends DynamicExtensionsBaseTestCase
{

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp()
	{
		String createTableSQL = "create table TEST_TABLE (ID BIGINT(38) NOT NULL, NAME VARCHAR(10) NULL,PRIMARY KEY (ID))";// insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
		Connection con = TestConnectionUtil.getConnection();
		SQLQueryUtil.executeUpdate(createTableSQL, con);
	}

	/**
	 * This method tests functionality provided by {@link SQLQueryUtil}
	 */
	public void testSQLQueryUtil()
	{
		String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
		int res = -1;
		Connection con = TestConnectionUtil.getConnection();
		res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

		assertEquals(4, res);

		String selectSQL = "SELECT id,name FROM test_table WHERE name like ?";
		int recordCount = 0;
		String[][] rs = null;

		rs = SQLQueryUtil.executeQuery(selectSQL, con, "%A%");

		for (int i = 0; i < rs.length; i++)
		{

			recordCount++;
			Long id = Long.parseLong(rs[i][0]);
			String name = rs[i][1];
			assertTrue(id != 4);
			assertTrue(name.indexOf('A') != -1);
		}

		assertTrue(recordCount == 3);
	}

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParams1()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, new BigDecimal(1));

        for (int i = 0; i < rs.length; i++)
        {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf('A') != -1);
        }

        assertTrue(recordCount == 1);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParams2()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, new Long(1));

        for (int i = 0; i < rs.length; i++)
        {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf('A') != -1);
        }

        assertTrue(recordCount == 1);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParams3()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, new Integer(1));

        for (int i = 0; i < rs.length; i++)
        {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf('A') != -1);
        }

        assertTrue(recordCount == 1);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParams4()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, new Float(1));

        for (int i = 0; i < rs.length; i++)
        {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf('A') != -1);
        }

        assertTrue(recordCount == 1);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParams5()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con, new Short((short) 1));

        for (int i = 0; i < rs.length; i++)
        {

            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = rs[i][1];
            assertTrue(id != 4);
            assertTrue(name.indexOf('A') != -1);
        }

        assertTrue(recordCount == 1);
    }

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testSQLQueryUtilParamsError()
    {
        String insertDataSQL = "insert into TEST_TABLE (ID,NAME) values (1,'ABC'),(2,'GAAL'),(3,'CLASS'),(4,'FOO')";
        int res = -1;
        Connection con = TestConnectionUtil.getConnection();
        res = SQLQueryUtil.executeUpdate(insertDataSQL, con);

        assertEquals(4, res);

        String selectSQL = "SELECT1 id,name FROM test_table WHERE ID = ?";
        int recordCount = 0;
        String[][] rs = null;

        try
        {
            rs = SQLQueryUtil.executeQuery(selectSQL, con, new Short((short) 1));
            fail("Must throw SQL Exception");
        }
        catch (edu.wustl.cab2b.common.exception.RuntimeException e)
        {
            assertEquals(ErrorCodeConstants.DB_0003, e.getErrorCode());
        }

    }

	/**
	 * This method tests functionality provided by {@link SQLQueryUtil}
	 */
	public void testExecuteQueryForException2()
	{
		String selectSQL = "UPDATE_TABLE1234 WHERE NEXT_ASSOCIATION_ID > ?";
		PreparedStatement pStmt = null;
		Connection con = TestConnectionUtil.getConnection();
		try
		{
			pStmt = con.prepareStatement(selectSQL);
			pStmt.setLong(1, 1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			assertTrue("Got SQLException while creating a PreparedStatement", false);
		}
		try
		{
			SQLQueryUtil.executeUpdate(selectSQL,con);
			assertTrue("Expected SQLException wrapped in RuntimeException, but it was not thrown",
					false);
		}
		catch (edu.wustl.cab2b.common.exception.RuntimeException e)
		{
			assertEquals(ErrorCodeConstants.DB_0004, e.getErrorCode());
		}
	}

    /**
     * This method tests functionality provided by {@link SQLQueryUtil}
     */
    public void testExecuteQueryForException()
    {
        String selectSQL = "SELECT id from ID_TABLE1234 WHERE NEXT_ASSOCIATION_ID > ?";
        PreparedStatement pStmt = null;
        Connection con = TestConnectionUtil.getConnection();
        try
        {
            pStmt = con.prepareStatement(selectSQL);
            pStmt.setLong(1, 1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            assertTrue("Got SQLException while creating a PreparedStatement", false);
        }
        try
        {
            SQLQueryUtil.executeQuery(pStmt);
            assertTrue("Expected SQLException wrapped in RuntimeException, but it was not thrown",
                    false);
        }
        catch (edu.wustl.cab2b.common.exception.RuntimeException e)
        {
            assertEquals(ErrorCodeConstants.DB_0003, e.getErrorCode());
        }
    }

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown()
	{
		Connection con = TestConnectionUtil.getConnection();
		SQLQueryUtil.executeUpdate("DROP table TEST_TABLE", con);
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}
}

/**
 *
 */
package edu.wustl.cab2b.server.util;

import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

/**
 * @author gaurav_sawant
 *
 */
public class TestServerProperties extends DynamicExtensionsBaseTestCase
{

    public void testGetDatabaseIp()
    {
        String expectedIp = "10.88.199.105";
        String actualIp = ServerProperties.getDatabaseIp();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatabaseLoader()
    {
        String expectedIp = "test";
        String actualIp = ServerProperties.getDatabaseLoader();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatabaseName()
    {
        String expectedIp = "dyext";
        String actualIp = ServerProperties.getDatabaseName();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatabasePassword()
    {
        String expectedIp = "root";
        String actualIp = ServerProperties.getDatabasePassword();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatabasePort()
    {
        String expectedIp = "3306";
        String actualIp = ServerProperties.getDatabasePort();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatabaseUser()
    {
        String expectedIp = "root";
        String actualIp = ServerProperties.getDatabaseUser();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetDatasourceName()
    {
        String expectedIp = "test";
        String actualIp = ServerProperties.getDatasourceName();
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetGridCert()
    {
        String gridType = "test";
        String expectedIp = "test";
        String actualIp = ServerProperties.getGridCert(gridType);
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }

    public void testGetGridKey()
    {
        String gridType = "test";
        String expectedIp = "test";
        String actualIp = ServerProperties.getGridKey(gridType);
        assertEquals("Testing database Ip ",expectedIp, actualIp);
    }


}

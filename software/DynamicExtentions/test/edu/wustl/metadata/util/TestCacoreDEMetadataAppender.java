package edu.wustl.metadata.util;

import java.io.File;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestCacoreDEMetadataAppender extends DynamicExtensionsBaseTestCase
{

    @Override
    protected void setUp()
    {
        super.setUp();
    }

    @Override
    protected void tearDown()
    {
        super.tearDown();
    }


    public final void testCacoreDEMetadataAppender()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWeb.cfg.xml";
        String ormCfgFileName = XML_FILE_PATH + File.separator
                + "Test_orm1.cfg.xml";
        String[] args = {deCfgFileName,ormCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
        } catch (DynamicExtensionsApplicationException e)
        {
            e.printStackTrace();
            fail("unable to append caCore to de metadata" + e.getMessage());
        }
    }

    public final void testCacoreDEMetadataAppender2()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWeb2.cfg.xml";
        String ormCfgFileName = XML_FILE_PATH + File.separator
                + "Test_orm1.cfg.xml";
        String[] args = {deCfgFileName,ormCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
        } catch (DynamicExtensionsApplicationException e)
        {
            e.printStackTrace();
            fail("unable to append caCore to de metadata" + e.getMessage());
        }
    }

    public final void testCacoreDEMetadataAppenderInvalidArgs()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWeb.cfg.xml";
        String[] args = {deCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
            fail("Expected DynamicExtensionsApplicationException.");
        } catch (DynamicExtensionsApplicationException e)
        {
            assertTrue("Expected DEAppException Error "+ e.getMessage(), true);
            assertEquals("Please specify paths for hibernate configuration files!", e.getMessage());
        }
    }

    public final void testIncorrectCacoreDEMetadataAppender1()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWeb3.cfg.xml";
        String ormCfgFileName = XML_FILE_PATH + File.separator
                + "Test_orm1.cfg.xml";
        String[] args = {deCfgFileName,ormCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
            fail("Expected DynamicExtensionsApplicationException.");
        } catch (DynamicExtensionsApplicationException e)
        {
            e.printStackTrace();
            assertTrue("Expected DEAppException Error "+ e.getMessage(), true);
        }
    }

    public final void testIncorrectCacoreDEMetadataAppender2()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWeb4.cfg.xml";
        String ormCfgFileName = XML_FILE_PATH + File.separator
                + "Test_orm1.cfg.xml";
        String[] args = {deCfgFileName,ormCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
            fail("Expected DynamicExtensionsApplicationException.");
        } catch (Exception e)
        {
            e.printStackTrace();
            assertTrue("Expected NullPointer Exception "+ e.getMessage(), true);
        }
    }

    public final void testIncorrectCacoreDEMetadataAppender3()
    {
        String deCfgFileName = XML_FILE_PATH + File.separator
                + "TestDynamicExtensionsHibernateWebNofile.cfg.xml";
        String ormCfgFileName = XML_FILE_PATH + File.separator
                + "Test_orm1.cfg.xml";
        String[] args = {deCfgFileName,ormCfgFileName};
        try
        {
            CacoreDEMetadataAppender.main(args);
            fail("Expected DynamicExtensionsApplicationException.");
        } catch (DynamicExtensionsApplicationException e)
        {
            e.printStackTrace();
            assertTrue("Expected DEAppException Error "+ e.getMessage(), true);
        }
    }



}

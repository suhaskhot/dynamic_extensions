/**
 *
 */
package edu.wustl.metadata.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class TestEntityGroupName.
 *
 * @author gaurav_sawant
 */
public class TestEntityGroupName extends DynamicExtensionsBaseTestCase
{

    /**
     * Instantiates a new test entity group name.
     */
    public TestEntityGroupName()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new test entity group name.
     *
     * @param arg0 the arg0
     */
    public TestEntityGroupName(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#tearDown()
     */
    @Override
    protected void tearDown()
    {
        super.tearDown();

        File filePath =  getFileFolderPath();
        String entityGroupFileName = getEntityGroupFileName();
        File fileName = new File(filePath,entityGroupFileName);
        if (fileName.exists())
        {
            fileName.delete();
        }
    }

    /**
     * Setup method
     *
     * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#setUp()
     */
    @Override
    protected void setUp()
    {
        super.setUp();

        File filePath =  getFileFolderPath();
        filePath.mkdirs();
        String entityGroupFileName = getEntityGroupFileName();
        File fileName = new File(filePath,entityGroupFileName);
        if (fileName.exists())
        {
            fileName.delete();
        }

    }

    /**
     * Test entity group name create file.
     */
    public void testEntityGroupNameCreateFile()
    {
        File filePath =  getFileFolderPath();
        String entityGroupFileName = getEntityGroupFileName();

        String[] arguments = {filePath.toString(),"",""};
        try
        {
            EntityGroupName.main(arguments);
            File fileName = new File(filePath,entityGroupFileName);
            assertTrue("File should exist.", fileName.exists());
        } catch (Exception e)
        {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test entity group name with exclude opt.
     */
    public void testEntityGroupNameWithExcludeOpt()
    {
        File filePath =  getFileFolderPath();
        String entityGroupFileName = getEntityGroupFileName();
        File oldFile = new File(filePath,entityGroupFileName);
        if (oldFile.exists())
        {
            oldFile.delete();
        }

        String[] arguments = {filePath.toString(),"test,",""};
        FileInputStream fis = null;
        try
        {
            EntityGroupName.main(arguments);
            File fileName = new File(filePath,entityGroupFileName);
            assertTrue("File should exist.", fileName.exists());
            fis = new FileInputStream(fileName);
            Properties prop = new Properties();
            prop.load(fis);
            assertFalse("File should not be empty!",prop.isEmpty());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }    }

    /**
     * Test entity group name with include opt.
     */
    public void testEntityGroupNameWithIncludeOpt()
    {
        File filePath =  getFileFolderPath();
        String entityGroupFileName = getEntityGroupFileName();

        String[] arguments = {filePath.toString(),"","test,"};
        FileInputStream fis = null;
        try
        {
            EntityGroupName.main(arguments);
            File fileName = new File(filePath,entityGroupFileName);
            assertTrue("File should exist.", fileName.exists());
            fis = new FileInputStream(fileName);
            Properties prop = new Properties();
            prop.load(fis);
            assertFalse("File should not be empty!",prop.isEmpty());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (Exception e)
        {
            fail(e.getMessage());
            e.printStackTrace();
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Test entity group name valid file contents.
     */
    public void testEntityGroupNameValidFileContents()
    {
        File filePath = getFileFolderPath();
        String entityGroupFileName = getEntityGroupFileName();

        String[] arguments = { filePath.toString(), "", "" };
        try
        {
            EntityGroupName.main(arguments);
            File fileName = new File(filePath, entityGroupFileName);
            assertTrue("File should exist.", fileName.exists());
            FileInputStream fis = new FileInputStream(fileName);
            Properties prop = new Properties();
            prop.load(fis);
            assertFalse("File should not be empty!", prop.isEmpty());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    /**
     * Test entity group name invalid path file.
     */
    public void testEntityGroupNameInvalidPathFile()
    {
        try
        {
            File filePath =  File.createTempFile("pre", "post");
            String entityGroupFileName = getEntityGroupFileName();

            String[] arguments = {filePath.toString(),"",""};
            try
            {
                EntityGroupName.main(arguments);
                fail("File should not exists.");
            } catch (Exception e)
            {
                assertTrue(e.getMessage(),true);
            }
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }


    /* -----------------------------------------------------------------------*/
    /**
     * Gets the entity group file name.
     *
     * @return the entity group file name
     */
    private String getEntityGroupFileName()
    {
        return "EntityGroup.properties";
    }

    /**
     * Gets the file folder path.
     *
     * @return the file folder path
     */
    private File getFileFolderPath()
    {
        String jbossHomeFolderPath = System.getProperty("jboss.home.dir");
        if (jbossHomeFolderPath == null)
        {
            jbossHomeFolderPath = ".";
        }
        return new File(jbossHomeFolderPath);
    }

}

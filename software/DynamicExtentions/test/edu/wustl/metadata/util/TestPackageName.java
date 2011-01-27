/**
 *
 */
package edu.wustl.metadata.util;

import java.io.File;

import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;


/**
 * @author Gaurav_mehta
 */
public class TestPackageName extends DynamicExtensionsBaseTestCase
{

    @Override
    protected void setUp()
    {
        // TODO Auto-generated method stub
        super.setUp();

        File filePath =  getFileFolderPath();
        filePath.mkdirs();
        String packageFileName = getPackageFileName();
        File fileName = new File(filePath,packageFileName);
        if (fileName.exists())
        {
            fileName.delete();
        }
    }

    @Override
    protected void tearDown()
    {
        // TODO Auto-generated method stub
        super.tearDown();
        File filePath =  getFileFolderPath();
        String packageFileName = getPackageFileName();
        File fileName = new File(filePath,packageFileName);
        if (fileName.exists())
        {
            fileName.delete();
        }
    }

	public void testPackageName()
	{
		String[] arguments = {"","CPUML/TestModels/TestModel_withTags/edited/test.xmi"};
		PackageName.main(arguments);

		String jbossHomeFolderPath = System.getProperty("jboss.home.dir");
		if (jbossHomeFolderPath == null)
        {
            jbossHomeFolderPath = "";
        }
		String filePath =  jbossHomeFolderPath + File.separator + "Package.properties";

		File file = new File(filePath);
		if(file.exists())
		{
			assertTrue("File Package.properties was created",true);
		}
		else
		{
			fail("File Package.properties was not created");
		}
	}

    public void testGetLogicalPackageName1()
    {
        String delimiter = ".";
        String expectedLogicalName = "edu.annotations";

        StringBuilder actualPackageName = new StringBuilder();
        actualPackageName.append(PackageName.LOGICAL_VIEW);
        actualPackageName.append(delimiter);
        actualPackageName.append(PackageName.LOGICAL_MODEL);
        actualPackageName.append(delimiter);
        actualPackageName.append(expectedLogicalName);
        String logicalString = PackageName.getLogicalPackageName(actualPackageName.toString(), delimiter);

        assertEquals(expectedLogicalName, logicalString);
    }

    public void testGetLogicalPackageName2()
    {
        String delimiter = ".";
        String expectedLogicalName = "";

        StringBuilder actualPackageName = new StringBuilder();
        actualPackageName.append(PackageName.LOGICAL_VIEW);
        actualPackageName.append(delimiter);
        actualPackageName.append(PackageName.LOGICAL_MODEL);
        actualPackageName.append(delimiter);
        actualPackageName.append(expectedLogicalName);
        String logicalString = PackageName.getLogicalPackageName(actualPackageName.toString(), delimiter);

        assertEquals(expectedLogicalName, logicalString);
    }

    /* -----------------------------------------------------------------------*/
    /**
     * Gets the entity group file name.
     *
     * @return the entity group file name
     */
    private String getPackageFileName()
    {
        return "Package.properties";
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
            jbossHomeFolderPath = "";
        }
        return new File(jbossHomeFolderPath);
    }
}

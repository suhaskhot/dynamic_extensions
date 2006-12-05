/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright://TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.AttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domain.FileAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FileExtension;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

public class TestEntityManager extends DynamicExtensionsBaseTestCase
{

    /**
     * 
     */
    public TestEntityManager()
    {
        super();
        //TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 name
     */
    public TestEntityManager(String arg0)
    {
        super(arg0);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#setUp()
     */
    protected void setUp()
    {
        super.setUp();
    }

    /**
     * @see edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase#tearDown()
     */
    protected void tearDown()
    {
        super.tearDown();
    }
    
    /**
     * 
     */

    public void testEditEntity()
    {
        Entity entity = new Entity();
        entity.setName("Stock Quote");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();

        try
        {
            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(entity);

            //Edit entity
            AttributeInterface floatAtribute = DomainObjectFactory
                    .getInstance().createFloatAttribute();
            floatAtribute.setName("Price");

            savedEntity.addAbstractAttribute(floatAtribute);
            EntityInterface editedEntity = entityManagerInterface
                    .persistEntity(savedEntity);

            Map dataValue = new HashMap();
            dataValue.put(floatAtribute, "15.90");
            entityManagerInterface.insertData(editedEntity, dataValue);

            //Edit entity
            AttributeInterface floatAtribute1 = DomainObjectFactory
                    .getInstance().createFloatAttribute();
            floatAtribute.setName("NewPrice");
            editedEntity.addAbstractAttribute(floatAtribute1);

            java.sql.ResultSetMetaData metadata = executeQueryForMetadata("select * from "
                    + editedEntity.getTableProperties().getName());
            assertEquals(metadata.getColumnCount(), 2);

            EntityInterface newEditedEntity = entityManagerInterface
                    .persistEntity(editedEntity);
            dataValue.put(floatAtribute1, "16.90");
            entityManagerInterface.insertData(newEditedEntity, dataValue);

            metadata = executeQueryForMetadata("select * from "
                    + editedEntity.getTableProperties().getName());
            assertEquals(metadata.getColumnCount(), 3);

        }
        catch (DynamicExtensionsSystemException e)
        {

            e.printStackTrace();
            fail();
        }
        catch (DynamicExtensionsApplicationException e)
        {
            e.printStackTrace();
            fail();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * 
     */

    public void testEditEntityRemoveAttribute()
    {
        Entity entity = new Entity();
        entity.setName("Stock Quote");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();

        try
        {

            //Edit entity
            AttributeInterface floatAtribute = DomainObjectFactory
                    .getInstance().createFloatAttribute();
            floatAtribute.setName("Price");
            AttributeInterface commentsAttributes = DomainObjectFactory
                    .getInstance().createStringAttribute();
            commentsAttributes.setName("coomments");

            entity.addAbstractAttribute(floatAtribute);
            entity.addAbstractAttribute(commentsAttributes);

            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(entity);

            Map dataValue = new HashMap();
            dataValue.put(floatAtribute, "15.90");
            entityManagerInterface.insertData(savedEntity, dataValue);

            java.sql.ResultSetMetaData metadata = executeQueryForMetadata("select * from "
                    + savedEntity.getTableProperties().getName());
            assertEquals(3, metadata.getColumnCount());

            AttributeInterface savedFloatAtribute = null;

            Iterator itr = savedEntity.getAttributeCollection().iterator();
            while (itr.hasNext())
            {
                savedFloatAtribute = (AttributeInterface) itr.next();
                System.out.println("id is: " + savedFloatAtribute.getId());

            }

            //remove attribute
            savedEntity.removeAbstractAttribute(savedFloatAtribute);

            EntityInterface editedEntity = entityManagerInterface
                    .persistEntity(savedEntity);

            metadata = executeQueryForMetadata("select * from "
                    + editedEntity.getTableProperties().getName());
            assertEquals(2, metadata.getColumnCount());
        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();

            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * 
     */

    public void testCreateEnityWithNullAttribute()
    {
        try
        {
            Entity entity = new Entity();
            entity.addAbstractAttribute(null);
            EntityManagerInterface entityManagerInterface = EntityManager
                    .getInstance();
            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(entity);
            fail();
        }
        catch (DynamicExtensionsApplicationException e)
        {
            
            Logger.out.debug("Exception should occur coz the attribute is null.. validation should fail");
        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * This method test for the created date null for an entity.
     */

    public void testCreateEntityNull()
    {
        EntityInterface entity = new Entity();
        entity.setName("test");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        entity.setCreatedDate(null);

        try
        {
            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(entity);
            assertEquals(savedEntity.getName(), entity.getName());

            String tableName = entity.getTableProperties().getName();
            String query = "Select * from " + tableName;
            executeQueryForMetadata(query);

            assertNotNull(savedEntity.getCreatedDate());
        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * 
     */
    public void testEditAttributeTypeChange()
    {
        Entity entity;
        try
        {
            entity = (Entity) new Entity();
            AttributeInterface ssn = DomainObjectFactory.getInstance()
                    .createIntegerAttribute();
            ssn.setName("SSN of participant");
            entity.addAbstractAttribute(ssn);
            entity.setName("test");
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);

            ResultSetMetaData metaData = executeQueryForMetadata("select * from "
                    + entity.getTableProperties().getName());

            assertEquals(metaData.getColumnType(2), Types.NUMERIC);

            AttributeTypeInformationInterface dateAttributeType = new StringAttributeTypeInformation();
            ssn.setAttributeTypeInformation(dateAttributeType);

            entity = (Entity) EntityManager.getInstance().persistEntity(entity);

            metaData = executeQueryForMetadata("select * from "
                    + entity.getTableProperties().getName());

            assertEquals(metaData.getColumnType(2), Types.VARCHAR);

        }
        catch (DynamicExtensionsApplicationException e)
        {
            // TODO Auto-generated catch block
            fail();
            e.printStackTrace();
        }
        catch (DynamicExtensionsSystemException e)
        {
            // TODO Auto-generated catch block
            fail();
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            fail();
            e.printStackTrace();
        }

    }

    /**
     * This test case tries to modify data type of the attribute,when data is present for that column.
     * for oracle it should throw exception.
     * for mysql  it works.  
     */
    public void testEditAttributeTypeChangeDataExists()
    {
        Entity entity = null;
        try
        {
            entity = (Entity) new Entity();
            AttributeInterface ssn = DomainObjectFactory.getInstance()
                    .createIntegerAttribute();
            ssn.setName("SSN of participant");
            entity.addAbstractAttribute(ssn);
            entity.setName("test");
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);

            Map dataValue = new HashMap();
            dataValue.put(ssn, 101202);
            EntityManager.getInstance().insertData(entity, dataValue);

            AttributeTypeInformationInterface dateAttributeType = new StringAttributeTypeInformation();
            ssn.setAttributeTypeInformation(dateAttributeType);

            entity = (Entity) EntityManager.getInstance().persistEntity(entity);

        }
        catch (DynamicExtensionsApplicationException e)
        {
            // TODO Auto-generated catch block
            fail();
            e.printStackTrace();
        }
        catch (DynamicExtensionsSystemException e)
        {

            e.printStackTrace();
            /*   assertTrue("Data exists so can't modify its data type",true); 
             ResultSetMetaData metaData = executeQueryForMetadata("select * from "
             + entity.getTableProperties().getName());
             
             try
             {
             assertEquals(metaData.getColumnType(2),Types.NUMERIC);
             }
             catch (SQLException e1)
             {
             fail();
             e1.printStackTrace();
             }
             e.printStackTrace();*/
        }

    }

    /**
     * This method tests the creation of entity group
     */
    public void testCreateEntityGroup()
    {
        try
        {
            EntityGroup entityGroup = (EntityGroup) new MockEntityManager()
                    .initializeEntityGroup();
            
            entityGroup = (EntityGroup) EntityManager.getInstance()
                    .persistEntityGroup(entityGroup);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            e.printStackTrace();
            fail("Exception occured");
        }
    }

    /**
     * This method tests GetEntityGroupByName method.
     *
     */
    public void testGetEntityGroupByName()
    {
        try
        {
            EntityManagerInterface entityManagerInterface = EntityManager
                    .getInstance();
            EntityGroup entityGroup = (EntityGroup) new MockEntityManager()
                    .initializeEntityGroup();
            entityManagerInterface.persistEntityGroup(entityGroup);

            EntityGroupInterface entityGroupInterface = entityManagerInterface
                    .getEntityGroupByShortName(entityGroup.getShortName());
            assertNotNull(entityGroupInterface);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            e.printStackTrace();
            fail("Exception occured");
        }
    }

    /**
     * This method tests GetEntitiesByConceptCode method.
     *
     */
    public void testGetEntitiesByConceptCode()
    {
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            SemanticPropertyInterface semanticPropertyInterface = (SemanticPropertyInterface) new MockEntityManager()
                    .initializeSemanticProperty();
            entity.addSemanticProperty(semanticPropertyInterface);

            entity = (Entity) entityManagerInterface.persistEntity(entity);

            Collection entityCollection = (Collection) EntityManager
                    .getInstance().getEntitiesByConceptCode(
                            semanticPropertyInterface.getConceptCode());
            assertTrue(entityCollection != null && entityCollection.size() > 0);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
    }

    /**
     * This method tests GetEntityByName method.
     *
     */
    public void testGetEntityByName()
    {
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            EntityInterface entityInterface = (EntityInterface) entityManagerInterface
                    .getEntityByName(entity.getName());
            assertNotNull(entityInterface);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
    }

    /**
     * This method tests GetAttribute method.
     *
     */
    public void testGetAttribute()
    {
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            AttributeInterface attributeInterface = (AttributeInterface) entityManagerInterface
                    .getAttribute("Employee", "gender");
            assertNotNull(attributeInterface);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * Test case to check the behaviour when first entity is saved with a collection attribute and then the that 
     * attribute is made non-collection. Expected behavior is that after editing the attribute in
     * such a way the column for that attribute should get added to the data table. This column was not present in
     * earlier scenario when the attribue was a collection attribute. 
     */
    public void testEditEntityWithCollectionAttribute()
    {
        try
        {
            EntityManagerInterface entityManager = EntityManager.getInstance();
            DomainObjectFactory factory = DomainObjectFactory.getInstance();

            // create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory
                    .createStringAttribute();
            userNameAttribute.setName("user name");
            userNameAttribute.setIsCollection(true);
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            user = (Entity) EntityManager.getInstance().persistEntity(user);

            Entity newEntity = (Entity) EntityManager.getInstance()
                    .getEntityByIdentifier(user.getId().toString());
            assertEquals(user.getName(), newEntity.getName());

            String tableName = newEntity.getTableProperties().getName();
            assertTrue(isTablePresent(tableName));
            ResultSetMetaData metaData = executeQueryForMetadata("select * from "
                    + tableName);
            assertEquals(1, metaData.getColumnCount());

            userNameAttribute = (AttributeInterface) newEntity
                    .getAttributeByIdentifier(userNameAttribute.getId());
            userNameAttribute.setIsCollection(false);

            newEntity = (Entity) EntityManager.getInstance().persistEntity(
                    newEntity);

            tableName = newEntity.getTableProperties().getName();
            assertTrue(isTablePresent(tableName));
            metaData = executeQueryForMetadata("select * from " + tableName);
            assertEquals(2, metaData.getColumnCount());

            userNameAttribute = (AttributeInterface) newEntity
                    .getAttributeByIdentifier(userNameAttribute.getId());
            userNameAttribute.setIsCollection(true);

            newEntity = (Entity) EntityManager.getInstance().persistEntity(
                    newEntity);

            tableName = newEntity.getTableProperties().getName();
            assertTrue(isTablePresent(tableName));
            metaData = executeQueryForMetadata("select * from " + tableName);
            assertEquals(1, metaData.getColumnCount());

        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            e.printStackTrace();
            fail("Exception occured");
        }

    }

    /**
     * This method tests GetRecordById method for the condition where record and entity does exists
     */

    public void testDeleteRecordById()
    {

        try
        {
            EntityManagerInterface entityManagerInterface = EntityManager
                    .getInstance();

            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            Entity newEntity = (Entity) entityManagerInterface
                    .getEntityByIdentifier(entity.getId().toString());
            Map dataValue = new HashMap();

            Iterator attrIterator = newEntity.getAttributeCollection()
                    .iterator();
            int i = 0;
            while (attrIterator.hasNext())
            {
                AttributeInterface attribute = (AttributeInterface) attrIterator
                        .next();
                AttributeTypeInformationInterface typeInfo = attribute
                        .getAttributeTypeInformation();

                if (typeInfo instanceof StringAttributeTypeInformation)
                {
                    dataValue.put(attribute, "temp" + i);
                }
                else if (attribute instanceof DateAttributeTypeInformation)
                {
                    dataValue.put(attribute, "11-01-2006");
                }

                i++;
            }

            entityManagerInterface.insertData(newEntity, dataValue);
            ResultSet resultSet = executeQuery("select count(*) from "
                    + newEntity.getTableProperties().getName());

            resultSet.next();

            assertEquals(1, resultSet.getInt(1));

            assertEquals("Employee", entity.getName());
            boolean isRecordDeleted = EntityManager.getInstance().deleteRecord(
                    entity, new Long(1));
            assertTrue(isRecordDeleted);
            resultSet = executeQuery("select count(*) from "
                    + newEntity.getTableProperties().getName());

            resultSet.next();

            assertEquals(0, resultSet.getInt(1));

        }
        catch (DynamicExtensionsSystemException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
        catch (SQLException e)
        {
            //          TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * This method test for inserting data for a multi select attribute
     */
    public void testInsertDataWithMultiSelectAttribute()
    {
        Entity entity = new Entity();
        entity.setName("Stock Quote");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

            //Edit entity
            AttributeInterface floatAtribute = factory.createFloatAttribute();
            floatAtribute.setName("Price");

            AttributeInterface commentsAttributes = factory
                    .createStringAttribute();
            commentsAttributes.setName("comments");
            commentsAttributes.setIsCollection(true);

            entity.addAbstractAttribute(floatAtribute);
            entity.addAbstractAttribute(commentsAttributes);

            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(entity);

            Map dataValue = new HashMap();
            dataValue.put(floatAtribute, "15.90");
            List<String> commentsValues = new ArrayList<String>();
            commentsValues.add("comments1");
            commentsValues.add("comments2");
            commentsValues.add("comments3");
            commentsValues.add("comments4");

            dataValue.put(commentsAttributes, commentsValues);

            entityManagerInterface.insertData(savedEntity, dataValue);

            ResultSet resultSet = executeQuery("select * from "
                    + savedEntity.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
            ResultSetMetaData metadata = resultSet.getMetaData();
            assertEquals(2, metadata.getColumnCount());

            boolean isRecordDeleted = entityManagerInterface.deleteRecord(
                    savedEntity, new Long(1));
            assertTrue(isRecordDeleted);

            resultSet = executeQuery("select count(*) from "
                    + savedEntity.getTableProperties().getName());
            resultSet.next();
            assertEquals(0, resultSet.getInt(1));

        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();

            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * This method test for updating record for an entity.
     */
    public void testEditRecord()
    {
        Entity study = new Entity();
        study.setName("Study");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

            AttributeInterface name = factory.createStringAttribute();
            name.setName("Study name");

            AttributeInterface userNames = factory.createStringAttribute();
            userNames.setName("users");

            AttributeInterface studyDate = factory.createDateAttribute();
            studyDate.setName("Date");

            study.addAbstractAttribute(name);
            study.addAbstractAttribute(userNames);
            study.addAbstractAttribute(studyDate);

            EntityInterface savedStudy = entityManagerInterface
                    .persistEntity(study);

            Map dataValue = new HashMap();

            dataValue.put(name, "Java Study");
            dataValue.put(userNames, "a");
            dataValue.put(studyDate, "11-20-2006");

            Long recordId = entityManagerInterface.insertData(savedStudy,
                    dataValue);

            Map map = entityManagerInterface
                    .getRecordById(savedStudy, recordId);

            String userName = (String) map.get("users");
            assertEquals("a", userName);

            dataValue.put(userNames, "b");
            dataValue.put(studyDate, "12-20-2006");

            entityManagerInterface.editData(savedStudy, dataValue, recordId);

            map = entityManagerInterface.getRecordById(savedStudy, recordId);
            userName = (String) map.get("users");
            assertEquals("b", userName);
            assertEquals("12-20-2006", (String) map.get("Date"));

        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();

            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * This method test for updating data for a multi select attribute
     */
    public void testEditRecordWithMultiselectAttrubteUpdate()
    {
        Entity study = new Entity();
        study.setName("Study");
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

            AttributeInterface name = factory.createStringAttribute();
            name.setName("Study name");

            AttributeInterface studyDate = factory.createDateAttribute();
            studyDate.setName("Date");

            AttributeInterface userNames = factory.createStringAttribute();
            userNames.setName("users");
            userNames.setIsCollection(true);

            study.addAttribute(name);
            study.addAttribute(userNames);
            study.addAttribute(studyDate);
            EntityInterface savedStudy = entityManagerInterface
                    .persistEntity(study);

            Map dataValue = new HashMap();
            List<String> userNameList = new ArrayList<String>();
            userNameList.add("a");
            userNameList.add("b");
            userNameList.add("c");

            dataValue.put(name, "Java Study");
            dataValue.put(userNames, userNameList);
            dataValue.put(studyDate, "11-20-2006");

            Long recordId = entityManagerInterface.insertData(savedStudy,
                    dataValue);

            Map map = entityManagerInterface
                    .getRecordById(savedStudy, recordId);

            int noOfUsers = ((List) map.get("users")).size();
            assertEquals(3, noOfUsers);
            System.out.println(map);

            dataValue.clear();
            userNameList.clear();
            userNameList.add("d");

            dataValue.put(userNames, userNameList);
            dataValue.put(studyDate, "12-20-2006");

            entityManagerInterface.editData(savedStudy, dataValue, recordId);

            map = entityManagerInterface.getRecordById(savedStudy, recordId);
            noOfUsers = ((List) map.get("users")).size();
            assertEquals(1, noOfUsers);
            assertEquals("12-20-2006", (String) map.get("Date"));

            System.out.println(map);

        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();

            Logger.out.debug(e.getStackTrace());
        }

    }
    
    /**
     * This method edits an existing attribute to a file type attribute.
     */
    public void testEditFileAttribute()
    {
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");
		AttributeInterface resume = factory.createStringAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		
		try
		
		
		{  
			
			ResultSet rs = executeQuery("select count(*) from dyextn_file_extensions");
			rs.next();
			int beforeCount = rs.getInt(1);
			System.out.println(beforeCount);
			
			
			
			// save the entity
			user = entityManager.persistEntity(user);
			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			
			
			//Edit attribute: change attribute type to file attrbiute type.
			Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

			FileExtension txtExtn = new FileExtension();
			txtExtn.setFileExtension("txt");
			allowedExtn.add(txtExtn);
			
			FileExtension pdfExtn = new FileExtension();
			pdfExtn.setFileExtension("pdf");
			allowedExtn.add(pdfExtn);
			
			
			allowedExtn.add(txtExtn);
			allowedExtn.add(pdfExtn);
			
			AttributeTypeInformation fileTypeInformation = factory.createFileAttributeTypeInformation();
			
			((FileAttributeTypeInformation) fileTypeInformation).setMaxFileSize(20F);
			((FileAttributeTypeInformation) fileTypeInformation).setFileExtensionCollection(allowedExtn);
			
			resume.setAttributeTypeInformation(fileTypeInformation);
			
			
			
			user = entityManager.persistEntity(user);
			
			DBUtil.closeConnection();
			//DBUtil.Connection();
			
			metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(1, metaData.getColumnCount());
			
			 //executeQuery("select * from dyextn_file_extensions");
			

		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
    }	
//
	/**
     * This method tests for creating a entity with file attribute.
     */
    public void testCreateFileAttribute()
    {
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");
		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);
		
		
		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);
		
		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);
		
		
		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);
		
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setFileExtensionCollection(allowedExtn);
		
		try
		{
			ResultSet rs = executeQuery("select count(*) from dyextn_file_extensions");
			rs.next();
			int beforeCount = rs.getInt(1);
			
			user = entityManager.persistEntity(user);
			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(1, metaData.getColumnCount());

		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
    }

    /**
     * 
     */
    public void testCreateEntity()
    {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            TaggedValueInterface taggedValue = DomainObjectFactory
                    .getInstance().createTaggedValue();
            taggedValue.setKey("a");
            taggedValue.setValue("b");
            entity.addTaggedValue(taggedValue);
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);

            Entity newEntity = (Entity) EntityManager.getInstance()
                    .getEntityByIdentifier(entity.getId().toString());
            assertEquals(entity.getName(), newEntity.getName());

            String tableName = entity.getTableProperties().getName();
            String query = "Select * from " + tableName;
            executeQueryForMetadata(query);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            e.printStackTrace();
            fail("Exception occured");
        }

    }

    /**
     * 
     */

    public void testCreateEntityWithEntityGroup()
    {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            EntityGroup entityGroup = new EntityGroup();
            entityGroup.setName("testEntityGroup");
            entity.addEntityGroupInterface(entityGroup);
            entityGroup.addEntity(entity);
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            Entity newEntity = (Entity) EntityManager.getInstance()
                    .getEntityByIdentifier(entity.getId().toString());
            //  Checking whether metadata information is saved properly or not.
            assertEquals(entity.getName(), newEntity.getName());
            Collection collection = newEntity.getEntityGroupCollection();
            Iterator iter = collection.iterator();
            EntityGroup eg = (EntityGroup) iter.next();
            assertEquals(eg.getId(), entityGroup.getId());

        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

   

   

    /**
     * This method tests GetRecordById method for the condition where record and entity does exists
     */

    public void testGetRecordById()
    {

        try
        {
            EntityManagerInterface entityManagerInterface = EntityManager
                    .getInstance();

            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            Entity newEntity = (Entity) entityManagerInterface
                    .getEntityByIdentifier(entity.getId().toString());
            Map dataValue = new HashMap();

            Iterator attrIterator = newEntity.getAttributeCollection()
                    .iterator();
            int i = 0;
            while (attrIterator.hasNext())
            {
                AttributeInterface attribute = (AttributeInterface) attrIterator
                        .next();
                AttributeTypeInformationInterface typeInfo = attribute
                        .getAttributeTypeInformation();

                if (typeInfo instanceof StringAttributeTypeInformation)
                {
                    dataValue.put(attribute, "temp" + i);
                }
                else if (attribute instanceof DateAttributeTypeInformation)
                {
                    dataValue.put(attribute, "11-01-2006");
                }

                i++;
            }

            entityManagerInterface.insertData(newEntity, dataValue);

            assertEquals("Employee", entity.getName());
            Map map = EntityManager.getInstance().getRecordById(entity,
                    new Long(1));

            System.out.println(map);
        }
        catch (DynamicExtensionsSystemException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * This method tests GetRecordById method for the condition wheer record does not exists for given id.
     */

    public void testGetRecordByIdNoRecord()
    {

        try
        {
            EntityManagerInterface entityManagerInterface = EntityManager
                    .getInstance();

            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            Map map = EntityManager.getInstance().getRecordById(entity,
                    new Long(1));
            assertEquals(0, map.size());
        }
        catch (DynamicExtensionsSystemException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * This method tests GetRecordById method for the condition where entity is not saved , entity  is null or record id id null
     */

    public void testGetRecordByIdEntityNotSaved()
    {

        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        Entity entity = null;

        try
        {
            entity = (Entity) new MockEntityManager().initializeEntity();

            Map map = EntityManager.getInstance().getRecordById(entity,
                    new Long(1));
            fail("Exception should have be thrown since entity is not saved");

        }
        catch (DynamicExtensionsSystemException e)
        {
            assertTrue(true);
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Unexpected Exception occured");
        }

        try
        {
            Map map = EntityManager.getInstance().getRecordById(null,
                    new Long(1));
            fail("Exception should have be thrown since entity is not saved");
        }
        catch (DynamicExtensionsSystemException e)
        {
            assertTrue(true);
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
        }

        try
        {
            entity = (Entity) entityManagerInterface.persistEntity(entity);

            Map map = EntityManager.getInstance().getRecordById(entity, null);
            fail("Exception should have be thrown since entity is not saved");
        }
        catch (DynamicExtensionsSystemException e)
        {
            assertTrue(true);
        }
        catch (DynamicExtensionsApplicationException e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
        }

    }

    /**
     * 
     */

    public void testCreateContainer()
    {
        try
        {
            Container container = (Container) new MockEntityManager()
                    .getContainer("abc");
            EntityManager.getInstance().persistContainer(container);
            Collection list = EntityManager.getInstance().getAllContainers();
            assertNotNull(list);
            Iterator iter = list.iterator();
            boolean flag = false;
            while (iter.hasNext())
            {
                Container cont = (Container) iter.next();
                if (cont.getId().equals(container.getId()))
                {
                    flag = true;
                    break;
                }
            }
            assertTrue(flag);
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * 
     */

    public void testCreateContainerForContainerWithoutEntity()
    {
        try
        {
            Container container = new Container();
            EntityManager.getInstance().persistContainer(container);
            Collection list = EntityManager.getInstance().getAllContainers();
            assertNotNull(list);
            Iterator iter = list.iterator();
            boolean flag = false;
            while (iter.hasNext())
            {
                Container cont = (Container) iter.next();
                if (cont.getId().equals(container.getId()))
                {
                    flag = true;
                    break;
                }
            }
            assertTrue(flag);
        }
        catch (Exception e)
        {
            // //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }
    }

    /**
     * 
     */

    public void testEditEntityForNewAddedAttribute()
    {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            AttributeInterface attr = new MockEntityManager()
                    .initializeStringAttribute("na", "ab");
            attr.setEntity(entity);
            entity.addAbstractAttribute(attr);
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            //   Checking whether metadata information is saved properly or not.
            String tableName = entity.getTableProperties().getName();

        }
        catch (Exception e)
        {
            //     //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

    /**
     * 
     */

    public void testEditEntityForModifiedIsNullableAttribute()
    {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            AttributeInterface attr = new MockEntityManager()
                    .initializeStringAttribute("na", "ab");
            attr.setEntity(entity);
            entity.addAbstractAttribute(attr);
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            attr = (AttributeInterface) entity.getAttributeByIdentifier(attr
                    .getId());
            attr.setIsNullable(new Boolean(false));
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            Attribute savedAttribute = (Attribute) entity
                    .getAttributeByIdentifier(attr.getId());
            assertFalse(savedAttribute.getIsNullable());
            //   Checking whether metadata information is saved properly or not.
            String tableName = entity.getTableProperties().getName();
            //   //TODO Check table query
        }
        catch (Exception e)
        {
            // //TODO Auto-generated catch block
            Logger.out.debug(e.getMessage());
            fail("Exception occured");
        }

    }

   

	/**
	 * This method tests for creating a entity with file attribute.
	 */
	public void testInsertRecordForFileAttribute1()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");

		AttributeInterface age = factory.createIntegerAttribute();
		age.setName("Age");
		user.addAbstractAttribute(age);

		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);

		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);

		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);

		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation())
				.setFileExtensionCollection(allowedExtn);

		try
		{
			user = entityManager.persistEntity(user);

			FileAttributeRecordValue fileRecord = new FileAttributeRecordValue();
			fileRecord.setContentType("PDF");
			fileRecord.setFileName("tp.java");
			String fileContent = "this is cntent of the file";
			//File f = new File("C:\\BinaryBlobType.java");
			fileRecord.setFileContent(fileContent.getBytes());

			Map dataValue = new HashMap();
			dataValue.put(age, "45");
			dataValue.put(resume, fileRecord);
			
			Long recordId = entityManager.insertData(user, dataValue);
			
			ResultSet resultSet = executeQuery("select count(*) from "
                    + user.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This method tests for creating a entity with file attribute.
	 */
	public void testInsertRecordForFileAttribute()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");

		AttributeInterface age = factory.createIntegerAttribute();
		age.setName("Age");
		user.addAbstractAttribute(age);

		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);

		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);

		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);

		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation())
				.setFileExtensionCollection(allowedExtn);

		try
		{
			user = entityManager.persistEntity(user);

			FileAttributeRecordValue fileRecord = new FileAttributeRecordValue();
			fileRecord.setContentType("PDF");
			fileRecord.setFileName("tp.java");
			String fileContent = "this is cntent of the file";
			//File f = new File("C:\\BinaryBlobType.java");
			fileRecord.setFileContent(fileContent.getBytes());

			Map dataValue = new HashMap();
			dataValue.put(age, "45");
			dataValue.put(resume, fileRecord);
			
			Long recordId = entityManager.insertData(user, dataValue);
			
			ResultSet resultSet = executeQuery("select count(*) from "
                    + user.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
	
	
	/**
	 * This method tests for creating a entity with file attribute.
	 */
	public void testGerRecordForFileAttribute()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");

		AttributeInterface age = factory.createIntegerAttribute();
		age.setName("Age");
		user.addAbstractAttribute(age);

		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);

		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);

		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);

		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation())
				.setFileExtensionCollection(allowedExtn);

		try
		{
			user = entityManager.persistEntity(user);

			FileAttributeRecordValue fileRecord = new FileAttributeRecordValue();
			fileRecord.setContentType("PDF");
			fileRecord.setFileName("tp.java");
			String fileContent = "this is content of the file";
			fileRecord.setFileContent(fileContent.getBytes());

			Map dataValue = new HashMap();
			dataValue.put(age, "45");
			dataValue.put(resume, fileRecord);
			
			Long recordId = entityManager.insertData(user, dataValue);
			
			dataValue = entityManager.getRecordById(user, recordId);
			
			assertEquals("45", dataValue.get("Age"));
			assertEquals("tp.java", ((FileAttributeRecordValue) dataValue.get("Resume")).getFileName());
			
			
			System.out.println(dataValue);
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This method tests for creating a entity with file attribute.
	 */
	public void testEditRecordForFileAttribute()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		user.setName("User");

		AttributeInterface age = factory.createIntegerAttribute();
		age.setName("Age");
		user.addAbstractAttribute(age);

		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);

		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);

		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);

		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation())
				.setFileExtensionCollection(allowedExtn);

		try
		{
			user = entityManager.persistEntity(user);

			FileAttributeRecordValue fileRecord = new FileAttributeRecordValue();
			fileRecord.setContentType("PDF");
			fileRecord.setFileName("tp.java");
			String fileContent = "this is content of the file";
			fileRecord.setFileContent(fileContent.getBytes());

			Map dataValue = new HashMap();
			dataValue.put(age, "45");
			dataValue.put(resume, fileRecord);
			
			Long recordId = entityManager.insertData(user, dataValue);
			recordId = entityManager.insertData(user, dataValue);
			
			//dataValue = entityManager.getRecordById(user, recordId);
			
			dataValue.clear();
			fileRecord.setFileName("new file name");
			fileRecord.setFileContent("modified file contents".getBytes());
			dataValue.put(resume, fileRecord);			
			entityManager.editData(user, dataValue,recordId);
			
			dataValue = entityManager.getRecordById(user, recordId);

			assertEquals("new file name", ((FileAttributeRecordValue) dataValue.get("Resume")).getFileName());
			
			System.out.println(dataValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * This method tests for creating a entity with file attribute.
	 */
	public void testDeleteRecordForFileAttribute()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		
		EntityInterface user = factory.createEntity();
		user.setName("User");

		AttributeInterface age = factory.createIntegerAttribute();
		age.setName("Age");
		user.addAbstractAttribute(age);

		AttributeInterface resume = factory.createFileAttribute();
		resume.setName("Resume");
		user.addAbstractAttribute(resume);

		Collection<FileExtension> allowedExtn = new HashSet<FileExtension>();

		FileExtension txtExtn = new FileExtension();
		txtExtn.setFileExtension("txt");
		allowedExtn.add(txtExtn);

		FileExtension pdfExtn = new FileExtension();
		pdfExtn.setFileExtension("pdf");
		allowedExtn.add(pdfExtn);

		allowedExtn.add(txtExtn);
		allowedExtn.add(pdfExtn);

		((FileAttributeTypeInformation) resume.getAttributeTypeInformation()).setMaxFileSize(20F);
		((FileAttributeTypeInformation) resume.getAttributeTypeInformation())
				.setFileExtensionCollection(allowedExtn);

		try
		{
			user = entityManager.persistEntity(user);

			FileAttributeRecordValue fileRecord = new FileAttributeRecordValue();
			fileRecord.setContentType("PDF");
			fileRecord.setFileName("tp.java");
			String fileContent = "this is content of the file";
			fileRecord.setFileContent(fileContent.getBytes());

			Map dataValue = new HashMap();
			dataValue.put(age, "45");
			dataValue.put(resume, fileRecord);
			

			
			Long recordId = entityManager.insertData(user, dataValue);
			
			dataValue = entityManager.getRecordById(user, recordId);
			
			
            ResultSet resultSet = executeQuery("select count(*) from dyextn_attribute_record");
            resultSet.next();
            int beforeCnt =  resultSet.getInt(1);
            resultSet.close();
            
			entityManager.deleteRecord(user, recordId);
			
            resultSet = executeQuery("select count(*) from dyextn_attribute_record");
            resultSet.next();
            int afterCnt =  resultSet.getInt(1);
            
            assertEquals(1,beforeCnt - afterCnt );
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
		
	}
	
	/**
	 * This method tests the creation of entity group
	 */
	public void testCreateEntityGroupForRollback()
	{
		EntityGroup entityGroup = null;
		try
		{
			MockEntityManager mock = new MockEntityManager();
			entityGroup = (EntityGroup) mock.initializeEntityGroup();
			for (int i = 0; i <= 11; i++)
			{
				Entity entity = (Entity) mock.initializeEntity();
				entityGroup.addEntity(entity);
				if (i == 10) {
					entity = (Entity) mock.initializeEntity();
					TableProperties tableProperties = new TableProperties();
					tableProperties.setName("@#$%@$%$^");
					entity.setTableProperties(tableProperties);
					entityGroup.addEntity(entity);
				}
			}
			entityGroup = (EntityGroup) EntityManager.getInstance().persistEntityGroup(entityGroup);
			fail();
		}
		catch (Exception e)
		{
			
			boolean isTablePresent = isTablePresent(entityGroup.getEntityCollection().iterator().next().getTableProperties().getName());
			assertFalse(isTablePresent);
			e.printStackTrace();
		}
	}
	/**
	 * This method tests the creation of entity group
	 */
	public void testCreateEntityGroupForMultipleEntities()
	{
		EntityGroup entityGroup = null;
		try
		{
			MockEntityManager mock = new MockEntityManager();
			entityGroup = (EntityGroup) mock.initializeEntityGroup();
			for (int i = 0; i <= 11; i++)
			{
				Entity entity = (Entity) mock.initializeEntity();
				entityGroup.addEntity(entity);
			}
			entityGroup = (EntityGroup) EntityManager.getInstance().persistEntityGroup(entityGroup);
			Iterator iterator = entityGroup.getEntityCollection().iterator();
			while(iterator.hasNext()) { 
			Entity entity = (Entity) iterator.next();
			boolean isTablePresent = isTablePresent(entity.getTableProperties().getName());
			assertTrue(isTablePresent);
			}
		}
		catch (Exception e)
		{
			fail();
			e.printStackTrace();
		}
	}

	 /**
     * 
     *
     */

    public void testCreateEntityForRollbackQuery()
    {
        Entity entity = null;
        try
        {
            entity = (Entity) new MockEntityManager().initializeEntity();
            TableProperties tableProperties = new TableProperties();
            tableProperties.setName("Created_table");
            entity.setTableProperties(tableProperties);
            String query = "create table Created_table (id integer)";
            executeQueryDDL(query);
            EntityManager.getInstance().persistEntity(entity);
            fail("Exception should have occured but did not");
        }
        catch (DynamicExtensionsSystemException e)
        {
            Logger.out.info("Exception because of wrong table name.");
            Logger.out.info(e.getMessage());
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            executeQueryDDL("drop table Created_table");
            try
            {
                Entity newEntity = (Entity) EntityManager.getInstance()
                        .getEntityByIdentifier(entity.getId().toString());
                fail();

            }
            catch (DynamicExtensionsApplicationException e)
            {
                Logger.out.info("Entity object not found in the database ....");
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }

        }

    }
    
    /**
     * 
     *
     */

    public void testCreateEntityForQueryException()
    {
        try
        {
            Entity entity = (Entity) new MockEntityManager().initializeEntity();
            TableProperties tableProperties = new TableProperties();
            tableProperties.setName("!##$$%");
            entity.setTableProperties(tableProperties);
            entity = (Entity) EntityManager.getInstance().persistEntity(entity);
            fail("Exception should have occured but did not");
        }
        catch (DynamicExtensionsSystemException e)
        {
            Logger.out.info("Exception because of wrong table name.");
            Logger.out.info(e.getMessage());
        }
        catch (Exception e)
        {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

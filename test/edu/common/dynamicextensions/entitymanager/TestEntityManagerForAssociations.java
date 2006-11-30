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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.exception.BaseDynamicExtensionsException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.util.logger.Logger;

public class TestEntityManagerForAssociations extends DynamicExtensionsBaseTestCase
{

	/**
	 * 
	 */
	public TestEntityManagerForAssociations()
	{
		super();
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0 name
	 */
	public TestEntityManagerForAssociations(String arg0)
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
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	private RoleInterface getRole(AssociationType associationType, String name, Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}

	/**
	 * This test case test for associating two entities with one to many association 
	 * 
	 * for oracle it should throw exception.
	 * for mysql  it works.  
	 */
	public void testCreateEntityWithOneToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// Associate user (1)------ >(*)study       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{
			//entityManager.createEntity(study);

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * This test case test for associating two entities with one to many association 
	 * 
	 * for oracle it should throw exception.
	 * for mysql  it works.  
	 */
	public void testCreateEntityWithManyToOneAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// Associate user (1)------ >(*)study       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

		user.addAbstractAttribute(association);

		try
		{
			//entityManager.createEntity(study);

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * This test case tries to modify data type of the attribute,when data is present for that column.
	 * for oracle it should throw exception.
	 * for mysql  it works.  
	 */
	public void testCreateEntityWithAssociationWithUnsavedTargetEntity()
	{
		Entity srcEntity;

		try
		{
			srcEntity = (Entity) new MockEntityManager().initializeEntity();
			srcEntity.setName("study");
			//Entity savedSrcEntity = (Entity) EntityManager.getInstance().createEntity(srcEntity);
			Entity targetEntity = (Entity) new MockEntityManager().initializeEntity();
			targetEntity.setName("user");
			//Entity savedTargetEntity = (Entity) EntityManager.getInstance().createEntity(targetEntity);
			Association association = (Association) DomainObjectFactory.getInstance().createAssociation();
			association.setEntity(srcEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			association.setName("primaryInvestigator");
			association.setSourceRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ONE, Cardinality.ONE));
			association.setTargetRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE));
			srcEntity.addAbstractAttribute(association);
			// association.sets

			EntityManager.getInstance().persistEntity(srcEntity);

		}
		catch (BaseDynamicExtensionsException e)
		{
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}

	}

	/**
	 * This test case test for associating two entities with many to many association  and direction is src_destination.
	 * 
	 * for oracle it should throw exception.
	 * for mysql  it works.  
	 */
	public void testCreateEntityWithManyToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// Associate user (1)------ >(*)study       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			String middleTableName = association.getConstraintProperties().getName();

			assertNotNull(middleTableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + middleTableName);
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * This test case test for associating three entities with  many to one to one 
	 * 
	 * User(*) ---- >(1)Study(1) ------>(1)institute
	 */
	public void testCreateEntityWithCascadeManyToOneAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create institution
		EntityInterface institution = factory.createEntity();
		AttributeInterface institutionNameAttribute = factory.createStringAttribute();
		institutionNameAttribute.setName("institution name");
		institution.setName("institution");
		institution.addAbstractAttribute(institutionNameAttribute);

		// Associate user (*)------ >(1)study       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

		user.addAbstractAttribute(association);

		// Associate study(1) ------> (1) institution       
		AssociationInterface studInstAssociation = factory.createAssociation();

		studInstAssociation.setTargetEntity(institution);
		studInstAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		studInstAssociation.setName("studyLocation");
		studInstAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "studyPerformed", Cardinality.ZERO, Cardinality.ONE));
		studInstAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "studyLocation", Cardinality.ZERO, Cardinality.ONE));

		study.addAbstractAttribute(studInstAssociation);

		try
		{
			//entityManager.createEntity(study);

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + institution.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());

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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * This test case test for associating three entities with  many to one to many to many
	 * 
	 *        User(*) ---- >(1)Study(1) ------>(1)institute(*)-- (*)User
	 *        
	 */
	public void testCreateEntityWithCyclicCascadeManyToOneAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create institution
		EntityInterface institution = factory.createEntity();
		AttributeInterface institutionNameAttribute = factory.createStringAttribute();
		institutionNameAttribute.setName("institution name");
		institution.setName("institution");
		institution.addAbstractAttribute(institutionNameAttribute);

		// Associate user (*)------ >(1)study       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

		user.addAbstractAttribute(association);

		// Associate study(1) ------> (1) institution       
		AssociationInterface studInstAssociation = factory.createAssociation();

		studInstAssociation.setTargetEntity(institution);
		studInstAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		studInstAssociation.setName("studyLocation");
		studInstAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "studyPerformed", Cardinality.ZERO, Cardinality.ONE));
		studInstAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "studyLocation", Cardinality.ZERO, Cardinality.ONE));

		study.addAbstractAttribute(studInstAssociation);

		// Associate institution (*)----->(*) user      
		AssociationInterface instUserAssociation = factory.createAssociation();

		instUserAssociation.setTargetEntity(user);
		instUserAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		instUserAssociation.setName("lecturers");
		instUserAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "university", Cardinality.ONE, Cardinality.MANY));
		instUserAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "lecturer", Cardinality.ONE, Cardinality.MANY));

		institution.addAbstractAttribute(instUserAssociation);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + institution.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());

			assertNotNull(instUserAssociation.getConstraintProperties().getName());

			metaData = executeQueryForMetadata("select * from " + instUserAssociation.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());

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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * Purpose is to test the self referencing of the entity. 
	 * Scenario - user(*)------>(1)User
	 *                   creator   
	 */
	public void testCreateEntityWithSelfReferencingManyToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// Associate user (*)------ >(1)user       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(user);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "subpi", Cardinality.ONE, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "pi", Cardinality.ONE, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			String tableName = user.getTableProperties().getName();

			assertNotNull(tableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + tableName);
			assertEquals(2, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + association.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * Purpose is to test the multiple self referencing of the entity. 
	 * Scenario - 
	 * user(*)------>(*)User
	 *       childUsers
	 * user(*)------>(1)User
	 *        creator   
	 */
	public void testCreateEntityWithSelfReferencingMultipleAssociations()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// Associate user (*)------ >(*)user       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(user);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("children");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "subpi", Cardinality.ONE, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "pi", Cardinality.ONE, Cardinality.MANY));

		user.addAbstractAttribute(association);

		// Associate user (*)------ >(1)user       
		AssociationInterface creatorAssociation = factory.createAssociation();

		creatorAssociation.setTargetEntity(user);
		creatorAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		creatorAssociation.setName("creator");
		creatorAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "user", Cardinality.ONE, Cardinality.MANY));
		creatorAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "creator", Cardinality.ONE, Cardinality.ONE));

		user.addAbstractAttribute(creatorAssociation);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			String tableName = user.getTableProperties().getName();

			assertNotNull(tableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + tableName);
			assertEquals(3, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + association.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * Purpose is to test the multiple self referencing of the entity. 
	 * Scenario - 
	 * user(*)------>(*)User
	 *       childUsers
	 * user(*)------>(*)User
	 *        creators  
	 */
	public void testCreateEntityWithSelfReferencingMultipleManyToManyAssociations()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// Associate user (*)------ >(*)user       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(user);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("children");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "subpi", Cardinality.ONE, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "pi", Cardinality.ONE, Cardinality.MANY));

		user.addAbstractAttribute(association);

		// Associate user (*)------ >(*)user       
		AssociationInterface creatorAssociation = factory.createAssociation();

		creatorAssociation.setTargetEntity(user);
		creatorAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		creatorAssociation.setName("parentUSers");
		creatorAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "user", Cardinality.ONE, Cardinality.MANY));
		creatorAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "creator", Cardinality.ONE, Cardinality.MANY));

		user.addAbstractAttribute(creatorAssociation);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			String tableName = user.getTableProperties().getName();

			assertNotNull(tableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + tableName);
			assertEquals(2, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + association.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + creatorAssociation.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());

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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * This test case test for associating two entities with one to many association  and bidirectional
	 * It tests for internal system generated association for the bidirectional.
	 * User(1) <---->(*)Study
	 */
	public void testCreateEntityBidirectionalOneToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		study.setName("Study");

		// Associate  User(1) <---->(*)Study 
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());

			assertEquals(1, study.getAbstractAttributeCollection().size());

			Association systemGeneratedAssociation = (Association) study.getAbstractAttributeCollection().toArray()[0];

			assertTrue(systemGeneratedAssociation.getIsSystemGenerated());

			assertEquals(user, systemGeneratedAssociation.getTargetEntity());

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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}

	/**
	 * 
	 *
	 */
	public void testGetAssociation()
	{
		try
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			DomainObjectFactory factory = DomainObjectFactory.getInstance();

			// create user 
			EntityInterface user = factory.createEntity();
			AttributeInterface userNameAttribute = factory.createStringAttribute();
			userNameAttribute.setName("user name");
			user.setName("user");
			user.addAbstractAttribute(userNameAttribute);

			// create study 
			EntityInterface study = factory.createEntity();
			AttributeInterface studyNameAttribute = factory.createStringAttribute();
			studyNameAttribute.setName("study name");
			study.setName("study");
			study.addAbstractAttribute(studyNameAttribute);

			// create institution
			EntityInterface institution = factory.createEntity();
			AttributeInterface institutionNameAttribute = factory.createStringAttribute();
			institutionNameAttribute.setName("institution name");
			institution.setName("institution");
			institution.addAbstractAttribute(institutionNameAttribute);

			// Associate user (*)------ >(1)study       
			AssociationInterface association = factory.createAssociation();

			association.setTargetEntity(study);
			association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			association.setName("primaryInvestigator");
			association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
			association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

			user.addAbstractAttribute(association);

			// Associate study(1) ------> (1) institution       
			AssociationInterface studInstAssociation = factory.createAssociation();

			studInstAssociation.setTargetEntity(institution);
			studInstAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			studInstAssociation.setName("studyLocation");
			studInstAssociation.setSourceRole(getRole(AssociationType.ASSOCIATION, "studyPerformed", Cardinality.ZERO, Cardinality.ONE));
			studInstAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "studyLocation", Cardinality.ZERO, Cardinality.ONE));

			study.addAbstractAttribute(studInstAssociation);

			user = (Entity)entityManager.persistEntity(user);
			AssociationInterface associationInterface = (AssociationInterface) entityManager.getAssociation("user",
					"primaryInvestigator");
			assertTrue(associationInterface != null);

		}

		catch (Exception e)
		{
			//TODO Auto-generated catch block
			e.printStackTrace();
			Logger.out.debug(e.getMessage());
			fail("Exception occured");
		}

	}
	
	public void testGetAssociations()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study 
		EntityInterface study = factory.createEntity();
		study.setName("study name");

		// Associate  User(1) <---->(*)Study 
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{
			EntityInterface savedUser = entityManager.persistEntity(user);
			Collection associationCollection = entityManager.getAssociations(savedUser.getId(), study.getId());
			assertTrue(associationCollection != null && associationCollection.size() > 0); 
			
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
		
	}
	
	/**
	 * Purpose is to test the self referencing of the entity. 
	 * Scenario - user(*)------>(1)User
	 *                   creator   
	 */
	public void testCreateEntityWithSelfReferencingBidirectionManyToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// Associate user (*)------ >(1)user       
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(user);
		association.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "subpi", Cardinality.ONE, Cardinality.MANY));
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "pi", Cardinality.ONE, Cardinality.MANY));

		user.addAbstractAttribute(association);

		try
		{

			EntityInterface savedUser = entityManager.persistEntity(user);

			String tableName = user.getTableProperties().getName();

			assertNotNull(tableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + tableName);
			assertEquals(2, metaData.getColumnCount());

			metaData = executeQueryForMetadata("select * from " + association.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
	
	/**
	 * This test case test for adding a new associatiion bet 2 entities 
	 * 
	 * for oracle it should throw exception.
	 * for mysql  it works.  
	 */
	public void testAddAssociationAfterSave()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		
		
		try
		{
			user = entityManager.persistEntity(user);

			// create study 
			EntityInterface study = factory.createEntity();
			AttributeInterface studyNameAttribute = factory.createStringAttribute();
			studyNameAttribute.setName("study name");
			study.setName("study");
			study.addAbstractAttribute(studyNameAttribute);

			// Associate user (1)------ >(*)study       
			AssociationInterface association = factory.createAssociation();

			association.setTargetEntity(study);
			association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			association.setName("primaryInvestigator");
			association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
			association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

			user.addAbstractAttribute(association);
			

			EntityInterface savedUser = entityManager.persistEntity(user);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			
			
//			 Associate user (1) <------>(*)study       
			AssociationInterface association1 = factory.createAssociation();

			association1.setTargetEntity(study);
			association1.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
			association1.setName("primaryInvestigator");
			association1.setSourceRole(getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.ONE));
			association1.setTargetRole(getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY));

			savedUser.addAbstractAttribute(association1);
			
			savedUser = entityManager.persistEntity(savedUser);

			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
	
	/**
	 * This test case test for removing an association bet 2 existing entities 
	 */
	public void testRemoveAssociationAfterSave()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		
		
		try
		{
			user = entityManager.persistEntity(user);

			// create study 
			EntityInterface study = factory.createEntity();
			AttributeInterface studyNameAttribute = factory.createStringAttribute();
			studyNameAttribute.setName("study name");
			study.setName("study");
			study.addAbstractAttribute(studyNameAttribute);

			// Associate user (1)------ >(*)study   
			RoleInterface sourceRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY);
			RoleInterface targetRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.ONE);
            AssociationInterface association = getAssociation(user, AssociationDirection.SRC_DESTINATION, "prim", sourceRole, targetRole);
			
			user.addAbstractAttribute(association);
			user = entityManager.persistEntity(user);
			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			
			user.removeAssociation(association);
			user = entityManager.persistEntity(user);

			metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			
			
			sourceRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY);
			targetRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY);
            association = getAssociation(user, AssociationDirection.SRC_DESTINATION, "prim", sourceRole, targetRole);

            user.addAbstractAttribute(association);
			user = entityManager.persistEntity(user);

			assertTrue(isTablePresent(association.getConstraintProperties().getName()));
			metaData = executeQueryForMetadata("select * from " + association.getConstraintProperties().getName());
			assertEquals(3, metaData.getColumnCount());

			user.removeAssociation(association);
			user = entityManager.persistEntity(user);

			assertFalse(isTablePresent(association.getConstraintProperties().getName()));
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
	
	/**
	 * This test case test for removing an association bet 2 existing entities
	 * Before- SRC-DESTINATION
	 * After - BIDIRECTIONAL 
	 */
	public void testEditAssociationDirection1AfterSave()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		
		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		
		// Associate user (1)------ >(*)study   
		RoleInterface sourceRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY);
		RoleInterface targetRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.ONE);
        AssociationInterface association = getAssociation(study, AssociationDirection.SRC_DESTINATION, "prim", sourceRole, targetRole);
		
		user.addAbstractAttribute(association);

		
		
		try
		{
			user = entityManager.persistEntity(user);
			
			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			
			association.getSourceRole().setMaximumCardinality(Cardinality.ONE);
			association.getTargetRole().setMaximumCardinality(Cardinality.MANY);
			association.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
			
			user = entityManager.persistEntity(user);
			
			metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			
			
			EntityInterface savedUser = entityManager.getEntityByIdentifier(user.getId().toString());
			assertEquals(1,savedUser.getAssociationCollection().size());
			
			EntityInterface savedStudy = entityManager.getEntityByIdentifier(study.getId().toString());
			assertEquals(1,savedStudy.getAssociationCollection().size());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
	
	/**
	 * This test case test for removing an association bet 2 existing entities
	 * Before-  BIDIRECTIONAL
	 * After -  SRC-DESTINATION
	 * system generated association should get removed.
	 */
	public void testEditAssociationDirectionAfterSave()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		
		// create study 
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		
		// Associate user (1)------ >(*)study   
		RoleInterface sourceRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.MANY);
		RoleInterface targetRole = getRole(AssociationType.ASSOCIATION, "association1", Cardinality.ZERO, Cardinality.ONE);
        AssociationInterface association = getAssociation(study, AssociationDirection.BI_DIRECTIONAL, "prim", sourceRole, targetRole);
		
		user.addAbstractAttribute(association);

		
		
		try
		{
			user = entityManager.persistEntity(user);
			
			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			
			association.getSourceRole().setMaximumCardinality(Cardinality.ONE);
			association.getTargetRole().setMaximumCardinality(Cardinality.MANY);
			association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			
			user = entityManager.persistEntity(user);
			
			metaData = executeQueryForMetadata("select * from " + user.getTableProperties().getName());
			assertEquals(2, metaData.getColumnCount());
			metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(3, metaData.getColumnCount());
			
			
			EntityInterface savedUser = entityManager.getEntityByIdentifier(user.getId().toString());
			assertEquals(1,savedUser.getAssociationCollection().size());
			
			EntityInterface savedStudy = entityManager.getEntityByIdentifier(study.getId().toString());
			assertEquals(0,savedStudy.getAssociationCollection().size());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
	
	/**
	 * Purpose is to test the self referencing of the entity. 
	 * Scenario - user(*)------>(1)User
	 *                   creator   
	 */
	public void testEditEntityWithSelfReferencingBiDirectionalManyToManyAssociation()
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user 
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		

		try
		{
			user = entityManager.persistEntity(user);

			// Associate user (*)------ >(1)user       
			AssociationInterface association = factory.createAssociation();

			association.setTargetEntity(user);
			association.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
			association.setName("primaryInvestigator");
			association.setSourceRole(getRole(AssociationType.ASSOCIATION, "subpi", Cardinality.ONE, Cardinality.ONE));
			association.setTargetRole(getRole(AssociationType.ASSOCIATION, "pi", Cardinality.ONE, Cardinality.MANY));

			user.addAbstractAttribute(association);

			EntityInterface savedUser = entityManager.persistEntity(user);

			String tableName = user.getTableProperties().getName();

			assertNotNull(tableName);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + tableName);
			assertEquals(3, metaData.getColumnCount());
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
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();

		}

	}
    
    /**
     * This method test for inserting data for a multi select attribute
     */
    public void testInsertDataForAssociationMany2Many()
    {
        
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

//          create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory.createStringAttribute();
            userNameAttribute.setName("user name");
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            
//          create study 
            EntityInterface study = factory.createEntity();
            AttributeInterface studyNameAttribute = factory.createStringAttribute();
            studyNameAttribute.setName("study name");
            study.setName("study");
            study.addAbstractAttribute(studyNameAttribute);
            
//          Associate user (1)------ >(*)study       
            AssociationInterface association = factory.createAssociation();
            association.setTargetEntity(study);
            association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
            association.setName("primaryInvestigator");
            association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
            association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

            user.addAbstractAttribute(association);


            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(user);

            Map dataValue = new HashMap();
            
            dataValue.put(studyNameAttribute, "study");
            entityManagerInterface.insertData(study, dataValue);
            dataValue.clear();
            dataValue.put(studyNameAttribute, "study1");
            entityManagerInterface.insertData(study, dataValue);
            
            dataValue.clear();
            dataValue.put(userNameAttribute, "rahul");
            dataValue.put(association, 1L);

            entityManagerInterface.insertData(savedEntity, dataValue);
            
            dataValue.clear();
            dataValue.put(userNameAttribute, "vishvesh");
            dataValue.put(association, 2L);
            entityManagerInterface.insertData(savedEntity, dataValue);

            ResultSet resultSet = executeQuery("select count(*) from "
                    + association.getConstraintProperties().getName());
            resultSet.next();
            assertEquals(2, resultSet.getInt(1));
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
     * This method test for inserting data for a multi select attribute
     */
    public void testInsertDataForAssociationOne2Many()
    {
        
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

//          create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory.createStringAttribute();
            userNameAttribute.setName("user name");
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            
//          create study 
            EntityInterface study = factory.createEntity();
            AttributeInterface studyNameAttribute = factory.createStringAttribute();
            studyNameAttribute.setName("study name");
            study.setName("study");
            study.addAbstractAttribute(studyNameAttribute);
            
//          Associate user (1)------ >(*)study       
            AssociationInterface association = factory.createAssociation();
            association.setTargetEntity(study);
            association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
            association.setName("primaryInvestigator");
            association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.ONE));
            association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

            user.addAbstractAttribute(association);


            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(user);

            Map dataValue = new HashMap();
            
            dataValue.put(studyNameAttribute, "study");
            entityManagerInterface.insertData(study, dataValue);
            
            dataValue.clear();
            dataValue.put(studyNameAttribute, "study1");
            entityManagerInterface.insertData(study, dataValue);
            
            dataValue.clear();
            dataValue.put(userNameAttribute, "rahul");
            dataValue.put(association, 1L);

            entityManagerInterface.insertData(savedEntity, dataValue);
            
            dataValue.clear();
            dataValue.put(userNameAttribute, "vishvesh");
            dataValue.put(association, 2L);
            entityManagerInterface.insertData(savedEntity, dataValue);

            ResultSet resultSet = executeQuery("select count(*) from "
                    + study.getTableProperties().getName());
            resultSet.next();
            assertEquals(2, resultSet.getInt(1));
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
//
    /**
     * This method test for inserting data for a multi select attribute
     */
    public void testInsertDataForAssociationMany2One()
    {
        
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();

        try
        {

//          create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory.createStringAttribute();
            userNameAttribute.setName("user name");
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            
//          create study 
            EntityInterface study = factory.createEntity();
            AttributeInterface studyNameAttribute = factory.createStringAttribute();
            studyNameAttribute.setName("study name");
            study.setName("study");
            study.addAbstractAttribute(studyNameAttribute);
            
//          Associate user (1)------ >(*)study       
            AssociationInterface association = factory.createAssociation();
            association.setTargetEntity(study);
            association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
            association.setName("primaryInvestigator");
            association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
            association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

            user.addAbstractAttribute(association);


            EntityInterface savedEntity = entityManagerInterface
                    .persistEntity(user);

            Map dataValue = new HashMap();
            dataValue.put(userNameAttribute, "rahul");
            dataValue.put(association, 1L);

            entityManagerInterface.insertData(savedEntity, dataValue);

            ResultSet resultSet = executeQuery("select * from "
                    + savedEntity.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
           

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
     * This test case is to check the constraint violation in case when the maximum cardinality for target is one. 
     * So in this test case we try to insert data such that the same target entity record is associated with the 
     * source entity record twice. After the first insertion is successful, when the second insertion takes place
     * at that time constraint violation fails and application exception is thrown.
     */
    public void testInsertDataForConstraintViolationForManyToOne()
    {
        
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();
        EntityInterface savedEntity = null;
        try
        {

//          create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory.createStringAttribute();
            userNameAttribute.setName("user name");
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            
//          create study 
            EntityInterface study = factory.createEntity();
            AttributeInterface studyNameAttribute = factory.createStringAttribute();
            studyNameAttribute.setName("study name");
            study.setName("study");
            study.addAbstractAttribute(studyNameAttribute);
            
//          Associate user (1)------ >(*)study       
            AssociationInterface association = factory.createAssociation();
            association.setTargetEntity(study);
            association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
            association.setName("primaryInvestigator");
            association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ZERO, Cardinality.MANY));
            association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.ONE));

            user.addAbstractAttribute(association);


            savedEntity = entityManagerInterface
                    .persistEntity(user);

            Map dataValue = new HashMap();
            dataValue.put(userNameAttribute, "rahul");
            dataValue.put(association, 1L);

            entityManagerInterface.insertData(savedEntity, dataValue);
            ResultSet resultSet = executeQuery("select * from "
                    + savedEntity.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
            
            entityManagerInterface.insertData(savedEntity, dataValue);
            
           fail();

        }
        catch (DynamicExtensionsSystemException e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
        catch (DynamicExtensionsApplicationException e)
        {
            
            ResultSet resultSet = executeQuery("select * from "
                    + savedEntity.getTableProperties().getName());
            try
            {
                resultSet.next();
                assertEquals(1, resultSet.getInt(1));
            }
            catch (SQLException e1)
            {
                fail();
                e1.printStackTrace();
            }
            
            Logger.out.debug("constraint validation should fail...because max target cardinality is one");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();

            Logger.out.debug(e.getStackTrace());
        }

    }

    /**
     * This test case is to check the scenario when user adds maximum cardinality less than the minimum cardinality
     * In such case DE internally corrects these cardinalities by swapping the minimum and maximum cardinalities.
     */
    public void testInsertDataForInvalidCardinalities()
    {
        
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        DomainObjectFactory factory = DomainObjectFactory.getInstance();
        EntityInterface savedEntity = null;
        try
        {

//          create user 
            EntityInterface user = factory.createEntity();
            AttributeInterface userNameAttribute = factory.createStringAttribute();
            userNameAttribute.setName("user name");
            user.setName("user");
            user.addAbstractAttribute(userNameAttribute);

            
//          create study 
            EntityInterface study = factory.createEntity();
            AttributeInterface studyNameAttribute = factory.createStringAttribute();
            studyNameAttribute.setName("study name");
            study.setName("study");
            study.addAbstractAttribute(studyNameAttribute);
            
//          Associate user (1)------ >(*)study       
            AssociationInterface association = factory.createAssociation();
            association.setTargetEntity(study);
            association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
            association.setName("primaryInvestigator");
            association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.MANY, Cardinality.ZERO));
            association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ONE, Cardinality.ZERO));

            user.addAbstractAttribute(association);
            savedEntity = entityManagerInterface
                    .persistEntity(user);

            Map dataValue = new HashMap();
            dataValue.put(userNameAttribute, "rahul");
            dataValue.put(association, 1L);

            entityManagerInterface.insertData(savedEntity, dataValue);
            ResultSet resultSet = executeQuery("select * from "
                    + savedEntity.getTableProperties().getName());
            resultSet.next();
            assertEquals(1, resultSet.getInt(1));
         }
        catch (Exception e)
        {
            fail();
            Logger.out.debug(e.getStackTrace());
        }
       
    }
    
	/**
	 * @param targetEntity
	 * @param associationDirection
	 * @param assoName
	 * @param sourceRole
	 * @param targetRole
	 * @return
	 */
	private AssociationInterface  getAssociation(EntityInterface targetEntity, AssociationDirection associationDirection, String assoName, RoleInterface sourceRole, RoleInterface targetRole) {
		AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
		association.setTargetEntity(targetEntity);
		association.setAssociationDirection(associationDirection);
		association.setName(assoName);
		association.setSourceRole(sourceRole);
		association.setTargetRole(targetRole);
        return association;
	}
}

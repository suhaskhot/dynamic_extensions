
package edu.common.dynamicextensions.categoryManager;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityGroupManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author mandar_shidhore
 *
 */
public class TestCategoryManager extends DynamicExtensionsBaseTestCase
{

	/**
	 *
	 */
	public TestCategoryManager()
	{
		super();
	}

	/**
	 * @param arg0 name
	 */
	public TestCategoryManager(String arg0)
	{
		super(arg0);
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
	 * Entities : user (1)------>(*) study
	 * 
	 * Category: Make 2 category entities, choosing attributes from user and study.
	 * Insert data for category.
	 */
	public void testCreateCategory()
	{
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user
		EntityGroupInterface entityGroup = DomainObjectFactory.getInstance().createEntityGroup();
		entityGroup.setName("User" + new Double(Math.random()).toString());

		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		((StringAttributeTypeInformation) userNameAttribute.getAttributeTypeInformation()).setSize(40);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);
		((StringAttributeTypeInformation) studyNameAttribute.getAttributeTypeInformation()).setSize(40);

		// Associate user (1)------ >(*)study
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		RoleInterface sourceRoleInterface = getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE);
		sourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
		association.setSourceRole(sourceRoleInterface);
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));
		user.addAbstractAttribute(association);

		try
		{
			//entityManager.createEntity(study);
			entityGroup.addEntity(user);
			user.setEntityGroup(entityGroup);
			entityGroup.addEntity(study);
			study.setEntityGroup(entityGroup);
			EntityGroupInterface savedUser = entityGroupManager.persistEntityGroup(entityGroup);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(metaData.getColumnCount(), noOfDefaultColumns + 2);

			CategoryInterface categoryInterface = factory.createCategory();

			CategoryEntityInterface userCategoryEntityInterface = factory.createCategoryEntity();
			userCategoryEntityInterface.setName("userCategoryEntity");
			userCategoryEntityInterface.setEntity(user);
			userCategoryEntityInterface.setNumberOfEntries(-1);

			userCategoryEntityInterface.setCategory(categoryInterface);
			categoryInterface.setRootCategoryElement(userCategoryEntityInterface);

			CategoryAttributeInterface userCategoryAttributeInterface = factory.createCategoryAttribute();
			userCategoryAttributeInterface.setName("userCategoryAttribute");
			userCategoryAttributeInterface.setAttribute(userNameAttribute);
			userCategoryEntityInterface.addCategoryAttribute(userCategoryAttributeInterface);
			userCategoryAttributeInterface.setCategoryEntity(userCategoryEntityInterface);

			CategoryEntityInterface studyCategoryEntityInterface = factory.createCategoryEntity();
			studyCategoryEntityInterface.setName("Study category entity");
			studyCategoryEntityInterface.setEntity(study);
			CategoryAttributeInterface studyCategoryAttributeInterface = factory.createCategoryAttribute();
			studyCategoryAttributeInterface.setName("studyCategoryAttribute");
			studyCategoryAttributeInterface.setAttribute(studyNameAttribute);
			studyCategoryEntityInterface.addCategoryAttribute(studyCategoryAttributeInterface);
			studyCategoryAttributeInterface.setCategoryEntity(studyCategoryEntityInterface);

			PathInterface pathInterface = factory.createPath();
			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setAssociation(association);
			pathAssociationRelationInterface.setPathSequenceNumber(1);

			pathInterface.addPathAssociationRelation(pathAssociationRelationInterface);
			pathAssociationRelationInterface.setPath(pathInterface);

			studyCategoryEntityInterface.setPath(pathInterface);
			userCategoryEntityInterface.addChildCategory(studyCategoryEntityInterface);

			CategoryAssociationInterface categoryAssociationInterface = factory.createCategoryAssociation();
			categoryAssociationInterface.setName("UserToStudyAssociation");
			categoryAssociationInterface.setTargetCategoryEntity(studyCategoryEntityInterface);
			userCategoryEntityInterface.getCategoryAssociationCollection().add(categoryAssociationInterface);
			categoryAssociationInterface.setCategoryEntity(userCategoryEntityInterface);

			int sequenceNumber = 1;
			ContainerInterface userContainerInterface = factory.createContainer();
			userContainerInterface.setAbstractEntity(userCategoryEntityInterface);
			userContainerInterface.setCaption("User container");
			userContainerInterface.setMainTableCss("formRequiredLabel");
			userContainerInterface.setRequiredFieldIndicatior("*");
			userContainerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
			userCategoryEntityInterface.addContaier(userContainerInterface);

			TextFieldInterface controlInterface = factory.createTextField();
			controlInterface.setBaseAbstractAttribute(userCategoryAttributeInterface);
			controlInterface.setColumns(50);
			controlInterface.setCaption("user control");
			controlInterface.setSequenceNumber(sequenceNumber++);

			controlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(controlInterface);

			ContainerInterface studyContainerInterface = factory.createContainer();
			studyContainerInterface.setAddCaption(false);
			TextFieldInterface studyControlInterface = factory.createTextField();
			studyControlInterface.setBaseAbstractAttribute(studyCategoryAttributeInterface);
			studyControlInterface.setColumns(50);
			studyControlInterface.setCaption("study control");
			studyControlInterface.setSequenceNumber(sequenceNumber++);
			studyCategoryEntityInterface.addContaier(studyContainerInterface);

			studyControlInterface.setParentContainer((Container) studyContainerInterface);
			studyContainerInterface.addControl(studyControlInterface);

			CategoryAssociationControlInterface categoryAssociationControlInterface = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface.setContainer(studyContainerInterface);
			categoryAssociationControlInterface.setBaseAbstractAttribute(categoryAssociationInterface);
			categoryAssociationControlInterface.setSequenceNumber(sequenceNumber++);

			categoryAssociationControlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(categoryAssociationControlInterface);

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			CategoryInterface savedCategory = categoryManager.persistCategory(categoryInterface);

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
	 * Entities : user (1)------>(*) study (1) ------>(*) experiment
	 * 
	 * Category: Make 2 category entities, choosing attributes from user and experiment. 
	 * Insert data for category.
	 */
	public void testCreateCategoryWithTwoCategoryEntities()
	{
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user
		EntityGroupInterface entityGroup = DomainObjectFactory.getInstance().createEntityGroup();
		entityGroup.setName("User" + new Double(Math.random()).toString());

		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) userNameAttribute.getAttributeTypeInformation()).setSize(40);
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) studyNameAttribute.getAttributeTypeInformation()).setSize(40);
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create experiment
		EntityInterface experiment = factory.createEntity();
		AttributeInterface experimentNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) experimentNameAttribute.getAttributeTypeInformation()).setSize(40);
		experimentNameAttribute.setName("experiment_name");
		experiment.setName("experiment");
		experiment.addAbstractAttribute(experimentNameAttribute);

		// Associate user (1)------ >(*)study
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		RoleInterface sourceRoleInterface = getRole(AssociationType.ASSOCIATION, "primaryExperiment", Cardinality.ONE, Cardinality.ONE);
		sourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);

		association.setName("primaryInvestigator");
		association.setSourceRole(sourceRoleInterface);
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		// Associate study (1)------ >(*) experiment
		AssociationInterface association2 = factory.createAssociation();

		association2.setTargetEntity(experiment);
		association2.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association2.setName("primaryExperiment");
		RoleInterface studyRoleInterface = getRole(AssociationType.ASSOCIATION, "primaryExperiment", Cardinality.ONE, Cardinality.ONE);
		studyRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
		association2.setSourceRole(studyRoleInterface);
		association2.setTargetRole(getRole(AssociationType.ASSOCIATION, "experiment", Cardinality.ZERO, Cardinality.MANY));

		study.addAbstractAttribute(association2);

		try
		{
			entityGroup.addEntity(user);
			user.setEntityGroup(entityGroup);
			entityGroup.addEntity(study);
			study.setEntityGroup(entityGroup);
			entityGroup.addEntity(experiment);
			experiment.setEntityGroup(entityGroup);

			EntityGroupInterface savedUser = entityGroupManager.persistEntityGroup(entityGroup);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(metaData.getColumnCount(), noOfDefaultColumns + 2);

			CategoryInterface categoryInterface = factory.createCategory();

			CategoryEntityInterface userCategoryEntityInterface = factory.createCategoryEntity();
			userCategoryEntityInterface.setNumberOfEntries(-1);
			userCategoryEntityInterface.setName("UsercategoryEntity");
			userCategoryEntityInterface.setEntity(user);

			userCategoryEntityInterface.setCategory(categoryInterface);
			categoryInterface.setRootCategoryElement(userCategoryEntityInterface);

			CategoryAttributeInterface userCategoryAttributeInterface = factory.createCategoryAttribute();
			userCategoryAttributeInterface.setName("userCategoryAttributeInterface");
			userCategoryAttributeInterface.setAttribute(userNameAttribute);
			userCategoryEntityInterface.addCategoryAttribute(userCategoryAttributeInterface);
			userCategoryAttributeInterface.setCategoryEntity(userCategoryEntityInterface);

			CategoryEntityInterface experimentCategoryEntityInterface = factory.createCategoryEntity();
			experimentCategoryEntityInterface.setEntity(experiment);
			experimentCategoryEntityInterface.setNumberOfEntries(-1);

			CategoryAttributeInterface experimentCategoryAttributeInterface = factory.createCategoryAttribute();
			experimentCategoryAttributeInterface.setName("experimentCategoryAttributeInterface");
			experimentCategoryAttributeInterface.setAttribute(experimentNameAttribute);
			experimentCategoryEntityInterface.addCategoryAttribute(experimentCategoryAttributeInterface);
			experimentCategoryAttributeInterface.setCategoryEntity(experimentCategoryEntityInterface);

			PathInterface pathInterface = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setAssociation(association);
			pathAssociationRelationInterface.setPathSequenceNumber(1);

			PathAssociationRelationInterface pathAssociationRelationInterface2 = factory.createPathAssociationRelation();
			pathAssociationRelationInterface2.setAssociation(association2);
			pathAssociationRelationInterface2.setPathSequenceNumber(2);

			pathAssociationRelationInterface.setPath(pathInterface);
			pathAssociationRelationInterface2.setPath(pathInterface);

			pathInterface.addPathAssociationRelation(pathAssociationRelationInterface);
			pathInterface.addPathAssociationRelation(pathAssociationRelationInterface2);

			experimentCategoryEntityInterface.setPath(pathInterface);
			userCategoryEntityInterface.addChildCategory(experimentCategoryEntityInterface);

			CategoryAssociationInterface categoryAssociationInterface = factory.createCategoryAssociation();
			categoryAssociationInterface.setTargetCategoryEntity(experimentCategoryEntityInterface);
			categoryAssociationInterface.setCategoryEntity(userCategoryEntityInterface);
			userCategoryEntityInterface.getCategoryAssociationCollection().add(categoryAssociationInterface);

			ContainerInterface userContainerInterface = factory.createContainer();
			userContainerInterface.setCaption("User container");
			userContainerInterface.setMainTableCss("formRequiredLabel");
			userContainerInterface.setRequiredFieldIndicatior("*");
			userContainerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
			userContainerInterface.setAbstractEntity(userCategoryEntityInterface);

			int sequenceNumber = 1;
			TextFieldInterface userControlInterface = factory.createTextField();
			userControlInterface.setColumns(50);
			userControlInterface.setCaption("user control");
			userControlInterface.setSequenceNumber(sequenceNumber++);

			userControlInterface.setBaseAbstractAttribute(userCategoryAttributeInterface);
			userControlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(userControlInterface);

			ContainerInterface experimentContainerInterface = factory.createContainer();
			experimentContainerInterface.setAddCaption(false);
			experimentContainerInterface.setAbstractEntity(experimentCategoryEntityInterface);

			TextFieldInterface experimentControlInterface = factory.createTextField();
			experimentControlInterface.setColumns(50);
			experimentControlInterface.setCaption("experiment control");
			experimentControlInterface.setSequenceNumber(sequenceNumber++);

			experimentControlInterface.setBaseAbstractAttribute(experimentCategoryAttributeInterface);
			experimentControlInterface.setParentContainer((Container) experimentContainerInterface);
			experimentContainerInterface.addControl(experimentControlInterface);

			userCategoryEntityInterface.addContaier(userContainerInterface);
			experimentCategoryEntityInterface.addContaier(experimentContainerInterface);

			CategoryAssociationControlInterface categoryAssociationControlInterface = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface.setContainer(experimentContainerInterface);
			categoryAssociationControlInterface.setBaseAbstractAttribute(categoryAssociationInterface);
			categoryAssociationControlInterface.setSequenceNumber(sequenceNumber++);

			categoryAssociationControlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(categoryAssociationControlInterface);

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			CategoryInterface savedCategory = categoryManager.persistCategory(categoryInterface);

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
	 * Entities : user (1)------>(*) study (1) ------>(*) experiment
	 * 
	 * Category: Make 3 category entities, choosing attributes from all entities. 
	 * Insert data for category.
	 */
	public void testCreateCategoryWithThreeCategoryEntities()
	{
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		// create user
		EntityGroupInterface entityGroup = DomainObjectFactory.getInstance().createEntityGroup();
		entityGroup.setName("User" + new Double(Math.random()).toString());

		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) userNameAttribute.getAttributeTypeInformation()).setSize(40);
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) studyNameAttribute.getAttributeTypeInformation()).setSize(40);
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create experiment
		EntityInterface experiment = factory.createEntity();
		AttributeInterface experimentNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation) experimentNameAttribute.getAttributeTypeInformation()).setSize(40);
		experimentNameAttribute.setName("experiment_name");
		experiment.setName("experiment");
		experiment.addAbstractAttribute(experimentNameAttribute);

		// Associate user (1)------ >(*)study
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");

		RoleInterface userSourceRole = getRole(AssociationType.ASSOCIATION, "primaryInvestigator", Cardinality.ONE, Cardinality.ONE);
		userSourceRole.setAssociationsType(AssociationType.CONTAINTMENT);
		association.setSourceRole(userSourceRole);
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO, Cardinality.MANY));

		user.addAbstractAttribute(association);

		// Associate study (1)------ >(*) experiment
		AssociationInterface association2 = factory.createAssociation();

		association2.setTargetEntity(experiment);
		association2.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association2.setName("primaryExperiment");

		RoleInterface studySourceRole = getRole(AssociationType.ASSOCIATION, "primaryExperiment", Cardinality.ONE, Cardinality.ONE);
		studySourceRole.setAssociationsType(AssociationType.CONTAINTMENT);
		association2.setSourceRole(studySourceRole);
		association2.setTargetRole(getRole(AssociationType.ASSOCIATION, "experiment", Cardinality.ZERO, Cardinality.MANY));

		study.addAbstractAttribute(association2);

		try
		{
			entityGroup.addEntity(user);
			user.setEntityGroup(entityGroup);
			entityGroup.addEntity(study);
			study.setEntityGroup(entityGroup);
			entityGroup.addEntity(experiment);
			experiment.setEntityGroup(entityGroup);

			EntityGroupInterface savedUser = entityGroupManager.persistEntityGroup(entityGroup);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from " + study.getTableProperties().getName());
			assertEquals(metaData.getColumnCount(), noOfDefaultColumns + 2);

			CategoryInterface categoryInterface = factory.createCategory();

			CategoryEntityInterface userCategoryEntityInterface = factory.createCategoryEntity();
			userCategoryEntityInterface.setNumberOfEntries(-1);
			userCategoryEntityInterface.setEntity(user);

			userCategoryEntityInterface.setCategory(categoryInterface);
			categoryInterface.setRootCategoryElement(userCategoryEntityInterface);

			CategoryAttributeInterface userCategoryAttributeInterface = factory.createCategoryAttribute();
			userCategoryAttributeInterface.setName("userCategoryAttributeInterface");
			userCategoryAttributeInterface.setAttribute(userNameAttribute);
			userCategoryEntityInterface.addCategoryAttribute(userCategoryAttributeInterface);
			userCategoryAttributeInterface.setCategoryEntity(userCategoryEntityInterface);

			CategoryEntityInterface studyCategoryEntityInterface = factory.createCategoryEntity();
			studyCategoryEntityInterface.setNumberOfEntries(-1);
			studyCategoryEntityInterface.setEntity(study);

			CategoryAttributeInterface studyCategoryAttributeInterface = factory.createCategoryAttribute();
			studyCategoryAttributeInterface.setName("studyCategoryAttributeInterface");
			studyCategoryAttributeInterface.setAttribute(studyNameAttribute);
			studyCategoryEntityInterface.addCategoryAttribute(studyCategoryAttributeInterface);
			studyCategoryAttributeInterface.setCategoryEntity(studyCategoryEntityInterface);

			CategoryEntityInterface experimentCategoryEntityInterface = factory.createCategoryEntity();
			experimentCategoryEntityInterface.setEntity(experiment);
			experimentCategoryEntityInterface.setNumberOfEntries(-1);

			CategoryAttributeInterface experimentCategoryAttributeInterface = factory.createCategoryAttribute();
			experimentCategoryAttributeInterface.setName("experimentCategoryAttributeInterface");
			experimentCategoryAttributeInterface.setAttribute(experimentNameAttribute);
			experimentCategoryEntityInterface.addCategoryAttribute(experimentCategoryAttributeInterface);
			experimentCategoryAttributeInterface.setCategoryEntity(experimentCategoryEntityInterface);

			PathInterface path1 = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setAssociation(association);
			pathAssociationRelationInterface.setPathSequenceNumber(1);

			PathInterface path2 = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelationInterface2 = factory.createPathAssociationRelation();
			pathAssociationRelationInterface2.setAssociation(association2);
			pathAssociationRelationInterface2.setPathSequenceNumber(2);

			pathAssociationRelationInterface.setPath(path1);
			pathAssociationRelationInterface2.setPath(path2);

			path1.addPathAssociationRelation(pathAssociationRelationInterface);
			path2.addPathAssociationRelation(pathAssociationRelationInterface2);

			studyCategoryEntityInterface.setPath(path1);
			experimentCategoryEntityInterface.setPath(path2);

			userCategoryEntityInterface.addChildCategory(studyCategoryEntityInterface);
			studyCategoryEntityInterface.addChildCategory(experimentCategoryEntityInterface);

			CategoryAssociationInterface categoryAssociationInterface = factory.createCategoryAssociation();
			categoryAssociationInterface.setCategoryEntity(userCategoryEntityInterface);
			categoryAssociationInterface.setName("user-study category association");
			categoryAssociationInterface.setTargetCategoryEntity(studyCategoryEntityInterface);
			userCategoryEntityInterface.getCategoryAssociationCollection().add(categoryAssociationInterface);

			CategoryAssociationInterface categoryAssociationInterface2 = factory.createCategoryAssociation();
			categoryAssociationInterface2.setCategoryEntity(studyCategoryEntityInterface);
			categoryAssociationInterface2.setName("study-experiment category association");
			categoryAssociationInterface2.setTargetCategoryEntity(experimentCategoryEntityInterface);
			studyCategoryEntityInterface.getCategoryAssociationCollection().add(categoryAssociationInterface2);

			int sequenceNumber = 1;
			ContainerInterface userContainerInterface = factory.createContainer();
			userContainerInterface.setCaption("User container");
			userContainerInterface.setMainTableCss("formRequiredLabel");
			userContainerInterface.setRequiredFieldIndicatior("*");
			userContainerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
			userContainerInterface.setAbstractEntity(userCategoryEntityInterface);

			TextFieldInterface userControlInterface = factory.createTextField();
			userControlInterface.setColumns(50);
			userControlInterface.setCaption("user control");
			userControlInterface.setSequenceNumber(sequenceNumber++);

			userControlInterface.setBaseAbstractAttribute(userCategoryAttributeInterface);
			userControlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(userControlInterface);

			ContainerInterface studyContainerInterface = factory.createContainer();
			studyContainerInterface.setAddCaption(false);
			studyContainerInterface.setCaption("study container");
			studyContainerInterface.setMainTableCss("formRequiredLabel");
			studyContainerInterface.setRequiredFieldIndicatior("*");
			studyContainerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
			studyContainerInterface.setAbstractEntity(studyCategoryEntityInterface);

			TextFieldInterface studyControlInterface = factory.createTextField();
			studyControlInterface.setColumns(50);
			studyControlInterface.setCaption("study control");
			studyControlInterface.setSequenceNumber(sequenceNumber++);

			studyControlInterface.setBaseAbstractAttribute(studyCategoryAttributeInterface);
			studyControlInterface.setParentContainer((Container) studyContainerInterface);
			studyContainerInterface.addControl(studyControlInterface);

			ContainerInterface experimentContainerInterface = factory.createContainer();
			experimentContainerInterface.setAddCaption(false);
			experimentContainerInterface.setCaption("experiment container");
			experimentContainerInterface.setMainTableCss("formRequiredLabel");
			experimentContainerInterface.setRequiredFieldIndicatior("*");
			experimentContainerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");

			experimentContainerInterface.setAbstractEntity(experimentCategoryEntityInterface);

			TextFieldInterface experimentControlInterface = factory.createTextField();
			experimentControlInterface.setColumns(50);
			experimentControlInterface.setCaption("experiment control");
			experimentControlInterface.setSequenceNumber(sequenceNumber++);

			experimentControlInterface.setBaseAbstractAttribute(experimentCategoryAttributeInterface);
			experimentControlInterface.setParentContainer((Container) experimentContainerInterface);
			experimentContainerInterface.addControl(experimentControlInterface);

			userCategoryEntityInterface.addContaier(userContainerInterface);
			studyCategoryEntityInterface.addContaier(studyContainerInterface);
			experimentCategoryEntityInterface.addContaier(experimentContainerInterface);

			CategoryAssociationControlInterface categoryAssociationControlInterface = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface.setBaseAbstractAttribute(categoryAssociationInterface);
			categoryAssociationControlInterface.setSequenceNumber(sequenceNumber++);

			categoryAssociationControlInterface.setContainer(studyContainerInterface);
			categoryAssociationControlInterface.setParentContainer((Container) userContainerInterface);
			userContainerInterface.addControl(categoryAssociationControlInterface);

			CategoryAssociationControlInterface categoryAssociationControlInterface2 = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface2.setBaseAbstractAttribute(categoryAssociationInterface2);
			categoryAssociationControlInterface2.setSequenceNumber(sequenceNumber++);

			categoryAssociationControlInterface2.setContainer(experimentContainerInterface);
			categoryAssociationControlInterface2.setParentContainer((Container) studyContainerInterface);
			studyContainerInterface.addControl(categoryAssociationControlInterface2);

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			CategoryInterface savedCategory = categoryManager.persistCategory(categoryInterface);

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
	public void testCreateCategoryWithInheritanceBetweenCategoryEntities()
	{
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();

		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroup = factory.createEntityGroup();
		entityGroup.setName("test_" + new Double(Math.random()).toString());
		try
		{
			// baseStudy entity.
			EntityInterface baseSolidTissuePathologyAnnotation = factory.createEntity();
			baseSolidTissuePathologyAnnotation.setName("BaseSolidTissuePathologyAnnotation");
			baseSolidTissuePathologyAnnotation.setAbstract(true);

			AttributeInterface tissueSlide = factory.createStringAttribute();
			((StringAttributeTypeInformation) tissueSlide.getAttributeTypeInformation()).setSize(40);
			tissueSlide.setName("tissueSlide");
			baseSolidTissuePathologyAnnotation.addAbstractAttribute(tissueSlide);

			AttributeInterface tumourTissueSite = factory.createStringAttribute();
			((StringAttributeTypeInformation) tumourTissueSite.getAttributeTypeInformation()).setSize(40);
			tumourTissueSite.setName("tumourTissueSite");
			baseSolidTissuePathologyAnnotation.addAbstractAttribute(tumourTissueSite);

			// study entity.
			EntityInterface prostatePathologyAnnotation = factory.createEntity();
			prostatePathologyAnnotation.setParentEntity(baseSolidTissuePathologyAnnotation);
			prostatePathologyAnnotation.setName("prostatePathologyAnnotation");

			AttributeInterface seminalVesicleInvasion = factory.createStringAttribute();
			((StringAttributeTypeInformation) seminalVesicleInvasion.getAttributeTypeInformation()).setSize(40);
			seminalVesicleInvasion.setName("seminalVesicleInvasion");
			prostatePathologyAnnotation.addAbstractAttribute(seminalVesicleInvasion);

			AttributeInterface periprostaticFatInvasion = factory.createStringAttribute();
			((StringAttributeTypeInformation) periprostaticFatInvasion.getAttributeTypeInformation()).setSize(40);
			periprostaticFatInvasion.setName("periprostaticFatInvasion");
			prostatePathologyAnnotation.addAbstractAttribute(periprostaticFatInvasion);

			EntityInterface gleasonScore = factory.createEntity();
			gleasonScore.setName("GleasonScore");

			AttributeInterface primaryPattern = factory.createStringAttribute();
			((StringAttributeTypeInformation) primaryPattern.getAttributeTypeInformation()).setSize(40);
			primaryPattern.setName("primaryPattern");
			gleasonScore.addAbstractAttribute(primaryPattern);

			AttributeInterface secondaryPattern = factory.createStringAttribute();
			((StringAttributeTypeInformation) secondaryPattern.getAttributeTypeInformation()).setSize(40);
			secondaryPattern.setName("secondaryPattern");
			gleasonScore.addAbstractAttribute(secondaryPattern);

			AssociationInterface prostateGleasonAssociation = factory.createAssociation();

			prostateGleasonAssociation.setTargetEntity(gleasonScore);
			prostateGleasonAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			prostateGleasonAssociation.setName("prostateGleasonAssociation");
			RoleInterface sourceRoleInterface = getRole(AssociationType.ASSOCIATION, "prostatePathologyAnnotation", Cardinality.ONE, Cardinality.ONE);
			sourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
			prostateGleasonAssociation.setSourceRole(sourceRoleInterface);
			prostateGleasonAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "gleasonScore", Cardinality.ZERO, Cardinality.MANY));
			prostatePathologyAnnotation.addAbstractAttribute(prostateGleasonAssociation);

			// experiment entity.
			EntityInterface radicalProstatectomyPathologyAnnotation = factory.createEntity();
			radicalProstatectomyPathologyAnnotation.setName("radicalProstatectomyPathologyAnnotation");
			radicalProstatectomyPathologyAnnotation.setParentEntity(prostatePathologyAnnotation);

			AttributeInterface radicalProstateName = factory.createStringAttribute();
			((StringAttributeTypeInformation) radicalProstateName.getAttributeTypeInformation()).setSize(40);
			radicalProstateName.setName("radicalProstateName");
			radicalProstatectomyPathologyAnnotation.addAttribute(radicalProstateName);

			AttributeInterface radicalProstateType = factory.createStringAttribute();
			((StringAttributeTypeInformation) radicalProstateType.getAttributeTypeInformation()).setSize(40);
			radicalProstateType.setName("radicalProstateType");
			radicalProstatectomyPathologyAnnotation.addAttribute(radicalProstateType);

			EntityInterface radicalProstatectomyMargin = factory.createEntity();
			radicalProstatectomyMargin.setName("BaseSolidTissuePathologyAnnotation");
			radicalProstatectomyMargin.setAbstract(true);

			AttributeInterface focality = factory.createStringAttribute();
			((StringAttributeTypeInformation) focality.getAttributeTypeInformation()).setSize(40);
			focality.setName("focality");
			radicalProstatectomyMargin.addAbstractAttribute(focality);

			AttributeInterface marginalStatus = factory.createStringAttribute();
			((StringAttributeTypeInformation) marginalStatus.getAttributeTypeInformation()).setSize(40);
			marginalStatus.setName("marginalStatus");
			radicalProstatectomyMargin.addAbstractAttribute(marginalStatus);

			AssociationInterface pathologyMarginAssociation = factory.createAssociation();

			pathologyMarginAssociation.setTargetEntity(radicalProstatectomyMargin);
			pathologyMarginAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			pathologyMarginAssociation.setName("pathologyMarginAssociation");
			RoleInterface radicalProstateSourceRoleInterface = getRole(AssociationType.ASSOCIATION, "radicalProstatectomyPathologyAnnotation",
					Cardinality.ONE, Cardinality.ONE);
			radicalProstateSourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
			pathologyMarginAssociation.setSourceRole(radicalProstateSourceRoleInterface);
			pathologyMarginAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "radicalProstatectomyMargin", Cardinality.ZERO,
					Cardinality.MANY));
			radicalProstatectomyPathologyAnnotation.addAbstractAttribute(pathologyMarginAssociation);

			EntityInterface melanomaMargin = factory.createEntity();
			melanomaMargin.setName("melanomaMargin ");

			AttributeInterface melanomaMarginName = factory.createStringAttribute();
			((StringAttributeTypeInformation) melanomaMarginName.getAttributeTypeInformation()).setSize(40);
			melanomaMarginName.setName("melanomaMarginName");
			melanomaMargin.addAbstractAttribute(melanomaMarginName);

			AttributeInterface melanomaMarginType = factory.createStringAttribute();
			((StringAttributeTypeInformation) melanomaMarginType.getAttributeTypeInformation()).setSize(40);
			melanomaMarginType.setName("melanomaMarginType");
			melanomaMargin.addAbstractAttribute(melanomaMarginType);

			AssociationInterface prostateMelanomaAssociation = factory.createAssociation();

			prostateMelanomaAssociation.setTargetEntity(melanomaMargin);
			prostateMelanomaAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			prostateMelanomaAssociation.setName("prostateMelanomaAssociation");
			RoleInterface prostateMelanomaSourceRoleInterface = getRole(AssociationType.ASSOCIATION, "prostateMelanomaSourceRoleInterface",
					Cardinality.ONE, Cardinality.ONE);
			prostateMelanomaSourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
			prostateMelanomaAssociation.setSourceRole(prostateMelanomaSourceRoleInterface);
			prostateMelanomaAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "melanomaMargin", Cardinality.ZERO, Cardinality.MANY));
			radicalProstatectomyMargin.addAbstractAttribute(prostateMelanomaAssociation);
			
			//added
			EntityInterface histologicGrade = factory.createEntity();
			histologicGrade.setName("Histologic Grade");

			AttributeInterface grade = factory.createStringAttribute();
			((StringAttributeTypeInformation) grade.getAttributeTypeInformation()).setSize(40);
			grade.setName("grade");
			histologicGrade.addAbstractAttribute(grade);

			AttributeInterface gradingSystemName = factory.createStringAttribute();
			((StringAttributeTypeInformation) gradingSystemName.getAttributeTypeInformation()).setSize(40);
			gradingSystemName.setName("Grading System Name");
			histologicGrade.addAbstractAttribute(gradingSystemName);

			AssociationInterface melanomaHistologicalAssociation = factory.createAssociation();

			melanomaHistologicalAssociation.setTargetEntity(histologicGrade);
			melanomaHistologicalAssociation.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
			melanomaHistologicalAssociation.setName("Melanoma histological Association");
			RoleInterface melanomaHistologicalSourceRoleInterface = getRole(AssociationType.ASSOCIATION, "MelanomaHistologicalSourceRoleInterface",
					Cardinality.ONE, Cardinality.ONE);
			melanomaHistologicalSourceRoleInterface.setAssociationsType(AssociationType.CONTAINTMENT);
			melanomaHistologicalAssociation.setSourceRole(prostateMelanomaSourceRoleInterface);
			melanomaHistologicalAssociation.setTargetRole(getRole(AssociationType.ASSOCIATION, "melanomaMargin", Cardinality.ZERO, Cardinality.MANY));
			melanomaMargin.addAbstractAttribute(melanomaHistologicalAssociation);
			//finished

			entityGroup.addEntity(baseSolidTissuePathologyAnnotation);
			baseSolidTissuePathologyAnnotation.setEntityGroup(entityGroup);
			entityGroup.addEntity(prostatePathologyAnnotation);
			prostatePathologyAnnotation.setEntityGroup(entityGroup);
			entityGroup.addEntity(gleasonScore);
			gleasonScore.setEntityGroup(entityGroup);
			entityGroup.addEntity(radicalProstatectomyPathologyAnnotation);
			radicalProstatectomyPathologyAnnotation.setEntityGroup(entityGroup);
			entityGroup.addEntity(radicalProstatectomyMargin);
			radicalProstatectomyMargin.setEntityGroup(entityGroup);
			entityGroup.addEntity(melanomaMargin);
			melanomaMargin.setEntityGroup(entityGroup);
			entityGroup.addEntity(histologicGrade);
			histologicGrade.setEntityGroup(entityGroup);

			// Step 2
			entityGroupManager.persistEntityGroup(entityGroup);

					
			CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
			category.setName("Radical prostate category");

			CategoryEntityInterface baseSolidTissuePathologyAnnotationCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			baseSolidTissuePathologyAnnotationCategoryEntity.setName("baseSolidTissuePathologyAnnotationCategoryEntity");
			baseSolidTissuePathologyAnnotationCategoryEntity.setEntity(baseSolidTissuePathologyAnnotation);
			baseSolidTissuePathologyAnnotationCategoryEntity.setNumberOfEntries(-1);

			CategoryAttributeInterface tissueSlideCategoryAttribute = factory.createCategoryAttribute();
			tissueSlideCategoryAttribute.setName("tissueSlideCategoryAttribute");
			tissueSlideCategoryAttribute.setAttribute(tissueSlide);
			baseSolidTissuePathologyAnnotationCategoryEntity.addCategoryAttribute(tissueSlideCategoryAttribute);
			tissueSlideCategoryAttribute.setCategoryEntity(baseSolidTissuePathologyAnnotationCategoryEntity);

			CategoryAttributeInterface tumourTissueSiteCategoryAttribute = factory.createCategoryAttribute();
			tumourTissueSiteCategoryAttribute.setName("tumourTissueSiteCategoryAttribute");
			tumourTissueSiteCategoryAttribute.setAttribute(tumourTissueSite);
			baseSolidTissuePathologyAnnotationCategoryEntity.addCategoryAttribute(tumourTissueSiteCategoryAttribute);
			tumourTissueSiteCategoryAttribute.setCategoryEntity(baseSolidTissuePathologyAnnotationCategoryEntity);

			CategoryEntityInterface prostatePathologyAnnotationCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			prostatePathologyAnnotationCategoryEntity.setName("prostatePathologyAnnotationCategoryEntity");
			prostatePathologyAnnotationCategoryEntity.setEntity(prostatePathologyAnnotation);
			prostatePathologyAnnotationCategoryEntity.setNumberOfEntries(-1);
			prostatePathologyAnnotationCategoryEntity.setParentCategoryEntity(baseSolidTissuePathologyAnnotationCategoryEntity);

			CategoryAttributeInterface seminalVesicleInvasionCategoryAttribute = factory.createCategoryAttribute();
			seminalVesicleInvasionCategoryAttribute.setName("seminalVesicleInvasionCategoryAttribute");
			seminalVesicleInvasionCategoryAttribute.setAttribute(seminalVesicleInvasion);
			prostatePathologyAnnotationCategoryEntity.addCategoryAttribute(seminalVesicleInvasionCategoryAttribute);
			seminalVesicleInvasionCategoryAttribute.setCategoryEntity(prostatePathologyAnnotationCategoryEntity);

			CategoryAttributeInterface periprostaticFatInvasionCategoryAttribute = factory.createCategoryAttribute();
			periprostaticFatInvasionCategoryAttribute.setName("periprostaticFatInvasionCategoryAttribute");
			periprostaticFatInvasionCategoryAttribute.setAttribute(periprostaticFatInvasion);
			prostatePathologyAnnotationCategoryEntity.addCategoryAttribute(periprostaticFatInvasionCategoryAttribute);
			periprostaticFatInvasionCategoryAttribute.setCategoryEntity(prostatePathologyAnnotationCategoryEntity);

			CategoryEntityInterface gleasonScoreCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			gleasonScoreCategoryEntity.setName("gleasonScoreCategoryEntity");
			gleasonScoreCategoryEntity.setEntity(gleasonScore);
			gleasonScoreCategoryEntity.setNumberOfEntries(-1);

			CategoryAttributeInterface primaryPatternCategoryAttribute = factory.createCategoryAttribute();
			primaryPatternCategoryAttribute.setName("primaryPatternCategoryAttribute");
			primaryPatternCategoryAttribute.setAttribute(primaryPattern);
			gleasonScoreCategoryEntity.addCategoryAttribute(primaryPatternCategoryAttribute);
			primaryPatternCategoryAttribute.setCategoryEntity(gleasonScoreCategoryEntity);

			CategoryAttributeInterface secondaryPatternCategoryAttribute = factory.createCategoryAttribute();
			secondaryPatternCategoryAttribute.setName("secondaryPatternCategoryAttribute");
			secondaryPatternCategoryAttribute.setAttribute(secondaryPattern);
			gleasonScoreCategoryEntity.addCategoryAttribute(secondaryPatternCategoryAttribute);
			secondaryPatternCategoryAttribute.setCategoryEntity(gleasonScoreCategoryEntity);

			PathInterface path = factory.createPath();
			PathAssociationRelationInterface pathAssociationRelation = factory.createPathAssociationRelation();
			pathAssociationRelation.setAssociation(prostateGleasonAssociation);
			pathAssociationRelation.setPathSequenceNumber(1);
			pathAssociationRelation.setPath(path);
			path.addPathAssociationRelation(pathAssociationRelation);
			gleasonScoreCategoryEntity.setPath(path);

			CategoryAssociationInterface prostateGleasonCategoryAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			prostateGleasonCategoryAssociation.setName("prostateGleasonAssociationCategoryAssociation");
			prostateGleasonCategoryAssociation.setCategoryEntity(prostatePathologyAnnotationCategoryEntity);
			prostateGleasonCategoryAssociation.setTargetCategoryEntity(gleasonScoreCategoryEntity);
			
			prostatePathologyAnnotationCategoryEntity.getCategoryAssociationCollection().add(prostateGleasonCategoryAssociation);


			prostatePathologyAnnotationCategoryEntity.getChildCategories().add(gleasonScoreCategoryEntity);

			CategoryEntityInterface radicalProstatectomyPathologyAnnotationCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			radicalProstatectomyPathologyAnnotationCategoryEntity.setName("radicalProstatectomyPathologyAnnotationCategoryEntity");
			radicalProstatectomyPathologyAnnotationCategoryEntity.setEntity(radicalProstatectomyPathologyAnnotation);
			radicalProstatectomyPathologyAnnotationCategoryEntity.setNumberOfEntries(-1);
			radicalProstatectomyPathologyAnnotationCategoryEntity.setParentCategoryEntity(prostatePathologyAnnotationCategoryEntity);

			CategoryAttributeInterface radicalProstateNameCategoryAttribute = factory.createCategoryAttribute();
			radicalProstateNameCategoryAttribute.setName("radicalProstateNameCategoryAttribute");
			radicalProstateNameCategoryAttribute.setAttribute(radicalProstateName);
			radicalProstatectomyPathologyAnnotationCategoryEntity.addCategoryAttribute(radicalProstateNameCategoryAttribute);
			radicalProstateNameCategoryAttribute.setCategoryEntity(radicalProstatectomyPathologyAnnotationCategoryEntity);

			CategoryAttributeInterface radicalProstateTypeCategoryAttribute = factory.createCategoryAttribute();
			radicalProstateTypeCategoryAttribute.setName("radicalProstateTypeCategoryAttribute");
			radicalProstateTypeCategoryAttribute.setAttribute(radicalProstateType);
			radicalProstatectomyPathologyAnnotationCategoryEntity.addCategoryAttribute(radicalProstateTypeCategoryAttribute);
			radicalProstateTypeCategoryAttribute.setCategoryEntity(radicalProstatectomyPathologyAnnotationCategoryEntity);

			
			
			CategoryEntityInterface melanomaMarginCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			melanomaMarginCategoryEntity.setName("melanomaMarginCategoryEntity");
			melanomaMarginCategoryEntity.setEntity(melanomaMargin);
			melanomaMarginCategoryEntity.setNumberOfEntries(-1);

			CategoryAttributeInterface melanomaMarginNameCategoryAttribute = factory.createCategoryAttribute();
			melanomaMarginNameCategoryAttribute.setName("melanomaMarginNameCategoryAttribute");
			melanomaMarginNameCategoryAttribute.setAttribute(melanomaMarginName);
			melanomaMarginCategoryEntity.addCategoryAttribute(melanomaMarginNameCategoryAttribute);
			melanomaMarginNameCategoryAttribute.setCategoryEntity(melanomaMarginCategoryEntity);

			CategoryAttributeInterface melanomaMarginTypeCategoryAttribute = factory.createCategoryAttribute();
			melanomaMarginTypeCategoryAttribute.setName("melanomaMarginTypeCategoryAttribute");
			melanomaMarginTypeCategoryAttribute.setAttribute(melanomaMarginType);
			melanomaMarginCategoryEntity.addCategoryAttribute(melanomaMarginTypeCategoryAttribute);
			melanomaMarginTypeCategoryAttribute.setCategoryEntity(melanomaMarginCategoryEntity);

			radicalProstatectomyPathologyAnnotationCategoryEntity.getChildCategories().add(melanomaMarginCategoryEntity);

			CategoryAssociationInterface prostatePathologyMelanomaMarginCategoryAssociation = DomainObjectFactory.getInstance()
					.createCategoryAssociation();
			prostatePathologyMelanomaMarginCategoryAssociation.setName("prostatePathologymelanomaMarginCategoryAssociation");
			prostatePathologyMelanomaMarginCategoryAssociation.setCategoryEntity(radicalProstatectomyPathologyAnnotationCategoryEntity);
			prostatePathologyMelanomaMarginCategoryAssociation.setTargetCategoryEntity(melanomaMarginCategoryEntity);
			radicalProstatectomyPathologyAnnotationCategoryEntity.getCategoryAssociationCollection().add(
					prostatePathologyMelanomaMarginCategoryAssociation);

			PathInterface pathInterface = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setAssociation(pathologyMarginAssociation);
			pathAssociationRelationInterface.setPathSequenceNumber(1);

			PathAssociationRelationInterface pathAssociationRelationInterface2 = factory.createPathAssociationRelation();
			pathAssociationRelationInterface2.setAssociation(prostateMelanomaAssociation);
			pathAssociationRelationInterface2.setPathSequenceNumber(2);

			pathAssociationRelationInterface.setPath(pathInterface);
			pathAssociationRelationInterface2.setPath(pathInterface);

			pathInterface.addPathAssociationRelation(pathAssociationRelationInterface);
			pathInterface.addPathAssociationRelation(pathAssociationRelationInterface2);

			melanomaMarginCategoryEntity.setPath(pathInterface);

			category.setRootCategoryElement(radicalProstatectomyPathologyAnnotationCategoryEntity);
			radicalProstatectomyPathologyAnnotationCategoryEntity.setCategory(category);

			//added
			
			CategoryEntityInterface histologicGradeCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			histologicGradeCategoryEntity.setName("histologicGradeCategoryEntity");
			histologicGradeCategoryEntity.setEntity(histologicGrade);
			histologicGradeCategoryEntity.setNumberOfEntries(-1);

			CategoryAttributeInterface gradeCategoryAttribute = factory.createCategoryAttribute();
			gradeCategoryAttribute.setName("gradeCategoryAttribute");
			gradeCategoryAttribute.setAttribute(grade);
			histologicGradeCategoryEntity.addCategoryAttribute(gradeCategoryAttribute);
			gradeCategoryAttribute.setCategoryEntity(histologicGradeCategoryEntity);

			CategoryAttributeInterface gradingSystemNameCategoryAttribute = factory.createCategoryAttribute();
			gradingSystemNameCategoryAttribute.setName("gradingSystemName");
			gradingSystemNameCategoryAttribute.setAttribute(gradingSystemName);
			histologicGradeCategoryEntity.addCategoryAttribute(gradingSystemNameCategoryAttribute);
			gradingSystemNameCategoryAttribute.setCategoryEntity(histologicGradeCategoryEntity);

			melanomaMarginCategoryEntity.getChildCategories().add(histologicGradeCategoryEntity);

			CategoryAssociationInterface melanomaHistologicalCategoryAssociation = DomainObjectFactory.getInstance()
					.createCategoryAssociation();
			melanomaHistologicalCategoryAssociation.setName("melanomaHistologicalCategoryAssociation");
			melanomaHistologicalCategoryAssociation.setCategoryEntity(melanomaMarginCategoryEntity);
			melanomaHistologicalCategoryAssociation.setTargetCategoryEntity(histologicGradeCategoryEntity);
			melanomaMarginCategoryEntity.getCategoryAssociationCollection().add(
					melanomaHistologicalCategoryAssociation);

			PathInterface pathInterface2 = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelationInterface5 = factory.createPathAssociationRelation();
			pathAssociationRelationInterface5.setAssociation(melanomaHistologicalAssociation);
			pathAssociationRelationInterface5.setPathSequenceNumber(3);

			pathAssociationRelationInterface.setPath(pathInterface);
			pathAssociationRelationInterface2.setPath(pathInterface);

			pathInterface2.addPathAssociationRelation(pathAssociationRelationInterface5);

			histologicGradeCategoryEntity.setPath(pathInterface2);

			category.setRootCategoryElement(radicalProstatectomyPathologyAnnotationCategoryEntity);
			radicalProstatectomyPathologyAnnotationCategoryEntity.setCategory(category);
			
			//finished
			
			
			//step 3: create containers
			int sequenceNumber = 1;

			//container 1 for category entity baseSolidTissuePathologyAnnotationCategoryEntity
			ContainerInterface baseSolidTissuePathologyAnnotationContainer = createContainer(baseSolidTissuePathologyAnnotationCategoryEntity);
			baseSolidTissuePathologyAnnotationContainer.setCaption("Base Solid Tissue Pathology");
			
			TextFieldInterface tissueSlideControl = createTextFieldControl(tissueSlideCategoryAttribute,sequenceNumber++);
			TextFieldInterface tumourTissueSiteControl = createTextFieldControl(tumourTissueSiteCategoryAttribute,sequenceNumber++);
			
			tissueSlideControl.setParentContainer((Container) baseSolidTissuePathologyAnnotationContainer);
			baseSolidTissuePathologyAnnotationContainer.addControl(tissueSlideControl);
			
			tumourTissueSiteControl.setParentContainer((Container) baseSolidTissuePathologyAnnotationContainer);
			baseSolidTissuePathologyAnnotationContainer.addControl(tumourTissueSiteControl);

			//container 2 for category entity prostatePathologyAnnotationCategoryEntity
			ContainerInterface prostatePathologyAnnotationCategoryContainer = createContainer(prostatePathologyAnnotationCategoryEntity);
			prostatePathologyAnnotationCategoryContainer.setCaption("Prostate Pathology Annotation");
			
			TextFieldInterface seminalVesicleInvasionControl = createTextFieldControl(seminalVesicleInvasionCategoryAttribute,sequenceNumber++);
			TextFieldInterface periprostaticFatInvasionControl = createTextFieldControl(periprostaticFatInvasionCategoryAttribute,sequenceNumber++);
			
			seminalVesicleInvasionControl.setParentContainer((Container) prostatePathologyAnnotationCategoryContainer);
			prostatePathologyAnnotationCategoryContainer.addControl(seminalVesicleInvasionControl);
			
			periprostaticFatInvasionControl.setParentContainer((Container) prostatePathologyAnnotationCategoryContainer);
			prostatePathologyAnnotationCategoryContainer.addControl(periprostaticFatInvasionControl);

			//container 3 for category entity gleasonScore
			ContainerInterface gleasonScoreContainer = createContainer(gleasonScoreCategoryEntity);
			gleasonScoreContainer.setCaption("Gleason Score");
			
			
			AbstractContainmentControlInterface  prostateGleasonCategoryContainmentControl = factory.getInstance().createCategoryAssociationControl();
			prostateGleasonCategoryContainmentControl.setBaseAbstractAttribute(prostateGleasonCategoryAssociation);
			prostateGleasonCategoryContainmentControl.setSequenceNumber(sequenceNumber++);
			prostateGleasonCategoryContainmentControl.setCaption("prostateGleasonCategory association");
			prostateGleasonCategoryContainmentControl.setContainer(gleasonScoreContainer);
			prostateGleasonCategoryContainmentControl.setParentContainer((Container)prostatePathologyAnnotationCategoryContainer);
			prostatePathologyAnnotationCategoryContainer.addControl(prostateGleasonCategoryContainmentControl);
			
			TextFieldInterface primaryPatternControl = createTextFieldControl(primaryPatternCategoryAttribute,sequenceNumber++);
			TextFieldInterface secondaryPatternControl = createTextFieldControl(secondaryPatternCategoryAttribute,sequenceNumber++);
			
			primaryPatternControl.setParentContainer((Container) gleasonScoreContainer);
			gleasonScoreContainer.addControl(primaryPatternControl);
			
			secondaryPatternControl.setParentContainer((Container) gleasonScoreContainer);
			gleasonScoreContainer.addControl(secondaryPatternControl);


			//container 4 for category entity baseSolidTissuePathologyAnnotationCategoryEntity
			ContainerInterface radicalProstatectomyPathologyAnnotationContainer = createContainer(radicalProstatectomyPathologyAnnotationCategoryEntity);
			radicalProstatectomyPathologyAnnotationContainer.setCaption("Radical Prostatectomy Pathology Annotation");
			
			TextFieldInterface radicalProstateNameControl = createTextFieldControl(radicalProstateNameCategoryAttribute,sequenceNumber++);
			TextFieldInterface radicalProstateTypeControl = createTextFieldControl(radicalProstateTypeCategoryAttribute,sequenceNumber++);
			
			radicalProstateNameControl.setParentContainer((Container) radicalProstatectomyPathologyAnnotationContainer);
			radicalProstatectomyPathologyAnnotationContainer.addControl(radicalProstateNameControl);
			
			radicalProstateTypeControl.setParentContainer((Container) radicalProstatectomyPathologyAnnotationContainer);
			radicalProstatectomyPathologyAnnotationContainer.addControl(radicalProstateTypeControl);

			//container 5 for category entity baseSolidTissuePathologyAnnotationCategoryEntity
			ContainerInterface melanomaMarginContainer = createContainer(melanomaMarginCategoryEntity);
			melanomaMarginContainer.setCaption("Melanoma Margin");
			
			AbstractContainmentControlInterface  prostatePathologyMelanomaMarginCategoryContainmentControl = factory.getInstance().createCategoryAssociationControl();
			prostatePathologyMelanomaMarginCategoryContainmentControl.setBaseAbstractAttribute(prostatePathologyMelanomaMarginCategoryAssociation);
			prostatePathologyMelanomaMarginCategoryContainmentControl.setSequenceNumber(sequenceNumber++);
			prostatePathologyMelanomaMarginCategoryContainmentControl.setCaption("prostatePathologyMelanomaMargin association");
			prostatePathologyMelanomaMarginCategoryContainmentControl.setContainer(melanomaMarginContainer);
			prostatePathologyMelanomaMarginCategoryContainmentControl.setParentContainer((Container)radicalProstatectomyPathologyAnnotationContainer);
			radicalProstatectomyPathologyAnnotationContainer.addControl(prostatePathologyMelanomaMarginCategoryContainmentControl);
			
			
			TextFieldInterface melanomaMarginNameControl = createTextFieldControl(melanomaMarginNameCategoryAttribute,sequenceNumber++);
			TextFieldInterface melanomaMarginTypeControl = createTextFieldControl(melanomaMarginTypeCategoryAttribute,sequenceNumber++);
			
			melanomaMarginNameControl.setParentContainer((Container) melanomaMarginContainer);
			melanomaMarginContainer.addControl(melanomaMarginNameControl);
			
			melanomaMarginTypeControl.setParentContainer((Container) melanomaMarginContainer);
			melanomaMarginContainer.addControl(melanomaMarginTypeControl);
			
			//container 6 for category entity histologicGradeCategoryEntity
			ContainerInterface histologicGradeContainer = createContainer(histologicGradeCategoryEntity);
			histologicGradeContainer.setCaption("histologic Grade Category Entity");
			
			AbstractContainmentControlInterface  melanomaHistologicalCategoryCategoryContainmentControl = 
				factory.getInstance().createCategoryAssociationControl();
			melanomaHistologicalCategoryCategoryContainmentControl.setBaseAbstractAttribute(melanomaHistologicalCategoryAssociation);
			melanomaHistologicalCategoryCategoryContainmentControl.setSequenceNumber(sequenceNumber++);
			melanomaHistologicalCategoryCategoryContainmentControl.setCaption("melanomaHistologicalCategory association");
			melanomaHistologicalCategoryCategoryContainmentControl.setContainer(histologicGradeContainer);
			melanomaMarginContainer.addControl(melanomaHistologicalCategoryCategoryContainmentControl);
			
			
			TextFieldInterface gradeControl = createTextFieldControl(gradeCategoryAttribute,sequenceNumber++);
			TextFieldInterface gradingSystemNameControl = createTextFieldControl(gradingSystemNameCategoryAttribute,sequenceNumber++);
			
			gradeControl.setParentContainer((Container) histologicGradeContainer);
			histologicGradeContainer.addControl(gradeControl);
			
			gradingSystemNameControl.setParentContainer((Container) histologicGradeContainer);
			histologicGradeContainer.addControl(gradingSystemNameControl);
			
			//container creation is finished
			
			//linking of container started
			prostatePathologyAnnotationCategoryContainer.setBaseContainer(baseSolidTissuePathologyAnnotationContainer);
			radicalProstatectomyPathologyAnnotationContainer.setBaseContainer(prostatePathologyAnnotationCategoryContainer);
			
			
			
			
			
			//linking of container end
			//save container and the category
			categoryManager.persistCategory(category);

			Map<BaseAbstractAttributeInterface, Object> radicalProstateDataCategoryMap = new HashMap<BaseAbstractAttributeInterface, Object>();

			radicalProstateDataCategoryMap.put(tissueSlideCategoryAttribute, "tissueSlideCategoryAttribute");
			radicalProstateDataCategoryMap.put(tumourTissueSiteCategoryAttribute, "tumourTissueSiteCategoryAttribute");
			radicalProstateDataCategoryMap.put(seminalVesicleInvasionCategoryAttribute, "seminalVesicleInvasionCategoryAttribute");
			radicalProstateDataCategoryMap.put(periprostaticFatInvasionCategoryAttribute, "periprostaticFatInvasionCategoryAttribute");

			List<Map<BaseAbstractAttributeInterface, Object>> prostateGleasonCategoryAssociationDataList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();

			Map<BaseAbstractAttributeInterface, Object> prostateGleasonAssociationCategoryAssociationDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			prostateGleasonAssociationCategoryAssociationDataMap.put(primaryPatternCategoryAttribute, "primaryPatternCategoryAttribute");
			prostateGleasonAssociationCategoryAssociationDataMap.put(secondaryPatternCategoryAttribute, "secondaryPatternCategoryAttribute");
			prostateGleasonCategoryAssociationDataList.add(prostateGleasonAssociationCategoryAssociationDataMap);

			radicalProstateDataCategoryMap.put(prostateGleasonCategoryAssociation, prostateGleasonCategoryAssociationDataList);

			radicalProstateDataCategoryMap.put(radicalProstateNameCategoryAttribute, "radicalProstateNameCategoryAttribute");
			radicalProstateDataCategoryMap.put(radicalProstateTypeCategoryAttribute, "radicalProstateTypeCategoryAttribute");

			List<Map<BaseAbstractAttributeInterface, Object>> prostatePathologyMelanomaMarginCategoryAssociationDataList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();
			Map<BaseAbstractAttributeInterface, Object> prostatePathologyMelanomaMarginCategoryAssociationDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			prostatePathologyMelanomaMarginCategoryAssociationDataMap.put(melanomaMarginNameCategoryAttribute, "melanomaMarginNameCategoryAttribute");
			prostatePathologyMelanomaMarginCategoryAssociationDataMap.put(melanomaMarginTypeCategoryAttribute, "melanomaMarginTypeCategoryAttribute");
			prostatePathologyMelanomaMarginCategoryAssociationDataList.add(prostatePathologyMelanomaMarginCategoryAssociationDataMap);

			radicalProstateDataCategoryMap.put(prostatePathologyMelanomaMarginCategoryAssociation,
					prostatePathologyMelanomaMarginCategoryAssociationDataList);

			categoryManager.insertData(category, radicalProstateDataCategoryMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.out.debug(DynamicExtensionsUtility.getStackTrace(e));
			fail();
		}
	}

	private TextFieldInterface createTextFieldControl(CategoryAttributeInterface tissueSlideCategoryAttribute, int sequenceNumber)
	{
		TextFieldInterface textFieldInterface = DomainObjectFactory.getInstance().createTextField();
		textFieldInterface.setBaseAbstractAttribute(tissueSlideCategoryAttribute);
		textFieldInterface.setColumns(50);
		textFieldInterface.setCaption(tissueSlideCategoryAttribute.getName());
		textFieldInterface.setSequenceNumber(sequenceNumber++);
		return textFieldInterface;
	}

	private ContainerInterface createContainer(CategoryEntityInterface baseSolidTissuePathologyAnnotationCategoryEntity)
	{
		ContainerInterface containerInterface = DomainObjectFactory.getInstance().createContainer();
		containerInterface.setAbstractEntity(baseSolidTissuePathologyAnnotationCategoryEntity);
		containerInterface.setMainTableCss("formRequiredLabel");
		containerInterface.setRequiredFieldIndicatior("*");
		containerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
		baseSolidTissuePathologyAnnotationCategoryEntity.addContaier(containerInterface);
		
		return containerInterface;

	}

	/**
	 * Create a new category, some category entities, category attributes and some new entities with attributes.
	 * Add the attributes to category attributes, entities to category entities. Add catrgory attributes
	 * to category entities, category entities to root category entity and root category entity to  category,
	 * and save it to database.
	 * 
	 * 											Category 1
	 * 												|				 
	 * 												|				|---> Category Attribute 1
	 * 										Root Category Entity ---|
	 *          									|				|---> Category Attribute 2
	 *          									|				 	 
	 *          									|
	 *          									|
	 *                  ____________________________|____________________________
	 *     				|	 						|							|							
	 *     				|							|							|							
	 *  		Child Category Entity 1	    Child Category Entity 2	    Child Category Entity 3	    
	 *         			|							|							|						
	 *     		   _____|_____				   _____|_____				   _____|_____				  
	 *    		  |			  |				  |			  |				  |			  |				  
	 *  		Attr 1      Attr 2			Attr 1      Attr 2			Attr 1      Attr 2			      
	 *       
	 */
	public void testCreateCategory1()
	{
		try
		{
			CategoryInterface category = new MockCategoryManager().createCategory();

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();

			if (category != null)
				categoryManager.persistCategory(category);
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
	 * Retrieve a category from database, change its metadata and save it to database.
	 * The metadata information for category with the identifier should get modified.
	 */
	public void testEditExistingCategoryMetadata()
	{
		try
		{
			Object object = null;

			object = new MockCategoryManager().getObjectByIdentifier(Category.class.getName(), 30);

			CategoryInterface category = (Category) object;

			if (category != null)
			{
				category.getRootCategoryElement().getEntity().setName("Entity One");
				category.setName("Category One");
				category.setDescription("This is description for Category One");

				CategoryManagerInterface categoryManager = CategoryManager.getInstance();
				categoryManager.persistCategory(category);
			}
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
	 * Load an existing category from the database and add a new category entity 
	 * and new category attributes to the root category element of the loaded
	 * category.
	 */
	public void testAddCategoryEntityAndCategoryAttributesToExistingCategory()
	{
		try
		{
			Object object = null;

			object = new MockCategoryManager().getObjectByIdentifier(Category.class.getName(), 30);

			CategoryInterface category = (Category) object;

			if (category != null)
			{
				new MockCategoryManager().addNewCategoryEntityToExistingCategory(category);

				CategoryManagerInterface categoryManager = CategoryManager.getInstance();
				categoryManager.persistCategory(category);
			}
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
	 * Load an existing category from the database and remove an existing category entity 
	 * from the root category element and save the category to the database.
	 */
	public void testRemoveCategoryEntityAndCategoryAttributesFromExistingCategory()
	{
		try
		{
			Object object = null;

			object = new MockCategoryManager().getObjectByIdentifier(Category.class.getName(), 30);

			CategoryInterface category = (Category) object;

			if (category != null)
			{
				CategoryEntityInterface rootCategoryEntity = category.getRootCategoryElement();

				CategoryEntityInterface categoryEntityToBeRemoved = null;

				if (rootCategoryEntity.getChildCategories() != null)
				{
					for (CategoryEntityInterface categoryEntity : rootCategoryEntity.getChildCategories())
					{
						if (categoryEntity.getName().equalsIgnoreCase("Child Category Entity 4"))
						{
							categoryEntityToBeRemoved = categoryEntity;
						}
					}
				}

				if (categoryEntityToBeRemoved != null)
					rootCategoryEntity.getChildCategories().remove(categoryEntityToBeRemoved);

				CategoryManagerInterface categoryManager = CategoryManager.getInstance();
				categoryManager.persistCategory(category);
			}
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

	public void testCreateCategoryWithCategoryAssociations()
	{
		try
		{
			CategoryInterface category = new MockCategoryManager().createCategoryWithCategoryAssociations();

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();

			if (category != null)
				categoryManager.persistCategory(category);
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
	 * Create a category, a category entity, category attributes and an entity with 3 attributes.
	 * Add the attributes to category attributes, entity to category entity. Add catrgory attributes
	 * to category entity, category entity to category and save it to database.
	 * Category tree created 
	 * 	categoryInterface => ce
	 * 
	 * 	ce
	 * 	|
	 * 	|_ce1
	 *  	|
	 * 		|_ce2
	 */
	/*public void testCreateCategoryWithContainer()
	 {
	 try
	 {
	 //create entity tree 
	 EntityInterface entity = new MockEntityManager().initializeEntity();
	 DomainObjectFactory factory = DomainObjectFactory.getInstance();

	 AssociationInterface association1 = factory.createAssociation();
	 EntityInterface entity1 = new MockEntityManager().initializeEntity();
	 association1.setTargetEntity(entity1);
	 entity.getAssociationCollection().add(association1);

	 AssociationInterface association2 = factory.createAssociation();
	 EntityInterface entity2 = new MockEntityManager().initializeEntity();
	 association2.setTargetEntity(entity2);
	 entity1.getAssociationCollection().add(association2);

	 //create category tree from above entity tree
	 CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
	 category.setName("C1");
	 category.setCreatedDate(new Date());

	 CategoryEntityInterface categoryEntityInterface = createCategoryEntity(entity);
	 CategoryEntityInterface categoryEntityInterface1 = createCategoryEntity(entity1);
	 categoryEntityInterface.getChildCategories().add((CategoryEntity) categoryEntityInterface1);

	 CategoryEntityInterface categoryEntityInterface2 = createCategoryEntity(entity2);
	 categoryEntityInterface1.getChildCategories().add((CategoryEntity) categoryEntityInterface2);

	 //save category
	 category.setRootCategoryElement((CategoryEntity) categoryEntityInterface);
	 CategoryManagerInterface categoryManager = CategoryManager.getInstance();
	 categoryManager.persistCategory(category);

	 //create conatiner for the category

	 ContainerInterface containerInterface = new MockEntityManager().getContainer("", categoryEntityInterface);
	 ContainerInterface containerInterface1 = new MockEntityManager().getContainer("", categoryEntityInterface1);
	 ContainmentAssociationControlInterface containmentAssociationControl = factory.createContainmentAssociationControl();
	 containerInterface.getControlCollection().add(containmentAssociationControl);
	 containmentAssociationControl.setContainer(containerInterface1);

	 ContainerInterface containerInterface2 = new MockEntityManager().getContainer("", categoryEntityInterface2);
	 ContainmentAssociationControlInterface containmentAssociationControl1 = factory.createContainmentAssociationControl();
	 containerInterface1.getControlCollection().add(containmentAssociationControl1);
	 containmentAssociationControl1.setContainer(containerInterface2);

	 category.setRootCategoryElement((CategoryEntity) categoryEntityInterface);
	 categoryManager.persistCategory(category);
	 }
	 catch (DynamicExtensionsSystemException e)
	 {
	 fail();
	 e.printStackTrace();
	 }
	 catch (DynamicExtensionsApplicationException e)
	 {
	 fail();
	 e.printStackTrace();
	 }
	 }*/

	/**
	 * Create a category with one root category entity, one child category entity and
	 * two category attributes. Create a Path, an Association and a PathAssociationRelation objects with 
	 * their appropriate metadata. Add the path information to the root category entity and save the category.
	 */
	public void testCreateCategoryWithPath()
	{
		try
		{
			CategoryInterface category = new MockCategoryManager().createCategoryWithPath();

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();

			if (category != null)
				categoryManager.persistCategory(category);
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

}

package edu.common.dynamicextensions.categoryManager;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityGroupManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

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
        entityGroup.setName("User"+ new Double(Math.random()).toString());
        
		EntityInterface user = factory.createEntity();
		AttributeInterface userNameAttribute = factory.createStringAttribute();
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);
		((StringAttributeTypeInformation)userNameAttribute.getAttributeTypeInformation()).setSize(40);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);
		((StringAttributeTypeInformation)studyNameAttribute.getAttributeTypeInformation()).setSize(40);

		// Associate user (1)------ >(*)study
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		RoleInterface sourceRoleInterface = getRole(AssociationType.ASSOCIATION, "primaryInvestigator",
				Cardinality.ONE, Cardinality.ONE);
		sourceRoleInterface .setAssociationsType(AssociationType.CONTAINTMENT);
		association.setSourceRole(sourceRoleInterface);
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "study", Cardinality.ZERO,
				Cardinality.MANY));
		user.addAbstractAttribute(association);

		try
		{
			//entityManager.createEntity(study);
            entityGroup.addEntity(user);
            user.setEntityGroup(entityGroup);
            entityGroup.addEntity(study);
            study.setEntityGroup(entityGroup);
			EntityGroupInterface savedUser = entityGroupManager.persistEntityGroup(entityGroup);

			ResultSetMetaData metaData = executeQueryForMetadata("select * from "
					+ study.getTableProperties().getName());
			assertEquals(metaData.getColumnCount(), noOfDefaultColumns + 2);
					
			CategoryInterface categoryInterface = factory.createCategory();
			
			CategoryEntityInterface userCategoryEntityInterface =  factory.createCategoryEntity();
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
			
			CategoryEntityInterface studyCategoryEntityInterface =  factory.createCategoryEntity();
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
			
			controlInterface.setParentContainer((Container)userContainerInterface);
			userContainerInterface.addControl(controlInterface);
						
			ContainerInterface studyContainerInterface = factory.createContainer();
			studyContainerInterface.setAddCaption(false);
			TextFieldInterface studyControlInterface = factory.createTextField();
			studyControlInterface .setBaseAbstractAttribute(studyCategoryAttributeInterface);
			studyControlInterface.setColumns(50);
			studyControlInterface.setCaption("study control");
			studyControlInterface.setSequenceNumber(sequenceNumber++);
			studyCategoryEntityInterface.addContaier(studyContainerInterface);
			
			studyControlInterface.setParentContainer((Container)studyContainerInterface);
			studyContainerInterface.addControl(studyControlInterface);
			
			CategoryAssociationControlInterface categoryAssociationControlInterface = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface.setContainer(studyContainerInterface);
			categoryAssociationControlInterface.setBaseAbstractAttribute(categoryAssociationInterface);
			categoryAssociationControlInterface.setSequenceNumber(sequenceNumber++);
			
			categoryAssociationControlInterface.setParentContainer((Container)userContainerInterface);
			userContainerInterface.addControl(categoryAssociationControlInterface);
			
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			CategoryInterface savedCategory = categoryManager.persistCategory(categoryInterface);
			
			/*Map<BaseAbstractAttributeInterface, Object> categoryDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			categoryDataMap.put(userCategoryAttributeInterface, "100");
			List<Map<BaseAbstractAttributeInterface, Object>> studyDataList = new ArrayList<Map<BaseAbstractAttributeInterface,Object>>();
			Map<BaseAbstractAttributeInterface, Object> studyData = new HashMap<BaseAbstractAttributeInterface, Object>();
			studyData.put(studyCategoryAttributeInterface, "200");
			studyDataList.add(studyData);
			
			categoryDataMap.put(categoryAssociationInterface, studyDataList);
			categoryManager.insertData(savedCategory, categoryDataMap);*/
			
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
		((StringAttributeTypeInformation)userNameAttribute.getAttributeTypeInformation()).setSize(40);
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation)studyNameAttribute.getAttributeTypeInformation()).setSize(40);
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create experiment
		EntityInterface experiment = factory.createEntity();
		AttributeInterface experimentNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation)experimentNameAttribute.getAttributeTypeInformation()).setSize(40);
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
		RoleInterface studyRoleInterface =  getRole(AssociationType.ASSOCIATION, "primaryExperiment", Cardinality.ONE, Cardinality.ONE);
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

			/*Map<BaseAbstractAttributeInterface, Object> categoryDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			categoryDataMap.put(userCategoryAttributeInterface, "100");

			List<Map> dataValueList = new ArrayList<Map>();
			Map map = null;
			for (int i = 0; i < experimentCategoryEntityInterface.getNumberOfEntries(); i++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : experimentCategoryEntityInterface.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				dataValueList.add(map);
			}
			categoryDataMap.put(userCategoryEntityInterface.getCategoryAssociation(), dataValueList);

			categoryManager.insertData(savedCategory, categoryDataMap);*/
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
		((StringAttributeTypeInformation)userNameAttribute.getAttributeTypeInformation()).setSize(40);
		userNameAttribute.setName("user_name");
		user.setName("user");
		user.addAbstractAttribute(userNameAttribute);

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation)studyNameAttribute.getAttributeTypeInformation()).setSize(40);
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// create experiment
		EntityInterface experiment = factory.createEntity();
		AttributeInterface experimentNameAttribute = factory.createStringAttribute();
		((StringAttributeTypeInformation)experimentNameAttribute.getAttributeTypeInformation()).setSize(40);
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

			/*Map<BaseAbstractAttributeInterface, Object> categoryDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			categoryDataMap.put(userCategoryAttributeInterface, "100");

			List<Map> dataValueList = new ArrayList<Map>();
			Map map = null;
			for (int i = 0; i < studyCategoryEntityInterface.getNumberOfEntries(); i++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : studyCategoryEntityInterface.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				List<Map> dataValueList2 = new ArrayList<Map>();
				Map map2 = null;
				for (int j = 0; j < experimentCategoryEntityInterface.getNumberOfEntries(); j++)
				{
					map2 = new HashMap();
					for (CategoryAttributeInterface c : experimentCategoryEntityInterface.getCategoryAttributeCollection())
					{
						map2.put(c, c.getName() + Math.random());
					}
					dataValueList2.add(map2);
				}
				map.put(studyCategoryEntityInterface.getCategoryAssociation(), dataValueList2);
				dataValueList.add(map);
			}

			categoryDataMap.put(userCategoryEntityInterface.getCategoryAssociation(), dataValueList);

			categoryManager.insertData(savedCategory, categoryDataMap);*/
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
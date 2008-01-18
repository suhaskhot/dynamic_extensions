
package edu.common.dynamicextensions.categoryManager;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Path;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
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
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.AbstractMetadataManager;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityGroupManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
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

		// create study
		EntityInterface study = factory.createEntity();
		AttributeInterface studyNameAttribute = factory.createStringAttribute();
		studyNameAttribute.setName("study_name");
		study.setName("study");
		study.addAbstractAttribute(studyNameAttribute);

		// Associate user (1)------ >(*)study
		AssociationInterface association = factory.createAssociation();

		association.setTargetEntity(study);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName("primaryInvestigator");
		association.setSourceRole(getRole(AssociationType.ASSOCIATION, "primaryInvestigator",
				Cardinality.ONE, Cardinality.ONE));
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
			userCategoryEntityInterface.setEntity(user);
			
			userCategoryEntityInterface.setCategory(categoryInterface);
			categoryInterface.setRootCategoryElement(userCategoryEntityInterface);
			
			CategoryAttributeInterface userCategoryAttributeInterface = factory.createCategoryAttribute();
			userCategoryAttributeInterface.setAttribute(userNameAttribute);
			userCategoryEntityInterface.addCategoryAttribute(userCategoryAttributeInterface);
			userCategoryAttributeInterface.setCategoryEntity(userCategoryEntityInterface);
			
			CategoryEntityInterface studyCategoryEntityInterface =  factory.createCategoryEntity();
			studyCategoryEntityInterface.setEntity(study);
			CategoryAttributeInterface studyCategoryAttributeInterface = factory.createCategoryAttribute();
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
			categoryAssociationInterface.setTargetCategoryEntity(studyCategoryEntityInterface);
			userCategoryEntityInterface.setCategoryAssociation(categoryAssociationInterface);
	
			ContainerInterface userContainerInterface = factory.createContainer();
			userContainerInterface.setAbstractEntity(userCategoryEntityInterface);
			userCategoryEntityInterface.addContaier(userContainerInterface);
			
			ControlInterface controlInterface = factory.createTextField();
			controlInterface.setBaseAbstractAttribute(userCategoryAttributeInterface);
			
			controlInterface.setParentContainer((Container)userContainerInterface);
			userContainerInterface.addControl(controlInterface);
						
			ContainerInterface studyContainerInterface = factory.createContainer();
			ControlInterface studyControlInterface = factory.createTextField();
			studyControlInterface .setBaseAbstractAttribute(studyCategoryAttributeInterface);
			studyCategoryEntityInterface.addContaier(studyContainerInterface);
			
			controlInterface.setParentContainer((Container)studyContainerInterface);
			studyContainerInterface.addControl(studyControlInterface);
			
			CategoryAssociationControlInterface categoryAssociationControlInterface = factory.createCategoryAssociationControl();
			categoryAssociationControlInterface.setContainer(studyContainerInterface);
			
			categoryAssociationControlInterface.setParentContainer((Container)userContainerInterface);
			userContainerInterface.addControl(categoryAssociationControlInterface);
			
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			CategoryInterface savedCategory = categoryManager.persistCategory(categoryInterface);
			
			Map<BaseAbstractAttributeInterface, Object> categoryDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			categoryDataMap.put(userCategoryAttributeInterface, "100");
			
			categoryManager.insertData(savedCategory, categoryDataMap);
			
			
			
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

	/**
	 * Study (1) ------> Experiment (*)
	 *
	 */
	public void testTransformCategoryDataMap()
	{
		try
		{
			EntityGroupInterface group = DomainObjectFactory.getInstance().createEntityGroup();
			group.setName("Group 1");

			// Study entity.
			EntityInterface study = DomainObjectFactory.getInstance().createEntity();
			study.setName("Study");

			AbstractAttributeInterface studyAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute1.setName("Study Name");
			studyAttribute1.setEntity(study);

			AbstractAttributeInterface studyAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute2.setName("Study Type");
			studyAttribute2.setEntity(study);

			study.getAbstractAttributeCollection().add(studyAttribute1);
			study.getAbstractAttributeCollection().add(studyAttribute2);

			// Experiment entity.
			EntityInterface experiment = DomainObjectFactory.getInstance().createEntity();
			experiment.setName("Experiment");

			AbstractAttributeInterface experimentAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute1.setName("Experiment Name");
			experimentAttribute1.setEntity(experiment);

			AbstractAttributeInterface experimentAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute2.setName("Experiment Type");
			experimentAttribute2.setEntity(experiment);

			experiment.getAbstractAttributeCollection().add(experimentAttribute1);
			experiment.getAbstractAttributeCollection().add(experimentAttribute2);

			// Study-Experiment association.
			AssociationInterface studyExperimentAssociation = DomainObjectFactory.getInstance().createAssociation();
			studyExperimentAssociation.setName("Study-Experiment association");
			studyExperimentAssociation.setEntity(study);
			studyExperimentAssociation.setTargetEntity(experiment);

			study.getAbstractAttributeCollection().add(studyExperimentAssociation);

			// Create a category.
			CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
			category.setName("Category 1");

			CategoryEntityInterface studyCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			studyCategoryEntity.setName("Study category entity");
			studyCategoryEntity.setEntity(study);

			List<AttributeInterface> studyAttributeCollection = new ArrayList<AttributeInterface>(study.getAttributeCollection());

			CategoryAttributeInterface studyCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			studyCategoryEntityCategoryAttribute1.setName("Study Name");
			studyCategoryEntityCategoryAttribute1.setAttribute(studyAttributeCollection.get(0));
			studyCategoryEntityCategoryAttribute1.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(studyCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface studyCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			studyCategoryEntityCategoryAttribute2.setName("Study Type");
			studyCategoryEntityCategoryAttribute2.setAttribute(studyAttributeCollection.get(1));
			studyCategoryEntityCategoryAttribute2.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(studyCategoryEntityCategoryAttribute2);

			CategoryEntityInterface experimentCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			experimentCategoryEntity.setName("Experiment category entity");
			experimentCategoryEntity.setEntity(experiment);
			experimentCategoryEntity.setNumberOfEntries(1);
			experimentCategoryEntity.setCategory(category);

			List<AttributeInterface> experimentAttributeCollection = new ArrayList<AttributeInterface>(experiment.getAttributeCollection());

			CategoryAttributeInterface experimentCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			experimentCategoryEntityCategoryAttribute1.setName("Experiment Name");
			experimentCategoryEntityCategoryAttribute1.setAttribute(experimentAttributeCollection.get(0));
			experimentCategoryEntityCategoryAttribute1.setCategoryEntity(experimentCategoryEntity);
			experimentCategoryEntity.getCategoryAttributeCollection().add(experimentCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface experimentCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			experimentCategoryEntityCategoryAttribute2.setName("Experiment Type");
			experimentCategoryEntityCategoryAttribute2.setAttribute(experimentAttributeCollection.get(1));
			experimentCategoryEntityCategoryAttribute2.setCategoryEntity(experimentCategoryEntity);
			experimentCategoryEntity.getCategoryAttributeCollection().add(experimentCategoryEntityCategoryAttribute2);

			// Add path information
			PathInterface path = DomainObjectFactory.getInstance().createPath();

			PathAssociationRelationInterface pathAssociationRelation = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation.setAssociation((Association) studyExperimentAssociation);
			pathAssociationRelation.setPathSequenceNumber(1);
			pathAssociationRelation.setPath((Path) path);

			path.getPathAssociationRelationCollection().add(pathAssociationRelation);

			experimentCategoryEntity.setPath(path);

			// Add category association between Study and User category entities.
			CategoryAssociationInterface studyExperimentCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			studyExperimentCategoryEntityAssociation.setName("Study Experiment Category Entity Association");
			studyExperimentCategoryEntityAssociation.setCategoryEntity((CategoryEntity) studyCategoryEntity);
			studyExperimentCategoryEntityAssociation.setCategoryEntity((CategoryEntity) experimentCategoryEntity);
			experimentCategoryEntity.setCategoryAssociation(studyExperimentCategoryEntityAssociation);

			// Add child category entity to root catergory entity.
			studyCategoryEntity.getChildCategories().add(experimentCategoryEntity);

			// Set root category entity for the category.
			category.setRootCategoryElement((CategoryEntity) studyCategoryEntity);

			Map<AbstractMetadataInterface, Object> categoryDataMap = new HashMap<AbstractMetadataInterface, Object>();
			categoryDataMap.put(studyCategoryEntityCategoryAttribute1, "Root Category Attribute 1");
			categoryDataMap.put(studyCategoryEntityCategoryAttribute2, "Root Category Attribute 2");

			List<Map> dataValueList = new ArrayList<Map>();
			Map map = null;
			for (int i = 0; i < experimentCategoryEntity.getNumberOfEntries(); i++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : experimentCategoryEntity.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				dataValueList.add(map);
			}

			categoryDataMap.put(experimentCategoryEntity.getCategoryAssociation(), dataValueList);

			//			Map<AbstractAttributeInterface, Object> entityDataMap = new HashMap<AbstractAttributeInterface, Object>();
			//			entityDataMap = CategoryManager.getInstance().generateEntityDataValueMap(rootCategoryEntity, entityDataMap, categoryDataMap,
			//					new ArrayList<Association>());

			System.out.println("EXITING testTransformCategoryDataMap METHOD");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 3 Entities:			 Study (1) ------> (*) Experiment (1) ------> (*) User
	 * 
	 * While creating category only Study and User chosen as category entities.
	 *
	 */
	public void testTransformCategoryDataMapWithTwoCategoryEntities()
	{
		try
		{
			// Study entity.
			EntityInterface study = DomainObjectFactory.getInstance().createEntity();
			study.setName("Study");

			AbstractAttributeInterface studyAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute1.setName("Study Name");
			studyAttribute1.setEntity(study);

			AbstractAttributeInterface studyAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute2.setName("Study Type");
			studyAttribute2.setEntity(study);

			study.getAbstractAttributeCollection().add(studyAttribute1);
			study.getAbstractAttributeCollection().add(studyAttribute2);

			// Experiment entity.
			EntityInterface experiment = DomainObjectFactory.getInstance().createEntity();
			experiment.setName("Experiment");

			AbstractAttributeInterface experimentAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute1.setName("Experiment Name");
			experimentAttribute1.setEntity(experiment);

			AbstractAttributeInterface experimentAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute2.setName("Experiment Type");
			experimentAttribute2.setEntity(experiment);

			experiment.getAbstractAttributeCollection().add(experimentAttribute1);
			experiment.getAbstractAttributeCollection().add(experimentAttribute2);

			// User entity
			EntityInterface user = DomainObjectFactory.getInstance().createEntity();
			user.setName("User");

			AbstractAttributeInterface userAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			userAttribute1.setName("User Name");
			userAttribute1.setEntity(user);

			AbstractAttributeInterface userAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			userAttribute2.setName("User Type");
			userAttribute2.setEntity(user);

			user.getAbstractAttributeCollection().add(userAttribute1);
			user.getAbstractAttributeCollection().add(userAttribute2);

			// Study-Experiment association.
			AssociationInterface studyExperimentAssociation = DomainObjectFactory.getInstance().createAssociation();
			studyExperimentAssociation.setName("Study-Experiment association");
			studyExperimentAssociation.setEntity(study);
			studyExperimentAssociation.setTargetEntity(experiment);

			study.getAbstractAttributeCollection().add(studyExperimentAssociation);

			// Experiment-User association.
			AssociationInterface experimentUserAssociation = DomainObjectFactory.getInstance().createAssociation();
			experimentUserAssociation.setName("Experiment-User association");
			experimentUserAssociation.setEntity(experiment);
			experimentUserAssociation.setTargetEntity(user);

			experiment.getAbstractAttributeCollection().add(experimentUserAssociation);

			// Create a category.
			CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
			category.setName("Category 1");

			// Add study category entity.
			CategoryEntityInterface studyCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			studyCategoryEntity.setName("Study category entity");
			studyCategoryEntity.setEntity(study);

			List<AttributeInterface> studyAttributeCollection = new ArrayList<AttributeInterface>(study.getAttributeCollection());

			CategoryAttributeInterface rootCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityCategoryAttribute1.setName("Study Name");
			rootCategoryEntityCategoryAttribute1.setAttribute(studyAttributeCollection.get(0));
			rootCategoryEntityCategoryAttribute1.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface rootCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityCategoryAttribute2.setName("Study Type");
			rootCategoryEntityCategoryAttribute2.setAttribute(studyAttributeCollection.get(1));
			rootCategoryEntityCategoryAttribute2.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityCategoryAttribute2);

			// Add user category entity.
			CategoryEntityInterface userCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			userCategoryEntity.setName("User category entity");
			userCategoryEntity.setEntity(user);
			userCategoryEntity.setNumberOfEntries(1);
			userCategoryEntity.setCategory(category);

			List<AttributeInterface> userAttributeCollection = new ArrayList<AttributeInterface>(user.getAttributeCollection());

			CategoryAttributeInterface userCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityCategoryAttribute1.setName("User Name");
			userCategoryEntityCategoryAttribute1.setAttribute(userAttributeCollection.get(0));
			userCategoryEntityCategoryAttribute1.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface userCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityCategoryAttribute2.setName("User Type");
			userCategoryEntityCategoryAttribute2.setAttribute(userAttributeCollection.get(1));
			userCategoryEntityCategoryAttribute2.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityCategoryAttribute2);

			// Add path information.
			PathInterface path = DomainObjectFactory.getInstance().createPath();

			PathAssociationRelationInterface pathAssociationRelation1 = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation1.setAssociation((Association) studyExperimentAssociation);
			pathAssociationRelation1.setPathSequenceNumber(1);
			pathAssociationRelation1.setPath((Path) path);

			PathAssociationRelationInterface pathAssociationRelation2 = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation2.setAssociation((Association) experimentUserAssociation);
			pathAssociationRelation2.setPathSequenceNumber(2);
			pathAssociationRelation2.setPath((Path) path);

			path.getPathAssociationRelationCollection().add(pathAssociationRelation1);
			path.getPathAssociationRelationCollection().add(pathAssociationRelation2);

			userCategoryEntity.setPath(path);

			// Add category association between Study and User category entities.
			CategoryAssociationInterface userCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			userCategoryEntityAssociation.setName("Study User Category Entity Association");
			userCategoryEntityAssociation.setCategoryEntity((CategoryEntity) studyCategoryEntity);
			userCategoryEntityAssociation.setTargetCategoryEntity((CategoryEntity) userCategoryEntity);
			studyCategoryEntity.setCategoryAssociation(userCategoryEntityAssociation);

			// Add child category entity to root catergory entity.
			studyCategoryEntity.getChildCategories().add(userCategoryEntity);

			// Set root category entity for the category.
			category.setRootCategoryElement((CategoryEntity) studyCategoryEntity);

			Map<AbstractMetadataInterface, Object> categoryDataMap = new HashMap<AbstractMetadataInterface, Object>();
			categoryDataMap.put(rootCategoryEntityCategoryAttribute1, "Root Category Attribute 1");
			categoryDataMap.put(rootCategoryEntityCategoryAttribute2, "Root Category Attribute 2");

			List<Map> dataValueList2 = new ArrayList<Map>();
			Map map = null;

			for (int j = 0; j < userCategoryEntity.getNumberOfEntries(); j++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : userCategoryEntity.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				dataValueList2.add(map);
			}

			categoryDataMap.put(userCategoryEntity.getCategoryAssociation(), dataValueList2);

			//			Map<AbstractAttributeInterface, Object> entityDataMap = new HashMap<AbstractAttributeInterface, Object>();
			//			entityDataMap = CategoryManager.getInstance().generateEntityDataValueMap(rootCategoryEntity, entityDataMap, categoryDataMap,
			//					new ArrayList<Association>());

			System.out.println("EXITING testTransformCategoryDataMapWithTwoCategoryEntities METHOD");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 3 Entities:			 Study (1) ------> (*) Experiment (1) ------> (*) User
	 * 
	 * While creating category all three are chosen as category entities.
	 *
	 */
	public void testTransformCategoryDataMapWithThreeCategoryEntities()
	{
		try
		{
			EntityGroupInterface group = DomainObjectFactory.getInstance().createEntityGroup();
			group.setName("Group 1");

			// Study entity.
			EntityInterface study = DomainObjectFactory.getInstance().createEntity();
			study.setName("Study");

			AbstractAttributeInterface studyAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute1.setName("Study Name");
			studyAttribute1.setEntity(study);

			AbstractAttributeInterface studyAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			studyAttribute2.setName("Study Type");
			studyAttribute2.setEntity(study);

			study.getAbstractAttributeCollection().add(studyAttribute1);
			study.getAbstractAttributeCollection().add(studyAttribute2);

			// Experiment entity.
			EntityInterface experiment = DomainObjectFactory.getInstance().createEntity();
			experiment.setName("Experiment");

			AbstractAttributeInterface experimentAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute1.setName("Experiment Name");
			experimentAttribute1.setEntity(experiment);

			AbstractAttributeInterface experimentAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			experimentAttribute2.setName("Experiment Type");
			experimentAttribute2.setEntity(experiment);

			experiment.getAbstractAttributeCollection().add(experimentAttribute1);
			experiment.getAbstractAttributeCollection().add(experimentAttribute2);

			// User entity
			EntityInterface user = DomainObjectFactory.getInstance().createEntity();
			user.setName("User");

			AbstractAttributeInterface userAttribute1 = DomainObjectFactory.getInstance().createStringAttribute();
			userAttribute1.setName("User Name");
			userAttribute1.setEntity(user);

			AbstractAttributeInterface userAttribute2 = DomainObjectFactory.getInstance().createStringAttribute();
			userAttribute2.setName("User Type");
			userAttribute2.setEntity(user);

			user.getAbstractAttributeCollection().add(userAttribute1);
			user.getAbstractAttributeCollection().add(userAttribute2);

			// Study-Experiment association.
			AssociationInterface studyExperimentAssociation = DomainObjectFactory.getInstance().createAssociation();
			studyExperimentAssociation.setName("Study-Experiment association");
			studyExperimentAssociation.setEntity(study);
			studyExperimentAssociation.setTargetEntity(experiment);

			RoleInterface studyExperimentAssociationSourceRole = DomainObjectFactory.getInstance().createRole();
			studyExperimentAssociationSourceRole.setName("Study-Experiment association source Role");
			studyExperimentAssociationSourceRole.setMaximumCardinality(Cardinality.ONE);
			studyExperimentAssociationSourceRole.setAssociationsType(AssociationType.ASSOCIATION);

			RoleInterface studyExperimentAssociationTargetRole = DomainObjectFactory.getInstance().createRole();
			studyExperimentAssociationTargetRole.setName("Study-Experiment association target Role");
			studyExperimentAssociationTargetRole.setMaximumCardinality(Cardinality.MANY);

			studyExperimentAssociation.setSourceRole(studyExperimentAssociationSourceRole);
			studyExperimentAssociation.setTargetRole(studyExperimentAssociationTargetRole);

			study.getAbstractAttributeCollection().add(studyExperimentAssociation);

			// Experiment-User association.
			AssociationInterface experimentUserAssociation = DomainObjectFactory.getInstance().createAssociation();
			experimentUserAssociation.setName("Experiment-User association");
			experimentUserAssociation.setEntity(experiment);
			experimentUserAssociation.setTargetEntity(user);

			RoleInterface experimentUserAssociationSourceRole = DomainObjectFactory.getInstance().createRole();
			experimentUserAssociationSourceRole.setName("Experiment-User association source Role");
			experimentUserAssociationSourceRole.setMaximumCardinality(Cardinality.ONE);
			experimentUserAssociationSourceRole.setAssociationsType(AssociationType.ASSOCIATION);

			RoleInterface experimentUserAssociationTargetRole = DomainObjectFactory.getInstance().createRole();
			experimentUserAssociationTargetRole.setName("Experiment-User association target Role");
			experimentUserAssociationTargetRole.setMaximumCardinality(Cardinality.MANY);

			experimentUserAssociation.setSourceRole(experimentUserAssociationSourceRole);
			experimentUserAssociation.setTargetRole(experimentUserAssociationTargetRole);

			experiment.getAbstractAttributeCollection().add(experimentUserAssociation);

			// Add all entities to group and persist the group.
			group.addEntity(study);
			group.addEntity(experiment);
			group.addEntity(user);

			EntityGroupManager.getInstance().persistEntityGroup(group);

			// Create a category.
			CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
			category.setName("Category 1");

			// Create a study category entity which will be the root category entity.
			CategoryEntityInterface studyCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			studyCategoryEntity.setName("Study category entity");
			studyCategoryEntity.setEntity(study);

			List<AttributeInterface> studyAttributeCollection = new ArrayList<AttributeInterface>(study.getAttributeCollection());

			CategoryAttributeInterface studyCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			studyCategoryEntityCategoryAttribute1.setName("Study Name");
			studyCategoryEntityCategoryAttribute1.setAttribute(studyAttributeCollection.get(0));
			studyCategoryEntityCategoryAttribute1.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(studyCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface studyCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			studyCategoryEntityCategoryAttribute2.setName("Study Type");
			studyCategoryEntityCategoryAttribute2.setAttribute(studyAttributeCollection.get(1));
			studyCategoryEntityCategoryAttribute2.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(studyCategoryEntityCategoryAttribute2);

			// Create a experiment category entity.
			CategoryEntityInterface experimentCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			experimentCategoryEntity.setName("Experiment category entity");
			experimentCategoryEntity.setEntity(experiment);
			experimentCategoryEntity.setNumberOfEntries(1);
			experimentCategoryEntity.setCategory(category);

			List<AttributeInterface> experimentAttributeCollection = new ArrayList<AttributeInterface>(experiment.getAttributeCollection());

			CategoryAttributeInterface experimentCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			experimentCategoryEntityCategoryAttribute1.setName("Experiment Name");
			experimentCategoryEntityCategoryAttribute1.setAttribute(experimentAttributeCollection.get(0));
			experimentCategoryEntityCategoryAttribute1.setCategoryEntity(experimentCategoryEntity);
			experimentCategoryEntity.getCategoryAttributeCollection().add(experimentCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface experimentCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			experimentCategoryEntityCategoryAttribute2.setName("Experiment Type");
			experimentCategoryEntityCategoryAttribute2.setAttribute(experimentAttributeCollection.get(1));
			experimentCategoryEntityCategoryAttribute2.setCategoryEntity(experimentCategoryEntity);
			experimentCategoryEntity.getCategoryAttributeCollection().add(experimentCategoryEntityCategoryAttribute2);

			// Create a user category entity.
			CategoryEntityInterface userCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			userCategoryEntity.setName("User category entity");
			userCategoryEntity.setEntity(user);
			userCategoryEntity.setNumberOfEntries(1);
			userCategoryEntity.setCategory(category);

			List<AttributeInterface> userAttributeCollection = new ArrayList<AttributeInterface>(user.getAttributeCollection());

			CategoryAttributeInterface userCategoryEntityCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityCategoryAttribute1.setName("User Name");
			userCategoryEntityCategoryAttribute1.setAttribute(userAttributeCollection.get(0));
			userCategoryEntityCategoryAttribute1.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityCategoryAttribute1);

			CategoryAttributeInterface userCategoryEntityCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityCategoryAttribute2.setName("User Type");
			userCategoryEntityCategoryAttribute2.setAttribute(userAttributeCollection.get(1));
			userCategoryEntityCategoryAttribute2.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityCategoryAttribute2);

			// Add path information
			PathInterface path = DomainObjectFactory.getInstance().createPath();

			PathAssociationRelationInterface pathAssociationRelation1 = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation1.setAssociation((Association) studyExperimentAssociation);
			pathAssociationRelation1.setPathSequenceNumber(1);
			pathAssociationRelation1.setPath((Path) path);

			PathAssociationRelationInterface pathAssociationRelation2 = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation2.setAssociation((Association) experimentUserAssociation);
			pathAssociationRelation2.setPathSequenceNumber(2);
			pathAssociationRelation2.setPath((Path) path);

			path.getPathAssociationRelationCollection().add(pathAssociationRelation1);
			path.getPathAssociationRelationCollection().add(pathAssociationRelation2);

			experimentCategoryEntity.setPath(path);
			userCategoryEntity.setPath(path);

			// Add category associations.
			CategoryAssociationInterface studyExperimentCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			studyExperimentCategoryEntityAssociation.setName("Study Experiment Category Entity Association");
			studyExperimentCategoryEntityAssociation.setCategoryEntity((CategoryEntity) studyCategoryEntity);
			studyExperimentCategoryEntityAssociation.setTargetCategoryEntity((CategoryEntity) experimentCategoryEntity);
			studyCategoryEntity.setCategoryAssociation(studyExperimentCategoryEntityAssociation);

			CategoryAssociationInterface experimentUserCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			experimentUserCategoryEntityAssociation.setName("Experiment User Category Entity Association");
			experimentUserCategoryEntityAssociation.setCategoryEntity((CategoryEntity) experimentCategoryEntity);
			experimentUserCategoryEntityAssociation.setTargetCategoryEntity((CategoryEntity) userCategoryEntity);
			experimentCategoryEntity.setCategoryAssociation(experimentUserCategoryEntityAssociation);

			// Add child category entities to each root catergory entity.
			studyCategoryEntity.getChildCategories().add(experimentCategoryEntity);
			experimentCategoryEntity.getChildCategories().add(userCategoryEntity);

			// Set root category entity for the category.
			category.setRootCategoryElement((CategoryEntity) studyCategoryEntity);

			Map<BaseAbstractAttributeInterface, Object> categoryDataMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			categoryDataMap.put(studyCategoryEntityCategoryAttribute1, "Root Category Attribute 1");
			categoryDataMap.put(studyCategoryEntityCategoryAttribute2, "Root Category Attribute 2");

			List<Map> dataValueList = new ArrayList<Map>();
			Map map = null;
			for (int i = 0; i < experimentCategoryEntity.getNumberOfEntries(); i++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : experimentCategoryEntity.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				List<Map> dataValueList2 = new ArrayList<Map>();
				Map map2 = null;
				for (int j = 0; j < userCategoryEntity.getNumberOfEntries(); j++)
				{
					map2 = new HashMap();
					for (CategoryAttributeInterface c : userCategoryEntity.getCategoryAttributeCollection())
					{
						map2.put(c, c.getName() + Math.random());
					}
					dataValueList2.add(map2);
					map.put(experimentCategoryEntity.getCategoryAssociation(), dataValueList2);
				}
				dataValueList.add(map);
			}

			categoryDataMap.put(studyCategoryEntity.getCategoryAssociation(), dataValueList);

			//Map<AbstractAttributeInterface, Object> entityDataMap = new HashMap<AbstractAttributeInterface, Object>();
			//Map<AbstractAttributeInterface, Object> entityDataMap = CategoryManager.getInstance().generateEntityDataValueMap(categoryDataMap);

//			EntityManager.getInstance().insertData(rootCategoryEntity.getEntity(), entityDataMap);

			System.out.println("EXITING testTransformCategoryDataMapWithThreeCategoryEntities METHOD");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

}

package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryAssociation;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Path;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.Role;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
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
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
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
	public void testCreateCategory()
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

	/**
	 * Create a simple category with entities that have already been stored to the database.
	 */
	public void testCreateCategoryFromExistingEntities()
	{
		try
		{
			CategoryInterface category = new MockCategoryManager().createCategoryFromExistingEntities();

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
			//CategoryInterface category = new MockCategoryManager().createCategoryWithPath();

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

			CategoryEntityInterface rootCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			rootCategoryEntity.setName("Study category entity");
			rootCategoryEntity.setEntity(study);

			List<AttributeInterface> studyAttributeCollection = new ArrayList<AttributeInterface>(study.getAttributeCollection());

			CategoryAttributeInterface rootCategoryEntityAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityAttribute1.setName("Study Name");
			rootCategoryEntityAttribute1.setAttribute(studyAttributeCollection.get(0));
			rootCategoryEntityAttribute1.setCategoryEntity(rootCategoryEntity);
			rootCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityAttribute1);

			CategoryAttributeInterface rootCategoryEntityAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityAttribute2.setName("Study Type");
			rootCategoryEntityAttribute2.setAttribute(studyAttributeCollection.get(1));
			rootCategoryEntityAttribute2.setCategoryEntity(rootCategoryEntity);
			rootCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityAttribute2);

			CategoryAssociationInterface rootCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			rootCategoryEntityAssociation.setName("Study Category Entity Association");
			rootCategoryEntityAssociation.setCategoryEntity((CategoryEntity) rootCategoryEntity);
			rootCategoryEntity.setCategoryAssociation(rootCategoryEntityAssociation);

			CategoryEntityInterface childCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			childCategoryEntity.setName("Experiment category entity");
			childCategoryEntity.setEntity(experiment);
			childCategoryEntity.setNumberOfEntries(1);
			childCategoryEntity.setCategory(category);

			List<AttributeInterface> experimentAttributeCollection = new ArrayList<AttributeInterface>(experiment.getAttributeCollection());

			CategoryAttributeInterface childCategoryEntityAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			childCategoryEntityAttribute1.setName("Experiment Name");
			childCategoryEntityAttribute1.setAttribute(experimentAttributeCollection.get(0));
			childCategoryEntityAttribute1.setCategoryEntity(childCategoryEntity);
			childCategoryEntity.getCategoryAttributeCollection().add(childCategoryEntityAttribute1);

			CategoryAttributeInterface childCategoryEntityAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			childCategoryEntityAttribute2.setName("Experiment Type");
			childCategoryEntityAttribute2.setAttribute(experimentAttributeCollection.get(1));
			childCategoryEntityAttribute2.setCategoryEntity(childCategoryEntity);
			childCategoryEntity.getCategoryAttributeCollection().add(childCategoryEntityAttribute2);

			CategoryAssociationInterface childCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			childCategoryEntityAssociation.setName("Experiment Category Entity Association");
			childCategoryEntityAssociation.setCategoryEntity((CategoryEntity) childCategoryEntity);
			childCategoryEntity.setCategoryAssociation(childCategoryEntityAssociation);

			// Add path information
			PathInterface path = DomainObjectFactory.getInstance().createPath();
			PathAssociationRelationInterface pathAssociationRelation = DomainObjectFactory.getInstance().createPathAssociationRelation();
			pathAssociationRelation.setAssociation((Association) studyExperimentAssociation);
			pathAssociationRelation.setPathSequenceNumber(1);
			pathAssociationRelation.setPath((Path) path);

			path.getPathAssociationRelationCollection().add(pathAssociationRelation);

			childCategoryEntity.setPath(path);

			rootCategoryEntity.getChildCategories().add(childCategoryEntity);

			category.setRootCategoryElement((CategoryEntity) rootCategoryEntity);

			Map<AbstractMetadataInterface, Object> categoryDataMap = new HashMap<AbstractMetadataInterface, Object>();
			categoryDataMap.put(rootCategoryEntityAttribute1, "Root Category Attribute 1");
			categoryDataMap.put(rootCategoryEntityAttribute2, "Root Category Attribute 2");

			List<Map> dataValueList = new ArrayList<Map>();
			Map map = null;
			for (int i = 0; i < childCategoryEntity.getNumberOfEntries(); i++)
			{
				map = new HashMap();
				for (CategoryAttributeInterface c : childCategoryEntity.getCategoryAttributeCollection())
				{
					map.put(c, c.getName() + Math.random());
				}
				dataValueList.add(map);
			}

			categoryDataMap.put(childCategoryEntity.getCategoryAssociation(), dataValueList);

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

			CategoryEntityInterface studyCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			studyCategoryEntity.setName("Study category entity");
			studyCategoryEntity.setEntity(study);

			List<AttributeInterface> studyAttributeCollection = new ArrayList<AttributeInterface>(study.getAttributeCollection());

			CategoryAttributeInterface rootCategoryEntityAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityAttribute1.setName("Study Name");
			rootCategoryEntityAttribute1.setAttribute(studyAttributeCollection.get(0));
			rootCategoryEntityAttribute1.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityAttribute1);

			CategoryAttributeInterface rootCategoryEntityAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			rootCategoryEntityAttribute2.setName("Study Type");
			rootCategoryEntityAttribute2.setAttribute(studyAttributeCollection.get(1));
			rootCategoryEntityAttribute2.setCategoryEntity(studyCategoryEntity);
			studyCategoryEntity.getCategoryAttributeCollection().add(rootCategoryEntityAttribute2);

			CategoryAssociationInterface rootCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			rootCategoryEntityAssociation.setName("Study Category Entity Association");
			rootCategoryEntityAssociation.setCategoryEntity((CategoryEntity) studyCategoryEntity);
			studyCategoryEntity.setCategoryAssociation(rootCategoryEntityAssociation);

			

			CategoryEntityInterface userCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
			userCategoryEntity.setName("User category entity");
			userCategoryEntity.setEntity(user);
			userCategoryEntity.setNumberOfEntries(1);
			userCategoryEntity.setCategory(category);

			List<AttributeInterface> userAttributeCollection = new ArrayList<AttributeInterface>(user.getAttributeCollection());

			CategoryAttributeInterface userCategoryEntityAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityAttribute1.setName("User Name");
			userCategoryEntityAttribute1.setAttribute(userAttributeCollection.get(0));
			userCategoryEntityAttribute1.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityAttribute1);

			CategoryAttributeInterface userCategoryEntityAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
			userCategoryEntityAttribute2.setName("User Type");
			userCategoryEntityAttribute2.setAttribute(userAttributeCollection.get(1));
			userCategoryEntityAttribute2.setCategoryEntity(userCategoryEntity);
			userCategoryEntity.getCategoryAttributeCollection().add(userCategoryEntityAttribute2);

			CategoryAssociationInterface userCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			userCategoryEntityAssociation.setName("User Category Entity Association");
			userCategoryEntityAssociation.setCategoryEntity((CategoryEntity) userCategoryEntity);
			userCategoryEntity.setCategoryAssociation(userCategoryEntityAssociation);


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
			
			userCategoryEntity.setPath(path);

			studyCategoryEntity.getChildCategories().add(userCategoryEntity);

			category.setRootCategoryElement((CategoryEntity) studyCategoryEntity);

			Map<AbstractMetadataInterface, Object> categoryDataMap = new HashMap<AbstractMetadataInterface, Object>();
			categoryDataMap.put(rootCategoryEntityAttribute1, "Root Category Attribute 1");
			categoryDataMap.put(rootCategoryEntityAttribute2, "Root Category Attribute 2");

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

			// Create an experiment category entity.
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

			// Create an user category entity.
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
			studyExperimentCategoryEntityAssociation.setName("Study Category Entity Association");
			studyExperimentCategoryEntityAssociation.setCategoryEntity((CategoryEntity) studyCategoryEntity);
			studyExperimentCategoryEntityAssociation.setTargetCategoryEntity((CategoryEntity) experimentCategoryEntity);
			studyCategoryEntity.setCategoryAssociation(studyExperimentCategoryEntityAssociation);
			
			CategoryAssociationInterface experimentUserCategoryEntityAssociation = DomainObjectFactory.getInstance().createCategoryAssociation();
			experimentUserCategoryEntityAssociation.setName("Experiment Category Entity Association");
			experimentUserCategoryEntityAssociation.setCategoryEntity((CategoryEntity) experimentCategoryEntity);
			experimentUserCategoryEntityAssociation.setTargetCategoryEntity((CategoryEntity) userCategoryEntity);
			experimentCategoryEntity.setCategoryAssociation(experimentUserCategoryEntityAssociation);	

			// Add child category entities to each root catergory entity.
			studyCategoryEntity.getChildCategories().add(experimentCategoryEntity);
			experimentCategoryEntity.getChildCategories().add(userCategoryEntity);

			// Set root category element of the category.
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
					map.put(userCategoryEntity.getCategoryAssociation(), dataValueList2);
				}
				dataValueList.add(map);
			}

			categoryDataMap.put(experimentCategoryEntity.getCategoryAssociation(), dataValueList);

//			Map<AbstractAttributeInterface, Object> entityDataMap = new HashMap<AbstractAttributeInterface, Object>();
//			entityDataMap = CategoryManager.getInstance().generateEntityDataValueMap(rootCategoryEntity, entityDataMap, categoryDataMap,
//					new ArrayList<Association>());
			
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

package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

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
	 * Create a category, some category entities, category attributes and some entities with attributes.
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
	public void testCreateSimpleCategory()
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
			fail();
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			fail();
			e.printStackTrace();
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
	 categoryEntityInterface.getChildCategories().add((CategoryEntity)categoryEntityInterface1);
	 
	 CategoryEntityInterface categoryEntityInterface2 = createCategoryEntity(entity2);
	 categoryEntityInterface1.getChildCategories().add((CategoryEntity)categoryEntityInterface2);

	 //save category
	 category.setRootCategoryElement((CategoryEntity) categoryEntityInterface);
	 CategoryManagerInterface categoryManager = CategoryManager.getInstance();
	 categoryManager.persistCategory(category);
	 
	 //create conatiner for the category
	 
	 ContainerInterface containerInterface = new MockEntityManager().getContainer("", categoryEntityInterface);
	 ContainerInterface containerInterface1 = new MockEntityManager().getContainer("", categoryEntityInterface1);
	 ContainmentAssociationControlInterface  containmentAssociationControl = factory.createContainmentAssociationControl();
	 containerInterface.getControlCollection().add(containmentAssociationControl);
	 containmentAssociationControl.setContainer(containerInterface1);
	 
	 ContainerInterface containerInterface2 = new MockEntityManager().getContainer("", categoryEntityInterface2);
	 ContainmentAssociationControlInterface  containmentAssociationControl1 = factory.createContainmentAssociationControl();
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
	 }
	 */

	/**
	 * Retrieve a category with some identifier, change its metadata and save it to database.
	 * The metadata information for category with the identifier should get modified.
	 */
	public void testEditCategoryMetadata()
	{
		try
		{
			Object object = null;

			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List objectList = new ArrayList();

			objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 30);

			if (objectList != null && objectList.size() > 0)
			{
				object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
			}

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
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
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

			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List objectList = new ArrayList();

			objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 30);

			if (objectList != null && objectList.size() > 0)
			{
				object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
			}

			CategoryInterface category = (Category) object;

			if (category != null)
			{
				new MockCategoryManager().addNewCategoryEntityToExistingCategory(category);

				CategoryManagerInterface categoryManager = CategoryManager.getInstance();
				categoryManager.persistCategory(category);
			}
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
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

			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List objectList = new ArrayList();

			objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 30);

			if (objectList != null && objectList.size() > 0)
			{
				object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
			}

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
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
	}

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

			CategoryInterface category = new MockCategoryManager().createCategoryWithMultiplePaths();

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();

			if (category != null)
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
	}

	//	/**
	//	 * Add Path information to existing category
	//	 *
	//	 */
	//	public void testAddPathToExistingCategory()
	//	{
	//		try
	//		{
	//			Object object = null;
	//
	//			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
	//			List objectList = new ArrayList();
	//
	//			objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 30);
	//
	//			if (objectList != null && objectList.size() > 0)
	//			{
	//				object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
	//			}
	//
	//			CategoryInterface category = (Category) object;
	//
	//			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
	//			categoryManager.persistCategory(category);
	//		}
	//		catch (DAOException e)
	//		{
	//			e.printStackTrace();
	//		}
	//		catch (DynamicExtensionsSystemException e)
	//		{
	//			e.printStackTrace();
	//		}
	//		catch (DynamicExtensionsApplicationException e)
	//		{
	//			e.printStackTrace();
	//		}
	//	}

	/**
	 * Retrieve a category with some identifier, and delete the same from database.
	 * The category, category entity and category attributes information should get deleted,
	 * but the entity and attributes related metadata should remain intact.
	 */
	public void testDeleteCategory()
	{
		try
		{
			Object object = null;

			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List objectList = new ArrayList();

			objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 30);

			if (objectList != null && objectList.size() > 0)
			{
				object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
			}

			CategoryInterface category = (Category) object;

			CategoryManagerInterface categoryManager = CategoryManager.getInstance();

			if (category != null)
				categoryManager.deleteCategory(category);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}
	}

}
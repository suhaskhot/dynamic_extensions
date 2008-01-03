package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.MockEntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

public class TestCategoryManager extends DynamicExtensionsBaseTestCase {

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
     */
    public void testCreateCategory()
    {
        try
        {
        	DomainObjectFactory factory = DomainObjectFactory.getInstance();
    		EntityGroupInterface entityGroup = factory.createEntityGroup();
    		entityGroup.setName("test_" + new Double(Math.random()).toString());

            CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
            category.setName("C1");
            category.setCreatedDate(new Date());

            // Create root category entity.
            CategoryEntityInterface categoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
            categoryEntity.setCreatedDate(new Date());
            categoryEntity.setName("CE1");

            EntityInterface entity = new MockEntityManager().initializeEntity(entityGroup);
            entity.setName("Entity 1");
            categoryEntity.setEntity((Entity) entity);

            List<AttributeInterface> attributeCollection = new ArrayList<AttributeInterface>(entity.getAttributeCollection());

            CategoryAttributeInterface categoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute1.setCreatedDate(new Date());
            categoryAttribute1.setName("C A 1");
            categoryAttribute1.setAttribute(attributeCollection.get(0));
            categoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute1);

            CategoryAttributeInterface categoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute2.setCreatedDate(new Date());
            categoryAttribute2.setName("C A 2");
            categoryAttribute2.setAttribute(attributeCollection.get(1));
            categoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute2);

            // Create another category entity.
            CategoryEntityInterface categoryEntityA = DomainObjectFactory.getInstance().createCategoryEntity();
            categoryEntityA.setCreatedDate(new Date());
            categoryEntityA.setName("categoryEntityA");

            EntityInterface entity2 = new MockEntityManager().initializeEntity(entityGroup);
            entity2.setName("Entity 2");
            categoryEntityA.setEntity((Entity) entity2);

            List<AttributeInterface> attributeCollection2 = new ArrayList<AttributeInterface>(entity2.getAttributeCollection());

            CategoryAttributeInterface categoryAttribute11 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute11.setCreatedDate(new Date());
            categoryAttribute11.setName("C A 1");
            categoryAttribute11.setAttribute(attributeCollection2.get(0));
            categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute11);

            CategoryAttributeInterface categoryAttribute22 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute22.setCreatedDate(new Date());
            categoryAttribute22.setName("C A 2");
            categoryAttribute22.setAttribute(attributeCollection2.get(1));
            categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute22);

            // Create another category entity.
            CategoryEntityInterface categoryEntityB = DomainObjectFactory.getInstance().createCategoryEntity();
            categoryEntityB.setCreatedDate(new Date());
            categoryEntityB.setName("categoryEntityB");

            EntityInterface entity3 = new MockEntityManager().initializeEntity(entityGroup);
            entity3.setName("Entity 3");
            categoryEntityB.setEntity((Entity) entity3);

            List<AttributeInterface> attributeCollection3 = new ArrayList<AttributeInterface>(entity3.getAttributeCollection());

            CategoryAttributeInterface categoryAttribute111 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute111.setCreatedDate(new Date());
            categoryAttribute111.setName("C A 1");
            categoryAttribute111.setAttribute(attributeCollection3.get(0));
            categoryEntityB.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute111);

            CategoryAttributeInterface categoryAttribute222 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute222.setCreatedDate(new Date());
            categoryAttribute222.setName("C A 2");
            categoryAttribute222.setAttribute(attributeCollection3.get(1));
            categoryEntityB.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute222);

            // Create another category entity.
            CategoryEntityInterface categoryEntityC = DomainObjectFactory.getInstance().createCategoryEntity();
            categoryEntityC.setCreatedDate(new Date());
            categoryEntityC.setName("categoryEntityB");

            EntityInterface entity4 = new MockEntityManager().initializeEntity(entityGroup);
            entity4.setName("Entity 4");
            categoryEntityC.setEntity((Entity) entity4);

            List<AttributeInterface> attributeCollection4 = new ArrayList<AttributeInterface>(entity4.getAttributeCollection());

            CategoryAttributeInterface categoryAttribute1111 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute1111.setCreatedDate(new Date());
            categoryAttribute1111.setName("C A 1");
            categoryAttribute1111.setAttribute(attributeCollection4.get(0));
            categoryEntityC.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute1111);

            CategoryAttributeInterface categoryAttribute2222 = DomainObjectFactory.getInstance().createCategoryAttribute();
            categoryAttribute2222.setCreatedDate(new Date());
            categoryAttribute2222.setName("C A 2");
            categoryAttribute2222.setAttribute(attributeCollection4.get(1));
            categoryEntityC.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute2222);

            // Add child category entities to root category entity.
            categoryEntity.getChildCategories().add((CategoryEntity)categoryEntityA);
            categoryEntity.getChildCategories().add((CategoryEntity)categoryEntityB);
            categoryEntity.getChildCategories().add((CategoryEntity)categoryEntityC);

            // Set root category element of the category.
            category.setRootCategoryElement((CategoryEntity) categoryEntity);

            CategoryManagerInterface categoryManager = CategoryManager.getInstance();
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
//    public void testEditCategory()
//    {
//        try
//        {
//            Object object = null;
//
//            DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
//            List objectList = new ArrayList();
//
//            objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 8);
//
//            if (objectList != null && objectList.size() > 0)
//            {
//                object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
//            }
//
//            CategoryInterface category = (Category) object;
//            category.getRootCategoryElement().getEntity().setName("Entity 1111");
//            category.setName("Category 1");
//            category.setDescription("This is description for category1");
//
//            CategoryManagerInterface categoryManager = CategoryManager.getInstance();
//
//            categoryManager.persistCategory(category);
//        }
//        catch (DAOException e)
//        {
//            e.printStackTrace();
//        }
//        catch (DynamicExtensionsSystemException e)
//        {
//            e.printStackTrace();
//        }
//        catch (DynamicExtensionsApplicationException e)
//        {
//            e.printStackTrace();
//        }
//    }

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

            objectList = defaultBizLogic.retrieve(Category.class.getName(), "id", 8);

            if (objectList != null && objectList.size() > 0)
            {
                object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
            }

            CategoryInterface category = (Category) object;

            CategoryManagerInterface categoryManager = CategoryManager.getInstance();

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
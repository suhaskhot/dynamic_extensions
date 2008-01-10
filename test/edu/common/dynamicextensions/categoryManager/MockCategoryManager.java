
package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.Path;
import edu.common.dynamicextensions.domain.PathAssociationRelation;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.entitymanager.MockEntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * 
 * @author mandar_shidhore
 *
 */
public class MockCategoryManager
{

	/**
	 * Create following hierarchy.
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
	public CategoryInterface createCategory() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroup = factory.createEntityGroup();
		entityGroup.setName("Entity Group " + new Double(Math.random()).toString());
		entityGroup.setCreatedDate(new Date());
		entityGroup.setDescription("This is a description for entity group");

		CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
		category.setName("Category 1");
		category.setCreatedDate(new Date());

		// Create root category entity.
		CategoryEntityInterface rootCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
		rootCategoryEntity.setCreatedDate(new Date());
		rootCategoryEntity.setName("Root Category Entity");

		EntityInterface entity = new MockEntityManager().initializeEntity(entityGroup);
		entity.setName("Entity 1");
		rootCategoryEntity.setEntity((Entity) entity);

		List<AttributeInterface> attributeCollection = new ArrayList<AttributeInterface>(entity.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute1.setCreatedDate(new Date());
		categoryAttribute1.setName("Category Attribute 1");
		categoryAttribute1.setAttribute(attributeCollection.get(0));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute1);

		CategoryAttributeInterface categoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute2.setCreatedDate(new Date());
		categoryAttribute2.setName("Category Attribute 2");
		categoryAttribute2.setAttribute(attributeCollection.get(1));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute2);

		// Create another category entity.
		CategoryEntityInterface categoryEntityA = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntityA.setCreatedDate(new Date());
		categoryEntityA.setName("Child Category Entity 1");

		EntityInterface entity2 = new MockEntityManager().initializeEntity(entityGroup);
		entity2.setName("Entity 2");
		categoryEntityA.setEntity((Entity) entity2);

		List<AttributeInterface> attributeCollection2 = new ArrayList<AttributeInterface>(entity2.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute11 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute11.setCreatedDate(new Date());
		categoryAttribute11.setName("Attr 1");
		categoryAttribute11.setAttribute(attributeCollection2.get(0));
		categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute11);

		CategoryAttributeInterface categoryAttribute22 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute22.setCreatedDate(new Date());
		categoryAttribute22.setName("Attr 2");
		categoryAttribute22.setAttribute(attributeCollection2.get(1));
		categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute22);

		// Create another category entity.
		CategoryEntityInterface categoryEntityB = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntityB.setCreatedDate(new Date());
		categoryEntityB.setName("Child Category Entity 2");

		EntityInterface entity3 = new MockEntityManager().initializeEntity(entityGroup);
		entity3.setName("Entity 3");
		categoryEntityB.setEntity((Entity) entity3);

		List<AttributeInterface> attributeCollection3 = new ArrayList<AttributeInterface>(entity3.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute111 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute111.setCreatedDate(new Date());
		categoryAttribute111.setName("Attr 1");
		categoryAttribute111.setAttribute(attributeCollection3.get(0));
		categoryEntityB.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute111);

		CategoryAttributeInterface categoryAttribute222 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute222.setCreatedDate(new Date());
		categoryAttribute222.setName("Attr 2");
		categoryAttribute222.setAttribute(attributeCollection3.get(1));
		categoryEntityB.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute222);

		// Create another category entity.
		CategoryEntityInterface categoryEntityC = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntityC.setCreatedDate(new Date());
		categoryEntityC.setName("Child Category Entity 3");

		EntityInterface entity4 = new MockEntityManager().initializeEntity(entityGroup);
		entity4.setName("Entity 4");
		categoryEntityC.setEntity((Entity) entity4);

		List<AttributeInterface> attributeCollection4 = new ArrayList<AttributeInterface>(entity4.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute1111 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute1111.setCreatedDate(new Date());
		categoryAttribute1111.setName("Attr 1");
		categoryAttribute1111.setAttribute(attributeCollection4.get(0));
		categoryEntityC.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute1111);

		CategoryAttributeInterface categoryAttribute2222 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute2222.setCreatedDate(new Date());
		categoryAttribute2222.setName("Attr 2");
		categoryAttribute2222.setAttribute(attributeCollection4.get(1));
		categoryEntityC.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute2222);

		// Add child category entities to root category entity.
		rootCategoryEntity.getChildCategories().add((CategoryEntity) categoryEntityA);
		rootCategoryEntity.getChildCategories().add((CategoryEntity) categoryEntityB);
		rootCategoryEntity.getChildCategories().add((CategoryEntity) categoryEntityC);

		// Set root category element of the category.
		category.setRootCategoryElement((CategoryEntity) rootCategoryEntity);

		return category;
	}

	/**
	 * Create a category with one root category entity, one child category entity and
	 * two category attributes. Create a Path, an Association and a PathAssociationRelation objects with 
	 * their appropriate metadata. Add the path information to the root category entity and save the category.
	 * @return category
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryInterface createCategoryWithPath1() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroup = factory.createEntityGroup();
		entityGroup.setName("Entity Group " + new Double(Math.random()).toString());
		entityGroup.setCreatedDate(new Date());
		entityGroup.setDescription("This is a description for entity group");

		CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
		category.setName("Category 1");
		category.setCreatedDate(new Date());

		// Create root category entity.
		CategoryEntityInterface rootCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
		rootCategoryEntity.setCreatedDate(new Date());
		rootCategoryEntity.setName("Root Category Entity");

		EntityInterface entity = new MockEntityManager().initializeEntity(entityGroup);
		entity.setName("Entity 1");
		rootCategoryEntity.setEntity((Entity) entity);

		EntityInterface entityTwo = new MockEntityManager().initializeEntity(entityGroup);
		entity.setName("Entity 2");

		AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
		association.setCreatedDate(new Date());
		association.setName("Association 1");
		association.setEntity(entity);
		association.setTargetEntity(entityTwo);

		entity.getAbstractAttributeCollection().add(association);

		PathInterface path = DomainObjectFactory.getInstance().createPath();
		PathAssociationRelationInterface pathAssociationRelation = DomainObjectFactory.getInstance().createPathAssociationRelation();
		pathAssociationRelation.setAssociation((Association) association);
		pathAssociationRelation.setPath((Path) path);
		pathAssociationRelation.setPathSequenceNumber(1);
		
		path.getPathAssociationRelationCollection().add(pathAssociationRelation);

		association.getPathAssociationRelationColletion().add(pathAssociationRelation);

		rootCategoryEntity.setPath(path);

		List<AttributeInterface> attributeCollection = new ArrayList<AttributeInterface>(entity.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute1.setCreatedDate(new Date());
		categoryAttribute1.setName("Category Attribute 1");
		categoryAttribute1.setAttribute(attributeCollection.get(0));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute1);

		CategoryAttributeInterface categoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute2.setCreatedDate(new Date());
		categoryAttribute2.setName("Category Attribute 2");
		categoryAttribute2.setAttribute(attributeCollection.get(1));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute2);

		// Create another category entity.
		CategoryEntityInterface categoryEntityA = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntityA.setCreatedDate(new Date());
		categoryEntityA.setName("Child Category Entity 1");

		EntityInterface entity2 = new MockEntityManager().initializeEntity(entityGroup);
		entity2.setName("Entity 2");
		categoryEntityA.setEntity((Entity) entity2);

		List<AttributeInterface> attributeCollection2 = new ArrayList<AttributeInterface>(entity2.getAttributeCollection());

		CategoryAttributeInterface categoryAttribute11 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute11.setCreatedDate(new Date());
		categoryAttribute11.setName("Attr 1");
		categoryAttribute11.setAttribute(attributeCollection2.get(0));
		categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute11);

		CategoryAttributeInterface categoryAttribute22 = DomainObjectFactory.getInstance().createCategoryAttribute();
		categoryAttribute22.setCreatedDate(new Date());
		categoryAttribute22.setName("Attr 2");
		categoryAttribute22.setAttribute(attributeCollection2.get(1));
		categoryEntityA.getCategoryAttributeCollection().add((CategoryAttribute) categoryAttribute22);

		// Add child category entities to root category entity.
		rootCategoryEntity.getChildCategories().add((CategoryEntity) categoryEntityA);

		// Set root category element of the category.
		category.setRootCategoryElement((CategoryEntity) rootCategoryEntity);

		return category;
	}

	/**
	 * Create a category with one root category entity, one child category entity and
	 * two category attributes. Create multiple Paths, Associations and PathAssociationRelation objects with 
	 * their appropriate metadata. Add the path information to the root category entity and save the category.
	 * 
	 * Three entities : Entity 1, Entity 2, Entity 3
	 * 
	 * We can have 2 different paths:
	 * 
	 * 							Path 1:			Entity 1 ------> Entity 2 ------> Entity 3	
	 * 	
	 * 																OR
	 * 	
	 * 							Path 2:					Entity 1 ------> Entity 3
	 * 
	 * @return category
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryInterface createCategoryWithPath() throws DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupInterface entityGroup = factory.createEntityGroup();
		entityGroup.setName("Entity Group " + new Double(Math.random()).toString());
		entityGroup.setCreatedDate(new Date());
		entityGroup.setDescription("This is a description for entity group");

		CategoryInterface category = DomainObjectFactory.getInstance().createCategory();
		category.setName("Category 1");
		category.setCreatedDate(new Date());

		// Create root category entity.
		CategoryEntityInterface rootCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
		rootCategoryEntity.setCreatedDate(new Date());
		rootCategoryEntity.setName("Root Category Entity");

		EntityInterface entity = new MockEntityManager().initializeEntity(entityGroup);
		entity.setName("Entity 1");
		rootCategoryEntity.setEntity((Entity) entity);
		
		List<AttributeInterface> attributeCollection = new ArrayList<AttributeInterface>(entity.getAttributeCollection());

		CategoryAttributeInterface rootCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
		rootCategoryAttribute1.setCreatedDate(new Date());
		rootCategoryAttribute1.setName("Category Attribute 1");
		rootCategoryAttribute1.setAttribute(attributeCollection.get(0));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) rootCategoryAttribute1);

		CategoryAttributeInterface rootCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
		rootCategoryAttribute2.setCreatedDate(new Date());
		rootCategoryAttribute2.setName("Category Attribute 2");
		rootCategoryAttribute2.setAttribute(attributeCollection.get(1));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) rootCategoryAttribute2);

		EntityInterface entityTwo = new MockEntityManager().initializeEntity(entityGroup);
		entityTwo.setName("Entity 2");

		// Create another category entity.
		CategoryEntityInterface chilCategoryEntity = DomainObjectFactory.getInstance().createCategoryEntity();
		chilCategoryEntity.setCreatedDate(new Date());
		chilCategoryEntity.setName("Child Category Entity 1");

		EntityInterface entityThree = new MockEntityManager().initializeEntity(entityGroup);
		entityThree.setName("Entity 3");
		chilCategoryEntity.setEntity((Entity) entityThree);

		List<AttributeInterface> attributeCollection2 = new ArrayList<AttributeInterface>(entityThree.getAttributeCollection());

		CategoryAttributeInterface childCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
		childCategoryAttribute1.setCreatedDate(new Date());
		childCategoryAttribute1.setName("Attr 1");
		childCategoryAttribute1.setAttribute(attributeCollection2.get(0));
		chilCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryAttribute1);

		CategoryAttributeInterface childCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
		childCategoryAttribute2.setCreatedDate(new Date());
		childCategoryAttribute2.setName("Attr 2");
		childCategoryAttribute2.setAttribute(attributeCollection2.get(1));
		chilCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryAttribute2);
		
		// Create association between Entity 1 and Entity 2
		AssociationInterface association1 = DomainObjectFactory.getInstance().createAssociation();
		association1.setCreatedDate(new Date());
		association1.setName("Association 1");
		association1.setEntity(entity);
		association1.setTargetEntity(entityTwo);
		
		entity.getAbstractAttributeCollection().add(association1);
		
		// Create association between Entity 2 and Entity 3
		AssociationInterface association2 = DomainObjectFactory.getInstance().createAssociation();
		association2.setCreatedDate(new Date());
		association2.setName("Association 2");
		association2.setEntity(entityTwo);
		association2.setTargetEntity(entityThree);
		
		entityTwo.getAbstractAttributeCollection().add(association2);
		
		// Create association between Entity 1 and Entity 3
		AssociationInterface association3 = DomainObjectFactory.getInstance().createAssociation();
		association3.setCreatedDate(new Date());
		association3.setName("Association 3");
		association3.setEntity(entity);
		association3.setTargetEntity(entityThree);
		
		entity.getAbstractAttributeCollection().add(association3);

		PathInterface path1 = DomainObjectFactory.getInstance().createPath();
		
		PathAssociationRelationInterface pathAssociationRelation1 = DomainObjectFactory.getInstance().createPathAssociationRelation();
		pathAssociationRelation1.setAssociation((Association) association1);
		pathAssociationRelation1.setPath((Path) path1);
		pathAssociationRelation1.setPathSequenceNumber(1);
		association1.getPathAssociationRelationColletion().add(pathAssociationRelation1);
		
		PathAssociationRelationInterface pathAssociationRelation2 = DomainObjectFactory.getInstance().createPathAssociationRelation();
		pathAssociationRelation2.setAssociation((Association) association2);
		pathAssociationRelation2.setPath((Path) path1);
		pathAssociationRelation2.setPathSequenceNumber(2);
		association2.getPathAssociationRelationColletion().add(pathAssociationRelation2);
		
		path1.getPathAssociationRelationCollection().add(pathAssociationRelation1);
		path1.getPathAssociationRelationCollection().add(pathAssociationRelation2);
		
		// Add all paths to root category element.
		rootCategoryEntity.setPath(path1);
		
		// Add child category entities to root category entity.
		rootCategoryEntity.getChildCategories().add((CategoryEntity) chilCategoryEntity);

		// Set root category element of the category.
		category.setRootCategoryElement((CategoryEntity) rootCategoryEntity);

		return category;
	}

	/**
	 * Create a new category entity with two category attributes and add it 
	 * to root category entity of the category.
	 * @param category
	 * @return category entity
	 * @throws DynamicExtensionsApplicationException
	 */
	public CategoryEntityInterface addNewCategoryEntityToExistingCategory(CategoryInterface category) throws DynamicExtensionsApplicationException
	{
		CategoryEntityInterface categoryEntityNew = DomainObjectFactory.getInstance().createCategoryEntity();
		categoryEntityNew.setCreatedDate(new Date());
		categoryEntityNew.setName("Child Category Entity 4");

		EntityInterface entity5 = new MockEntityManager().initializeEntity(category.getRootCategoryElement().getEntity().getEntityGroup());
		entity5.setName("Entity 5");
		categoryEntityNew.setEntity((Entity) entity5);

		List<AttributeInterface> attributeCollection4 = new ArrayList<AttributeInterface>(entity5.getAttributeCollection());

		CategoryAttributeInterface newCategoryAttribute1 = DomainObjectFactory.getInstance().createCategoryAttribute();
		newCategoryAttribute1.setCreatedDate(new Date());
		newCategoryAttribute1.setName("Attr 15");
		newCategoryAttribute1.setAttribute(attributeCollection4.get(0));
		categoryEntityNew.getCategoryAttributeCollection().add((CategoryAttribute) newCategoryAttribute1);

		CategoryAttributeInterface newCategoryAttribute2 = DomainObjectFactory.getInstance().createCategoryAttribute();
		newCategoryAttribute2.setCreatedDate(new Date());
		newCategoryAttribute2.setName("Attr 25");
		newCategoryAttribute2.setAttribute(attributeCollection4.get(1));
		categoryEntityNew.getCategoryAttributeCollection().add((CategoryAttribute) newCategoryAttribute2);

		CategoryEntityInterface rootCategoryEntity = category.getRootCategoryElement();
		rootCategoryEntity.getChildCategories().add(categoryEntityNew);

		return categoryEntityNew;
	}

}
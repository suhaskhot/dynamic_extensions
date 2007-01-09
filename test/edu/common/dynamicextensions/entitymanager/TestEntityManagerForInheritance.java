
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Rahul Ner
 * @author Vishvesh Mulay
 */
public class TestEntityManagerForInheritance extends DynamicExtensionsBaseTestCase
{

	/**
	 *  PURPOSE: This method tests for metadata save of the inheriatance. 
	 *
	 *  EXPECTED BEHAVIOUR: All the hierarchy should get saved 
	 *  
	 *  TEST CASE FLOW: 1. Create Specimen
	 *                  2. Create TissueSpecimen and set parent as Specimen
	 *                  3. persist TissueSpecimen                       
	 *                  6. Check if the parent of TissueSpecimen and children of Specimen
	 */

	public void testInheritanceMetadataSave()
	{

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		try
		{
			EntityInterface specimen = factory.createEntity();
			specimen.setName("specimen");
			specimen.setAbstract(true);
			AttributeInterface barcode = factory.createStringAttribute();
			barcode.setName("barcode");
			specimen.addAbstractAttribute(barcode);
			
			

			EntityInterface tissueSpecimen = factory.createEntity();
			tissueSpecimen.setName("tissueSpecimen");
			AttributeInterface quantityInCellCount = factory.createIntegerAttribute();
			quantityInCellCount.setName("quantityInCellCount");			
			tissueSpecimen.addAbstractAttribute(quantityInCellCount);
			
			tissueSpecimen.setParentEntity(specimen);
			
			EntityInterface savedTissueSpecimen = entityManagerInterface.persistEntityMetadata(tissueSpecimen,false);
			
			
			
			savedTissueSpecimen =  entityManagerInterface.getEntityByIdentifier(savedTissueSpecimen.getId().toString());
			String id = savedTissueSpecimen.getParentEntity().getId().toString();
			System.out.println("Id is" + id);
			
			EntityInterface savedSpecimen =  entityManagerInterface.getEntityByIdentifier(id);
			Collection childColelction =  entityManagerInterface.getChildrenEntities(savedSpecimen);
			assertEquals(childColelction.size(),1);
			childColelction.contains(savedTissueSpecimen);

		}
		catch (Exception e)
		{
			Logger.out.debug(DynamicExtensionsUtility.getStackTrace(e));
			fail();
		}
	}
	
	/**
	 *  PURPOSE: This method tests for getAllAttributes method of the entity
	 *
	 *  EXPECTED BEHAVIOUR: All the attributes from the hierarchy should be returned 
	 *  
	 *  TEST CASE FLOW: 1. Create following hierarchy
	 *                  3. persist leaf nodes 
	 *                  2. check no of AllAttributes for each node.
	 *                  
	 *                  Specimen (barcode and label)
	 *                    |
	 *                    |
	 *               tissueSpecimen (quantityInCellCount)
	 *                    |
	 *                    |
	 *            ----------------------
	 *            |                     |
	 *            |                     |
	 *    advanceTissueSpecimenA      advanceTissueSpecimenB
	 *     (newAttributeA)             (newAttributeB1 and newAttributeB2)   
	 */ 

	public void testInheritanceGetAllAttributes()
	{

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		try
		{
			// Step 1
			EntityInterface specimen = factory.createEntity();
			specimen.setName("specimen");
			specimen.setAbstract(true);
			AttributeInterface barcode = factory.createStringAttribute();
			barcode.setName("barcode");
			specimen.addAbstractAttribute(barcode);
			
			AttributeInterface label = factory.createStringAttribute();
			label.setName("label");
			specimen.addAbstractAttribute(label);
			
			EntityInterface tissueSpecimen = factory.createEntity();			
			tissueSpecimen.setParentEntity(specimen);
			
			specimen = entityManagerInterface.persistEntity(specimen);
			
			tissueSpecimen.setName("tissueSpecimen");
			AttributeInterface quantityInCellCount = factory.createIntegerAttribute();
			quantityInCellCount.setName("quantityInCellCount");			
			tissueSpecimen.addAbstractAttribute(quantityInCellCount);
			
			tissueSpecimen = entityManagerInterface.persistEntity(tissueSpecimen);
			EntityInterface advanceTissueSpecimenA = factory.createEntity();
			advanceTissueSpecimenA.setParentEntity(tissueSpecimen);
			advanceTissueSpecimenA.setName("advanceTissueSpecimenA");
			AttributeInterface newAttribute = factory.createIntegerAttribute();
			newAttribute.setName("newAttributeA");			
			advanceTissueSpecimenA.addAbstractAttribute(newAttribute);
			
			
			EntityInterface advanceTissueSpecimenB = factory.createEntity();
			advanceTissueSpecimenB.setParentEntity(tissueSpecimen);
			advanceTissueSpecimenB.setName("advanceTissueSpecimenB");
			AttributeInterface newAttributeB = factory.createIntegerAttribute();
			newAttributeB.setName("newAttributeB");			
			advanceTissueSpecimenB.addAbstractAttribute(newAttributeB);
			AttributeInterface newAttributeB2 = factory.createIntegerAttribute();
			newAttributeB2.setName("newAttributeB2");			
			advanceTissueSpecimenB.addAbstractAttribute(newAttributeB2);

			// Step 2			
			entityManagerInterface.persistEntity(advanceTissueSpecimenA);
			entityManagerInterface.persistEntity(advanceTissueSpecimenB);
			
			// Step 3	
			
			Collection<AttributeInterface> specimenAttributes = specimen.getAllAttributes();
			assertEquals(2,specimenAttributes.size());

			
			Collection<AttributeInterface> tissueSpecimenAttributes = tissueSpecimen.getAllAttributes();
			assertEquals(3,tissueSpecimenAttributes.size());
			
			Collection<AttributeInterface> advanceTissueSpecimenAAttributes = advanceTissueSpecimenA.getAllAttributes();
			assertEquals(4,advanceTissueSpecimenAAttributes.size());
			
			
			Collection<AttributeInterface> advanceTissueSpecimenBAttributes = advanceTissueSpecimenB.getAllAttributes();
			assertEquals(5,advanceTissueSpecimenBAttributes.size());
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.out.debug(DynamicExtensionsUtility.getStackTrace(e));
			fail();
		}
	}
	
	/**
	 *  PURPOSE: This method tests for getAllChildrenEntities method of the entity
	 *
	 *  EXPECTED BEHAVIOUR: All the attributes from the hierarchy should be returned 
	 *  
	 *  TEST CASE FLOW: 1. Create following hierarchy
	 *                  3. persist leaf nodes 
	 *                  2. check no of getAllChildrenEntities for base node.
	 *                  
	 *                    A
	 *                    |
	 *                    |
	 *             ---------------------
	 * 	           |             		| 
	 *             |             		| 
	 *             B		            C 
	 *			   |        		    |  
	 * 		  ------------- 	 -------------   
	 *       |            |      |      |      |
	 *       D             E     F      g      h
	 */

	public void testGetAllChildrenEntities()
	{

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		DomainObjectFactory factory = DomainObjectFactory.getInstance();

		try
		{
			// Step 1
			EntityInterface entityA = factory.createEntity();
			entityA.setName("entityA");

			EntityInterface entityB = factory.createEntity();
			entityB.setName("entityB");
			entityB.setParentEntity(entityA);

			
			
			EntityInterface entityC = factory.createEntity();
			entityC.setName("entityC");
			entityC.setParentEntity(entityA);

			EntityInterface entityD = factory.createEntity();
			entityD.setName("entityD");
			entityD.setParentEntity(entityB);

			EntityInterface entityE = factory.createEntity();
			entityE.setName("entityE");
			entityE.setParentEntity(entityB);

			EntityInterface entityF = factory.createEntity();
			entityF.setName("entityF");
			entityF.setParentEntity(entityC);

			EntityInterface entityG = factory.createEntity();
			entityG.setName("entityG");
			entityG.setParentEntity(entityC);

			EntityInterface entityH = factory.createEntity();
			entityH.setName("entityH");
			entityH.setParentEntity(entityC);

			entityManagerInterface.persistEntity(entityA);
			entityManagerInterface.persistEntity(entityB);
			entityC = entityManagerInterface.persistEntity(entityC);

			entityManagerInterface.persistEntity(entityD);
			entityManagerInterface.persistEntity(entityE);
			entityManagerInterface.persistEntity(entityF);
			entityManagerInterface.persistEntity(entityG);
			entityManagerInterface.persistEntity(entityH);


			Collection<EntityInterface> cChildren =  entityManagerInterface.getChildrenEntities(entityC);
			assertEquals(3, cChildren.size());

			Collection<EntityInterface> hChildren = entityManagerInterface.getChildrenEntities(entityH);
			assertEquals(0, hChildren.size());

			entityA = entityManagerInterface.getEntityByIdentifier(entityA.getId().toString());
			entityB = entityManagerInterface.getEntityByIdentifier(entityB.getId().toString());
			entityC = entityManagerInterface.getEntityByIdentifier(entityC.getId().toString());
			entityD = entityManagerInterface.getEntityByIdentifier(entityD.getId().toString());
			entityE = entityManagerInterface.getEntityByIdentifier(entityE.getId().toString());
			
			assertEquals(2, entityManagerInterface.getChildrenEntities(entityA).size());
			assertEquals(2, entityManagerInterface.getChildrenEntities(entityB).size());
			assertEquals(3, entityManagerInterface.getChildrenEntities(entityC).size());
			assertEquals(0, entityManagerInterface.getChildrenEntities(entityD).size());
			assertEquals(0, entityManagerInterface.getChildrenEntities(entityE).size());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.out.debug(DynamicExtensionsUtility.getStackTrace(e));
			fail();
		}
	}

}

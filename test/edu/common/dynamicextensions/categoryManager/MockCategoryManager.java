
package edu.common.dynamicextensions.categoryManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityGroupManagerInterface;
import edu.common.dynamicextensions.entitymanager.MockEntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

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

		CategoryInterface category = factory.createCategory();
		category.setName("Category 1");
		category.setCreatedDate(new Date());

		// Create root category entity.
		CategoryEntityInterface rootCategoryEntity = factory.createCategoryEntity();
		rootCategoryEntity.setCreatedDate(new Date());
		rootCategoryEntity.setName("Root Category Entity");

		EntityInterface entity1 = new MockEntityManager().initializeEntity(entityGroup);
		entity1.setName("Entity 1");
		rootCategoryEntity.setEntity((Entity) entity1);

		List<AttributeInterface> entity1AttributeCollection = new ArrayList<AttributeInterface>(entity1.getAttributeCollection());

		CategoryAttributeInterface rootCategoryEntityAttribute1 = factory.createCategoryAttribute();
		rootCategoryEntityAttribute1.setCreatedDate(new Date());
		rootCategoryEntityAttribute1.setName("Category Attribute 1");
		rootCategoryEntityAttribute1.setAttribute(entity1AttributeCollection.get(0));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) rootCategoryEntityAttribute1);

		CategoryAttributeInterface rootCategoryEntityAttribute2 = factory.createCategoryAttribute();
		rootCategoryEntityAttribute2.setCreatedDate(new Date());
		rootCategoryEntityAttribute2.setName("Category Attribute 2");
		rootCategoryEntityAttribute2.setAttribute(entity1AttributeCollection.get(1));
		rootCategoryEntity.getCategoryAttributeCollection().add((CategoryAttribute) rootCategoryEntityAttribute2);

		// Create another category entity.
		CategoryEntityInterface childCategoryEntity1 = factory.createCategoryEntity();
		childCategoryEntity1.setCreatedDate(new Date());
		childCategoryEntity1.setName("Child Category Entity 1");

		EntityInterface entity2 = new MockEntityManager().initializeEntity(entityGroup);
		entity2.setName("Entity 2");
		childCategoryEntity1.setEntity((Entity) entity2);

		List<AttributeInterface> entity2AttributeCollection = new ArrayList<AttributeInterface>(entity2.getAttributeCollection());

		CategoryAttributeInterface childCategoryEntity1CategoryAttribute1 = factory.createCategoryAttribute();
		childCategoryEntity1CategoryAttribute1.setCreatedDate(new Date());
		childCategoryEntity1CategoryAttribute1.setName("Attr 1");
		childCategoryEntity1CategoryAttribute1.setAttribute(entity2AttributeCollection.get(0));
		childCategoryEntity1.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity1CategoryAttribute1);

		CategoryAttributeInterface childCategoryEntity1CategoryAttribute2 = factory.createCategoryAttribute();
		childCategoryEntity1CategoryAttribute2.setCreatedDate(new Date());
		childCategoryEntity1CategoryAttribute2.setName("Attr 2");
		childCategoryEntity1CategoryAttribute2.setAttribute(entity2AttributeCollection.get(1));
		childCategoryEntity1.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity1CategoryAttribute2);

		// Create another category entity.
		CategoryEntityInterface childCategoryEntity2 = factory.createCategoryEntity();
		childCategoryEntity2.setCreatedDate(new Date());
		childCategoryEntity2.setName("Child Category Entity 2");

		EntityInterface entity3 = new MockEntityManager().initializeEntity(entityGroup);
		entity3.setName("Entity 3");
		childCategoryEntity2.setEntity((Entity) entity3);

		List<AttributeInterface> entity3AttributeCollection = new ArrayList<AttributeInterface>(entity3.getAttributeCollection());

		CategoryAttributeInterface childCategoryEntity2CategoryAttribute1 = factory.createCategoryAttribute();
		childCategoryEntity2CategoryAttribute1.setCreatedDate(new Date());
		childCategoryEntity2CategoryAttribute1.setName("Attr 1");
		childCategoryEntity2CategoryAttribute1.setAttribute(entity3AttributeCollection.get(0));
		childCategoryEntity2.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity2CategoryAttribute1);

		CategoryAttributeInterface childCategoryEntity2CategoryAttribute2 = factory.createCategoryAttribute();
		childCategoryEntity2CategoryAttribute2.setCreatedDate(new Date());
		childCategoryEntity2CategoryAttribute2.setName("Attr 2");
		childCategoryEntity2CategoryAttribute2.setAttribute(entity3AttributeCollection.get(1));
		childCategoryEntity2.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity2CategoryAttribute2);

		// Create another category entity.
		CategoryEntityInterface childCategoryEntity3 = factory.createCategoryEntity();
		childCategoryEntity3.setCreatedDate(new Date());
		childCategoryEntity3.setName("Child Category Entity 3");

		EntityInterface entity4 = new MockEntityManager().initializeEntity(entityGroup);
		entity4.setName("Entity 4");
		childCategoryEntity3.setEntity((Entity) entity4);

		List<AttributeInterface> entity4AttributeCollection = new ArrayList<AttributeInterface>(entity4.getAttributeCollection());

		CategoryAttributeInterface childCategoryEntity3CategoryAttribute1 = factory.createCategoryAttribute();
		childCategoryEntity3CategoryAttribute1.setCreatedDate(new Date());
		childCategoryEntity3CategoryAttribute1.setName("Attr 1");
		childCategoryEntity3CategoryAttribute1.setAttribute(entity4AttributeCollection.get(0));
		childCategoryEntity3.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity3CategoryAttribute1);

		CategoryAttributeInterface childCategoryEntity3CategoryAttribute2 = factory.createCategoryAttribute();
		childCategoryEntity3CategoryAttribute2.setCreatedDate(new Date());
		childCategoryEntity3CategoryAttribute2.setName("Attr 2");
		childCategoryEntity3CategoryAttribute2.setAttribute(entity4AttributeCollection.get(1));
		childCategoryEntity3.getCategoryAttributeCollection().add((CategoryAttribute) childCategoryEntity3CategoryAttribute2);

		// Add child category entities to root category entity.
		rootCategoryEntity.getChildCategories().add((CategoryEntity) childCategoryEntity1);
		rootCategoryEntity.getChildCategories().add((CategoryEntity) childCategoryEntity2);
		rootCategoryEntity.getChildCategories().add((CategoryEntity) childCategoryEntity3);

		// Set root category element of the category.
		category.setRootCategoryElement((CategoryEntity) rootCategoryEntity);

		return category;
	}

	/**
	 * 
	 * @return Category
	 */
	public CategoryInterface createCategoryFromModel1()
	{
		CategoryInterface category = null;
		try
		{
			EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
			DomainObjectFactory factory = DomainObjectFactory.getInstance();

			// Create entity group.
			EntityGroupInterface entityGroup = factory.createEntityGroup();
			entityGroup.setName("Entity Group " + new Double(Math.random()).toString());
			entityGroup.setLongName("Long Name " + String.valueOf(new Double(Math.random()).intValue()));
			entityGroup.setShortName("Short Name " + String.valueOf(new Double(Math.random()).intValue()));

			// Create entity trialAgent.
			EntityInterface trialAgent = factory.createEntity();
			trialAgent.setName("TrialAgent");

			// Create attribute(s) for trialAgent entity.	
			AttributeInterface agent = factory.createStringAttribute();
			agent.setName("Agent");
			((StringAttributeTypeInformation) agent.getAttributeTypeInformation()).setSize(40);

			trialAgent.addAbstractAttribute(agent);

			// Create entity chemotherapyTrials.
			EntityInterface chemotherapyTrials = factory.createEntity();
			chemotherapyTrials.setName("ChemotherapyTrials");
			chemotherapyTrials.setParentEntity(trialAgent);

			// Create attribute(s) for chemotherapyTrials entity.	
			AttributeInterface trialsName = factory.createStringAttribute();
			trialsName.setName("TrialsName");
			((StringAttributeTypeInformation) trialsName.getAttributeTypeInformation()).setSize(40);

			chemotherapyTrials.addAbstractAttribute(trialsName);

			// Create entity testDetails.
			EntityInterface testDetails = factory.createEntity();
			testDetails.setName("TestDetails");

			// Create attribute(s) for testDetails entity.	
			AttributeInterface testName = factory.createStringAttribute();
			testName.setName("TestName");
			((StringAttributeTypeInformation) testName.getAttributeTypeInformation()).setSize(40);

			testDetails.addAbstractAttribute(testName);

			// Create entity agentDetails.
			EntityInterface agentDetails = factory.createEntity();
			agentDetails.setName("agentDetails");

			// Create attribute(s) for agentDetails entity.	
			AttributeInterface molecularFormula = factory.createStringAttribute();
			molecularFormula.setName("MolecularFormula");
			((StringAttributeTypeInformation) molecularFormula.getAttributeTypeInformation()).setSize(40);

			agentDetails.addAbstractAttribute(molecularFormula);

			// Associate chemotherapyTrials entity with testDetails entity : chemotherapyTrials (1)------ >(*) testDetails
			AssociationInterface association1 = factory.createAssociation();
			association1.setName("chemotherapyTrialsTestDetailsAssociation");
			association1.setTargetEntity(testDetails);
			association1.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for chemotherapyTrials to testDetails association.
			RoleInterface sourceRole1 = getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsTestDetailsAssociation Source Role", Cardinality.ONE,
					Cardinality.ONE);
			sourceRole1.setAssociationsType(AssociationType.CONTAINTMENT);

			association1.setSourceRole(sourceRole1);
			association1.setTargetRole(getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsTestDetailsAssociation Target Role", Cardinality.ZERO,
					Cardinality.MANY));

			chemotherapyTrials.addAbstractAttribute(association1);

			// Associate chemotherapyTrials entity with agentDetails entity : chemotherapyTrials (1)------ >(*) agentDetails
			AssociationInterface association2 = factory.createAssociation();
			association2.setName("chemotherapyTrialsAgentDetailsAssociation");
			association2.setTargetEntity(agentDetails);
			association2.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for chemotherapyTrials to agentDetails association.
			RoleInterface sourceRole2 = getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsAgentDetailsAssociation Source Role",
					Cardinality.ONE, Cardinality.ONE);
			sourceRole2.setAssociationsType(AssociationType.CONTAINTMENT);

			association2.setSourceRole(sourceRole2);
			association2.setTargetRole(getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsAgentDetailsAssociation Target Role",
					Cardinality.ZERO, Cardinality.MANY));

			chemotherapyTrials.addAbstractAttribute(association2);

			// Create entity testResults.
			EntityInterface testResults = factory.createEntity();
			testResults.setName("testResults");

			// Create attribute(s) for testResults entity.	
			AttributeInterface resultType = factory.createStringAttribute();
			resultType.setName("ResultType");
			((StringAttributeTypeInformation) resultType.getAttributeTypeInformation()).setSize(40);

			testResults.addAbstractAttribute(resultType);

			// Create entity testIteration.
			EntityInterface testIteration = factory.createEntity();
			testIteration.setName("testIteration");

			// Create attribute(s) for testIteration entity.	
			AttributeInterface testCycle = factory.createStringAttribute();
			testCycle.setName("TestCycle");
			((StringAttributeTypeInformation) testCycle.getAttributeTypeInformation()).setSize(40);

			testIteration.addAbstractAttribute(testCycle);

			// Associate testDetails entity with testResults entity : testDetails (1)------ >(*) testResults
			AssociationInterface association3 = factory.createAssociation();
			association3.setName("testDetailsTestResultsAssociation");
			association3.setTargetEntity(testResults);
			association3.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for testDetails to testResults association.
			RoleInterface sourceRole3 = getRole(AssociationType.ASSOCIATION, "testDetailsTestResultsAssociation Source Role", Cardinality.ONE,
					Cardinality.ONE);
			sourceRole3.setAssociationsType(AssociationType.CONTAINTMENT);

			association3.setSourceRole(sourceRole3);
			association3.setTargetRole(getRole(AssociationType.ASSOCIATION, "testDetailsTestResultsAssociation Target Role", Cardinality.ZERO,
					Cardinality.MANY));

			testDetails.addAbstractAttribute(association3);

			// Associate testDetails entity with testIteration entity : testDetails (1)------ >(*) testIteration
			AssociationInterface association4 = factory.createAssociation();
			association4.setName("testDetailsTestIterationAssociation");
			association4.setTargetEntity(testIteration);
			association4.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for testDetails to testIteration association.
			RoleInterface sourceRole4 = getRole(AssociationType.ASSOCIATION, "testDetailsTestIterationAssociation Source Role", Cardinality.ONE,
					Cardinality.ONE);
			sourceRole4.setAssociationsType(AssociationType.CONTAINTMENT);

			association4.setSourceRole(sourceRole4);
			association4.setTargetRole(getRole(AssociationType.ASSOCIATION, "testDetailsTestIterationAssociation Target Role", Cardinality.ZERO,
					Cardinality.MANY));

			testDetails.addAbstractAttribute(association4);

			// Create entity radioTherapyTrials.
			EntityInterface radioTherapyTrials = factory.createEntity();
			radioTherapyTrials.setName("RadioTherapyTrials");
			radioTherapyTrials.setParentEntity(trialAgent);

			// Create attribute(s) for radioTherapyTrials entity.	
			AttributeInterface radiationWavelengthType = factory.createStringAttribute();
			radiationWavelengthType.setName("RadiationWavelengthType");
			((StringAttributeTypeInformation) radiationWavelengthType.getAttributeTypeInformation()).setSize(40);

			radioTherapyTrials.addAbstractAttribute(radiationWavelengthType);

			// Create entity treatmentDetails.
			EntityInterface treatmentDetails = factory.createEntity();
			treatmentDetails.setName("TreatmentDetails");

			// Create attribute(s) for radioTherapyTrials entity.	
			AttributeInterface targetOrgan = factory.createStringAttribute();
			targetOrgan.setName("TargetOrgan");
			((StringAttributeTypeInformation) targetOrgan.getAttributeTypeInformation()).setSize(40);

			treatmentDetails.addAbstractAttribute(targetOrgan);

			// Create entity labDetails.
			EntityInterface labDetails = factory.createEntity();
			labDetails.setName("LabDetails");

			// Create attribute(s) for labDetails entity.	
			AttributeInterface labName = factory.createStringAttribute();
			labName.setName("LabName");
			((StringAttributeTypeInformation) labName.getAttributeTypeInformation()).setSize(40);

			labDetails.addAbstractAttribute(labName);

			// Create entity tumorInfo.
			EntityInterface tumorInfo = factory.createEntity();
			tumorInfo.setName("TumorInfo");

			// Create attribute(s) for tumorInfo entity.	
			AttributeInterface site = factory.createStringAttribute();
			site.setName("Site");
			((StringAttributeTypeInformation) site.getAttributeTypeInformation()).setSize(40);

			tumorInfo.addAbstractAttribute(site);

			// Associate trialAgent entity with radioTherapyTrials entity : trialAgent (1)------ >(*) radioTherapyTrials
			AssociationInterface association5 = factory.createAssociation();
			association5.setName("trialAgentRadioTherapyTrialsAssociation");
			association5.setTargetEntity(radioTherapyTrials);
			association5.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for trialAgent to radioTherapyTrials association.
			RoleInterface sourceRole5 = getRole(AssociationType.ASSOCIATION, "trialAgentRadioTherapyTrialsAssociation Source Role", Cardinality.ONE,
					Cardinality.ONE);
			sourceRole5.setAssociationsType(AssociationType.CONTAINTMENT);

			association5.setSourceRole(sourceRole5);
			association5.setTargetRole(getRole(AssociationType.ASSOCIATION, "trialAgentRadioTherapyTrialsAssociation Target Role", Cardinality.ZERO,
					Cardinality.MANY));

			trialAgent.addAbstractAttribute(association5);

			// Associate radioTherapyTrials entity with treatmentDetails entity : trialAgent (1)------ >(*) radioTherapyTrials
			AssociationInterface association6 = factory.createAssociation();
			association6.setName("radioTherapyTrialsTreatmentDetailsAssociation");
			association6.setTargetEntity(treatmentDetails);
			association6.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for radioTherapyTrials to treatmentDetails association.
			RoleInterface sourceRole6 = getRole(AssociationType.ASSOCIATION, "radioTherapyTrialsTreatmentDetailsAssociation Source Role",
					Cardinality.ONE, Cardinality.ONE);
			sourceRole6.setAssociationsType(AssociationType.CONTAINTMENT);

			association6.setSourceRole(sourceRole6);
			association6.setTargetRole(getRole(AssociationType.ASSOCIATION, "radioTherapyTrialsTreatmentDetailsAssociation Target Role",
					Cardinality.ZERO, Cardinality.MANY));

			radioTherapyTrials.addAbstractAttribute(association6);

			// Associate radioTherapyTrials entity with tumorInfo entity : radioTherapyTrials (1)------ >(*) tumorInfo
			AssociationInterface association7 = factory.createAssociation();
			association7.setName("radioTherapyTrialsTumorInfoAssociation");
			association7.setTargetEntity(tumorInfo);
			association7.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for radioTherapyTrials to tumorInfo association.
			RoleInterface sourceRole7 = getRole(AssociationType.ASSOCIATION, "radioTherapyTrialsTreatmentDetailsAssociation Source Role",
					Cardinality.ONE, Cardinality.ONE);
			sourceRole7.setAssociationsType(AssociationType.CONTAINTMENT);

			association7.setSourceRole(sourceRole7);
			association7.setTargetRole(getRole(AssociationType.ASSOCIATION, "radioTherapyTrialsTreatmentDetailsAssociation Target Role",
					Cardinality.ZERO, Cardinality.MANY));

			radioTherapyTrials.addAbstractAttribute(association7);

			// Associate treatmentDetails entity with labDetails entity : treatmentDetails (1)------ >(*) labDetails
			AssociationInterface association8 = factory.createAssociation();
			association8.setName("treatmentDetailsLabDetailsAssociation");
			association8.setTargetEntity(labDetails);
			association8.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

			// Create source role for treatmentDetails to labDetails association.
			RoleInterface sourceRole8 = getRole(AssociationType.ASSOCIATION, "treatmentDetailsLabDetailsAssociation Source Role", Cardinality.ONE,
					Cardinality.ONE);
			sourceRole8.setAssociationsType(AssociationType.CONTAINTMENT);

			association8.setSourceRole(sourceRole8);
			association8.setTargetRole(getRole(AssociationType.ASSOCIATION, "treatmentDetailsLabDetailsAssociation Target Role", Cardinality.ZERO,
					Cardinality.MANY));

			treatmentDetails.addAbstractAttribute(association8);

			entityGroup.addEntity(trialAgent);
			trialAgent.setEntityGroup(entityGroup);
			entityGroup.addEntity(chemotherapyTrials);
			chemotherapyTrials.setEntityGroup(entityGroup);
			entityGroup.addEntity(radioTherapyTrials);
			radioTherapyTrials.setEntityGroup(entityGroup);
			entityGroup.addEntity(testDetails);
			testDetails.setEntityGroup(entityGroup);
			entityGroup.addEntity(agentDetails);
			agentDetails.setEntityGroup(entityGroup);
			entityGroup.addEntity(treatmentDetails);
			treatmentDetails.setEntityGroup(entityGroup);
			entityGroup.addEntity(tumorInfo);
			tumorInfo.setEntityGroup(entityGroup);
			entityGroup.addEntity(testResults);
			testResults.setEntityGroup(entityGroup);
			entityGroup.addEntity(testIteration);
			testIteration.setEntityGroup(entityGroup);
			entityGroup.addEntity(labDetails);
			labDetails.setEntityGroup(entityGroup);

			// Save entity group.
			entityGroupManager.persistEntityGroup(entityGroup);

			// Create a category.
			category = factory.createCategory();
			category.setName("Category");

			// Create trialAgentCategoryEntity.
			CategoryEntityInterface trialAgentCategoryEntity = factory.createCategoryEntity();
			trialAgentCategoryEntity.setName("trialAgentCategoryEntity");
			trialAgentCategoryEntity.setEntity(trialAgent);
			trialAgentCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for trialAgentCategoryEntity.
			CategoryAttributeInterface agentCategoryAttribute = factory.createCategoryAttribute();
			agentCategoryAttribute.setName("agentCategoryAttribute");
			agentCategoryAttribute.setAttribute(agent);

			trialAgentCategoryEntity.addCategoryAttribute(agentCategoryAttribute);
			agentCategoryAttribute.setCategoryEntity(trialAgentCategoryEntity);

			// Create chemotherapyTrialsCategoryEntity.
			CategoryEntityInterface chemotherapyTrialsCategoryEntity = factory.createCategoryEntity();
			chemotherapyTrialsCategoryEntity.setName("chemotherapyTrialsCategoryEntity");
			chemotherapyTrialsCategoryEntity.setEntity(chemotherapyTrials);
			chemotherapyTrialsCategoryEntity.setNumberOfEntries(-1);
			chemotherapyTrialsCategoryEntity.setParentCategoryEntity(trialAgentCategoryEntity);

			// Create category attribute(s) for chemotherapyTrialsCategoryEntity.
			CategoryAttributeInterface trialsNameCategoryAttribute = factory.createCategoryAttribute();
			trialsNameCategoryAttribute.setName("trialNameCategoryAttribute");
			trialsNameCategoryAttribute.setAttribute(trialsName);

			chemotherapyTrialsCategoryEntity.addCategoryAttribute(trialsNameCategoryAttribute);
			trialsNameCategoryAttribute.setCategoryEntity(chemotherapyTrialsCategoryEntity);

			// Create radioTherapyTrialsCategoryEntity.
			CategoryEntityInterface radioTherapyTrialsCategoryEntity = factory.createCategoryEntity();
			radioTherapyTrialsCategoryEntity.setName("radioTherapyTrialsCategoryEntity");
			radioTherapyTrialsCategoryEntity.setEntity(radioTherapyTrials);
			radioTherapyTrialsCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for radioTherapyTrialsCategoryEntity.
			CategoryAttributeInterface radiationWavelengthTypeCategoryAttribute = factory.createCategoryAttribute();
			radiationWavelengthTypeCategoryAttribute.setName("radiationWavelengthTypeCategoryAttribute");
			radiationWavelengthTypeCategoryAttribute.setAttribute(radiationWavelengthType);

			radioTherapyTrialsCategoryEntity.addCategoryAttribute(radiationWavelengthTypeCategoryAttribute);
			radiationWavelengthTypeCategoryAttribute.setCategoryEntity(radioTherapyTrialsCategoryEntity);

			// Create testDetailsCategoryEntity.
			CategoryEntityInterface testDetailsCategoryEntity = factory.createCategoryEntity();
			testDetailsCategoryEntity.setName("testDetailsCategoryEntity");
			testDetailsCategoryEntity.setEntity(testDetails);
			testDetailsCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for testDetailsCategoryEntity.
			CategoryAttributeInterface testNameCategoryAttribute = factory.createCategoryAttribute();
			testNameCategoryAttribute.setName("testNameCategoryAttribute");
			testNameCategoryAttribute.setAttribute(testName);

			testDetailsCategoryEntity.addCategoryAttribute(testNameCategoryAttribute);
			testNameCategoryAttribute.setCategoryEntity(testDetailsCategoryEntity);

			// Create agentDetailsCategoryEntity.
			CategoryEntityInterface agentDetailsCategoryEntity = factory.createCategoryEntity();
			agentDetailsCategoryEntity.setName("agentDetailsCategoryEntity");
			agentDetailsCategoryEntity.setEntity(agentDetails);
			agentDetailsCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for agentDetailsCategoryEntity.
			CategoryAttributeInterface molecularFormulaCategoryAttribute = factory.createCategoryAttribute();
			molecularFormulaCategoryAttribute.setName("molecularFormulaCategoryAttribute");
			molecularFormulaCategoryAttribute.setAttribute(molecularFormula);

			agentDetailsCategoryEntity.addCategoryAttribute(molecularFormulaCategoryAttribute);
			molecularFormulaCategoryAttribute.setCategoryEntity(agentDetailsCategoryEntity);

			//			// Create treatmentDetailsCategoryEntity.
			//			CategoryEntityInterface treatmentDetailsCategoryEntity = factory.createCategoryEntity();
			//			treatmentDetailsCategoryEntity.setName("agentDetailsCategoryEntity");
			//			treatmentDetailsCategoryEntity.setEntity(treatmentDetails);
			//			treatmentDetailsCategoryEntity.setNumberOfEntries(-1);
			//			
			//			// Create category attribute(s) for treatmentDetailsCategoryEntity.
			//			CategoryAttributeInterface targetOrganCategoryAttribute = factory.createCategoryAttribute();
			//			targetOrganCategoryAttribute.setName("targetOrganCategoryAttribute");
			//			targetOrganCategoryAttribute.setAttribute(targetOrgan);
			//
			//			treatmentDetailsCategoryEntity.addCategoryAttribute(targetOrganCategoryAttribute);
			//			targetOrganCategoryAttribute.setCategoryEntity(treatmentDetailsCategoryEntity);

			// Create tumorInfoCategoryEntity.
			CategoryEntityInterface tumorInfoCategoryEntity = factory.createCategoryEntity();
			tumorInfoCategoryEntity.setName("tumorInfoCategoryEntity");
			tumorInfoCategoryEntity.setEntity(tumorInfo);
			tumorInfoCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for tumorInfoCategoryEntity.
			CategoryAttributeInterface siteCategoryAttribute = factory.createCategoryAttribute();
			siteCategoryAttribute.setName("siteCategoryAttribute");
			siteCategoryAttribute.setAttribute(site);

			tumorInfoCategoryEntity.addCategoryAttribute(siteCategoryAttribute);
			siteCategoryAttribute.setCategoryEntity(tumorInfoCategoryEntity);

			// Create testResultsCategoryEntity.
			CategoryEntityInterface testResultsCategoryEntity = factory.createCategoryEntity();
			testResultsCategoryEntity.setName("testResultsCategoryEntity");
			testResultsCategoryEntity.setEntity(testResults);
			testResultsCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for testResultsCategoryEntity.
			CategoryAttributeInterface resultTypeCategoryAttribute = factory.createCategoryAttribute();
			resultTypeCategoryAttribute.setName("siteCategoryAttribute");
			resultTypeCategoryAttribute.setAttribute(resultType);

			testResultsCategoryEntity.addCategoryAttribute(resultTypeCategoryAttribute);
			resultTypeCategoryAttribute.setCategoryEntity(testResultsCategoryEntity);

			// Create testIterationCategoryEntity.
			CategoryEntityInterface testIterationCategoryEntity = factory.createCategoryEntity();
			testIterationCategoryEntity.setName("testIterationCategoryEntity");
			testIterationCategoryEntity.setEntity(testIteration);
			testIterationCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for testIterationCategoryEntity.
			CategoryAttributeInterface testCycleCategoryAttribute = factory.createCategoryAttribute();
			testCycleCategoryAttribute.setName("testCycleCategoryAttribute");
			testCycleCategoryAttribute.setAttribute(testCycle);

			testIterationCategoryEntity.addCategoryAttribute(testCycleCategoryAttribute);
			testCycleCategoryAttribute.setCategoryEntity(testIterationCategoryEntity);

			// Create labDetailsCategoryEntity.
			CategoryEntityInterface labDetailsCategoryEntity = factory.createCategoryEntity();
			labDetailsCategoryEntity.setName("labDetailsCategoryEntity");
			labDetailsCategoryEntity.setEntity(labDetails);
			labDetailsCategoryEntity.setNumberOfEntries(-1);

			// Create category attribute(s) for labDetailsCategoryEntity.
			CategoryAttributeInterface labNameCategoryAttribute = factory.createCategoryAttribute();
			labNameCategoryAttribute.setName("labNameCategoryAttribute");
			labNameCategoryAttribute.setAttribute(labName);

			labDetailsCategoryEntity.addCategoryAttribute(labNameCategoryAttribute);
			labNameCategoryAttribute.setCategoryEntity(labDetailsCategoryEntity);

			// Create a path between radioTherapyTrialsCategoryEntity and labDetailsCategoryEntity.
			PathInterface path = factory.createPath();

			PathAssociationRelationInterface pathAssociationRelation1 = factory.createPathAssociationRelation();
			pathAssociationRelation1.setAssociation(association6);
			pathAssociationRelation1.setPathSequenceNumber(1);

			PathAssociationRelationInterface pathAssociationRelation2 = factory.createPathAssociationRelation();
			pathAssociationRelation2.setAssociation(association8);
			pathAssociationRelation2.setPathSequenceNumber(2);

			pathAssociationRelation1.setPath(path);
			pathAssociationRelation2.setPath(path);

			path.addPathAssociationRelation(pathAssociationRelation1);
			path.addPathAssociationRelation(pathAssociationRelation2);

			// Add path information to the target category entity.
			labDetailsCategoryEntity.setPath(path);

			// Create a category association between chemotherapyTrialsCategoryEntity and testDetailsCategoryEntity 
			// that corresponds to association between chemotherapyTrials and testDetails.
			CategoryAssociationInterface categoryAssociation1 = factory.createCategoryAssociation();
			categoryAssociation1.setName("chemotherapyTrialsCategoryEntity to testDetailsCategoryEntity association");
			categoryAssociation1.setCategoryEntity(chemotherapyTrialsCategoryEntity);
			categoryAssociation1.setTargetCategoryEntity(testDetailsCategoryEntity);
			chemotherapyTrialsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation1);

			// Create a category association between chemotherapyTrialsCategoryEntity and agentDetailsCategoryEntity 
			// that corresponds to association between chemotherapyTrials and agentDetails.
			CategoryAssociationInterface categoryAssociation2 = factory.createCategoryAssociation();
			categoryAssociation2.setName("chemotherapyTrialsCategoryEntity to agentDetailsCategoryEntity association");
			categoryAssociation2.setCategoryEntity(chemotherapyTrialsCategoryEntity);
			categoryAssociation2.setTargetCategoryEntity(agentDetailsCategoryEntity);
			chemotherapyTrialsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation2);

			// Create a category association between testDetailsCategoryEntity and testResultsCategoryEntity 
			// that corresponds to association between testDetails and testResults.
			CategoryAssociationInterface categoryAssociation3 = factory.createCategoryAssociation();
			categoryAssociation3.setName("testDetailsCategoryEntity to testResultsCategoryEntity association");
			categoryAssociation3.setCategoryEntity(testDetailsCategoryEntity);
			categoryAssociation3.setTargetCategoryEntity(testResultsCategoryEntity);
			testDetailsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation3);

			// Create a category association between testDetailsCategoryEntity and testIterationCategoryEntity 
			// that corresponds to association between testDetails and testIteration.
			CategoryAssociationInterface categoryAssociation4 = factory.createCategoryAssociation();
			categoryAssociation4.setName("testDetailsCategoryEntity to testIterationCategoryEntity association");
			categoryAssociation4.setCategoryEntity(testDetailsCategoryEntity);
			categoryAssociation4.setTargetCategoryEntity(testIterationCategoryEntity);
			testDetailsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation4);

			// Create a category association between trialAgentCategoryEntity and radioTherapyTrialsCategoryEntity 
			// that corresponds to association between trialAgent and radioTherapyTrials.
			CategoryAssociationInterface categoryAssociation5 = factory.createCategoryAssociation();
			categoryAssociation5.setName("trialAgentCategoryEntity to radioTherapyTrialsCategoryEntity association");
			categoryAssociation5.setCategoryEntity(trialAgentCategoryEntity);
			categoryAssociation5.setTargetCategoryEntity(radioTherapyTrialsCategoryEntity);
			trialAgentCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation5);

			// Create a category association between radioTherapyTrialsCategoryEntity and labDetailsCategoryEntity 
			// that corresponds to association between radioTherapyTrials and radioTherapyTrials.
			CategoryAssociationInterface categoryAssociation6 = factory.createCategoryAssociation();
			categoryAssociation6.setName("radioTherapyTrialsCategoryEntity to labDetailsCategoryEntity association");
			categoryAssociation6.setCategoryEntity(radioTherapyTrialsCategoryEntity);
			categoryAssociation6.setTargetCategoryEntity(labDetailsCategoryEntity);
			radioTherapyTrialsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation6);

			// Create a category association between radioTherapyTrialsCategoryEntity and tumorInfoCategoryEntity 
			// that corresponds to association between radioTherapyTrials and tumorInfo.
			CategoryAssociationInterface categoryAssociation7 = factory.createCategoryAssociation();
			categoryAssociation7.setName("radioTherapyTrialsCategoryEntity to tumorInfoCategoryEntity association");
			categoryAssociation7.setCategoryEntity(radioTherapyTrialsCategoryEntity);
			categoryAssociation7.setTargetCategoryEntity(tumorInfoCategoryEntity);
			radioTherapyTrialsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation7);

			chemotherapyTrialsCategoryEntity.addChildCategory(testDetailsCategoryEntity);
			chemotherapyTrialsCategoryEntity.addChildCategory(agentDetailsCategoryEntity);
			testDetailsCategoryEntity.addChildCategory(testResultsCategoryEntity);
			testDetailsCategoryEntity.addChildCategory(testIterationCategoryEntity);
			trialAgentCategoryEntity.addChildCategory(radioTherapyTrialsCategoryEntity);
			radioTherapyTrialsCategoryEntity.addChildCategory(labDetailsCategoryEntity);
			radioTherapyTrialsCategoryEntity.addChildCategory(tumorInfoCategoryEntity);

			// Create containers for category entities.
			int sequenceNumber = 1;

			// Create a container for trialAgentCategoryEntity.
			ContainerInterface trialAgentContainer = createContainer(trialAgentCategoryEntity);
			trialAgentContainer.setCaption("trialAgentContainer");

			// Create a control for agentCategoryAttribute.
			TextFieldInterface agentControl = createTextFieldControl(agentCategoryAttribute, sequenceNumber++);
			agentControl.setParentContainer((Container) trialAgentContainer);

			trialAgentContainer.addControl(agentControl);

			// Create a container for chemotherapyTrialsCategoryEntity.
			ContainerInterface chemotherapyTrialsContainer = createContainer(chemotherapyTrialsCategoryEntity);
			chemotherapyTrialsContainer.setCaption("chemotherapyTrialsContainer");

			// Create a control for trialsNameCategoryAttribute.
			TextFieldInterface trialsNameControl = createTextFieldControl(trialsNameCategoryAttribute, sequenceNumber++);
			trialsNameControl.setParentContainer((Container) chemotherapyTrialsContainer);

			chemotherapyTrialsContainer.addControl(trialsNameControl);

			// Create a container for trialAgentCategoryEntity.
			ContainerInterface testDetailsContainer = createContainer(testDetailsCategoryEntity);
			testDetailsContainer.setCaption("testDetailsContainer");

			// Create a control for testNameCategoryAttribute.
			TextFieldInterface testNameControl = createTextFieldControl(testNameCategoryAttribute, sequenceNumber++);
			testNameControl.setParentContainer((Container) testDetailsContainer);

			testDetailsContainer.addControl(testNameControl);

			// Create a container for agentDetailsCategoryEntity.
			ContainerInterface agentDetailsContainer = createContainer(agentDetailsCategoryEntity);
			agentDetailsContainer.setCaption("agentDetailsContainer");

			// Create a control for molecularFormulaCategoryAttribute.
			TextFieldInterface molecularFormulaControl = createTextFieldControl(molecularFormulaCategoryAttribute, sequenceNumber++);
			molecularFormulaControl.setParentContainer((Container) agentDetailsContainer);

			agentDetailsContainer.addControl(molecularFormulaControl);

			// Create a container for testResultsCategoryEntity.
			ContainerInterface testResultsContainer = createContainer(testResultsCategoryEntity);
			testResultsContainer.setCaption("testResultsContainer");

			// Create a control for resultTypeCategoryAttribute.
			TextFieldInterface resultTypeControl = createTextFieldControl(resultTypeCategoryAttribute, sequenceNumber++);
			resultTypeControl.setParentContainer((Container) testResultsContainer);

			testResultsContainer.addControl(resultTypeControl);

			// Create a container for testIterationCategoryEntity.
			ContainerInterface testIterationContainer = createContainer(testIterationCategoryEntity);
			testIterationContainer.setCaption("testIterationContainer");

			// Create a control for testCycleCategoryAttribute.
			TextFieldInterface testCycleControl = createTextFieldControl(testCycleCategoryAttribute, sequenceNumber++);
			testCycleControl.setParentContainer((Container) testIterationContainer);

			testIterationContainer.addControl(testCycleControl);

			// Create a container for radioTherapyTrialsCategoryEntity.
			ContainerInterface radioTherapyTrialsContainer = createContainer(radioTherapyTrialsCategoryEntity);
			radioTherapyTrialsContainer.setCaption("radioTherapyTrialsContainer");

			// Create a control for radiationWavelengthTypeCategoryAttribute.
			TextFieldInterface radiationWavelengthTypeControl = createTextFieldControl(radiationWavelengthTypeCategoryAttribute, sequenceNumber++);
			radiationWavelengthTypeControl.setParentContainer((Container) radioTherapyTrialsContainer);

			radioTherapyTrialsContainer.addControl(radiationWavelengthTypeControl);

			// Create a container for labDetailsCategoryEntity.
			ContainerInterface labDetailsContainer = createContainer(labDetailsCategoryEntity);
			labDetailsContainer.setCaption("labDetailsContainer");

			// Create a control for labNameCategoryAttribute.
			TextFieldInterface labNameControl = createTextFieldControl(labNameCategoryAttribute, sequenceNumber++);
			labNameControl.setParentContainer((Container) labDetailsContainer);

			labDetailsContainer.addControl(labNameControl);

			// Create a container for tumorInfoCategoryEntity.
			ContainerInterface tumorInfoContainer = createContainer(tumorInfoCategoryEntity);
			tumorInfoContainer.setCaption("tumorInfoContainer");

			// Create a control for siteCategoryAttribute.
			TextFieldInterface siteControl = createTextFieldControl(siteCategoryAttribute, sequenceNumber++);
			siteControl.setParentContainer((Container) tumorInfoContainer);

			tumorInfoContainer.addControl(siteControl);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl1 = factory.createCategoryAssociationControl();
			containmentControl1.setBaseAbstractAttribute(categoryAssociation1);
			containmentControl1.setSequenceNumber(sequenceNumber++);
			containmentControl1.setCaption("containmentControl1");
			containmentControl1.setContainer(testDetailsContainer);
			containmentControl1.setParentContainer((Container) chemotherapyTrialsContainer);

			chemotherapyTrialsContainer.addControl(containmentControl1);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl2 = factory.createCategoryAssociationControl();
			containmentControl2.setBaseAbstractAttribute(categoryAssociation2);
			containmentControl2.setSequenceNumber(sequenceNumber++);
			containmentControl2.setCaption("containmentControl2");
			containmentControl2.setContainer(agentDetailsContainer);
			containmentControl2.setParentContainer((Container) chemotherapyTrialsContainer);

			chemotherapyTrialsContainer.addControl(containmentControl2);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl3 = factory.createCategoryAssociationControl();
			containmentControl3.setBaseAbstractAttribute(categoryAssociation3);
			containmentControl3.setSequenceNumber(sequenceNumber++);
			containmentControl3.setCaption("containmentControl3");
			containmentControl3.setContainer(testResultsContainer);
			containmentControl3.setParentContainer((Container) testDetailsContainer);

			testDetailsContainer.addControl(containmentControl3);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl4 = factory.createCategoryAssociationControl();
			containmentControl4.setBaseAbstractAttribute(categoryAssociation4);
			containmentControl4.setSequenceNumber(sequenceNumber++);
			containmentControl4.setCaption("containmentControl4");
			containmentControl4.setContainer(testIterationContainer);
			containmentControl4.setParentContainer((Container) testDetailsContainer);

			testDetailsContainer.addControl(containmentControl4);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl5 = factory.createCategoryAssociationControl();
			containmentControl5.setBaseAbstractAttribute(categoryAssociation5);
			containmentControl5.setSequenceNumber(sequenceNumber++);
			containmentControl5.setCaption("containmentControl5");
			containmentControl5.setContainer(radioTherapyTrialsContainer);
			containmentControl5.setParentContainer((Container) trialAgentContainer);

			trialAgentContainer.addControl(containmentControl5);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl6 = factory.createCategoryAssociationControl();
			containmentControl6.setBaseAbstractAttribute(categoryAssociation6);
			containmentControl6.setSequenceNumber(sequenceNumber++);
			containmentControl6.setCaption("containmentControl6");
			containmentControl6.setContainer(labDetailsContainer);
			containmentControl6.setParentContainer((Container) radioTherapyTrialsContainer);

			radioTherapyTrialsContainer.addControl(containmentControl6);

			// Create a containment control. 
			AbstractContainmentControlInterface containmentControl7 = factory.createCategoryAssociationControl();
			containmentControl7.setBaseAbstractAttribute(categoryAssociation7);
			containmentControl7.setSequenceNumber(sequenceNumber++);
			containmentControl7.setCaption("containmentControl7");
			containmentControl7.setContainer(tumorInfoContainer);
			containmentControl7.setParentContainer((Container) radioTherapyTrialsContainer);

			radioTherapyTrialsContainer.addControl(containmentControl7);

			// Link containers.
			chemotherapyTrialsContainer.setBaseContainer(trialAgentContainer);

			// Set root category element.
			category.setRootCategoryElement(chemotherapyTrialsCategoryEntity);
			chemotherapyTrialsCategoryEntity.setCategory(category);
		}
		catch (DynamicExtensionsSystemException e)
		{
			e.printStackTrace();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			e.printStackTrace();
		}

		return category;
	}

	/**
	 * 
	 * @param category
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public CategoryInterface addNewCategoryEntityToExistingCategory(CategoryInterface category) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		EntityGroupInterface entityGroup = category.getRootCategoryElement().getEntity().getEntityGroup();

		// Create geneticInfo entity.
		EntityInterface geneticInfo = factory.createEntity();
		geneticInfo.setName("GeneticInfo");

		// Create attribute(s) for geneticInfo entity.	
		AttributeInterface chromosomeName = factory.createStringAttribute();
		chromosomeName.setName("chromosomeName");
		((StringAttributeTypeInformation) chromosomeName.getAttributeTypeInformation()).setSize(40);

		geneticInfo.addAbstractAttribute(chromosomeName);

		EntityInterface chemotherapyTrials = entityGroup.getEntityByName("ChemotherapyTrials");

		// Associate chemotherapyTrials entity with geneticInfo entity : chemotherapyTrials (1)------ >(*) geneticInfo
		AssociationInterface association = factory.createAssociation();
		association.setName("chemotherapyTrialsGeneticInfoAssociation");
		association.setTargetEntity(geneticInfo);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);

		// Create source role for tumorInfo to geneticInfo association.
		RoleInterface sourceRole = getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsGeneticInfoAssociation Source Role", Cardinality.ONE,
				Cardinality.ONE);
		sourceRole.setAssociationsType(AssociationType.CONTAINTMENT);

		association.setSourceRole(sourceRole);
		association.setTargetRole(getRole(AssociationType.ASSOCIATION, "chemotherapyTrialsGeneticInfoAssociation Target Role", Cardinality.ZERO,
				Cardinality.MANY));

		chemotherapyTrials.addAbstractAttribute(association);

		entityGroup.addEntity(geneticInfo);
		geneticInfo.setEntityGroup(entityGroup);

		// Save entity group.
		entityGroupManager.persistEntityGroup(entityGroup);

		// Create geneticInfoCategoryEntity.
		CategoryEntityInterface geneticInfoCategoryEntity = factory.createCategoryEntity();
		geneticInfoCategoryEntity.setName("geneticInfoCategoryEntity");
		geneticInfoCategoryEntity.setEntity(geneticInfo);
		geneticInfoCategoryEntity.setNumberOfEntries(-1);

		// Create category attribute(s) for geneticInfoCategoryEntity.
		CategoryAttributeInterface chromosomeNameCategoryAttribute = factory.createCategoryAttribute();
		chromosomeNameCategoryAttribute.setName("chromosomeNameCategoryAttribute");
		chromosomeNameCategoryAttribute.setAttribute(chromosomeName);

		geneticInfoCategoryEntity.addCategoryAttribute(chromosomeNameCategoryAttribute);
		chromosomeNameCategoryAttribute.setCategoryEntity(geneticInfoCategoryEntity);

		CategoryEntityInterface chemotherapyTrialsCategoryEntity = category.getRootCategoryElement();
		chemotherapyTrialsCategoryEntity.addChildCategory(geneticInfoCategoryEntity);
		List<ContainerInterface> containersList = new ArrayList<ContainerInterface>(chemotherapyTrialsCategoryEntity.getContainerCollection());

		// Create a category association between tumorInfoCategoryEntity and geneticInfoCategoryEntity 
		// that corresponds to association between tumorInfo and geneticInfo.
		CategoryAssociationInterface categoryAssociation = factory.createCategoryAssociation();
		categoryAssociation.setName("tumorInfoCategoryEntity to geneticInfoCategoryEntity association");
		categoryAssociation.setCategoryEntity(chemotherapyTrialsCategoryEntity);
		categoryAssociation.setTargetCategoryEntity(geneticInfoCategoryEntity);
		chemotherapyTrialsCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation);

		// Create a container for geneticInfoCategoryEntity.
		int sequenceNumber = 1;

		// Create a container for geneticInfoCategoryEntity.
		ContainerInterface geneticInfoContainer = createContainer(geneticInfoCategoryEntity);
		geneticInfoContainer.setCaption("geneticInfoContainer");

		// Create a control for chromosomeNameCategoryAttribute.
		TextFieldInterface chromosomeNameControl = createTextFieldControl(chromosomeNameCategoryAttribute, sequenceNumber++);
		chromosomeNameControl.setParentContainer((Container) geneticInfoContainer);

		geneticInfoContainer.addControl(chromosomeNameControl);

		// Create a containment control. 
		AbstractContainmentControlInterface containmentControl = factory.createCategoryAssociationControl();
		containmentControl.setBaseAbstractAttribute(categoryAssociation);
		containmentControl.setSequenceNumber(sequenceNumber++);
		containmentControl.setCaption("containmentControl8");
		containmentControl.setContainer(geneticInfoContainer);
		containmentControl.setParentContainer((Container) containersList.get(0));

		containersList.get(0).addControl(containmentControl);

		return category;
	}

	/**
	 * 
	 * @param category
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public CategoryInterface addCategoryAttributetyToCategoryEntity(CategoryInterface category) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();
		EntityGroupInterface entityGroup = category.getRootCategoryElement().getEntity().getEntityGroup();

		// Retrieve the entity to which we wish to add an attribute.
		EntityInterface chemotherapyTrials = entityGroup.getEntityByName("ChemotherapyTrials");

		// Create attribute(s) for chemotherapyTrials entity.	
		AttributeInterface trialsType = factory.createStringAttribute();
		trialsType.setName("trialsType");
		((StringAttributeTypeInformation) trialsType.getAttributeTypeInformation()).setSize(40);

		chemotherapyTrials.addAbstractAttribute(trialsType);

		// Save entity group.
		entityGroupManager.persistEntityGroup(entityGroup);

		// Retrieve the category entity to which we wish to add a category attribute.
		CategoryEntityInterface chemotherapyTrialsCategoryEntity = category.getRootCategoryElement();

		// Create category attribute(s) for chemotherapyTrialsCategoryEntity.
		CategoryAttributeInterface trialsTypeCategoryAttribute = factory.createCategoryAttribute();
		trialsTypeCategoryAttribute.setName("trialsTypeCategoryAttribute");
		trialsTypeCategoryAttribute.setAttribute(trialsType);

		chemotherapyTrialsCategoryEntity.addCategoryAttribute(trialsTypeCategoryAttribute);
		trialsTypeCategoryAttribute.setCategoryEntity(chemotherapyTrialsCategoryEntity);

		// Fetch container for chemotherapyTrialsCategoryEntity.
		List<ContainerInterface> containersList = new ArrayList<ContainerInterface>(chemotherapyTrialsCategoryEntity.getContainerCollection());

		int sequenceNumber = 1;
		ContainerInterface chemotherapyTrialsContainer = containersList.get(0);

		// Create a control for trialsTypeCategoryAttribute.
		TextFieldInterface trialsTypeControl = createTextFieldControl(trialsTypeCategoryAttribute, sequenceNumber++);
		trialsTypeControl.setParentContainer((Container) chemotherapyTrialsContainer);

		chemotherapyTrialsContainer.addControl(trialsTypeControl);

		return category;
	}

	/**
	 * 
	 * @param objectClassName
	 * @param identifier
	 * @return
	 */
	protected AbstractMetadataInterface getObjectByIdentifier(String objectClassName, int identifier)
	{
		Object object = null;

		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		List objectList = new ArrayList();

		try
		{
			objectList = defaultBizLogic.retrieve(objectClassName, "id", identifier);
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}

		if (objectList != null && objectList.size() > 0)
		{
			object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
		}

		return (AbstractMetadataInterface) object;
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
	 * 
	 * @param categoryEntity
	 * @return ContainerInterface
	 */
	private ContainerInterface createContainer(CategoryEntityInterface categoryEntity)
	{
		ContainerInterface containerInterface = DomainObjectFactory.getInstance().createContainer();
		containerInterface.setAbstractEntity(categoryEntity);
		containerInterface.setMainTableCss("formRequiredLabel");
		containerInterface.setRequiredFieldIndicatior("*");
		containerInterface.setRequiredFieldWarningMessage("indicates mandatory fields.");
		categoryEntity.addContaier(containerInterface);

		return containerInterface;
	}

	/**
	 * 
	 * @param categoryAttribute
	 * @param sequenceNumber
	 * @return
	 */
	private TextFieldInterface createTextFieldControl(CategoryAttributeInterface categoryAttribute, int sequenceNumber)
	{
		TextFieldInterface textFieldInterface = DomainObjectFactory.getInstance().createTextField();
		textFieldInterface.setBaseAbstractAttribute(categoryAttribute);
		textFieldInterface.setColumns(50);
		textFieldInterface.setCaption(categoryAttribute.getName());
		textFieldInterface.setSequenceNumber(sequenceNumber++);

		return textFieldInterface;
	}

}
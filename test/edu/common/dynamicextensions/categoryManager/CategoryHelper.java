
package edu.common.dynamicextensions.categoryManager;

import java.util.List;

import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author kunal_kamble
 * @author mandar_shidhore
 *
 */
public class CategoryHelper implements CategoryHelperInterface
{
	DomainObjectFactory factory = DomainObjectFactory.getInstance();

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCtaegory(java.lang.String)
	 */
	public CategoryInterface createCtaegory(String name)
	{
		CategoryInterface category = factory.createCategory();
		category.setName(name);
		return category;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#createCategoryEntity(java.lang.String, edu.common.dynamicextensions.domaininterface.CategoryInterface[])
	 */
	public ContainerInterface createCategoryEntityAndContainer(EntityInterface entity, CategoryInterface... category)
	{
		CategoryEntityInterface categoryEntity = factory.createCategoryEntity();
		categoryEntity.setName(entity.getName() + " Category Entity");
		categoryEntity.setEntity(entity);

		if (category != null && category.length != 0)
		{
			for (CategoryInterface c : category)
			{
				categoryEntity.setCategory(c);
				c.setRootCategoryElement(categoryEntity);
			}
		}

		ContainerInterface container = createContainer(categoryEntity);
		return container;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#addControl(edu.common.dynamicextensions.domaininterface.AttributeInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.categoryManager.CategoryHelperInterface.ControlEnum, java.util.List<edu.common.dynamicextensions.domaininterface.PermissibleValueInterface>[])
	 */
	public void addControl(EntityInterface entity, String attributeName, ContainerInterface container, ControlEnum controlValue,
			List<PermissibleValueInterface>... permissibleValueList)
	{
		CategoryAttributeInterface categoryAttribute = factory.createCategoryAttribute();
		categoryAttribute.setName(attributeName + " Category Attribute");
		categoryAttribute.setAttribute(entity.getAttributeByName(attributeName));

		CategoryEntity categoryEntity = (CategoryEntity) container.getAbstractEntity();
		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);

		if (controlValue == ControlEnum.textFieldControlNumber)
		{
			int sequenceNo = getNextSequenceNumber(container);
			createTextFieldControl(container, categoryAttribute, sequenceNo);
		}

		if (permissibleValueList != null && permissibleValueList.length != 0)
		{
			UserDefinedDEInterface userDefinedDE = factory.createUserDefinedDE();

			for (PermissibleValueInterface pv : permissibleValueList[0])
			{
				userDefinedDE.addPermissibleValue(pv);
			}
		}

		categoryEntity.addCategoryAttribute(categoryAttribute);
		categoryAttribute.setCategoryEntity(categoryEntity);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setChildCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setChildCategoryEntity(ContainerInterface parentContainer, ContainerInterface childContainer)
	{
		CategoryEntityInterface parentCategoryEntity = (CategoryEntity) parentContainer.getAbstractEntity();
		CategoryEntityInterface childCategoryEntity = (CategoryEntity) childContainer.getAbstractEntity();
		parentCategoryEntity.addChildCategory(childCategoryEntity);
	}
	
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.categoryManager.CategoryHelperInterface#setParentCategoryEntity(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface, edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setParentCategoryEntity(ContainerInterface parentContainer, ContainerInterface childContainer)
	{
		CategoryEntityInterface parentCategoryEntity = (CategoryEntity) parentContainer.getAbstractEntity();
		CategoryEntityInterface childCategoryEntity = (CategoryEntity) childContainer.getAbstractEntity();
		childCategoryEntity.setParentCategoryEntity(parentCategoryEntity);
	}

	public void associateCategoryContainers(ContainerInterface sourceContainer, ContainerInterface targetContainer, List<String> sourceRoleList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		PathInterface path = factory.createPath();

		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		int pathSequenceNumber = 1;
		
		for (String sourceRole : sourceRoleList)
		{
			PathAssociationRelationInterface pathAssociationRelationInterface = factory.createPathAssociationRelation();
			pathAssociationRelationInterface.setPathSequenceNumber(pathSequenceNumber++);

			//todo: what if many associtions exist with this combination
			EntityInterface sourceEntity = ((CategoryEntityInterface) sourceContainer.getAbstractEntity()).getEntity();
			pathAssociationRelationInterface.setAssociation(((List<AssociationInterface>) entityManagerInterface.getAssociation(sourceEntity
					.getName(), sourceRole)).get(0));

			pathAssociationRelationInterface.setPath(path);
			path.addPathAssociationRelation(pathAssociationRelationInterface);
		}

		CategoryEntityInterface sourceCategoryEntity = (CategoryEntityInterface) sourceContainer.getAbstractEntity();
		CategoryEntityInterface targetCategoryEntity = (CategoryEntityInterface) targetContainer.getAbstractEntity();

		addPathBetweenCategoryEntities(sourceCategoryEntity, targetCategoryEntity, path);

		CategoryAssociationInterface categoryAssociation = associateCategoryEntities(sourceCategoryEntity, targetCategoryEntity, sourceCategoryEntity
				.getName()
				+ " to " + targetCategoryEntity.getName() + " category association");

		createCategoryAssociationControl(sourceContainer, targetContainer, categoryAssociation, sourceCategoryEntity.getName() + "-"
				+ targetCategoryEntity.getName() + " association control");
	}

	/**
	 * @param sourceCategoryEntity
	 * @param targetCategoryEntity
	 * @param path
	 */
	private void addPathBetweenCategoryEntities(CategoryEntityInterface sourceCategoryEntity, CategoryEntityInterface targetCategoryEntity,
			PathInterface path)
	{
		targetCategoryEntity.setPath(path);
		sourceCategoryEntity.addChildCategory(targetCategoryEntity);
		targetCategoryEntity.setNumberOfEntries(-1);
	}

	/**
	 * Method associates the source and the target category entity
	 * @param sourceCategoryEntity
	 * @param targetCategoryEntity
	 * @param name
	 * @return category association object
	 */
	private CategoryAssociationInterface associateCategoryEntities(CategoryEntityInterface sourceCategoryEntity,
			CategoryEntityInterface targetCategoryEntity, String name)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		CategoryAssociationInterface categoryAssociation = factory.createCategoryAssociation();

		categoryAssociation.setName(name);
		categoryAssociation.setTargetCategoryEntity(targetCategoryEntity);
		categoryAssociation.setCategoryEntity(sourceCategoryEntity);

		sourceCategoryEntity.getCategoryAssociationCollection().add(categoryAssociation);

		return categoryAssociation;
	}

	/**
	 * Method creates the association between the given parent and the target container 
	 * @param parentContainer
	 * @param targetContainer
	 * @param categoryAssociation
	 * @param caption
	 */
	private void createCategoryAssociationControl(ContainerInterface parentContainer, ContainerInterface targetContainer,
			CategoryAssociationInterface categoryAssociation, String caption)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		CategoryAssociationControlInterface categoryAssociationControl = factory.createCategoryAssociationControl();

		categoryAssociationControl.setCaption(caption);
		categoryAssociationControl.setContainer(targetContainer);
		categoryAssociationControl.setBaseAbstractAttribute(categoryAssociation);
		categoryAssociationControl.setSequenceNumber(getNextSequenceNumber(parentContainer));

		categoryAssociationControl.setParentContainer((Container) parentContainer);
		parentContainer.addControl(categoryAssociationControl);
	}

	/**
	 * @param abstractEntity
	 * @return
	 */
	private ContainerInterface createContainer(AbstractEntityInterface abstractEntity)
	{
		ContainerInterface container = DomainObjectFactory.getInstance().createContainer();
		container.setCaption(abstractEntity.getName() + "_container");
		container.setAbstractEntity(abstractEntity);
		container.setMainTableCss("formRequiredLabel");
		container.setRequiredFieldIndicatior("*");
		container.setRequiredFieldWarningMessage("indicates mandatory fields.");
		abstractEntity.addContainer(container);

		return container;
	}

	/**
	 * @param container
	 * @param baseAbstractAttribute
	 * @param sequenceNumber
	 * @return
	 */
	private TextFieldInterface createTextFieldControl(ContainerInterface container, BaseAbstractAttributeInterface baseAbstractAttribute,
			int sequenceNumber)
	{
		TextFieldInterface textField = DomainObjectFactory.getInstance().createTextField();
		textField.setCaption(baseAbstractAttribute.getName());
		textField.setBaseAbstractAttribute(baseAbstractAttribute);
		textField.setColumns(50);
		textField.setSequenceNumber(sequenceNumber);

		textField.setParentContainer((Container) container);
		container.addControl(textField);

		return textField;
	}

	/**
	 * Method returns the next sequenceNumber
	 * @param container
	 * @return
	 */
	private int getNextSequenceNumber(ContainerInterface container)
	{
		int nextSequenceNumber = 1;

		if (container.getControlCollection() != null)
		{
			nextSequenceNumber = container.getControlCollection().size() + 1;
		}
		return nextSequenceNumber;
	}

}
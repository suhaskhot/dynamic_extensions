
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 */
import java.util.Collection;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

public class ApplyFormDefinitionProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default Constructor.
	 */
	protected ApplyFormDefinitionProcessor()
	{
	}

	/**
	 * Returns the instance of ApplyFormDefinitionProcessor.
	 * @return ApplyFormDefinitionProcessor
	 */
	public static ApplyFormDefinitionProcessor getInstance()
	{
		return new ApplyFormDefinitionProcessor();
	}

	/**
	 * This method creates a Container if not present in cache. Then it will call to ContainerProcessor will
	 * populate this Object with the data from actionform.Then EntityProcessor's methods will be called to either create and Populate
	 * or create and save the entity, Then finally this entity is added to the container. 
	 * @param containerInterface : Container object 
	 * @param actionForm : Form object
	 * @param isActionSave : flag stating whether the object is to be saved to DB
	 * @return ContainerInterface : Container object
	 * @throws DynamicExtensionsApplicationException :Exception thrown by Entity Manager 
	 * @throws DynamicExtensionsSystemException :Exception thrown by Entity Manager
	 */
	public ContainerInterface addEntityToContainer(ContainerInterface containerInterface, FormDefinitionForm actionForm, boolean isActionSave,
			EntityGroupInterface entityGroupInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if (containerInterface == null)
		{
			containerInterface = containerProcessor.createContainer();
		}

		containerProcessor.populateContainerInterface(containerInterface, actionForm);

		//Add entity
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = containerInterface.getEntity();
		if (entityInterface == null)
		{
			entityInterface = entityProcessor.createAndPopulateEntity(actionForm);
		}
		else
		{
			entityProcessor.populateEntity(actionForm, entityInterface);
		}
		containerInterface.setEntity(entityInterface);

		if (entityGroupInterface != null)
		{
			associateEntityToGroup(entityGroupInterface, containerInterface.getEntity());
		}
		if (isActionSave)
		{
			containerProcessor.saveContainer(containerInterface);
		}
		else
		{
			containerProcessor.populateContainerInterface(containerInterface, actionForm);
		}
		return containerInterface;
	}

	/**
	 * @param formDefinitionForm 
	 * @param containerInterface 
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 * 
	 */
	public AssociationInterface associateEntity(ContainerInterface sourceContainer, ContainerInterface targetContainer,
			FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AssociationInterface association = null;
		if ((formDefinitionForm != null) && (targetContainer != null) && (sourceContainer != null))
		{
			EntityInterface targetEntity = targetContainer.getEntity();
			EntityInterface sourceEntity = sourceContainer.getEntity();
			if ((sourceEntity != null) && (targetEntity != null))
			{
				String viewAs = formDefinitionForm.getViewAs();

				if ((viewAs != null) && (viewAs.equals(ProcessorConstants.VIEW_AS_FORM)))
				{
					association = associateEntity(AssociationType.CONTAINTMENT, sourceEntity, targetEntity, Cardinality.ONE, Cardinality.ONE);
				}
				else if ((viewAs != null) && (viewAs.equals(ProcessorConstants.VIEW_AS_SPREADSHEET)))
				{
					association = associateEntity(AssociationType.CONTAINTMENT, sourceEntity, targetEntity, Cardinality.ONE, Cardinality.MANY);
				}
			}
		}
		return association;
	}

	/**
	 * @param formDefinitionForm
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 */
	private ContainerInterface createTargetEntityContainer(FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException
	{
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		ContainerInterface containerInterface = containerProcessor.createContainer();
		containerProcessor.populateContainerInterface(containerInterface, formDefinitionForm);

		//Add entity
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = entityProcessor.createAndPopulateEntity(formDefinitionForm);

		//Add entity to container
		containerInterface.setEntity(entityInterface);
		return containerInterface;
	}

	/**
	 * @param associationType
	 * @param sourceEntity
	 * @param targetEntity
	 * @param sourceCardinality
	 * @param targetCardinality
	 */
	private AssociationInterface associateEntity(AssociationType associationType, EntityInterface sourceEntity, EntityInterface targetEntity,
			Cardinality sourceCardinality, Cardinality targetCardinality)
	{
		AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
		association.setTargetEntity(targetEntity);
		association.setAssociationDirection(AssociationDirection.SRC_DESTINATION);
		association.setName(targetEntity.getName());
		association.setSourceRole(getRole(associationType, null, Cardinality.ONE, sourceCardinality));
		association.setTargetRole(getRole(associationType, targetEntity.getName(), Cardinality.ONE, targetCardinality));
		sourceEntity.addAssociation(association);
		return association;
	}

	/**
	 * @param selectedObjectId
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private ContainerInterface getTargetEntityContainer(String targetContainerId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		ContainerInterface targetContainer = null;
		if ((targetContainerId != null) && (!targetContainerId.trim().equals("")))
		{
			targetContainer = DynamicExtensionsUtility.getContainerByIdentifier(targetContainerId);
		}
		return targetContainer;
	}

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
	 * @param entityGroup : Entity Group containing entity
	 * @param entity : Entity to be associated
	 */
	public void associateEntityToGroup(EntityGroupInterface entityGroup, EntityInterface entity)
	{
		if ((entityGroup != null) && (entity != null))
		{
			entity.removeAllEntityGroups();

			entityGroup.addEntity(entity);
			entity.addEntityGroupInterface(entityGroup);
		}
	}

	/**
	 * @param mainFormContainer
	 * @param subFormContainer
	 */
	public void addSubFormControlToContainer(ContainerInterface mainFormContainer, ContainerInterface subFormContainer,AssociationInterface association)
	{
		if ((mainFormContainer != null) && (subFormContainer != null))
		{
			ControlProcessor controlProcessor = ControlProcessor.getInstance();
			ControlInterface subFormControl = controlProcessor.createContainmentAssociationControl(subFormContainer,association);
			subFormControl.setSequenceNumber(WebUIManager.getSequenceNumberForNextControl(mainFormContainer));
			if(subFormControl!=null)
			{
				mainFormContainer.addControl(subFormControl);
			}
		}
	}

	/**
	 * @param formDefinitionForm
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public ContainerInterface getSubFormContainer(FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		ContainerInterface targetEntityContainer = null;
		if (formDefinitionForm != null)
		{
			//Get target entity container
			String createAs = formDefinitionForm.getCreateAs();
			if ((createAs != null) && (createAs.equals(ProcessorConstants.CREATE_FROM_EXISTING)))
			{
				targetEntityContainer = getTargetEntityContainer(formDefinitionForm.getSelectedObjectId());
			}
			else if ((createAs != null) && (createAs.equals(ProcessorConstants.CREATE_AS_NEW)))
			{
				targetEntityContainer = createTargetEntityContainer(formDefinitionForm);
			}
		}
		return targetEntityContainer;
	}

	/**
	 * @param subFormContainer
	 * @param mainFormContainer
	 */
	public void associateParentGroupToNewEntity(ContainerInterface subFormContainer, ContainerInterface mainFormContainer)
	{
		if((subFormContainer!=null)&&(mainFormContainer!=null))
		{
			EntityInterface targetEntity = subFormContainer.getEntity();
			EntityInterface sourceEntity = mainFormContainer.getEntity();
			Collection<EntityGroupInterface> sourceEntityGroups = sourceEntity.getEntityGroupCollection();
			if(sourceEntityGroups!=null)
			{
				Iterator<EntityGroupInterface> entityGroupIter = sourceEntityGroups.iterator();
				if(entityGroupIter.hasNext())
				{
					EntityGroupInterface sourceEntityGroup = entityGroupIter.next();
					targetEntity.addEntityGroupInterface(sourceEntityGroup);
				}
			}
			
		}
	}

}

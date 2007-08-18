
package edu.common.dynamicextensions.xmi.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domain.AssociationDisplayAttribute;
import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domain.userinterface.SelectControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.wustl.cab2b.server.path.BaseDomainModelProecessor;
import edu.wustl.cab2b.server.path.DomainModelParser;


/**
 * 
 * @author sujay_narkar
 * @author ashish_gupta
 *
 */
public class DynamicExtensionsDomainModelProcessor extends BaseDomainModelProecessor
{

	/**
	 * Default constructor
	 *
	 */
	public DynamicExtensionsDomainModelProcessor()
	{
		super();
	}

	/**
	 * 
	 * @param parser
	 * @param applicationName
	 */
	public DynamicExtensionsDomainModelProcessor(DomainModelParser parser, String applicationName)
			throws Exception
	{
		super(parser, applicationName);
	}

	/**
	 * This method creates a container object.
	 * @param entityInterface
	 * @return
	 */
	protected void createContainer(EntityInterface entityInterface) throws Exception
	{
		ContainerInterface containerInterface = deFactory.createContainer();
		containerInterface.setCaption(entityInterface.getName());
		containerInterface.setEntity(entityInterface);

		//Adding Required field indicator
		containerInterface.setRequiredFieldIndicatior(" ");
		containerInterface.setRequiredFieldWarningMessage(" ");

		Collection<AbstractAttributeInterface> abstractAttributeCollection = entityInterface
				.getAbstractAttributeCollection();
		Integer sequenceNumber = new Integer(0);
		ControlInterface controlInterface;
		for (AbstractAttributeInterface abstractAttributeInterface : abstractAttributeCollection)
		{
			controlInterface = getControlForAttribute(abstractAttributeInterface);
			sequenceNumber++;
			controlInterface.setSequenceNumber(sequenceNumber);
			containerInterface.addControl(controlInterface);
			controlInterface.setParentContainer((Container) containerInterface);
		}
		List<ContainerInterface> containerList = new ArrayList<ContainerInterface>();
		containerList.add(containerInterface);
		entityIdVsContainers.put(entityInterface.getName(), containerList);
	}

	/**
	 * @param parentIdVsChildrenIds
	 * This method add the parent container to the child container for Generalisation.
	 */
	protected void postProcessInheritence(Map<String, List<String>> parentIdVsChildrenIds)
			throws Exception
	{
		for (Entry<String, List<String>> entry : parentIdVsChildrenIds.entrySet())
		{
			EntityInterface parent = umlClassIdVsEntity.get(entry.getKey());
				
			List parentContainerList = (ArrayList) entityIdVsContainers.get(parent.getName());
			ContainerInterface parentContainer = (ContainerInterface) parentContainerList.get(0);
			for (String childId : entry.getValue())
			{
				EntityInterface child = umlClassIdVsEntity.get(childId);
						
				List childContainerList = (ArrayList) entityIdVsContainers.get(child.getName());
				ContainerInterface childContainer = (ContainerInterface) childContainerList.get(0);

				childContainer.setBaseContainer(parentContainer);

			}
		}
	}

	/**
	 * This method adds the target container to the containment association control
	 */
	protected void addControlsForAssociation() throws Exception
	{
		Set<String> entityIdKeySet = entityIdVsContainers.keySet();
		for (String entityId : entityIdKeySet)
		{
			List containerList = (ArrayList) entityIdVsContainers.get(entityId);
			ContainerInterface containerInterface = (ContainerInterface) containerList.get(0);
			Collection<ControlInterface> controlCollection = containerInterface
					.getControlCollection();

			for (ControlInterface controlInterface : controlCollection)
			{
				if (controlInterface instanceof ContainmentAssociationControl)
				{
					ContainmentAssociationControl containmentAssociationControl = (ContainmentAssociationControl) controlInterface;
					AssociationInterface associationInterface = (AssociationInterface) controlInterface
							.getAbstractAttribute();				

					String targetEntityId = associationInterface.getTargetEntity().getName();

					List targetContainerInterfaceList = (ArrayList) entityIdVsContainers
							.get(targetEntityId.toString());
					ContainerInterface targetContainerInterface = (ContainerInterface) targetContainerInterfaceList
							.get(0);
					containmentAssociationControl.setContainer(targetContainerInterface);
				}
			}
		}
	}

	/**
	 * 
	 * @param abstractAttributeInterface
	 * @return
	 * This method creates a control for the attribute.
	 */
	private ControlInterface getControlForAttribute(
			AbstractAttributeInterface abstractAttributeInterface)
	{
		ControlInterface controlInterface = null;

		if (abstractAttributeInterface instanceof AssociationInterface)
		{
			AssociationInterface associationInterface = (AssociationInterface) abstractAttributeInterface;
			//TODO This line is for containment association.
			controlInterface = deFactory.createContainmentAssociationControl();
			associationInterface.getSourceRole().setAssociationsType(AssociationType.CONTAINTMENT);
			associationInterface.getTargetRole().setAssociationsType(AssociationType.CONTAINTMENT);

			//			TODO this is for Linking Association
			//if source maxcardinality or target  maxcardinality or both == -1, then control is listbox.
			//int  sourceMaxCardinality = associationInterface.getSourceRole().getMaximumCardinality().getValue().intValue();

			//			int targetMaxCardinality = associationInterface.getTargetRole().getMaximumCardinality()
			//					.getValue().intValue();
			//			if (targetMaxCardinality == -1)
			//			{//List box for 1 to many or many to many relationship
			//				controlInterface = deFactory.createListBox();
			//				((ListBoxInterface) controlInterface).setIsMultiSelect(true);
			//			}
			//			else
			//			{//Combo box for the rest
			//				controlInterface = deFactory.createComboBox();
			//			}
			//
			//			((SelectControl) controlInterface).setSeparator(",");
			//			addAssociationDisplayAttributes(associationInterface, controlInterface);

		}
		else
		{
			AttributeInterface attributeInterface = (AttributeInterface) abstractAttributeInterface;
			AttributeTypeInformationInterface attributeTypeInformation = attributeInterface
					.getAttributeTypeInformation();
			UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) attributeTypeInformation
					.getDataElement();

			if (userDefinedDEInterface != null
					&& userDefinedDEInterface.getPermissibleValueCollection() != null
					&& userDefinedDEInterface.getPermissibleValueCollection().size() > 0)
			{
				controlInterface = deFactory.createListBox();

				// multiselect for permisible values 
				((ListBoxInterface) controlInterface).setIsMultiSelect(true);
				attributeInterface.setIsCollection(new Boolean(true));

			}
			else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
			{
				controlInterface = deFactory.createDatePicker();
			}
			//Creating check box for boolean attributes
			else if (attributeTypeInformation instanceof BooleanAttributeTypeInformation)
			{
				controlInterface = deFactory.createCheckBox();
			}
			else
			{
				controlInterface = deFactory.createTextField();
				((TextFieldInterface) controlInterface).setColumns(0);
			}
		}
		controlInterface.setName(abstractAttributeInterface.getName());
		controlInterface.setCaption(abstractAttributeInterface.getName());
		controlInterface.setAbstractAttribute(abstractAttributeInterface);

		return controlInterface;
	}

	/**
	 * @param associationInterface
	 * @param controlInterface
	 * In case of linking association, this method adds the association display attributes.
	 */
	private void addAssociationDisplayAttributes(AssociationInterface associationInterface,
			ControlInterface controlInterface)
	{
		EntityInterface targetEntity = associationInterface.getTargetEntity();
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		//		This method returns all attributes and not associations
		Collection<AttributeInterface> targetEntityAttrColl = targetEntity.getAttributeCollection();
		int seqNo = 1;
		for (AttributeInterface attr : targetEntityAttrColl)
		{
			AssociationDisplayAttributeInterface associationDisplayAttribute = domainObjectFactory
					.createAssociationDisplayAttribute();
			associationDisplayAttribute.setSequenceNumber(seqNo);
			associationDisplayAttribute.setAttribute(attr);
			//This method adds to the associationDisplayAttributeCollection
			((SelectControl) controlInterface)
					.addAssociationDisplayAttribute(associationDisplayAttribute);
			seqNo++;
		}
	}

	/**
	 * @param entity
	 * This method removes inherited attributes.
	 */

	protected void removeInheritedAttributes(EntityInterface entity,
			List duplicateAttributeCollection)
	{
		if (duplicateAttributeCollection != null)
		{
			entity.getAbstractAttributeCollection().removeAll(duplicateAttributeCollection);
		}
	}

	/**
	 * @param umlClasses
	 * This method creates all containers.
	 */
	protected void processPersistence() throws Exception
	{
		Collection<ContainerInterface> containerColl = new HashSet<ContainerInterface>();

		Set<String> entityIdKeySet = entityIdVsContainers.keySet();
		for (String entityId : entityIdKeySet)
		{
			List containerList = (ArrayList) entityIdVsContainers.get(entityId);
			ContainerInterface containerInterface = (ContainerInterface) containerList.get(0);
			containerColl.add(containerInterface);			
		}
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

		try
		{
			EntityGroupInterface entityGroupInterface = entityManagerInterface
					.persistEntityGroupWithAllContainers(entityGroup, containerColl);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw new DynamicExtensionsApplicationException(e.getMessage(), e);
		}
		catch (DynamicExtensionsSystemException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @throws Exception
	 */
	protected void postProcessAssociation() throws Exception
	{
		addControlsForAssociation();
	}
}

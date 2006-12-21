
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * @author sujay_narkar
 * @author chetan_patil
 *
 */
public class LoadDataEntryFormProcessor
{

	/**
	 * Empty Constructor
	 */
	protected LoadDataEntryFormProcessor()
	{
	}

	/**
	 * This method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static LoadDataEntryFormProcessor getInstance()
	{
		return new LoadDataEntryFormProcessor();
	}

	/**
	 * 
	 * @param actionForm
	 * @param containerInterface
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 */
	public ContainerInterface loadDataEntryForm(AbstractActionForm actionForm, ContainerInterface containerInterface,Map valueMap, 
			String recordIdentifier,String mode) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DataEntryForm dataEntryForm = (DataEntryForm) actionForm;


		if (mode != null && mode.equalsIgnoreCase(WebUIManagerConstants.VIEW_MODE)) {
			
			containerInterface.setMode(mode);
		}
		EntityInterface entity = containerInterface.getEntity();

		// Get corresponding Control Collection of the Container
		Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();

		if (recordIdentifier != null)
		{
			//Get corresponding Entity of the Container
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Map<AbstractAttributeInterface, Object> recordMap = entityManager.getRecordById(entity, Long.valueOf(recordIdentifier));
			valueMap = recordMap;
			
		}
		
		//setControlsRecordValue(controlCollection, valueMap);
		if(valueMap != null && !valueMap.isEmpty()  )
			
		{
			containerInterface.setContainerValueMap(valueMap);
		}
		

		dataEntryForm.setContainerInterface(containerInterface);
		if (dataEntryForm.getErrorList() == null)
		{
			List<String> errorList = new ArrayList<String>();
			dataEntryForm.setErrorList(errorList);
		}
		if (dataEntryForm.getShowFormPreview() == null)
		{
			dataEntryForm.setShowFormPreview("");
		}
		if (recordIdentifier != null)
		{
			dataEntryForm.setRecordIdentifier(recordIdentifier);
		}
		else
		{
			dataEntryForm.setRecordIdentifier("");
		}
		return containerInterface;
	}

	/**
	 * This method sets the values obtained form the record to the corresponding Control of the respective Attributes.
	 * @param entity the Entity whose record is to be fetched from database.
	 * @param controlCollection the Collection of Controls whose values are to be set.
	 * @param recordIdentifier the identifier of the record which is to be fetched form database.
	 * @throws NumberFormatException if recordIdentifier is not a valid number.
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException
	 */
	@SuppressWarnings("unchecked")
	private void setControlsRecordValue(Collection<ControlInterface> controlCollection, Map<AbstractAttributeInterface, Object> recordMap)
			throws NumberFormatException, DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		

		Set<Map.Entry<AbstractAttributeInterface, Object>> recordSet = recordMap.entrySet();
		for (Map.Entry<AbstractAttributeInterface, Object> recordNode : recordSet)
		{
			AbstractAttributeInterface abstractAttribute = recordNode.getKey();
			ControlInterface control = getControlInterface(controlCollection, abstractAttribute);

			if (abstractAttribute instanceof AttributeInterface)
			{
				control.setValue(recordNode.getValue());
			}
			else if (abstractAttribute instanceof AssociationInterface)
			{
				//TODO
			}
			
		/*	for (ControlInterface control : controlCollection)
			{
				AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
				Object recordAttributeValue = recordNode.getValue();
				if (abstractAttribute.getName().equals(recordAttributeName))
				{
					if (recordAttributeValue != null)
					{
						if (abstractAttribute instanceof AttributeInterface)
						{
							control.setValue(recordAttributeValue);
						}
						else if (abstractAttribute instanceof AssociationInterface)
						{
							AssociationInterface association = (AssociationInterface) abstractAttribute;

							RoleInterface role = association.getTargetRole();
							if (role != null)
							{
								AssociationType associationType = role.getAssociationsType();
								if (associationType != null)
								{
									String associationTypeName = associationType.getValue();
									if (associationTypeName.equals(AssociationType.CONTAINTMENT))
									{
										EntityInterface targetEntity = association.getTargetEntity();
										ContainerInterface targetContainer = entityManager.getContainerByEntityIdentifier(targetEntity.getId());
										Collection<ControlInterface> targetControlCollection = targetContainer.getControlCollection();
										if (targetControlCollection != null && !targetControlCollection.isEmpty())
										{
											setControlsRecordValue(targetEntity, targetControlCollection, (Map<String, Object>) recordAttributeValue);
										}
									}
								}
							}
						}
					}
				}				
			}*/
		}
	}
	
	/**
	 * @param controlCollection
	 * @param attribute
	 * @return
	 */
	private ControlInterface getControlInterface(Collection<ControlInterface> controlCollection, AbstractAttributeInterface attribute) {
		for(ControlInterface control: controlCollection) {
			if(control.getAbstractAttribute().equals(attribute)) {
				return control;
			}
		}
		return null;
	}
}



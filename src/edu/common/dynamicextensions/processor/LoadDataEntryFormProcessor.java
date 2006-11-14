
package edu.common.dynamicextensions.processor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * @author sujay_narkar, chetan_patil
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
	@SuppressWarnings("unchecked")
	public ContainerInterface loadDataEntryForm(AbstractActionForm actionForm, ContainerInterface containerInterface, String containerIdentifier,
			String recordId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		DataEntryForm dataEntryForm = (DataEntryForm) actionForm;
		Map recordMap = null;

		if (containerInterface == null || containerIdentifier != null)
		{
			containerInterface = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
		}

		if (recordId != null)
		{
			//Get corresponding Entity of the Container
			EntityInterface entity = containerInterface.getEntity();
			// Get corresponding Control Collection of the Container
			Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();

			//Get Records of the corresponding Entity
			EntityManagerInterface entityManager = EntityManager.getInstance();
			recordMap = entityManager.getRecordById(entity, Long.valueOf(recordId));

			Set<Map.Entry> recordSet = recordMap.entrySet();
			for (Map.Entry recordNode : recordSet)
			{
				String recordAttributeName = (String) recordNode.getKey();
				String recordAttributeValue = (String) recordNode.getValue();

				for (ControlInterface control : controlCollection)
				{
					AbstractAttributeInterface controlAbstractAttribute = control.getAbstractAttribute();
					if (controlAbstractAttribute.getName().equals(recordAttributeName))
					{
						if (recordAttributeValue != null)
						{
							control.setValue(recordAttributeValue);
						}
					}
				}
			}
		}
		dataEntryForm.setContainerInterface(containerInterface);
		if (dataEntryForm.getShowFormPreview() == null)
		{
			dataEntryForm.setShowFormPreview("");
		}
		if(recordId != null)
		{
			dataEntryForm.setRecordId(recordId);
		}
		else
		{
			dataEntryForm.setRecordId("");
		}
		return containerInterface;
	}

}

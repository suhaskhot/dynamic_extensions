
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	public ContainerInterface loadDataEntryForm(AbstractActionForm actionForm, ContainerInterface containerInterface, String containerIdentifier,
			String recordIdentifier) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		DataEntryForm dataEntryForm = (DataEntryForm) actionForm;
		Map<String, String> recordMap = null;

		if (containerInterface == null || containerIdentifier != null)
		{
			containerInterface = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
		}

		if (recordIdentifier != null)
		{
			//Get corresponding Entity of the Container
			EntityInterface entity = containerInterface.getEntity();
			// Get corresponding Control Collection of the Container
			Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();

			//Get Records of the corresponding Entity
			EntityManagerInterface entityManager = EntityManager.getInstance();
			recordMap = entityManager.getRecordById(entity, Long.valueOf(recordIdentifier));

			Set<Map.Entry<String, String>> recordSet = recordMap.entrySet();
			for (Map.Entry<String, String> recordNode : recordSet)
			{
				String recordAttributeName = recordNode.getKey();
				String recordAttributeValue = recordNode.getValue();

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
		if(dataEntryForm.getErrorList() == null)
		{
			List<String> errorList = new ArrayList<String>();
			dataEntryForm.setErrorList(errorList);
		}
		if (dataEntryForm.getShowFormPreview() == null)
		{
			dataEntryForm.setShowFormPreview("");
		}
		if(recordIdentifier != null)
		{
			dataEntryForm.setRecordIdentifier(recordIdentifier);
		}
		else
		{
			dataEntryForm.setRecordIdentifier("");
		}
		return containerInterface;
	}

}

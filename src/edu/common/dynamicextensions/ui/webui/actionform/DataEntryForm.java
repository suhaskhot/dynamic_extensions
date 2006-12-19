
package edu.common.dynamicextensions.ui.webui.actionform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 *
 */
public class DataEntryForm extends AbstractActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7828307676065035418L;

	/**
	 * 
	 */
	protected String entitySaved;
	/**
	 * 
	 */
	protected String showFormPreview;

	/**
	 * 
	 */
	protected ContainerInterface containerInterface;
	/**
	 * 
	 */
	protected String recordIdentifier = "abc";
	/**
	 * 
	 */
	protected List<String> errorList;
	
	/**
	 * 
	 */
	protected String mode = WebUIManagerConstants.EDIT_MODE;

	/**
	 * 
	 */
	protected Map valueMap = new HashMap();

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value)
	{
		valueMap.put(key, value);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue(String key)
	{
		return valueMap.get(key);
	}

	/**
	 * @return Returns the valueMap.
	 */
	public Map getValueMap()
	{
		return valueMap;
	}

	/**
	 * @param valueMap The valueMap to set.
	 */
	public void setValueMap(Map valueMap)
	{
		this.valueMap = valueMap;
	}

	/**
	 * @return int formId
	 */
	public int getFormId()
	{
		return 0;
	}

	/**
	 * @param arg0 abstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
	}

	/**
	 * 
	 */
	protected void reset()
	{
	}

	/**
	 * @return Returns the container.
	 */
	public ContainerInterface getContainerInterface()
	{
		return containerInterface;
	}

	/**
	 * @param containerInterface The container to set.
	 */
	public void setContainerInterface(ContainerInterface containerInterface)
	{
		this.containerInterface = containerInterface;
	}

	/**
	 * 
	 * @return entitySaved
	 */
	public String getEntitySaved()
	{
		return entitySaved;
	}

	/**
	 * 
	 * @param entitySaved entitySaved
	 */
	public void setEntitySaved(String entitySaved)
	{
		this.entitySaved = entitySaved;
	}

	/**
	 * 
	 * @return String showFormPreview 
	 */
	public String getShowFormPreview()
	{
		return showFormPreview;
	}

	/**
	 * 
	 * @param showFormPreview String showFormPreview
	 */
	public void setShowFormPreview(String showFormPreview)
	{
		this.showFormPreview = showFormPreview;
	}

	public String getRecordIdentifier()
	{
		return recordIdentifier;
	}

	public void setRecordIdentifier(String recordIdentifier)
	{
		this.recordIdentifier = recordIdentifier;
	}

	/**
	 * @return the errorList
	 */
	public List<String> getErrorList()
	{
		return errorList;
	}

	/**
	 * @param errorList the errorList to set
	 */
	public void setErrorList(List<String> errorList)
	{
		this.errorList = errorList;
	}

	
	/**
	 * @return Returns the mode.
	 */
	public String getMode()
	{
		return mode;
	}

	
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

}

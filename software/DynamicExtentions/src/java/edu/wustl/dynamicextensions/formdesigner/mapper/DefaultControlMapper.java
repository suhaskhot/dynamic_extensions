
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public abstract class DefaultControlMapper
{

	/**
	 * @param controlProps
	 * @return
	 * @throws Exception 
	 */
	public abstract Control propertiesToControl(Properties controlProps) throws Exception;

	/**
	 * @param control
	 * @return
	 */
	public abstract Properties controlToProperties(Control control);

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void setCommonProperties(Properties controlProps, Control control)
	{
		control.setCaption(controlProps.getString(CSDConstants.CONTROL_CAPTION));
		control.setName(controlProps.getString(CSDConstants.CONTROL_NAME));
		control.setPhi(controlProps.getBoolean("isPHI"));
		control.setMandatory(controlProps.getBoolean("isMandatory"));
		control.setCalculatedSourceControl(controlProps.getBoolean("calculatedSourceControl"));
		control.setConceptDefinitionSource(controlProps.getString("conceptDefinitionSource"));
		control.setConceptCode(controlProps.getString("conceptCode"));
		control.setConceptDefinition(controlProps.getString("conceptDefinition"));
		control.setConceptPreferredName(controlProps.getString("conceptPreferredName"));
		control.setCustomLabel(controlProps.getString("customLabel"));
		control.setxPos(controlProps.getInteger("xPos"));
		control.setSequenceNumber(controlProps.getInteger("sequenceNumber"));
		control.setToolTip(controlProps.getString("toolTip"));
	}

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void getCommonProperties(Properties controlProps, Control control)
	{
		controlProps.setProperty(CSDConstants.CONTROL_CAPTION, control.getCaption());
		controlProps.setProperty(CSDConstants.CONTROL_NAME, control.getName());
		controlProps.setProperty("id", control.getId());
		controlProps.setProperty("isPHI", control.isPhi());
		controlProps.setProperty("isMandatory", control.isMandatory());
		controlProps.setProperty("calculatedSourceControl", control.isCalculatedSourceControl());
		controlProps.setProperty("conceptDefinitionSource", control.getConceptDefinitionSource());
		controlProps.setProperty("conceptCode", control.getConceptCode());
		controlProps.setProperty("conceptDefinition",control.getConceptDefinition());
		controlProps.setProperty("conceptPreferredName",control.getConceptPreferredName());
		controlProps.setProperty("xPos", control.getxPos());
		controlProps.setProperty("sequenceNumber", control.getSequenceNumber());
		controlProps.setProperty("customLabel", control.getCustomLabel());
		controlProps.setProperty("toolTip", control.getToolTip());
		controlProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
	}
}

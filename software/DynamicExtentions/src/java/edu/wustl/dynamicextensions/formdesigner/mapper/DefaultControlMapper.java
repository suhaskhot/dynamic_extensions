
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

public abstract class DefaultControlMapper {

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
	protected void setCommonProperties(Properties controlProps, Control control) {

		String controlCaption = controlProps.getString(CSDConstants.CONTROL_CAPTION);
		if (controlCaption != null) {
			control.setCaption(controlCaption);
		}

		String controlName = controlProps.getString(CSDConstants.CONTROL_NAME);
		if (controlName != null) {
			control.setName(controlName);
		}

		control.setPhi(controlProps.getBoolean("isPHI"));
		control.setMandatory(controlProps.getBoolean("isMandatory"));

		String conceptDefinitionSource = controlProps.getString("conceptDefinitionSource");
		if (conceptDefinitionSource != null) {
			control.setConceptDefinitionSource(conceptDefinitionSource);
		}

		String conceptCode = controlProps.getString("conceptCode");
		if (conceptCode != null) {
			control.setConceptCode(conceptCode);
		}

		String conceptDefinition = controlProps.getString("conceptDefinition");
		if (conceptDefinition != null) {
			control.setConceptDefinition(conceptDefinition);
		}

		String conceptPreferredName = controlProps.getString("conceptPreferredName");
		if (conceptPreferredName != null) {
			control.setConceptPreferredName(conceptPreferredName);
		}

		String customLabel = controlProps.getString("customLabel");
		if (customLabel != null) {
			control.setCustomLabel(customLabel);
		}

		Integer xPos = controlProps.getInteger("xPos");
		if (xPos != null) {
			control.setxPos(xPos);
		}

		Integer sequenceNumber = controlProps.getInteger("sequenceNumber");
		if (sequenceNumber != null) {
			control.setSequenceNumber(sequenceNumber);
		}

		String toolTip = controlProps.getString("toolTip");
		if (toolTip != null) {
			control.setToolTip(toolTip);
		}

	}

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void getCommonProperties(Properties controlProps, Control control) {
		controlProps.setProperty(CSDConstants.CONTROL_CAPTION, control.getCaption());
		controlProps.setProperty(CSDConstants.CONTROL_NAME, control.getName());
		controlProps.setProperty("id", control.getId());
		controlProps.setProperty("isPHI", control.isPhi());
		controlProps.setProperty("isMandatory", control.isMandatory());
		controlProps.setProperty("calculatedSourceControl", control.isCalculatedSourceControl());
		controlProps.setProperty("conceptDefinitionSource", control.getConceptDefinitionSource());
		controlProps.setProperty("conceptCode", control.getConceptCode());
		controlProps.setProperty("conceptDefinition", control.getConceptDefinition());
		controlProps.setProperty("conceptPreferredName", control.getConceptPreferredName());
		controlProps.setProperty("xPos", control.getxPos());
		controlProps.setProperty("sequenceNumber", control.getSequenceNumber());
		controlProps.setProperty("customLabel", control.getCustomLabel());
		controlProps.setProperty("toolTip", control.getToolTip());
		controlProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
	}
}

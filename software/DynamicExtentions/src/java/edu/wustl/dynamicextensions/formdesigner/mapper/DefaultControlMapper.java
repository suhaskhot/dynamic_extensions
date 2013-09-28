
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Control.LabelPosition;
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

		Long id = controlProps.getLong("id");
		if (id != null) {
			control.setId(id);
		}

		String controlCaption = controlProps.getString(CSDConstants.CONTROL_CAPTION);
		if (controlCaption != null) {
			control.setCaption(controlCaption);
		}
		
		String userDefinedName = controlProps.getString("userDefinedName");
		if (controlCaption != null) {
			control.setUserDefinedName(userDefinedName);
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

		String labelPosition = controlProps.getString("labelPosition");
		if (labelPosition != null) {
			control.setLabelPosition(getLabelPosition(labelPosition));
		}

	}

	protected LabelPosition getLabelPosition(String labelPos) {
		if (labelPos.equalsIgnoreCase("LEFT")) {
			return LabelPosition.LEFT_SIDE;
		} else if (labelPos.equalsIgnoreCase("TOP")) {
			return LabelPosition.TOP;
		} else {
			return null;
		}
	}

	protected String getStringLabelPosition(LabelPosition labelPos) {
		if (labelPos == LabelPosition.LEFT_SIDE) {
			return "LEFT";
		} else if (labelPos == LabelPosition.TOP) {
			return "TOP";
		} else {
			return null;
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
		controlProps.setProperty("labelPosition", getStringLabelPosition(control.getLabelPosition()));
		controlProps.setProperty(CSDConstants.STATUS, CSDConstants.STATUS_SAVED);
		controlProps.setProperty("userDefinedName", control.getUserDefinedName());
	}
}

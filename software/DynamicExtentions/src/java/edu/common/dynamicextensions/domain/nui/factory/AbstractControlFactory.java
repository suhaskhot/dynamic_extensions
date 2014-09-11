package edu.common.dynamicextensions.domain.nui.factory;

import static edu.common.dynamicextensions.nutility.ParserUtil.getBooleanValue;
import static edu.common.dynamicextensions.nutility.ParserUtil.getTextValue;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.nutility.ParserUtil;

public abstract class AbstractControlFactory implements ControlFactory {
	
	public void setControlProps(Control ctrl, Element ctrlEle, int currentRow, int xPos) {	
		String name = getTextValue(ctrlEle, "name");
		String userDefName = getTextValue(ctrlEle, "udn");
	
		if (name == null) {
			throw new RuntimeException("Control name can't be null. Type = " + ctrlEle.getNodeName());
		}
		
		if (userDefName == null) {
			throw new RuntimeException("User defined name of control can't be null. Type = " + ctrlEle.getNodeName());
		}
		
		ctrl.setName(name);
		ctrl.setUserDefinedName(userDefName);
		ctrl.setCaption(getTextValue(ctrlEle, "caption"));
		ctrl.setCustomLabel(getTextValue(ctrlEle, "customLabel"));
		ctrl.setToolTip(getTextValue(ctrlEle, "toolTip", ""));
		ctrl.setPhi(getBooleanValue(ctrlEle, "phi"));
		ctrl.setMandatory(getBooleanValue(ctrlEle, "mandatory"));
		ctrl.setShowInGrid(getBooleanValue(ctrlEle, "showInGrid"));
		ctrl.setShowLabel(getBooleanValue(ctrlEle, "showLabel", true));
		ctrl.setSequenceNumber(currentRow);
		ctrl.setConceptCode(getTextValue(ctrlEle, "conceptCode", null));
		ctrl.setxPos(xPos);
		
		String dbColumn = getTextValue(ctrlEle, "column");
		if (dbColumn != null && !dbColumn.trim().isEmpty()) {
			ctrl.setDbColumnName(dbColumn.trim());
		}
	} 	
	
	public void setSelectProps(SelectControl selectControl, Element ctrlEle, int currentRow, int xPos, String pvDir) {		
		setControlProps(selectControl, ctrlEle, currentRow, xPos);		
			
		PvDataSource pvDataSource = new PvDataSource();
		pvDataSource.setDataType(DataType.STRING); // TODO: Need to read data type of options as well
		selectControl.setPvDataSource(pvDataSource);
		
		String sql = getSql(ctrlEle);
		if (sql == null) {
			List<PermissibleValue> permissibleValues = ParserUtil.getPermissibleValues(ctrlEle, pvDir);
			PvVersion pvVersion = new PvVersion();
			pvVersion.setPermissibleValues(permissibleValues);

			String defVal = getTextValue(ctrlEle, "defaultValue");
			if (defVal != null) {
				PermissibleValue pv = new PermissibleValue();
				pv.setOptionName(defVal);
				pv.setValue(defVal);
				pvVersion.setDefaultValue(pv);
			}
						
			List<PvVersion> pvVersions = new ArrayList<PvVersion>();
			pvVersions.add(pvVersion);			
			pvDataSource.setPvVersions(pvVersions);
		} else {
			pvDataSource.setSql(sql);
		}		
	}
	
	private String getSql(Element optionsParentEl) {
		Element options = (Element)optionsParentEl.getElementsByTagName("options").item(0);
		NodeList sqlNode = options.getElementsByTagName("sql");
		return (sqlNode != null && sqlNode.getLength() == 1) ? 
				sqlNode.item(0).getFirstChild().getNodeValue() : null;  
	}	
}
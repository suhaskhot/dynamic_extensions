package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.napi.ControlValue;

public class ShowPvAction extends Action {

	private static final long serialVersionUID = -3930481754067679419L;
	
	private List<PermissibleValue> listOfPvs = new ArrayList<PermissibleValue>();
	
	private PermissibleValue defaultPv;

	public List<PermissibleValue> getListOfPvs() {
		return listOfPvs;
	}

	public void setListOfPvs(List<PermissibleValue> listOfPvs) {
		this.listOfPvs = listOfPvs;
	}

	public PermissibleValue getDefaultPv() {
		return defaultPv;
	}

	public void setDefaultPv(PermissibleValue defaultPv) {
		this.defaultPv = defaultPv;
	}

	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setPermissibleValues(listOfPvs);
	}

	@Override
	public void reset(ControlValue fieldValue) {
		if (fieldValue.getPermissibleValues() != null) {
			fieldValue.getPermissibleValues().clear();
		}
	}
}

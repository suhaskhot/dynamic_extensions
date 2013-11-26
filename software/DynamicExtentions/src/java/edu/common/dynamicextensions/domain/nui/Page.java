package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;

public class Page extends DynamicExtensionBaseDomainObject {
	private static final long serialVersionUID = 2920126612122931143L;

	private List<Control> controls = new ArrayList<Control>();

	public Page() {
	}

	@Override
	public Long getId() {
		return id;
	}
	
	public boolean isEmptyPage() {
		return controls.isEmpty();
	}

	public List<Control> getControls() {
		return controls;
	}

	public void setControls(List<Control> controls) {
		this.controls = controls;
	}
	
	public void addControl(Control ctrl) {
		this.controls.add(ctrl);
	}
}

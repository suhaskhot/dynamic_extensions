package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Control;

public class ContainerChangeLog {
	private List<Control> addedCtrls = new ArrayList<Control>();
	
	private List<Control> deletedCtrls = new ArrayList<Control>();
	
	private List<Control> editedCtrls = new ArrayList<Control>();

	public List<Control> getAddedCtrls() {
		return addedCtrls;
	}

	public void setAddedCtrls(List<Control> addedCtrls) {
		this.addedCtrls = addedCtrls;
	}

	public List<Control> getDeletedCtrls() {
		return deletedCtrls;
	}

	public void setDeletedCtrls(List<Control> deletedCtrls) {
		this.deletedCtrls = deletedCtrls;
	}

	public List<Control> getEditedCtrls() {
		return editedCtrls;
	}

	public void setEditedCtrls(List<Control> editedCtrls) {
		this.editedCtrls = editedCtrls;
	}
	
	public boolean anyChanges() {
		return !addedCtrls.isEmpty() || !editedCtrls.isEmpty() ||  !deletedCtrls.isEmpty();
	}
}

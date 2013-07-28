package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.napi.ControlValue;

public abstract class SkipAction extends DynamicExtensionBaseDomainObject {

	private static final long serialVersionUID = 7539790039693163100L;
	
	private Control targetCtrl;
	
	@Override
	public Long getId() {
		return id;
	}
		
	public Control getTargetCtrl() {
		return targetCtrl;
	}

	public void setTargetCtrl(Control targetCtrl) {
		this.targetCtrl = targetCtrl;
	}

	public abstract void perform(ControlValue fieldValue);

	public abstract void reset(ControlValue targetControl);
}

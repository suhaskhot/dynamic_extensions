package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.napi.ControlValue;

public abstract class Action extends DynamicExtensionBaseDomainObject {

	private static final long serialVersionUID = 7539790039693163100L;
	
	@Override
	public Long getId() {
		return id;
	}

	public abstract void perform(ControlValue fieldValue);

	public abstract void reset(ControlValue targetControl);
}

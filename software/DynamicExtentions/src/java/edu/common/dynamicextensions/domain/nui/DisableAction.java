package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class DisableAction extends Action {

	private static final long serialVersionUID = -7211808180412341154L;

	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setReadOnly(true);
	}

	@Override
	public void reset(ControlValue fieldValue) {
		fieldValue.setReadOnly(false);
	}

}

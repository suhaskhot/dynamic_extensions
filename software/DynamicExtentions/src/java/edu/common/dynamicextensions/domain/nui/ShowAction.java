package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class ShowAction extends SkipAction {

	private static final long serialVersionUID = -946481681134771177L;

	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setHidden(false);

	}

	@Override
	public void reset(ControlValue fieldValue) {
		fieldValue.setHidden(true);
	}
}

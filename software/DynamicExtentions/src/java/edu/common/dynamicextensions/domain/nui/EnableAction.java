package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class EnableAction extends SkipAction {

	private static final long serialVersionUID = 1873793302286285542L;

	@Override
	public void perform(ControlValue fieldValue) {
		fieldValue.setReadOnly(false);

	}

	@Override
	public void reset(ControlValue fieldValue) {
		fieldValue.setReadOnly(true);
	}
}

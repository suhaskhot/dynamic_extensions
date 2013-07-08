package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class HideAction extends Action {

	private static final long serialVersionUID = -7569240889103608834L;

	@Override
	public void perform(ControlValue targetControl) {
		targetControl.setHidden(true);
	}

	@Override
	public void reset(ControlValue targetControl) {
		targetControl.setHidden(false);

	}
}

package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.nui.UserContext;

public interface FormPublishHook {

	public void process(UserContext userCtxt, Long formId);

}

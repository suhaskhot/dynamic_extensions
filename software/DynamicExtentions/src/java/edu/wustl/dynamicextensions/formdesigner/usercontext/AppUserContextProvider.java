/**
 * 
 */

package edu.wustl.dynamicextensions.formdesigner.usercontext;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.nui.UserContext;

public interface AppUserContextProvider {

	public UserContext getUserContext(HttpServletRequest request);
}

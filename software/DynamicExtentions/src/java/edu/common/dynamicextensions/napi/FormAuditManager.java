package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;

public interface FormAuditManager {

	public void audit(UserContext userCtxt, FormData formData, String operation);

	public void audit(UserContext userCtxt, FormData formData, String operation, JdbcDao jdbcDao);
}

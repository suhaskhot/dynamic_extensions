package edu.common.dynamicextensions.napi;

import java.sql.Blob;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.ndao.JdbcDao;

//
// Persistence layer interface
//
public interface FormDataManager {
	public FormData getFormData(Long containerId, Long recordId);
	
	public FormData getFormData(Container container, Long recordId);
	
	public Long saveOrUpdateFormData(UserContext userCtxt, FormData formData);
	
	public Long saveOrUpdateFormData(UserContext userCtxt, FormData formData, JdbcDao jdbcDao);

	public void deleteFormData(UserContext userCtxt, Long containerId, Long recordId);

	public Blob getFileData(long recordId, FileUploadControl control);
}

package edu.common.dynamicextensions.napi;

import java.util.Date;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.UserContext;

public interface VersionedContainer {
	public Container getContainer(Long formId);
	
	public Container getContainer(Long formId, Date activationDate);
	
	public Container getDraftContainer(Long formId);
	
	public Long saveAsDraft(UserContext usrCtx, Long draftContainerId);
	
	public void publishRetrospective(UserContext usrCtx, Long formId);
	
	public void publishProspective(UserContext usrCtx, Long formId, Date activationDate);

}

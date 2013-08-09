package edu.common.dynamicextensions.nversioning;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.nutility.ContainerChangeLog;
import edu.common.dynamicextensions.nutility.ContainerUtility;

public class VersionedContainer {
	public VersionedContainer() {
		
	}
	
	// TODO:
	public Container getContainer(Long formId) {
		return null;
	}
	
	// TODO:
	public Container getContainer(Long formId, Date activationDate) {
		return null;
	}
	
	// TODO:
	public Container getDraftContainer(Long formId) {
		return null;
	}
	
	// TODO:
	public void publishRetrospective(UserContext usrCtx, Long formId) {
		Container draftContainer = getDraftContainer(formId);
		Container latestContainer = getContainer(formId, Calendar.getInstance().getTime());
		
		ContainerChangeLog changeLog = ContainerUtility.getChangeLog(latestContainer, draftContainer);
		if (!changeLog.anyChanges()) {
			return; // no changes nothing to do
		}
		
		applyChangeAndSave(usrCtx, latestContainer, changeLog);
		
		List<Long> publishedContainerIds = getPublishedContainerIds(formId);
		for (int i = 0; i < publishedContainerIds.size() - 1; ++i) {
			Container publishedContainer = Container.getContainer(publishedContainerIds.get(i));
			applyChangeAndSave(usrCtx, publishedContainer, changeLog);
		}		
	}
	
	//
	// TODO: Validate the activationDate is after 
	//
	public void publishProspective(UserContext usrCtx, Long formId, Date activationDate) {
		Container draftContainer = getDraftContainer(formId);
		nullifyContainerIds(draftContainer);
		
		Long publishedContainerId = draftContainer.save(usrCtx, false);
		activationDate = (activationDate == null) ? Calendar.getInstance().getTime() : activationDate; 
		// insert into versioned_forms (identifier, container_id, date, user) 
		// values(identifier, publishedContainerId, activateDate, usrCtx)		
	}
	
	
	private void nullifyContainerIds(Container container) {
		container.setId(null);
		for (Container sub : container.getAllSubContainers()) {
			sub.setId(null);
		}
	}

	// TODO:
	private List<Long> getPublishedContainerIds(Long formId) {
		return Collections.emptyList();
	}
	
	private void applyChangeAndSave(UserContext usrCtx, Container container, ContainerChangeLog changeLog) {
		applyChange(container, changeLog);
		container.save(usrCtx, false);
	}
	
	private void applyChange(Container container, ContainerChangeLog changeLog) {
		for (Control newCtrl : changeLog.getAddedCtrls()) {
			container.addControl(newCtrl);
		}		
		
		for (Control deletedCtrl : changeLog.getDeletedCtrls()) {
			if (container.getControl(deletedCtrl.getName()) != null) {				
				container.deleteControl(deletedCtrl.getName());
			}			
		}
		
		for (Control editedCtrl : changeLog.getEditedCtrls()) {
			if (container.getControl(editedCtrl.getName()) != null) {
				container.editControl(editedCtrl.getName(), editedCtrl);
			}
		}
		
		for (Map.Entry<String, ContainerChangeLog> editedSf : changeLog.getEditedSubCtrls().entrySet()) {
			SubFormControl sfCtrl = (SubFormControl)container.getControl(editedSf.getKey());
			if (sfCtrl != null) {
				applyChange(sfCtrl.getSubContainer(), editedSf.getValue());
			}
		}
	}
}
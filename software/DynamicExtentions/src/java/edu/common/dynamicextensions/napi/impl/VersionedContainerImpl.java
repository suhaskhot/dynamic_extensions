package edu.common.dynamicextensions.napi.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.domain.nui.VersionedContainerInfo;
import edu.common.dynamicextensions.napi.VersionedContainer;
import edu.common.dynamicextensions.ndao.ContainerDao;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.VersionedContainerDao;
import edu.common.dynamicextensions.nutility.ContainerChangeLog;
import edu.common.dynamicextensions.nutility.ContainerUtility;

public class VersionedContainerImpl implements VersionedContainer {
	private JdbcDao jdbcDao;
	
	public void setJdbcDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
	
	@Override
	public Container getContainer(Long formId) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			return getContainer(jdbcDao, formId);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container: " + formId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}
		
	}
	
	public Container getContainer(JdbcDao jdbcDao, Long formId) {
		return getContainer(jdbcDao, formId, Calendar.getInstance().getTime());
	}
	
	@Override
	public Container getContainer(Long formId, Date activationDate) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			return getContainer(jdbcDao, formId, activationDate);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container: " + formId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}
	}
	
	public Container getContainer(JdbcDao jdbcDao, Long formId, Date activationDate) {		
		Container published = null;
		
		try {
			VersionedContainerDao vdao = new VersionedContainerDao(jdbcDao);
			List<VersionedContainerInfo> versionedContainers = vdao.getPublishedContainersInfo(formId);
			
			Long resultId = null;
			Date resultDate = null;
			for (VersionedContainerInfo info : versionedContainers) {
				if (info.getActivationDate().before(activationDate) && 
					(resultDate == null || info.getActivationDate().after(resultDate))) {

					resultId = info.getContainerId();
					resultDate = info.getActivationDate();
				}
			}
			
			if (resultId != null) {
				ContainerDao cdao = new ContainerDao(jdbcDao);
				published = cdao.getById(resultId);				
			}
			
			return published;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining published container: " + formId, e);
		} 
	}
	
	@Override
	public Container getDraftContainer(Long formId) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			return getDraftContainer(jdbcDao, formId);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining draft container:" + formId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}
	}
	
	public Container getDraftContainer(JdbcDao jdbcDao, Long formId) {
		Container draft = null;
		
		try {
			VersionedContainerDao vdao = new VersionedContainerDao(jdbcDao);
			VersionedContainerInfo info = vdao.getDraftContainerInfo(formId);
			
			if (info != null) {
				ContainerDao cdao = new ContainerDao(jdbcDao);
				draft = cdao.getById(info.getContainerId());
			}
			
			return draft;
		} catch (Exception e) {
			throw new RuntimeException("Error obtainer draft container:" + formId, e);
		}
	}
	
	@Override
	public Long saveAsDraft(UserContext usrCtx, Long draftContainerId) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			return saveAsDraft(jdbcDao, usrCtx, draftContainerId);
		} catch (Exception e) {
			throw new RuntimeException("Error saving draft container:" + draftContainerId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}		
	}
	
	public Long saveAsDraft(JdbcDao jdbcDao, UserContext usrCtx, Long draftContainerId) {
		VersionedContainerDao vdao = new VersionedContainerDao(jdbcDao);
		Long formId = vdao.getFormIdByDraftContainerId(draftContainerId);
		
		if (formId == null) {
			VersionedContainerInfo vc = new VersionedContainerInfo();
			vc.setContainerId(draftContainerId);
			vc.setCreatedBy(usrCtx.getUserId());
			vc.setCreationTime(Calendar.getInstance().getTime());
			vc.setStatus("draft");
			
			vdao.insertVersionedContainerInfo(vc);
			formId = vc.getFormId();
		}
		
		return formId;
	}
	
	@Override
	public void publishRetrospective(UserContext usrCtx, Long formId) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			publishRetrospective(jdbcDao, usrCtx, formId);
		} catch (Exception e) {
			throw new RuntimeException("Error saving container retrospectively:" + formId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}				
	}
	
	public void publishRetrospective(JdbcDao jdbcDao, UserContext usrCtx, Long formId) {		
		List<Long> publishedIds = getPublishedContainerIds(jdbcDao, formId); 		
		if (publishedIds.isEmpty()) {
			publishProspective(usrCtx, formId, Calendar.getInstance().getTime());
			return;
		}
		
		try {
			ContainerDao cdao = new ContainerDao(jdbcDao);
			Container draftContainer = getDraftContainer(formId);			
			Container latestContainer = cdao.getById(publishedIds.get(publishedIds.size() - 1));
			
			ContainerChangeLog changeLog = ContainerUtility.getChangeLog(latestContainer, draftContainer);
			if (!changeLog.anyChanges()) {
				return; // no changes nothing to do
			}
			
			applyChangeAndSave(jdbcDao, usrCtx, latestContainer, changeLog);
			
			for (int i = 0; i < publishedIds.size() - 1; ++i) {
				Container publishedContainer = cdao.getById(publishedIds.get(i));
				applyChangeAndSave(jdbcDao, usrCtx, publishedContainer, changeLog);
			}					
		} catch (Exception e) {
			throw new RuntimeException("Error publishing container retrospectively", e);
		}
	}
	
	//
	// TODO: Validate the activationDate is after 
	//
	@Override
	public void publishProspective(UserContext usrCtx, Long formId, Date activationDate) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = this.jdbcDao != null ? this.jdbcDao : new JdbcDao();
			publishProspective(jdbcDao, usrCtx, formId, activationDate);
		} catch (Exception e) {
			throw new RuntimeException("Error saving container prospectively:" + formId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}						
	}
		
	public void publishProspective(JdbcDao jdbcDao, UserContext usrCtx, Long formId, Date activationDate) {
		Container draftContainer = getDraftContainer(formId);
		nullifyContainerIds(draftContainer);
		
		Long publishedContainerId = draftContainer.save(usrCtx, jdbcDao, false);
		activationDate = (activationDate == null) ? Calendar.getInstance().getTime() : activationDate;
		
		VersionedContainerInfo vc = new VersionedContainerInfo();
		vc.setFormId(formId);
		vc.setContainerId(publishedContainerId);
		vc.setActivationDate(activationDate);
		vc.setCreatedBy(usrCtx.getUserId());
		vc.setCreationTime(Calendar.getInstance().getTime());
		vc.setStatus("published");
		
		VersionedContainerDao vdao = new VersionedContainerDao(jdbcDao);
		vdao.insertVersionedContainerInfo(vc);
	}
	
	private void nullifyContainerIds(Container container) {
		container.setId(null);
		for (Container sub : container.getAllSubContainers()) {
			sub.setId(null);
		}
	}

	private List<Long> getPublishedContainerIds(JdbcDao jdbcDao, Long formId) {
		VersionedContainerDao vdao = new VersionedContainerDao(jdbcDao);
		List<VersionedContainerInfo> containerInfoList = vdao.getPublishedContainersInfo(formId);
		List<Long> publishedIds = new ArrayList<Long>();
		
		for (VersionedContainerInfo containerInfo : containerInfoList) {
			publishedIds.add(containerInfo.getContainerId());
		}
		
		return publishedIds;
	}
	
	private void applyChangeAndSave(JdbcDao jdbcDao, UserContext usrCtx, Container container, ContainerChangeLog changeLog) {
		applyChange(container, changeLog);
		container.save(usrCtx, jdbcDao, false);
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

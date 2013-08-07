
package edu.wustl.dynamicextensions.formdesigner.resource.facade;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.dynamicextensions.formdesigner.mapper.ContainerMapper;
import edu.wustl.dynamicextensions.formdesigner.mapper.ControlMapper;
import edu.wustl.dynamicextensions.formdesigner.mapper.Properties;
import edu.wustl.dynamicextensions.formdesigner.mapper.PvMapper;

public class ContainerFacade {

	private Container container = null;
	private static ContainerMapper containerMapper = new ContainerMapper();

	/**
	 * @param container
	 */
	private ContainerFacade(Container container) {
		this.container = container;
	}

	/**
	 * @param containerProps
	 * @return
	 * @throws Exception 
	 */
	public static ContainerFacade createContainer(Properties containerProps) throws Exception {
		return new ContainerFacade(containerMapper.propertiesToContainer(containerProps, true));
	}

	public void updateContainer(Properties containerProps) throws Exception {
		containerMapper.propertiesToContainer(containerProps, container, true);
	}

	/**
	 * @param id
	 * @return
	 */
	public static ContainerFacade loadContainer(Long id) {
		Container newContainer = Container.getContainer(id);

		return new ContainerFacade(newContainer);
	}

	/**
	 * @param controlProps
	 * @throws Exception 
	 */
	public void createControl(Properties controlProps) throws Exception {
		Control control = new ControlMapper().propertiesToControl(controlProps);
		container.addControl(control);
	}

	public void persistContainer(UserContext userContext) {
		container.save(userContext);
	}

	public String getHTML(HttpServletRequest request) {

		Stack<FormData> formDataStack = new Stack<FormData>();
		CacheManager.addObjectToCache(request, DEConstants.FORM_DATA_STACK, formDataStack);
		formDataStack.push(new FormData(container));
		return container.render();
	}

	public Properties getProperties() {
		return containerMapper.containerToProperties(container);
	}

	public File getPvFile(String controlName) throws IOException {

		Control control = container.getControl(controlName,"\\.");
		File pvFile = null;
		if (control instanceof SelectControl) {
			String uploadedFileLocation = "/tmp/" + new Date().getTime() + "_Permissible_Values.csv";
			pvFile = PvMapper.getPvFile(uploadedFileLocation, ((SelectControl) control).getPvs());
		}
		return pvFile;
	}

}

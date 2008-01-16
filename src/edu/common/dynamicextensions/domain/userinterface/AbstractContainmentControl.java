
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;

/**
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_ABSTRACT_CONTAINMENT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public abstract class AbstractContainmentControl extends Control implements AbstractContainmentControlInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		ContainerInterface containerInterface = this.getContainer();
		this.setIsSubControl(true);
		  if (this.getParentContainer().showAssociationControlsAsLink)
          {
              String link = containerInterface.generateLink(containerInterface);
              
              link = UserInterfaceiUtility.getControlHTMLAsARow(this, link);
              return link;
          }
         
		String subContainerHTML = "";
		if (isCardinalityOneToMany())
		{
			List<Map<BaseAbstractAttributeInterface, Object>> valueMapList = (List<Map<BaseAbstractAttributeInterface, Object>>) value;
			subContainerHTML = containerInterface.generateControlsHTMLAsGrid(valueMapList);
		}
		else
		{
			if (value != null && ((List) value).size() > 0)
			{
				Map<BaseAbstractAttributeInterface, Object> displayContainerValueMap = ((List<Map<BaseAbstractAttributeInterface, Object>>) value).get(0);
				containerInterface.setContainerValueMap(displayContainerValueMap);
			}
			this.getContainer().setShowAssociationControlsAsLink(true);
			subContainerHTML = containerInterface.generateControlsHTML();
		}
		return subContainerHTML;
	}

	protected abstract String generateViewModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * 
	 */
	protected ContainerInterface container;

	/**
	 * @return container
	 * @hibernate.many-to-one cascade="save-update" column="CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
	 */
	public ContainerInterface getContainer()
	{
		return container;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface#setContainer(edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface)
	 */
	public void setContainer(ContainerInterface container)
	{
		this.container = container;
	}
	

}
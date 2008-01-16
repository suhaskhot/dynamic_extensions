/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

/**
 * @author vishvesh_mulay
 * @hibernate.joined-subclass table="DYEXTN_CONTAINMENT_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ContainmentAssociationControl extends AbstractContainmentControl implements ContainmentAssociationControlInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ContainmentAssociationControl()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#getControlHTML(java.lang.String)
	 */
	protected String getControlHTML(String htmlString)
	{
		return htmlString;
	}


	/**
	 * This method returns true if the cardinality of the Containment Association is One to Many.
	 * @return true if Caridnality is One to Many, false otherwise.
	 *//*
	public boolean isCardinalityOneToMany()
	{
		return UserInterfaceiUtility.isCardinalityOneToMany(this);
	}
*/
	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		String subContainerHTML = "";
		if (isCardinalityOneToMany())
		{
			List<Map<BaseAbstractAttributeInterface, Object>> valueMapList = (List<Map<BaseAbstractAttributeInterface, Object>>) value;
			String oldMode = this.getContainer().getMode();
			this.getContainer().setMode("view");
			subContainerHTML = this.getContainer().generateControlsHTMLAsGrid(valueMapList);
			this.getContainer().setMode(oldMode);
		}
		else
		{
			if (value != null && ((List) value).size() > 0)
			{
				Map<BaseAbstractAttributeInterface, Object> displayContainerValueMap = ((List<Map<BaseAbstractAttributeInterface, Object>>) value).get(0);
				this.getContainer().setContainerValueMap(displayContainerValueMap);
			}
			this.getContainer().setShowAssociationControlsAsLink(true);
			String oldMode = this.getContainer().getMode();
			this.getContainer().setMode("view");
			subContainerHTML = this.getContainer().generateControlsHTML();
			this.getContainer().setMode(oldMode);
		}
		return subContainerHTML;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface#generateLinkHTML()
	 */
	public String generateLinkHTML() throws DynamicExtensionsSystemException
	{
		String detailsString = "Details";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<a href='#' style='cursor:hand' class='" + cssClass + "' ");
		stringBuffer.append("onclick='showChildContainerInsertDataPage(");
		stringBuffer.append(this.getParentContainer().getIncontextContainer().getId() + ",this");
		stringBuffer.append(")'>");
		stringBuffer.append(detailsString);
		stringBuffer.append("</a>");

		return stringBuffer.toString();
	}

	/**
	 * This method returns true if the cardinality of the Containment Association is One to Many.
	 * @return true if Caridnality is One to Many, false otherwise.
	 */
	public boolean isCardinalityOneToMany()
	{
		boolean isOneToMany = false;
		AssociationInterface associationInterface = (AssociationInterface) this.getBaseAbstractAttribute();
		RoleInterface targetRole = associationInterface.getTargetRole();
		if (targetRole.getMaximumCardinality() == Cardinality.MANY)
		{
			isOneToMany = true;
		}
		return isOneToMany;
	}
}


/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author chetan_patil
 *
 */
public class UserInterfaceiUtility
{
	/**
	 * 
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */

	public static String generateHTML(ContainerInterface container) throws DynamicExtensionsSystemException
	{
		StringBuffer stringBuffer = new StringBuffer();
		List<ControlInterface> controlsList = new ArrayList<ControlInterface>(container.getControlCollection());
		Collections.sort(controlsList);

		stringBuffer.append("<table summary='' cellpadding='3' cellspacing='0'  align='center' width = '100%'>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formMessage' colspan='3'>");
		stringBuffer.append(container.getRequiredFieldIndicatior() + "&nbsp;");
		stringBuffer.append(container.getRequiredFieldWarningMessage());
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");

		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formTitle' colspan='3' align='left'>");
		stringBuffer.append(ApplicationProperties.getValue("app.add") + "&nbsp;");
		stringBuffer.append(container.getCaption());
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");

		for (ControlInterface control : controlsList)
		{
			if (control instanceof ContainmentAssociationControlInterface)
			{
				ContainmentAssociationControlInterface containmentAssociationControl = (ContainmentAssociationControlInterface) control;
				if (containmentAssociationControl.isCardinalityOneToMany())
				{
					ContainerInterface subContainer = containmentAssociationControl.getContainer();
					generateHTMLforGrid(stringBuffer, containmentAssociationControl, subContainer);
				}
				else
				{
					generateHTMLforControl(stringBuffer, control, container);
				}
			}
			else
			{
				generateHTMLforControl(stringBuffer, control, container);
			}
		}
		stringBuffer.append("</table>");
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param stringBuffer
	 * @throws DynamicExtensionsSystemException 
	 */
	private static void generateHTMLforControl(StringBuffer stringBuffer, ControlInterface controlInterface, ContainerInterface containerInterface)
			throws DynamicExtensionsSystemException
	{
		boolean isControlRequired = isControlRequired(controlInterface);
		stringBuffer.append("<tr>");
		if (isControlRequired)
		{
			stringBuffer.append("<td class='formRequiredNotice' width='2%'>");
			stringBuffer.append(containerInterface.getRequiredFieldIndicatior() + "&nbsp;");
			stringBuffer.append("</td>");

			stringBuffer.append("<td class='formRequiredLabel' width='20%'>");
			stringBuffer.append(controlInterface.getCaption());
			stringBuffer.append("</td>");
		}
		else
		{
			stringBuffer.append("<td class='formRequiredNotice' width='2%'>");
			stringBuffer.append("&nbsp;");
			stringBuffer.append("</td>");

			stringBuffer.append("<td class='formLabel' width='20%'>");
			stringBuffer.append(controlInterface.getCaption());
		}

		stringBuffer.append("<td class='formField'>");
		stringBuffer.append(controlInterface.generateHTML());
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");
	}

	/**
	 * 
	 * @param stringBuffer
	 * @param controlInterface
	 * @param containerInterface
	 * @throws DynamicExtensionsSystemException
	 */
	@SuppressWarnings("unchecked")
	private static void generateHTMLforGrid(StringBuffer stringBuffer, ContainmentAssociationControlInterface containmentAssociationControl,
			ContainerInterface subContainer) throws DynamicExtensionsSystemException
	{
		List<ControlInterface> controlsList = new ArrayList<ControlInterface>(subContainer.getControlCollection());
		Collections.sort(controlsList);

		stringBuffer.append("<tr><td>");
		stringBuffer.append("<div style='display:none' id='" + subContainer.getId() + "_substitutionDiv'>");
		stringBuffer.append("<table>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formField' width='1%'>");
		stringBuffer.append("<input type='checkbox' name='deleteRow' value=''/>");
		stringBuffer.append("</td>");
		for (ControlInterface control : controlsList)
		{
			stringBuffer.append("<td class='formField'>");
			stringBuffer.append(control.generateHTML());
			stringBuffer.append("</td>");
		}
		stringBuffer.append("</tr>");
		stringBuffer.append("</table>");
		stringBuffer.append("</div>");

		stringBuffer.append("<input type='hidden' name='" + subContainer.getId() + "_rowCount' id= '" + subContainer.getId()
				+ "_rowCount' value='0'/> ");
		stringBuffer.append("</td></tr>");
		
		
		stringBuffer.append("<tr class='formRequiredNotice'>");

		stringBuffer.append("<td class='formRequiredNotice' width='2%'>");
		stringBuffer.append("&nbsp;");
		stringBuffer.append("</td>");
		stringBuffer.append("<td class='formLabel' width='20%'>");
		stringBuffer.append(subContainer.getCaption());
		stringBuffer.append("</td>");

		stringBuffer.append("<td class='formField'>");
		stringBuffer.append("<table id='" + subContainer.getId()
				+ "_table' summary='' cellpadding='3' cellspacing='0' align='center' width = '100%'>");

		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formField' width='1%'>");
		stringBuffer.append("<input type='checkbox' name='dummy' disabled/>");
		stringBuffer.append("</td>");
		for (ControlInterface control : controlsList)
		{
			boolean isControlRequired = isControlRequired(control);
			if (isControlRequired)
			{
				stringBuffer.append("<th class='formRequiredLabel'>");
				stringBuffer.append(subContainer.getRequiredFieldIndicatior() + "&nbsp;" + control.getCaption());
			}
			else
			{
				stringBuffer.append("<th class='formLabel'>");
				stringBuffer.append("&nbsp;" + control.getCaption());
			}
			stringBuffer.append("</th>");
		}

		stringBuffer.append("<th class='formLabel' align='left'>");
		stringBuffer.append("<button type='button' class='actionButton' id='addMore' onclick=\"addRow('" + subContainer.getId() + "')\">");
		stringBuffer.append(ApplicationProperties.getValue("eav.button.AddRow"));
		stringBuffer.append("</button>");
		stringBuffer.append("</th>");

		stringBuffer.append("</tr>");
		stringBuffer.append("</table>");
		
		stringBuffer.append("<table><tr><td>");
		stringBuffer.append("<button type='button' class='actionButton' id='removeRow' onclick=\"removeCheckedRow('" + subContainer.getId() + "')\">");
		stringBuffer.append(ApplicationProperties.getValue("buttons.delete"));
		stringBuffer.append("</button>");
		stringBuffer.append("</td></tr></table>");
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");
		
		
	}

	/**
	 * 
	 * @param controlInterface
	 * @return
	 */
	public static boolean isControlRequired(ControlInterface controlInterface)
	{
		AbstractAttributeInterface abstractAttribute = controlInterface.getAbstractAttribute();
		Collection<RuleInterface> ruleCollection = abstractAttribute.getRuleCollection();
		boolean required = false;
		if (ruleCollection != null && !ruleCollection.isEmpty())
		{
			for (RuleInterface attributeRule : ruleCollection)
			{
				if (attributeRule.getName().equals("required"))
				{
					required = true;
					break;
				}
			}
		}
		return required;
	}

}

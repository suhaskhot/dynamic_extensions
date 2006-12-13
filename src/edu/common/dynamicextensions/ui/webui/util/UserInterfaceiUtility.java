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
	@SuppressWarnings("unchecked")
	public static String generateHTML(ContainerInterface containerInterface) throws DynamicExtensionsSystemException
	{
		StringBuffer stringBuffer = new StringBuffer();
		List<ControlInterface> controlsList = new ArrayList<ControlInterface>(containerInterface.getControlCollection());
		Collections.sort(controlsList);

		stringBuffer.append("<table summary='' cellpadding='3' cellspacing='0'  align='center' width = '100%'>");
		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formMessage' colspan='3'>");
		stringBuffer.append(containerInterface.getRequiredFieldIndicatior() + "&nbsp;");
		stringBuffer.append(containerInterface.getRequiredFieldWarningMessage());
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");

		stringBuffer.append("<tr>");
		stringBuffer.append("<td class='formTitle' colspan='3' align='left'>");
		stringBuffer.append(ApplicationProperties.getValue("app.add") + "&nbsp;");
		stringBuffer.append(containerInterface.getCaption());
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");

		for (ControlInterface controlInterface : controlsList)
		{
			generateHTMLforControl(stringBuffer, controlInterface, containerInterface);
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
	 * @param controlInterface
	 * @return
	 */
	private static boolean isControlRequired(ControlInterface controlInterface)
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

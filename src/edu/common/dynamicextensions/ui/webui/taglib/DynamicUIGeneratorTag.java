
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.domain.userinterface.SelectControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class DynamicUIGeneratorTag extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	protected ContainerInterface containerInterface = null;

	/**
	 * 
	 * @return
	 */
	public ContainerInterface getContainerInterface()
	{
		return containerInterface;
	}

	/**
	 * 
	 * @param containerInterface
	 */
	public void setContainerInterface(ContainerInterface containerInterface)
	{
		this.containerInterface = containerInterface;
	}

	/**
	 * Validates all the attributes passed to the tag
	 * @return boolean - true if all the attributes passed to the tag are valid
	 * @since TODO
	 */
	private boolean isDataValid()
	{
		if (this.getContainerInterface() == null)
		{
			Logger.out.debug("Container interface is null");
			return false;
		}
		return true;
	}

	/**
	 * This method contains no operations.
	 * @return int SKIP_BODY
	 * @since TODO
	 */
	public int doStartTag()
	{
		Logger.out.debug("Entering Selector List Tag ...");
		return SKIP_BODY;
	}

	/**
	 * 
	 */
	public int doEndTag()
	{
		if (!isDataValid())
		{
			return EVAL_PAGE;
		}
		try
		{
			String generatedHTML =  generateHTML();
			JspWriter out = pageContext.getOut();
			out.println(generatedHTML);
		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.debug("DynamicExtensionsSystemException. No response generated.");
		}
		catch (IOException e)
		{
			Logger.out.debug("IOException. No response generated.");
		}
		return EVAL_PAGE;
	}

	/**
	 * @throws DynamicExtensionsSystemException 
	 * 
	 *
	 */
	public String  generateHTML() throws DynamicExtensionsSystemException
	{
		StringBuffer stringBuffer = new StringBuffer();
		List<ControlInterface> controlsList = new ArrayList<ControlInterface>(containerInterface
				.getControlCollection());
		Collections.sort(controlsList);

		stringBuffer
				.append("<table summary='' cellpadding='3' cellspacing='0'  align='center' width = '100%'>");
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
			generateHTMLforControl(stringBuffer,controlInterface);
		}
		stringBuffer.append("</table>");
		return stringBuffer.toString();
	}
	
/**
 * 
 * @param stringBuffer
 * @throws DynamicExtensionsSystemException 
 */
	private void generateHTMLforControl(StringBuffer stringBuffer,ControlInterface controlInterface) throws DynamicExtensionsSystemException
	{
		AbstractAttributeInterface abstractAttribute = controlInterface.getAbstractAttribute();
		if (abstractAttribute instanceof AttributeInterface
				|| abstractAttribute instanceof SelectControl)
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
		
	}

	/**
	 * 
	 * @param controlInterface
	 * @return
	 */
	public boolean isControlRequired(ControlInterface controlInterface)
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

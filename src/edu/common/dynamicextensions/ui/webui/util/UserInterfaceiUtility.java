/**
 *
 */

package edu.common.dynamicextensions.ui.webui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domain.userinterface.AbstractContainmentControl;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AssociationMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FormControlNotesInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.common.dynamicextensions.util.global.DEConstants.Cardinality;

/**
 * @author chetan_patil
 *
 */
public class UserInterfaceiUtility
{

	/**
	 *
	 * @param stringBuffer
	 * @param controlInterface
	 * @param containerInterface
	 * @throws DynamicExtensionsSystemException
	 */
	public static String generateHTMLforGrid(ContainerInterface subContainer,
			List<Map<BaseAbstractAttributeInterface, Object>> valueMapList)
			throws DynamicExtensionsSystemException
	{
		StringBuffer stringBuffer = new StringBuffer();
		int rowCount = 0;
		if (valueMapList != null)
		{
			rowCount = valueMapList.size();
		}

		List<ControlInterface> controls = new ArrayList<ControlInterface>(subContainer
				.getAllControls());

		// Do not sort the controls list; it jumbles up the attributes order.
		//Collections.sort(controlsList);

		stringBuffer.append("<tr width='100%'><td colspan='3'<div style='display:none' id='");
		stringBuffer.append(subContainer.getId());
		stringBuffer.append("_substitutionDiv'><table>");
		subContainer.setContainerValueMap(new HashMap<BaseAbstractAttributeInterface, Object>()); //empty hashmap to generate hidden row
		stringBuffer.append(getContainerHTMLAsARow(subContainer, -1));
		stringBuffer.append("</table></div><input type='hidden' name='");

		stringBuffer.append(subContainer.getId());
		stringBuffer.append("_rowCount' id= '");
		stringBuffer.append(subContainer.getId());
		stringBuffer.append("_rowCount' value='");
		stringBuffer.append(rowCount);
		stringBuffer
				.append("'/> </td></tr><tr width='100%'><td class='formFieldContainer_withoutBorder' colspan='3' align='center'><table cellpadding='3' cellspacing='0' align='center' width='100%'>");

		String tableHeaderClass = "formLabelWithTopBorder";
		String formFieldContainerClass = "formFieldContainerWithTopBorder";
		String formformRequiredNoticeClass = "formRequiredNoticeWithTopBorder";
		if (subContainer.getAddCaption())
		{
			stringBuffer.append("<tr width='100%'><td class='td_color_6e81a6' colspan='3' align='left'>");
			stringBuffer.append(DynamicExtensionsUtility
					.getFormattedStringForCapitalization(subContainer.getCaption()));
			stringBuffer.append("</td></tr>");

			tableHeaderClass = "formLabel_withoutBorder";
			formFieldContainerClass = "formFieldContainer_withoutBorder";
			formformRequiredNoticeClass = "formRequiredNotice_withoutBorder";
		}

		stringBuffer.append("<tr width='100%'><td colspan='3' width='100%'>");

		// For category attribute controls, if heading and/or notes are specified, then
		// render the UI that displays heading followed by notes for particular
		// category attribute controls.
		for (ControlInterface control : controls)
		{
			if (control.getHeading() != null && control.getHeading().length() != 0)
			{
				stringBuffer.append("<div width=100% class='td_color_6e81a6' align='left'>"
						+ control.getHeading() + "</div>");
			}

			if (control.getFormNotes() != null && control.getFormNotes().size() != 0)
			{
				stringBuffer.append("<div style='width:100%'>&nbsp</div>");

				for (FormControlNotesInterface fcNote : control.getFormNotes())
				{
					stringBuffer.append("<div style='width:100%' class='notes' align='left'>"
							+ fcNote.getNote() + "</div>");
				}
			}
		}

		stringBuffer.append("<table id='");

		stringBuffer.append(subContainer.getId());

		stringBuffer
				.append("_table' cellpadding='3' cellspacing='3' border='0' align='center' width='100%'><tr width='100%' class='formLabel_withoutBorder'><th width='1%'>&nbsp;</th>");

		for (ControlInterface control : controls)
		{
			boolean isControlRequired = isControlRequired(control);

			stringBuffer.append("<th>");
			if (isControlRequired)
			{

				stringBuffer.append("<span class='font_red'>");
				stringBuffer.append(subContainer.getRequiredFieldIndicatior());
				stringBuffer.append("</span>&nbsp;&nbsp;<span class='font_bl_nor'>");
				stringBuffer.append(DynamicExtensionsUtility
						.getFormattedStringForCapitalization(control.getCaption()));
				stringBuffer.append("</span>");
			}
			else
			{
				stringBuffer.append("&nbsp;&nbsp;<span class='font_bl_nor'>");
				stringBuffer.append(DynamicExtensionsUtility
						.getFormattedStringForCapitalization(control.getCaption()));
				stringBuffer.append("</span>");
			}
			stringBuffer.append("</th>");
		}

		stringBuffer.append("</tr>");
		if (valueMapList != null)
		{
			int index = 1;
			for (Map<BaseAbstractAttributeInterface, Object> rowValueMap : valueMapList)
			{
				subContainer.setContainerValueMap(rowValueMap);
				stringBuffer.append(getContainerHTMLAsARow(subContainer, index));
				index++;
			}
		}

		stringBuffer.append("</table>");

		if (subContainer.getMode().equals("edit"))
		{
			stringBuffer
					.append("<table cellpadding='3' cellspacing='0' align='center' width='100%' class='td_color_e3e2e7'><tr><td align='left'><img src='de/images/b_delete.gif' alt='Delete' width='59' height='20' hspace='3' align='absmiddle' onclick=\"removeCheckedRow('");
			stringBuffer.append(subContainer.getId());
			stringBuffer.append("')\"><map alt='Delete'><area href='javascript:removeCheckedRow('");
			stringBuffer.append(subContainer.getId());
			stringBuffer
					.append("')' shape='default'></map></td><td align='right'><img src='de/images/b_add_more.gif' alt='Add More' width='76' height='20' hspace='3' vspace='2' align='absmiddle' onclick=\"addRow('");
			//stringBuffer.append("<button type='button' class='actionButton' id='removeRow' onclick=\"removeCheckedRow('" + subContainer.getId()
			//		+ "')\">");
			//stringBuffer.append(ApplicationProperties.getValue("buttons.delete"));
			//stringBuffer.append("</button>");
			stringBuffer.append(subContainer.getId());
			stringBuffer.append("')\"><map alt='Add More'><area href='javascript:\"addRow('");
			stringBuffer.append(subContainer.getId());
			stringBuffer.append("')' shape='default'></map></td></tr></table>");
			//stringBuffer.append("<button type='button' class='actionButton' id='addMore' onclick=\"addRow('" + subContainer.getId() + "')\">");
			//stringBuffer.append(ApplicationProperties.getValue("eav.button.AddRow"));
			//stringBuffer.append("</button>");
		}

		stringBuffer.append("</td></tr></table></td></tr>");
		return stringBuffer.toString();
	}

	/**
	 *
	 * @param controlInterface
	 * @return
	 */
	public static boolean isControlRequired(ControlInterface controlInterface)
	{
		if (controlInterface.getBaseAbstractAttribute() instanceof AssociationMetadataInterface)
		{
			return false;
		}
		AttributeMetadataInterface attributeMetadataInterface = (AttributeMetadataInterface) controlInterface
				.getBaseAbstractAttribute();
		Collection<RuleInterface> ruleCollection = attributeMetadataInterface.getRuleCollection();
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

	/**
	 *
	 * @param containerStack
	 * @param containerInterface
	 * @param valueMapStack
	 * @param valueMap
	 */
	public static void addContainerInfo(Stack<ContainerInterface> containerStack,
			ContainerInterface containerInterface,
			Stack<Map<BaseAbstractAttributeInterface, Object>> valueMapStack,
			Map<BaseAbstractAttributeInterface, Object> valueMap)
	{
		containerStack.push(containerInterface);
		valueMapStack.push(valueMap);
	}

	/**
	 *
	 * @param containerStack
	 * @param valueMapStack
	 */
	public static void removeContainerInfo(Stack<ContainerInterface> containerStack,
			Stack<Map<BaseAbstractAttributeInterface, Object>> valueMapStack)
	{
		containerStack.pop();
		valueMapStack.pop();
	}

	/**
	 * @param request
	 */
	public static void clearContainerStack(HttpServletRequest request)
	{
		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, DEConstants.CONTAINER_INTERFACE);
		if (containerInterface != null && containerInterface.getId() != null)
		{
			request.setAttribute("containerIdentifier", containerInterface.getId().toString());
		}

		CacheManager.addObjectToCache(request, DEConstants.CONTAINER_STACK, null);
		CacheManager.addObjectToCache(request, DEConstants.VALUE_MAP_STACK, null);
		CacheManager.addObjectToCache(request, DEConstants.CONTAINER_INTERFACE, null);
		CacheManager.addObjectToCache(request, "rootRecordIdentifier", null);
	}

	/**
	 * @param container
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private static String getContainerHTMLAsARow(ContainerInterface container, int rowId)
			throws DynamicExtensionsSystemException
	{

		StringBuffer stringBuffer = new StringBuffer();
		Map<BaseAbstractAttributeInterface, Object> containerValueMap = container
				.getContainerValueMap();
		List<ControlInterface> controlsList = new ArrayList<ControlInterface>(container
				.getAllControls());

		// Do not sort the controls list; it jumbles up the attribute order
		//Collections.sort(controlsList);

		String rowClass = "formField_withoutBorder";
		if (rowId % 2 == 0)
		{
			rowClass = "td_color_f0f2f6";
		}
		stringBuffer.append("<tr width='100%'class='");
		stringBuffer.append(rowClass);
		stringBuffer.append("'><td width='1%'>");
		if (container.getMode().equals("edit"))
		{
			stringBuffer.append("<input type='checkbox' name='deleteRow' value='' id='1'/>");
		}
		else
		{
			stringBuffer.append("&nbsp;");
		}
		stringBuffer.append("</td>");
		for (ControlInterface control : controlsList)
		{
			String controlHTML = "";
			control.setIsSubControl(true);

			if (control instanceof AbstractContainmentControlInterface)
			{
				controlHTML = ((AbstractContainmentControlInterface) control).generateLinkHTML();
			}
			else
			{
				if (containerValueMap != null)
				{
					Object value = containerValueMap.get(control.getBaseAbstractAttribute());
					control.setValue(value);
				}
				controlHTML = control.generateHTML();
				if (rowId != -1)
				{
					String oldName = control.getHTMLComponentName();
					String newName = oldName + "_" + rowId;
					controlHTML = controlHTML.replaceAll(oldName, newName);
				}
			}

			stringBuffer.append("<td valign='middle' NOWRAP='true'>");
			stringBuffer.append(controlHTML);
			stringBuffer.append("</td>");
		}
		stringBuffer.append("</tr>");

		return stringBuffer.toString();
	}

	/**
	 * This method returns the associationControl for a given Container and its child caintener id
	 * @param containerInterface
	 * @param childContainerId
	 * @return
	 */
	public static AbstractContainmentControlInterface getAssociationControl(
			ContainerInterface containerInterface, String childContainerId)
	{
		Collection<ControlInterface> controlCollection = containerInterface.getAllControls();
		for (ControlInterface control : controlCollection)
		{
			if (control instanceof AbstractContainmentControlInterface)
			{
				AbstractContainmentControl abstractContainmentControl = (AbstractContainmentControl) control;
				Long containerId = abstractContainmentControl.getContainer().getId();
				if (containerId != null)
				{
					String associationControlId = containerId.toString();
					if (associationControlId.equals(childContainerId))
					{
						return abstractContainmentControl;
					}
					else
					{
						abstractContainmentControl = (AbstractContainmentControl) getAssociationControl(
								abstractContainmentControl.getContainer(), childContainerId);
						if (abstractContainmentControl != null)
						{
							return abstractContainmentControl;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param controlInterface
	 * @param htmlString
	 * @return
	 */
	public static String getControlHTMLAsARow(ControlInterface controlInterface, String htmlString)
	{
		boolean isControlRequired = UserInterfaceiUtility.isControlRequired(controlInterface);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<tr><td class='formRequiredNotice_withoutBorder' width='2%'>");
		if (isControlRequired)
		{
			stringBuffer.append(controlInterface.getParentContainer().getRequiredFieldIndicatior());
			stringBuffer.append("&nbsp;</td><td class='formRequiredLabel_withoutBorder'>");
		}
		else
		{
			stringBuffer.append("&nbsp;</td><td class='formRequiredLabel_withoutBorder'>");
		}
		stringBuffer.append(DynamicExtensionsUtility
				.getFormattedStringForCapitalization(controlInterface.getCaption()));
		stringBuffer.append("</td><td class='formField_withoutBorder'>");
		stringBuffer.append(htmlString);
		stringBuffer.append("</td></tr>");
		return stringBuffer.toString();
	}

	/**
	 * This method returns true if the cardinality of the Containment Association is One to Many.
	 * @return true if Caridnality is One to Many, false otherwise.
	 */
	public static boolean isCardinalityOneToMany(AbstractContainmentControlInterface control)
	{
		boolean isOneToMany = false;
		AssociationInterface associationInterface = (AssociationInterface) control
				.getBaseAbstractAttribute();
		RoleInterface targetRole = associationInterface.getTargetRole();
		if (targetRole.getMaximumCardinality() == Cardinality.MANY)
		{
			isOneToMany = true;
		}
		return isOneToMany;
	}

	/**
	 *
	 * @param container
	 * @return
	 */
	public static boolean isDataPresent(Map<BaseAbstractAttributeInterface, Object> valueMap)
	{
		boolean isDataPresent = false;
		if (valueMap != null)
		{
			Set<Map.Entry<BaseAbstractAttributeInterface, Object>> mapEntrySet = valueMap
					.entrySet();
			for (Map.Entry<BaseAbstractAttributeInterface, Object> mapEntry : mapEntrySet)
			{
				Object value = mapEntry.getValue();
				if (value != null)
				{
					if ((value instanceof String) && (((String) value).length() > 0))
					{
						isDataPresent = true;
						break;
					}
					else if ((value instanceof FileAttributeRecordValue)
							&& (((FileAttributeRecordValue) value).getFileName().length() != 0))
					{
						isDataPresent = true;
						break;
					}
					else if ((value instanceof List) && (!((List) value).isEmpty()))
					{
						List valueList = (List) value;
						Object valueObject = valueList.get(0);

						if ((valueObject != null) && (valueObject instanceof Long))
						{
							isDataPresent = true;
							break;
						}
						else if ((valueObject != null) && (valueObject instanceof Map))
						{
							isDataPresent = isDataPresent((Map<BaseAbstractAttributeInterface, Object>) valueObject);
							break;
						}
					}
				}
			}
		}
		return isDataPresent;
	}

}


package edu.common.dynamicextensions.domain.userinterface;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import edu.common.dynamicextensions.domain.BaseAbstractAttribute;
import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FormControlNotesInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_CONTROL"
 */
public abstract class Control extends DynamicExtensionBaseDomainObject
		implements
			Serializable,
			ControlInterface
{

	/**
	 * @return
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_CONTROL_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * The caption of the control.
	 */
	protected String caption = null;

	/**
	 * The css class that is to be used for this control.
	 */
	protected String cssClass = null;

	/**
	 * whether this attribute should be displayed on screen.
	 */
	protected Boolean isHidden = null;

	/**
	 * Decides whether the control should be desabled or not
	 */
	protected Boolean isReadOnly = false;

	/**
	 * Name of the control.
	 */
	protected String name = null;

	/**
	 * Sequence number of the control.This governs in which order it will be shown on the UI.
	 */
	protected Integer sequenceNumber = null;

	/**
	 * Tool tip message for the control.
	 */
	protected String tooltip = null;

	/**
	 * Value to be shown in the control
	 */
	protected Object value = null;

	/**
	 * Attribute to which this control is associated.
	 */
	protected BaseAbstractAttribute baseAbstractAttribute;
	/**
	 *
	 */
	protected Boolean sequenceNumberChanged = false;
	/**
	 *
	 */
	protected Container parentContainer;

	/**
	 *
	 */
	protected boolean isSubControl = false;

	/**
	 * 
	 */
	protected String heading;

	/**
	 * 
	 */
	protected List<FormControlNotesInterface> formNotes = new LinkedList<FormControlNotesInterface>();

	/**
	 * Empty Constructor
	 */
	public Control()
	{
	}

	/**
	 * @hibernate.property name="caption" type="string" column="CAPTION" length="800"
	 * @return Returns the caption.
	 */
	public String getCaption()
	{
		return caption;
	}

	/**
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	/**
	 * @hibernate.property name="cssClass" type="string" column="CSS_CLASS"
	 * @return Returns the cssClass.
	 */
	public String getCssClass()
	{
		return cssClass;
	}

	/**
	 * @param cssClass The cssClass to set.
	 */
	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	/**
	 * @hibernate.property name="isHidden" type="boolean" column="HIDDEN"
	 * @return Returns the isHidden.
	 */
	public Boolean getIsHidden()
	{
		return isHidden;
	}

	/**
	 * @param isHidden The isHidden to set.
	 */
	public void setIsHidden(Boolean isHidden)
	{
		this.isHidden = isHidden;
	}

	/**
	 * @hibernate.property name="name" type="string" column="NAME"
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.property name="sequenceNumber" type="integer" column="SEQUENCE_NUMBER"
	 * @return Returns the sequenceNumber.
	 */
	public Integer getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @hibernate.property name="tooltip" type="string" column="TOOLTIP"
	 * @return Returns the tooltip.
	 */
	public String getTooltip()
	{
		return tooltip;
	}

	/**
	 * @param tooltip The tooltip to set.
	 */
	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}

	/**
	 * @return return the HTML string for this type of a object
	 * @throws DynamicExtensionsSystemException  exception
	 */
	public final String generateHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "";

		String innerHTML = "";
		if (getParentContainer().getMode() != null
				&& getParentContainer().getMode().equalsIgnoreCase(WebUIManagerConstants.VIEW_MODE))
		{
			innerHTML = generateViewModeHTML();
		}
		else
		{
			innerHTML = generateEditModeHTML();
		}

		if (this.isSubControl)
		{
			htmlString = innerHTML;
		}
		else
		{
			htmlString = getControlHTML(innerHTML);
		}

		this.isSubControl = false;
		return htmlString;
	}

	protected String getControlHTML(String htmlString)
	{
		boolean isControlRequired = UserInterfaceiUtility.isControlRequired(this);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<tr valign='top'>");
		stringBuffer.append("<tr>");

		// For category attribute controls, if heading and/or notes are specified, then
		// render the UI that displays heading followed by notes for particular
		// category attribute controls.
		if ((this.heading != null)
				|| (this.getFormNotes() != null && this.getFormNotes().size() != 0))
		{
			stringBuffer.append("<td width='100%' colspan='3' align='left'>");

			if (this.heading != null && this.heading.length() != 0)
			{
				stringBuffer.append("<div style='width:100%' class='td_color_6e81a6'>"
						+ this.getHeading() + "</div>");
			}

			if (this.getFormNotes() != null && this.getFormNotes().size() != 0)
			{
				stringBuffer.append("<div style='width:100%'>&nbsp</div>");

				for (FormControlNotesInterface fcNote : this.getFormNotes())
				{
					stringBuffer.append("<div style='width:100%' class='notes'>" + fcNote.getNote()
							+ "</div>");
				}
			}

			stringBuffer.append("</td>");
		}
		else
		{
			stringBuffer.append("<td>");
			stringBuffer.append("</td>");
		}
		stringBuffer.append("</tr>");

		stringBuffer
				.append("<td valign='top' class='formRequiredNotice_withoutBorder' width='2%'>");
		if (isControlRequired)
		{
			stringBuffer.append("<span class='font_red'>");
			stringBuffer.append(this.getParentContainer().getRequiredFieldIndicatior() + "&nbsp;");
			stringBuffer.append("</span>");
			stringBuffer.append("</td>");

			stringBuffer
					.append("<td class='formRequiredLabel_withoutBorder' width='20%' valign='top'>");
		}
		else
		{
			stringBuffer.append("&nbsp;");
			stringBuffer.append("</td>");

			stringBuffer
					.append("<td class='formRequiredLabel_withoutBorder' width='20%' valign='top'>");
		}
		if (this instanceof ComboBox)
		{
			stringBuffer.append("<br/>");
		}
		stringBuffer.append(DynamicExtensionsUtility.getFormattedStringForCapitalization(this
				.getCaption()));
		stringBuffer.append("</td>");

		stringBuffer.append("<td class='formField_withoutBorder' valign='top'>");
		stringBuffer.append("&nbsp;");
		stringBuffer.append(htmlString);
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");

		stringBuffer.append("<tr>");
		stringBuffer.append("<td>");
		stringBuffer.append("</td>");
		stringBuffer.append("</tr>");
		return stringBuffer.toString();
	}

	/**
	 * @return String html
	 * @throws DynamicExtensionsSystemException exception
	 */
	protected abstract String generateViewModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return String html
	 * @throws DynamicExtensionsSystemException exception
	 */
	protected abstract String generateEditModeHTML() throws DynamicExtensionsSystemException;

	/**
	 * @return
	 */
	public Object getValue()
	{
		return this.value;
	}

	/**
	 * @param value
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}

	//	/**
	//	 * @hibernate.many-to-one  cascade="save-update" column="ABSTRACT_ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.AbstractAttribute" constrained="true"
	//	 */
	//	public AbstractAttributeInterface getAbstractAttribute()
	//	{
	//		return this.abstractAttribute;
	//	}
	//
	//	/**
	//	 * @param abstractAttribute The abstractAttribute to set.
	//	 */
	//	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface)
	//	{
	//		this.abstractAttribute = abstractAttributeInterface;
	//	}

	/**
	 * @return String
	 * @throws DynamicExtensionsSystemException
	 */
	public String getHTMLComponentName() throws DynamicExtensionsSystemException
	{
		//		AbstractAttributeInterface abstractAttributeInterface = this.getAbstractAttribute();
		//		EntityInterface entity = abstractAttributeInterface.getEntity();
		//		Long entityIdentifier = entity.getId();
		//		EntityManagerInterface entityManager = EntityManager.getInstance();
		//		ContainerInterface container = null;
		//		if (entityIdentifier != null)
		//		{
		//			container = entityManager.getContainerByEntityIdentifier(entityIdentifier);
		//		}
		String htmlComponentName = null;
		ContainerInterface parentContainer = this.getParentContainer();
		if (this.getSequenceNumber() != null)
		{
			htmlComponentName = "Control_" + parentContainer.getIncontextContainer().getId() + "_"
					+ parentContainer.getId() + "_" + this.getSequenceNumber();
		}
		return htmlComponentName;
	}

	/**
	 *
	 */
	public Boolean getSequenceNumberChanged()
	{
		return sequenceNumberChanged;
	}

	/**
	 *
	 * @param sequenceNumberChanged
	 */
	public void setSequenceNumberChanged(Boolean sequenceNumberChanged)
	{
		this.sequenceNumberChanged = sequenceNumberChanged;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		Control control = (Control) object;
		Integer thisSequenceNumber = this.sequenceNumber;
		Integer otherSequenceNumber = control.getSequenceNumber();
		return thisSequenceNumber.compareTo(otherSequenceNumber);
	}

	/**
	 *@hibernate.many-to-one column="CONTAINER_ID" class="edu.common.dynamicextensions.domain.userinterface.Container" constrained="true"
	 * @return parentContainer
	 */
	public Container getParentContainer()
	{
		return parentContainer;
	}

	/**
	 * @param parentContainer parentContainer
	 */
	public void setParentContainer(Container parentContainer)
	{
		this.parentContainer = parentContainer;
	}

	/**
	 * This method returns boolean indicating whether the control is a part of Sub-Form or not.
	 * true value indicates the control is a part of Sub-Form, false value indicates its not.
	 * @return the isSubControl
	 */
	public boolean getIsSubControl()
	{
		return isSubControl;
	}

	/**
	 * This method sets whether the control is a part of Sub-Form or not.
	 * true value indicates the control is a part of Sub-Form, false value indicates its not.
	 * @param isSubControl the isSubControl to set
	 */
	public void setIsSubControl(boolean isSubControl)
	{
		this.isSubControl = isSubControl;
	}

	/**
	 * @hibernate.many-to-one  cascade="save-update" column="BASE_ABST_ATR_ID" class="edu.common.dynamicextensions.domain.BaseAbstractAttribute" constrained="true"
	 */

	public BaseAbstractAttributeInterface getBaseAbstractAttribute()
	{
		return baseAbstractAttribute;
	}

	/**
	 *
	 * @param baseAbstractAttribute
	 */

	public void setBaseAbstractAttribute(BaseAbstractAttributeInterface baseAbstractAttribute)
	{
		this.baseAbstractAttribute = (BaseAbstractAttribute) baseAbstractAttribute;
	}

	public AttributeMetadataInterface getAttibuteMetadataInterface()
	{
		AttributeMetadataInterface attributeMetadataInterface = null;
		if (baseAbstractAttribute instanceof AttributeMetadataInterface)
		{
			attributeMetadataInterface = (AttributeMetadataInterface) baseAbstractAttribute;
		}
		return attributeMetadataInterface;

	}

	/**
	 * @hibernate.property name="isReadOnly" type="boolean" column="READ_ONLY"
	 * @return
	 */
	public Boolean getIsReadOnly()
	{
		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setIsReadOnly(java.lang.Boolean)
	 */
	public void setIsReadOnly(Boolean isReadOnly)
	{
		this.isReadOnly = isReadOnly;
	}

	/**
	 * @hibernate.property name="heading" type="string" column="HEADING"
	 * @return Returns the caption.
	 */
	public String getHeading()
	{
		return heading;
	}

	/**
	 * @param heading the heading to set
	 */
	public void setHeading(String heading)
	{
		this.heading = heading;
	}

	/**
	 * This method Returns the formNotes.
	 * @hibernate.list name="formNotes" table="DYEXTN_FORM_CTRL_NOTES"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="FORM_CONTROL_ID"
	 * @hibernate.collection-index column="INSERTION_ORDER" type="long"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.FormControlNotes" 
	 * @return Returns the formNotes.
	 */
	public List<FormControlNotesInterface> getFormNotes()
	{
		return formNotes;
	}

	/**
	 * @param formNotes the formNotes to set
	 */
	public void setFormNotes(List<FormControlNotesInterface> formNotes)
	{
		this.formNotes = formNotes;
	}

}

package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domain.TaggedValue;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.LabelInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @hibernate.joined-subclass table="DYEXTN_LABEL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author kunal_kamble
 */
public class Label extends Control implements LabelInterface
{

	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateEditModeHTML()
	 */
	protected String generateEditModeHTML(ContainerInterface container) throws DynamicExtensionsSystemException
	{
		StringBuilder controlHTML = new StringBuilder(); 
		

		if (checkIsHeading())
		{
			controlHTML.append("<table width='100%'><tr><td width='100%' colspan='3' align='left'>");
			controlHTML.append("<div style='width:100%' class='td_color_6e81a6'>");
			controlHTML.append(caption);
			controlHTML.append("</div>");
			controlHTML.append("</td></tr>");
		}else if (checkIsNote())
		{

			controlHTML.append("<table width='100%'><tr><td width='100%' colspan='3' align='left'>");
			controlHTML.append("<div style='width:100%' class='notes'>");
			controlHTML.append(DynamicExtensionsUtility.replaceHTMLSpecialCharacters(caption));
			controlHTML.append("</div>");
			controlHTML.append("</td></tr>");
		}else
		{
			controlHTML.append("<div style='float:left'><b>" + caption + "</b></div>");
			
		}
		

		return controlHTML.toString();
/*		return "<table> <tr><td class='formRequiredNotice_withoutBorder' width='2%' valign='center' align='right' >&nbsp; </td> " +
				"<td colspan='100'><div style='float:left'><b>" + caption + "</b></div></td></tr>";
*/		
		
	}

	public boolean checkIsNote()
	{
		boolean isNotes = false;
		if(getTaggedValues() != null && !getTaggedValues().isEmpty())
		{
			for(TaggedValue taggedValue: getTaggedValues())
			{
				if(Constants.LABEL_TYPE.equals(taggedValue.getKey()) && Constants.NOTES.equals(taggedValue.getValue()))
				{
					isNotes = true;
					break;
				}
			}
		}
		return isNotes;
	}
	public boolean checkIsHeading()
	{
		boolean isHeading = false;
		if(getTaggedValues() != null && !getTaggedValues().isEmpty())
		{
			for(TaggedValue taggedValue: getTaggedValues())
			{
				if(Constants.LABEL_TYPE.equals(taggedValue.getKey()) && Constants.HEADING.equals(taggedValue.getValue()))
				{
					isHeading = true;
					break;
				}
			}
		}
		return isHeading;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateViewModeHTML()
	 */
	protected String generateViewModeHTML(ContainerInterface container) throws DynamicExtensionsSystemException
	{
		return generateEditModeHTML(container);
	}

	/**
	 *
	 */
	public List<String> getValueAsStrings() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 */
	public void setValueAsStrings(List<String> listOfValues)
	{
		// TODO Auto-generated method stub

	}
	/**
	 *
	 */
	public boolean getIsEnumeratedControl()
	{
		return false;
	}
	
	public Label()
	{
		super();
	}
	public Label(Label control)
	{
		super(control);
	}
}


package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.userinterface.LabelInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

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
	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		return "<div style='float:left'><b>" + caption + "</b></div>";
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.userinterface.Control#generateViewModeHTML()
	 */
	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		return "<b>" + caption + "</b>";
	}

}

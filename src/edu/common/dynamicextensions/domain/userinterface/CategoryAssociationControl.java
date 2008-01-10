
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This Class represents the category.
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ASSOCIATION_CONTROL"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class CategoryAssociationControl extends AbstractContainmentControl implements CategoryAssociationControlInterface
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public CategoryAssociationControl()
	{
		super();
	}

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractAttributeInterface getAbstractAttribute()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAbstractAttribute(AbstractAttributeInterface abstractAttributeInterface)
	{
		// TODO Auto-generated method stub

	}

	public String generateLinkHTML() throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub
		return null;
	}

}

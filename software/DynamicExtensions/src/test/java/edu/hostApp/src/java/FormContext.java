
package edu.hostApp.src.java;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

public class FormContext extends AbstractFormContext
{

	/**
	* The Constant serialVersionUID.
	*/
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Equals.
	 *
	 * @param formContext
	 *            the record entry
	 * @return true, if successful
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object formContext)
	{
		boolean flag = false;
		if (formContext instanceof FormContext)
		{
			FormContext context = (FormContext) formContext;
			if (this == context || (getId() != null && (this).getId().equals(context.getId())))
			{
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public int hashCode()
	{
		return 1;
	}

	/**
	 * Sets the all values.
	 *
	 * @param arg0
	 *            the new all values
	 * @throws AssignDataException
	 *             the assign data exception
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.IValueObject)
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub
	}
}

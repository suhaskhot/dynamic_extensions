
package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This is base domain object for all the dynamic extension domain object
 * @author Rahul Ner
 */
public abstract class DynamicExtensionBaseDomainObject extends AbstractDomainObject
		implements
			Serializable
{

	/**
	 * Serial Version Unique Identifief
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 * Internally generated identifier.
	 */
	protected Long id;

	/** 
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException
	{

	}

	/** 
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
}


package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CategoryAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;

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

	/**
	 * This method returns true if the cardinality of the Containment Association is One to Many.
	 * @return true if Caridnality is One to Many, false otherwise.
	 */
	public boolean isCardinalityOneToMany()
	{
		boolean isOneToMany = false;
		CategoryAssociationInterface associationInterface = (CategoryAssociationInterface) this.getBaseAbstractAttribute();
		/*//Quick fix: since association is not getting saved properly
		if(associationInterface == null){
			return false;
		}
		//quickfix ends
*/		if (associationInterface.getCategoryEntity().getNumberOfEntries() == -1)
		{
			isOneToMany = true;
		}
		return isOneToMany;
	}

}

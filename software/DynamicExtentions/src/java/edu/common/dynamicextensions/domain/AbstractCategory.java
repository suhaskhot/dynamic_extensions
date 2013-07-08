/**
 *
 */

package edu.common.dynamicextensions.domain;

/**
* @author suhas_khot
* @hibernate.joined-subclass table="DYEXTN_ABSTR_CATEGORY"
* @hibernate.joined-subclass-key column="IDENTIFIER"
*/
public class AbstractCategory extends AbstractMetadata
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty Constructor
	 */
	AbstractCategory()
	{
		super();
	}
	
	AbstractCategory(AbstractCategory abstractCategory)
	{
		super(abstractCategory);
	}
}

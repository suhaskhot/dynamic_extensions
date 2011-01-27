/**
 *
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StaticCategoryInterface;

/**
* @author suhas_khot
* @hibernate.joined-subclass table="DYEXTN_STATIC_CATEGORY"
* @hibernate.joined-subclass-key column="IDENTIFIER"
*/
public class StaticCategory extends AbstractCategory implements StaticCategoryInterface
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private String formURL;

	/**
	 * This method returns the form URL.
	 * @hibernate.property name="formURL" type="string" column="FORM_URL" length="800"
	 * @return the formURL
	 */
	public String getFormURL()
	{
		return formURL;
	}

	/**
	 * @param formURL the formURL to set
	 */
	public void setFormURL(String formURL)
	{
		this.formURL = formURL;
	}
}

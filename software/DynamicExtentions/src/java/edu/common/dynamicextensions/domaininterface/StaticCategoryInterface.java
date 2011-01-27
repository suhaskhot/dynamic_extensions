/**
 *
 */
package edu.common.dynamicextensions.domaininterface;


/**
 * @author suhas_khot
 *
 */
public interface StaticCategoryInterface extends AbstractMetadataInterface
{
	/**
	 * @return form URL
	 */
	String getFormURL();

	/**
	 * @param formURL Form URL
	 */
	void setFormURL(String formURL);

}

/**
 *
 */
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.GridViewColumn;


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
	
	/**
	 * Gets the grid view column list.
	 * 
	 * @return the grid view column list
	 */
	public Collection<GridViewColumn> getGridViewColumnList();
	
	/**
	 * Sets the grid view column list.
	 * 
	 * @param gridViewColumnList the new grid view column list
	 */
	public void setGridViewColumnList(Collection<GridViewColumn> gridViewColumnList);
	/**
	 * Gets the data query.
	 * 
	 * @return the data query
	 */
	public String getDataQuery();
	
	/**
	 * Sets the data query.
	 * 
	 * @param dataQuery the new data query
	 */
	public void setDataQuery(String dataQuery);

}
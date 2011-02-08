package edu.common.dynamicextensions.domain;



/**
 * The Class GridViewColumn.
 */
public class GridViewColumn
{
	
	
	/** The id. */
	private Long id;
	
	/** The grid display column. */
	private String gridDisplayColumn;
	
	/** The grid table column list. */
	private String gridTableColumn;
	
	private Long displayOrder; 
	/**
	 * Gets the grid display column .
	 * 
	 * @return the grid display column 
	 */
	public String getGridDisplayColumn()
	{
		return gridDisplayColumn;
	}

	
	/**
	 * Sets the grid display column list.
	 * 
	 * @param gridDisplayColumnList the new grid display column list
	 */
	public void setGridDisplayColumn(String gridDisplayColumn)
	{
		this.gridDisplayColumn = gridDisplayColumn;
	}

	
	public Long getDisplayOrder()
	{
		return displayOrder;
	}


	
	public void setDisplayOrder(Long displayOrder)
	{
		this.displayOrder = displayOrder;
	}


	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}


	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}


	
	/**
	 * Gets the grid table column.
	 * 
	 * @return the grid table column
	 */
	public String getGridTableColumn()
	{
		return gridTableColumn;
	}


	
	/**
	 * Sets the grid table column.
	 * 
	 * @param gridTableColumn the new grid table column
	 */
	public void setGridTableColumn(String gridTableColumn)
	{
		this.gridTableColumn = gridTableColumn;
	}
}

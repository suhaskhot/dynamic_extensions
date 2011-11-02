
package edu.common.dynamicextensions.permissiblevalue.version;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class SortDateCollection implements Comparator<Date>, Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1955253458664639319L;

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Date date1, Date date2)
	{
		int returnValue = 0;
		if (date1.after(date2))
		{
			returnValue = 1;
		}
		if (date1.before(date2))
		{
			returnValue = -1;
		}
		return returnValue;
	}
}

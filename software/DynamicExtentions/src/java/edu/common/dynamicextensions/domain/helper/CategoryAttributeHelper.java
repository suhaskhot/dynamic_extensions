/**
 *
 */
package edu.common.dynamicextensions.domain.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;


/**
 * This class will provide methods which are related to category attribute but are not actually
 * part of category attribute object.
 * @author Gaurav_mehta
 */
public class CategoryAttributeHelper
{

	/** The Constant LOGGER. */
	//private static final Logger LOGGER = Logger.getCommonLogger(CategoryAttributeHelper.class);


	/**
	 * This method returns the UserDefinedDE object of given category attribute with given activation date.
	 * If there is no UserDefinedDE object with given activation date then it will return null.
	 * @param categoryAttribute the category attribute
	 * @param activationDate the activation date
	 * @return the data element by activation date
	 */
	public UserDefinedDEInterface getDataElementByActivationDate(CategoryAttributeInterface categoryAttribute,
																						Date activationDate)
	{
		Set<DataElementInterface> allDataElementColl = categoryAttribute.getDataElementCollection();
		for (DataElementInterface dataElementInterface : allDataElementColl)
		{
			final UserDefinedDEInterface userDefined = (UserDefinedDEInterface) dataElementInterface;
			final Date existingActivationDate = userDefined.getActivationDate();
			if(existingActivationDate!= null && existingActivationDate.compareTo(activationDate) == 0)
			{
				return userDefined;
			}
		}
		return null;
	}

	/**
	 * Gets the all activation date for category attribute.
	 * @param categoryAttribute the category attribute
	 * @return the all activation date for category attribute
	 */
	public List<Date> getAllActivationDateForCategoryAttribute(CategoryAttributeInterface categoryAttribute)
	{
		List<Date> allActivationDate = new ArrayList<Date>();
		Set<DataElementInterface> allDataElementColl = categoryAttribute.getDataElementCollection();
		for (DataElementInterface dataElementInterface : allDataElementColl)
		{
			final UserDefinedDEInterface userDefined = (UserDefinedDEInterface) dataElementInterface;
			if(userDefined.getActivationDate()!=null)
			{
				allActivationDate.add(userDefined.getActivationDate());
			}
		}
		return allActivationDate;
	}
}

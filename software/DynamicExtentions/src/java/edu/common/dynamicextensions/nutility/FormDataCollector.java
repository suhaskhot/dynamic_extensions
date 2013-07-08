
package edu.common.dynamicextensions.nutility;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.FormData;

/**
 * @author Kunal Kamble
 *
 */
public interface FormDataCollector
{

	/**
	 * This method populates the FormData object for the give containerId from the specified source.
	 * @param containerId
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException
	 */
	FormData collectFormData() throws DynamicExtensionsApplicationException;
}

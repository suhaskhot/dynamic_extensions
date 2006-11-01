
package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;




/**
 * @author Rahul Ner
 *
 */
public class UniqueValidator implements ValidatorRuleInterface {

	public boolean validate(AttributeInterface attribute, Object valueObject, Map paramMap) throws DynamicExtensionsApplicationException
	{
		
		return true;
	}
    

}

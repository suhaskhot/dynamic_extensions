package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;



/**
 * @author Rahul Ner
 *
 */
public class RequiredValidator implements ValidatorRuleInterface {

	/* 
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map paramMap) throws DynamicExtensionsApplicationException
	{
        String attributeName = attribute.getName();
        if(valueObject == null || ((String) valueObject).equals("")){
            throw new DynamicExtensionsApplicationException("dynExtn.validation.RequiredValidator",null,attributeName);
        } 
        return true;
	}

}

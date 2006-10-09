/**
 *<p>Title: DynamicExtensionsApplicationException</p>
 *<p>Description: Application level exception for dynamic extension  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.exception;


/**
 *<p>Title: DynamicExtensionsApplicationException</p>
 *<p>Description:This exception class represents all the exceptions which are application specific 
 *for example 
 *<BR> Duplicate name for the entity is found. 
 *<BR> Backend Validation is failed <BR>
 * When these exceptions are caught , they are supposed to be processed so that user is shown
 * approprite error message which is extracted from the exception. In such exception cases user 
 * should not be shown error page.
 *</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
public class DynamicExtensionsApplicationException
        extends
            BaseDynamicExtensionsException
{

    
}

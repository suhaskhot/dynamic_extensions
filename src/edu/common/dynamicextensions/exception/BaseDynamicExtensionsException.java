/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.exception;


/**
 * This class acts as a base exception from which all the exceptions will extend.
 * The class holds the information about the cause of the exception along with the unique error code.
 * @author Vishvesh Mulay
 *
 */
public abstract class BaseDynamicExtensionsException extends Exception implements DynamicExtensionExceptionInterface
{
    /**Instance of the wrapped exception that is thrown from the previous layer*/
    protected Throwable wrappedException;
    /**A unique error code from which the cause of the exception can be displayed to the user.*/
    protected String errorCode;
    
    /**Detailed error message explaining the cause of the exception*/
    protected String errorMessage;
    
    
    /**
     * Getter method for errorMessage
     * @return errorMessage errorMessage that is set.
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Setter method for errorMessage.
     * @param errorMessage errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    /**
     * Getter method for errorCode
     * @return errorCode errorCode that is set
     */
    public String getErrorCode()
    {
        return errorCode;
    }


    /**
     * Setter method for errorCode
     * @param errorCode errorCode to set.
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * Getter method for wrappedException
     * @return wrappedException wrappedException that is set
     */
    public Throwable getWrappedException()
    {
        return wrappedException;
    }

    /**
     * Setter method for wrappedException 
     * @param wrappedException wrappedException instance to set.
     */
    public void setWrappedException(Throwable wrappedException)
    {
        this.wrappedException = wrappedException;
    }

}

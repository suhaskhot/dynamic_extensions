package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type byte array.This type of primitive attribute  can be used for
 * storing large amount of data such as image file or word file. 
 * @author geetika_bangard
 */
public interface ByteArrayAttributeInterface extends AttributeInterface 
{
    /**
     * The content type for the file to be stored. 
     * @return Returns the contentType.
     */
     String getContentType();
    /**
     * @param contentType The contentType to set.
     */
    void setContentType(String contentType);


}

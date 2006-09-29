package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_BYTE_ARRAY_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ByteArrayAttribute extends PrimitiveAttribute {
    /**
     * Content type for this file.
     */
	protected String contentType;

	/**
	 * Empty Constructor.
	 *
	 */
	public ByteArrayAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	

    /**
     * @hibernate.property name="contentType" type="string" column="CONTENT_TYPE" 
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
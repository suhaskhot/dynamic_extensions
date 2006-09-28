package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class ByteArrayAttribute extends PrimitiveAttribute {

	protected String contentType;

	public ByteArrayAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	

    /**
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
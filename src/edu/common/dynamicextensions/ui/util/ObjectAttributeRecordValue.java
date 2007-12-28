package edu.common.dynamicextensions.ui.util; 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domaininterface.ObjectAttributeRecordValueInterface;

/**
 * This Class represents the a single value for multi select attribute.
 * 
 * @author Rahul Ner 
 * @author Sujay Narkar
 * 
 */
public class ObjectAttributeRecordValue extends DynamicExtensionBaseDomainObject implements Serializable,
        ObjectAttributeRecordValueInterface {

    private Object object;

    /**
     * this is the name of the file 
     */
    private String className;

    /**
     * This method returns the unique identifier of the AbstractMetadata.
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @param objectContent The objectContent to set.
     * @throws ClassNotFoundException 
     * @throws IOException 
     */
    private void setObjectContent(byte[] objectContent) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(objectContent);
        ObjectInputStream ojectInputStream = new ObjectInputStream(byteStream);
        object = ojectInputStream.readObject();
    }

    /**
     * @return Returns the objectContent.
     * @throws IOException 
     */
    private byte[] getObjectContent() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream ojectOutputStream = new ObjectOutputStream(byteStream);
        ojectOutputStream.writeObject(object);       
        return byteStream.toByteArray();
    }

    /**
     * This method copies the values from one file record to another
     * @param fileRecordValue
     */
    public void copyValues(ObjectAttributeRecordValue objectRecordValue) {
        this.className = objectRecordValue.getClassName();
        this.object = objectRecordValue.getObject();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.className;
    }

    public Object getObject() {
        return object;
    }

    /**
     * @see edu.common.dynamicextensions.domaininterface.ObjectAttributeRecordValueInterface#setObject(java.lang.Object)
     */
    public void setObject(Object value) throws IOException {
        this.object = value;
    }
}

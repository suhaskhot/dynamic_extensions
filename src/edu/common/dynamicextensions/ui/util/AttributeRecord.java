package edu.common.dynamicextensions.ui.util; 

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This Class represents the a single record for multi select attribute.
 * 
 * @author Rahul Ner 
 */
public class AttributeRecord extends DynamicExtensionBaseDomainObject {

    /**
     * Empty Constructor.
     */
    public AttributeRecord() {
    }

    /**
     * Serial Version Unique Identifier
     */
    protected static final long serialVersionUID = 1234567890L;

    /**
     * Entity to which this collectionRecord belongs
     */
    protected EntityInterface entity;

    /**
     * Attribute to which this collectionRecord belongs
     */
    protected AttributeInterface attribute;

    /**
     * a record Id to which this collectionRecord belongs
     */
    protected Long recordId;

    /**
     * value of this collectionRecord.
     */
    protected Collection<CollectionAttributeRecordValue> valueCollection = new HashSet<CollectionAttributeRecordValue>();

    /**
     * 
     */
    protected Collection<FileAttributeRecordValue> fileRecordCollection = new HashSet<FileAttributeRecordValue>();

    /**
     * 
     */
    protected Collection<ObjectAttributeRecordValue> objectRecordCollection = new HashSet<ObjectAttributeRecordValue>();

    /**
     * This method returns the unique identifier of the AbstractMetadata.
     * @return the identifier of the AbstractMetadata.
     */
    public Long getId() {
        return id;
    }

    /**
     * This method returns the Entity associated with this collectionRecord.
     * @return EntityInterface the Entity associated with the collectionRecord.
     */
    public EntityInterface getEntity() {
        return entity;
    }

    /**
     * This method sets the Entity associated with this collectionRecord.
     * @param entityInterface The entity to be set.
     */
    public void setEntity(EntityInterface entityInterface) {
        if (entityInterface != null) {
            this.entity = (Entity) entityInterface;
        }
    }

    /**
     * This method returns the Attribute associated with this collectionRecord.
     * @return AttributeInterface the Attribut associated with the collectionRecord.
     */
    public AttributeInterface getAttribute() {
        return attribute;
    }

    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

    /**
     * This method returns the record associated with this collectionRecord.
     * @return AttributeInterface the Attribut associated with the collectionRecord.
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The recordId to set.
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * This method returns the Collection of AbstractAttribute.
     * @return the Collection of AbstractAttribute.
     */
    public Collection<CollectionAttributeRecordValue> getValueCollection() {
        return valueCollection;
    }

    /**
     * @param valueCollection The valueCollection to set.
     */
    public void setValueCollection(Collection<CollectionAttributeRecordValue> valueCollection) {
        this.valueCollection = valueCollection;
    }

    /**
     * This method return Returns the fileRecord.
     * @return Returns the fileRecord.
     */
    private Collection<FileAttributeRecordValue> getFileRecordCollection() {
        return fileRecordCollection;
    }

    /**
     * @param fileRecord The fileRecord to set.
     */
    private void setFileRecordCollection(Collection<FileAttributeRecordValue> fileRecordCollection) {
        this.fileRecordCollection = fileRecordCollection;
    }

    /**
     * @return Returns the fileRecord.
     */
    public FileAttributeRecordValue getFileRecord() {
        if (fileRecordCollection != null && !fileRecordCollection.isEmpty()) {
            return fileRecordCollection.iterator().next();
        }
        return null;
    }

    /**
     * @param fileRecord The fileRecord to set.
     */
    public void setFileRecord(FileAttributeRecordValue fileRecord) {
        if (fileRecordCollection == null) {
            fileRecordCollection = new HashSet<FileAttributeRecordValue>();
        }
        this.fileRecordCollection.clear();
        this.fileRecordCollection.add(fileRecord);
    }

    /**
     * This method return Returns the objectRecord.
     * @return Returns the objectRecordCollection.
     */
    private Collection<ObjectAttributeRecordValue> getObjectRecordCollection() {
        return objectRecordCollection;
    }

    /**
     * @param objectRecordCollection The objectRecordCollection to set.
     */
    private void setObjectRecordCollection(Collection<ObjectAttributeRecordValue> objectRecordCollection) {
        this.objectRecordCollection = objectRecordCollection;
    }

    public void setObjectRecord(ObjectAttributeRecordValue objectRecord) {
        if (objectRecordCollection == null) {
            objectRecordCollection = new HashSet<ObjectAttributeRecordValue>();
        }
        this.objectRecordCollection.clear();
        this.objectRecordCollection.add(objectRecord);

    }

    /**
     * @return Returns the fileRecord.
     */
    public ObjectAttributeRecordValue getObjectRecord() {
        if (objectRecordCollection != null && !objectRecordCollection.isEmpty()) {
            return objectRecordCollection.iterator().next();
        }
        return null;
    }

}

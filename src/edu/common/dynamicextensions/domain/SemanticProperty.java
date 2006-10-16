package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_SEMANTIC_PROPERTY"
 */
public class SemanticProperty extends AbstractDomainObject implements SemanticPropertyInterface,Serializable {

    /**
     * Unique identifier for the object
     */
	protected Long id;
    /**
     * The concept code.
     */
	protected String conceptCode;
	/**
	 * Term
	 */
	protected String term;
	/**
	 * Thesauras Name
	 */
	protected String thesaurasName;
	/**
	 * Empty Constructor.
	 */
	public SemanticProperty(){

	}

	 /**
     * @hibernate.id name="id" column="IDENTIFIER" type="long"
     * length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="DYEXTN_SEMANTIC_PROPERTY_SEQ"
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.property name="conceptCode" type="string" column="CONCEPT_CODE" 
     * @return Returns the conceptCode.
     */
    public String getConceptCode() {
        return conceptCode;
    }
    /**
     * @param conceptCode The conceptCode to set.
     */
    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }
    /**
     * @hibernate.property name="term" type="string" column="TERM" 
     * @return Returns the term.
     */
    public String getTerm() {
        return term;
    }
    /**
     * @param term The term to set.
     */
    public void setTerm(String term) {
        this.term = term;
    }
    /**
     * @hibernate.property name="thesaurasName" type="string" column="THESAURAS_NAME" 
     * @return Returns the thesaurasName.
     */
    public String getThesaurasName() {
        return thesaurasName;
    }
    /**
     * @param thesaurasName The thesaurasName to set.
     */
    public void setThesaurasName(String thesaurasName) {
        this.thesaurasName = thesaurasName;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#getSystemIdentifier()
     */
    public Long getSystemIdentifier() {
        return id;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
     */
    public void setSystemIdentifier(Long systemIdentifier) {
        systemIdentifier = id;        
    }
}
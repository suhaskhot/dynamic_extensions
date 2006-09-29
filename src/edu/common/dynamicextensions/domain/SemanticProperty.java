package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class SemanticProperty extends AbstractDomainObject implements Serializable{

	protected String conceptCode;
	protected String term;
	protected String thesaurasName;

	public SemanticProperty(){

	}

	public void finalize() throws Throwable {

	}
	

    /**
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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setSystemIdentifier(java.lang.Long)
     */
    public void setSystemIdentifier(Long arg0) {
        // TODO Auto-generated method stub
        
    }
}
package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class SemanticProperty {

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
}
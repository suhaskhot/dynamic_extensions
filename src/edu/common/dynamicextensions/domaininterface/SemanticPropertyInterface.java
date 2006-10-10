
package edu.common.dynamicextensions.domaininterface;


/**
 * @author sujay_narkar
 *
 */
public interface SemanticPropertyInterface {
    
    /**
     * @return Returns the id.
     */
    public Long getId();
    
    /**
     * @return Returns the conceptCode.
     */
    public String getConceptCode();
    
    
    /**
     * @param conceptCode The conceptCode to set.
     */
    public void setConceptCode(String conceptCode);
    
    
    /**
     * @return Returns the term.
     */
    public String getTerm();
    
    
    /**
     * @param term The term to set.
     */
    public void setTerm(String term);
        
    
    /**
     * @return Returns the thesaurasName.
     */
    public String getThesaurasName();
    
    
    /**
     * @param thesaurasName The thesaurasName to set.
     */
    public void setThesaurasName(String thesaurasName);
    
}


package edu.common.dynamicextensions.domaininterface;


/**
 * For every abstract metadata object semantic properties are associated.
 * @author sujay_narkar
 *
 */
public interface SemanticPropertyInterface 
{
    
    /**
     * @return Returns the id.
     */
     Long getId();
    
    /**
     * Concept code for metadata object
     * @return Returns the conceptCode.
     */
     String getConceptCode();
    
    
    /**
     * @param conceptCode The conceptCode to set.
     */
     void setConceptCode(String conceptCode);
    
    
    /**
     * Concept name for the metadata object
     * @return Returns the term.
     */
     String getTerm();
    
    
    /**
     * @param term The term to set.
     */
     void setTerm(String term);
        
    
    /**
     * ThesaurasName for the metadata object
     * @return Returns the thesaurasName.
     */
     String getThesaurasName();
    
    
    /**
     * @param thesaurasName The thesaurasName to set.
     */
     void setThesaurasName(String thesaurasName);
    
}

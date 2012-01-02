package edu.common.metadata;

/**
 *
 * @author Vinayak Pawar (vinayak.pawar@krishagni.com)
 */
public interface ClassMetadataMap {
    public String[] getClassNames();
    
    public String[] getTableNames();
    
    public ClassMetadata getClassMetadata(String className);
    
    public int getClassCount();
    
    public int getTableCount();
    
    public void clear();
}

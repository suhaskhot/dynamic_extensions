/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.ui.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

public class SemanticPropertyBuilderUtil
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String concept = "    Vishvesh Mulay, Priti munot, chetan patil , sujay Narkar ,    ";
        getSymanticPropertyCollection(concept);

    }

    /**This method builds the semantic property collection based on the string passed to it.
     * The method splits the string using "," and then builds one instance of semantic property per string segment.
     * @param conceptCodes Comma separated string of concept codes.
     * @return Collection collection of semantic property objects.
     */
    public static Collection getSymanticPropertyCollection(String conceptCodes)
    {
        if (conceptCodes == null || conceptCodes.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Collection collection = new HashSet();
            String individualConceptCodes[] = conceptCodes.split("[,]");
            DomainObjectFactory factory = DomainObjectFactory.getInstance();
            for (int i = 0; i < individualConceptCodes.length; i++)
            {
                String conceptCode = individualConceptCodes[i];
                if (conceptCode != null && conceptCode.trim().length() != 0)
                {
                    SemanticPropertyInterface semanticPropertyInterface = factory
                            .createSemanticProperty();
                    semanticPropertyInterface
                            .setConceptCode(conceptCode.trim());
                    //TODO Check how to fetch the thesauras name and term for the semantic property.
                    semanticPropertyInterface.setTerm(Constants.DEFAULT_TERM);
                    semanticPropertyInterface
                            .setThesaurasName(Constants.DEFAULT_THESAURAS_NAME);
                    collection.add(semanticPropertyInterface);
                }
            }
            return collection;
        }

    }
    
    public static String getConceptCodeString(AbstractMetadataInterface abstractMetadataInterface)
    {
        if (abstractMetadataInterface == null || abstractMetadataInterface.getSemanticPropertyCollection() == null || abstractMetadataInterface.getSemanticPropertyCollection().isEmpty())
        {
            return "";
        } else 
        {
            StringBuffer conceptCode = new StringBuffer();
            Iterator iterator = abstractMetadataInterface.getSemanticPropertyCollection().iterator();
            while (iterator.hasNext()) {
                SemanticPropertyInterface semanticPropertyInterface = (SemanticPropertyInterface) iterator.next();
                conceptCode.append(semanticPropertyInterface.getConceptCode());
                if (iterator.hasNext()) {
                    conceptCode.append(",");
                }
            }
            return conceptCode.toString();
        }
       
    }
}

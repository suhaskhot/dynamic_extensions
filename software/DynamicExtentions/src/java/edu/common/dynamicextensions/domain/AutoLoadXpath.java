
package edu.common.dynamicextensions.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class represent the xpath & its associated concept codes which can go in
 * its corrosponding category.
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.class table="DYEXTN_AUTO_LOADXPATH"
 * @hibernate.cache  usage="read-write"
 */
public class AutoLoadXpath implements Serializable
{

	/**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
	 * Class identifier
	 */
	private Long identifier;

	/**
	 * it will return the identifier of the object
	 * @return identifier
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="increment"
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier
	 * @param identifier identifier to set.
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Xpath to which this object corresponds.
	 */
	private String xpath;

	/**
	 * Concept code collection associated with this xpath.
	 */
	private Collection<String> conceptCodeCollection = new HashSet<String>();

	/**
	 * It will return the xpath.
	 * @hibernate.property name="xpath" type="string" column="XPATH" length="4000"
	 * @return Returns the caption.
	 */
	public String getXpath()
	{
		return xpath;
	}

	/**
	 * Sets the Xpath.
	 * @param xpath xapth to use.
	 */
	public void setXpath(String xpath)
	{
		this.xpath = xpath;
	}

	/**
	 * @hibernate.map name="conceptCodeCollection" table="DYEXTN_CAT_XPATH_CONCEPTCODE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="AUTO_LOAD_XPATH_ID"
	 * @hibernate.collection-element column="CONCEPT_CODE" type="String"
	 * @hibernate.cache  usage="read-write"
	 * @return Returns the controlCollection.
	 */
	public Collection<String> getConceptCodeCollection()
	{
		return conceptCodeCollection;
	}

	/**
	 * Sets the concept code collection.
	 * @param conceptCodeCollection concept code coll.
	 */
	public void setConceptCodeCollection(Collection<String> conceptCodeCollection)
	{
		this.conceptCodeCollection = conceptCodeCollection;
	}

	/**
	 * It will add the given concept code to the concept code collection.
	 * @param conceptCode
	 */
	public void addConceptCode(String conceptCode)
	{
		if (conceptCodeCollection == null)
		{
			conceptCodeCollection = new HashSet<String>();
		}
		conceptCodeCollection.add(conceptCode);

	}

	@Override
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof AutoLoadXpath)
		{
			AutoLoadXpath xpathObject = (AutoLoadXpath) obj;
			if ((xpathObject.getIdentifier() != null && identifier != null && xpathObject
					.getIdentifier().equals(identifier))
					|| xpathObject.getXpath().equals(xpath))
			{
				isEqual = true;
			}
		}
		return isEqual;
	}

	@Override
	public int hashCode()
	{
		int length = 8;
		if (xpath != null)
		{
			length = xpath.length() % 10;
		}
		return length;
	}
}

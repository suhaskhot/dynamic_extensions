
package edu.common.dynamicextensions.domain;

import java.io.Serializable;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * For every abstract metadata object semantic properties are associated.
 * This Class represents the Semantic Properties of a Metadata.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_SEMANTIC_PROPERTY"
 */
public class SemanticProperty extends DynamicExtensionBaseDomainObject implements SemanticPropertyInterface, Serializable
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -1955066885211283279L;

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
	public SemanticProperty()
	{
	}

	/**
	 * This method returns the Unique identifier.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_SEMANTIC_PROPERTY_SEQ"
	 * @return the Unique identifier.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method returns the concept code.
	 * @hibernate.property name="conceptCode" type="string" column="CONCEPT_CODE" 
	 * @return the concept code.
	 */
	public String getConceptCode()
	{
		return conceptCode;
	}

	/**
	 * This method sets the concept code.
	 * @param conceptCode the concept code to be set.
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}

	/**
	 * This method returns the tem i.e. concept name. 
	 * @hibernate.property name="term" type="string" column="TERM" 
	 * @return Returns the term i.e. concept name
	 */
	public String getTerm()
	{
		return term;
	}

	/**
	 * This method sets the term.
	 * @param term the term to be set.
	 */
	public void setTerm(String term)
	{
		this.term = term;
	}

	/**
	 * This method returns the thesaurus name.
	 * @hibernate.property name="thesaurasName" type="string" column="THESAURAS_NAME" 
	 * @return the thesaurus name.
	 */
	public String getThesaurasName()
	{
		return thesaurasName;
	}

	/**
	 * This method sets the thesauras name.
	 * @param thesaurasName the thesauras name to be set.
	 */
	public void setThesaurasName(String thesaurasName)
	{
		this.thesaurasName = thesaurasName;
	}



}
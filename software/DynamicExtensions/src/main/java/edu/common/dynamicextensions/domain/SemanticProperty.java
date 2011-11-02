
package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * For every abstract metadata object semantic properties are associated.
 * This Class represents the Semantic Properties of a Metadata.
 *
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_SEMANTIC_PROPERTY"
 * @hibernate.cache  usage="read-write"
 */
public class SemanticProperty extends DynamicExtensionBaseDomainObject
		implements
			SemanticPropertyInterface
{

	/** Serial Version Unique Identifier. */
	private static final long serialVersionUID = -1955066885211283279L;

	/** The concept code. */
	private String conceptCode;

	/** The concept preferred name. */
	private String conceptPreferredName;

	/** The concept definition source. */
	private String conceptDefinitionSource;

	/** The concept definition. */
	private String conceptDefinition;

	/** The list of qualifier. */
	private Collection<SemanticPropertyInterface> listOfQualifier;

	/** The sequence number. */
	private long sequenceNumber = 0;


	/**
	 * This is the default constructor.
	 * Instantiates a new semantic property.
	 */
	public SemanticProperty()
	{
		// This is for default implementation.
		super();
	}

	/**
	 * This constructor is a copy constructor which copies given semantic property object values to the
	 * new semantic Property object.
	 * Instantiates a new semantic property.
	 * @param semanticProperty the semantic property
	 */
	public SemanticProperty(SemanticPropertyInterface semanticProperty)
	{
		super();
		conceptCode = semanticProperty.getConceptCode();
		conceptPreferredName = semanticProperty.getConceptPreferredName();
		conceptDefinitionSource = semanticProperty.getConceptDefinitionSource();
		conceptDefinition = semanticProperty.getConceptDefinition();
		listOfQualifier = semanticProperty.getListOfQualifier();
		sequenceNumber = semanticProperty.getSequenceNumber();
	}

	/**
	 * This method returns the Unique identifier.
	 * @return the Unique identifier.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_SEMANTIC_PROPERTY_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method returns the concept code.
	 * @return the concept code.
	 * @hibernate.property name="conceptCode" type="string" column="CONCEPT_CODE"
	 */
	public String getConceptCode()
	{
		return conceptCode;
	}

	/**
	 * This method sets the concept code.
	 *
	 * @param conceptCode the concept code to be set.
	 */
	public void setConceptCode(String conceptCode)
	{
		this.conceptCode = conceptCode;
	}

	/**
	 * This method returns the concept preferred Name i.e. concept name.
	 * @return Returns the concept preferred Name i.e. concept name
	 * @hibernate.property name="conceptPreferredName" type="string" column="TERM"
	 */
	public String getConceptPreferredName()
	{
		return conceptPreferredName;
	}

	/**
	 * This method sets the conceptPreferredName.
	 * @param conceptPreferredName the term to be set.
	 */
	public void setConceptPreferredName(String conceptPreferredName)
	{
		this.conceptPreferredName = conceptPreferredName;
	}

	/**
	 * This method returns the conceptDefinitionSource name.
	 * @return the conceptDefinitionSource name.
	 * @hibernate.property name="conceptDefinitionSource" type="string" column="THESAURAS_NAME"
	 */
	public String getConceptDefinitionSource()
	{
		return conceptDefinitionSource;
	}

	/**
	 * This method sets the conceptDefinitionSource name.
	 * @param conceptDefinitionSource the conceptDefinitionSource name to be set.
	 */
	public void setConceptDefinitionSource(String conceptDefinitionSource)
	{
		this.conceptDefinitionSource = conceptDefinitionSource;
	}

	/**
	 * This method returns the tem i.e. concept name.
	 * @return the conceptDefinition
	 * @hibernate.property name="conceptDefinition" type="string" column="CONCEPT_DEFINITION" length="4000"
	 */
	public String getConceptDefinition()
	{
		return conceptDefinition;
	}

	/**
	 * Sets the concept definition.
	 * @param conceptDefinition the conceptDefinition to set
	 */
	public void setConceptDefinition(String conceptDefinition)
	{
		this.conceptDefinition = conceptDefinition;
	}

	/**
	 * This method gets the list of Qualifiers for the given concept definition.
	 * @return the listOfQualifier
	 */
	public Collection<SemanticPropertyInterface> getListOfQualifier()
	{
		return listOfQualifier;
	}

	/**
	 * This method sets the qualifiers for given concept definition.
	 * @param listOfQualifier the listOfQualifier to set
	 */
	public void setListOfQualifier(Collection<SemanticPropertyInterface> listOfQualifier)
	{
		this.listOfQualifier = listOfQualifier;
	}

	/**
	 * Gets the sequence number.
	 * @return sequence number
	 * @hibernate.property name="sequenceNumber" type="int" column="SEQUENCE_NUMBER"
	 */
	public long getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * Sets the sequence number.
	 * @param sequenceNumber sequence number
	 */
	public void setSequenceNumber(long sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Compare to.
	 * @param object the object
	 * @return the int
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		SemanticProperty semanticProperty = (SemanticProperty) object;
		Long thisSequenceNumber = sequenceNumber;
		Long otherSequenceNumber = semanticProperty.getSequenceNumber();
		return thisSequenceNumber.compareTo(otherSequenceNumber);
	}
}
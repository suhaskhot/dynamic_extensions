package edu.common.dynamicextensions.domain;

/**
 * @hibernate.class table="DYEXTN_PATH_ASSOCIATION_RELATION"
 * @author mandar_shidhore
 *
 */
public class PathAssociationRelation extends DynamicExtensionBaseDomainObject implements PathAssociationRelationInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 12345678L;

	public PathAssociationRelation()
	{
		super();
	}
	
	/**
	 * This method returns the unique identifier of the PathAssociationRelation.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_PATH_ASSOCIATION_RELATION_SEQ"
	 * @return the identifier of the Path.
	 */
	public Long getId()
	{
		return id;
	}
	
	protected Path path;
	
	protected Association association;
	
	protected int pathSequenceNumber;

	/**
	 * @hibernate.many-to-one column="PATH_ID" class="edu.common.dynamicextensions.domain.Path" cascade="save-update"
	 * @return the path
	 */
	public Path getPath()
	{
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path)
	{
		this.path = path;
	}

	/**
	 * @hibernate.many-to-one column="ASSOCIATION_ID" class="edu.common.dynamicextensions.domain.Association" cascade="save-update"
	 * @return the association
	 */
	public Association getAssociation()
	{
		return association;
	}

	/**
	 * @param association the association to set
	 */
	public void setAssociation(Association association)
	{
		this.association = association;
	}

	/**
	 * @hibernate.property name="pathSequenceNumber" type="int" column="PATH_SEQUENCE_NUMBER"
	 * @return the pathSequenceNumber
	 */
	public int getPathSequenceNumber()
	{
		return pathSequenceNumber;
	}

	/**
	 * @param pathSequenceNumber the pathSequenceNumber to set
	 */
	public void setPathSequenceNumber(int pathSequenceNumber)
	{
		this.pathSequenceNumber = pathSequenceNumber;
	}

}
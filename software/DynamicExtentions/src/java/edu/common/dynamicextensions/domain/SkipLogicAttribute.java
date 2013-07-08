
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SkipLogicAttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.permissiblevalue.PermissibleValueUtility;

/**
 * This Class represents the general Attribute of the Entities
 * @hibernate.joined-subclass table="DYEXTN_SKIP_LOGIC_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class SkipLogicAttribute extends BaseAbstractAttribute
		implements
			SkipLogicAttributeInterface
{

	/**
	 *
	 */
	protected static final long serialVersionUID = 12345254735L;
	/**
	 *
	 */
	protected CategoryAttributeInterface sourceSkipLogicAttribute;
	/**
	 *
	 */
	protected CategoryAttributeInterface targetSkipLogicAttribute;
	/**
	 *
	 */
	protected Collection<PermissibleValueInterface> defaultPermissibleValuesCollection = new HashSet<PermissibleValueInterface>();
	/**
	 * The PV set from targetSkipLogicAttribute to be displayed,
	 * depending on the value of source attribute
	 */
	protected Set<DataElementInterface> dataElementCollection = new HashSet<DataElementInterface>();

	public SkipLogicAttribute()
	{
		// TODO Auto-generated constructor stub
	}
	public SkipLogicAttribute(SkipLogicAttribute skipLogicAttribute) throws DynamicExtensionsSystemException
	{
		if(skipLogicAttribute.getDefaultValuePermissibleValue() != null)
		{
			this.defaultPermissibleValuesCollection.add(PermissibleValueUtility.copy(skipLogicAttribute.getDefaultValuePermissibleValue()));
		}
		if(!skipLogicAttribute.getDataElementCollection().isEmpty())
		{
			
			for(DataElementInterface dataElement: skipLogicAttribute.getDataElementCollection())
			{
				this.dataElementCollection.add(new UserDefinedDE((UserDefinedDE)dataElement));
			}
		}
	}

	/**
	 * @return
	 * @hibernate.many-to-one cascade="save-update" column="SOURCE_SKIP_LOGIC_ID" class="edu.common.dynamicextensions.domain.CategoryAttribute" constrained="true"
	 */
	public CategoryAttributeInterface getSourceSkipLogicAttribute()
	{
		return sourceSkipLogicAttribute;
	}

	/**
	 *
	 */
	public void setSourceSkipLogicAttribute(CategoryAttributeInterface sourceSkipLogicAttribute)
	{
		this.sourceSkipLogicAttribute = sourceSkipLogicAttribute;
	}

	/**
	 * @return
	 * @hibernate.many-to-one cascade="save-update" column="TARGET_SKIP_LOGIC_ID" class="edu.common.dynamicextensions.domain.CategoryAttribute" constrained="true"
	 */
	public CategoryAttributeInterface getTargetSkipLogicAttribute()
	{
		return targetSkipLogicAttribute;
	}

	/**
	 *
	 */
	public void setTargetSkipLogicAttribute(CategoryAttributeInterface targetSkipLogicAttribute)
	{
		this.targetSkipLogicAttribute = targetSkipLogicAttribute;
	}

	/**
	 * @hibernate.set name="defaultPermissibleValuesCollection" table="DYEXTN_PERMISSIBLE_VALUE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SKIP_LOGIC_ATTRIBUTE_ID"
	 * @hibernate.cache usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.PermissibleValue"
	 * @return Returns the dataElementCollection.
	 */
	private Collection<PermissibleValueInterface> getDefaultPermissibleValuesCollection()
	{
		return defaultPermissibleValuesCollection;
	}

	/**
	 * @param defaultPermissibleValuesCollection the defaultPermissibleValuesCollection to set
	 */
	private void setDefaultPermissibleValuesCollection(
			Collection<PermissibleValueInterface> defaultPermissibleValuesCollection)
	{
		this.defaultPermissibleValuesCollection = defaultPermissibleValuesCollection;
	}

	/**
	 *
	 * @return
	 */
	public PermissibleValueInterface getDefaultValuePermissibleValue()
	{
		PermissibleValueInterface permissibleValueInterface = null;
		if (defaultPermissibleValuesCollection != null
				&& !defaultPermissibleValuesCollection.isEmpty())
		{
			Iterator<PermissibleValueInterface> dataElementIter = defaultPermissibleValuesCollection
					.iterator();
			permissibleValueInterface = dataElementIter.next();
		}
		return permissibleValueInterface;
	}

	/**
	 *
	 */
	public String getDefaultValue()
	{
		String defaultValue = null;
		if (defaultPermissibleValuesCollection != null
				&& !defaultPermissibleValuesCollection.isEmpty())
		{
			Iterator<PermissibleValueInterface> dataElementIter = defaultPermissibleValuesCollection
					.iterator();
			Object nextPV = dataElementIter.next().getValueAsObject();
			if (nextPV != null)
			{
				defaultValue = String.valueOf(nextPV);
			}
		}
		return defaultValue;
	}

	/**
	 *
	 */
	public void setDefaultValue(PermissibleValueInterface permissibleValue)
	{
		if (defaultPermissibleValuesCollection == null)
		{
			defaultPermissibleValuesCollection = new HashSet<PermissibleValueInterface>();
		}
		if (defaultPermissibleValuesCollection != null
				&& !defaultPermissibleValuesCollection.isEmpty())
		{
			Iterator<PermissibleValueInterface> iterator = defaultPermissibleValuesCollection
					.iterator();
			defaultPermissibleValuesCollection.remove(iterator.next());
		}

		defaultPermissibleValuesCollection.add(permissibleValue);
	}

	/**
	 * @hibernate.set name="dataElementCollection" table="DYEXTN_DATA_ELEMENT" cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SKIP_LOGIC_ATTRIBUTE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.DataElement"
	 * @return Returns the dataElementCollection.
	 */
	private Set<DataElementInterface> getDataElementCollection()
	{
		return dataElementCollection;
	}

	/**
	 * @param dataElementCollection The dataElementCollection to set.
	 */
	private void setDataElementCollection(Set<DataElementInterface> dataElementCollection)
	{
		this.dataElementCollection = dataElementCollection;
	}

	/**
	 *
	 * @return
	 */
	public DataElementInterface getDataElement()
	{
		DataElementInterface dataElementInterface = null;
		if (dataElementCollection != null)
		{
			Iterator<DataElementInterface> dataElementIterator = dataElementCollection.iterator();
			if (dataElementIterator.hasNext())
			{
				dataElementInterface = dataElementIterator.next();
			}
		}
		return dataElementInterface;
	}

	/**
	 *
	 */
	public void clearDataElementCollection()
	{
		dataElementCollection = null;
	}

	/**
	 *
	 * @param dataElementInterface
	 */
	public void setDataElement(DataElementInterface dataElementInterface)
	{
		if (dataElementCollection == null)
		{
			dataElementCollection = new HashSet();
		}
		dataElementCollection.add(dataElementInterface);
	}

	/**
	 *
	 */
	public boolean getIsSkipLogic()
	{
		return dataElementCollection.isEmpty();
	}

}
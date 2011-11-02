
package edu.common.dynamicextensions.category.beans;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAttribute;

public class UIProperty
{

	/**
	 * value.
	 */
	private String value;

	/**
	 * key.
	 */
	private String key;

	/**
	 * uiProperty.
	 */
	private Collection<UIProperty> uiProperty;

	/**
	 * Initializes internal data structures.
	 */
	public UIProperty()
	{
		uiProperty = new ArrayList<UIProperty>();
	}

	/**
	 * Instantiate key-value pair. Initializes internal data structures.
	 * @param key key.
	 * @param value value.
	 */
	public UIProperty(String key, String value)
	{
		super();
		this.key = key;
		this.value = value;
		uiProperty = new ArrayList<UIProperty>();
	}

	/**
	 * Instantiate key-value pair and Collection of UIProperty.
	 * @param key key.
	 * @param value value.
	 * @param uiProperty UI property.
	 */
	public UIProperty(String key, String value, Collection<UIProperty> uiProperty)
	{
		super();
		this.key = key;
		this.value = value;
		this.uiProperty = uiProperty;
	}

	/**
	 * Adds a UIProperty to Collection.
	 * @param propertyToBeAdd
	 */
	public void add(UIProperty propertyToBeAdd)
	{
		uiProperty.add(propertyToBeAdd);
	}

	/**
	 * Returns Collection of UIProperty.
	 * @return collection.
	 */
	public Collection<UIProperty> getUiProperty()
	{
		return uiProperty;
	}

	/**
	 * Sets a Collection of UIProperty.
	 * @param uiProperty UI property.
	 */
	public void setUiProperty(Collection<UIProperty> uiProperty)
	{
		this.uiProperty = uiProperty;
	}

	/**
	 * Returns key for this UIProperty.
	 * @return key.
	 */
	@XmlAttribute
	public String getKey()
	{
		return key;
	}

	/**
	 * Sets key for this UIProperty.
	 * @param key
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * Returns Value of this UIProperty.
	 * @return value.
	 */
	@XmlAttribute
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the value for this UIProperty.
	 * @param value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Overrides toString().
	 * @return string.
	 */
	public String toString()
	{
		return "UIProperty [key=" + key + ", value=" + value + "]";
	}

}

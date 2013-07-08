
package edu.common.dynamicextensions.permissiblevalue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class contains the utility method to create zip, extract zip etc.
 * @author pavan_kalantri
 *
 */
public final class PermissibleValueUtility
{

	private static final String ERROR_CREATING_PV_COPY = "Error creating PV copy";

	private PermissibleValueUtility()
	{

	}

	/**
	 * It will check whether the default PV of entity is in the PV list or not and if not
	 * add the default PV in PV list.
	 * @param attributeTypeInfo entity for which PV is being import.
	 * @param pvList PV list which is being imported.
	 */
	public static void checkAndUpdateDefaultPV(AttributeTypeInformationInterface attributeTypeInfo,Collection<PermissibleValueInterface> pvList)
	{
		if(attributeTypeInfo.getDefaultValue()!=null && attributeTypeInfo.getDefaultValue().getValueAsObject()!=null)
		{
			boolean ifDefaultValueInPV=false;
			for (PermissibleValueInterface permissibleValueInterface : pvList)
			{
					if(attributeTypeInfo.getDefaultValue().getValueAsObject().equals(permissibleValueInterface.getValueAsObject()))
					{
						ifDefaultValueInPV=true;
						break;
					}
			}
			if(!ifDefaultValueInPV)
			{
				pvList.add(attributeTypeInfo.getDefaultValue());
			}
		}
	}

	public static PermissibleValueInterface copy(
			PermissibleValueInterface defaultValuePermissibleValue)
			throws DynamicExtensionsSystemException
	{
		if (defaultValuePermissibleValue == null)
		{
			return null;
		}
		PermissibleValueInterface copyPV = null;
		try
		{
			copyPV = defaultValuePermissibleValue.getClass()
					.getConstructor(new Class[]{defaultValuePermissibleValue.getClass()})
					.newInstance(defaultValuePermissibleValue);

		}
		catch (IllegalArgumentException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}
		catch (SecurityException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}
		catch (InstantiationException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}
		catch (IllegalAccessException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}
		catch (InvocationTargetException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}
		catch (NoSuchMethodException e)
		{
			throw new DynamicExtensionsSystemException(ERROR_CREATING_PV_COPY, e);
		}

		return copyPV;
	}
}

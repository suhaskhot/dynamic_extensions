
package edu.common.dynamicextensions.permissiblevalue;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

/**
 * This class contains the utility method to create zip, extract zip etc.
 * @author pavan_kalantri
 *
 */
public final class PermissibleValueUtility
{

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
}

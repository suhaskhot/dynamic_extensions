/**
 *
 */

package edu.common.dynamicextensions.skiplogic;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.category.creation.PopulateSkipLogicHelper;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @author Gaurav_mehta
 *
 */
public final class SkipLogicUtility
{

	private SkipLogicUtility()
	{
	}

	/**
	 * Gets the default value for control.
	 * @param value the value
	 * @param targetControl the target control
	 * @return the default value for control
	 * @throws ParseException the parse exception
	 */
	public static PermissibleValueInterface getDefaultValueForControl(String value,
			ControlInterface targetControl) throws ParseException
	{
		CategoryAttributeInterface attribute = (CategoryAttributeInterface) targetControl
				.getAttibuteMetadataInterface();
		AbstractAttributeInterface abstractAttribute = attribute.getAbstractAttribute();
		AttributeTypeInformationInterface attributeTypeInfo = DynamicExtensionsUtility
				.getAttributeTypeInformation(abstractAttribute);
		PermissibleValueInterface defaultValue = attributeTypeInfo
				.getPermissibleValueForString(value);

		for (DataElementInterface dataElement : attribute.getDataElementCollection())
		{
			UserDefinedDEInterface userDefinedDe = (UserDefinedDEInterface) dataElement;
			Collection<PermissibleValueInterface> allPermissibleValues = userDefinedDe
					.getPermissibleValueCollection();
			if (allPermissibleValues.contains(defaultValue))
			{
				for (PermissibleValueInterface permissibleValue : allPermissibleValues)
				{
					if (permissibleValue.equals(defaultValue))
					{
						defaultValue.setObjectValue(permissibleValue.getValueAsObject());
						break;
					}
				}
			}
		}
		return defaultValue;
	}

	public static Condition copyCondition(Condition condition,CategoryInterface categoryInterface) throws DynamicExtensionsSystemException
	{
		Condition copyCondition = null;
		if(condition instanceof PrimitiveCondition)
		{
			copyCondition = new PrimitiveCondition((PrimitiveCondition) condition);
			PrimitiveCondition primitiveCondition = (PrimitiveCondition) condition;
			((PrimitiveCondition) copyCondition).setCategoryAttribute((categoryInterface
					.getCategoryEntityByName(primitiveCondition.getCategoryAttribute()
							.getCategoryEntity().getName()).getAttributeByName(primitiveCondition
					.getCategoryAttribute().getName())));
		}
		Action action = copyAction(condition.getAction(), categoryInterface);
		copyCondition.setAction(action);
		return copyCondition;
	}
	public static Action copyAction(Action action,CategoryInterface categoryInterface) throws DynamicExtensionsSystemException
	{
		Action copyCondition = null;
		if(action instanceof DisableAction)
		{
			copyCondition = new DisableAction((DisableAction)action);
		}else if(action instanceof EnableAction)
		{
			copyCondition = new EnableAction((EnableAction)action);
		}else if(action instanceof ShowAction)
		{
			copyCondition = new ShowAction((ShowAction)action);
		}else if(action instanceof HideAction)
		{
			copyCondition = new HideAction((HideAction)action);
		}else if(action instanceof PermissibleValueAction)
		{
			copyCondition = new PermissibleValueAction((PermissibleValueAction)action);
		}
		
		
		String categoryEntityName = ((CategoryAttributeInterface)action.getControl().getAttibuteMetadataInterface()).getCategoryEntity().getName();
		ContainerInterface container= (ContainerInterface) categoryInterface.getCategoryEntityByName(categoryEntityName).getContainerCollection().iterator().next();
		ControlInterface actionControl = container.getControlByPosition(action.getControl().getSequenceNumber(), action.getControl().getYPosition());
		copyCondition.setControl(actionControl);
		if(action instanceof PermissibleValueAction)
		{
			Set<PermissibleValueInterface> permissibleValues = new HashSet<PermissibleValueInterface>();
			
			for(PermissibleValueInterface permissibleValue:((PermissibleValueAction)action).getListOfPermissibleValues())
			{
				permissibleValues.add(PopulateSkipLogicHelper.getPermissibleValueObject((String) permissibleValue.getValueAsObject(),actionControl));
			}
			((PermissibleValueAction)copyCondition).setListOfPermissibleValues(permissibleValues);
		}
		return copyCondition;
	}
}

/**
 *
 */

package edu.common.dynamicextensions.skiplogic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * The Class ConditionStatements.
 *
 * @author Gaurav_mehta
 */
public class ConditionStatements
{

	/** The identifier. */
	private Long identifier;

	/** The category attribute identifier. */
	private Long controlIdentifier;

	/** The map of condition vs actions. */
	private Set<Condition> listOfConditions;
	
	private transient Long insertationOrder;

	public ConditionStatements()
	{
		// TODO Auto-generated constructor stub
	}
	public ConditionStatements(ConditionStatements conditionStatements, Long controlId, ContainerInterface container, CategoryInterface category) throws DynamicExtensionsSystemException
	{
		CategoryAttributeInterface catAttr = (CategoryAttributeInterface)EntityCache.getInstance().getControlById(controlId).getAttibuteMetadataInterface();
		ContainerInterface targetContainer = (ContainerInterface) category.getCategoryEntityByName(catAttr.getCategoryEntity().getName()).getContainerCollection().iterator().next();
		
		ControlInterface sourceControl = EntityCache.getInstance().getControlById(conditionStatements.getControlIdentifier());
		this.controlIdentifier = targetContainer.getControlByPosition(sourceControl.getSequenceNumber(), sourceControl.getYPosition()).getId();
		this.listOfConditions = new HashSet<Condition>();
		for(Condition condition :conditionStatements.getListOfConditions())
		{
			this.listOfConditions.add(SkipLogicUtility.copyCondition(condition, category));
		}
	}

	/**
	 * Sets the identifier.
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Gets the identifier.
	 * @return the identifier
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param controlIdentifier the controlIdentifier to set
	 */
	public void setControlIdentifier(Long controlIdentifier)
	{
		this.controlIdentifier = controlIdentifier;
	}

	/**
	 * @return the controlIdentifier
	 */
	public Long getControlIdentifier()
	{
		return controlIdentifier;
	}

	/**
	 * @param mapOfConditionVsActions the mapOfConditionVsActions to set
	 */
	public void setListOfConditions(Set<Condition> listOfConditions)
	{
		this.listOfConditions = listOfConditions;
	}

	/**
	 * @return the mapOfConditionVsActions
	 */
	public Set<Condition> getListOfConditions()
	{
		return listOfConditions;
	}

	public void evaluateConditions(Map<BaseAbstractAttributeInterface, Object> objectValueState,
			ContainerInterface controllingContainer) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		boolean conditionSatisfied = false;
		for (Condition condition : listOfConditions)
		{
			Action action = condition.getAction();
			if (condition.checkCondition(objectValueState,controllingContainer))
			{
				action.performAction(controllingContainer.getControlById(action.getControl().getId()));
				conditionSatisfied = true;
				break;
			}
		}
		if (!conditionSatisfied)
		{
			Condition condition = listOfConditions.iterator().next();
			ControlInterface control = condition.getAction().getControl();
			ControlInterface controlFromCache = controllingContainer.getControlById(control.getId());
			condition.getAction().resetAction(controlFromCache);
		}
	}

	
	public Long getInsertationOrder()
	{
		return insertationOrder;
	}

	
	public void setInsertationOrder(Long insertationOrder)
	{
		this.insertationOrder = insertationOrder;
	}

	
}

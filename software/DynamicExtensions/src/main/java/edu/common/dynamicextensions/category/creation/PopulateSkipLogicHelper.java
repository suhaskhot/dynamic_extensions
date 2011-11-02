/**
 *
 */

package edu.common.dynamicextensions.category.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.skiplogic.PermissibleValueAction;
import edu.common.dynamicextensions.util.CategoryGenerationUtil;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.validation.category.CategoryValidator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * The Class PopulateSkipLogicHelper.
 *
 * @author Gaurav_mehta
 */
public class PopulateSkipLogicHelper
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The entity group. */
	private transient final EntityGroupInterface entityGroup;

	/** The category. */
	private transient final CategoryInterface category;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(PopulateSkipLogicHelper.class);

	/**
	 * Instantiates a new populate skip logic helper.
	 * @param category the category
	 * @param entityGroup the entity group
	 */
	public PopulateSkipLogicHelper(CategoryInterface category, EntityGroupInterface entityGroup)
	{
		this.entityGroup = entityGroup;

		this.category = category;
	}

	/**
	 * This method returns the Control for the attribute on which Action is to be performed. If the action is to be performed on an association
	 * i.e when "all" is specified in dependent attribute, then it returns Control for that category association.
	 * @param categoryAttributePath the category attribute path
	 * @param attributeName the attribute name
	 * @param entityNameVsListOfAsso the entity name vs list of asso
	 * @return the category attribute control
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public List<ControlInterface> getCategoryAttributeControl(String categoryAttributePath,
			String attributeName, Map<String, List<AssociationInterface>> entityNameVsListOfAsso)
			throws DynamicExtensionsSystemException
	{
		CategoryHelper categoryHelper = new CategoryHelper();

		final String categoryEntityName = CategoryGenerationUtil.getCategoryEntityName(
				categoryAttributePath, entityNameVsListOfAsso);

		EntityInterface entity = getActualEntityForAttribute(attributeName,
				getEntity(categoryAttributePath));

		CategoryEntityInterface categoryEntity = categoryHelper.createOrUpdateCategoryEntity(
				category, entity, categoryEntityName);

		List<ControlInterface> allcontrolForAttribute = new ArrayList<ControlInterface>();

		if ("all".equalsIgnoreCase(attributeName))
		{
			ContainerInterface parentContainer = (ContainerInterface) categoryEntity
					.getContainerCollection().iterator().next();
			allcontrolForAttribute.addAll(parentContainer.getAllControls());
		}
		else
		{
			String errorMessage = "Attribute " + attributeName + " is not present in "
					+ entity.getName();
			CategoryValidator.checkForNullRefernce(entity.getAttributeByName(attributeName),
					errorMessage);
			LOGGER.debug(errorMessage);

			CategoryAttributeInterface categoryAttribute = categoryHelper.getCategoryAttribute(
					entity, attributeName, categoryEntity);

			final ContainerInterface container = DynamicExtensionsUtility
					.getContainerForAbstractEntity(categoryAttribute.getCategoryEntity());

			ControlInterface controlForAttribute = DynamicExtensionsUtility
					.getControlForAbstractAttribute((AttributeMetadataInterface) categoryAttribute,
							container, categoryEntityName);

			allcontrolForAttribute.add(controlForAttribute);
		}
		return allcontrolForAttribute;
	}

	/**
	 * This method returns the category Attribute of the Condition defined.
	 * @param categoryAttributePath the category attribute path
	 * @param attributeName the attribute name
	 * @param entityNameVsListOfAsso the entity name vs list of asso
	 * @return the category attribute control
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public CategoryAttributeInterface getConditionCategoryAttribute(String categoryAttributePath,
			String attributeName, Map<String, List<AssociationInterface>> entityNameVsListOfAsso)
			throws DynamicExtensionsSystemException
	{
		CategoryHelper categoryHelper = new CategoryHelper();

		final String categoryEntityName = CategoryGenerationUtil.getCategoryEntityName(
				categoryAttributePath, entityNameVsListOfAsso);

		EntityInterface entity = getActualEntityForAttribute(attributeName,
				getEntity(categoryAttributePath));

		CategoryEntityInterface categoryEntity = categoryHelper.createOrUpdateCategoryEntity(
				category, entity, categoryEntityName);

		String errorMessage = "Attribute " + attributeName + " is not present in "
				+ entity.getName();
		CategoryValidator.checkForNullRefernce(entity.getAttributeByName(attributeName),
				errorMessage);
		LOGGER.debug(errorMessage);

		return categoryHelper.getCategoryAttribute(entity, attributeName, categoryEntity);
	}

	/**
	 * Gets the permissible value object of the Permissible values passed.
	 * The Permissible value object returned is the reference of the permissible value present in Entity/Class level.
	 * Also that permissible values has to be present in any one of the versions created for that category or in its default version.
	 * @param permissibleValue the permissible value
	 * @param sourceControl the source control
	 * @return the permissible value object
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public PermissibleValueInterface getPermissibleValueObject(String permissibleValue,
			ControlInterface sourceControl) throws DynamicExtensionsSystemException
	{
		CategoryAttributeInterface categoryAttribute = (CategoryAttributeInterface) sourceControl
				.getAttibuteMetadataInterface();
		Set<DataElementInterface> allDataElement = categoryAttribute.getDataElementCollection();

		for (DataElementInterface dataElement : allDataElement)
		{
			final UserDefinedDEInterface userDefinedDE = (UserDefinedDEInterface) dataElement;
			Collection<PermissibleValueInterface> allPermissibleValues = userDefinedDE
					.getPermissibleValueCollection();
			for (PermissibleValueInterface pv : allPermissibleValues)
			{
				if (permissibleValue.equalsIgnoreCase(pv.getValueAsObject().toString()))
				{
					return pv;
				}
			}
		}
		throw new DynamicExtensionsSystemException("Permissible value " + permissibleValue
				+ " does not exists in any of the versions created for category attribute "
				+ categoryAttribute + " nor in its default version");
	}

	/**
	 * Gets the all dependent skip logics for each Permissible values.
	 * @param allPVs the all p vs
	 * @param targetControl the target control
	 * @return the all dependent skip logics
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public PermissibleValueAction getPermissibleValueAction(Set<String> allPVs,
			ControlInterface targetControl) throws DynamicExtensionsSystemException
	{
		PermissibleValueAction pvAction = new PermissibleValueAction();
		pvAction.setListOfPermissibleValues(new HashSet<PermissibleValueInterface>());

		for (String pv : allPVs)
		{
			PermissibleValueInterface permissibleValue = getPermissibleValueObject(pv,
					targetControl);
			pvAction.getListOfPermissibleValues().add(permissibleValue);
		}
		return pvAction;
	}

	/**
	 * Gets the actual entity for attribute.
	 * @param attributeName the attribute name.
	 * @param entity the entity.
	 * @return the actual entity for attribute.
	 */
	private EntityInterface getActualEntityForAttribute(String attributeName, EntityInterface entity)
	{
		EntityInterface actualEntity = entity;
		if (!entity.isAttributePresent(attributeName))
		{
			while (actualEntity.getParentEntity() != null)
			{
				actualEntity = actualEntity.getParentEntity();
				if (actualEntity.isAttributePresent(attributeName))
				{
					break;
				}
			}
		}
		return actualEntity;
	}

	/**
	 * Gets the entity.
	 * @param categoryAttributePath the category attribute path.
	 * @return the entity.
	 */
	private EntityInterface getEntity(final String categoryAttributePath)
	{
		final String entityNameWithAssociation = CategoryGenerationUtil
				.getEntityNameFromCategoryEntityInstancePath(categoryAttributePath);

		final String entityName = CategoryGenerationUtil
				.getEntityNameExcludingAssociationRoleName(entityNameWithAssociation);

		return entityGroup.getEntityByName(entityName);
	}
}

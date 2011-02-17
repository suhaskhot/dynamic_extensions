/**
 *
 */

package edu.common.dynamicextensions.permissiblevalue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.owasp.stinger.Stinger;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.XMLUtility;
import edu.common.dynamicextensions.util.xml.AttributeType;
import edu.common.dynamicextensions.util.xml.XmlClassType;
import edu.common.dynamicextensions.util.xml.XmlPermissibleValues;
import edu.common.dynamicextensions.util.xml.XmlPermissibleValues.XmlEntityGroup;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author Gaurav_mehta
 *
 */
public class PermissibleValuesProcessor
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(PermissibleValuesProcessor.class);

	/** The stinger validator. */
	private final Stinger stingerValidator;

	/** The Constant ENTITY_CACHE. */
	private static final EntityCache ENTITY_CACHE = EntityCache.getInstance();

	/** The entity manager. */
	private static final EntityManagerInterface ENTITY_MANAGER = EntityManager.getInstance();

	/**
	 * Instantiates a new permissible values processor.
	 * @param fileName the file name
	 * @param filePath the file path
	 * @param stinger the stinger
	 */
	public PermissibleValuesProcessor(Stinger stinger)
	{
		this.stingerValidator = stinger;
	}

	/**
	 * Import permissible values.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws SAXException
	 */
	public void importPermissibleValues(String fileName, String filePath) throws DynamicExtensionsSystemException
	{
		try
		{
			// This packageName is used for parsing XML.
			final String packageName = XmlPermissibleValues.class.getPackage().getName();

			final XmlPermissibleValues xmlPermissibleValues = (XmlPermissibleValues) XMLUtility
					.getJavaObjectForXML(fileName, filePath, packageName, "PV_XML.xsd");

			// Step 1. Fetch the Entity Group.
			EntityGroupInterface entityGroup = fetchEntityGroupAndClassName(xmlPermissibleValues);

			// Step 2. Populate classVsAttribute Map
			Map<String, List<AttributeType>> classVsAttributes = populateClassVsXmlAttributeMap(xmlPermissibleValues);
			// Step 3. Fetch the Attribute for each class and populate its Permissible Values.
			fetchAttributeAndPopulatePVs(entityGroup, classVsAttributes);
		}
		catch (SAXException saxException)
		{
			LOGGER.error(ApplicationProperties.getValue("pv.parse.exception"), saxException);
			throw new DynamicExtensionsSystemException(ApplicationProperties
					.getValue("pv.parse.exception"), saxException);
		}
	}

	/**
	 * Fetch entity group and class name from POJO object generated from XML.
	 * @param xmlPermissibleValues
	 * @return the entity group interface
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception.
	 */
	private EntityGroupInterface fetchEntityGroupAndClassName(
			XmlPermissibleValues xmlPermissibleValues) throws DynamicExtensionsSystemException
	{
		XmlEntityGroup xmlEntityGroup = xmlPermissibleValues.getXmlEntityGroup();
		Long entityGroupId = ENTITY_MANAGER.getEntityGroupId(xmlEntityGroup.getName());

		//Validates whether Entity Group exists or not
		PermissibleValuesValidator.validateForNull(entityGroupId, "pv.entity.group.error",
				xmlEntityGroup.getName());

		return ENTITY_CACHE.getEntityGroupById(entityGroupId);
	}

	/**
	 * Populate class Vs xmlAttribute map.
	 * @param xmlPermissibleValues the xmlPermissibleValues
	 * @return the map< string, list< attribute type>>
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private Map<String, List<AttributeType>> populateClassVsXmlAttributeMap(
			XmlPermissibleValues xmlPermissibleValues) throws DynamicExtensionsSystemException
	{
		Map<String, List<AttributeType>> classVsAttributes = new HashMap<String, List<AttributeType>>();

		for (XmlClassType eachClass : xmlPermissibleValues.getXmlEntityGroup().getXmlClassName())
		{
			classVsAttributes.put(eachClass.getName(), eachClass.getXmlDEAttribute());
		}
		return classVsAttributes;
	}

	/**
	 * Fetch attribute and populate permissible values.
	 * @param entityGroup the entity group
	 * @param classVsAttributes
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private void fetchAttributeAndPopulatePVs(EntityGroupInterface entityGroup,
			Map<String, List<AttributeType>> classVsAttributes)
			throws DynamicExtensionsSystemException
	{
		for (Entry<String, List<AttributeType>> classKey : classVsAttributes.entrySet())
		{
			String className = classKey.getKey();
			EntityInterface entity = entityGroup.getEntityByName(className);

			// Validation to check whether Entity exists or not.
			PermissibleValuesValidator.validateForNull(entity, "pv.entity.error", className);

			for (AttributeType attribute : classKey.getValue())
			{
				Long attributeId = ENTITY_MANAGER.getAttributeId(attribute.getName(), entity
						.getId());

				// Validation to check whether Attribute exists or not.
				PermissibleValuesValidator.validateForNull(attributeId, "pv.attribute.error",
						attribute.getName());
				Long assoAttrId = ENTITY_MANAGER.getAssociationAttributeId(attributeId);
				if (assoAttrId != null)
				{
					attributeId = assoAttrId;
				}
				AttributeTypeInformationInterface attrTypeInfo = ENTITY_MANAGER
						.getAttributeTypeInformation(attributeId);

				PermissibleValueProcessorHelper pvProcessorHelper = new PermissibleValueProcessorHelper(
						stingerValidator);
				Collection<PermissibleValueInterface> pvList = pvProcessorHelper
						.populatePermissibleValues(attribute, attrTypeInfo);
				pvProcessorHelper.updatePermissibleValueInCacheAndDB(entity, attributeId, pvList);
			}
		}
	}
}

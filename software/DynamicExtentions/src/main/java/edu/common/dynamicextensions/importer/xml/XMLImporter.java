
package edu.common.dynamicextensions.importer.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.importer.jaxb.Association;
import edu.common.dynamicextensions.importer.jaxb.Entity;
import edu.common.dynamicextensions.importer.jaxb.StaticMetaData;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.XMLUtility;
import edu.common.metadata.ClassMetadataMap;
import edu.common.metadata.impl.hbm.ClassMetadataMapImpl;
import edu.common.metadata.util.HibernateDaoHelper;
import edu.wustl.dao.HibernateDAO;

public class XMLImporter
{

	protected EntityGroupInterface entityGroup;
	protected StaticMetaData staticMetaData;
	private ClassMetadataMap classMetadataMap;

	public XMLImporter(String filePath, String entityGroupName, HibernateDAO dao)
			throws DynamicExtensionsSystemException
	{
		final String packageName = StaticMetaData.class.getPackage().getName();
		JAXBElement jAXBElement;
		try
		{
			jAXBElement = (JAXBElement) XMLUtility.getJavaObjectForXML(packageName,
					"SimplifiedMetaData.xsd", filePath);
			staticMetaData = (StaticMetaData) jAXBElement.getValue();
			entityGroup = EntityGroupManager.getInstance().getEntityGroupByName(entityGroupName);

			classMetadataMap = ClassMetadataMapImpl.createClassMetadataMap(HibernateDaoHelper
					.getSessionFactory(dao), getClassNames(staticMetaData.getEntity()));
		}
		catch (SAXException e)
		{
			throw new DynamicExtensionsSystemException("Error generating object using xml:"
					+ filePath, e);
		}

	}

	private Collection<String> getClassNames(List<Entity> entityCollection)
	{

		List<String> list = new ArrayList<String>();
		for (Entity entity : entityCollection)
		{
			list.add(entity.getName());
		}
		return list;
	}

	public void processXml()
	{
		EntityTypeProcessor entityTypeProcessor = new EntityTypeProcessor(classMetadataMap,
				entityGroup);

		entityTypeProcessor.process(staticMetaData.getEntity());

		List<Association> associationsList = staticMetaData.getAssociation();

		AssociationTypeProcessor associationProcessor = new AssociationTypeProcessor(
				classMetadataMap, entityGroup);

		associationProcessor.process(associationsList);

	}

}

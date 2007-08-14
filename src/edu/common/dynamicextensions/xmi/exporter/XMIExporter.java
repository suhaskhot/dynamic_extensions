/*
 * Created on Aug 13, 2007
 * @author
 *
 */
package edu.common.dynamicextensions.xmi.exporter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.openide.util.Lookup;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.xmi.XMIUtilities;

/**
 * @author preeti_lodha
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class XMIExporter implements XMIExportInterface
{
	//Repository
	private static MDRepository repository = MDRManager.getDefault().getDefaultRepository();
	// name of a UML extent (instance of UML metamodel) that the UML models will be loaded into
	private static final String UML_INSTANCE = "UMLInstance";
	// name of a MOF extent that will contain definition of UML metamodel
	private static final String UML_MM = "UML";
	
	// UML extent
	private static UmlPackage umlPackage;
	//Model
	private static Model umlModel = null;
	//Leaf package
	org.omg.uml.modelmanagement.UmlPackage logicalModel = null; 
	
	/*// MOF extent
	private static ModelPackage mof;*/
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, javax.jmi.reflect.RefPackage, java.lang.String)
	 */
	public void exportXMI(String filename, RefPackage extent, String xmiVersion) throws IOException
	{

		//get xmi writer
		XmiWriter writer = XMIUtilities.getXMIWriter();
		//get output stream for file : appendmode : false
		FileOutputStream outputStream = new FileOutputStream(filename,false);

		repository.beginTrans(true);
		try {
			writer.write(outputStream, extent, xmiVersion);
			System.out.println( "XMI written successfully");
		} finally {

			repository.endTrans(true);
			// shutdown the repository to make sure all caches are flushed to disk
			MDRManager.getDefault().shutdownAll();
			outputStream.close();
		}
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.xmi.exporter.XMIExportInterface#exportXMI(java.lang.String, edu.common.dynamicextensions.domaininterface.EntityGroupInterface, java.lang.String)
	 */
	public void exportXMI(String filename, EntityGroupInterface entityGroup, String xmiVersion) throws Exception
	{
		init();
		generateUMLModel(entityGroup);
		exportXMI(filename, umlPackage, xmiVersion);
	} 

	/**
	 * @param entityGroup
	 * @return
	 */
	private void generateUMLModel(EntityGroupInterface entityGroup)
	{
		if(entityGroup!=null)
		{
			//Create package for entity group
			org.omg.uml.modelmanagement.UmlPackage leafPackage = getLeafPackage(entityGroup.getName());
			//create classes for entities 
			Collection<UmlClass> entityClasses = createClassesForEntities(entityGroup.getEntityCollection());
			//add entity classes to group package
			leafPackage.getOwnedElement().addAll(entityClasses);
		}
	}

	/**
	 * @param entityCollection
	 * @return
	 */
	private Collection<UmlClass> createClassesForEntities(Collection<EntityInterface> entityCollection)
	{
		ArrayList<UmlClass> entityClasses = new ArrayList<UmlClass>();
		if(entityCollection!=null)
		{
			Iterator entityCollectionIter = entityCollection.iterator();
			while(entityCollectionIter.hasNext())
			{
				EntityInterface entity = (EntityInterface)entityCollectionIter.next();
				UmlClass entityClass = createClassForEntity(entity);
				if(entityClass!=null)
				{
					entityClasses.add(entityClass);
				}
			}
		}
		return entityClasses;
	}

	/**
	 * @param entity
	 * @return
	 */
	private UmlClass createClassForEntity(EntityInterface entity)
	{
		UmlClass entityClass = null;
		if(entity!=null)
		{
			String className = XMIUtilities.getClassNameForEntity(entity);  
			CorePackage corePackage = umlPackage.getCore();
			entityClass = corePackage.getUmlClass().createUmlClass(className, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, false);
		}
		return entityClass;
	}

	/**
	 * @param entity
	 * @return
	 */
	private String getEntityName(EntityInterface entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	private org.omg.uml.modelmanagement.UmlPackage getLeafPackage(String leafName)
	{
		org.omg.uml.modelmanagement.UmlPackage leafPackage = createPackage(leafName);
		logicalModel.getOwnedElement().add(leafPackage);
		return leafPackage;
	}
	/**
	 * @param modelManagement
	 * @param umlModel
	 * @param string
	 * @return
	 */
	private static org.omg.uml.modelmanagement.UmlPackage createPackage(String packageName)
	{   
		ModelManagementPackage modelManagement = umlPackage.getModelManagement();
		org.omg.uml.modelmanagement.UmlPackage newPackage = modelManagement.getUmlPackage().createUmlPackage(packageName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
		return newPackage;
	}

	public void init() throws CreationFailedException, Exception
	{
		initializeUMLPackage();
		initializeModel();
		initializePackageHierarchy();
	}
	/**
	 * 
	 */
	private void initializePackageHierarchy()
	{
		org.omg.uml.modelmanagement.UmlPackage logicalView = createPackage("Logical View");
		umlModel.getOwnedElement().add(logicalView);
		
		logicalModel = createPackage("Logical Model");
		logicalView.getOwnedElement().add(logicalModel);
	}

	/**
	 * 
	 */
	private void initializeModel()
	{
		//Initialize model
		ModelManagementPackage modelManagementPackage = umlPackage.getModelManagement();
		
		//Create Logical Model
		umlModel = modelManagementPackage.getModel().createModel("EA Model", VisibilityKindEnum.VK_PUBLIC, false, false,false,false); 
		System.out.println("Model Created " + umlModel);
		
		
	}

	/**
	 * @throws Exception 
	 * @throws CreationFailedException 
	 * 
	 */
	private void initializeUMLPackage() throws CreationFailedException, Exception
	{
		//Get UML Package
		umlPackage = (UmlPackage) repository.getExtent(UML_INSTANCE);
		if (umlPackage == null) {
			// UML extent does not exist -> create it (note that in case one want's to instantiate
			// a metamodel other than MOF, they need to provide the second parameter of the createExtent
			// method which indicates the metamodel package that should be instantiated)
			umlPackage = (UmlPackage) repository.createExtent(UML_INSTANCE, getUmlPackage());
		}
	}

	/** Finds "UML" package -> this is the topmost package of UML metamodel - that's the
	 * package that needs to be instantiated in order to create a UML extent
	 */
	private static MofPackage getUmlPackage() throws Exception {
		// get the MOF extent containing definition of UML metamodel
		ModelPackage umlMM = (ModelPackage) repository.getExtent(UML_MM);
		if (umlMM == null) {
			// it is not present -> create it
			umlMM = (ModelPackage) repository.createExtent(UML_MM);
		}
		// find package named "UML" in this extent
		MofPackage result = getUmlPackage(umlMM);
		if (result == null) {
			// it cannot be found -> UML metamodel is not loaded -> load it from XMI
			XmiReader reader = (XmiReader) Lookup.getDefault().lookup(XmiReader.class);
			reader.read(UmlPackage.class.getResource("resources/01-02-15_Diff.xml").toString(), umlMM);
			// try to find the "UML" package again
			result = getUmlPackage(umlMM);
		}
		return result;
	}
	/** Finds "UML" package in a given extent
	 * @param umlMM MOF extent that should be searched for "UML" package.
	 */
	private static MofPackage getUmlPackage(ModelPackage umlMM) {
		// iterate through all instances of package
		System.out.println("Here");
		for (Iterator it = umlMM.getMofPackage().refAllOfClass().iterator(); it.hasNext();) {
			MofPackage pkg = (MofPackage) it.next();
			// is the package topmost and is it named "UML"?
			if (pkg.getContainer() == null && "UML".equals(pkg.getName())) {
				// yes -> return it
				return pkg;
			}
		}
		// a topmost package named "UML" could not be found
		return null;
	}
	public static void main(String args[])
	{
		XMIExporter e =  new XMIExporter();
		EntityGroup entityGroup = new EntityGroup();
		entityGroup.setName("New Group");
		EntityInterface entity1  = new Entity();
		entity1.setName("Entity One");
		EntityInterface entity2  = new Entity();
		entity2.setName("Entity Two");
		EntityInterface entity3  = new Entity();
		entity3.setName("Entity Three");
		EntityInterface entity4  = new Entity();
		entity4.setName("Entity Four");
		
		entityGroup.addEntity(entity1);
		entityGroup.addEntity(entity2);
		entityGroup.addEntity(entity3);
		entityGroup.addEntity(entity4);
		
		try
		{
			e.exportXMI("abc.xmi", entityGroup, null);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

}

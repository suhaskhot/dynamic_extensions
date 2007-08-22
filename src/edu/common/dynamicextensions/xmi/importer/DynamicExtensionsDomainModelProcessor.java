
package edu.common.dynamicextensions.xmi.importer;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelClass;
import org.omg.uml.modelmanagement.ModelManagementPackage;

import edu.common.dynamicextensions.domain.AssociationDisplayAttribute;
import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domain.userinterface.SelectControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.RuleConfigurationObject;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.xmi.XMIConstants;



/**
 * 
 * @author sujay_narkar
 * @author ashish_gupta
 *
 */
public class DynamicExtensionsDomainModelProcessor //extends BaseDomainModelProecessor
{
	/**
	 * Instance of Domain object factory, which will be used to create  dynamic extension's objects.
	 */
	protected static DomainObjectFactory deFactory = DomainObjectFactory.getInstance();

	/**
	 * Map with KEY : UML id of a class(coming from domain model) VALUE : dynamic extension Entity created for this UML class.  
	 */
	protected Map<String, EntityInterface> umlClassIdVsEntity;

	/**
	 * Saved entity group created by this class
	 */
	private EntityGroupInterface entityGroup;

	/**
	 * Map for storing containers corresponding to entities
	 */
	protected Map<String, List<ContainerInterface>> entityIdVsContainers = new HashMap<String, List<ContainerInterface>>();
	


	/**
	 * Default constructor
	 *
	 */
	public DynamicExtensionsDomainModelProcessor()
	{
		super();
	}

	/**
	 * 
	 * @param parser
	 * @param applicationName
	 */
	public DynamicExtensionsDomainModelProcessor(UmlPackage umlPackage, String entityGroupName)
			throws Exception
	{		
		List<UmlClass> umlClassColl = new ArrayList<UmlClass>(); 
	    List<UmlAssociation> umlAssociationColl = new ArrayList<UmlAssociation>(); 
	    List<Generalization> umlGeneralisationColl = new ArrayList<Generalization>(); 
	    
		//		super(parser, applicationName);
		entityGroup = DomainObjectFactory.getInstance().createEntityGroup();
		entityGroup.setShortName(entityGroupName);
		entityGroup.setName(entityGroupName);
		entityGroup.setLongName(entityGroupName);
		entityGroup.setDescription(entityGroupName);
		entityGroup.setIsSystemGenerated(false);		
		
		processModel(umlPackage,umlClassColl,umlAssociationColl,umlGeneralisationColl);
		
		int noOfClasses = umlClassColl.size();
		umlClassIdVsEntity = new HashMap<String, EntityInterface>(noOfClasses);

		//Creating entities and entity group.
		for (UmlClass umlClass : umlClassColl)
		{
			EntityInterface entity = createEntity(umlClass);
			entity.addEntityGroupInterface(entityGroup);
			entityGroup.addEntity(entity);
			umlClassIdVsEntity.put(umlClass.refMofId(), entity);
		}

		Map<String, List<String>> parentIdVsChildrenIds = new HashMap<String, List<String>>();

		if (umlGeneralisationColl.size() > 0)
		{
			parentIdVsChildrenIds = getParentVsChildrenMap(umlGeneralisationColl);
		}

		if (umlAssociationColl != null)
		{
			for (UmlAssociation umlAssociation : umlAssociationColl)
			{
				addAssociation(umlAssociation, parentIdVsChildrenIds);
			}
		}
		if (umlGeneralisationColl.size() > 0)
		{
			processInheritance(parentIdVsChildrenIds);
//			markInheritedAttributes(entityGroup);
		}		
		
		for (UmlClass umlClass : umlClassColl)
		{
			EntityInterface entity = umlClassIdVsEntity.get(umlClass.refMofId());
			//In memory operation
			createContainer(entity);
		}
		if (umlGeneralisationColl.size() > 0)
		{
			//setting base container in child container.
			postProcessInheritence(parentIdVsChildrenIds);			
		}
		if (umlAssociationColl.size() > 0)
		{
			//Adding container for containment control
			postProcessAssociation();
		}
		//Persist container in DB
		processPersistence();
	}
	/**
	 * @param umlPackage
	 * @param umlClassColl
	 * @param umlAssociationColl
	 * @param umlGeneralisationColl
	 */
	private void processModel(UmlPackage umlPackage,List<UmlClass> umlClassColl,List<UmlAssociation> umlAssociationColl,List<Generalization> umlGeneralisationColl)
	{
		ModelManagementPackage modelManagementPackage = umlPackage.getModelManagement();	       
	    ModelClass modelClass = modelManagementPackage.getModel();	       
	    Collection<Model> modelColl = modelClass.refAllOfClass();
	    	    
	    for(Model model : modelColl)
        {	    	
        	Collection ownedElementColl = model.getOwnedElement();
        	System.out.println("MODEL OWNED ELEMENT SIZE: "+ownedElementColl.size());
        	Iterator iter = ownedElementColl.iterator();
        	while(iter.hasNext())
        	{
        		Object obj = iter.next();
        		if(obj instanceof org.omg.uml.modelmanagement.UmlPackage)
        		{
        			org.omg.uml.modelmanagement.UmlPackage umlPackageObj = (org.omg.uml.modelmanagement.UmlPackage)obj;
        			processPackage(umlPackageObj,umlClassColl,umlAssociationColl,umlGeneralisationColl); 
        		}
        	}	        
        }	
	}
	 /**
     * @param parentPkg
     * @param pkgName
     * @return
     */
    private void processPackage(org.omg.uml.modelmanagement.UmlPackage parentPkg,List<UmlClass> umlClasses,List<UmlAssociation> associations,List<Generalization> generalizations)
    {	    	
		for (Iterator i = parentPkg.getOwnedElement().iterator(); i.hasNext();) 
		{
			Object o = i.next();					
			if (o instanceof org.omg.uml.modelmanagement.UmlPackage)
			{			
				org.omg.uml.modelmanagement.UmlPackage subPkg = (org.omg.uml.modelmanagement.UmlPackage) o;
				processPackage(subPkg,umlClasses,associations,generalizations);					
			}
			else if(o instanceof UmlAssociation)
			{
				associations.add((UmlAssociation)o);
			}
//			else if(o instanceof Generalization)
//			{
//				generalizations.add((Generalization)o);
//			}
			else if(o instanceof UmlClass)
			{
				UmlClass umlClass = (UmlClass)o;
				Collection<Generalization> generalizationColl = umlClass.getGeneralization();
				if(generalizationColl != null && generalizationColl.size() > 0)
				{
					generalizations.addAll(generalizationColl);
				}
				umlClasses.add(umlClass);
			}
		}			
	}
    /**
	 * Creates a Dynamic Exension Entity from given UMLClass.<br>
	 * It also assigns all the attributes of the UMLClass to the Entity as the
	 * Dynamic Extension Primitive Attributes.Then stores the input UML class,
	 * adds the Dynamic Extension's PrimitiveAttributes to the Collection.
	 * Properties which are copied from UMLAttribute to DE Attribute are
	 * name,description,semanticMetadata,permissible values
	 * @param umlClass
	 *            The UMLClass from which to form the Dynamic Extension Entity
	 * @return the unsaved entity for given UML class
	 */
	private EntityInterface createEntity(UmlClass umlClass)
	{
		//TODO
		String name =(/*(org.omg.uml.modelmanagement.UmlPackage)(umlClass.refImmediatePackage())).getName() + "." + */umlClass.getName());
		EntityInterface entity = deFactory.createEntity();
		entity.setName(name);
		entity.setDescription(umlClass.getName());
		Collection<Attribute> attrColl = getAttributes(umlClass,false);
		
		if (attrColl != null)
		{
			for (Attribute umlAttribute : attrColl)
			{
				DataType dataType = DataType.get(umlAttribute.getType().getName());
				AttributeInterface attribute = dataType.createAttribute(umlAttribute);
				if (attribute != null)
				{ // to bypass attributes of invalid datatypes
					attribute.setName(umlAttribute.getName());
//					attribute.setDescription(umlAttribute.getTaggedValue().getDescription());
//					setSemanticMetadata(attribute, umlAttribute.getSemanticMetadata());
					entity.addAttribute(attribute);
				}
			}
		}
//		setSemanticMetadata(entity, umlClass.getSemanticMetadata());
		return entity;
	}
	 /**
     * @param klass
     * @param includeInherited
     * @return
     */
    private Collection getAttributes(UmlClass klass,
			boolean includeInherited) {

		Collection atts = new ArrayList();

		if (includeInherited) {

			Map attsMap = new HashMap();
			UmlClass superClass = klass;
			do {
				for (Iterator i = superClass.getFeature().iterator(); i
						.hasNext();) {
					Object o = i.next();
					if (o instanceof Attribute) {
						Attribute att = (Attribute) o;
						if (attsMap.get(att.getName()) == null) {
							attsMap.put(att.getName(), att);
						}
					}
				}
				superClass = getSuperClass(superClass);
			} while (superClass != null);

			atts = attsMap.values();
		} else {
			for (Iterator i = klass.getFeature().iterator(); i.hasNext();) {
				Object o = i.next();
				if (o instanceof Attribute) {
					atts.add(o);
				}
			}
		}
		return atts;
	}
    /**
     * @param klass
     * @return
     */
    private UmlClass getSuperClass(UmlClass klass) {
		UmlClass superClass = null;
		List superClasses = getSuperClasses(klass);
		if (superClasses.size() > 0) {
			superClass = (UmlClass) superClasses.iterator().next();
		}
		return superClass;
	}

	/**
	 * @param klass
	 * @return
	 */
	private List getSuperClasses(UmlClass klass) {
		List superClasses = new ArrayList();
		for (Iterator i = klass.getGeneralization().iterator(); i.hasNext();) {
			superClasses.add(((Generalization) i.next()).getParent());
		}
		return superClasses;
	}
	 /**
     * Gives a map having parent child information.
     * @return Map with key as UML-id of parent class and value as list of UML-id of all children classes.
     */
    private Map<String,List<String>> getParentVsChildrenMap(List<Generalization> umlGeneralisationColl)
    {        
        if (umlGeneralisationColl != null)
        {           
            HashMap<String, List<String>> parentIdVsChildrenIds = new HashMap<String, List<String>>(umlGeneralisationColl.size());
            for (Generalization umlGeneralization : umlGeneralisationColl)
            {            	
                String childClass = umlGeneralization.getChild().refMofId();
                String parentClass = umlGeneralization.getParent().refMofId();
                List<String> children = parentIdVsChildrenIds.get(parentClass);
                if (children == null)
                {
                    children = new ArrayList<String>();
                    parentIdVsChildrenIds.put(parentClass, children);
                }
                children.add(childClass);
            }
            return parentIdVsChildrenIds;
        }
        
        return new HashMap<String, List<String>>(0);
    }
    /**
	 * Converts the UML association to dynamic Extension Association.Adds it to the entity group.
	 * It replicates this association in all children of source and all children of target class.
	 * It taggs replicated association to identify them later on and mark them inherited. 
	 * Also a back pointer is added to replicated association go get original association.
	 * @param umlAssociation umlAssociation to process
	 * @param parentIdVsChildrenIds Map with key as UML-id of parent class and value as list of UML-id of all children classes.
	 */
	private void addAssociation(UmlAssociation umlAssociation,
			Map<String, List<String>> parentIdVsChildrenIds)
	{			
		List<AssociationEnd> associationEnds = umlAssociation.getConnection();				
		String direction = "";		
		
		AssociationEnd sourceAssociationEnd = associationEnds.get(0);
		AssociationEnd targetAssociationEnd = associationEnds.get(1);
		
		Collection<TaggedValue> taggedValueColl = umlAssociation.getTaggedValue();
		for(TaggedValue taggedValue:  taggedValueColl)
		{
			if(taggedValue.getName() != null)
			{
				if(taggedValue.getName().equalsIgnoreCase(XMIConstants.TAGGED_NAME_ASSOC_DIRECTION))
				{
					Collection<String> dataValueColl = taggedValue.getDataValue();
					for(String value : dataValueColl)
					{
						direction = value;
					}
				}
			}
		}		
		String srcId = sourceAssociationEnd.getParticipant().refMofId();
		Multiplicity srcMultiplicity = sourceAssociationEnd.getMultiplicity();
		String sourceRoleName = sourceAssociationEnd.getName();
		EntityInterface srcEntity = umlClassIdVsEntity.get(srcId);	
		
		String tgtId = targetAssociationEnd.getParticipant().refMofId();
		Multiplicity tgtMultiplicity = targetAssociationEnd.getMultiplicity();
		String tgtRoleName = targetAssociationEnd.getName();
		EntityInterface tgtEntity = umlClassIdVsEntity.get(tgtId);

		//Adding association to entity
		AssociationInterface association = getAssociation(srcEntity);
		association.setSourceRole(getRole(srcMultiplicity,sourceRoleName));
		association.setTargetEntity(tgtEntity);
		association.setTargetRole(getRole(tgtMultiplicity,tgtRoleName));
	
		if (direction.equalsIgnoreCase(XMIConstants.TAGGED_VALUE_ASSOC_BIDIRECTIONAL))
		{
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
		}
		else
		{
			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
		}		
	}
	/**
	 * Processes inheritance relation ship present in domain model 
	 * @param parentIdVsChildrenIds Map with key as UML-id of parent class and value as list of UML-id of all children classes.
	 */
	private void processInheritance(Map<String, List<String>> parentIdVsChildrenIds)
	{
		for (Entry<String, List<String>> entry : parentIdVsChildrenIds.entrySet())
		{
			EntityInterface parent = umlClassIdVsEntity.get(entry.getKey());
			for (String childId : entry.getValue())
			{
				EntityInterface child = umlClassIdVsEntity.get(childId);
				child.setParentEntity(parent);
			}
		}
	}
	/**
	 * Taggs inherited attributes present in given entity group. The processing is based on name.
	 * For a attribute, if attribute with same name present in parent hirarchy then it is considered as inherited. 
	 * @param eg Entity Group top process
	 */
	private void markInheritedAttributes(EntityGroupInterface eg)
	{
		for (EntityInterface entity : eg.getEntityCollection())
		{
			if (entity.getParentEntity() != null)
			{
				List<AbstractAttributeInterface> duplicateAttrColl = new ArrayList<AbstractAttributeInterface>();
				Collection<AttributeInterface> parentAttributeCollection = entity.getParentEntity()
						.getAttributeCollection();
				for (AttributeInterface attributeFromChild : entity.getAttributeCollection())
				{
					boolean isInherited = false;
					for (AttributeInterface attributeFromParent : parentAttributeCollection)
					{
						if (attributeFromChild.getName().equals(attributeFromParent.getName()))
						{
							isInherited = true;
							duplicateAttrColl.add(attributeFromChild);
							break;
						}
					}
//					if (isInherited)
//					{
//						markInherited(attributeFromChild);
//					}
				}
				//removeInheritedAttributes(entity,duplicateAttrColl,true);
				removeInheritedAttributes(entity, duplicateAttrColl);
			}
		}
	}
	/**
	 * @param sourceEntity Entity to which a association is to be attached
	 * @return A assocition attached to given entity.
	 */
	private AssociationInterface getAssociation(EntityInterface sourceEntity)
	{
		AssociationInterface association = deFactory.createAssociation();
		// TODO remove it after getting DE fix,association name should not be compulsory
		association.setName("AssociationName_"
				+ (sourceEntity.getAssociationCollection().size() + 1));
		association.setEntity(sourceEntity);
		sourceEntity.addAssociation(association);
		return association;
	}
	/**
	 * Creates Role for the input UMLAssociationEdge
	 * @param edge UML Association Edge to process
	 * @return the Role for given UML Association Edge
	 */
	private RoleInterface getRole(Multiplicity srcMultiplicity,String sourceRoleName)
	{
		Collection<MultiplicityRange> rangeColl = srcMultiplicity.getRange();
		int minCardinality = 0;
		int maxCardinality = 0;
		for(MultiplicityRange range : rangeColl)
		{
			minCardinality = range.getLower();
			maxCardinality = range.getUpper();
		}
		
		RoleInterface role = deFactory.createRole();
		role.setAssociationsType(Constants.AssociationType.ASSOCIATION);
		role.setName(sourceRoleName);
		role.setMaximumCardinality(getCardinality(maxCardinality));
		role.setMinimumCardinality(getCardinality(minCardinality));
		return role;
	}
	/**
	 * Gets dynamic extension's Cardinality enumration for passed integer value.
	 * @param cardinality intger value of cardinality.
	 * @return Dynamic Extension's Cardinality enumration
	 */
	private Constants.Cardinality getCardinality(int cardinality)
	{
		if (cardinality == 0)
		{
			return Constants.Cardinality.ZERO;
		}
		if (cardinality == 1)
		{
			return Constants.Cardinality.ONE;
		}
		return Constants.Cardinality.MANY;
	}


	/**
	 * This method creates a container object.
	 * @param entityInterface
	 * @return
	 */
	protected void createContainer(EntityInterface entityInterface) throws Exception
	{
		ContainerInterface containerInterface = deFactory.createContainer();
		containerInterface.setCaption(entityInterface.getName());
		containerInterface.setEntity(entityInterface);

		//Adding Required field indicator
		containerInterface.setRequiredFieldIndicatior(" ");
		containerInterface.setRequiredFieldWarningMessage(" ");

		Collection<AbstractAttributeInterface> abstractAttributeCollection = entityInterface
				.getAbstractAttributeCollection();
		Integer sequenceNumber = new Integer(0);
		ControlInterface controlInterface;
		for (AbstractAttributeInterface abstractAttributeInterface : abstractAttributeCollection)
		{
			controlInterface = getControlForAttribute(abstractAttributeInterface);
			sequenceNumber++;
			controlInterface.setSequenceNumber(sequenceNumber);
			containerInterface.addControl(controlInterface);
			controlInterface.setParentContainer((Container) containerInterface);
		}
		List<ContainerInterface> containerList = new ArrayList<ContainerInterface>();
		containerList.add(containerInterface);
		entityIdVsContainers.put(entityInterface.getName(), containerList);
	}

	/**
	 * @param parentIdVsChildrenIds
	 * This method add the parent container to the child container for Generalisation.
	 */
	protected void postProcessInheritence(Map<String, List<String>> parentIdVsChildrenIds)
			throws Exception
	{
		for (Entry<String, List<String>> entry : parentIdVsChildrenIds.entrySet())
		{
			EntityInterface parent = umlClassIdVsEntity.get(entry.getKey());
				
			List parentContainerList = (ArrayList) entityIdVsContainers.get(parent.getName());
			ContainerInterface parentContainer = (ContainerInterface) parentContainerList.get(0);
			for (String childId : entry.getValue())
			{
				EntityInterface child = umlClassIdVsEntity.get(childId);
						
				List childContainerList = (ArrayList) entityIdVsContainers.get(child.getName());
				ContainerInterface childContainer = (ContainerInterface) childContainerList.get(0);

				childContainer.setBaseContainer(parentContainer);

			}
		}
	}

	/**
	 * This method adds the target container to the containment association control
	 */
	protected void addControlsForAssociation() throws Exception
	{
		Set<String> entityIdKeySet = entityIdVsContainers.keySet();
		for (String entityId : entityIdKeySet)
		{
			List containerList = (ArrayList) entityIdVsContainers.get(entityId);
			ContainerInterface containerInterface = (ContainerInterface) containerList.get(0);
			Collection<ControlInterface> controlCollection = containerInterface
					.getControlCollection();

			for (ControlInterface controlInterface : controlCollection)
			{
				if (controlInterface instanceof ContainmentAssociationControl)
				{
					ContainmentAssociationControl containmentAssociationControl = (ContainmentAssociationControl) controlInterface;
					AssociationInterface associationInterface = (AssociationInterface) controlInterface
							.getAbstractAttribute();				

					String targetEntityId = associationInterface.getTargetEntity().getName();

					List targetContainerInterfaceList = (ArrayList) entityIdVsContainers
							.get(targetEntityId.toString());
					ContainerInterface targetContainerInterface = (ContainerInterface) targetContainerInterfaceList
							.get(0);
					containmentAssociationControl.setContainer(targetContainerInterface);
				}
			}
		}
	}

	/**
	 * 
	 * @param abstractAttributeInterface
	 * @return
	 * This method creates a control for the attribute.
	 */
	private ControlInterface getControlForAttribute(
			AbstractAttributeInterface abstractAttributeInterface) throws DynamicExtensionsSystemException
	{
		ControlInterface controlInterface = null;
		
		ControlConfigurationsFactory configurationsFactory = ControlConfigurationsFactory
		.getInstance();
				
		// Collect all the applicable Rule names 
		List<String> implicitRuleList = null;
		
	

		if (abstractAttributeInterface instanceof AssociationInterface)
		{
			AssociationInterface associationInterface = (AssociationInterface) abstractAttributeInterface;
			//TODO This line is for containment association.
			controlInterface = deFactory.createContainmentAssociationControl();
			associationInterface.getSourceRole().setAssociationsType(AssociationType.CONTAINTMENT);
			associationInterface.getTargetRole().setAssociationsType(AssociationType.CONTAINTMENT);

			//			TODO this is for Linking Association
			//if source maxcardinality or target  maxcardinality or both == -1, then control is listbox.
			//int  sourceMaxCardinality = associationInterface.getSourceRole().getMaximumCardinality().getValue().intValue();

			//			int targetMaxCardinality = associationInterface.getTargetRole().getMaximumCardinality()
			//					.getValue().intValue();
			//			if (targetMaxCardinality == -1)
			//			{//List box for 1 to many or many to many relationship
			//				controlInterface = deFactory.createListBox();
			//				((ListBoxInterface) controlInterface).setIsMultiSelect(true);
			//			}
			//			else
			//			{//Combo box for the rest
			//				controlInterface = deFactory.createComboBox();
			//			}
			//
			//			((SelectControl) controlInterface).setSeparator(",");
			//			addAssociationDisplayAttributes(associationInterface, controlInterface);

		}
		else
		{
			AttributeInterface attributeInterface = (AttributeInterface) abstractAttributeInterface;
			AttributeTypeInformationInterface attributeTypeInformation = attributeInterface
					.getAttributeTypeInformation();
			UserDefinedDEInterface userDefinedDEInterface = (UserDefinedDEInterface) attributeTypeInformation
					.getDataElement();

			if (userDefinedDEInterface != null
					&& userDefinedDEInterface.getPermissibleValueCollection() != null
					&& userDefinedDEInterface.getPermissibleValueCollection().size() > 0)
			{
				controlInterface = deFactory.createListBox();

				// multiselect for permisible values 
				((ListBoxInterface) controlInterface).setIsMultiSelect(true);
				attributeInterface.setIsCollection(new Boolean(true));
				implicitRuleList = configurationsFactory.getAllImplicitRules(ProcessorConstants.LISTBOX_CONTROL,
						attributeInterface.getDataType());	

			}
			else if (attributeTypeInformation instanceof DateAttributeTypeInformation)
			{
				controlInterface = deFactory.createDatePicker();
				implicitRuleList = configurationsFactory.getAllImplicitRules(ProcessorConstants.DATEPICKER_CONTROL,
						attributeInterface.getDataType());
			}
			//Creating check box for boolean attributes
			else if (attributeTypeInformation instanceof BooleanAttributeTypeInformation)
			{
				controlInterface = deFactory.createCheckBox();
				implicitRuleList = configurationsFactory.getAllImplicitRules(ProcessorConstants.CHECKBOX_CONTROL,
						attributeInterface.getDataType());
			}
			else
			{
				controlInterface = deFactory.createTextField();
				((TextFieldInterface) controlInterface).setColumns(0);
				implicitRuleList = configurationsFactory.getAllImplicitRules(ProcessorConstants.TEXT_CONTROL,
						attributeInterface.getDataType());
			}
		}
		controlInterface.setName(abstractAttributeInterface.getName());
		controlInterface.setCaption(abstractAttributeInterface.getName());
		controlInterface.setAbstractAttribute(abstractAttributeInterface);
		
		if (implicitRuleList != null && implicitRuleList.size() > 0)
		{
			for (String validationRule : implicitRuleList)
			{
				RuleInterface rule = instantiateRule(validationRule);
				abstractAttributeInterface.addRule(rule);
			}
		}
		return controlInterface;
	}
	/**
	 * @param validationRule
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private RuleInterface instantiateRule(String validationRule)
			
			throws DynamicExtensionsSystemException
	{
		RuleConfigurationObject ruleConfigurationObject = null;
		RuleInterface rule = null;

		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		ControlConfigurationsFactory configurationsFactory = ControlConfigurationsFactory
				.getInstance();
		Collection<RuleParameterInterface> ruleParameterCollection = new HashSet<RuleParameterInterface>();

		ruleConfigurationObject = configurationsFactory.getRuleObject(validationRule);
//		ruleParameterCollection = getRuleParameterCollection(ruleConfigurationObject,
//				attributeUIBeanInformationIntf);

		rule = domainObjectFactory.createRule();
		rule.setName(ruleConfigurationObject.getRuleName());

		if (ruleParameterCollection != null && !(ruleParameterCollection.isEmpty()))
		{
			rule.setRuleParameterCollection(ruleParameterCollection);
		}

		return rule;
	}

	/**
	 * @param associationInterface
	 * @param controlInterface
	 * In case of linking association, this method adds the association display attributes.
	 */
	private void addAssociationDisplayAttributes(AssociationInterface associationInterface,
			ControlInterface controlInterface)
	{
		EntityInterface targetEntity = associationInterface.getTargetEntity();
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		//		This method returns all attributes and not associations
		Collection<AttributeInterface> targetEntityAttrColl = targetEntity.getAttributeCollection();
		int seqNo = 1;
		for (AttributeInterface attr : targetEntityAttrColl)
		{
			AssociationDisplayAttributeInterface associationDisplayAttribute = domainObjectFactory
					.createAssociationDisplayAttribute();
			associationDisplayAttribute.setSequenceNumber(seqNo);
			associationDisplayAttribute.setAttribute(attr);
			//This method adds to the associationDisplayAttributeCollection
			((SelectControl) controlInterface)
					.addAssociationDisplayAttribute(associationDisplayAttribute);
			seqNo++;
		}
	}

	/**
	 * @param entity
	 * This method removes inherited attributes.
	 */

	protected void removeInheritedAttributes(EntityInterface entity,
			List duplicateAttributeCollection)
	{
		if (duplicateAttributeCollection != null)
		{
			entity.getAbstractAttributeCollection().removeAll(duplicateAttributeCollection);
		}
	}

	/**
	 * @param umlClasses
	 * This method creates all containers.
	 */
	protected void processPersistence() throws Exception
	{
		Collection<ContainerInterface> containerColl = new HashSet<ContainerInterface>();

		Set<String> entityIdKeySet = entityIdVsContainers.keySet();
		for (String entityId : entityIdKeySet)
		{
			List containerList = (ArrayList) entityIdVsContainers.get(entityId);
			ContainerInterface containerInterface = (ContainerInterface) containerList.get(0);
			containerColl.add(containerInterface);			
		}
		EntityManagerInterface entityManagerInterface = EntityManager.getInstance();

		try
		{
			entityManagerInterface.persistEntityGroupWithAllContainers(entityGroup, containerColl);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw new DynamicExtensionsApplicationException(e.getMessage(), e);
		}
		catch (DynamicExtensionsSystemException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @throws Exception
	 */
	protected void postProcessAssociation() throws Exception
	{
		addControlsForAssociation();
	}
}

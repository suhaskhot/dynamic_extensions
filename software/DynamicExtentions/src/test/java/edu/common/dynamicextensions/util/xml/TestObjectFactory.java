
package edu.common.dynamicextensions.util.xml;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import edu.wustl.common.util.logger.Logger;

public class TestObjectFactory extends TestCase
{

	private static final Logger LOGGER = Logger.getCommonLogger(TestObjectFactory.class);
	boolean result = true;

	public void testObjectFactory() throws SAXException
	{
		try
		{
			ObjectFactory objFact = new ObjectFactory();
			ClassType objClassType = objFact.createClassType();
			objClassType.getInstance();
			objClassType.setName(objClassType.getName());
			objFact.createPvVersionXmlCategory();
			objFact.createXmlAttributeType();
			objFact.createPvVersion();
			objFact.createInstanceType();
			objFact.createPropertyType();
			objFact.createPvSetTypeXmlPermissibleValues();
			objFact.createPvSetType();
			objFact.createPropertyTypeOption();
			objFact.createXmlPermissibleValuesXmlEntityGroup();
			QualifierType QualifierObj = objFact.createQualifierType();
			QualifierObj.setConceptDefinitionSource("ConceptDef");
			QualifierObj.setConceptCode("ConceptCode");
			QualifierObj.setConceptPreferredName("conceptPreferredName");
			QualifierObj.setConceptDefinition("conceptDef");
			QualifierObj.setSequenceNumber(123);

			objFact.createAttributeTypeXmlPermissibleValue();
			objFact.createValueType();
			objFact.createXmlPermissibleValues();
			objFact.createXmlClassType();
			objFact.createSourceType();
			objFact.createSourceTypeQualifierDefinition();
			objFact.createAttributeType();
			PrimaryDefinitionType objPrimaryDefinitionType = objFact.createPrimaryDefinitionType();
			objPrimaryDefinitionType.setConceptDefinitionSource("ConceptDefSource");
			objPrimaryDefinitionType.setConceptCode("conceptCode");
			objPrimaryDefinitionType.setConceptPreferredName("conceptPreferedName");
			objPrimaryDefinitionType.setConceptDefinition("ConceptDef");

			objFact.createFormDefinitionFormTag();
			objFact.createFormDefinition();
			objFact.createFormDefinitionForm();
			objFact.createValidatorRulesValidationRuleParam();
			objFact.createValidatorRules();
			objFact.createValidatorRulesValidationRuleErrorKey();
			objFact.createValidatorRulesValidationRule();
			objFact.createControlsControlCommonValidation();
			objFact.createDataTypeClassDataType();
			objFact.createControlsControl();
			objFact.createDataTypeClassDataTypeValidations();
			objFact.createDataTypeClassDataTypeValidationsValidationRule();
			objFact.createControls();
			objFact.createDataTypeClass();
			objFact.createControlsControlCommonValidationCommonValidationRule();

			assertEquals(result, true);
		}
		catch (Exception ex)
		{
			LOGGER.error(ex.getMessage());
			assertEquals(result, false);
			fail();
		}

	}
}

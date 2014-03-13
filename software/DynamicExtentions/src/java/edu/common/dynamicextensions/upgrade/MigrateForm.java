package edu.common.dynamicextensions.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.CategoryEntityRecord;
import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Control.LabelPosition;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DatePicker.DefaultDateType;
import edu.common.dynamicextensions.domain.nui.DisableAction;
import edu.common.dynamicextensions.domain.nui.EnableAction;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.HideAction;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvDataSource.Ordering;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.ShowAction;
import edu.common.dynamicextensions.domain.nui.ShowPvAction;
import edu.common.dynamicextensions.domain.nui.SkipAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.domain.nui.TextField;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.domain.userinterface.SurveyModeLayout;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.FormControlNotesInterface;
import edu.common.dynamicextensions.domaininterface.NumericTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.StringTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.LabelInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.MultiSelectCheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.SelectInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.napi.impl.VersionedContainerImpl;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.nutility.DeleteOnCloseFileInputStream;
import edu.common.dynamicextensions.nutility.IoUtil;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.skiplogic.Condition;
import edu.common.dynamicextensions.skiplogic.ConditionStatements;
import edu.common.dynamicextensions.skiplogic.PrimitiveCondition;
import edu.common.dynamicextensions.skiplogic.RelationalOperator;
import edu.common.dynamicextensions.skiplogic.SkipLogic;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DataValueMapUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.common.dynamicextensions.util.parser.FormulaParser;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author Vinayak Pawar
 *
 */
public class MigrateForm {
	private class FormMigrationCtxt {
		private Container newForm;
		
		private Control sfCtrl;
		
		private Map<BaseAbstractAttributeInterface, Object> fieldMap = 
				new HashMap<BaseAbstractAttributeInterface, Object>();
	}
	
	private class SelectCtrlPvDataSource {
		private SelectControl selectCtrl;
		
		private PvDataSource pvDataSource;
		
		public String toString() {
			return selectCtrl.getName();
		}
	}
	
	private static Logger logger = Logger.getLogger(MigrateForm.class);
	
	private static EntityCache entityCache = null;
	
	private static Map<RelationalOperator, RelationalOp> relationalOpMap = null;
	
	private static Map<String, String> datePatternMap = null;
	
	private boolean verticalCtrlAlignment = false;
	
	private UserContext usrCtx = null;
	
	private Map<Date, List<SelectCtrlPvDataSource>> versionedCtrls = new TreeMap<Date, List<SelectCtrlPvDataSource>>();
	
	private int userDefId = 0;
	
	private static Set<String> containerNames = new HashSet<String>();

	//private Map<Date, >
	
	static {
		entityCache = EntityCache.getInstance();
		entityCache.cacheSkipLogic();
		
		relationalOpMap = new HashMap<RelationalOperator, RelationalOp>();
		relationalOpMap.put(RelationalOperator.EQUALS, RelationalOp.EQ);
		relationalOpMap.put(RelationalOperator.GREATER_THAN, RelationalOp.GT);
		relationalOpMap.put(RelationalOperator.LESS_THAN, RelationalOp.LE);		

		datePatternMap = new HashMap<String, String>();
		datePatternMap.put("DateAndTime", "MM-dd-yyyy HH:mm");
		datePatternMap.put("MonthAndYear", "MM-yyyy");
		datePatternMap.put("YearOnly", "yyyy");		
	}
	
	public MigrateForm(UserContext usrCtx) {
		this.usrCtx = usrCtx;
	}

	public void migrateForm(Long containerId, String formCtxtTbl, Long recEntityId) 
	throws Exception {
		logger.info("Starting to migrate container: " + containerId);
		
		ContainerInterface container = getOldFormDefinition(containerId);
		if (container == null) {
			return;
		}
		
		FormMigrationCtxt formMigrationCtxt = getNewFormDefinition(container);
		if (formMigrationCtxt == null) {
			return;
		}

		Long newFormId = saveForm(formMigrationCtxt.newForm);
		if (newFormId == null) {
			return;
		}
		
		logger.info("Versioned controls for form newFormId " + newFormId + " are : " + versionedCtrls);
		migrateFormData(container, formMigrationCtxt, formCtxtTbl, recEntityId);		
		updateFormContext(container.getId(), newFormId);		
	}

	////////////////////////////////////////////////////////////////////////////////////////
	//
	// Fixing form contexts
	//
	////////////////////////////////////////////////////////////////////////////////////////
	public void updateFormContext(Long oldFormId, Long newFormId) 
	throws Exception {				
		JDBCDAO jdbcDao = null;
		
		try {
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			List<ColumnValueBean> params = new ArrayList<ColumnValueBean>();
			params.add(new ColumnValueBean(newFormId));
			params.add(new ColumnValueBean(oldFormId));
			
			jdbcDao.executeUpdate(UPDATE_FORM_CTXT_SQL, params);
			jdbcDao.commit();
		} finally {
			DynamicExtensionsUtility.closeDAO(jdbcDao);
		}		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//
	// Container/form migration logic
	//
	////////////////////////////////////////////////////////////////////////////////////////
	
	private ContainerInterface getOldFormDefinition(Long containerId) 
	throws Exception {
		HibernateDAO dao = null;		
		ContainerInterface container = null;
		
		try {
			dao = DynamicExtensionsUtility.getHibernateDAO();
			String objectType = edu.common.dynamicextensions.domain.userinterface.Container.class.getName();
			container = (ContainerInterface)dao.retrieveById(objectType, containerId); 
		} catch (Exception e) {
			logger.error("Error obtaining container: " + containerId, e);
		} finally {
			DynamicExtensionsUtility.closeDAO(dao);			
		}
		
		return container;		
	}


	private FormMigrationCtxt getNewFormDefinition(ContainerInterface oldForm) 
	throws Exception {
		if (oldForm.getAbstractEntity() instanceof EntityInterface) {
			logger.warn("Container " + oldForm.getCaption() + " point to entity. Skipping migration!");
			return null;
		}

		FormMigrationCtxt formMigrationCtxt = null;
		CategoryEntityInterface catEntity = (CategoryEntityInterface)oldForm.getAbstractEntity();
		if (catEntity.getCategory().getLayout() instanceof SurveyModeLayout) {
			verticalCtrlAlignment = true;
			formMigrationCtxt = getNewSurveyFormDefinition(oldForm); 
		} else {
			formMigrationCtxt = getNewFormDefinition0(oldForm);
		}
		
		populateSkipRules(oldForm, formMigrationCtxt.newForm, formMigrationCtxt.fieldMap);
		populateCalculatedControls(oldForm, formMigrationCtxt.fieldMap);
		return formMigrationCtxt;
	}
	
	private FormMigrationCtxt getNewFormDefinition0(ContainerInterface oldForm) 
	throws Exception {
		if (oldForm.getAbstractEntity() instanceof EntityInterface) {
			logger.warn("Container " + oldForm.getCaption() + " points to entity. Skipping migration!");
			return null;
		}
				
		CategoryEntityInterface catEntity = (CategoryEntityInterface)oldForm.getAbstractEntity();
		CategoryInterface category = catEntity.getCategory();
		String categoryName = (category != null) ? category.getName() : null;
		
		Container newForm = new Container();
		newForm.setCaption(categoryName != null ? categoryName : oldForm.getCaption());
		newForm.setName(getEntityName(oldForm.getAbstractEntity()));
		
		int seqOffset = 0;
		FormMigrationCtxt formMigrationCtxt = new FormMigrationCtxt();
		formMigrationCtxt.newForm = newForm;
		
		for (ControlInterface oldCtrl : oldForm.getAllControlsUnderSameDisplayLabel()) {
			Control newCtrl = null;
			Object mapCtxt = null;
			
			if (oldCtrl.getHeading() != null) {
				Label heading = getHeading(oldCtrl);
				heading.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
				ensureUniqueUdn(newForm, heading);
				newForm.addControl(getHeading(oldCtrl));
				
				seqOffset++;
			}
			
			if (oldCtrl.getFormNotes() != null) {
				for (Label note : getNotes(oldCtrl)) {
					note.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
					ensureUniqueUdn(newForm, note);
					newForm.addControl(note);
					seqOffset++;
				}				
			}
								
			if (oldCtrl instanceof AbstractContainmentControlInterface) {
				AbstractContainmentControlInterface oldSfCtrl = (AbstractContainmentControlInterface)oldCtrl; 
				FormMigrationCtxt sfCtxt = getNewFormDefinition0(oldSfCtrl.getContainer());				
				
				newCtrl = getNewSubFormControl(oldSfCtrl, sfCtxt.newForm);
				sfCtxt.sfCtrl = newCtrl;
				
				mapCtxt = sfCtxt;
			} else { 
				newCtrl = getNewControl(oldCtrl);
				mapCtxt = newCtrl;
			}
			
			newCtrl.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
			ensureUniqueUdn(newForm, newCtrl);
			newForm.addControl(newCtrl);

			formMigrationCtxt.fieldMap.put(oldCtrl.getBaseAbstractAttribute(), mapCtxt);			
		}
		
		return formMigrationCtxt;
	}

	private FormMigrationCtxt getNewSurveyFormDefinition(ContainerInterface oldForm) 
	throws Exception {
		SurveyModeLayout layout = (SurveyModeLayout)((CategoryEntityInterface)oldForm.getAbstractEntity())
				.getCategory().getLayout();
		
		Container newForm = new Container();
		newForm.setCaption(oldForm.getCaption());
		newForm.setName(getEntityName(oldForm.getAbstractEntity()));
		
		FormMigrationCtxt formMigrationCtxt = new FormMigrationCtxt();
		formMigrationCtxt.newForm = newForm;
		
		int seqOffset = 0, lastSeq = 0;
		for (edu.common.dynamicextensions.domain.userinterface.Page page : layout.getPageCollection()) {
			List<ControlInterface> oldCtrls = new ArrayList<ControlInterface>(page.getControlCollection()); 
			Collections.sort(oldCtrls);
			Collections.reverse(oldCtrls);
			
			for (ControlInterface oldCtrl : oldCtrls) {
				Control newCtrl = null;
				Object mapCtxt = null;

				if (oldCtrl.getHeading() != null) {
					Label heading = getHeading(oldCtrl);
					heading.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
					ensureUniqueUdn(newForm, heading);
					newForm.addControl(getHeading(oldCtrl));
					seqOffset++;
				}
				
				if (oldCtrl.getFormNotes() != null) {
					for (Label note : getNotes(oldCtrl)) {
						note.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
						ensureUniqueUdn(newForm, note);
						newForm.addControl(note);
						seqOffset++;
					}				
				}
				
				if (oldCtrl instanceof AbstractContainmentControlInterface) {
					AbstractContainmentControlInterface oldSfCtrl = (AbstractContainmentControlInterface)oldCtrl; 
					FormMigrationCtxt sfCtxt = getNewFormDefinition0(oldSfCtrl.getContainer());				
					
					newCtrl = getNewSubFormControl(oldSfCtrl, sfCtxt.newForm);
					sfCtxt.sfCtrl = newCtrl;
					
					mapCtxt = sfCtxt;
				} else { 
					newCtrl = getNewControl(oldCtrl);
					mapCtxt = newCtrl;
				}

				newCtrl.setSequenceNumber(oldCtrl.getSequenceNumber() + seqOffset);
				ensureUniqueUdn(newForm, newCtrl);
				newForm.addControl(newCtrl);
				formMigrationCtxt.fieldMap.put(oldCtrl.getBaseAbstractAttribute(), mapCtxt);				
				lastSeq = oldCtrl.getSequenceNumber() + seqOffset;
			}

			PageBreak pageBreak = new PageBreak();
			pageBreak.setName("pgBrk" + (lastSeq + 1));
			pageBreak.setUserDefinedName(pageBreak.getName());
			pageBreak.setSequenceNumber(lastSeq + 1);
			newForm.addControl(pageBreak);
			seqOffset++;
		}
		
		return formMigrationCtxt;
	}
	
	private Control getNewControl(ControlInterface oldCtrl) {
		Control ctrl = null; 

		if (oldCtrl instanceof CheckBoxInterface) {
			ctrl = getNewCheckBox((CheckBoxInterface)oldCtrl);
		} else if (oldCtrl instanceof DatePickerInterface) {
			ctrl = getNewDatePicker((DatePickerInterface)oldCtrl);
		} else if (oldCtrl instanceof LabelInterface) {
			ctrl = getNewLabel((LabelInterface)oldCtrl);
		} else if (oldCtrl instanceof FileUploadInterface) {
			ctrl = getNewFileControl((FileUploadInterface)oldCtrl);
		} else if (oldCtrl instanceof TextAreaInterface) {
			ctrl = getNewTextArea((TextAreaInterface)oldCtrl);
		} else if (oldCtrl instanceof TextFieldInterface) {
			ctrl = getNewTextField((TextFieldInterface)oldCtrl);
		} else if (oldCtrl instanceof RadioButtonInterface) {
			ctrl = getNewRadioButton((RadioButtonInterface)oldCtrl);			
		} else if (oldCtrl instanceof MultiSelectCheckBoxInterface) {
			ctrl = getNewMultiSelectCheckBox((MultiSelectCheckBoxInterface)oldCtrl);
		} else if (oldCtrl instanceof ComboBoxInterface) {
			ctrl = getNewComboBox((ComboBoxInterface)oldCtrl);
		} else if (oldCtrl instanceof ListBoxInterface) {
			ctrl = getNewListBox((ListBoxInterface)oldCtrl);
		} else {
			throw new RuntimeException("Unknown control type: " + oldCtrl);
		}		
		return ctrl;
	}
		
	private SubFormControl getNewSubFormControl(
			AbstractContainmentControlInterface oldCtrl, Container newSubContainer) {		
		SubFormControl sfCtrl = new SubFormControl();
		setCtrlProps(sfCtrl, oldCtrl);
		
		newSubContainer.setName(sfCtrl.getName());
		sfCtrl.setSubContainer(newSubContainer);
		sfCtrl.setNoOfEntries(oldCtrl.isCardinalityOneToMany() ? -1 : 0);
		
		//		
		// TODO: setting of properties - showAddMoreLink and pasteButtonEnabled
		//		
		return sfCtrl;
	}
	
	private Label getHeading(ControlInterface oldCtrl) {
		Label label = new Label();
		label.setName("heading" + oldCtrl.getId());
		label.setUserDefinedName("heading" + oldCtrl.getId());
		label.setCaption(oldCtrl.getHeading());
		label.setHeading(true);
		label.setLabelPosition(verticalCtrlAlignment ? LabelPosition.TOP : LabelPosition.LEFT_SIDE);
		label.setShowLabel(true);
		return label;
	}
	
	private List<Label> getNotes(ControlInterface oldCtrl) {		
		List<FormControlNotesInterface> oldNotes = oldCtrl.getFormNotes();
		
		List<Label> newNotes = new ArrayList<Label>();
		if (oldNotes != null) {
			int i = 0;
			for (FormControlNotesInterface oldNote : oldNotes) {
				Label note = new Label();
				note.setName("note" + oldCtrl.getId() + "_" + i);
				note.setUserDefinedName("note" + oldCtrl.getId() + "_" + i);
				note.setCaption(oldNote.getNote());
				note.setNote(true);
				note.setLabelPosition(verticalCtrlAlignment ? LabelPosition.TOP : LabelPosition.LEFT_SIDE);
				note.setShowLabel(true);
				newNotes.add(note);				
				
				++i;
			}
		}
		
		return newNotes;
	}
	
	private CheckBox getNewCheckBox(CheckBoxInterface oldCheckBox) {
		CheckBox checkBox = new CheckBox();
		setCtrlProps(checkBox, oldCheckBox);
		
		String defValue = getDefaultValue(oldCheckBox);
		boolean isChecked = defValue != null && 
				(defValue.equalsIgnoreCase("true") || defValue.equals("1"));		
		checkBox.setDefaultValueChecked(isChecked); 
		return checkBox;
	}
	
	private DatePicker getNewDatePicker(DatePickerInterface oldCtrl) {
		edu.common.dynamicextensions.domain.userinterface.DatePicker oldDatePicker = 
				(edu.common.dynamicextensions.domain.userinterface.DatePicker) oldCtrl;
		
		DatePicker datePicker = new DatePicker();
		setCtrlProps(datePicker, oldDatePicker);
		
		String dateValueType = oldDatePicker.getDateValueType(); 
		if (dateValueType != null && dateValueType.equals(ProcessorConstants.DATE_VALUE_TODAY)) {
			datePicker.setDefaultDateType(DefaultDateType.CURRENT_DATE);
		}		
				
		datePicker.setShowCalendar(bool(oldDatePicker.getShowCalendar()));
		
		AttributeMetadataInterface attrMetadata = (AttributeMetadataInterface)
				oldDatePicker.getBaseAbstractAttribute();
		AttributeTypeInformationInterface attrTypeInfo = attrMetadata.getAttributeTypeInformation();		
		if (attrTypeInfo instanceof DateTypeInformationInterface) {
			DateTypeInformationInterface dateTypeInfo = (DateTypeInformationInterface)
					attrMetadata.getAttributeTypeInformation();

			String format = null;
			if (dateTypeInfo.getFormat() != null) {
				format = datePatternMap.get(dateTypeInfo.getFormat());
				if (format != null) {
					datePicker.setFormat(format);
				}
			}
		} else {
			logger.error("Date picker control refers to category attribute that is not of date type " +
					"Control name: " + oldCtrl.getCaption() + 
					", attribute id: " + oldCtrl.getBaseAbstractAttribute().getId());
		}
		
		RuleInterface rule = null;
		if ((rule = getRule(oldCtrl, "allowPastAndPresentDateOnly")) != null) {
			datePicker.addValidationRule("allowPastAndPresentDateOnly", null);
		} else if ((rule = getRule(oldCtrl, "allowfuturedate")) != null) {
			datePicker.addValidationRule("allowfuturedate", null);
		}
		
		if ((rule = getRule(oldCtrl, "dateRange")) != null) {
			Collection<RuleParameterInterface> ruleParams = rule.getRuleParameterCollection();
			if (ruleParams != null) {
				Map<String, String> params = new HashMap<String, String>();
				for (RuleParameterInterface ruleParam : ruleParams) {
					params.put(ruleParam.getName(), ruleParam.getValue());
				}
				
				if (!params.isEmpty() && params.containsKey("min") && params.containsKey("max")) {
					datePicker.addValidationRule("dateRange", params);
				}
			}			
		}
		
		return datePicker;
	}
	
	private Label getNewLabel(LabelInterface oldCtrl) {
		edu.common.dynamicextensions.domain.userinterface.Label oldLabel = 
				(edu.common.dynamicextensions.domain.userinterface.Label) oldCtrl;
		
		Label label = new Label();
		setCtrlProps(label, oldLabel);
		return label;
	}
	
	private FileUploadControl getNewFileControl(FileUploadInterface oldCtrl) {
		edu.common.dynamicextensions.domain.userinterface.FileUploadControl oldFileCtrl = 
				(edu.common.dynamicextensions.domain.userinterface.FileUploadControl) oldCtrl;
		
		FileUploadControl fileCtrl = new FileUploadControl();
		setCtrlProps(fileCtrl, oldFileCtrl);
		return fileCtrl;
	}
	
	private TextArea getNewTextArea(TextAreaInterface oldCtrl) {
		edu.common.dynamicextensions.domain.userinterface.TextArea oldTextArea = 
				(edu.common.dynamicextensions.domain.userinterface.TextArea) oldCtrl;
		
		TextArea textArea = new TextArea();
		setCtrlProps(textArea, oldTextArea);		
		
		textArea.setNoOfColumns(oldTextArea.getColumns());
		textArea.setNoOfRows(oldTextArea.getRows());
		textArea.setDefaultValue(getDefaultValue(oldCtrl));
		addCharSetRuleIfPresent(oldCtrl, textArea);
		
		return textArea;
	}
	
	private TextField getNewTextField(TextFieldInterface oldCtrl) {	
		AttributeTypeInformationInterface attrType = getDataType(oldCtrl);
		
		TextField textField = null; 
		if (attrType instanceof NumericTypeInformationInterface) {
			textField = getNewNumberField(oldCtrl);
		} else {
			textField = getNewStringTextField(oldCtrl);
		}
		
		textField.setDefaultValue(getDefaultValue(oldCtrl));
		return textField;
	}
	
	private StringTextField getNewStringTextField(TextFieldInterface oldCtrl) {
		StringTextField textField = new StringTextField();
		setCtrlProps(textField, oldCtrl);
		
		textField.setNoOfColumns(oldCtrl.getColumns());
		textField.setUrl(bool(oldCtrl.getIsUrl()));
		textField.setPassword(bool(oldCtrl.getIsPassword()));
		
		AttributeTypeInformationInterface attrType = getDataType(oldCtrl);
		if (attrType instanceof StringTypeInformationInterface) {
			StringTypeInformationInterface stringType = (StringTypeInformationInterface)attrType;
			Integer maxSize = stringType.getSize();
			if (maxSize != null) {
				textField.setMinLength(0);
				textField.setMaxLength(maxSize);
			}
		}
		
		addCharSetRuleIfPresent(oldCtrl, textField);
		return textField;
	}
	

	private NumberField getNewNumberField(TextFieldInterface oldCtrl) {
		NumberField numberField = new NumberField();
		setCtrlProps(numberField, oldCtrl);

		AttributeTypeInformationInterface typeInfo = getDataType(oldCtrl);
		NumericTypeInformationInterface numInfo = (NumericTypeInformationInterface)typeInfo;
				
		numberField.setNoOfColumns(oldCtrl.getColumns());		
		numberField.setNoOfDigits(numInfo.getDigits() != null ? numInfo.getDigits() : 19); 	
		
		if (numInfo instanceof DoubleTypeInformationInterface || 
				numInfo instanceof FloatTypeInformationInterface) {
			Integer decimalPlaces = numInfo.getDecimalPlaces(); 
			if (decimalPlaces != null && decimalPlaces > 0) {
				numberField.setNoOfDigitsAfterDecimal(decimalPlaces);
			} else {
				numberField.setNoOfDigitsAfterDecimal(5);
			}
		}
		
		numberField.setMeasurementUnits(numInfo.getMeasurementUnits());
		
		RuleInterface rule = getRule(oldCtrl, "range");
		if (rule != null) {
			Collection<RuleParameterInterface> ruleParams = rule.getRuleParameterCollection();
			if (ruleParams != null) {
				for (RuleParameterInterface ruleParam : ruleParams) {
					if (ruleParam.getName().equals("min")) {
						numberField.setMinValue(ruleParam.getValue());
					} else if (ruleParam.getName().equals("max")) {
						numberField.setMaxValue(ruleParam.getValue());
					}
				}
			}
		}
		
		return numberField;
	}
	
	private RadioButton getNewRadioButton(RadioButtonInterface oldCtrl) {
		RadioButton radioButton = new RadioButton();
		setSelectProps(radioButton, oldCtrl);
		radioButton.setOptionsPerRow(getOptionsPerRow(oldCtrl));
		return radioButton;
	}
	
	private MultiSelectCheckBox getNewMultiSelectCheckBox(MultiSelectCheckBoxInterface oldCtrl) {
		MultiSelectCheckBox multiSelectCb = new MultiSelectCheckBox();
		setSelectProps(multiSelectCb, oldCtrl);
		multiSelectCb.setOptionsPerRow(getOptionsPerRow(oldCtrl));
		return multiSelectCb;
	}
	
	private ComboBox getNewComboBox(ComboBoxInterface oldCtrl) {
		ComboBox comboBox = new ComboBox();
		setSelectProps(comboBox, oldCtrl);
		
		comboBox.setLazyPvFetchingEnabled(bool(oldCtrl.getIsLazy()));
		comboBox.setNoOfColumns(oldCtrl.getColumns() == null ? 0 : oldCtrl.getColumns());
		comboBox.setMinQueryChars(3); // TODO: Check whether this is appropriate
		return comboBox;
	}
	
	private ListBox getNewListBox(ListBoxInterface oldCtrl) {
		ListBox listBox = null;
		if (oldCtrl.getIsMultiSelect() != null && oldCtrl.getIsMultiSelect()) {
			listBox = new MultiSelectListBox();
		} else {
			listBox = new ListBox();
		}
		
		setSelectProps(listBox, oldCtrl);
		if (oldCtrl.getNoOfRows() != null) {
			listBox.setNoOfRows(oldCtrl.getNoOfRows() == null ? 0 : oldCtrl.getNoOfRows());
		}		
		listBox.setAutoCompleteDropdownEnabled(bool(oldCtrl.getIsUsingAutoCompleteDropdown()));
		listBox.setMinQueryChars(3);
		return listBox;		
	}
	
	private void setSelectProps(SelectControl newCtrl, SelectInterface oldCtrl) {
		setCtrlProps(newCtrl, oldCtrl);
		newCtrl.setPvDataSource(getPvDataSource(newCtrl, oldCtrl));		
	}
	
	private void setCtrlProps(Control newCtrl, ControlInterface oldCtrl) {
		String ctrlName = getCtrlName(oldCtrl);
		newCtrl.setName(ctrlName.concat(oldCtrl.getId().toString()));
		newCtrl.setUserDefinedName(ctrlName);
		newCtrl.setCaption(oldCtrl.getCaption());
		newCtrl.setCustomLabel(getCustomLabel(oldCtrl));
		newCtrl.setLabelPosition(verticalCtrlAlignment ? LabelPosition.TOP : LabelPosition.LEFT_SIDE);
		newCtrl.setToolTip(oldCtrl.getTooltip());
		newCtrl.setPhi(isPhi(oldCtrl));
		newCtrl.setMandatory(isMandatory(oldCtrl));
		newCtrl.setSequenceNumber(oldCtrl.getSequenceNumber());
		newCtrl.setxPos(oldCtrl.getYPosition());
		newCtrl.setShowLabel(showLabel(oldCtrl));
		newCtrl.setShowInGrid(showInGrid(oldCtrl));
		
		BaseAbstractAttributeInterface categoryAttr = oldCtrl.getBaseAbstractAttribute();
		if (!(categoryAttr instanceof CategoryAttributeInterface)) {
			return;
		}
		
		AbstractAttributeInterface attr = ((CategoryAttributeInterface)categoryAttr).getAbstractAttribute();
		if (attr != null) {
			Collection<SemanticPropertyInterface> semanticProps = attr.getSemanticPropertyCollection();
			if (semanticProps != null && !semanticProps.isEmpty()) {
				SemanticPropertyInterface sp = semanticProps.iterator().next();
				newCtrl.setConceptCode(sp.getConceptCode());
				newCtrl.setConceptDefinition(sp.getConceptDefinition());
				newCtrl.setConceptDefinitionSource(sp.getConceptDefinitionSource());
				newCtrl.setConceptPreferredName(sp.getConceptPreferredName());
				
				logger.info("Migrated concept code of " + attr.getId());
			}
		}
	}
	
	private void ensureUniqueUdn(Container c, Control ctrl) {
		String useDefName = ctrl.getUserDefinedName();
		if (c.getUserDefCtrlNames().contains(useDefName)) {
			ctrl.setUserDefinedName(useDefName + (++userDefId));
		}
	}

	private String getCtrlName(ControlInterface ctrl) {
		String name = "";
		if (ctrl instanceof LabelInterface) {
			name = new StringBuilder("label").append(ctrl.getId()).toString();
		} else if (ctrl instanceof AbstractContainmentControlInterface) {
			name = getAssociationName(ctrl.getBaseAbstractAttribute());			
		} else {
			name = ctrl.getBaseAbstractAttribute().getName();
			int idx = name.lastIndexOf(" Category Attribute");
			if (idx != -1) {
				name = name.substring(0, idx);
			}
		}
 		
		return StringUtils.deleteWhitespace(name).replaceAll("\\.", "_");
	}
	
	private String getCustomLabel(ControlInterface ctrl) {
		String customLabel = null;		
		BaseAbstractAttributeInterface attr = ctrl.getBaseAbstractAttribute();
		
		if (attr != null) {
			customLabel = attr.getTaggedValue("displaylabel");
		}
		
		return customLabel;
	}
	
	private boolean isPhi(ControlInterface ctrl) {
		BaseAbstractAttributeInterface battr = ctrl.getBaseAbstractAttribute();
		if (!(battr instanceof CategoryAttributeInterface)) {
			return false;
		}
		
		boolean isPhi = false;
		CategoryAttributeInterface cattr = (CategoryAttributeInterface)battr;
		if (cattr instanceof AttributeInterface) {
			AttributeInterface attr = (AttributeInterface)cattr.getAbstractAttribute();
			isPhi = bool(attr.getIsIdentified());
		} else if (cattr instanceof AssociationInterface) {
			AssociationInterface assoc = (AssociationInterface)cattr.getAbstractAttribute();
			for (AbstractAttributeInterface attr1 : assoc.getTargetEntity().getAllAbstractAttributes()) {
				if (attr1.getName().equals("id")) {
					continue;
				}
					
				AttributeInterface attr = (AttributeInterface)cattr.getAbstractAttribute();
				isPhi = bool(attr.getIsIdentified());
			}				
		}
		
		return isPhi;
	}
	
	private boolean isMandatory(ControlInterface ctrl) {
		RuleInterface rule = getRule(ctrl, "required");
		return rule != null;
	}
	
	private boolean showLabel(ControlInterface ctrl) {
		Boolean showLabel = ((edu.common.dynamicextensions.domain.userinterface.Control)ctrl)
				.getShowLabel();
		return showLabel != null ? showLabel : true;
	}

	private boolean showInGrid(ControlInterface ctrl) {
		boolean showInGrid = false;
		
		BaseAbstractAttributeInterface attr = ctrl.getBaseAbstractAttribute();		
		if (attr != null) {
			String tagValue = attr.getTaggedValue("showingrid");
			showInGrid = (tagValue != null && tagValue.equalsIgnoreCase("true"));			
		}
		
		return showInGrid;
	}
	
	private String getDefaultValue(ControlInterface ctrl) {
		BaseAbstractAttributeInterface bAttr = (BaseAbstractAttributeInterface)ctrl.getBaseAbstractAttribute();
		String defaultValue = null;
		
		if (bAttr instanceof CategoryAttributeInterface) {
			CategoryAttributeInterface cAttr = (CategoryAttributeInterface)bAttr;
			PermissibleValueInterface defPv = cAttr.getDefaultValuePermissibleValue();
			if (defPv != null) {
				defaultValue = defPv.getValueAsObject().toString();
			}			
		}
		
		if (defaultValue == null) {
			defaultValue = getDataType(ctrl).getDefaultValueAsString();
		}
		
		return defaultValue;
	}
	
	private AttributeTypeInformationInterface getDataType(ControlInterface ctrl) {
		BaseAbstractAttributeInterface baseAttr = ctrl.getBaseAbstractAttribute();
		AttributeInterface attr = null;
		
		if (baseAttr instanceof CategoryAttributeInterface) {
			CategoryAttributeInterface catAttr = (CategoryAttributeInterface)baseAttr;
			
			if (catAttr.getAbstractAttribute() instanceof AttributeInterface) {
				attr = (AttributeInterface)catAttr.getAbstractAttribute();
			} else if (catAttr.getAbstractAttribute() instanceof AssociationInterface) { // case of multi-select control
				AssociationInterface association = (AssociationInterface)catAttr.getAbstractAttribute();
				for (AbstractAttributeInterface attr1 : association.getTargetEntity().getAllAbstractAttributes()) {
					if (attr1.getName().equals("id")) {
						continue;
					}
					
					attr = (AttributeInterface)attr1;
					break;
				}
			}			
		} else if (baseAttr instanceof AttributeInterface) {
			attr = (AttributeInterface)baseAttr;
		} else {
			throw new RuntimeException(ctrl.getCaption() + " is neither category attribute nor simple attribute");
		}
		
		return attr.getAttributeTypeInformation();
	}
	
	private PvDataSource getPvDataSource(SelectControl newSelectCtrl, SelectInterface selectCtrl) {
		AttributeTypeInformationInterface attrInfo = getDataType(selectCtrl);

		PvDataSource result = null;		
		
		DataType dataType = DataType.STRING;
		String dateFormat = null;		
		if (attrInfo instanceof BooleanTypeInformationInterface) {
			dataType = DataType.BOOLEAN;
		} else if (attrInfo instanceof DateTypeInformationInterface) {
			DateTypeInformationInterface dateType = (DateTypeInformationInterface)attrInfo;
			dataType = DataType.DATE;
			dateFormat = dateType.getFormat();
		} else if (attrInfo instanceof DoubleTypeInformationInterface || 
				attrInfo instanceof FloatTypeInformationInterface) {
			dataType = DataType.FLOAT;
		} else if (attrInfo instanceof NumericTypeInformationInterface) {
			dataType = DataType.INTEGER;
		}

		//pvDataSource.setDataType(dataType);
		//pvDataSource.setDateFormat(dateFormat);		
		for (UserDefinedDEInterface userDataElement : getUserDataElements(selectCtrl)) {			
			List<PermissibleValue> pvs = getPvs(userDataElement);
				
			PvVersion pvVersion = new PvVersion();
			pvVersion.setActivationDate(userDataElement.getActivationDate());
			pvVersion.setPermissibleValues(pvs);
				
			Collection<PermissibleValueInterface> defPvs = userDataElement.getDefaultPermissibleValues();
			if (defPvs != null && defPvs.size() > 0) {				
				pvVersion.setDefaultValue(getPv(defPvs.iterator().next()));								
			}
			
			PvDataSource pvDataSource = new PvDataSource();
			pvDataSource.setDataType(dataType);
			pvDataSource.setDateFormat(dateFormat);
			pvDataSource.getPvVersions().add(pvVersion);
			
			if (bool(userDataElement.getIsOrdered())) {
				String ordering = userDataElement.getOrder();
				if (ordering != null && ordering.equalsIgnoreCase("descending")) {
					pvDataSource.setOrdering(Ordering.DESC);
				} else {
					pvDataSource.setOrdering(Ordering.ASC);
				}
			}
			
			Date versionDate = getEpochDate();
			if (pvVersion.getActivationDate() != null) {
				versionDate = timeTrimmedDate(pvVersion.getActivationDate());
			}
			
			List<SelectCtrlPvDataSource> selectCtrls = versionedCtrls.get(versionDate);
			if (selectCtrls == null) {		
				selectCtrls = new ArrayList<SelectCtrlPvDataSource>();
				versionedCtrls.put(versionDate, selectCtrls);
			}
				
			SelectCtrlPvDataSource pvSource = new SelectCtrlPvDataSource();
			pvSource.selectCtrl   = newSelectCtrl;
			pvSource.pvDataSource = pvDataSource;
			selectCtrls.add(pvSource);

			result = pvDataSource;
		}

		if (result == null && ControlsUtility.isSQLPv(selectCtrl.getAttibuteMetadataInterface())) {
			String sql = selectCtrl.getAttibuteMetadataInterface().getTaggedValue(DEConstants.PV_PROCESSOR);
			result = new PvDataSource();
			result.setDataType(dataType);
			result.setDateFormat(dateFormat);
			result.setSql(sql);			
		}
				
		return result;
	}
	
	private List<UserDefinedDEInterface> getUserDataElements(SelectInterface selectCtrl) {
		CategoryAttributeInterface cattr = (CategoryAttributeInterface)selectCtrl.getBaseAbstractAttribute();
		List<UserDefinedDEInterface> userDataElements = new ArrayList<UserDefinedDEInterface>();
		for (DataElementInterface dataElement : cattr.getDataElementCollection()) {			
			if (!(dataElement instanceof UserDefinedDEInterface)) {
				continue;
			}
			
			userDataElements.add((UserDefinedDEInterface)dataElement);
		}
		
		Collections.sort(userDataElements, new Comparator<UserDefinedDEInterface>() {
			@Override
			public int compare(UserDefinedDEInterface arg0,	UserDefinedDEInterface arg1) {
				if (arg0.getActivationDate() == null && arg1.getActivationDate() == null) {
					return 0;
				} else if (arg0.getActivationDate() == null && arg1.getActivationDate() != null) {
					return -1;
				} else if (arg0.getActivationDate() != null && arg1.getActivationDate() == null) {
					return 1;
				} else if (arg0.getActivationDate().equals(arg1.getActivationDate())) {
					return 0;
				} else if (arg0.getActivationDate().before(arg1.getActivationDate())) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		
		return userDataElements;
		
	}
	
	private List<PermissibleValue> getPvs(UserDefinedDEInterface userDataElement) {
		List<PermissibleValue> result = new ArrayList<PermissibleValue>();
		
		Collection<PermissibleValueInterface> oldPvs = userDataElement.getPermissibleValues();
		for (PermissibleValueInterface oldPv : oldPvs) {
			PermissibleValue newPv = getPv(oldPv);
			result.add(newPv);
		}
					
		return result;	
	}
	
	private PermissibleValue getPv(PermissibleValueInterface oldPv) {		
		PermissibleValue newPv = new PermissibleValue();
		newPv.setNumericCode(oldPv.getNumericCode());
		newPv.setValue(oldPv.getValueAsObject().toString());
		newPv.setOptionName(oldPv.getValueAsObject().toString());
		
		Collection<SemanticPropertyInterface> semanticProps = oldPv.getSemanticPropertyCollection();
		if (semanticProps != null) {
			Iterator<SemanticPropertyInterface> iterator = semanticProps.iterator();
			if (iterator.hasNext()) {
				SemanticPropertyInterface semanticProp = iterator.next();
				newPv.setConceptCode(semanticProp.getConceptCode());
				newPv.setDefinitionSource(semanticProp.getConceptDefinitionSource());
			}
		}
		
		return newPv;		
	}
	
	private Date timeTrimmedDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	private String getEntityName(AbstractEntityInterface entity) {
		String specialChars = "[+-/*(){}%. ]";
		String name = entity.getName();
		if (entity instanceof CategoryEntityInterface) {
			CategoryInterface category = ((CategoryEntityInterface) entity).getCategory();
			
			if (category != null) {
				name = getUniqueFormName(category.getName());
			} 
		}
		
		return StringUtils.deleteWhitespace(name).replaceAll(specialChars, "_");
	}
	
	private String getAssociationName(BaseAbstractAttributeInterface attr) {
		String name = attr.getName();
		if (attr instanceof CategoryAssociationInterface) {
			name = getLastPart(attr.getName(), 3);
		}
		
		return name;
	}
	

	private String getUniqueFormName(String name) {
		String specialChars = "[+-/*(){}%. ]";
		String uniqueName = StringUtils.deleteWhitespace(name).replaceAll(specialChars, "_");
		int i = 0;
		while (containerNames.contains(uniqueName)) {                 
			uniqueName = name + "_" + ++i;
		}
		containerNames.add(uniqueName);
		return uniqueName;
    }
	
	private String getLastPart(String name, int startIdx) {
		String[] nameParts = name.split("[\\[\\]]");
		int numParts = nameParts.length;			
		return new StringBuilder(nameParts[numParts - startIdx])
			.append(nameParts[numParts - (startIdx - 1)]).toString();		
	}
	
	private RuleInterface getRule(ControlInterface ctrl, String ruleName) {
		BaseAbstractAttributeInterface battr = ctrl.getBaseAbstractAttribute();
		if (!(battr instanceof CategoryAttributeInterface)) {
			return null;
		}
		
		AttributeMetadataInterface attr = (AttributeMetadataInterface)battr;
		Collection<RuleInterface> rules = attr.getRuleCollection();		
		RuleInterface rule = getRule(rules, ruleName);
		if (rule == null) {
			CategoryAttributeInterface cattr = (CategoryAttributeInterface)attr;
			rules = cattr.getAbstractAttribute().getRuleCollection();
			rule = getRule(rules, ruleName);			
		}
		
		return rule;
	}
	
	private RuleInterface getRule(Collection<RuleInterface> rules, String ruleName) {
		RuleInterface result = null;
		
		for (RuleInterface rule : rules) {
			if (rule.getName().equals(ruleName)) {
				result = rule;
				break;
			}
		}
		
		return result;
	}
	
	private void addCharSetRuleIfPresent(ControlInterface oldCtrl, Control newCtrl) {
		RuleInterface rule = getRule(oldCtrl, "characterSet");
		if (rule != null) {
			newCtrl.addValidationRule("characterSet", null);
		}		
	}
	
	private int getOptionsPerRow(ControlInterface oldCtrl) {
		BaseAbstractAttributeInterface battr = null;
		if (oldCtrl != null) {
			battr = oldCtrl.getBaseAbstractAttribute();
		}
		
		String column = null;
		if (battr != null) {
			column = battr.getTaggedValue("column");
		}
		
		int optionsPerRow = 3;
		try {
			if (column != null) {
				optionsPerRow = Integer.parseInt(column);
			}				
		} catch (Exception e) {
			optionsPerRow = 3;
			logger.info("Parsing of string to integer failed for optionsPerRow", e);
		}
		
		return optionsPerRow;
	}
		
	/////////////////////////////////////////////////////////////////////////////////////////
	//
	// Skip logic migration
	//
	/////////////////////////////////////////////////////////////////////////////////////////
	private void populateSkipRules(
			ContainerInterface oldContainer, Container newContainer,
			Map<BaseAbstractAttributeInterface, Object> fieldMap)
	throws Exception {		
		List<ContainerInterface> containers = getAllChildAndSubContainers(oldContainer);
		containers.add(0, oldContainer);
		
		for (ContainerInterface container : containers) {
			populateContainerSkipRules(container, newContainer, fieldMap);
		}
	}
	
	private void populateContainerSkipRules(
			ContainerInterface oldContainer, Container newContainer,
			Map<BaseAbstractAttributeInterface, Object> fieldMap)
	throws Exception {
		
		SkipLogic skipLogic = entityCache.getSkipLogicByContainerIdentifier(oldContainer.getId());
		if (skipLogic == null) {
			logger.info("No skip logic for old container: " + oldContainer.getId());
			return;
		}
		
		logger.info("Migrating skip logic for old container: " + oldContainer.getId());
		for (ConditionStatements condStmts : skipLogic.getListOfconditionStatements()) {
			for (Condition condition : condStmts.getListOfConditions()) {
				if (!(condition instanceof PrimitiveCondition)) {
					logger.warn("Enountered a non-primitive skip logic condition: " + condition);
					continue;
				}
				
				PrimitiveCondition primitiveCond = (PrimitiveCondition)condition;
				BaseAbstractAttributeInterface sourceAttr = primitiveCond.getCategoryAttribute();
				RelationalOperator relationalOp = primitiveCond.getRelationalOperator();
				Object value = primitiveCond.getValue();
					
				SkipCondition skipCondition = 
						getSkipCondition(fieldMap, sourceAttr, relationalOp, value);
					
				edu.common.dynamicextensions.skiplogic.Action action = primitiveCond.getAction();					
				BaseAbstractAttributeInterface targetAttr = action.getControl().getBaseAbstractAttribute(); 
					
				SkipAction skipAction = getSkipAction(action);
				skipAction.setTargetCtrl(getControl(fieldMap, targetAttr));
					
				SkipRule skipRule = new SkipRule();
				skipRule.setLogicalOp(LogicalOp.AND);
				skipRule.getConditions().add(skipCondition);
				skipRule.getActions().add(skipAction);
				
				newContainer.addSkipRule(skipRule);
			}
		}
	}
	
	private SkipCondition getSkipCondition(
			Map<BaseAbstractAttributeInterface, Object> fieldMap, 
			BaseAbstractAttributeInterface sourceAttr, RelationalOperator relOp, Object value) {
		Control sourceCtrl = getControl(fieldMap, sourceAttr);
		
		if (sourceCtrl == null) {
			throw new RuntimeException(
					"Couldn't find source control for category attr: " + sourceAttr + 
					" Attr-control map is: " + fieldMap);
		}
				
		SkipCondition skipCondition = new SkipCondition();
		skipCondition.setSourceControl(sourceCtrl);
		skipCondition.setValue(value.toString());
		skipCondition.setRelationalOp(relationalOpMap.get(relOp));
		sourceCtrl.setSkipLogicSourceControl(true);
		
		return skipCondition;
	}
	
	private SkipAction getSkipAction(edu.common.dynamicextensions.skiplogic.Action oldAction) {
		SkipAction newAction = null;
		
		if (oldAction instanceof edu.common.dynamicextensions.skiplogic.DisableAction) {
			newAction = new DisableAction();
		} else if (oldAction instanceof edu.common.dynamicextensions.skiplogic.EnableAction) {
			newAction = new EnableAction();
		} else if (oldAction instanceof edu.common.dynamicextensions.skiplogic.HideAction) {
			newAction = new HideAction();
		} else if (oldAction instanceof edu.common.dynamicextensions.skiplogic.ShowAction) {
			newAction = new ShowAction();
		} else if (oldAction instanceof edu.common.dynamicextensions.skiplogic.PermissibleValueAction) {			
			newAction = getShowPvAction(
					(edu.common.dynamicextensions.skiplogic.PermissibleValueAction)oldAction);
		} else {
			throw new RuntimeException("Unknown skip action: " + oldAction);
		}
		
		return newAction;
	}
	
	private SkipAction getShowPvAction(edu.common.dynamicextensions.skiplogic.PermissibleValueAction oldPvAction) {
		ShowPvAction newPvAction = new ShowPvAction();
		
		if (oldPvAction.getDefaultValue() != null) {
			newPvAction.setDefaultPv(getPv(oldPvAction.getDefaultValue()));
		}
		
		if (oldPvAction.getListOfPermissibleValues() != null) {
			for (PermissibleValueInterface oldPv : oldPvAction.getListOfPermissibleValues()) {
				newPvAction.getListOfPvs().add(getPv(oldPv));
			}			
		}
		
		return newPvAction;
	}
	
	private Control getControl(
			Map<BaseAbstractAttributeInterface, Object> fieldMap, 
			BaseAbstractAttributeInterface sourceAttr) {
		
		Object ctxt = fieldMap.get(sourceAttr);
		Control ctrl = null;
		
		if (ctxt instanceof Control) {
			ctrl = (Control)ctxt;
		} else if (ctxt instanceof FormMigrationCtxt) {
			ctrl = ((FormMigrationCtxt)ctxt).sfCtrl;
		} else {
			for (Map.Entry<BaseAbstractAttributeInterface, Object> field : fieldMap.entrySet()) {
				if (!(field.getKey() instanceof CategoryAssociationInterface)) {
					continue;
				}
				
				FormMigrationCtxt sfCtxt = (FormMigrationCtxt)field.getValue();
				ctrl = getControl(sfCtxt.fieldMap, sourceAttr);
				if (ctrl != null) {
					break;
				}									
			}
		}
		
		return ctrl;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//
	// Calculated attributes migration logic
	//
	/////////////////////////////////////////////////////////////////////////////////////////
	private void populateCalculatedControls(
			ContainerInterface oldContainer, 
			Map<BaseAbstractAttributeInterface, Object> fieldMap) 
	throws Exception {
		logger.info("Populating formulas for calculated controls. Old container id: " + oldContainer.getId());
		populateCalculatedControls(oldContainer, fieldMap, fieldMap);
	}
	
	private void populateCalculatedControls(
			ContainerInterface oldContainer, 
			Map<BaseAbstractAttributeInterface, Object> fieldMap,
			Map<BaseAbstractAttributeInterface, Object> currFormFieldMap)
	throws Exception {		
		for (ControlInterface oldCtrl : oldContainer.getAllControlsUnderSameDisplayLabel()) {
			if (oldCtrl instanceof AbstractContainmentControlInterface) {
				FormMigrationCtxt sfMigrationCtxt = (FormMigrationCtxt)currFormFieldMap.get(oldCtrl.getBaseAbstractAttribute());				
				AbstractContainmentControlInterface oldSf = (AbstractContainmentControlInterface)oldCtrl;				
				populateCalculatedControls(oldSf.getContainer(), fieldMap, sfMigrationCtxt.fieldMap);
			} else if (oldCtrl.getIsCalculated() != null && oldCtrl.getIsCalculated()) {
				CategoryAttributeInterface attr = 
						(CategoryAttributeInterface)oldCtrl.getBaseAbstractAttribute();
				NumberField newCtrl = (NumberField)currFormFieldMap.get(attr);
				
				String formula = createNewFormula(attr.getFormula().getExpression(), fieldMap);				
				newCtrl.setFormula(formula);
				newCtrl.setCalculated(true);								
			}
		}		
	}
	
	private String createNewFormula(
			String oldFormula, 
			Map<BaseAbstractAttributeInterface, Object> fieldMap) 
	throws Exception {
				
		FormulaParser formulaParser = new FormulaParser();
		formulaParser.parseExpression(oldFormula);
			
		// optimize for multiple occurrences of operand
		String newFormula = oldFormula;		
		for (String operand : formulaParser.getSymbols()) {
			StringBuilder newOperand = new StringBuilder();
			Control ctrl = getControl(newOperand, operand, fieldMap);
			
			newFormula = newFormula.replaceAll(operand, newOperand.toString());
			ctrl.setCalculatedSourceControl(true);
		}
		
		return newFormula;
	}
	
	private Control getControl(
			StringBuilder name, String operand, 
			Map<BaseAbstractAttributeInterface, Object> fieldMap) {
		
		String[] operandParts = operand.split("_");
		return getControl(name, operandParts[0], operandParts[2], fieldMap);
	}
	
	private Control getControl(
			StringBuilder name, String entity, String attrName,
			Map<BaseAbstractAttributeInterface, Object> fieldMap) {
								
		for (Map.Entry<BaseAbstractAttributeInterface, Object> field : fieldMap.entrySet()) {
			if (field.getKey() instanceof CategoryAttributeInterface) {
				CategoryAttributeInterface catAttr = (CategoryAttributeInterface)field.getKey();
				if (isMatching(catAttr, entity, attrName)) {					
					Control ctrl = (Control)field.getValue();
					name.append(ctrl.getName());
					return ctrl;
				}
			}
		}
		
		for (Map.Entry<BaseAbstractAttributeInterface, Object> field : fieldMap.entrySet()) {
			if (field.getKey() instanceof CategoryAssociationInterface) {				
				FormMigrationCtxt sfCtxt = (FormMigrationCtxt)field.getValue();				
				
				StringBuilder name1 = new StringBuilder();
				Control ctrl = getControl(name1, entity, attrName, sfCtxt.fieldMap);
				if (ctrl != null) {
					name.append(sfCtxt.sfCtrl.getName()).append(".").append(name1);
					return ctrl;
				}
			}
		}
		
		return null;
	}
	
	private boolean isMatching(CategoryAttributeInterface catAttr, String entity, String attrName) {
		return catAttr.getCategoryEntity().getEntity().getName().equals(entity) &&
				catAttr.getAbstractAttribute().getName().equals(attrName);				
	}
		
	private Long saveForm(Container container) 
	throws Exception {
		JDBCDAO dao = null;
		JdbcDao jdbcDao = null;
		Long formId = null;		
		VersionedContainerImpl versionedForm = new VersionedContainerImpl();
		
		try {
			dao = DynamicExtensionsUtility.getJDBCDAO(null);
			jdbcDao = new JdbcDao(dao);
			versionedForm.setJdbcDao(jdbcDao);
			
			if (versionedCtrls.isEmpty()) {
				versionedCtrls.put(getEpochDate(), Collections.<SelectCtrlPvDataSource>emptyList());
			}
			
			Long draftContainerId = null;
			for (Map.Entry<Date, List<SelectCtrlPvDataSource>> selectCtrls : versionedCtrls.entrySet()) {
				for (SelectCtrlPvDataSource selectCtrl : selectCtrls.getValue()) {
					setControlPv(container, selectCtrl);
				}
				
				if (draftContainerId == null) {
					draftContainerId = container.save(usrCtx, jdbcDao);
					formId = versionedForm.saveAsDraft(jdbcDao, usrCtx, draftContainerId);
				} else {
					container.save(usrCtx, jdbcDao);
				}
				
				versionedForm.publishProspective(jdbcDao, usrCtx, formId, selectCtrls.getKey());				
			}
			
			dao.commit();
			jdbcDao.close();
		} catch (Exception e) {
			logger.error("Error saving container", e);
		} finally {
			DynamicExtensionsUtility.closeDAO(dao);
		}
		
		return formId;		
	}
	
	private Date getEpochDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0L);
		return cal.getTime();
	}
	
	private void setControlPv(Container container, SelectCtrlPvDataSource selectPvDs) {
		for (Control ctrl : container.getAllControls()) {
			if (selectPvDs.selectCtrl == ctrl) {
				SelectControl selectCtrl = (SelectControl)ctrl;
				selectCtrl.setPvDataSource(selectPvDs.pvDataSource);
				break;
			}
		}
	}
	
	private List<ContainerInterface> getAllChildAndSubContainers(ContainerInterface container) {
		List<ContainerInterface> result = new ArrayList<ContainerInterface>();		
		List<ContainerInterface> current = new ArrayList<ContainerInterface>();
		current.add(container);
				
		while (!current.isEmpty()) {
			container = current.remove(0);
			
			current.addAll(container.getChildContainerCollection());
			result.addAll(container.getChildContainerCollection());
			
			for (ControlInterface oldCtrl : container.getAllControlsUnderSameDisplayLabel()) {
				if (oldCtrl instanceof AbstractContainmentControlInterface) {			
					AbstractContainmentControlInterface oldSf = (AbstractContainmentControlInterface)oldCtrl;
					current.add(oldSf.getContainer());					
					result.add(oldSf.getContainer());
				}
			}			
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//
	// Form data migration logic
	//
	/////////////////////////////////////////////////////////////////////////////////////////
	private void migrateFormData(
			ContainerInterface oldContainer, 
			FormMigrationCtxt formMigrationCtxt, 
			String formCtxTable, Long recEntityId) // CATISSUE_BASE_FORM_CONTEXT 
	throws Exception{
		long t1 = System.currentTimeMillis();
		List<List<Object>> recordIdRows = getRecordIdRows(oldContainer.getId(), formCtxTable);
		logger.info("Number of records to migrate for container: " + 
				oldContainer.getId() + ": " + recordIdRows.size());
				
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();		
		Iterator<List<Object>> recordIdRowIter = recordIdRows.iterator();
		while (recordIdRowIter.hasNext()) {			
			List<Object> recordIdRow = recordIdRowIter.next();
			Long oldRecordId = Long.parseLong((String)recordIdRow.get(0));
						
			try {
				List<Map<BaseAbstractAttributeInterface, Object>> records = 
						categoryManager.getRecordByRecordEntryId(
								oldContainer, Collections.singletonList(oldRecordId), recEntityId);
				
				Map<BaseAbstractAttributeInterface, Object> record = records.get(0);
				DataValueMapUtility.updateDataValueMapDataLoading(record, oldContainer);
				migrateFormData(formMigrationCtxt, oldRecordId, oldContainer, records.get(0));				
			} catch (DynamicExtensionsSystemException e) {
				if (!e.getMessage().startsWith("Exception in execution query :: Unhooked data present in database for recordEntryId")) {
					throw e;
				}
				
				logger.warn(e.getMessage());
			}
		}
		
		logger.info("Migrated records for container: " + oldContainer.getId() + 
				", number of records = " + recordIdRows.size() + 
				", time = " + (System.currentTimeMillis() - t1) / 1000 + " seconds"); 
	}
	
	private List<List<Object>> getRecordIdRows(Long oldContainerId, String formCtxTable) {										
		JDBCDAO jdbcDao = null;
		try {
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			String recordIdQuery = String.format(GET_RECORD_IDS_SQL, formCtxTable, oldContainerId);
			List<List<Object>> recordIdRows = jdbcDao.executeQuery(recordIdQuery, 0, null);
			return recordIdRows;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining record ids for old container: " + oldContainerId);
		} finally {			
			DynamicExtensionsUtility.closeDAO(jdbcDao);
		}
	}
	
	
	private void migrateFormData(
			FormMigrationCtxt formMigrationCtxt, Long oldRecordId,
			ContainerInterface oldForm,
			Map<BaseAbstractAttributeInterface, Object> oldRecord) 
	throws Exception {
		JDBCDAO dao = null;
		
		try {			
			dao = DynamicExtensionsUtility.getJDBCDAO();
			JdbcDao jdbcDao = new JdbcDao(dao);
			
			FormData newRecord = getFormData(jdbcDao, formMigrationCtxt, oldForm, oldRecord);
			FormDataManager formDataMgr = new FormDataManagerImpl(false);
			Long newRecordId = formDataMgr.saveOrUpdateFormData(null, newRecord, jdbcDao);

			List<Long> params = new ArrayList<Long>();
			params.add(newRecordId);
			params.add(oldRecordId);			
			jdbcDao.executeUpdate(UPDATE_RECORD_ID, params);
			
			dao.commit();
			jdbcDao.close();
		} finally {
			DynamicExtensionsUtility.closeDAO(dao);
		}
	}
	
	
	private FormData getFormData(
			JdbcDao jdbcDao,
			FormMigrationCtxt formMigrationCtxt,
			ContainerInterface oldForm,
			Map<BaseAbstractAttributeInterface, Object> oldFormData) {
				
		Container newForm = formMigrationCtxt.newForm;
		Map<BaseAbstractAttributeInterface, Object> fieldMap = formMigrationCtxt.fieldMap;
		
		FormData formData = new FormData(newForm);		
		for (ControlInterface oldCtrl : oldForm.getAllControlsUnderSameDisplayLabel()) {
			BaseAbstractAttributeInterface oldAttr = oldCtrl.getBaseAbstractAttribute();

			Object newValue = null;
			if (oldAttr instanceof CategoryAttributeInterface) {
				Control newControl = (Control)fieldMap.get(oldAttr);
				Object oldValue = oldFormData.get(oldAttr);
				
				if (oldValue instanceof List) {
					newValue = getMultiSelectValues((List<Map<BaseAbstractAttributeInterface, Object>>)oldValue); 
				} else if (oldCtrl instanceof FileUploadInterface){
					newValue = getNewFileControlValue(jdbcDao, oldFormData, (CategoryAttributeInterface)oldAttr);
				} else {
					newValue = oldValue;
				}

				ControlValue cv = new ControlValue(newControl, newValue);
				formData.addFieldValue(cv);				
			} else if (oldAttr instanceof CategoryAssociationInterface) {
				FormMigrationCtxt sfMigrationCtxt = (FormMigrationCtxt)fieldMap.get(oldAttr);				
				Control newSfCtrl = sfMigrationCtxt.sfCtrl;
				
				List<Map<BaseAbstractAttributeInterface, Object>> oldSfRecs = (List) oldFormData.get(oldAttr);
				List<FormData> newSfData = new ArrayList<FormData>();
				
				AbstractContainmentControlInterface oldSfCtrl = (AbstractContainmentControlInterface)oldCtrl;
				for (Map<BaseAbstractAttributeInterface, Object> oldSfRec : oldSfRecs) {
					newSfData.add(getFormData(jdbcDao, sfMigrationCtxt, oldSfCtrl.getContainer(), oldSfRec));
				}
				
				ControlValue cv = new ControlValue(newSfCtrl, newSfData);
				formData.addFieldValue(cv);
			}
		}
		
		return formData;
	}
	
	private FileControlValue getNewFileControlValue(
			JdbcDao jdbcDao, 
			Map<BaseAbstractAttributeInterface, Object> oldFormData, 
			CategoryAttributeInterface catAttr) {
		
		CategoryEntityInterface catEntity = catAttr.getCategoryEntity();
		CategoryEntityRecord catEntityRecord = new CategoryEntityRecord(catEntity.getId(), catEntity.getName());
		
		Long recordId = (Long)oldFormData.get(catEntityRecord);
		if (recordId == null) {
			logger.error("Could not obtain record id in " + oldFormData);
			logger.error("Key used was: " + catEntity.getId() + ":" + catEntity.getName());
			return null;
		}
		
		AttributeInterface attr = (AttributeInterface)catAttr.getAbstractAttribute();
		String tableName = attr.getEntity().getTableProperties().getName();
		String columnName = attr.getColumnProperties().getName();
		
		ResultSet rs = null;
		FileControlValue fcv = null;
		File blobFile = null;
		try {
			String sql = String.format(GET_FILE_CONTENT, columnName, columnName, columnName, tableName);
			rs = jdbcDao.getResultSet(sql, Collections.singletonList(recordId));
			if (rs.next()) {
				Blob fileContent   = rs.getBlob(1);
				String fileName    = rs.getString(2);
				String contentType = rs.getString(3);
				
				fcv = new FileControlValue();
				if (fileContent != null) {
					blobFile = copyBlobToTempFile(fileContent); 
					fcv.setIn(new DeleteOnCloseFileInputStream(blobFile));
				}
				
				fcv.setFileName(fileName);
				fcv.setContentType(contentType);				
			}
		} catch (Exception e) {
			IoUtil.delete(blobFile);
			logger.error(
					"Error obtaining file content from: " + 
					tableName + "." + columnName + ", identifier = " + recordId, e);						
		} finally {
			jdbcDao.close(rs);
		}
		
		return fcv;
	}
	
	private File copyBlobToTempFile(Blob blob) {
		File file = null;
		FileOutputStream fout = null;
		
		try {
			file = File.createTempFile("form-migrate", ".dat");
			fout = new FileOutputStream(file);
			IoUtil.copy(blob.getBinaryStream(), fout);			
		} catch (Exception e) {
			IoUtil.delete(file);			
			throw new RuntimeException("Error copying blob data to file", e);
		} finally {
			IoUtil.close(fout);
		}
		
		return file;
	}
	
	private String[] getMultiSelectValues(List<Map<BaseAbstractAttributeInterface, Object>> msValuesMap) {
		List<String> values = new ArrayList<String>();
		
		for (Iterator i$ = msValuesMap.iterator(); i$.hasNext(); ) 
		{ 
			Object dataValue = i$.next();
			if (dataValue == null) {
				continue;
			}
			
			Map<BaseAbstractAttributeInterface, Object> valueMap = (Map<BaseAbstractAttributeInterface, Object>) dataValue;
			for (Object obj : valueMap.values()) {
				if (obj.toString() == null || obj.toString().trim().isEmpty()) {
					continue;
				}
				
				values.add(obj.toString());
			}
		}
		
		return values.toArray(new String[0]);
	}
	
	private boolean bool(Boolean booleanVal) {
		return booleanVal != null ? booleanVal : false;
	}
	
	
	private static final String UPDATE_FORM_CTXT_SQL = 
			"update DYEXTN_ABSTRACT_FORM_CONTEXT set CONTAINER_ID = ? where OLD_CONTAINER_ID = ?";
	
	private static final String GET_RECORD_IDS_SQL =
			"select re.IDENTIFIER from DYEXTN_ABSTRACT_RECORD_ENTRY re " +
			"inner join DYEXTN_ABSTRACT_FORM_CONTEXT fc on re.ABSTRACT_FORM_CONTEXT_ID = fc.IDENTIFIER " +
			"inner join %s cfc on cfc.IDENTIFIER = fc.identifier " +
			"where fc.container_id = %d";
	
	private static final String UPDATE_RECORD_ID = 
			"update DYEXTN_ABSTRACT_RECORD_ENTRY set RECORD_ID = ? where IDENTIFIER = ?";
	
	private static final String GET_FILE_CONTENT = 
			"select %s, %s_file_name, %s_content_type from %s where IDENTIFIER = ?";
	
	public static void main(String[] args) 
	throws Exception {
		MigrateForm migrateForm = new MigrateForm(null);
		migrateForm.migrateForm(Long.parseLong(args[0]), "CATISSUE_BASE_FORM_CONTEXT", 22L);		
	}
}

package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.ndao.ContainerDao;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.nutility.ContainerParser;
import edu.common.dynamicextensions.nutility.FormDataUtility;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.IdGeneratorUtil;

public class Container extends DynamicExtensionBaseDomainObject {	
	private static final long serialVersionUID = 449976852456002554L;
		
	private String name;

	private String caption;
	
	private String dbTableName;
   	
	private int sequenceNo;

	private long ctrlId;
	
	private Map<String, Control> controlsMap = new LinkedHashMap<String, Control>();
	
	private List<SkipRule> skipRules = new ArrayList<SkipRule>();
	
	private static final String tableNameFmt = "DE_E_%d";
	
	private static final String columnNameFmt = "DE_A_%d";
	
	private transient boolean isDto;
		
	private transient List<Control> addLog = new ArrayList<Control>();
	
	private transient List<Control> editLog = new ArrayList<Control>();
	
	private transient List<Control> delLog = new ArrayList<Control>();
	
	private transient Long createdBy;

	private transient Long lastUpdatedBy;

	private transient Date creationTime;

	private transient Date lastUpdatedTime;
	
	public void useAsDto() {
		this.isDto = true;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}
	
	public int getSequenceNo() {
		return sequenceNo;
	}
	
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Collection<Control> getControls() {
		return controlsMap.values();
	}

	public Map<String, Control> getControlsMap() {
		return controlsMap;
	}
	
	public void setControlsMap(Map<String, Control> controlsMap) {
		this.controlsMap = controlsMap;
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void setControls(Set<Control> controls) {
		controlsMap.clear();
		for (Control control : controls) {
			controlsMap.put(control.getName(), control);
		}
	}
	
	public List<SkipRule> getSkipRules() {
		return skipRules;
	}
	
	public SkipRuleBuilder newSkipRule() {
		return new SkipRuleBuilder(this);
	}
	
	public void addSkipRule(SkipRule rule) {
		skipRules.add(rule);
	}
	
	public void removeSkipRule(int i) {
		if (i < 0 || i > skipRules.size()) {
			throw new RuntimeException("SkipRule index out of bounds: " + i + " : " + skipRules.size());
		}
		
		skipRules.remove(i);
	}
	
	public boolean isSurveyForm() {
		boolean result = false;
		
		for (Control ctrl : controlsMap.values()) {
			if (ctrl instanceof PageBreak) {
				result = true;
				break;
			}
		}
		
		return result;
	}
			
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result	+ ((controlsMap == null) ? 0 : controlsMap.hashCode());
		result = prime * result + (int) (ctrlId ^ (ctrlId >>> 32));
		result = prime * result	+ ((dbTableName == null) ? 0 : dbTableName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sequenceNo;
		result = prime * result	+ ((skipRules == null) ? 0 : skipRules.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		Container other = (Container) obj;
		if (!StringUtils.equals(caption, other.caption) ||
		    (controlsMap == null && other.controlsMap != null) ||
			controlsMap.equals(other.controlsMap) ||
			ctrlId != other.ctrlId ||
			!StringUtils.equals(dbTableName, other.dbTableName) ||
			!StringUtils.equals(name, other.name) ||
			sequenceNo != other.sequenceNo ||
			(skipRules == null && other.skipRules != null) ||
			!skipRules.equals(other.skipRules)) {
			return false;
		}
			
		return true;
	}

	public List<Container> getSubContainers() {
		List<Container> containers = new ArrayList<Container>();
		
		for (Control ctrl : controlsMap.values()) {
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				containers.add(sfCtrl.getSubContainer());
			}
		}
		
		return containers;
	}
	
	public List<Container> getAllSubContainers() {
		List<Container> result = new ArrayList<Container>();
	
		List<Container> working = new ArrayList<Container>();
		working.add(this);
		while (!working.isEmpty()) {
			Container container = working.remove(0);
			List<Container> subContainers = container.getSubContainers();
			
			result.addAll(subContainers);			
			working.addAll(subContainers);			
		}
		
		return result;
	}	
	
	public List<Control> getAllControls() {
		List<Control> controls = new ArrayList<Control>();
		getAllControls(this, controls);
		return controls;
	}
		
	//
	// Behavioral API
	//
	public void addControl(Control control) {		
		if (controlsMap.containsKey(control.getName())) {
			// change this exception to status code
			throw new RuntimeException("Control with same name already exists: " + control.getName());
		}
				
		if (control.getSequenceNumber() == 0) {
			control.setSequenceNumber(++sequenceNo);
		} else if (control.getSequenceNumber() > sequenceNo) {
			sequenceNo = control.getSequenceNumber();
		}
				
		addLog.add(control);
		
		if (!isDto) {
			control.setId(++ctrlId);		
			if (control.getDbColumnName() == null) {
				control.setDbColumnName(String.format(columnNameFmt, ctrlId)); // set db name here
			}			
						
			if (control instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)control;
				Container sfContainer = sfCtrl.getSubContainer();
				if (sfContainer.isDto) {
					sfCtrl.setContainer(fromDto(sfContainer));
				}
			}
		}
		
		controlsMap.put(control.getName(), control);
		control.setContainer(this);
	}
	
	public void editControl(String name, Control control) {
		throwExceptionIfDto();
		editControl(name, control, false);
	}
	
	public void editControl(String name, Control control, boolean bulkEdit) {
		throwExceptionIfDto();
		
		Control existingControl = controlsMap.remove(name);
		if (existingControl == null) {
			// change this exception to status code
			throw new RuntimeException("Control with name doesn't exist: " + name);
		}

		if (control.getSequenceNumber() == 0) {
			control.setSequenceNumber(existingControl.getSequenceNumber());
		} else if (control.getSequenceNumber() > sequenceNo) {
			sequenceNo = control.getSequenceNumber();
		}
		
		if (remove(addLog, existingControl)) { 
			//
			// newly added control was edited
			//
			control.setId(existingControl.getId());
			control.setDbColumnName(existingControl.getDbColumnName());			
			add(addLog, control);
		} else if (!existingControl.getClass().getName().equals(control.getClass().getName())) { 
			//
			// the control type got changed -> remove old + add new
			//
			control.setId(++ctrlId);
			control.setDbColumnName(String.format(columnNameFmt, ctrlId)); // Set DB name here
			
			add(delLog, existingControl);			
			add(addLog, control);
		} else {
			//
			// saved control is edited
			//
			if (existingControl instanceof MultiSelectControl) {
				MultiSelectControl existingMsCtrl = (MultiSelectControl)existingControl;
				MultiSelectControl newMsCtrl = (MultiSelectControl)control;
				newMsCtrl.setTableName(existingMsCtrl.getTableName());
			} else if (existingControl instanceof SubFormControl) {
				SubFormControl existingSfCtrl = (SubFormControl)existingControl;
				SubFormControl newSfCtrl = (SubFormControl)control;				
				newSfCtrl.setTableName(existingSfCtrl.getTableName());
				
				if (bulkEdit) {
					Container newSfContainer = newSfCtrl.getSubContainer();
					Container existingSfContainer = existingSfCtrl.getSubContainer();
					newSfContainer.setId(existingSfContainer.getId());
					newSfContainer.setDbTableName(existingSfContainer.getDbTableName());
					existingSfContainer.editContainer(newSfCtrl.getSubContainer());
				}								
			}
			control.setContainer(this);
			control.setId(existingControl.getId());
			control.setDbColumnName(existingControl.getDbColumnName());			
			add(editLog, control);			
		}	

		controlsMap.put(control.getName(), control);
	}
	
	public void deleteControl(String name) {
		throwExceptionIfDto();
		
		Control existingControl = controlsMap.remove(name);
		if (existingControl == null) {
			// change this exception to status code
			throw new RuntimeException("Control with name doesn't exist: " + name);			
		}
		
		if (!remove(addLog, existingControl)) {
			add(delLog, existingControl);
			remove(editLog, existingControl);		
		}
		
		//
		// TODO: Remove skip rules of deleted controls
		//
	}
	
	public Control getControl(String name) {
		return controlsMap.get(name);
	}
	
	public Long save(UserContext userCtxt) {
		return save(userCtxt, true);
	}
	
	public Long save(UserContext userCtxt, boolean createTables) {
		JdbcDao jdbcDao = null;		
		try {
			jdbcDao = new JdbcDao();
			return save(userCtxt, jdbcDao, createTables);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}				
	}
	
	public Long save(UserContext userCtxt, JdbcDao jdbcDao) {
		return save(userCtxt, jdbcDao, true);
	}
	
	public Long save(UserContext userCtxt, JdbcDao jdbcDao, boolean createTables) {
		throwExceptionIfDto();
		
		try {
			if (createTables) {
				executeDDLWithoutTxn(jdbcDao);
			}
			
			boolean insert = (id == null);			
			int numIds = 0;
			List<Container> subContainers = getAllSubContainers();
			
			if (id == null) {
				numIds = 1 + subContainers.size();
			} else {
				for (Container c : subContainers) {
					if (c.getId() == null) {
						++numIds;
					}
				}
			}
			
			ContainerDao dao = new ContainerDao(jdbcDao);
			List<Long> ids = null;			
			if (numIds > 0) {
				ids = dao.getContainerIds(numIds);
			}
			
			
			int i = 0;
			if (id == null) {
				id = ids.get(i++);
			}
			
			for (Container c : subContainers) {
				if (c.getId() == null) {
					c.setId(ids.get(i++));
				}
			}		

			setSkipControlFlags();
			
			if (insert) {
				dao.insert(userCtxt, this);
			} else {
				dao.update(userCtxt, this);
			}

			return id;			
		} catch (Exception e) {
			throw new RuntimeException("Error saving container", e);
		} 
	}
				
	public static Container getContainer(Long id) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = new JdbcDao();
			ContainerDao dao = new ContainerDao(jdbcDao);
			return dao.getById(id);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container: " + id, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}			
		}		
	}
	
	public static List<ContainerInfo> getContainerInfoByCreator(Long creatorId) {
		JdbcDao jdbcDao = null;
		try {
			jdbcDao = new JdbcDao();
			ContainerDao dao = new ContainerDao(jdbcDao);
			return dao.getContainerInfoByCreator(creatorId);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container info by creator id: " + creatorId, e);
		} finally {
			if (jdbcDao != null) {
				jdbcDao.close();
			}
		}
	}
	
	public static Long createContainer(String formXml, String pvDir) 
	throws Exception {
		return createContainer(formXml, pvDir, true);
	}
	
	public static Long createContainer(String formXml, String pvDir, boolean createTables)
	throws Exception {
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container parsedContainer = parser.parse();

		Container existingContainer = null;		
		if (parsedContainer.getId() != null) {
			ContainerDao dao = new ContainerDao(new JdbcDao());
			existingContainer = dao.getById(parsedContainer.getId());
		}
		
		Container container = null;
		if (existingContainer != null) {
			existingContainer.editContainer(parsedContainer);
			container = existingContainer;
		} else if (parsedContainer.isDto) {
			container = fromDto(parsedContainer);
		}
		
		return container.save(null, createTables);
	}
				
	protected void editContainer(Container newContainer) {
		for (Control  ctrl : newContainer.getControls()) {
			if (getControl(ctrl.getName()) == null) {
				addControl(ctrl);
			} else {
				editControl(ctrl.getName(), ctrl, true);				
			}			
		}
		
		deleteRemovedControls(newContainer);
		
		//
		// TODO: Simply copying skip rules of new container.
		// Check whether this is correct way to handle?
		// Probably check for skip rules referring to non-existing fields
		//
		this.skipRules.clear();
		this.skipRules.addAll(newContainer.getSkipRules());
	}

	protected void deleteRemovedControls(Container newContainer) {
		Collection<Control> existingCtrls = getControls();
		Collection<String> removedCtrls = new ArrayList<String>();
		for (Control ctrl : existingCtrls) {
			if (newContainer.getControl(ctrl.getName()) == null) {
				//deleteControl(ctrl.getName());
				removedCtrls.add(ctrl.getName());
			}			
		}
		
		for (String removedCtrl : removedCtrls) {
			deleteControl(removedCtrl);
		}
	}
	
	protected void executeDDLWithoutTxn(JdbcDao jdbcDao) 
	throws Exception {
//		jdbcDao.suspendTxn();
		executeDDL(jdbcDao, null);
//		jdbcDao.resumeTxn();
	}
	

	protected void executeDDL(JdbcDao jdbcDao, String parentTableName) {
		//
		// 1. Execute DDL referring to addLog, editLog and delLog
		//
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
		for (Control ctrl : addLog) {
			boolean isMultiValuedControl = ctrl instanceof MultiSelectControl || ctrl instanceof SubFormControl;
			boolean nonDataColumn = ctrl instanceof Label || ctrl instanceof PageBreak;
			if (!isMultiValuedControl && !nonDataColumn) {
				columnDefs.addAll(ctrl.getColumnDefs());
			}
		}

		if (id == null) {
			dbTableName = getUniqueTableName();			

			if (parentTableName != null) {
				columnDefs.add(ColumnDef.get("PARENT_RECORD_ID", "NUMBER"));
			}
			
			createTable(jdbcDao, dbTableName, columnDefs, true);
		} else {
			addTableColumns(jdbcDao, dbTableName, columnDefs);
		}
				
		for (Control ctrl : addLog) {
			if (ctrl instanceof MultiSelectControl) {
				MultiSelectControl mCtrl = (MultiSelectControl)ctrl;
				mCtrl.setTableName(getUniqueTableName());
				createTable(jdbcDao, mCtrl.getTableName(), ctrl.getColumnDefs(), false);
			} else if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				sfCtrl.getSubContainer().executeDDL(jdbcDao, dbTableName);
			}
		}

		//
		// 2. clear logs once done
		//
		addLog.clear();
		delLog.clear();
		editLog.clear();		
	}
	
	private void setSkipControlFlags() {
		for (SkipRule rule : skipRules) {
			for (SkipCondition cond : rule.getConditions()) {
				cond.getSourceControl().setSkipLogicSourceControl(true);
			}
			
			for (SkipAction action : rule.getActions()) {
				action.getTargetCtrl().setSkipLogicTargetControl(true);
			}
		}
	}
	
	private void createTable(JdbcDao jdbcDao, String tableName, List<ColumnDef> columnDefs, boolean crtIdColumn) { 
		StringBuilder ddl = new StringBuilder();
		ddl.append("CREATE TABLE ").append(tableName).append(" (");
		
		if (crtIdColumn) {
			ddl.append("IDENTIFIER NUMBER PRIMARY KEY, ");
		}
		
		for (ColumnDef columnDef : columnDefs) {
			ddl.append(columnDef.getColumnName()).append(" ").append(columnDef.getDbType()).append(", ");
		}		
		ddl.replace(ddl.length() - 2, ddl.length(), ")");
		
		jdbcDao.executeDDL(ddl.toString());
	}
	
	private void addTableColumns(JdbcDao jdbcDao, String tableName, List<ColumnDef> columnDefs) {
		if (columnDefs == null || columnDefs.isEmpty()) {
			return;
		}
		
		StringBuilder ddl = new StringBuilder();
		ddl.append("ALTER TABLE ").append(tableName).append(" ADD (");
		for (ColumnDef columnDef :columnDefs) {
			ddl.append(columnDef.getColumnName()).append(" ").append(columnDef.getDbType()).append(", ");
		}
		ddl.delete(ddl.length() - 2, ddl.length());
		ddl.append(")");
		
		jdbcDao.executeDDL(ddl.toString());
	}
	
	private void add(List<Control> list, Control ctrl) {
		list.add(ctrl);
	}
	
	private boolean remove(List<Control> list, Control ctrl) {
		boolean removed = false;
		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i).getName().equals(ctrl.getName())) {
				list.remove(i);
				removed = true;
				break;
			}
		}
		
		return removed;
	}
	
	private void throwExceptionIfDto() {
		if (isDto) {
			throw new RuntimeException("Cannot invoke this operation on DTO");
		}
	}
	
	protected void clearLogs() {
		addLog.clear();
		editLog.clear();
		delLog.clear();
	}
	
	public String toXml() {
//		XStream xstream = new XStream(new KXml2Driver());
		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		setUpAliases(xstream);
		return xstream.toXML(this);
	}
	
	public static Container fromXml(String xml) {
//		XStream xstream = new XStream(new KXml2Driver());
		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.ID_REFERENCES);
		setUpAliases(xstream);
		
		Container container = (Container)xstream.fromXML(xml);
		container.initLogs(); // for some reason, xstream is not initializing add/edit/deleteLogs of container
		return container;
	}
	
	private static Container fromDto(Container dtoContainer) {
		Container container = new Container();
		container.setName(dtoContainer.getName());
		container.setCaption(dtoContainer.getCaption());
		container.setDbTableName(dtoContainer.getDbTableName());
		
		for (Control ctrl : dtoContainer.addLog) {
			container.addControl(ctrl);
		}		
		
		container.skipRules.addAll(dtoContainer.getSkipRules());
		return container;
	}
		
	private static void setUpAliases(XStream xstream) {
		xstream.alias("container", Container.class);
		xstream.alias("datePicker", DatePicker.class);
		xstream.alias("fileUpload", FileUploadControl.class);
		xstream.alias("label", Label.class);
		xstream.alias("subForm", SubFormControl.class);
		xstream.alias("checkBox", CheckBox.class);
		xstream.alias("comboBox", ComboBox.class);
		xstream.alias("listBox", ListBox.class);
		xstream.alias("radioButton", RadioButton.class);
		xstream.alias("numberField", NumberField.class);
		xstream.alias("stringField", StringTextField.class);
		xstream.alias("textArea", TextArea.class);
		
		xstream.alias("validationRule", ValidationRule.class);
		xstream.alias("skipRule", SkipRule.class);
		xstream.alias("skipCondition", SkipCondition.class);
		xstream.alias("logicalOp", LogicalOp.class);
		xstream.alias("relationalOp", RelationalOp.class);
		xstream.alias("showAction", ShowAction.class);
		xstream.alias("hideAction", HideAction.class);
		xstream.alias("enableAction", EnableAction.class);
		xstream.alias("disableAction", DisableAction.class);
		xstream.alias("showPvAction", ShowPvAction.class);
		xstream.alias("permissibleValue", PermissibleValue.class);
	}
	
	private String getUniqueTableName() {
		return String.format(tableNameFmt, getUniqueId());
	}
	
	private Long getUniqueId() {
		return IdGeneratorUtil.getNextUniqeId();
	}
	
	private void getAllControls(Container container, List<Control> controls) {	
		for (Control control : container.getOrderedControlList()) {
			if (control instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)control;
				getAllControls(sfCtrl.getSubContainer(), controls);
			} else {
				controls.add(control);
			}
		}
	}
	
	public static void main(String[] args) {
		Container c = new Container();
		c.setName("Person Demographics");
		
		StringTextField txtField = new StringTextField();
		txtField.setName("First Name");
		c.addControl(txtField);
		
		System.err.println(c.toXml());
	}
		
	/**
	 * @return container HTML
	 */
	public String render() {
		Map<ContextParameter, String> contextParameter = new HashMap<ContextParameter, String>();
		contextParameter.put(ContextParameter.MODE,
				WebUIManagerConstants.EDIT_MODE);
		return render(contextParameter, new FormData(this));
	}

	public String render(Map<ContextParameter, String> contextParameter, FormData formData) {
		if (isSurveyForm()) {
			return renderSurveyForm(contextParameter, formData);
		}
		
		final StringBuffer containerHTML = new StringBuffer(128);
		/*FIXME handling for override container caption */
		boolean addCaption = true;

		containerHTML.append("<table summary='' cellpadding='3' cellspacing='0' align='center' width='100%'>");

		updateWarningMessageHTML(contextParameter, containerHTML);
		
		String allControlHTML = generateContainerHTML(caption, addCaption, formData, contextParameter, true);
		containerHTML.append(allControlHTML);

		containerHTML.append("</table>");

		return containerHTML.toString();

	}

	private void updateWarningMessageHTML(Map<ContextParameter, String> contextParameter,
			final StringBuffer containerHTML) {
		boolean isShowRequiredFieldWarningMessage = Boolean.valueOf(contextParameter
				.get(ContextParameter.MANDATORY_MESSAGE));
		if (contextParameter.get(ContextParameter.MODE) != null
				&& contextParameter.get(ContextParameter.MODE).equalsIgnoreCase(WebUIManagerConstants.EDIT_MODE)
				&& isShowRequiredFieldWarningMessage) {

			containerHTML.append("<tr><td class='formMessage' colspan='3'><span class='font_red'>")
					.append(getRequiredFieldIndicatior()).append("&nbsp;</span><span class='font_gr_s'>")
					.append(getRequiredFieldWarningMessage()).append("</span></td></tr><tr><td height='5'/></tr>");
		}
	}

	/**
	 * FIXME this is a temporary method, this field should be configurable as it was in the legacy model
	 * @return
	 */
	private Object getRequiredFieldWarningMessage() {

		return "indicates mandatory fields.";
	}

	/**
	 * FIXME this is a temporary method, this field should be configurable as it was in the legacy model
	 * @return
	 */
	protected String getRequiredFieldIndicatior() {
		return "*";
	}

	/**
	 * @param generateSubformHTML 
	 * @return return the HTML string for this type of a object
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException 
	 */
	public String generateContainerHTML(final String caption, boolean addCaption, FormData formData,
			Map<ContextParameter, String> contextParameter, boolean generateSubformHTML) {
		final StringBuffer rowHTML = new StringBuffer(108);
		final List<Object> values = new ArrayList<Object>();
		final List<Control> controls = getOrderedControlList();
		int lastRow = 0;
		Control lastControl = null;
		addCaptionHTML(rowHTML, caption, values, addCaption, formData);

		for (final Control control : controls) {
			ControlValue fieldValue = formData.getFieldValue(control.getName());
			String controlName = control.getControlName();
			String controlHTML;

			//TODO: needs a better way to handle for restricting form display up to two levels
			if (control instanceof SubFormControl) {
				controlHTML = ((SubFormControl) control).render(controlName, fieldValue, contextParameter,
						generateSubformHTML);
			} else {
				controlHTML = control.render(fieldValue, contextParameter);
			}

			if (lastRow < control.getSequenceNumber()) {
				//Do not add row close tag before first row.
				if (lastControl != null) {
					rowHTML.append(CLOSE_ROW_HTML);
				}

				//No extra space should be added between control and a Note above it.
				if (!fieldValue.isHidden() && !(lastControl instanceof Label)) {
					rowHTML.append(EMPTY_ROW_HTML);
				}

				rowHTML.append(String.format(TBODY_TAG, controlName)).append(
						String.format(CONTROL_ROW_HTML_START_TAG, getRowDisplay(fieldValue)));
			} 
			rowHTML.append(controlHTML);

			if (control.isDynamic()) {
				rowHTML.append("<input type='hidden' name='dynamicControl'  value = '").append(controlName)
						.append("_tbody' />");
			}
			lastControl = control;
			lastRow = control.getSequenceNumber();
		}
		rowHTML.append("</td></tr>");

		return rowHTML.toString();
	}

	/**
	 * This method is used for UI rendering
	 */
	private String getRowDisplay(ControlValue fieldValue) {
		String rowDisplay = "display:row";

		if (fieldValue != null && fieldValue.isHidden()) {
			rowDisplay = "display:none";
		}
		return rowDisplay;
	}

	private List<Control> getOrderedControlList() {
		final List<Control> controls = new ArrayList<Control>(getControls());
		java.util.Collections.sort(controls);
		return controls;
	}

	/**
	 * @param captionHTML
	 * @param caption in the format -- NewCaption:Main ContainerId
	 * @throws DynamicExtensionsSystemException
	 */
	private void addCaptionHTML(final StringBuffer captionHTML, final String caption, final List<Object> values,
			boolean addCaption, FormData formData) {
		// Check added for CSD project to ensure that HTML is generated for mock
		// container with NULL id.
		boolean isIdNull = false;
		if (getId() == null) {
			isIdNull = true;
			setId(-1L);
		}

		//check if Id in caption matches current id - if yes then it is main form, so replace caption
		if (caption == null || !caption.endsWith(getId().toString())) {
			if (addCaption) // for subform
			{
				captionHTML.append("<tr ");
				addDisplayOptionForRow(values, captionHTML, "_caption", formData);
				captionHTML.append("<td class='td_color_6e81a6' colspan='100' align='left'>")
						.append(DynamicExtensionsUtility.replaceHTMLSpecialCharacters(caption)).append("<tr ");
				addDisplayOptionForRow(values, captionHTML, "_emptyrow", formData);
				captionHTML.append("<td height='5'></td></tr>");
			}
		} else { // for Main form
			captionHTML.append("<tr><td class='td_color_6e81a6_big' colspan='100' align='left'>").append(caption.substring(0, (caption.length() - 1 - getId().toString().length()))).append("<tr><td height='5'></td></tr>");
		}

		if (isIdNull) {
			setId(null);
		}
	}

	/**
	 *
	 * @param container
	 * @param values
	 * @param captionHTML
	 * @throws DynamicExtensionsSystemException
	 */
	private void addDisplayOptionForRow(final List<Object> values, final StringBuffer captionHTML,
			final String identifier, FormData formData) {
		if (!getControls().isEmpty()) {
			final Control control = getControls().iterator().next();
			String controlName = control.getControlName();
			captionHTML.append("id='" + controlName + identifier + "_container_div' name='" + controlName + identifier
					+ "_container_div'");

			values.clear();

			if (doAllControlsHaveHideAction(formData)) {
				captionHTML.append(" style='display:none' >");
			} else {
				captionHTML.append(" style='display:row' >");
			}
			captionHTML.append("<input type='hidden' name='skipLogicHideControls' id='skipLogicHideControls' value = '"
					+ controlName + identifier + "_container_div' />");
		}

	}

	private boolean doAllControlsHaveHideAction(FormData formData) {
		boolean isAlltargetControls = true;
		for (final Control control : getControls()) {
			if (!control.isSkipLogicTargetControl() || !formData.getFieldValue(control.getName()).isHidden()) {
				isAlltargetControls = false;
				break;
			}
		}
		return isAlltargetControls;
	}

	public String renderAsGrid(List<FormData> formDataList, Map<ContextParameter, String> contextParameter,
			boolean isPasteEnable, boolean showCaption) {
		StringBuffer htmlForGrid = new StringBuffer(1404);
		htmlForGrid.append("<input type='hidden' name='");
		htmlForGrid.append(name);
		htmlForGrid.append("_rowCount' id= '");
		htmlForGrid.append(name);
		htmlForGrid.append("_rowCount' value='");
		htmlForGrid.append((formDataList != null ? formDataList.size() : 0));
		htmlForGrid.append("'/>");
		//2: update sub form heading
		htmlForGrid.append("<tr width='100%'> <td " + " colspan='100'"
				+ " align='center'> <table cellpadding='3' cellspacing='0' " + "align='center' width='100%'>");

		if (showCaption) {
			htmlForGrid.append("<tr width='100%'><td class='td_color_6e81a6' " + "colspan='3' align='left'>")
					.append(getCaption()).append("</td></tr>");
		}

		//3: configure paste button depending on flag
		String dataEntryOperation = contextParameter.get(ContextParameter.MODE);
		if (isPasteEnable && WebUIManagerConstants.EDIT_MODE.equals(dataEntryOperation)) {
			htmlForGrid
					.append("<tr><td colspan='3' width='100%' style='background-color:#E3E2E7;'><input type='button' ")
					.append("style='border: 0px; background-image: url(images/de/b_paste.gif);height: 20px; ")
					.append("width: 59px;' align='middle'  id='paste_").append(name).append("' onclick='pasteData(\"")
					.append(name).append("\",\"many\")'/> </td>")
					.append("</tr>");
		}

		List<Control> labels = new ArrayList<Control>();
		List<Control> dataControls = new ArrayList<Control>();
		segregateControls(dataControls, labels);

		for (Control label : labels) {
			htmlForGrid.append(" <tr width='100%'>").append(label.render(null, contextParameter))
					.append("</table></tr>");
		}
		htmlForGrid
				.append(" <tr width='100%'><td colspan='3' width='100%'><table id='")
				.append(name)
				.append("_table' cellpadding='3' cellspacing='3' border='0' align='center' width='100%'><tr width='100%' class='formLabel_withoutBorder'><th width='1%'>&nbsp;</th>");

		//4: update table headings 
		for (Control control : dataControls) {
			htmlForGrid.append("<th>");

			if (control.isMandatory()) {

				htmlForGrid.append("<span class='font_red'>");
				htmlForGrid.append(getRequiredFieldIndicatior());
				htmlForGrid.append("</span>&nbsp;&nbsp;<span class='font_bl_nor'>");
				htmlForGrid.append(control.getCaption());
				htmlForGrid.append("</span>");
			} else {
				htmlForGrid.append("&nbsp;&nbsp;<span class='font_bl_nor'>");
				htmlForGrid.append(control.getCaption());
				htmlForGrid.append("</span>");
			}

			htmlForGrid.append("</th>");
		}

		htmlForGrid.append("</tr>");

		//5: update data rows
		if (formDataList != null) {
			int index = 1;
			for (FormData rowValueMap : formDataList) {
				htmlForGrid.append(getContainerHTMLAsARow(dataControls, index, rowValueMap, contextParameter));
				index++;
			}
		}

		htmlForGrid.append("</table> <div id='wrapper_div_").append(name).append("' > &nbsp;</div>");

		//6: add buttons to the grid
		if (dataEntryOperation.equals("edit")) {
			htmlForGrid
					.append("<table cellpadding='3' cellspacing='0' align='center' width='100%' class='td_color_e3e2e7'><tr><td align='left'>"
							+ "<input type='button' style='border: 0px; background-image: url(images/de/b_delete.gif); height: 20px; width: 59px;' align='middle' onClick=\"removeCheckedRow('")
					.append(name)
					.append("');" + "\" id='btnDelete")
					.append(name)
					.append("'/></td><td align='right'>"
							+ "<input type='button' style='border: 0px; background-image: url(images/de/b_add_more.gif); height: 20px; width: 76px;' align='middle' onClick=\"addRow('")
					.append(name).append("')\" id='btnAddMore").append(name).append("'/>" + "</td></tr></table>");

		}

		htmlForGrid.append("</td></tr>");

		return htmlForGrid.toString();
	}
	
	/**
	 * This is used for UI rendering
	 */
	public String getContainerHTMLAsARow(int rowId, FormData formData, Map<ContextParameter, String> contextParameter) {
		return getContainerHTMLAsARow(getDataControls(), rowId, formData, contextParameter);
	}

	/**
	 * @param rowId
	 * @param formData
	 * @param contextParameter
	 * @return
	 */
	public String getContainerHTMLAsARow(List<Control> controlList, int rowId, FormData formData,
			Map<ContextParameter, String> contextParameter) {
		StringBuffer contHtmlAsARow = new StringBuffer(96);

		String rowClass = "formField_withoutBorder";
		if (rowId % 2 == 0) {
			rowClass = "td_color_f0f2f6";
		}
		contHtmlAsARow.append("<tr width='100%' class='").append(rowClass).append("'><td width='1%'>");

		if (contextParameter != null
				&& WebUIManagerConstants.EDIT_MODE.equals(contextParameter.get(ContextParameter.MODE))) {
			contHtmlAsARow.append("<input type='checkbox' name='deleteRow' value='' " + "id='checkBox_")
					.append(getId()).append('_').append(rowId).append("'/>");
		} else {
			contHtmlAsARow.append("&nbsp;");
		}

		contHtmlAsARow.append("</td>");
		for (Control control : controlList) {
			generateHTMLforControl(rowId, contHtmlAsARow, formData, control, contextParameter);
		}

		contHtmlAsARow.append("</tr>");

		return contHtmlAsARow.toString();
	}

	/**
	 * @param rowId
	 * @param contHtmlAsARow
	 * @param formData
	 * @param control
	 * @param contextParameter
	 */
	private void generateHTMLforControl(int rowId, StringBuffer contHtmlAsARow, FormData formData,
			final Control control, Map<ContextParameter, String> contextParameter) {
		String controlHTML = control.renderInGrid(rowId, formData.getFieldValue(control.getName()), contextParameter);

		if (control.isDynamic()) {
			String tbodyHTML = "<td valign='middle' NOWRAP='true' id='%stbody'> <input type='hidden' name='dynamicControl'  value = '%stbody' />";
			tbodyHTML = String.format(tbodyHTML, control.getControlName(rowId), control.getControlName(rowId));
			contHtmlAsARow.append(tbodyHTML).append(controlHTML.replaceAll("style='float:left'", "")).append("</td>");
		} else {
			contHtmlAsARow.append("<td valign='middle' NOWRAP='true'>")
				.append(controlHTML.replaceAll("style='float:left'", "")).append("</td>");
		}
	}

	/**
	 * This is used for UI rendering
	 */
	private void segregateControls(List<Control> dataControls, List<Control> labels) {

		for (Control control : getOrderedControlList()) {

			if (control instanceof Label) {
				labels.add(control);
			} else {
				dataControls.add(control);
			}
		}
	}

	/**
	 * This is used for UI rendering
	 */
	private List<Control> getDataControls() {
		List<Control> dataControls = new ArrayList<Control>();

		for (Control control : getOrderedControlList()) {

			if (!(control instanceof Label)) {
				dataControls.add(control);
			}
		}
		return dataControls;
	}

	private void initLogs() {
		List<Container> containers = new ArrayList<Container>();
		containers.add(this);
		
		while (!containers.isEmpty()) {
			Container container = containers.remove(0);
			container.addLog = new ArrayList<Control>();
			container.editLog = new ArrayList<Control>();
			container.delLog = new ArrayList<Control>();
			
			for (Control ctrl : container.getControls()) {
				if (ctrl instanceof SubFormControl) {
					SubFormControl sfCtrl = (SubFormControl)ctrl;
					containers.add(sfCtrl.getSubContainer());
				}
			}			
		}		
	}
	
	 
	public String renderSurveyForm(Map<ContextParameter, String> contextParameter, FormData formData) {
		String categorydiv = "<div><div id='sm-pages'>%s</div></div>";
		String pagediv = "<div class='sm-page' id='%d' style='display:none'>%s</div>";
		StringBuilder pages = new StringBuilder();
		StringBuilder categoryHtml = new StringBuilder(renderHiddenInputs(formData));

		for (Page p : getPages()) {
			pages.append(String.format(pagediv, p.getId().longValue(), p.render(formData, contextParameter)));
		}

		categoryHtml.append(String.format(categorydiv, pages.toString()));
		return categoryHtml.toString();
	}
	
	private String renderHiddenInputs(FormData formData) throws NumberFormatException {
		String containerIdentifier = String.format(CONTAINER_ID, getId());
		String formCaption = String.format(FORM_CAPTION, getCaption());
		String controlsCount = String.format(CONTROL_COUNT, FormDataUtility.getFilledControlCount(formData));
		String emptyControlsCount = String.format(EMPTY_CONTROL_COUNT, FormDataUtility.getEmptyControlCount(formData));
		String displayPage = String.format(DISPLAY_PAGE, getFirstEmptyPage(formData));

		StringBuilder results = new StringBuilder();
		results.append(containerIdentifier);
		results.append(formCaption);
		results.append(controlsCount);
		results.append(emptyControlsCount);
		results.append(displayPage);
		return results.toString();
	}
	
	private long getFirstEmptyPage(FormData data) {
		long result = -1L, pageId = 1L;
		for (Control ctrl : getOrderedControlList()) {
			if (ctrl instanceof PageBreak) {
				pageId++;
				continue;
			}
			
			if (data.getFieldValue(ctrl.getName()).isEmpty()) {
				result = pageId;
				break;
			}
		}
		
		return result;
	}
	
	public List<Page> getPages() {
		List<Page> pages = new ArrayList<Page>();		
		Long pageId = 1L;
		
		Page page = new Page();
		page.setId(pageId);
		
		for (Control ctrl :getOrderedControlList()) {
			if (ctrl instanceof PageBreak) {
				if (!page.isEmptyPage()) {
					pages.add(page);
				}
				
				++pageId;
				page = new Page();
				page.setId(pageId);				
			} else {
				page.addControl(ctrl);
			}
		}
		
		if (!page.isEmptyPage()) {
			pages.add(page);
		}
		
		return pages;
	}
		
	//
	// Works if the input control belongs to container
	// on which this method is invoked
	//
	public String getControlCanonicalName(Control ctrl) {
		if (ctrl.getName() == null) {
			throw new RuntimeException("Control name is null. Invalid control state");
		}
		
		Control formCtrl = controlsMap.get(ctrl.getName());
		if (formCtrl != null) {
			return formCtrl.getName();
		}
		
		for (Control c : getControls()) {
			if (!(c instanceof SubFormControl)) {
				continue;
			}
			
			SubFormControl sfCtrl = (SubFormControl)c;
			String name = sfCtrl.getSubContainer().getControlCanonicalName(ctrl);
			if (name != null) {
				return new StringBuilder(sfCtrl.getName()).append(".").append(name).toString();
			}
		}
		
		return null;		
	}
		
	public Control getControl(String controlName, String separator) {
		Container container = this;
		Control ctrl = container.getControl(controlName);
		if (ctrl != null) {
			return ctrl;
		}
		
		String[] controlNameParts = controlName.split(separator);
		if (controlNameParts.length == 1) { // no sub form control name
			throw new RuntimeException("Invalid control name: " + controlName); 
		}
		
		for (int i = 0; i < controlNameParts.length - 1; ++i) {
			ctrl = container.getControl(controlNameParts[i]);
			if (!(ctrl instanceof SubFormControl)) {
				throw new RuntimeException("Invalid control name: " + controlName);
			}
			
			SubFormControl sfCtrl = (SubFormControl)ctrl;
			container = sfCtrl.getSubContainer();			
		}
		
		ctrl = container.getControl(controlNameParts[controlNameParts.length - 1]);
		if (ctrl == null) {
			throw new RuntimeException("Invalid control name: " + controlName);
		}
		
		return ctrl;
	}

	public SubFormControl getSubFormControl(String name) {

		SubFormControl subFormControl = null;
		for (Control control : getControls()) {
			if (!(control instanceof SubFormControl)) {
				continue;
			}
			SubFormControl sfCtl = (SubFormControl) control;
			if (name.equals(sfCtl.getSubContainer().getName())) {
				subFormControl = sfCtl;
				break;
			}

		}
		return subFormControl;
	}
	
	private static final String EMPTY_ROW_HTML = "<tr><td height='7'></td></tr>";

	private static final String CLOSE_ROW_HTML = "</table></td></tr></tbody>";

	private static final String TBODY_TAG = "<tbody id='%s_tbody'>";

	private static final String CONTROL_ROW_HTML_START_TAG = "<tr valign='center' style='%s'/>";
	
	private static final String CONTAINER_ID = "<input type='hidden' id='containerIdentifier' name='containerIdentifier'  value='%d'></input>";

	private static final String FORM_CAPTION = "<input type='hidden' id='categoryName' value='%s'></input>";

	private static final String CONTROL_COUNT = "<input type='hidden' id='controlsCount' value='%d'></input>";

	private static final String EMPTY_CONTROL_COUNT = "<input type='hidden' id='emptyControlsCount' value='%d'></input>";

	private static final String DISPLAY_PAGE = "<input type='hidden' id='displayPage' name='displayPage'  value='%d'></input>";	
}

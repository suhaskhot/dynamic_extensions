package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.common.dynamicextensions.domain.nui.SkipCondition.RelationalOp;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;
import edu.common.dynamicextensions.ndao.ContainerDao;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.TransactionManager;
import edu.common.dynamicextensions.ndao.TransactionManager.Transaction;
import edu.common.dynamicextensions.nutility.ContainerCache;
import edu.common.dynamicextensions.nutility.ContainerParser;
import edu.common.dynamicextensions.nutility.IdGenerator;
import edu.common.dynamicextensions.util.parser.FormulaParser;

public class Container implements Serializable {			
	private static final long serialVersionUID = -6178237643696575798L;

	private static final String tableNameFmt = "DE_E_%d";
	
	private static final String columnNameFmt = "DE_A_%d";
	
	private static final String specialChars = "[+-/*(){}%. ]";
	
	private static Pattern notAllowed = Pattern.compile(specialChars, Pattern.CASE_INSENSITIVE);
			
	private Long id;
	
	private String name;

	private String caption;
	
	private String dbTableName;
	
	private String primaryKey = "IDENTIFIER";
	
	private String hierarchyTable;
	
	private String hierarchyAncestorCol;
	
	private String hierarchyDescendentCol;
	
	private String activeCond;
   	
	private int sequenceNo;

	private long ctrlId;
	
	private Map<String, Control> controlsMap = new LinkedHashMap<String, Control>();
	
	private Set<String> userDefCtrlNames = new HashSet<String>();
	
	private List<SkipRule> skipRules = new ArrayList<SkipRule>();
		
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
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (notAllowed.matcher(name).find()) {
			throw new RuntimeException("Following special characters in form names not allowed: " + specialChars);
		}
		
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
	
	public String getPrimaryKey() {
		return primaryKey == null ? "IDENTIFIER" : primaryKey;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getHierarchyTable() {
		return hierarchyTable;
	}

	public void setHierarchyTable(String hierarchyTable) {
		this.hierarchyTable = hierarchyTable;
	}

	public String getHierarchyAncestorCol() {
		return hierarchyAncestorCol;
	}

	public void setHierarchyAncestorCol(String hierarchyAncestorCol) {
		this.hierarchyAncestorCol = hierarchyAncestorCol;
	}

	public String getHierarchyDescendentCol() {
		return hierarchyDescendentCol;
	}

	public void setHierarchyDescendentCol(String hierarchyDescendentCol) {
		this.hierarchyDescendentCol = hierarchyDescendentCol;
	}

	public String getActiveCond() {
		return activeCond;
	}

	public void setActiveCond(String activeCond) {
		this.activeCond = activeCond;
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
	
	public Set<String> getUserDefCtrlNames() {
		return userDefCtrlNames;
	}

	public void setUserDefCtrlNames(Set<String> userDefCtrlNames) {
		this.userDefCtrlNames = userDefCtrlNames;
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
		
	
	public String getUdnFormula(String shortCodeFormula) {
		FormulaParser formulaParser = new FormulaParser();
		String udnFormula = shortCodeFormula;
		
		try {
			formulaParser.parseExpression(shortCodeFormula);
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing the formula : "+shortCodeFormula,e);
		}
	
		List<String> symbols = formulaParser.getSymbols();
		orderByLength(symbols);
		
		for (String symbol : symbols) {
			Control ctrl =  getControl(symbol, "\\.");
			
			if (ctrl == null) {
				throw new RuntimeException("Control with name doesn't exist: " + symbol);
			}
			
			String userDefName = getControlCanonicalUdn(ctrl);
			udnFormula = udnFormula.replaceAll(symbol, userDefName);
		}
	
		return udnFormula;
	}
	
	public String getShortCodeFormula(String udnFormula) { 
		FormulaParser formulaParser = new FormulaParser();
		String shortCodeFormula = udnFormula;
		
		try {
			formulaParser.parseExpression(udnFormula);
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing the formula : "+shortCodeFormula,e);
		}
	
		List<String> symbols = formulaParser.getSymbols();
		orderByLength(symbols);
		
		for (String symbol : symbols) {
			Control ctrl =  getControlByUdn(symbol, "\\.");
			
			if (ctrl == null) {
				throw new RuntimeException("Control with udn doesn't exist: " + symbol);
			}
			String ctrlName = getControlCanonicalName(ctrl);
			shortCodeFormula = shortCodeFormula.replaceAll(symbol, ctrlName);
		}
	
		return shortCodeFormula;
	}
	
	private void orderByLength(List<String> symbols) {
		Collections.sort(symbols, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s2.length() - s1.length(); // < 0 =0 > 0
			}
		});
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result	+ ((controlsMap == null) ? 0 : controlsMap.hashCode());
		result = prime * result + (int) (ctrlId ^ (ctrlId >>> 32));
		result = prime * result	+ ((dbTableName == null) ? 0 : dbTableName.hashCode());
		result = prime * result + ((primaryKey == null ? 0 : primaryKey.hashCode()));
		result = prime * result + ((hierarchyTable == null ? 0 : hierarchyTable.hashCode()));
		result = prime * result + ((hierarchyAncestorCol == null ? 0 : hierarchyAncestorCol.hashCode()));
		result = prime * result + ((hierarchyDescendentCol == null ? 0 : hierarchyDescendentCol.hashCode()));
		result = prime * result + ((activeCond == null ? 0 : activeCond.hashCode()));
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
			!StringUtils.equals(primaryKey, other.primaryKey) ||
			!StringUtils.equals(hierarchyTable, other.hierarchyTable) ||
			!StringUtils.equals(hierarchyAncestorCol, other.hierarchyAncestorCol) ||
			!StringUtils.equals(hierarchyDescendentCol, other.hierarchyDescendentCol) ||
			!StringUtils.equals(activeCond, other.activeCond) ||
			!StringUtils.equals(name, other.name) ||
			sequenceNo != other.sequenceNo ||
			(skipRules == null && other.skipRules != null) ||
			!skipRules.equals(other.skipRules)) {
			return false;
		}
			
		return true;
	}
	
	private Object readResolve() {
		if (userDefCtrlNames == null) {
			userDefCtrlNames = new HashSet<String>();
		}
		
		return this;
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
	
	public Collection<List<Control>> getControlsGroupedByRow() {
		Map<Integer, List<Control>> rows = new TreeMap<Integer, List<Control>>();
		
		for (Control ctrl : controlsMap.values()) {
			List<Control> row = rows.get(ctrl.getSequenceNumber());
			if (row == null) {
				row = new ArrayList<Control>();
				rows.put(ctrl.getSequenceNumber(), row);
			}
			
			int xPos = ctrl.getxPos();
			int i;
			for (i = 0; i < row.size(); ++i) {
				if (row.get(i).getxPos() > xPos) {
					break;
				}
			}
			
			row.add(i, ctrl);
		}
		
		return rows.values();
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
	public Container getReplica() {
		Container replica = Container.fromXml(this.toXml());
		replica.nullifyContainerIds();
		return replica;
	}

	public void nullifyContainerIds() {
		setId(null);
		for (Container sub : getAllSubContainers()) {
			sub.setId(null);
		}		
	}
		
	public void addControl(Control control) {		
		if (control.getName() == null || controlsMap.containsKey(control.getName())) {
			// change this exception to status code
			throw new RuntimeException("Control with same name already exists or control name is null " + control.getName());
		}
		
		if (control.getUserDefinedName() == null || userDefCtrlNames.contains(control.getUserDefinedName())) {
			throw new RuntimeException("Control with same user defined name already exists or user defined name of control is null " + control.getName());
		}
				
		validateNameAndUdn(control);
		
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
					sfCtrl.setSubContainer(fromDto(sfContainer));
				}
			}
		}
		
		controlsMap.put(control.getName(), control);
		userDefCtrlNames.add(control.getUserDefinedName());
		control.setContainer(this);
	}
	
	private void validateNameAndUdn(Control control) {		
		if (notAllowed.matcher(control.getName()).find()) {
			throw new RuntimeException("Control name contains spl characters: " + specialChars + ", " + control.getName());
		} else if (notAllowed.matcher(control.getUserDefinedName()).find()) {
			throw new RuntimeException("Control user defined name contains spl characters: " + specialChars + ", " +  control.getUserDefinedName());
		}
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
		userDefCtrlNames.remove(existingControl.getUserDefinedName());

		validateNameAndUdn(control);

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
			if (!(control instanceof MultiSelectControl)) {						// For MSCtrls Column name is "VALUE"
				control.setDbColumnName(String.format(columnNameFmt, ctrlId)); 	// Set DB name here
			}
			
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
					existingSfContainer.editContainer(newSfContainer);
					existingSfCtrl.setUserDefinedName(newSfCtrl.getUserDefinedName());
					control = existingSfCtrl;
				}				
			}
			
			control.setId(existingControl.getId());
			control.setDbColumnName(existingControl.getDbColumnName());
			control.setContainer(this);
			add(editLog, control);			
		}	

		controlsMap.put(control.getName(), control);
		userDefCtrlNames.add(control.getUserDefinedName());
	}
	
	public void deleteControl(String name) {
		throwExceptionIfDto();
		
		Control existingControl = controlsMap.remove(name);
		if (existingControl == null) {
			// change this exception to status code
			throw new RuntimeException("Control with name doesn't exist: " + name);			
		}
		userDefCtrlNames.remove(existingControl.getUserDefinedName());
		
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
	
	public Control getControlByUdn(String userDefName) {
		if (!userDefCtrlNames.contains(userDefName)) {
			return null;
		}
		
		Control result = null;		
		for (Control ctrl : getControls()) {
			if (ctrl.getUserDefinedName().equals(userDefName)) {
				result = ctrl;
				break;
			}
		}
		
		return result;
	}
	
	public Long save(UserContext userCtxt) {
		return save(userCtxt, true);
	}
	
	public Long save(UserContext userCtxt, boolean createTables) {
		JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
		return save(userCtxt, jdbcDao, createTables);
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
			
			ContainerCache.getInstance().remove(id);
						
			return id;			
		} catch (Exception e) {
			throw new RuntimeException("Error saving container", e);
		} 
	}
	
	public static boolean deleteContainer(Long id) {
		return new ContainerDao(JdbcDaoFactory.getJdbcDao()).delete(id, false);
	}
	
	public static boolean softDeleteContainer(Long id) {
		return new ContainerDao(JdbcDaoFactory.getJdbcDao()).delete(id, true);
	}
				
	public static Container getContainer(Long id) {
		return getContainer(JdbcDaoFactory.getJdbcDao(), id);
	}
	
	public static Container getContainer(JdbcDao jdbcDao, Long id) {
		try {						
			ContainerDao containerDao = new ContainerDao(jdbcDao);		
			return containerDao.getById(id);				
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container: " + id, e);
		}	
	}
	
	public static Container getContainer(String name) {
		return getContainer(JdbcDaoFactory.getJdbcDao(), name);
	}
	
	public static Container getContainer(JdbcDao jdbcDao, String name) {
		try {						
			ContainerDao containerDao = new ContainerDao(jdbcDao);
			return containerDao.getByName(name);				
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container: " + name, e);
		}	
	}
		
	public static List<ContainerInfo> getContainerInfo() {
		try {
			ContainerDao dao = new ContainerDao(JdbcDaoFactory.getJdbcDao());
			return dao.getContainerInfo();
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container info", e);
		}		
	}
	
	public static List<ContainerInfo> getContainerInfoByCreator(Long creatorId) {
		try {
			ContainerDao dao = new ContainerDao(JdbcDaoFactory.getJdbcDao());
			return dao.getContainerInfoByCreator(creatorId);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container info by creator id: " + creatorId, e);
		}
	}
	
	public static Long createContainer(String formXml, String pvDir) 
	throws Exception {
		return createContainer(formXml, pvDir, true);
	}
	
	public static Long createContainer(String formXml, String pvDir, boolean createTables)
	throws Exception {
		return createContainer(null, formXml, pvDir, createTables);
	}
	
	public static Long createContainer(UserContext ctxt, String formXml, String pvDir, boolean createTables)
	throws Exception {
		ContainerParser parser = new ContainerParser(formXml, pvDir);
		Container parsedContainer = parser.parse();

		Container existingContainer = null;		
		if (parsedContainer.getId() != null) {
			existingContainer = getContainer(parsedContainer.getId());
		}
		
		if (existingContainer == null) { 
			if (parsedContainer.getName() != null) {
				existingContainer = getContainer(parsedContainer.getName());
			}
		}
		
		Container container = null;
		if (existingContainer != null) {
			existingContainer.editContainer(parsedContainer);
			container = existingContainer;
		} else if (parsedContainer.isDto) {
			container = fromDto(parsedContainer);
		}
		
		return container.save(ctxt, createTables);
	}
				
	public void editContainer(Container newContainer) {
		if (! this.getName().equals(newContainer.getName())) {
			throw new RuntimeException("Error : Container name cannot be edited");
		}
		
		this.setName(newContainer.getName());
		this.setCaption(newContainer.getCaption());
		
		if (newContainer.getPrimaryKey() != null) {
			this.setPrimaryKey(newContainer.getPrimaryKey());
		}
		
		setHierarchyTable(newContainer.getHierarchyTable());
		setHierarchyAncestorCol(newContainer.getHierarchyAncestorCol());
		setHierarchyDescendentCol(newContainer.getHierarchyDescendentCol());
		setActiveCond(newContainer.getActiveCond());
		
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
		Transaction txn = TransactionManager.getInstance().newTxn();
		try {
			executeDDL(jdbcDao, null);
		} finally {
			if (txn != null) {
				TransactionManager.getInstance().commit(txn);
			}
		}
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
				columnDefs.add(ColumnDef.get("PARENT_RECORD_ID", ColumnTypeHelper.getIntegerColType()));
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
		
		for (Control ctrl : editLog) {
			if (ctrl instanceof SubFormControl) {
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
			ddl.append("IDENTIFIER ").append(ColumnTypeHelper.getIntegerColType()).append(" PRIMARY KEY, ");
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
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
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
		container.setPrimaryKey(dtoContainer.getPrimaryKey());
		container.setHierarchyTable(dtoContainer.getHierarchyTable());
		container.setHierarchyAncestorCol(dtoContainer.getHierarchyAncestorCol());
		container.setHierarchyDescendentCol(dtoContainer.getHierarchyDescendentCol());
		container.setActiveCond(dtoContainer.getActiveCond());
		
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
		xstream.alias("pageBreak", PageBreak.class);
		
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
	
	// TODO: Hard coded tab name
	private Long getUniqueId() {
		return IdGenerator.getInstance().getNextId("DE_E_TNAMES");
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
	 * FIXME this is a temporary method, this field should be configurable as it was in the legacy model
	 * @return
	 */
	protected String getRequiredFieldIndicatior() {
		return "*";
	}

	private List<Control> getOrderedControlList() {
		final List<Control> controls = new ArrayList<Control>(getControls());
		java.util.Collections.sort(controls);
		return controls;
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
		if (formCtrl != null && formCtrl.getContainer() == this) {
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
	
	public String getControlCanonicalUdn(Control ctrl) {
		if (ctrl.getUserDefinedName() == null) {
			throw new RuntimeException("User defined name of control is null. Invalid control state");
		}
		
		Control formCtrl = controlsMap.get(ctrl.getName());
		if (formCtrl != null && formCtrl.getContainer() == this) {
			return formCtrl.getUserDefinedName();
		}
		
		for (Control c : getControls()) {
			if (!(c instanceof SubFormControl)) {
				continue;
			}
			
			SubFormControl sfCtrl = (SubFormControl)c;
			String userDefName = sfCtrl.getSubContainer().getControlCanonicalUdn(ctrl);
			if (userDefName != null) {
				return new StringBuilder(sfCtrl.getUserDefinedName()).append(".").append(userDefName).toString();
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
	
	public Control getControlByUdn(String userDefinedName, String separator) {
		Container container = this;
		Control ctrl = container.getControlByUdn(userDefinedName);
		if (ctrl != null) {
			return ctrl;
		}
		
		String[] udnParts = userDefinedName.split(separator);
		if (udnParts.length == 1) { // no sub form control name
			throw new RuntimeException("Invalid user defined name: " + userDefinedName); 
		}
		
		for (int i = 0; i < udnParts.length - 1; ++i) {
			ctrl = container.getControlByUdn(udnParts[i]);
			if (!(ctrl instanceof SubFormControl)) {
				throw new RuntimeException("Invalid user defined name: " + userDefinedName);
			}
			
			SubFormControl sfCtrl = (SubFormControl)ctrl;
			container = sfCtrl.getSubContainer();			
		}
		
		ctrl = container.getControlByUdn(udnParts[udnParts.length - 1]);
		if (ctrl == null) {
			throw new RuntimeException("Invalid user defined name: " + userDefinedName);
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
	
	public static String getContainerNameById(Long id) {
		return getContainerNameById(JdbcDaoFactory.getJdbcDao(), id);
	}

	public static String getContainerNameById(JdbcDao jdbcDao, Long id) {
		try {
			ContainerDao containerDao = new ContainerDao(jdbcDao);
			return containerDao.getNameById(id);
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining container name with id : " + id);
		}
	}
	
	public Map<String, Object> getProps() {
		Map<String, Object> props = new HashMap<String, Object>();		
		props.put("name", getName());
		props.put("caption", getCaption());
		putControls(props);
		return props;
	}
			
	private void putControls(Map<String, Object> containerProps) {
		List<List<Map<String, Object>>> rows = new ArrayList<List<Map<String, Object>>>();
		containerProps.put("rows", rows);
		
		for (List<Control> rowCtrls : getControlsGroupedByRow()) {
			List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
			for (Control ctrl : rowCtrls) {
				row.add(ctrl.getProps());
			}
			
			rows.add(row);
		}		
	}
}

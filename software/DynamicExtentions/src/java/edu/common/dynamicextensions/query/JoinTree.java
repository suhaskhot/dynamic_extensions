package edu.common.dynamicextensions.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;

public class JoinTree
{
	private String tab;
	
    private String alias;
    
	private String parentKey;
	
	private String foreignKey;
	
	private Container form;
	
	private Control field;
	
	private JoinTree parent;
	
	private boolean innerJoin;
	
	private boolean subForm;
	
	private boolean extensionForm;
	
	private String extnFk;
	
	private Map<String, JoinTree> children = new HashMap<String, JoinTree>();	

    public JoinTree() {
    }

    public JoinTree(String tab, String alias) {
        this.tab = tab;
        this.alias = alias;
    }

    public JoinTree(Container form, String alias) {
        this.form  = form;
        this.alias = alias;
        this.tab   = form.getDbTableName();
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getAlias()  {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getForeignKey()  {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey)  {
        this.foreignKey = foreignKey;
    }

    public Long getFormId() {
        return form.getId();
    }
    
    public String getFormName() {
    	return form.getName();
    }

    public Container getForm() {
        return form;
    }

    public void setForm(Container form) {
        this.form = form;
    }

    public Control getField() {
        return field;
    }

    public void setField(Control field) {
        this.field = field;
    }

    public JoinTree getParent() {
        return parent;
    }

    public void setParent(JoinTree parent) {
        this.parent = parent;
    }

    public boolean isInnerJoin() {
		return innerJoin;
	}

	public void setInnerJoin(boolean innerJoin) {
		this.innerJoin = innerJoin;
	}

	public boolean isSubForm() {
		return subForm;
	}

	public void setSubForm(boolean subForm) {
		this.subForm = subForm;
	}

	public boolean isExtensionForm() {
		return extensionForm;
	}

	public void setExtensionForm(boolean extensionForm) {
		this.extensionForm = extensionForm;
	}

	public String getExtnFk() {
		return extnFk;
	}

	public void setExtnFk(String extnFk) {
		this.extnFk = extnFk;
	}

	public Collection<JoinTree> getChildren() {
        return children.values();
    }

    public JoinTree getChild(String name) {
        return children.get(name);
    }

    public void addChild(String name, JoinTree child)  {
        children.put(name, child);
    }
    
    public void removeChild(JoinTree child) {
    	String key = null;
    	for (Map.Entry<String, JoinTree> childEntry : children.entrySet()) {
    		if (childEntry.getValue() == child) {
    			key = childEntry.getKey();
    			break;
    		}    		
    	}
    	
    	if (key != null) {
    		children.remove(key);
    	}
    }
    
    public void addChildrenOf(JoinTree another) {
    	for (Map.Entry<String, JoinTree> childEntry : another.children.entrySet()) {
    		if (children.containsKey(childEntry.getKey())) {
    			throw new RuntimeException("Bug! Bug!!!");
    		}
    		
    		childEntry.getValue().setParent(this);
    		children.put(childEntry.getKey(), childEntry.getValue());    		
    	}
    }
    
    public JoinTree getNode(String alias) {
    	if (this.getAlias().equals(alias)) {
    		return this;
    	}
    	
    	JoinTree result = null;
    	for (JoinTree child : children.values()) {
    		if (alias.equals(child.getAlias())) {
    			result = child;
    			break;
    		}
    	}
    	
    	if (result != null) {
    		return result;
    	}

    	for (JoinTree child : children.values()) {
    		result = child.getNode(alias);
    		if (result != null) {
    			break;
    		}
    	}
    	
    	return result;    	
    }
    
    public boolean isAncestorOf(JoinTree tree) {
    	boolean yes = false;
    	for (JoinTree child : children.values()) {
    		if (tree == child) {
    			yes = true;
    			break;
    		}
    	}
    	
    	if (yes) {
    		return yes;
    	}
    	
    	for (JoinTree child : children.values()) {
    		if ((yes = child.isAncestorOf(tree))) {
    			break;
    		}
    	}
   		
    	return yes;
    }
    
    public JoinTree getCommonAncestor(JoinTree tree1, JoinTree tree2) {
    	if (tree1.getParent() == tree2.getParent()) {
    		return getNonLinkParentNode(tree1);
    	}
    	
    	JoinTree tree1Parent = getNonLinkParentNode(tree1.getParent());
    	JoinTree tree2Parent = getNonLinkParentNode(tree2.getParent());
    	return getCommonAncestor(tree1Parent, tree2Parent);
    }
    
    public boolean isSubFormOrMultiSelect() {
    	return field != null || subForm;
    }
    
    private JoinTree getNonLinkParentNode(JoinTree joinTree) {
    	if ((joinTree.form == null && joinTree.field == null) || 
    		(joinTree.form != null && joinTree.form.getName().equals("extensions")))  {
    		return getNonLinkParentNode(joinTree.getParent());
    	}
    	
    	return joinTree;
    }
}
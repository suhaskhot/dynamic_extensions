package edu.common.dynamicextensions.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;

public class JoinTree
{
	private String tab;
	
    private String alias;
    
	private String parentKey;
	
	private String foreignKey;
	
	private Container form;
	
	private MultiSelectControl field;
	
	private JoinTree parent;
	
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

    public Container getForm() {
        return form;
    }

    public void setForm(Container form) {
        this.form = form;
    }

    public MultiSelectControl getField() {
        return field;
    }

    public void setField(MultiSelectControl field) {
        this.field = field;
    }

    public JoinTree getParent() {
        return parent;
    }

    public void setParent(JoinTree parent) {
        this.parent = parent;
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
}
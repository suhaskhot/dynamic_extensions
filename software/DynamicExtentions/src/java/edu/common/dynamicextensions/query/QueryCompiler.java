package edu.common.dynamicextensions.query;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class QueryCompiler
{
    private int tabCnt;
    
    private Long rootFormId;
    
    private String query;
    
    private Node queryExpr;
    
    private JoinTree queryJoinTree;
	
    public QueryCompiler(Long rootFormId, String query) {
        this.rootFormId = rootFormId;
        this.query = query;
    }

    public void compile() {
        QueryParser queryParser = new QueryParser(query);
        queryExpr     = queryParser.getExpressionAst();
        queryJoinTree = buildJoinTree(queryExpr);
    }

    public Node getQueryExpr() {
        return queryExpr;
    }

    public JoinTree getQueryJoinTree() {
        return queryJoinTree;
    }

    private Map<Long, JoinTree> analyzeExpr(Node expr) {
    	Map<Long, JoinTree> joinMap = new HashMap<Long, JoinTree>();
        analyzeExpr(expr, joinMap);
        return joinMap;
    }

    private void analyzeExpr(Node expr, Map<Long, JoinTree> joinMap) {
        if(expr instanceof Filter) {
            Filter filter = (Filter)expr;
            analyzeFilter(filter, joinMap);
        } else {
        	Expression subExpr = (Expression)expr;
        	for (Node childExpr : subExpr.getOperands()) {
        		analyzeExpr(childExpr, joinMap);
        	}
        }
    }

    private void analyzeFilter(Filter filter, Map<Long, JoinTree> joinMap) {
        String fieldNameParts[] = filter.getFieldName().split("\\.");        
        
        Long formId = Long.valueOf(Long.parseLong(fieldNameParts[0]));        
        Container form = Container.getContainer(formId);        
        if(form == null) {
            throw new RuntimeException("Invalid filter referring to non-existing form: " + formId);
        }
        
        JoinTree formTree = joinMap.get(formId);
        if(formTree == null) {
            formTree = new JoinTree(form, "t" + tabCnt++);
            joinMap.put(formId, formTree);
        }
        
        Control field = form.getControl(fieldNameParts[1]);
        if(field instanceof SubFormControl && fieldNameParts.length > 2) {
            for(int i = 1; i < fieldNameParts.length - 1; i++) {
                field = form.getControl(fieldNameParts[i]);
                
                if(!(field instanceof SubFormControl)) {
                    throw new RuntimeException("Invalid filter referring to invalid field: " + filter.getFieldName());
                }
                
                SubFormControl sfCtrl = (SubFormControl)field;
                JoinTree sfTree = formTree.getChild(sfCtrl.getName());
                if(sfTree == null) {
                    sfTree = getSubFormTree(formTree, sfCtrl);
                    formTree.addChild(sfCtrl.getName(), sfTree);
                }
                
                formTree = sfTree;
                form = sfCtrl.getSubContainer();
            }

            field = form.getControl(fieldNameParts[fieldNameParts.length - 1]);
        }
        
        if(field == null) {
            throw new RuntimeException("Invalid filter referring to invalid field: " + filter.getFieldName());
        }
        
        String tabAlias = formTree.getAlias();
        if(field instanceof MultiSelectControl) {
            JoinTree fieldTree = formTree.getChild(field.getName());
            if(fieldTree == null) {
                MultiSelectControl msField = (MultiSelectControl)field;
                fieldTree = getFieldTree(formTree, msField);
                formTree.addChild(field.getName(), fieldTree);
            }
            
            tabAlias = fieldTree.getAlias();
        }
        
        filter.setField(field);
        filter.setTabAlias(tabAlias);
    }

    private JoinTree buildJoinTree(Node expr) {
        Map<Long, JoinTree> joinMap = analyzeExpr(expr);
        JoinTree rootTree = joinMap.get(rootFormId);
        
        if(rootTree == null) {
            Container rootForm = Container.getContainer(rootFormId);
            rootTree = new JoinTree(rootForm, "t" + tabCnt++);
        }
        
        for (Map.Entry<Long, JoinTree> formTreeEntry : joinMap.entrySet()) {
        	if (formTreeEntry.getKey() == rootFormId) {
        		continue;
        	}
        	
        	JoinTree childTree = formTreeEntry.getValue();
        	Path path = PathConfig.getInstance().getPath(rootFormId, childTree.getFormId());
        	if (path == null) {
        		throw new RuntimeException("No path between root form " + rootFormId + " and " + childTree.getFormId());
        	}
        	
        	createPath(rootTree, childTree, path);
        }

        return rootTree;
    }

    private void createPath(JoinTree from, JoinTree to, Path path) {
        JoinTree current = createPath(from, path.getStartField());
        for (PathLink link : path.getLinks()) {
            if (link.getRefTab() == null && to.getParent() == null) {
                to.setParent(current);
                to.setForeignKey(link.getRefTabKey());
                to.setParentKey(link.getKey());
                current.addChild(to.getTab(), to);
                break;
            }
            
            JoinTree child = current.getChild(link.getRefTab());
            if(child == null) {
                child = new JoinTree(link.getRefTab(), "t" + tabCnt++);
                child.setParent(current);
                child.setForeignKey(link.getRefTabKey());
                child.setParentKey(link.getKey());
                current.addChild(child.getTab(), child);
            }
            current = child;        	
        }
    }

    private JoinTree createPath(JoinTree formTree, String startField) {
        if(startField == null) {
            return formTree;
        }
        
        String fieldNameParts[] = startField.split("\\.");
        for(int i = 0; i < fieldNameParts.length; i++) {
            JoinTree child = formTree.getChild(fieldNameParts[i]);
            if(child == null) {
                SubFormControl sfCtrl = (SubFormControl)formTree.getForm().getControl(fieldNameParts[i]);
                JoinTree sfTree = getSubFormTree(formTree, sfCtrl);
                formTree.addChild(fieldNameParts[i], sfTree);
                formTree = sfTree;
            }
        }

        return formTree;
    }

    private JoinTree getSubFormTree(JoinTree parentNode, SubFormControl sfCtrl) {
        JoinTree sfTree = new JoinTree();
        sfTree.setForm(sfCtrl.getSubContainer());
        sfTree.setTab(sfCtrl.getSubContainer().getDbTableName());
        sfTree.setParent(parentNode);
        sfTree.setAlias((new StringBuilder()).append("t").append(tabCnt++).toString());
        sfTree.setParentKey(sfCtrl.getParentKey());
        sfTree.setForeignKey(sfCtrl.getForeignKey());
        return sfTree;
    }

    private JoinTree getFieldTree(JoinTree parentNode, MultiSelectControl field) {
        JoinTree fieldTree = new JoinTree();
        fieldTree.setField(field);
        fieldTree.setTab(field.getTableName());
        fieldTree.setParent(parentNode);
        fieldTree.setAlias((new StringBuilder()).append("t").append(tabCnt++).toString());
        fieldTree.setParentKey(field.getParentKey());
        fieldTree.setForeignKey(field.getForeignKey());
        return fieldTree;
    }
}
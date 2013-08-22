package edu.common.dynamicextensions.query;

import java.util.HashMap;
import java.util.Map;

//import edu.QueryTester;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode;
import edu.common.dynamicextensions.query.ast.FilterExpressionNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.FilterNode;
import edu.common.dynamicextensions.query.ast.FilterNodeMarker;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public class QueryCompiler
{
    private int tabCnt;
    
    private Long rootFormId;
    
    private String query;
    
    private QueryExpressionNode queryExpr;
    
    private JoinTree queryJoinTree;
	
    public QueryCompiler(Long rootFormId, String query) {
        this.rootFormId = rootFormId;
        this.query = query;
    }

    public void compile() {
        QueryParser queryParser = new QueryParser(query);
        queryExpr     = queryParser.getQueryAst();
        queryJoinTree = buildJoinTree(queryExpr);
    }

    public QueryExpressionNode getQueryExpr() {
        return queryExpr;
    }

    public JoinTree getQueryJoinTree() {
        return queryJoinTree;
    }

    private JoinTree buildJoinTree(QueryExpressionNode queryExpr) {
        Map<Long, JoinTree> joinMap = analyzeExpr(queryExpr);
        JoinTree rootTree = joinMap.get(rootFormId);
        
        if (rootTree == null) {
            Container rootForm = Container.getContainer(rootFormId);
        	//Container rootForm = QueryTester.getContainer(rootFormId);
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
    
    private Map<Long, JoinTree> analyzeExpr(QueryExpressionNode expr) {
    	Map<Long, JoinTree> joinMap = new HashMap<Long, JoinTree>();
    	
    	analyzeFilterNodeMarker(expr.getFilterExpr(), joinMap);
    	
    	for (ExpressionNode element : expr.getSelectList().getElements()) {
    		analyzeExpressionNode(element, joinMap);
    	}    	
        return joinMap;
    }

    private void analyzeFilterNodeMarker(FilterNodeMarker expr, Map<Long, JoinTree> joinMap) {
        if (expr instanceof FilterNode) {
            FilterNode filter = (FilterNode)expr;
            analyzeFilterNode(filter, joinMap);
        } else {
        	FilterExpressionNode subExpr = (FilterExpressionNode)expr;
        	for (FilterNodeMarker childExpr : subExpr.getOperands()) {
        		analyzeFilterNodeMarker(childExpr, joinMap);
        	}
        }
    }

    private void analyzeFilterNode(FilterNode filter, Map<Long, JoinTree> joinMap) {
    	analyzeExpressionNode(filter.getLhs(), joinMap);
    	analyzeExpressionNode(filter.getRhs(), joinMap);    	
    }
    
    private void analyzeArithExpressionNode(ArithExpressionNode expr, Map<Long, JoinTree> joinMap) {
    	analyzeExpressionNode(expr.getLeftOperand(), joinMap);
    	analyzeExpressionNode(expr.getRightOperand(), joinMap);    	
    }
    
    private void analyzeDateDiffFuncNode(DateDiffFuncNode dateDiff, Map<Long, JoinTree> joinMap) {
    	analyzeExpressionNode(dateDiff.getLeftOperand(), joinMap);
    	analyzeExpressionNode(dateDiff.getRightOperand(), joinMap);
    }

    private void analyzeExpressionNode(ExpressionNode exprNode, Map<Long, JoinTree> joinMap) {
		if (exprNode instanceof FieldNode) {
			analyzeField((FieldNode)exprNode, joinMap);
		} else if (exprNode instanceof ArithExpressionNode) {
			analyzeArithExpressionNode((ArithExpressionNode)exprNode, joinMap);
		} else if (exprNode instanceof DateDiffFuncNode) {
			analyzeDateDiffFuncNode((DateDiffFuncNode)exprNode, joinMap);
		}    	
    }
    
    
    private void analyzeField(FieldNode field, Map<Long, JoinTree> joinMap) {
        String fieldNameParts[] = field.getName().split("\\.");        
        
        Long formId = Long.valueOf(Long.parseLong(fieldNameParts[0]));
        JoinTree formTree = joinMap.get(formId);

        Container form = null;        
        if(formTree == null) {
            form = Container.getContainer(formId);
        	//form = QueryTester.getContainer(formId);
            if(form == null) {
                throw new RuntimeException("Invalid field " + field.getName() + " referring to non-existing form: " + formId);
            }
                    	
            formTree = new JoinTree(form, "t" + tabCnt++);
            joinMap.put(formId, formTree);
        } else {
        	form = formTree.getForm();
        }
                
        Control ctrl = form.getControl(fieldNameParts[1]);
        if(ctrl instanceof SubFormControl && fieldNameParts.length > 2) {
            for(int i = 1; i < fieldNameParts.length - 1; i++) {
                ctrl = form.getControl(fieldNameParts[i]);
                
                if(!(ctrl instanceof SubFormControl)) {
                    throw new RuntimeException("Invalid filter referring to invalid field: " + field.getName());
                }
                
                SubFormControl sfCtrl = (SubFormControl)ctrl;
                JoinTree sfTree = formTree.getChild(sfCtrl.getName());
                if(sfTree == null) {
                    sfTree = getSubFormTree(formTree, sfCtrl);
                    formTree.addChild(sfCtrl.getName(), sfTree);
                }
                
                formTree = sfTree;
                form = sfCtrl.getSubContainer();
            }

            ctrl = form.getControl(fieldNameParts[fieldNameParts.length - 1]);
        }
        
        if(ctrl == null) {
            throw new RuntimeException("Invalid filter referring to invalid field: " + field.getName());
        }
        
        String tabAlias = formTree.getAlias();
        if (ctrl instanceof MultiSelectControl) {
            JoinTree fieldTree = formTree.getChild(ctrl.getName());
            if (fieldTree == null) {
                MultiSelectControl msField = (MultiSelectControl)ctrl;
                fieldTree = getFieldTree(formTree, msField);
                formTree.addChild(ctrl.getName(), fieldTree);
            }
            
            tabAlias = fieldTree.getAlias();
        }
        
        field.setCtrl(ctrl);
        field.setTabAlias(tabAlias);
    }    
}

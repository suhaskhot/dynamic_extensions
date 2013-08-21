package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.query.Filter.RelationalOp;

public class QueryGenerator {
	
	private static String LIMIT_QUERY = "select * from (select rownum rnum, tab.* from (%s) tab where rownum < %d) where rnum >= %d";

    public QueryGenerator() {
    }

    public String getCountSql(QueryExpr queryExpr, JoinTree joinTree) {
        String fromClause  = buildFromClause(joinTree);
        String whereClause = buildWhereClause(queryExpr.getExpr());
        
        return new StringBuilder("select count(*) from ")
        	.append(fromClause)
        	.append(" where ").append(whereClause)
        	.toString();
    }

    public String getDataSql(QueryExpr queryExpr, JoinTree joinTree) {
    	String selectClause = buildSelectClause(queryExpr.getSelectList());
        String fromClause  = buildFromClause(joinTree);
        String whereClause = buildWhereClause(queryExpr.getExpr());
        
        return new StringBuilder("select ").append(selectClause)
        	.append(" from ").append(fromClause)
        	.append(" where ").append(whereClause)
        	.toString();
    }

    public String getDataSql(QueryExpr queryExpr, JoinTree joinTree, int start, int numRows) {
        String dataSql = getDataSql(queryExpr, joinTree);

        String result = null;
        if(start == 0 && numRows == 0) {
            result = dataSql;
        } else {
            String orderedQuery = new StringBuilder(dataSql)
            	.append(" order by ")
            	.append(joinTree.getAlias()).append(".IDENTIFIER")
            	.toString();
            
            result = String.format(LIMIT_QUERY, orderedQuery, start + numRows, start);
        }
        
        return result;
    }

    private String buildSelectClause(SelectList selectList) {
    	StringBuilder select = new StringBuilder();
    	for (ConditionOperand element : selectList.getElements()) { 
    		select.append(getCondOperandExpr(element, element.getType())).append(", ");
    	}
    	
    	select.delete(select.length() - 2, select.length());
    	return select.toString();
    }
    
    private String buildFromClause(JoinTree joinTree) {
        StringBuilder from = new StringBuilder();
        JoinTree parentTree = joinTree.getParent();
        
        if(parentTree != null) {
            from.append(" left join ");
        }        
        from.append(joinTree.getTab()).append(" ").append(joinTree.getAlias()).append(" ");
        
        if(parentTree != null) {
            from.append(" on ").append(joinTree.getAlias()).append(".").append(joinTree.getForeignKey())
                .append(" = ").append(parentTree.getAlias()).append(".").append(joinTree.getParentKey());
        }
        
        for (JoinTree child : joinTree.getChildren()) {
        	from.append(buildFromClause(child));
        }

        return from.toString();
    }

    private String buildWhereClause(Node root) {
        String exprStr = null;
        
        if (root instanceof Filter) {
            return buildFilter((Filter)root);
        } 
        
        Expression expr = (Expression)root;
        String lhs = buildWhereClause(expr.getOperands().get(0));
        String rhs = null;
            
        switch (expr.getOperator()) {
          	case AND:
           	case OR:
           		rhs = buildWhereClause(expr.getOperands().get(1));
           		exprStr = new StringBuilder(lhs).append(" ")
           				.append(expr.getOperator()).append(" ")
           				.append(rhs).toString();
                break;
                    
            case NOT:
            	exprStr = new StringBuilder("NOT (").append(lhs).append(")").toString();
            	break;
            		
            case PARENTHESIS:
            	exprStr = new StringBuilder("(").append(lhs).append(")").toString();
            	break;
            		
            case PAND:
            	break;
        }

        return exprStr;
    }

    private String buildFilter(Filter filter) {
        if(!isValidOp(filter.getLhs().getType(), filter.getRelOp(), filter.getRhs().getType())) {
            throw new RuntimeException("Invalid operator: " + filter.getRelOp() + " used");
        }
        
        String lhs = getCondOperandExpr(filter.getLhs(), filter.getLhs().getType());
        String rhs = getCondOperandExpr(filter.getRhs(), filter.getLhs().getType());        
        return lhs + " " + filter.getRelOp().symbol() + " " + rhs;
    }

    private boolean isValidOp(DataType lhsType, RelationalOp op, DataType rhsType) {
        return true;
    }

    private String removeQuotes(String value) {
        if (value != null && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            value = value.substring(1, value.length() - 1);
        }
        
        return value;
    }
    
    private String getCondOperandExpr(ConditionOperand condOperand, DataType type) {
    	String result = "";
    	
    	if (condOperand instanceof Field) {
    		Field field = (Field)condOperand;
    		result = field.getTabAlias() + "." + field.getCtrl().getDbColumnName();
    	} else if (condOperand instanceof Value) {
    		Value value = (Value)condOperand;
            switch (value.getType()) {
        		case STRING:
        			if (type == DataType.DATE) {
        				result = "to_date('" + removeQuotes(value.getValues().get(0).toString()) + "', 'MM-DD-YYYY')";
        			} else {
        				result = "'" + removeQuotes(value.getValues().get(0).toString()) + "'";
        			}        		
        			break;
        		
        		default:
        			result = value.getValues().get(0).toString();
        			break;
            }
    	} else if (condOperand instanceof ArithExpression) {
    		ArithExpression arithExpr = (ArithExpression)condOperand;    		
    		String loperand = getCondOperandExpr(arithExpr.getLeftOperand(),  arithExpr.getLeftOperandCoercion());
    		String roperand = getCondOperandExpr(arithExpr.getRightOperand(), arithExpr.getRightOperandCoercion());
    		result = "(" + loperand + " " + arithExpr.getOp().symbol() + " " + roperand + ")";
    	}
    	
    	return result;
    }
}
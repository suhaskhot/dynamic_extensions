package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.query.Filter.RelationalOp;

public class QueryGenerator {
	
	private static String LIMIT_QUERY = "select * from (select rownum rnum, tab.* from (%s) tab where rownum < %d) where rnum >= %d";

    public QueryGenerator() {
    }

    public String getCountSql(Node expr, JoinTree joinTree) {
        String fromClause  = buildFromClause(joinTree);
        String whereClause = buildWhereClause(expr);
        
        return new StringBuilder("select count(*) from ")
        	.append(fromClause)
        	.append(" where ").append(whereClause)
        	.toString();
    }

    public String getDataSql(Node expr, JoinTree joinTree) {
        String fromClause  = buildFromClause(joinTree);
        String whereClause = buildWhereClause(expr);
        
        return new StringBuilder("select * from ")
        	.append(fromClause)
        	.append(" where ").append(whereClause)
        	.toString();
    }

    public String getDataSql(Node expr, JoinTree joinTree, int start, int numRows) {
        String dataSql = getDataSql(expr, joinTree);

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
        
        if(root instanceof Filter) {
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
        Control field = filter.getField();
        DataType fieldType = getFieldType(field);
        
        if(!isValidOp(fieldType, filter.getRelOp())) {
            throw new RuntimeException("Invalid operator: " + filter.getRelOp() + " used for " + filter.getFieldName());
        }
        
        String lhs = filter.getTabAlias() + "." + field.getDbColumnName();
        String rhs = "";
        
        switch (fieldType) {
            case STRING:
            	rhs = "'" + removeQuotes(filter.getValues().get(0)) + "'";
            	break;
            
            default:
            	rhs = filter.getValues().get(0);
            	break;            
        }
        
        return lhs + " " + filter.getRelOp().symbol() + " " + rhs;
    }

    private boolean isValidOp(DataType dataType, RelationalOp op) {
        return true;
    }

    private String removeQuotes(String value) {
        if (value != null && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            value = value.substring(1, value.length() - 1);
        }
        
        return value;
    }

    private DataType getFieldType(Control ctrl) {
    	DataType result = null;
    	
        if (ctrl instanceof SelectControl) {
            result = ctrl.getDataType();
        } else if (ctrl instanceof StringTextField || ctrl instanceof TextArea) {
            result = DataType.STRING;
        } else if (ctrl instanceof NumberField) {
            result = DataType.INTEGER;
        } else if (ctrl instanceof DatePicker) {
            result = DataType.DATE;
        } else if (ctrl instanceof CheckBox) {
            result = DataType.BOOLEAN;
        }
        
        return result;
    }
}
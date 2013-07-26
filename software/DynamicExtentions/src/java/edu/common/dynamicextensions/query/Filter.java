package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Control;

public class Filter implements Node {
    public static enum RelationalOp {
        EQ("="),
        LT("<"),
        LE("<="),
        GT(">"),
        GE(">="),
        NE("!=");
        
        private String symbol;
        
        private RelationalOp(String symbol) {
            this.symbol = symbol;  
        }
        
        public String symbol() {
            return symbol;
        }
        
        public static RelationalOp getBySymbol(String symbol) {
            RelationalOp result = null;
            for (RelationalOp op : RelationalOp.values()) {
                if (op.symbol.equals(symbol)) {
                    result = op;
                    break;
                }
            }
            
            return result;
        }                
    }
    
    private String fieldName;
    
    private RelationalOp relOp;
    
    private List<String> values = new ArrayList<String>();
    
    private Control field;
    
    private String tabAlias;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public RelationalOp getRelOp() {
        return relOp;
    }

    public void setRelOp(RelationalOp relOp) {
        this.relOp = relOp;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Control getField() {
        return field;
    }

    public void setField(Control field) {
        this.field = field;
    }

    public String getTabAlias() {
        return tabAlias;
    }

    public void setTabAlias(String tabAlias) {
        this.tabAlias = tabAlias;
    }
}

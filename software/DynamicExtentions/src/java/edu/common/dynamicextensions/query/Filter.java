package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

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
    
    private Field field = new Field();
    
    private RelationalOp relOp;
    
    private List<String> values = new ArrayList<String>();
    
    public Field getField() {
    	return field;
    }
    
    public void setField(Field field) {
    	this.field = field;
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
}

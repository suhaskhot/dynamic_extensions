package edu.common.dynamicextensions.query.ast;


public class FilterNode implements FilterNodeMarker {
    public static enum RelationalOp {
        EQ("="),
        LT("<"),
        LE("<="),
        GT(">"),
        GE(">="),
        NE("!="),
        IN("in"),
        NOT_IN("not in"),
        STARTS_WITH("starts with"),
        ENDS_WITH("ends with"),
        CONTAINS("contains"),
        EXISTS("exists"),
        NOT_EXISTS("not exists");
        
        
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
    
    private ExpressionNode lhs;
   
	private RelationalOp relOp;
    
    private ExpressionNode rhs;
   
    public ExpressionNode getLhs() {
		return lhs;
	}

	public void setLhs(ExpressionNode lhs) {
		this.lhs = lhs;
	}

	public RelationalOp getRelOp() {
		return relOp;
	}

	public void setRelOp(RelationalOp relOp) {
		this.relOp = relOp;
	}

	public ExpressionNode getRhs() {
		return rhs;
	}

	public void setRhs(ExpressionNode rhs) {
		this.rhs = rhs;
	}    
}

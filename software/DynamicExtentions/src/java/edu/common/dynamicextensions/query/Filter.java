package edu.common.dynamicextensions.query;

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
    
    private ConditionOperand lhs;
   
	private RelationalOp relOp;
    
    private ConditionOperand rhs;
   
    public ConditionOperand getLhs() {
		return lhs;
	}

	public void setLhs(ConditionOperand lhs) {
		this.lhs = lhs;
	}

	public RelationalOp getRelOp() {
		return relOp;
	}

	public void setRelOp(RelationalOp relOp) {
		this.relOp = relOp;
	}

	public ConditionOperand getRhs() {
		return rhs;
	}

	public void setRhs(ConditionOperand rhs) {
		this.rhs = rhs;
	}    
}

package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.DataType;

public class ArithExpression extends ConditionOperand {
	public static enum ArithOp {
		PLUS("+"), MINUS("-"), MUL("*"), DIV("/");
		
        private String symbol;
        
        private ArithOp(String symbol) {
            this.symbol = symbol;  
        }
        
        public String symbol() {
            return symbol;
        }
        
        public static ArithOp getBySymbol(String symbol) {
            ArithOp result = null;
            for (ArithOp op : ArithOp.values()) {
                if (op.symbol.equals(symbol)) {
                    result = op;
                    break;
                }
            }
            
            return result;
        }                
	};
	
	private ConditionOperand leftOperand;
	
	private ConditionOperand rightOperand;
	
	private ArithOp op;

	public ConditionOperand getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(ConditionOperand leftOperand) {
		this.leftOperand = leftOperand;
	}

	public ConditionOperand getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(ConditionOperand rightOperand) {
		this.rightOperand = rightOperand;
	}

	public ArithOp getOp() {
		return op;
	}

	public void setOp(ArithOp op) {
		this.op = op;
	}

	@Override
	public DataType getType() {
		DataType result = null;
		if (leftOperand.isNumber() && rightOperand.isNumber()) {
			if (leftOperand.getType() == DataType.FLOAT || rightOperand.getType() == DataType.FLOAT) {
				result = DataType.FLOAT;
			} else {
				result = DataType.INTEGER;
			}
		} else if (op == ArithOp.PLUS && leftOperand.isNumber() && rightOperand.isDate()) {
			result = DataType.DATE;
		} else if (op == ArithOp.PLUS && leftOperand.isDate() && rightOperand.isNumber()) {
			result = DataType.DATE;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isNumber()) {
			result = DataType.DATE;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isDate()) {
			result = DataType.FLOAT;
		} else if (op == ArithOp.MINUS && leftOperand.isDate() && rightOperand.isString()) {
			result = DataType.FLOAT;
		} else if (op == ArithOp.MINUS && leftOperand.isString() && rightOperand.isDate()) {
			result = DataType.FLOAT;
		}

		return result;
	}		
	
	public DataType getLeftOperandCoercion() {
		return leftOperand.isString() && rightOperand.isDate() ? DataType.DATE : null;
	}
	
	public DataType getRightOperandCoercion() {
		return leftOperand.isDate() && rightOperand.isString() ? DataType.DATE : null; 
	}
}

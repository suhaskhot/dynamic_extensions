package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.nui.DataType;

public class CountNode extends ExpressionNode implements Serializable {	
	private static final long serialVersionUID = 2686666253832434643L;

	private boolean distinct;
	
	private FieldNode field;
	
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public FieldNode getField() {
		return field;
	}

	public void setField(FieldNode field) {
		this.field = field;
	}

	@Override
	public DataType getType() {		
		return DataType.INTEGER;
	}

	@Override
	public ExpressionNode copy() {
		CountNode copy = new CountNode();
		copy.setDistinct(distinct);
		copy.setField(field.copy());
		copy.setLabel(this.getLabel());		
		return copy;
	}

	@Override
	public String[] getFormNames() {
		return field.getFormNames();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (distinct ? 1231 : 1237);
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
			
		CountNode other = (CountNode) obj;
		if (distinct != other.distinct) {
			return false;
		} 
		
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isPhi() {
		return field != null && field.isPhi();
	}	
}

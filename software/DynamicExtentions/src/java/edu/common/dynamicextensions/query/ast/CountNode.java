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
}

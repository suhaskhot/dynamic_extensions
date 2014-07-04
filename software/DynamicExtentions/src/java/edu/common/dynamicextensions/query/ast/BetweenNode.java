package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.DataType;

public class BetweenNode extends ExpressionNode implements Serializable {
	private FieldNode lhs;

	private ExpressionNode minNode;

	private ExpressionNode maxNode;

	@Override
	public DataType getType() {
		return lhs.getType();
	}

	@Override
	public ExpressionNode copy() {
		BetweenNode copy = new BetweenNode();
		copy.setLhs(lhs.copy());
		copy.setMinNode(minNode.copy());
		copy.setMaxNode(maxNode.copy());
		return copy;
	}

	@Override
	public String[] getFormNames() {
		Set<String> formNames = new HashSet<String>();
		formNames.addAll(Arrays.asList(lhs.getFormNames()));
		formNames.addAll(Arrays.asList(minNode.getFormNames()));
		formNames.addAll(Arrays.asList(maxNode.getFormNames()));
		return formNames.toArray(new String[0]);
	}

	public FieldNode getLhs() {
		return lhs;
	}

	public void setLhs(FieldNode lhs) {
		this.lhs = lhs;
	}

	public ExpressionNode getMinNode() {
		return minNode;
	}

	public void setMinNode(ExpressionNode minNode) {
		this.minNode = minNode;
	}

	public ExpressionNode getMaxNode() {
		return maxNode;
	}

	public void setMaxNode(ExpressionNode maxNode) {
		this.maxNode = maxNode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BetweenNode that = (BetweenNode) o;

		if (!lhs.equals(that.lhs)) return false;
		if (!maxNode.equals(that.maxNode)) return false;
		if (!minNode.equals(that.minNode)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = lhs.hashCode();
		result = 31 * result + minNode.hashCode();
		result = 31 * result + maxNode.hashCode();
		return result;
	}
}

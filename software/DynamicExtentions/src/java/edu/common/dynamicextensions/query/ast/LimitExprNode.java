package edu.common.dynamicextensions.query.ast;

public class LimitExprNode implements Node {
	private int startAt;
	
	private int numRecords;

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
}

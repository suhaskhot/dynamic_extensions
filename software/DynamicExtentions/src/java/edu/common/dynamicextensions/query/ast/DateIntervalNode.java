package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateIntervalNode extends ExpressionNode {
	private int years, months, days;
	
	@Override
	public DataType getType() {
		return DataType.DATE_INTERVAL;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
}

package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateInterval extends ConditionOperand {
	private int years;
	
	private int months;
	
	private int days;
	
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

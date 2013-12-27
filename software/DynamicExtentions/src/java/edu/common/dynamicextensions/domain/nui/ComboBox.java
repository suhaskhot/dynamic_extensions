
package edu.common.dynamicextensions.domain.nui;

public class ComboBox extends SelectControl {

	private static final long serialVersionUID = 6117463441002075089L;

	private boolean lazyPvFetchingEnabled;

	private int noOfColumns;

	private int minQueryChar;

	public boolean isLazyPvFetchingEnabled() {
		return lazyPvFetchingEnabled;
	}

	public void setLazyPvFetchingEnabled(boolean lazyPvFetchingEnabled) {
		this.lazyPvFetchingEnabled = lazyPvFetchingEnabled;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}
	
	public int getMinQueryChars() {
		return minQueryChar;
	}
	
	public void setMinQueryChars(int minQueryChar) {
		this.minQueryChar = minQueryChar;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (lazyPvFetchingEnabled ? 1231 : 1237);
		result = prime * result + noOfColumns;
		result = prime * result + minQueryChar;		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}

		ComboBox other = (ComboBox) obj;
		if (lazyPvFetchingEnabled != other.lazyPvFetchingEnabled ||
			noOfColumns != other.noOfColumns ||
			minQueryChar != other.minQueryChar) {
			return false;
		}
		
		return true;
	}
}

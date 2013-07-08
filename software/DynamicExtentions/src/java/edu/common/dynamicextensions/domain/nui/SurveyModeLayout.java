package edu.common.dynamicextensions.domain.nui;

import java.util.LinkedHashSet;
import java.util.Set;

public class SurveyModeLayout extends Layout {

	private static final long serialVersionUID = -6653457543982772923L;
	
	private Set<Page> pages = new LinkedHashSet<Page>();

	public Set<Page> getPages() {
		return pages;
	}

	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}
}

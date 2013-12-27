package edu.common.dynamicextensions.domain.nui;

import java.util.LinkedHashSet;
import java.util.Set;

public class SkipLogic {
	
	private Long id;

	private Set<SkipRule> skipRules = new LinkedHashSet<SkipRule>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Set<SkipRule> getSkipRules() {
		return skipRules;
	}

	public void setSkipRules(Set<SkipRule> skipRules) {
		this.skipRules = skipRules;
	}
}

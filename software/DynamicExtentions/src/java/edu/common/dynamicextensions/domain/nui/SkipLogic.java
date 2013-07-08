package edu.common.dynamicextensions.domain.nui;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;

public class SkipLogic extends DynamicExtensionBaseDomainObject {

	private static final long serialVersionUID = -7591132951726233943L;
	
	private Set<SkipRule> skipRules = new LinkedHashSet<SkipRule>();
	
	@Override
	public Long getId() {
		return id;
	}

	public Set<SkipRule> getSkipRules() {
		return skipRules;
	}

	public void setSkipRules(Set<SkipRule> skipRules) {
		this.skipRules = skipRules;
	}
}

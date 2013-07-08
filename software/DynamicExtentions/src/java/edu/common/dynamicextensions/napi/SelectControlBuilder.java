package edu.common.dynamicextensions.napi;

import java.util.Collection;

import edu.common.dynamicextensions.domain.PermissibleValue;

public interface SelectControlBuilder extends ControlBuilder {
	SelectControlBuilder permissibleValues(Collection<PermissibleValue> pvs);
}

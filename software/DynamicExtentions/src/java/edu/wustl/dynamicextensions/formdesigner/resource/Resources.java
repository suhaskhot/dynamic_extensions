
package edu.wustl.dynamicextensions.formdesigner.resource;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class Resources extends Application
{

	@Override
	public Set<Class<?>> getClasses()
	{
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(Form.class);
		//classes.add(PersistentForm.class);
		return classes;
	}
}

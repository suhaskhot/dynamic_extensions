
package edu.wustl.dynamicextensions.formdesigner.usercontext;

public class CSDProperties {

	private static CSDProperties instance = null;

	private AppUserContextProvider userContextProvider;

	protected CSDProperties() {
		// Exists only to defeat instantiation.
	}

	public static CSDProperties getInstance() {
		if (instance == null) {
			instance = new CSDProperties();
		}
		return instance;
	}

	public AppUserContextProvider getUserContextProvider() {
		return userContextProvider;
	}

	public void setUserContextProvider(AppUserContextProvider userContextProvider) {
		this.userContextProvider = userContextProvider;
	}

}

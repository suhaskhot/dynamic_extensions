package edu.common.dynamicextensions.nutility;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.napi.FormPublishHook;

public class FormPublishHookMgr {

	private static final FormPublishHookMgr instance = new FormPublishHookMgr();
	
	private List<FormPublishHook> postPublishHooks = new ArrayList<FormPublishHook>();
	
	public static FormPublishHookMgr getInstance() {
		return instance;
	}
	
	public void registerPostPublishHook(FormPublishHook hook) {    
	        postPublishHooks.add(hook);
	}
	
	public List<FormPublishHook> getPostPublishHooks() {
		return postPublishHooks;
	}
}

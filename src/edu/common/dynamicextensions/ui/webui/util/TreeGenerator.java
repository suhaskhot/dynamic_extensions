/*
 * Created on Oct 4, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

import java.util.Iterator;
import java.util.List;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TreeGenerator {

	private String contextPath = null;
    
	/**
     * 
	 * @return
	 */
	public String getContextPath() {
		return this.contextPath;
	}
    /**
     * 
     * @param contextPath
     */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
    /**
     * 
     * @return
     */
	public TreeData getTreeData(String rootName,List childList)
	{
		TreeData treedata = new TreeData();
		treedata.setImagesUrl(this.getContextPath() + "/images");
		System.out.println("Images url  path = " + treedata.getImagesUrl());
		TNode node = new TNode(rootName);
        Iterator childIterator = childList.iterator();
        String child;
        while(childIterator.hasNext())
        {
            child = (String)childIterator.next();
            node.add(child);
        }
	
		
		/*node.add("subnode - 7");
		node.add("subnode - 8");
		TNode node2 = new TNode("Node-2");
		node2.add("subnode2 - 1");
		node2.add("subnode2 - 2");
		node2.add("subnode2 - 3");
		node2.add("subnode2 - 4");
		node2.add("subnode2 - 5");
		node2.add("subnode2 - 6");
		node2.add("subnode2 - 7");
		node2.add("subnode2 - 8");
		treedata.add(node2);*/
		
		treedata.add(node);
		return treedata;
	}
}

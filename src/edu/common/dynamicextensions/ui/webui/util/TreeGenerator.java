/*
 * Created on Oct 4, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TreeGenerator {

	private String contextPath = null;
	
	public String getContextPath() {
		return this.contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public TreeData getTreeData()
	{
		TreeData treedata = new TreeData();
		treedata.setImagesUrl(this.getContextPath() + "/images");
		System.out.println("Images url  path = " + treedata.getImagesUrl());
		TNode node = new TNode("Entity 1");
		node.add("Attribute 1");
		node.add("Attribute 2");
		node.add("Attribute 3");
		node.add("Attribute 4");
		node.add("Attribute 5");
		
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

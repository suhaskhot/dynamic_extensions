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

	public TreeData getTreeData()
	{
		TreeData treedata = new TreeData();
		TNode node = new TNode("Node-1");
		node.add("subnode - 1");
		node.add("subnode - 2");
		node.add("subnode - 3");
		node.add("subnode - 4");
		node.add("subnode - 5");
		node.add("subnode - 6");
		node.add("subnode - 7");
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
		treedata.add(node);
		treedata.add(node2);
		return treedata;
	}
}

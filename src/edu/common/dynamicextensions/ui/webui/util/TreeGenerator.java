/*
 * Created on Oct 4, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.util;

import java.util.List;

import edu.wustl.common.beans.NameValueBean;

/**
 * @author preeti_munot
 *
 */
public class TreeGenerator
{

	private String contextPath = null;

	/**
	 * 
	 * @return context path
	 */
	public String getContextPath()
	{
		return this.contextPath;
	}

	/**
	 * 
	 * @param contextPath  : Context path
	 */
	public void setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
	}

	/**
	 * @param rootName : name of the root node
	 * @param childList : List of child nodes
	 * @return TreeData object for the given root  and list of child nodes
	 */
	public TreeData getTreeData(String rootName, List childList)
	{
		TreeData treedata = new TreeData();
		/*treedata.setImagesUrl(this.getContextPath() + "/images");*/
		treedata.setImagesUrl("images/");
		
		TNode node = new TNode(rootName, 0);
		String name = null;
		String sequenceNumber = null;
		int seqno = 0;
		if (childList != null)
		{
			int noOfChildren = childList.size();
			for (int i = 0; i < noOfChildren; i++)
			{
				NameValueBean childElt = (NameValueBean) childList.get(i);
				if (childElt != null)
				{
					name = childElt.getName();
					sequenceNumber = childElt.getValue();
					seqno = Integer.parseInt(sequenceNumber);
					node.add(name, seqno);
				}
			}
		}

		treedata.add(node);
		return treedata;
	}
}

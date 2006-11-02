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
		TNode node = new TNode(rootName,0);
		String name = null;
		String sequenceNumber = null;
		int seqno = 0;
		if(childList!=null)
		{
			int noOfChildren = childList.size();
			for(int i=0;i<noOfChildren;i++)
			{
				NameValueBean childElt = (NameValueBean)childList.get(i);
				if(childElt!=null)
				{
					name = childElt.getName();
					sequenceNumber = childElt.getValue();
					try
					{
						seqno = Integer.parseInt(sequenceNumber);
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
						seqno = 0;
					}
					node.add(name, seqno);
				}
			}
		}
      
		treedata.add(node);
		return treedata;
	}
}

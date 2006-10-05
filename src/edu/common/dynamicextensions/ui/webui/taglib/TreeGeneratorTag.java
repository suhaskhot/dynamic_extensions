/*
 * Created on Oct 4, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.common.dynamicextensions.ui.webui.util.TreeData;
import edu.common.dynamicextensions.ui.webui.util.TreeGenerator;



/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TreeGeneratorTag extends TagSupport {

	private TreeData treeDataObject = null;
	
	public TreeGeneratorTag()
	{
		super();
	}
	
	public int doStartTag() throws JspException {
		JspWriter jspWriter = pageContext.getOut();
		try {
			if(treeDataObject!=null)
			{
				jspWriter.print("<div  valign='top' scroll='auto' style='overflow:auto;' >");
				jspWriter.print(treeDataObject.getTree());
				jspWriter.print("</div>");
			}
			else
			{
				jspWriter.print("Tree Data obect null Please check");
			}
		} catch (IOException e) {
			System.out.println("Exception occured " +e);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}


	public TreeData getTreeDataObject() {
		return this.treeDataObject;
	}


	public void setTreeDataObject(TreeData treeDataObject) {
		this.treeDataObject = treeDataObject;
	}
	
}

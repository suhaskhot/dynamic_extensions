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
				//Add hidden variable
				jspWriter.print("<input type='hidden' name='selectedAttrib' value=''>");
				//Add the actual code for tree generation
				jspWriter.print("<div  valign='top' scroll='auto' style='overflow:auto;' >");
				jspWriter.print(treeDataObject.getTree());
				jspWriter.print("</div>");

				//Add Javascript code
				String toggleFunctionJSCode=
					"function toggle(id,p) \n" + 
					"{ " + 
						"selId =document.getElementById('selectedAttrib').value; \n" +
						"if(selId!='')\n" +
							"document.getElementById(selId).style.fontWeight='normal';\n" +
						"document.getElementById('selectedAttrib').value='';\n" +
						"var myChild = document.getElementById(id);\n" +
						"if((myChild!=null)&&(myChild!=undefined))\n" +
						"{\n" +
							"if(myChild.style.display!='block')\n" +
							"{\n" +
								"myChild.style.display='block';\n" +
								"document.getElementById(p).className='folderOpen';\n" +
							"}\n" +
							"else\n" +
							"{\n" +
								"myChild.style.display='none';\n" +
								"document.getElementById(p).className='folder';\n" +
							"}\n" +
						"}\n" +
					"}";

				String changeSelectionJSCode =
					"function changeSelection(str1,seqno)\n" +
					"{	\n" +
						
						"document.getElementById('controlOperation').value='Edit';\n" +
						"document.getElementById('selectedControlId').value=seqno ;\n" +
						"selId =document.getElementById('selectedAttrib').value;\n" +
						"document.getElementById('selectedAttrib').value=str1;\n" +
						"document.getElementById(str1).style.fontWeight='bold';\n" +
						"if(selId!='')\n" +
						"{\n" +
							"document.getElementById(selId).style.fontWeight='normal';\n" +
						"}\n" +
						"var controlsForm=document.getElementById('controlsForm');\n" +
					    "controlsForm.action='/dynamicExtensions/LoadFormControlsAction.do'\n;" +
					    "controlsForm.submit();\n" +
					"}\n" ;
				jspWriter.print("<script language='JavaScript'> \n" + 
						"<!-- \n"+
						toggleFunctionJSCode + 
						changeSelectionJSCode + 
						"toggle('N0_0','P00'); \n" + 
						"// --> \n" +
				"</script>" );

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

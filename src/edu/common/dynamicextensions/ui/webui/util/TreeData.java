/*
 * Created on Oct 4, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

import java.util.Vector;

/**
 * @author preeti_munot
 *

 */
public class TreeData {
	private String folder="/images";
	private String color="navy";
	private TNodeList nodes;
	private String target="";
	public int length=0;
	private StringBuffer buf = null;

	public TreeData(){
		try {
			nodes=new TNodeList();
		} catch (Exception e) {
			System.out.println("Exception " + e);
		}
	}

	public void setImagesUrl(String url){
		this.folder = url;
	}
	public String getImagesUrl(){
		return (this.folder);
	}

	public void add(TNode node){
		try {
			nodes.add(node);
			length++;
		} catch (Exception e) {
			System.out.println("Error in treeview during addition of node"+e);
		}
	}
	public void add(String text){
		add(new TNode(text));
	}
	public TNode createNode(String text){
		return (new TNode(text));
	}
	public TNode createNode(String text,String href,String toolTip){
		return (new TNode(text,href,toolTip));
	}

	private void print(String text){
		buf.append(text);
	}


	public String getTree(){
		buf = new StringBuffer();

		try {
			System.out.println("Entering get tree function");
			print("<style>ul.tree{display:none;margin-left:17px;}li.folder{list-style-image: url("+ folder +"/plus.gif);}li.folderOpen{list-style-image: url("+ folder +"/minus.gif);}li.file{FONT-WEIGHT:normal;list-style-image: url("+ folder +"/dot.gif);}a.treeview{color:"+ color +";font-family:verdana;font-size:9pt;}a.treeview:link {text-decoration:none;}a.treeview:visited{text-decoration:none;}a.treeview:hover {text-decoration:underline;}</style>");
			//print("<input type=hidden id=\"selectedAttrib\" value=\"\"  name=\"selAttrib\" > ");
			if(nodes!=null)
			{
				loopThru(nodes,"0");
			}
			else
			{
				System.out.println("Nodes List Is null(get Tree)");
			}
		} catch (Exception e) {
			System.out.println("Error in tree generation "+e);
		}
		return buf.toString();
	}

	private void loopThru(TNodeList nodeList, String parent){
		try {
			if(nodeList!=null)
			{
				boolean hasChild;
				String style;
				String id ="";
				if(parent!="0")
				{
					id="N" + parent ;
					print("<ul class=tree id='"+id+"' >");
				}
				else
				{
					id="N" + parent ;
					print("<ul  id='"+id+"' >");
				}
				for (int i=0;i<nodeList.length;i++){
					TNode node = nodeList.item(i);
					if(node!=null)
					{
						if(node.childNodes.length>0){
							hasChild=true;
						}else{
							hasChild=false;
						}

						if(node.imageUrl==""){
							style="style = 'FONT-WEIGHT:normal;'";
						}else{
							style="style='list-style-image: url("+ node.imageUrl +");'";
						}
						if(hasChild)
						{
							id="P" + parent + i ;
							print("<li "+ style +" class=folder id='"+id+ "'><a class=treeview href=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\">" + node.text + "</a>");
						}
						else		//Means it is a leaf node
						{
							id="L" + parent + i ;
							node.href = "javascript:changeSelection('"+id+"')";
							if(node.target=="")
							{
								node.target=target;
							}
							print("<li "+ style +" class=file><a class=treeview href=\"" + node.href + "\"  title=\"" + node.toolTip + "\" id='"+id+ "'>" + node.text + "</a>");
						}

						if(hasChild){
							loopThru(node.childNodes,parent + "_" + i);
						}

						print("</li>");
					}//If node != null
				}//End of For
				print("</ul>");
			}//End of if nodelist ! null
		} 
		catch (Exception e) 
		{
			System.out.println("Error in parsing schema tree");	
		}
	}
}

class TNodeList{
	Vector v;
	int length=0;
	public TNodeList(){
		v=new Vector();
	}

	public void add(TNode node){
		v.add(node);
		length++;
	}
	public void add(String text){
		add(new TNode(text));
	}
	public TNode item(int index){
		return (TNode)v.get(index);
	}
}

class TNode{
	public String text="";
	public String href;
	public String target="";
	public String toolTip;
	public String path = "";
	public TNodeList childNodes;
	public String imageUrl="";
	public int length=0;

	public TNode(){
		childNodes = new TNodeList();
	}

	public TNode(String text){
		this(text,"");
	}
	public TNode(String text,String href){
		this(text,href,"");
	}

	public TNode(String text,String href,String toolTip){
		this();
		this.text=text.trim();
		this.href=href;
		this.toolTip=toolTip;
	}
	public void add(TNode TNode){
		childNodes.add(TNode);
		length++;
	}
	public void add(String text){
		add(new TNode(text));
	}
}

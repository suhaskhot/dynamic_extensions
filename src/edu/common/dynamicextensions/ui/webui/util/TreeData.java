/*
 * Created on Oct 4, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.util;

import java.util.Vector;

import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *

 */
public class TreeData
{
	private String folder = "/images";
	private String color = "navy";
	private TNodeList nodes;
	private String target = "";
	private int length = 0;
	private StringBuffer buf = null;

	/**
	 * Create a new Tree Data object
	 *
	 */
	public TreeData()
	{
		try
		{
			nodes = new TNodeList();
		}
		catch (Exception e)
		{
			Logger.out.error(e);
		}
	}

	/**
	 * 
	 * @param url URL for the image files
	 */
	public void setImagesUrl(String url)
	{
		this.folder = url;
	}
	/**
	 * 
	 * @return URL of image files
	 */
	public String getImagesUrl()
	{
		return (this.folder);
	}

	/**
	 * Add a new tree node
	 * @param node : Tree Node Object
	 */
	public void add(TNode node)
	{
		try
		{
			nodes.add(node);
			length++;
		}
		catch (Exception e)
		{
			Logger.out.error(e);
		}
	}

	/**
	 * Add a new tree node with given text and sequence number
	 * @param text  : Text for the node
	 * @param seqno : Node sequence number
	 */
	public void add(String text, int seqno)
	{
		add(new TNode(text, seqno));
	}

	/**
	 * Create a new tree node with given text and sequence number
	 * @param text  : Text for the node
	 * @param seqno : Node sequence number
	 * @return newly created Tree Node
	 */
	public TNode createNode(String text, int seqno)
	{
		return (new TNode(text, seqno));
	}

	/**
	 * 
	 * @param text  : Text for the node
	 * @param href : refernce for the node link
	 * @param toolTip : tooltip for tree node
	 * @param seqno : Sequence number
	 * @return : newly created tree node
	 */
	public TNode createNode(String text, String href, String toolTip, int seqno)
	{
		return (new TNode(text, href, toolTip, seqno));
	}

	/**
	 * 
	 * @param text text to be added to buffer
	 */
	private void print(String text)
	{
		buf.append(text);
	}

	/**
	 * 
	 * @return : String having the HTML code for the tree like representation of the data
	 */
	public String getTree()
	{
		buf = new StringBuffer();

		try
		{
			print("<style>ul.tree{display:none;margin-left:17px;}li.folder{list-style-image: url("
					+ folder
					+ "/plus.gif);}li.folderOpen{list-style-image: url("
					+ folder
					+ "/minus.gif);}li.file{FONT-WEIGHT:normal;list-style-image: url("
					+ folder
					+ "/dot.gif);}a.treeview{color:"
					+ color
					+ ";font-family:verdana;font-size:9pt;}a.treeview:link {text-decoration:none;}a.treeview:visited{text-decoration:none;}a.treeview:hover {text-decoration:underline;}</style>");
			//print("<input type=hidden id=\"selectedAttrib\" value=\"\"  name=\"selAttrib\" > ");
			if (nodes != null)
			{
				loopThru(nodes, "0");
			}
			else
			{
				Logger.out.error("Nodes List Is null(get Tree)");
			}
		}
		catch (Exception e)
		{
			Logger.out.error("Error in tree generation " + e);
		}
		return buf.toString();
	}

	/**
	 * 
	 * @param nodeList :  List of tree nodes
	 * @param parent : Name of parent node
	 */
	private void loopThru(TNodeList nodeList, String parent)
	{
		try
		{
			if (nodeList != null)
			{
				boolean hasChild;
				String style;
				String id = "";
				if (parent != "0")
				{
					id = "N" + parent;
					print("<ul class=tree id='" + id + "' >");
				}
				else
				{
					id = "N" + parent;
					print("<ul  id='" + id + "' >");
				}
				for (int i = 0; i < nodeList.getLength(); i++)
				{
					TNode node = nodeList.item(i);
					if (node != null)
					{
						if (node.getChildNodes().getLength() > 0)
						{
							hasChild = true;
						}
						else
						{
							hasChild = false;
						}

						if (node.getImageUrl()== "")
						{
							style = "style = 'FONT-WEIGHT:normal;'";
						}
						else
						{
							style = "style='list-style-image: url(" + node.getImageUrl() + ");'";
						}
						if (hasChild)
						{
							id = "P" + parent + i;
							print("<li " + style + " class=folder id='" + id + "'><a class=treeview href=\"javascript:toggle('N" + parent + "_" + i
									+ "','P" + parent + i + "')\">" + node.getText() + "</a>");
						}
						else
							//Means it is a leaf node
						{
							id = "L" + parent + i;
							node.setHref("javascript:changeSelection('" + id + "','" + node.getSequenceNumber() + "')");
							if (node.getTarget() == "")
							{
								node.setTarget(target);
							}
							print("<li " + style + " class=file><a class=treeview href=\"" + node.getHref() + "\"  title=\"" + node.getToolTip() + "\" id='"
									+ id + "'>" + node.getText() + "</a>");
						}

						if (hasChild)
						{
							loopThru(node.getChildNodes(), parent + "_" + i);
						}

						print("</li>");
					}//If node != null
				}//End of For
				print("</ul>");
			}//End of if nodelist ! null
		}
		catch (Exception e)
		{
			Logger.out.error("Error in parsing schema tree");
		}
	}
}

class TNodeList
{
	Vector<TNode> childNodeList = null;
	private int length = 0;

	/**
	 */
	public TNodeList()
	{
		childNodeList = new Vector<TNode>();
	}

	/**
	 * Add new sub-node to list
	 * @param node Tree node 
	 */
	public void add(TNode node)
	{
		childNodeList.add(node);
		length++;
	}

	/**
	 * Create a new tree node and add to list
	 * @param text  : Name of tree node
	 * @param seqno : Node sequence number
	 */
	public void add(String text, int seqno)
	{
		add(new TNode(text, seqno));
	}

	/**
	 * Get node at specified location in child list
	 * @param index : index in the child list
	 * @return Tree node at index
	 */
	public TNode item(int index)
	{
		return (TNode) childNodeList.get(index);
	}

	/**
	 * 
	 * @return Length of list
	 */
	public int getLength()
	{
		return this.length;
	}
	/**
	 * 
	 * @param length : Length of list
	 */
	public void setLength(int length)
	{
		this.length = length;
	}
}

class TNode
{
	private String text = "";
	private String href;
	private String target = "";
	private String toolTip;
	private TNodeList childNodes;
	private String imageUrl = "";
	private int length = 0;
	private int sequenceNumber = 0;

	/**
	 * Create a new tree node
	 *
	 */
	public TNode()
	{
		childNodes = new TNodeList();
	}

	/**
	 * Create a new tree node with specified text and sequence number
	 * @param text : Text for tree node 
	 * @param seqno : Sequence number
	 */
	public TNode(String text, int seqno)
	{
		this(text, "", seqno);
	}

	/**
	 * Create a new tree node
	 * @param text : Text for tree node
	 * @param href : URL ref for the tree node hyperlink
	 * @param seqno : Sequence number
	 */
	public TNode(String text, String href, int seqno)
	{
		this(text, href, "", seqno);
	}

	/**
	 * 
	 * @param text : Text for tree node
	 * @param href : URL ref for the tree node hyperlink
	 * @param toolTip : Tooltip for tree node
	 * @param seqno : Sequence number
	 */
	public TNode(String text, String href, String toolTip, int seqno)
	{
		this();
		this.text = text.trim();
		this.href = href;
		this.toolTip = toolTip;
		this.sequenceNumber = seqno;
	}

	/**
	 * Add subnode to tree node
	 * @param treeNode :  sub node to be added 
	 */
	public void add(TNode treeNode)
	{
		childNodes.add(treeNode);
		length++;
	}

	/**
	 * Add new sub node with specified text and sequence number
	 * @param text : Text for tree node 
	 * @param seqno : Sequence number
	 */
	public void add(String text, int seqno)
	{
		add(new TNode(text, seqno));
	}

	/**
	 * 
	 * @return Child node list
	 */
	public TNodeList getChildNodes()
	{
		return this.childNodes;
	}

	/**
	 * 
	 * @param childNodes Child node list
	 */
	public void setChildNodes(TNodeList childNodes)
	{
		this.childNodes = childNodes;
	}

	/**
	 * 
	 * @return href
	 */
	public String getHref()
	{
		return this.href;
	}

	/**
	 * 
	 * @param href href
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * 
	 * @return image files URL 
	 */
	public String getImageUrl()
	{
		return this.imageUrl;
	}

	/**
	 * 
	 * @param imageUrl image files URL
	 */
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * 
	 * @return number of childnodes
	 */
	public int getLength()
	{
		return this.length;
	}

	/**
	 * 
	 * @param length length of child node list
	 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * @return Sequence number
	 */
	public int getSequenceNumber()
	{
		return this.sequenceNumber;
	}
	/**
	 * 
	 * @param sequenceNumber Sequence number
	 */
	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 *
	 * @return Target
	 */
	public String getTarget()
	{
		return this.target;
	}

	/**
	 * 
	 * @param target target
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * 
	 * @return text
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 * 
	 * @param text text
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * 
	 * @return tool tip
	 */
	public String getToolTip()
	{
		return this.toolTip;
	}

	/**
	 * 
	 * @param toolTip tooltip
	 */
	public void setToolTip(String toolTip)
	{
		this.toolTip = toolTip;
	}
}

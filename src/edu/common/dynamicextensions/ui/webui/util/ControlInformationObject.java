/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.ui.webui.util;


/**
 * @author vishvesh_mulay
 *
 */

public class ControlInformationObject
{
    private String controlName;
    
    private String controlType;
    
    private String identifier;

    
    public String getControlName()
    {
        return controlName;
    }

    
    public void setControlName(String controlName)
    {
        this.controlName = controlName;
    }

    
    public String getControlType()
    {
        return controlType;
    }

    
    public void setControlType(String controlType)
    {
        this.controlType = controlType;
    }

    
    public String getIdentifier()
    {
        return identifier;
    }

    
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    public ControlInformationObject(String name, String type, String identifier)
    {
        super();
        // TODO Auto-generated constructor stub
        controlName = name;
        controlType = type;
        this.identifier = identifier;
    }

    
}

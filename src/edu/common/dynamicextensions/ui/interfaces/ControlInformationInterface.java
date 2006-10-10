package edu.common.dynamicextensions.ui.interfaces;
/**
 * @author deepti_shelar
 */
import java.util.List;

public interface ControlInformationInterface {
	/**
	 * @return the toolsList
	 */
	public List getToolsList(); 

	/**
	 * @param toolsList the toolsList to set
	 */
	public void setToolsList(List toolsList); 
	/**
	 * @return the userSelectedTool
	 */
	public String getUserSelectedTool();

	/**
	 * @param userSelectedTool the userSelectedTool to set
	 */
	public void setUserSelectedTool(String userSelectedTool) ;
}

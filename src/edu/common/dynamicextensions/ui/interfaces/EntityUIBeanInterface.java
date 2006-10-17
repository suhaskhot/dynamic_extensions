package edu.common.dynamicextensions.ui.interfaces;
/**
 * 
 * @author deepti_shelar
 *
 */
public interface EntityUIBeanInterface {
	/**
	 * @return Returns the name.
	 */
	public String getFormName();
	/**
	 * @param name The name to set.
	 */
	public void setFormName(String formName); 
	/**
	 * @return Returns the description.
	 */
	public String getFormDescription(); 
	/**
	 * @param description The description to set.
	 */
	public void setFormDescription(String description); 
	/**
	 * @param returns createAs .
	 */
	public String getCreateAs();
	/**
	 * @param createAs The createAs to set.
	 */
	public void setCreateAs(String createAs) ;
	/**
	 * @param returns ConceptCode .
	 */
	public String getConceptCode();
	/**
	 * @param ConceptCode The ConceptCode to set.
	 */
	public void setConceptCode(String ConceptCode) ;
	
}

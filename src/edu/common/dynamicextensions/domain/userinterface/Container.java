package edu.common.dynamicextensions.domain.userinterface;
import java.util.Collection;

import edu.common.dynamicextensions.domain.Entity;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class Container {

	protected String buttonCss;
	protected String caption;
	protected String mainTableCss;
	protected String requiredFieldIndicatior;
	protected String requiredFieldWarningMessage;
	protected String titleCss;
	protected Collection controlCollection;
	public Entity entity;

	public Container(){

	}

	public void finalize() throws Throwable {

	}
	

    /**
     * @return Returns the buttonCss.
     */
    public String getButtonCss() {
        return buttonCss;
    }
    /**
     * @param buttonCss The buttonCss to set.
     */
    public void setButtonCss(String buttonCss) {
        this.buttonCss = buttonCss;
    }
    /**
     * @return Returns the caption.
     */
    public String getCaption() {
        return caption;
    }
    /**
     * @param caption The caption to set.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }
    /**
     * @return Returns the controlCollection.
     */
    public Collection getControlCollection() {
        return controlCollection;
    }
    /**
     * @param controlCollection The controlCollection to set.
     */
    public void setControlCollection(Collection controlCollection) {
        this.controlCollection = controlCollection;
    }
    /**
     * @return Returns the entity.
     */
    public Entity getEntity() {
        return entity;
    }
    /**
     * @param entity The entity to set.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    /**
     * @return Returns the mainTableCss.
     */
    public String getMainTableCss() {
        return mainTableCss;
    }
    /**
     * @param mainTableCss The mainTableCss to set.
     */
    public void setMainTableCss(String mainTableCss) {
        this.mainTableCss = mainTableCss;
    }
    /**
     * @return Returns the requiredFieldIndicatior.
     */
    public String getRequiredFieldIndicatior() {
        return requiredFieldIndicatior;
    }
    /**
     * @param requiredFieldIndicatior The requiredFieldIndicatior to set.
     */
    public void setRequiredFieldIndicatior(String requiredFieldIndicatior) {
        this.requiredFieldIndicatior = requiredFieldIndicatior;
    }
    /**
     * @return Returns the requiredFieldWarningMessage.
     */
    public String getRequiredFieldWarningMessage() {
        return requiredFieldWarningMessage;
    }
    /**
     * @param requiredFieldWarningMessage The requiredFieldWarningMessage to set.
     */
    public void setRequiredFieldWarningMessage(
            String requiredFieldWarningMessage) {
        this.requiredFieldWarningMessage = requiredFieldWarningMessage;
    }
    /**
     * @return Returns the titleCss.
     */
    public String getTitleCss() {
        return titleCss;
    }
    /**
     * @param titleCss The titleCss to set.
     */
    public void setTitleCss(String titleCss) {
        this.titleCss = titleCss;
    }
}
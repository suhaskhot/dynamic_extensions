package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * @author geetika_bangard
 */
public interface ContainerInterface {
    
    /**
     * @return
     */
    public Long getId();
    
    /**
     * @return Returns the buttonCss.
     */
    public String getButtonCss();
    /**
     * @param buttonCss The buttonCss to set.
     */
    public void setButtonCss(String buttonCss);
    /**
     * @return Returns the caption.
     */
    public String getCaption();
    /**
     * @param caption The caption to set.
     */
    public void setCaption(String caption);
    /**
     * @return Returns the controlCollection.
     */
    public Collection getControlCollection();
    /**
     * @param controlCollection The controlCollection to set.
     */
    public void addControl(ControlInterface controlInterface);
    /**
     * @return Returns the entity.
     */
    public EntityInterface getEntity();
    /**
     * @param entity The entity to set.
     */
    public void setEntity(EntityInterface entityInterface);
    /**
     * @return Returns the mainTableCss.
     */
    public String getMainTableCss();
    /**
     * @param mainTableCss The mainTableCss to set.
     */
    public void setMainTableCss(String mainTableCss);
    /**
     * @return Returns the requiredFieldIndicatior.
     */
    public String getRequiredFieldIndicatior();
    /**
     * @param requiredFieldIndicatior The requiredFieldIndicatior to set.
     */
    public void setRequiredFieldIndicatior(String requiredFieldIndicatior);
    /**
     * @return Returns the requiredFieldWarningMessage.
     */
    public String getRequiredFieldWarningMessage();
    /**
     * @param requiredFieldWarningMessage The requiredFieldWarningMessage to set.
     */
    public void setRequiredFieldWarningMessage(
            String requiredFieldWarningMessage);
    /**
     * @return Returns the titleCss.
     */
    public String getTitleCss();
    /**
     * @param titleCss The titleCss to set.
     */
    public void setTitleCss(String titleCss);

       
}

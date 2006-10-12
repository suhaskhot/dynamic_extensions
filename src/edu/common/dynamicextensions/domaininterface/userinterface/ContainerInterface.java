package edu.common.dynamicextensions.domaininterface.userinterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This interface stores the necessary information about the container on dynamically generated user interface.
 * 
 * @author geetika_bangard
 */
public interface ContainerInterface 
{
    
    /**
     * @return
     */
     Long getId();
    
    /**
     * @return Returns the buttonCss.
     */
     String getButtonCss();
    /**
     * @param buttonCss The buttonCss to set.
     */
     void setButtonCss(String buttonCss);
    /**
     * @return Returns the caption.
     */
     String getCaption();
    /**
     * @param caption The caption to set.
     */
     void setCaption(String caption);
    /**
     * @return Returns the controlCollection.
     */
     Collection getControlCollection();
    /**
     * @param controlCollection The controlCollection to set.
     */
     void addControl(ControlInterface controlInterface);
    /**
     * @return Returns the entity.
     */
     EntityInterface getEntity();
    /**
     * @param entity The entity to set.
     */
     void setEntity(EntityInterface entityInterface);
    /**
     * @return Returns the mainTableCss.
     */
     String getMainTableCss();
    /**
     * @param mainTableCss The mainTableCss to set.
     */
     void setMainTableCss(String mainTableCss);
    /**
     * @return Returns the requiredFieldIndicatior.
     */
     String getRequiredFieldIndicatior();
    /**
     * @param requiredFieldIndicatior The requiredFieldIndicatior to set.
     */
     void setRequiredFieldIndicatior(String requiredFieldIndicatior);
    /**
     * @return Returns the requiredFieldWarningMessage.
     */
     String getRequiredFieldWarningMessage();
    /**
     * @param requiredFieldWarningMessage The requiredFieldWarningMessage to set.
     */
     void setRequiredFieldWarningMessage(
            String requiredFieldWarningMessage);
    /**
     * @return Returns the titleCss.
     */
     String getTitleCss();
    /**
     * @param titleCss The titleCss to set.
     */
     void setTitleCss(String titleCss);

       
}

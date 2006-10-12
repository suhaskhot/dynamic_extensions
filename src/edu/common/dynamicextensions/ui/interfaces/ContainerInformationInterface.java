package edu.common.dynamicextensions.ui.interfaces;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;

public interface ContainerInformationInterface {
	 /**
     * css style for the main table.
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
     * css style for the Title.
     * @return Returns the titleCss.
     */
     String getTitleCss();
    /**
     * @param titleCss The titleCss to set.
     */
     void setTitleCss(String titleCss);
     /**
      * The css style defined for button.
      * @return Returns the buttonCss.
      */
      String getButtonCss();
     /**
      * @param buttonCss The buttonCss to set.
      */
      void setButtonCss(String buttonCss);
     /**
      * caption for the container.
      * @return Returns the caption.
      */
      String getCaption();
     /**
      * @param caption The caption to set.
      */
      void setCaption(String caption);
}

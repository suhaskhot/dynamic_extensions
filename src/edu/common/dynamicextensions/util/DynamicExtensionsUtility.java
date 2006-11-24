/**
 * 
 */

package edu.common.dynamicextensions.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author chetan_patil
 *
 */
public class DynamicExtensionsUtility
{

	/**
	 * This method fetches the Container instance from the Database given the corresponding Container Identifier.
	 * @param containerIdentifier The Idetifier of the Container.
	 * @return the ContainerInterface
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	public static ContainerInterface getContainerByIdentifier(String containerIdentifier) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		ContainerInterface containerInterface = null;
		containerInterface = (ContainerInterface) getObjectByIdentifier(ContainerInterface.class.getName(), containerIdentifier);
		return containerInterface;
	}

	/**
	 * This method returns object for a given class name and identifer 
	 * @param objectName  name of the class of the object
	 * @param identifier identifier of the object
	 * @return  obejct
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	private static Object getObjectByIdentifier(String objectName, String identifier) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		Object object = null;
		try
		{
			List objectList = bizLogic.retrieve(objectName, Constants.ID, identifier);

			if (objectList == null || objectList.isEmpty())
			{
				throw new DynamicExtensionsSystemException("OBJECT_NOT_FOUND");
			}

			object = objectList.get(0);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return object;
	}
	/**
	 * @param controlInterface ControlInterface
	 * @return String ControlName
	 */
	public static String getControlName(ControlInterface controlInterface)
	{
		if (controlInterface != null)
		{
			if (controlInterface instanceof TextFieldInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}
			else if (controlInterface instanceof ComboBoxInterface)
			{
				return ProcessorConstants.COMBOBOX_CONTROL;
			}
			else if (controlInterface instanceof ListBoxInterface)
			{
				return ProcessorConstants.COMBOBOX_CONTROL;
			}
			else if (controlInterface instanceof DatePickerInterface)
			{
				return ProcessorConstants.DATEPICKER_CONTROL;
			}
			else if (controlInterface instanceof TextAreaInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}
			else if (controlInterface instanceof RadioButtonInterface)
			{
				return ProcessorConstants.RADIOBUTTON_CONTROL;
			}
			else if (controlInterface instanceof CheckBoxInterface)
			{
				return ProcessorConstants.CHECKBOX_CONTROL;
			}
			else if (controlInterface instanceof FileUploadInterface)
			{
				return ProcessorConstants.FILEUPLOAD_CONTROL;
			}

		}
		return null;
	}
	/**
	 * 
	 * @param controlCollectio
	 * @param sequenceNumber
	 * @return
	 */
	public static ControlInterface getControlBySequenceNumber(Collection controlCollection, int sequenceNumber)
	{
		Iterator controlIterator = controlCollection.iterator();
		ControlInterface controlInterface = null;
		while (controlIterator.hasNext())
		{
			controlInterface = (ControlInterface) controlIterator.next();
			if (controlInterface.getSequenceNumber() != null && controlInterface.getSequenceNumber() == sequenceNumber
					&& !controlInterface.getSequenceNumberChanged())
			{
				controlInterface.setSequenceNumberChanged(true);
				return controlInterface;
			}
		}
		return controlInterface;
	}

	/**
	 * 
	 * @param controlCollection
	 */
	public static void resetSequenceNumberChanged(Collection controlCollection)
	{
		if (controlCollection != null)
		{
			Iterator controlIterator = controlCollection.iterator();
			ControlInterface controlInterface = null;
			while (controlIterator.hasNext())
			{
				controlInterface = (ControlInterface) controlIterator.next();
				controlInterface.setSequenceNumberChanged(false);
			}
		}
	}

	/**
	 * 
	 */
	public static void initialiseApplicationVariables()
	{
		if (Variables.databaseName == null || Variables.databaseName.trim().length() == 0
				|| Variables.datePattern.trim().length() != 0)
		{
			return;
		}

		if (Variables.databaseName.equals(Constants.ORACLE_DATABASE))
		{
			//set string/function for oracle

			Variables.datePattern = "mm-dd-yyyy";
			Variables.timePattern = "hh-mi-ss";
			Variables.dateFormatFunction = "TO_CHAR";
			Variables.timeFormatFunction = "TO_CHAR";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "TO_DATE";
		}
		else
		{
			Variables.datePattern = "%m-%d-%Y";
			Variables.timePattern = "%H:%i:%s";
			Variables.dateFormatFunction = "DATE_FORMAT";
			Variables.timeFormatFunction = "TIME_FORMAT";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "STR_TO_DATE";
		}
	}
	public static AttributeTypeInformationInterface getAttributeTypeInformation(AbstractAttributeInterface abstractAttributeInterface)
	{
		AttributeTypeInformationInterface attributeTypeInformation = null;
		if(abstractAttributeInterface!=null)
		{
			if(abstractAttributeInterface instanceof AttributeInterface)
			{
				attributeTypeInformation = ((AttributeInterface)abstractAttributeInterface).getAttributeTypeInformation();
			}
		}
		return attributeTypeInformation;
	}	

	/**
	 * This method converts stack trace to the string representation
	 * @param aThrowable   throwable object
	 * @return String representation  of the stack trace
	 */
	public static String getStackTrace(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}
	/**
	 * Converts string to integer
	 * @param string
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 */
	public static int convertStringToInt(String string) throws DynamicExtensionsApplicationException
	{
		int intValue = 0; 
		if (string != null)
		{
			try
			{
				if (string.trim().equals(""))
				{
					intValue = 0; //Assume 0 for blank values
				}
				else
				{
					intValue = Integer.parseInt(string);
				}
			}
			catch (NumberFormatException e)
			{
				throw new DynamicExtensionsApplicationException(e.getMessage(), e);
			}
		}
		return intValue;
	}

}

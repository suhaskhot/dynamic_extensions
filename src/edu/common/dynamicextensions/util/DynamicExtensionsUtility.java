/**
 * 
 */

package edu.common.dynamicextensions.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
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
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.util.CVSTagReader;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_patil
 *
 */
public class DynamicExtensionsUtility
{

	/**
	 * This method fetches the Control instance from the Database given the corresponding Control Identifier.
	 * @param controlIdentifier The Idetifier of the Control.
	 * @return the ControlInterface
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	public static ControlInterface getControlByIdentifier(String controlIdentifier) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		ControlInterface  controlInterface = null;
		controlInterface  = (ControlInterface) getObjectByIdentifier(ControlInterface.class.getName(),controlIdentifier);
		return controlInterface;
	}
	/**
	 * This method fetches the Container instance from the Database given the corresponding Container Identifier.
	 * @param containerIdentifier The Idetifier of the Container.
	 * @return the ContainerInterface
	 * @throws DynamicExtensionsSystemException on System exception
	 * @throws DynamicExtensionsApplicationException on Application exception
	 */
	public static EntityGroupInterface getEntityGroupByIdentifier(String entityGroupIdentifier) throws DynamicExtensionsSystemException,
	DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroupInterface = null;
		entityGroupInterface  = (EntityGroupInterface) getObjectByIdentifier(EntityGroupInterface.class.getName(),entityGroupIdentifier);
		return entityGroupInterface;
	}

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
			if (controlInterface instanceof ContainmentAssociationControl)
			{
				return ProcessorConstants.ADD_SUBFORM_CONTROL;
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
		try
		{
			DBUtil.currentSession();
			DBUtil.closeSession();
		}
		catch (HibernateException e)
		{
			throw new RuntimeException(e);
		}

		if (Logger.out == null) {
			Logger.configure("");
		}

		if (!Variables.databaseName.equals(Constants.MYSQL_DATABASE))
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

	/**
	 * 
	 */
	public static void initialiseApplicationInfo() {
		String fileName = Variables.dynamicExtensionsHome + System.getProperty("file.separator")+ ApplicationProperties.getValue("application.version.file");
		CVSTagReader cvsTagReader = new CVSTagReader();
		String cvsTag = cvsTagReader.readTag(fileName);
		Variables.applicationCvsTag = cvsTag;
		Logger.out.info("========================================================");
		Logger.out.info("Application Information");
		Logger.out.info("Name: "+Variables.applicationName);
		Logger.out.info("Version: "+Variables.applicationVersion);
		Logger.out.info("CVS TAG: "+Variables.applicationCvsTag);
		Logger.out.info("Path: "+ Variables.applicationHome);
		Logger.out.info("Database Name: "+Variables.databaseName);
		Logger.out.info("========================================================");  
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

	/**
     * Checks that the input String contains only numeric digits.
     * @param numString The string whose characters are to be checked.
     * @return Returns false if the String contains any alphabet else returns true. 
     * */
    public static boolean isNaturalNumber(String numString)
    {
        boolean isNaturalNumber = true;
    	try
        {
            double doubleValue = Double.parseDouble(numString);
            if (doubleValue < 0)
            {
            	isNaturalNumber = false;
            }
        }
        catch(NumberFormatException exp)
        {
        	isNaturalNumber = false;
        }
        return isNaturalNumber;
    }
    
    public static boolean isNumeric(String numString)
    {
        boolean isNumeric = true;
    	try
        {
            double doubleValue = Double.parseDouble(numString);
        }
        catch(NumberFormatException exp)
        {
        	isNumeric = false;
        }
        return isNumeric;
    }

	public static int getCurrentDay()
	{
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getCurrentMonth()
	{
		return (Calendar.getInstance().get(Calendar.MONTH)+1);
	}
	public static int getCurrentYear()
	{
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 *
	 * @param originalObject Object
	 * @return Object
	 */
	public static Object cloneObject(Object originalObject) {
		Object clonedObject = null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(originalObject);
			//retrieve back
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			clonedObject = objectInputStream.readObject();
		} catch (IOException ioe) {

			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		return clonedObject;
	}


	/**
	 * @param string : string to be checked
	 * @param list: List that is to be checked if string is contained
	 * @return check if a string is contained in the passed list and return true if yes
	 */
	public static boolean isStringInList(String string,List<String> list)
	{
		boolean isContainedInList = false;
		if((string!=null)&&(list!=null))
		{
			String listString  = null;
			Iterator<String> iterator = list.iterator();
			while(iterator.hasNext())
			{
				listString = iterator.next();
				if(string.equals(listString))
				{
					isContainedInList = true;
					break;
				}
			}
		}
		return isContainedInList;
	}

	/**
	 *
	 * @param list list of NameValueBeanObjects
	 */
	@SuppressWarnings("unchecked")
	public static void sortNameValueBeanListByName(List<NameValueBean> list) {
		Collections.sort(list,new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = ((NameValueBean) o1).getName();
				String s2 = ((NameValueBean) o2).getName();
				return s1.compareTo(s2);
			}
		});
	}

	public static EntityGroupInterface getEntityGroup(EntityInterface  entity)
	{
		EntityGroupInterface entityGroup  = null;
		if(entity!=null)
		{
			Collection<EntityGroupInterface> entityGroupCollection = entity.getEntityGroupCollection();
			if(entityGroupCollection!=null)
			{
				Iterator<EntityGroupInterface> entityGroupIter = entityGroupCollection.iterator();
				if(entityGroupIter.hasNext())
				{
					entityGroup = entityGroupIter.next();
				}
			}
		}
		return entityGroup;
	}
}
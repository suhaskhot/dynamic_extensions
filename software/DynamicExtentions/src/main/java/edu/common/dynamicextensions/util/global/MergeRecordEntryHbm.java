
package edu.common.dynamicextensions.util.global;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class is specifically used for the merging of 
 * generated AbstractRecordEntry.hbm with the respective recordEntry hbm
 * @author Kunal
 *
 */
public class MergeRecordEntryHbm
{

	public static final String NAME = "name";
	private static final String SET = "set";
	public static final String NEW_LINE_CHARACTER = "\n";
	public static final String JOINED_SUBCLASS = "</joined-subclass>";
	public static final String LINE_SEPARATOR = "line.separator";

	private String abstractRecordEntryPath;
	private String recordEntryPath;
	private StringBuffer setString = new StringBuffer();

	public static void main(String[] args) throws DynamicExtensionsSystemException,
			FactoryConfigurationError, DynamicExtensionsApplicationException
	{
		MergeRecordEntryHbm entryHbm = new MergeRecordEntryHbm();
		//Step 1: Validate Parameters
		entryHbm.validateParam(args);

		//Step 2: initialise parameters
		entryHbm.initParam(args);

		//Step 3: Merge hbms
		entryHbm.merge();

	}

	private void merge() throws DynamicExtensionsSystemException, FactoryConfigurationError
	{
		//step 1:initialise abstractRecordEntry document 
		Document src = getDoc(abstractRecordEntryPath);

		//step 2:initialise recordEntry document
		Document dest = getDoc(recordEntryPath);

		//step 3:Get list of associations
		NodeList setList = src.getElementsByTagName(SET);

		//step 4:get the parent node of the hbm
		Element joinedSubClassNode = getJoinedSubClassNode(dest);

		//step 5:Copy sets to the destination hbm
		copySet(joinedSubClassNode, setList, dest);

		//step 6:commit changes to the destination file 
		writeFile(recordEntryPath, dest);

	}

	/**
	 * This method copies the association from the source hbm
	 * @param joinedSubClassNode
	 * @param setList
	 * @param dest
	 */
	private void copySet(Element joinedSubClassNode, NodeList setList, Document dest)
	{

		List<String> existingSet = getSetName(joinedSubClassNode);
		for (int i = 0; i < setList.getLength(); i++)
		{
			Element set = (Element) setList.item(i);
			String setName = set.getAttributeNode(NAME).getValue();
			//check for duplicate entries
			if (!existingSet.contains(setName))
			{
				updateSetString((Element) setList.item(i));
			}
		}

	}

	/**
	 * This methods needs to be improved, with the better mechanism for xml merges
	 * @param item
	 */
	private void updateSetString(Element item)
	{
		String tokenizedStringSet = "<set name=\"@@NAME@@\" lazy=\"false\" cascade=\"save-update\" inverse=\"true\">"
				+ " <cache usage=\"read-write\" />"
				+ " <key column=\"@@COLUMN_NAME@@\" not-null=\"false\" />"
				+ "<one-to-many class=\"@@CLASS_NAME@@\"/>" + "</set>";
		String setName = item.getAttributeNode(NAME).getValue();
		String columnName = ((Element) item.getElementsByTagName("key").item(0)).getAttributeNode(
				"column").getValue();
		String className = ((Element) item.getElementsByTagName("one-to-many").item(0))
				.getAttributeNode("class").getValue();
		tokenizedStringSet = tokenizedStringSet.replace("@@NAME@@", setName);
		tokenizedStringSet = tokenizedStringSet.replace("@@COLUMN_NAME@@", columnName);
		tokenizedStringSet = tokenizedStringSet.replace("@@CLASS_NAME@@", className);
		setString.append(tokenizedStringSet);
		setString.append(NEW_LINE_CHARACTER);

	}

	/**
	 * @param joinedSubClassNode
	 * @return
	 */
	private List<String> getSetName(Element joinedSubClassNode)
	{

		List<String> setName = new ArrayList<String>();
		NodeList setList = joinedSubClassNode.getElementsByTagName(SET);
		for (int i = 0; i < setList.getLength(); i++)
		{
			setName.add(((Element) setList.item(i)).getAttributeNode(NAME).getValue());
		}
		return setName;
	}

	/**
	 * @param dest
	 * @return
	 */
	private Element getJoinedSubClassNode(Document dest)
	{
		return (Element) dest.getElementsByTagName("joined-subclass").item(0);
	}

	/**
	 * @param filePath
	 * @return
	 * @throws FactoryConfigurationError
	 * @throws DynamicExtensionsSystemException
	 */
	private Document getDoc(String filePath) throws FactoryConfigurationError,
			DynamicExtensionsSystemException
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.parse(filePath);
		}
		catch (ParserConfigurationException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}
		catch (SAXException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("Error initializing from file " + filePath,
					e);
		}
	}

	/**
	 * @param args
	 */
	private void initParam(String[] args)
	{

		abstractRecordEntryPath = args[0];
		recordEntryPath = args[1];

	}

	private void validateParam(String[] args) throws DynamicExtensionsApplicationException
	{
		if (args.length < 2)
		{
			throw new DynamicExtensionsApplicationException(
					"Min 2 parameters are required for running this class");
		}

	}

	private void writeFile(String destinatonFile, Document document)
	{

		/*// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			// 2: Update encoding to encoding="windows-1252"
			transformer.setOutputProperty(OutputKeys.ENCODING, "windows-1252");

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new FileOutputStream(
					destinatonFile));
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		try
		{
			String fileContent = readFile(destinatonFile);
			setString.append(NEW_LINE_CHARACTER);
			setString.append(JOINED_SUBCLASS);
			PrintWriter printWriter = new PrintWriter(destinatonFile);
			printWriter.print(fileContent.replace(JOINED_SUBCLASS, setString.toString()));
			printWriter.close();
			/*System.out.println(fileContent.replace("</joined-subclass>", setString.toString()));*/
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Utility method used for reading all contenst of the file in a string
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	private String readFile(String pathname) throws IOException
	{

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty(LINE_SEPARATOR);

		try
		{
			while (scanner.hasNextLine())
			{
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		}
		finally
		{
			scanner.close();
		}
	}
}

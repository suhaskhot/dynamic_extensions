package edu.wustl.metadata.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;

/**
 * The Class CacoreDEMetadataAppender.
 *
 * @author mandar_shidhore
 */
public class CacoreDEMetadataAppender {

    /** The Constant MAPPING. */
    private static final String MAPPING = "mapping";

    /** The Constant SESSION_FACTORY. */
    private static final String SESSION_FACTORY = "session-factory";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = "resource";

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
     */
    public static void main(final String[] args) throws DynamicExtensionsApplicationException {
        final CacoreDEMetadataAppender cacoreDEMetadata = new CacoreDEMetadataAppender(); // NOPMD by gaurav_sawant on 9/16/10 1:20 PM

        validateArgs(args);
        String tempFilePath = ""; // NOPMD

        try {
            final String deHibCfgFile = args[0];
            tempFilePath = args[0]; // NOPMD
            final Document deHibCfg = cacoreDEMetadata.getDocumentObjectForXML(deHibCfgFile);
            final NodeList deHibCfgNodes = deHibCfg.getElementsByTagName(SESSION_FACTORY);
            final Node deHibCfgNode = deHibCfgNodes.item(0);

            // Get the original resource mappings from 'DynamicExtensionsHibernate.cfg.xml' file.
            final List<String> deResrcMappings = cacoreDEMetadata.getOrigDEResourceMappings(deHibCfgNode); // NOPMD by gaurav_sawant on 9/16/10 1:21 PM

            final String cacoreHibCfg = args[1];
            tempFilePath = args[1];
            final Document cacoreGenHibCfg = cacoreDEMetadata.getDocumentObjectForXML(cacoreHibCfg);
            final NodeList cacoreHibCfgNodes = cacoreGenHibCfg.getElementsByTagName(SESSION_FACTORY);
            final Node cacoreHibCfgNode = cacoreHibCfgNodes.item(0);

            final NodeList cacoreCfgChdNodes = cacoreHibCfgNode.getChildNodes();
            for (int i = 0; i < cacoreCfgChdNodes.getLength(); i++) {
                final Node node = cacoreCfgChdNodes.item(i);
                addNodes(deHibCfg, deHibCfgNode, deResrcMappings,
                        node);
            }
            //printDEResourceMappings(deHibCfgNode);
            cacoreDEMetadata.writeConfigurationFile(deHibCfgFile, deHibCfg);
        } catch (SAXException e) {
            throw new DynamicExtensionsApplicationException("Exception while reading file " + tempFilePath, e);
        } catch (IOException e) {
            throw new DynamicExtensionsApplicationException("Exception while reading file " + tempFilePath, e);
        } catch (TransformerException e) {
            throw new DynamicExtensionsApplicationException("Exception while reading file " + tempFilePath, e);
        }
    }

    /**
     * Validate args.
     *
     * @param args the args
     * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
     */
    private static void validateArgs(final String[] args)
            throws DynamicExtensionsApplicationException
    {
        if (args.length != 2) {
            throw new DynamicExtensionsApplicationException(
                    "Please specify paths for hibernate configuration files!");
        }
    }

    /**
     * Adds the nodes.
     *
     * @param deHibCfg the de hib cfg
     * @param deHibCfgNode the de hib cfg node
     * @param deResrcMappings the de resrc mappings
     * @param node the node
     */
    private static void addNodes(
            final Document deHibCfg,
            final Node deHibCfgNode, final List<String> deResrcMappings,
            final Node node)
    {
        if (node.hasAttributes()) {
            final Node innerNode = node.getAttributes().item(0);
            String nodeValue = innerNode.getNodeValue();
            if (MAPPING.equals(node.getNodeName()) && !deResrcMappings.contains(nodeValue)) {
                final Element mappingNode = deHibCfg.createElement(MAPPING);
                mappingNode.setAttribute(RESOURCE, nodeValue);
                deHibCfgNode.appendChild(mappingNode);
            }
        }
    }

    /**
     * This method returns the list of original DE mappings.
     *
     * @param node the node
     * @return the orig de resource mappings
     */
    private List<String> getOrigDEResourceMappings(final Node node) {
        final List<String> resourceMappings = new ArrayList<String>();

        final NodeList propertyNodes = node.getChildNodes();
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            final Node property = propertyNodes.item(i);
            if (property.hasAttributes()) {
                final Node innerNode = property.getAttributes().item(0); //NOPMD
                if (MAPPING.equals(property.getNodeName())) {
                    resourceMappings.add(innerNode.getNodeValue());
                }
            }
        }
        return resourceMappings;
    }

    /**
     * Gets the document object for xml.
     *
     * @param xmlFile the xml file
     * @return the document object for xml
     * @throws SAXException the sAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Document getDocumentObjectForXML(final String xmlFile) throws SAXException, IOException {
        final DOMParser parser = new DOMParser();

        // Ignore DTD URL : without this we get connection timeout
        // error as the application tries to access the URL.
        parser.setEntityResolver(new DtdResolver());
        parser.parse(new InputSource(xmlFile));

        final Document document = parser.getDocument();

        return document;
    }

    /**
     * Method writes the changes to the configuration file.
     *
     * @param cfgFile the cfg file
     * @param cfgDocument the cfg document
     * @throws TransformerException the transformer exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void writeConfigurationFile(final String cfgFile, final Document cfgDocument)
            throws TransformerException, IOException {
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                                      "-//Hibernate/Hibernate Configuration DTD 3.0//EN");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                                      "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd");

        // Initialize StreamResult with file object to save to file.
        final StreamResult result = new StreamResult(new FileWriter(cfgFile));
        final DOMSource source = new DOMSource(cfgDocument);
        transformer.transform(source, result);
    }
}

/**
 * The Class DtdResolver.
 */
class DtdResolver implements EntityResolver
{

    /** The Constant HIBERNATECONFIG. */
    private static final String HIBERNATECONFIG = "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd";

    /** The Constant HIBERNATEMAPPING. */
    private static final String HIBERNATEMAPPING = "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd";

    /**
     * Resolve entity.
     *
     * @param publicId
     *            the public id.
     * @param systemId
     *            the system id.
     * @return the input source.
     * @throws SAXException
     *             the sAX exception.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
     *      java.lang.String)
     */
    public InputSource resolveEntity(final String publicId,
            final String systemId) throws SAXException, IOException
    {
        InputSource inputSource = null; // NOPMD

        if (systemId.contains(HIBERNATECONFIG)
                || systemId.contains(HIBERNATEMAPPING))
        {
            inputSource = new InputSource(new StringReader(""));
        }
        return inputSource;
    }

}

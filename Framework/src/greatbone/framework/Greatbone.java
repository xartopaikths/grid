package greatbone.framework;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/**
 * Some resources and operations shared among our modules.
 */
public class Greatbone {

    static final int CORES = Runtime.getRuntime().availableProcessors();

    public static final int IO_THREADS = CORES * 2;

    public static final int WORKER_THREADS = CORES * 16;

    public final static XnioWorker WORKER;

    static final String CONFIG_FILE = "config.xml";

    // the root element of the config xml document
    public static Element config;

    static {

        // initialize the global XNIO worker
        Xnio xnio = Xnio.getInstance(Greatbone.class.getClassLoader());
        XnioWorker worker = null;
        try {
            worker = xnio.createWorker(OptionMap.builder()
                    .set(Options.WORKER_IO_THREADS, CORES * 2)
                    .set(Options.CONNECTION_HIGH_WATER, 1000000)
                    .set(Options.CONNECTION_LOW_WATER, 1000000)
                    .set(Options.WORKER_TASK_CORE_THREADS, CORES * 8)
                    .set(Options.WORKER_TASK_MAX_THREADS, CORES * 12)
                    .set(Options.TCP_NODELAY, true)
                    .set(Options.CORK, true)
                    .getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        WORKER = worker;

        // load config xml
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(fis));
            Greatbone.config = parser.getDocument().getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // enable remote JMX through RMI
        try {
            LocateRegistry.createRegistry(8888);
            MBeanServer mserver = ManagementFactory.getPlatformMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8888/server");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mserver);
            cs.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Element getConfigXmlTopTag(String tag) {
        NodeList lst = config.getElementsByTagName(tag);
        if (lst.getLength() > 0) {
            return (Element) lst.item(0);
        }
        return null;
    }

    public static Element getXmlSubElement(Element parent, String tag, String keyattr) {
        Element el = null;
        NodeList lst = parent.getElementsByTagName(tag);
        for (int i = 0; i < lst.getLength(); i++) {
            Element e = (Element) lst.item(i);
            if (keyattr.equals(e.getAttribute("name"))) el = e;
        }
        return el;
    }

    public static void saveConfigXml() {
        Document doc = config.getOwnerDocument();
        DOMImplementationLS dom = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = dom.createLSSerializer();
        try {
            FileWriter fos = new FileWriter(CONFIG_FILE);
            String xml = lsSerializer.writeToString(doc);
            fos.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

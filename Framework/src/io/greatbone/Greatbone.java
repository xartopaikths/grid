package io.greatbone;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

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

    public final static EventLoopGroup BOSS = new NioEventLoopGroup(CORES > 32 ? 4 : 2);

    public final static EventLoopGroup WORK = new NioEventLoopGroup(CORES * 16);

    static final String CONFIG_FILE = "config.xml";

    // the root element of the config xml document
    public static Element config;

    static {

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

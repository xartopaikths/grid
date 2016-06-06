package io.greatbone;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
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

    public static final boolean LINUX;

    static final int CORES = Runtime.getRuntime().availableProcessors();

    // accepting and execution event loop groups reused in all bootstraps, minimize the thread-usage
    public final static EventLoopGroup BOSS, WORK;

    static final String CONFIG_FILE = "config.xml";

    // the root element of the config xml document
    public static Element config;


    static {

        String os = System.getProperty("os.name").toLowerCase();
        LINUX = os.contains("linux");

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


        // event loop groups by os
        if (LINUX) {
            BOSS = new EpollEventLoopGroup(
                    CORES >= 32 ? 5 : CORES >= 16 ? 4 : CORES >= 4 ? 3 : 2
            );
            WORK = new EpollEventLoopGroup(CORES * 8);
        } else {
            BOSS = new NioEventLoopGroup(
                    CORES >= 32 ? 5 : CORES >= 16 ? 4 : CORES >= 4 ? 3 : 2
            );
            WORK = new NioEventLoopGroup(CORES * 8);
        }
        Runtime.getRuntime().removeShutdownHook(new Thread() {
            @Override
            public void run() {
                BOSS.shutdownGracefully();
                WORK.shutdownGracefully();
            }
        });

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

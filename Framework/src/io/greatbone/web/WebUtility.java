package io.greatbone.web;

import io.greatbone.Configurable;
import io.greatbone.Greatbone;
import io.greatbone.util.Roll;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * The containment of static resources and virtual hosts.
 */
public class WebUtility implements WebMBean, Configurable {

    // singleton instance
    static WebUtility WEB;

    final Element config;

    // static resources, shared among all virtual hosts
    final Roll<String, WebStatic> statics;

    // virtual hosts maintained by this JVM process
    final ArrayList<WebHost> vhosts = new ArrayList<>(4);

    WebUtility() {
        this.config = Greatbone.getConfigXmlTopTag("web");
        // load static resources recursively
        statics = new Roll<>(256);
        File resDir = new File("./RES/");
        if (resDir.exists() && resDir.isDirectory()) {
            gather("/", resDir);
        }
    }

    final void gather(String base, File dir) {
        //noinspection ConstantConditions
        for (File sub : dir.listFiles()) {
            if (sub.isDirectory()) {
                gather(base + sub.getName() + "/", sub);
            } else {
                int flen = (int) sub.length(); // NOTE: large file not supported
                byte[] content = new byte[flen];
                try {
                    FileInputStream in = new FileInputStream(sub);
                    if (flen != in.read(content)) { // never happen
                        throw new IOException("file reading error: " + sub.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WebStatic sta = new WebStatic(sub.getName(), content);
                statics.put(base + sta.key, sta);
            }
        }
    }

    WebStatic getStatic(String path) {
        return statics.get(path);
    }

    final <T extends WebHost> T putHost(String name, Class<T> clazz, Authorize authorize) {
        try {
            Constructor<T> ctor = clazz.getConstructor(WebUtility.class, String.class);
            T vhost = ctor.newInstance(this, name);
            vhost.authorize = authorize;
            vhosts.add(vhost);
            return vhost;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() throws IOException {
        for (WebHost host : vhosts) {
            host.start();
        }
    }

    @Override
    public void stop() throws IOException {
        for (WebHost host : vhosts) {
            host.stop();
        }
    }

    @Override
    public Element config() {
        return config;
    }

    public static <T extends WebHost> T addHost(String key, Class<T> hostClass, Authorize authorize) {
        if (WEB == null) {
            WEB = new WebUtility();
        }
        return WEB.putHost(key, hostClass, authorize);
    }

    public static final String
            NONE = "application/octet-stream",
            CER = "application/x-x509-ca-cert",
            CLASS = "application/java-vm",
            CSS = "text/css",
            DOC = "application/msword",
            GIF = "image/gif",
            HTM = "text/html",
            HTML = "text/html",
            JPEG = "image/jpeg",
            JPG = "image/jpeg",
            JS = "application/x-javascript",
            MOV = "video/quicktime",
            MP3 = "audio/mpeg",
            MPEG = "video/mpeg",
            MPG = "video/mpeg",
            PDF = "application/pdf",
            PNG = "image/png",
            PPT = "application/vnd.ms-powerpoint",
            QT = "video/quicktime",
            RTF = "application/rtf",
            SVG = "image/svg+xml",
            SWF = "application/x-shockwave-flash",
            TAR = "application/x-tar",
            TXT = "text/plain",
            XLS = "application/vnd.ms-excel",
            XML = "application/xml",
            ZIP = "application/zip";

    static final Roll<String, String> types = new Roll<>(256);

    static {
        types.put("", NONE);
        types.put("cer", CER);
        types.put("class", CLASS);
        types.put("css", CSS);
        types.put("doc", DOC);
        types.put("gif", GIF);
        types.put("htm", HTML);
        types.put("html", HTML);
        types.put("jpeg", JPEG);
        types.put("jpg", JPEG);
        types.put("js", JS);
        types.put("mov", MOV);
        types.put("mp3", MP3);
        types.put("mpeg", MPEG);
        types.put("mpg", MPEG);
        types.put("pdf", PDF);
        types.put("png", PNG);
        types.put("ppt", PPT);
        types.put("qt", QT);
        types.put("rtf", RTF);
        types.put("svg", SVG);
        types.put("swf", SWF);
        types.put("tar", TAR);
        types.put("txt", TXT);
        types.put("xls", XLS);
        types.put("xml", XML);
        types.put("zip", ZIP);
    }

    public static String getMimeType(String extension) {
        String mime = types.get(extension);
        return mime != null ? mime : NONE;
    }

    //
    // CLOCK
    //

    static final int SEC = 1000; // aligned to second

    static volatile long NOW = System.currentTimeMillis() / SEC * SEC;

    static final Thread CLOCK = new Thread("Clock") {
        public void run() {
            while (!interrupted()) {
                try {
                    Thread.sleep(1000);
                    NOW = System.currentTimeMillis() / SEC * SEC;
                } catch (InterruptedException e) {
                }
            }
        }
    };

    static {
        CLOCK.start();
    }

    public static long now() {
        return NOW;
    }

}
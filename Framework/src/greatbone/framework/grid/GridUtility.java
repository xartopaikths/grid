package greatbone.framework.grid;

import greatbone.framework.Configurable;
import greatbone.framework.Greatbone;
import greatbone.framework.util.Roll;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The singleton environment for in-memory data grid operations. This is the control center of grid-related assets and operations.
 */
public class GridUtility implements GridMBean, Configurable {

    // the singleton grid instance
    static GridUtility GRID;

    //
    // REGISTERED (fixed structures)

    // gathering of data schemas
    final Roll<Class<? extends GridRecord>, GridSchema> schemas = new Roll<>(64);

    // registered datasets
    final Roll<String, GridCache> datasets = new Roll<>(64);

    // registered filesets
    final Roll<String, GridFileCache> filesets = new Roll<>(16);

    //
    // CONFIGURED

    final Element config;

    // each corresponds to a member peer, in configuration order so that query results are consistent
    final Roll<String, GridEndPoint> endpoints = new Roll<>(256);
    GridServer server;

    volatile int status;

    // cluster status monitoring
    final Poller poller = new Poller();

    // async data persistence
    final Persister persister = new Persister();

    @SafeVarargs
    GridUtility(Class<? extends GridCache>... setcs) {

        this.config = Greatbone.getXmlTopTag("grid");

        // register datasets & filesets
        for (Class<? extends GridCache> c : setcs) {
            register(c);
        }
        // register as mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("grid:type=Grid");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // parse and validate config attributes
        String bind = config.getAttribute("bind");
        String interf = config.getAttribute("interfaces");
        List<String> addrs = parseAddresses(interf);
        for (String addr : addrs) {
            GridEndPoint last = endpoints.last();
            GridEndPoint new_;
            try {
                if (addr.equals(bind) || (bind.isEmpty() && isLocalAddress(InetAddress.getByName(addr)))) {
                    new_ = server = new GridServer(this, addr);
                } else {
                    new_ = new GridClient(this, addr, 10);
                }
                endpoints.put(addr, new_);
                if (last != null) {
                    last.next = new_;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // register datasets & filesets from classes.
    void register(Class<? extends GridCache> classes) {
        if (GridRecordCache.class.isAssignableFrom(classes)) {
            try { // create a dataset instance
                Class<? extends GridRecordCache> c = classes.asSubclass(GridRecordCache.class);
                Constructor<? extends GridCache> ctor = c.getConstructor(GridUtility.class);
                GridCache set = ctor.newInstance(this);
                datasets.put(set.name, set);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() throws IOException {
        for (int i = 0; i < endpoints.count(); i++) {
            endpoints.get(i).start();
        }
        persister.start();
        poller.start();
    }

    @Override
    public void stop() {
        persister.interrupt();
        poller.interrupt();
        for (int i = 0; i < endpoints.count(); i++) {
            endpoints.get(i).stop();
        }
    }

    @Override
    public void reload() {
    }

    @Override
    public void clear() {
    }

    @Override
    public void remove(String cache) {
    }

    @Override
    public void flush() {

    }

    @Override
    public Element config() {
        return config;
    }

    @SafeVarargs
    public static void initialize(Class<? extends GridCache>... setcs) throws IOException {
        if (GRID == null) {
            GRID = new GridUtility(setcs);
        }
        GRID.DDL();
        // start the grid service
        GRID.start();
    }

    public void DDL() {
        for (int i = 0; i < datasets.count(); i++) {
            GridCache dset = datasets.get(i);
            if (dset instanceof GridRecordCache) {
                System.out.println(((GridRecordCache) dset).CREATE());
            }
        }
    }

    static List<String> parseAddresses(String interf) {
        ArrayList<String> lst = new ArrayList<>(64);
        StringTokenizer st = new StringTokenizer(interf, ",");
        while (st.hasMoreTokens()) {
            String tok = st.nextToken().trim();
            int dot = tok.lastIndexOf('.');
            int hyphen = tok.indexOf('-', dot);
            if (hyphen != -1) {
                String pre = tok.substring(0, dot + 1);
                String from = tok.substring(dot + 1, hyphen);
                String to = tok.substring(hyphen + 1);
                int min = Integer.parseInt(from);
                int max = Integer.parseInt(to);
                for (int i = min; i <= max; i++) {
                    String addr = pre + i;
                    lst.add(addr);
                }
            } else {
                lst.add(tok);
            }
        }
        return lst;
    }

    static boolean isLocalAddress(InetAddress addr) {
        // check if the address is a valid special local or loop back
        if (addr.isAnyLocalAddress() || addr.isLoopbackAddress())
            return true;

        // check if the address is defined on any interface
        try {
            return NetworkInterface.getByInetAddress(addr) != null;
        } catch (SocketException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    <D extends GridRecord<D>> GridSchema<D> schema(Class<D> datc) {
        GridSchema<D> sch = schemas.get(datc);
        if (sch == null) {
            D inst;
            try {
                inst = datc.newInstance(); // create an instance in order to get its schema
                sch = inst.schema();
                schemas.put(datc, sch);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sch;
    }

    @SuppressWarnings("unchecked")
    <T extends GridRecordCache> T dataset(String key) {
        return (T) datasets.get(key);
    }

    public static <T extends GridRecordCache> T getDataSet(Class<T> clazz) {
        String key = clazz.getSimpleName().toLowerCase();
        return GRID.dataset(key);
    }

    /**
     * For monitoring status changes of peers and pages in the cluster.
     */
    final class Poller extends Thread {

        static final int INTERVAL = 7 * 1000;

        Poller() {
            super("Monitor");
        }

        @Override
        public void run() {
            while (status != 0) {

                for (int i = 0; i < endpoints.count(); i++) {
                    GridEndPoint peer = endpoints.get(i);
                    if (peer instanceof GridClient) {
                        try {
//                            ((GridClient) peer).call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                // sleep
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

    }

    /**
     * For asynchronous batch persistence of grid data into the underlying database.
     */
    final class Persister extends Thread {

        static final int INTERVAL = 60 * 1000;

        Persister() {
            super("Persister");
        }

        @Override
        public void run() {

            while (!isInterrupted()) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                }
                for (int i = 0; i < datasets.count(); i++) {
//                    datasets.get(i).load();
                }
            }
        }
    }

}

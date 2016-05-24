package greatbone.framework.grid;

import greatbone.framework.Configurable;
import greatbone.framework.Greatbone;
import greatbone.framework.util.SpinWait;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * An abstract dataset that can be either a recordset or a fileset.
 */
public abstract class GridCache<P extends GridPartition> implements GridCacheMBean, Configurable {

    // the grid instance
    final GridUtility grid;

    // the identifying name
    final String name;

    // configurative xml element, can be null when no store on this VM for a registered set
    final Element config;

    // spec for local data, null when no local attribute is found, empty when the local atrribute is empty
    final List<String> localspec;

    //
    // partitions
    final SpinWait sync = new SpinWait();
    P[] partitions;
    int pcount;

    GridCache(GridUtility grid) {
        this.grid = grid;

        // derive the key
        Class c = getClass();
        this.name = c.getSimpleName().toLowerCase(); // from class name

        // derive the config tag
        Class p = c;
        String tag = null;
        while ((c = c.getSuperclass()) != GridCache.class) {
            String n = c.getSimpleName().toLowerCase();
            if (n.startsWith("grid") && n.endsWith("set") && n.length() > 8) {
                tag = n.substring(4);
            }
        }
        this.config = Greatbone.getChildElementOf(grid.config, tag, name);

        // parse the local attribute
        List<String> lst = null;
        if (config != null) {
            String attlocal = config.getAttribute("local");
            if (!attlocal.isEmpty()) {
                lst = new ArrayList<>(16);
                StringTokenizer st = new StringTokenizer(attlocal, ",");
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken().trim();
                    if (!tok.isEmpty()) lst.add(tok);
                }
            }
        }
        this.localspec = lst;
    }

    public abstract void flush();

    @Override
    public Element config() {
        return config;
    }


    public P get(String partition) {
        sync.enterRead();
        try {
            for (int i = 0; i < pcount; i++) {
                P part = partitions[i];
                if ((partition == null)) { // equals
                    if (part.id == null) {
                        return part;
                    }
                } else if (partition.equals(part.id)) {
                    return part;
                }
            }
            return null;
        } finally {
            sync.exitRead();
        }
    }

    P locate(String dataKey) {
        if (dataKey != null) {
            sync.enterRead();
            try {
                for (int i = 0; i < pcount; i++) {
                    P part = partitions[i];
                    if (dataKey.startsWith(part.id)) { // starts with
                        return part;
                    }
                }
            } finally {
                sync.exitRead();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    void add(P v) {
        sync.enterWrite();
        try {
            int len = partitions.length;
            if (pcount == len) {
                P[] new_ = (P[]) new Object[len * 2];
                System.arraycopy(partitions, 0, new_, 0, len);
                partitions = new_;
            }
            partitions[pcount++] = v;
        } finally {
            sync.exitWrite();
        }
    }

}

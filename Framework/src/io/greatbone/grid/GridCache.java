package io.greatbone.grid;

import io.greatbone.Configurable;
import io.greatbone.Greatbone;
import io.greatbone.util.SpinWait;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * An abstract dataset that can be either a recordset or a fileset.
 */
public abstract class GridCache<S extends GridShard> implements GridCacheMBean, Configurable {

    // the grid instance
    final GridUtility grid;

    // the identifying name
    final String name;

    // configurative xml element, can be null when no store on this VM for a registered set
    final Element config;

    // spec for local data, null when no local attribute is found, empty when the local atrribute is empty
    final List<String> shardsSpec;

    //
    // partitions
    final SpinWait sync = new SpinWait();
    S[] shards;
    int count;

    GridCache(GridUtility grid) {
        this.grid = grid;

        // derive name from the class name
        Class c = getClass();
        this.name = c.getSimpleName().toLowerCase(); // from class name
        this.config = Greatbone.getXmlSubElement(grid.config, "cache", name);

        // parse the shards attribute
        List<String> lst = null;
        if (config != null) {
            String att = config.getAttribute("shards");
            if (!att.isEmpty()) {
                lst = new ArrayList<>(32);
                StringTokenizer st = new StringTokenizer(att, ",");
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken().trim();
                    if (!tok.isEmpty()) lst.add(tok);
                }
            }
        }
        this.shardsSpec = lst;
    }

    public abstract void flush();

    @Override
    public Element config() {
        return config;
    }

    public S getShard(String shardKey) {
        sync.enterRead();
        try {
            for (int i = 0; i < count; i++) {
                S shard = shards[i];
                if (shardKey == null) { // equals
                    if (shard.key == null) {
                        return shard;
                    }
                } else if (shardKey.equals(shard.key)) {
                    return shard;
                }
            }
            return null;
        } finally {
            sync.exitRead();
        }
    }

    S locateShard(String dataKey) {
        if (dataKey != null) {
            sync.enterRead();
            try {
                for (int i = 0; i < count; i++) {
                    S shard = shards[i];
                    if (dataKey.startsWith(shard.key)) { // starts with
                        return shard;
                    }
                }
            } finally {
                sync.exitRead();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    void addShard(S shard) {
        sync.enterWrite();
        try {
            int len = shards.length;
            if (count == len) {
                S[] new_ = (S[]) new Object[len * 2];
                System.arraycopy(shards, 0, new_, 0, len);
                shards = new_;
            }
            shards[count++] = shard;
        } finally {
            sync.exitWrite();
        }
    }

}

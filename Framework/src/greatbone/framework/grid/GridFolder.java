package greatbone.framework.grid;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * evictable file  cache
 */
class GridFolder extends GridShard {

    String name;

    final ConcurrentHashMap<String, GridFile> files;

    GridFolder(int capacity) {
        files = new ConcurrentHashMap<>(capacity);
    }

    public GridFile get(String key) throws IOException {
        GridFile info = files.get(key);
        if (info == null) {
            // It's OK to construct a Heavy that ends up not being used
            info = new GridFile(Paths.get(key));
            GridFile other = files.putIfAbsent(key, info);
            if (other != null) {
                info = other;
            }
        }
        return info;
    }

}

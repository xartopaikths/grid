package greatbone.framework.grid;

/**
 * The management interface.for a particular cache.
 */
public interface GridCacheMBean {

    void reload();

    void clear();

    void flush();

}

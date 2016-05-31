package io.greatbone.grid;

/**
 * The management interface for a partition.
 */
public interface GridLocalMBean {

    void flush();

    void reload();

    void clear();

    /**
     * @return the number of data items cached
     */
    int getCount();

}

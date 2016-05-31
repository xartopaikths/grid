package io.greatbone.grid;

import java.io.IOException;

/**
 * The management interface for the local grid service.
 */
public interface GridMBean {

    void start() throws IOException;

    void stop();

    void flush();

    void reload();

    void clear();

}

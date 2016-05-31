package io.greatbone.grid;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The connectivity correponding to a particular networking peer.
 */
abstract class GridEndPoint {

    // the container grid instance
    final GridUtility grid;

    final String interf;

    final InetSocketAddress address;

    // the peer next to this
    GridEndPoint next;

    // is currently available
    boolean available;

    GridEndPoint(GridUtility grid, String interf) {
        this.grid = grid;
        this.interf = interf;
        this.address = new InetSocketAddress(interf, 8192);
    }

    public String interf() {
        return interf;
    }

    public void callasd() throws IOException {

        final Charset charset = Charset.forName("utf-8");
    }

    public void start() throws IOException {

    }

    public void stop() {

    }

}


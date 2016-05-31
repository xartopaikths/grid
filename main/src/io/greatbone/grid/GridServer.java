package io.greatbone.grid;

import io.greatbone.Greatbone;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * The local server connectivity and RPC endpoint..
 */
class GridServer extends GridEndPoint {

    // the connection acceptor
    volatile Channel acceptchan;

    GridServer(GridUtility grid, String interf) {
        super(grid, interf);
    }

    public void start() throws IOException {

        // Create an accept listener.

//        // server options
//        OptionMap options = OptionMap.builder()
//                .set(Options.RECEIVE_BUFFER, (1024 * 1024 * 4)) // large buffer for high speed LAN
//                .set(Options.SEND_BUFFER, (1024 * 1024 * 4))
//                .set(Options.BACKLOG, 512)
//                .getMap();

        // create the server.

    }

    void handle(GridContext gc) {

    }

    // handle a query call requested by the remote client
    <D> void doBrowse() {

    }

    // handle a query call requested by the remote client
    <D extends GridData<D>> void doQuery(String dataset, String page, Critera<D> filter) {

    }

}

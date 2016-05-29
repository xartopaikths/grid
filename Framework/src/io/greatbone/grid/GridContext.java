package io.greatbone.grid;

import org.xnio.conduits.ConduitStreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;

/**
 * A call & reply exchange between two grid endpoints.
 */
class GridContext implements AutoCloseable {

    Channel conn;

    // the header part of the call
    ByteBuffer call;

    // the header part of the reply
    ByteBuffer reply;

    // body input for either call or reply
    InputStream input;

    // body output for either call or reply
    OutputStream output;

    GridContext() {
    }

    GridContext(Channel conn) {
        this.conn = conn;
    }

    final InputStream getInputStream() {
        if (input == null) {
            input = new InputStream() {
                final ConduitStreamSourceChannel chan = conn.getSourceChannel();
                ByteBuffer buf;

                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
        }
        return input;
    }

    final OutputStream getOutputStream() {
        if (output == null) {
            final OutputStream ostream = new OutputStream() {
                final ConduitStreamSinkChannel chan = conn.getSinkChannel();
                ByteBuffer buf;

                @Override
                public void write(int b) throws IOException {
                    chan.write(buf);
                }
            };
        }
        return output;
    }

    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

}


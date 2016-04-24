package greatbone.framework.grid;

import io.undertow.server.DefaultByteBufferPool;
import org.xnio.StreamConnection;
import org.xnio.conduits.ConduitStreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * A call & reply exchange between two grid endpoints.
 */
class GridContext implements AutoCloseable{

    // buffer pool for calls, replies and streams
    static final DefaultByteBufferPool
            CALLP = new DefaultByteBufferPool(true, 1024 * 64, -1, 4),
            REPLYP = new DefaultByteBufferPool(true, 64, -1, 4),
            STREAMP = new DefaultByteBufferPool(true, 64, -1, 4);

    StreamConnection conn;

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

    GridContext(StreamConnection conn) {
        this.conn = conn;
        this.call = CALLP.allocate().getBuffer();
        this.reply = REPLYP.allocate().getBuffer();
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


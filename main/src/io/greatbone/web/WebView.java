package io.greatbone.web;

import io.greatbone.Out;
import io.greatbone.grid.GridData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.IOException;
import java.util.List;

/**
 */
@SuppressWarnings("unchecked")
public abstract class WebView<O extends Out<O>> implements Out<O> {

    static final int OUTBUF_INITIAL = 4 * 1024;

    static final int OUTBUF_MAX = 64 * 1024;

    // possible chars for representing a number
    static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static final char MINUS = '-';

    static final short[] SHORTS = {10, 100, 1000, 10000};

    static final int[] INTS = {10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 1000000000};

    static final long[] LONGS = {10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 1000000000, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 100000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

    // the output buffer
    ByteBuf buf;

    protected abstract String ctype();

    protected abstract void render();

    //
    // OUTPUT FUNCTIONS

    void write(char c) {

        ByteBuf buf = this.buf;
        if (buf == null) {
            buf = this.buf = PooledByteBufAllocator.DEFAULT.buffer(OUTBUF_INITIAL, OUTBUF_MAX);
        }

        // UTF-8 encoding but without surrogate support
        if (c < 0x80) {
            // have at most seven bits
            buf.writeByte((byte) c);
        } else if (c < 0x800) {
            // 2 text, 11 bits
            buf.writeByte((byte) (0xc0 | (c >> 6)));
            buf.writeByte((byte) (0x80 | (c & 0x3f)));
        } else {
            // 3 text, 16 bits
            buf.writeByte((byte) (0xe0 | ((c >> 12))));
            buf.writeByte((byte) (0x80 | ((c >> 6) & 0x3f)));
            buf.writeByte((byte) (0x80 | (c & 0x3f)));
        }
    }

    public O $(CharSequence v) {
        if (v != null) {
            for (int i = 0; i < v.length(); i++) {
                write(v.charAt(i));
            }
        }
        return (O) this;
    }

    @Override
    public O $(boolean v) throws IOException {
        return (O) this;
    }

    public O $(short v) throws IOException {
        int x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = SHORTS.length - 1; i >= 0; i--) {
            short base = SHORTS[i];
            int q = x / base;
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[x]); // last reminder
        return (O) this;
    }

    public O $(int v) throws IOException {
        int x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = INTS.length - 1; i >= 0; i--) {
            int base = INTS[i];
            int q = x / base;
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[x]); // last reminder
        return (O) this;
    }

    public O $(long v) throws IOException {
        long x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = LONGS.length - 1; i >= 0; i--) {
            long base = LONGS[i];
            int q = (int) (x / base);
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[(int) x]); // last reminder
        return (O) this;
    }

    public O $obj(GridData dat) {

        return (O) this;
    }

    public O $obj(GridData dat, int flags) {

        return (O) this;
    }

    public O $obj(List<GridData> data) {

        return (O) this;
    }

    public O $obj(List<GridData> datalst, int flags) {

        return (O) this;
    }


}

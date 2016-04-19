package greatbone.framework.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * A sin-wait implementation of lock-free synchronization of read and write operations. It suits for prominent reads rare wites situations.
 */
public class SpinWait {

    // for atomic operation
    static final Unsafe UNSAFE;

    // offset of the sync field
    static final long SYNC;

    static {
        try { // a common trick to get HotSpot's internal Unsafe
            Field fld = Unsafe.class.getDeclaredField("theUnsafe");
            fld.setAccessible(true);
            UNSAFE = (Unsafe) fld.get(null);
            SYNC = UNSAFE.objectFieldOffset(SpinWait.class.getDeclaredField("sync"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    // flags for read/write sync, -1 = writing, 0 = nothing, n = reading count
    volatile int sync;

    final int cycle;

    public SpinWait() {
        this(16);
    }

    public SpinWait(int cycle) {
        this.cycle = cycle;
    }

    public void enterRead() {
        int prev;
        for (; ; ) {
            prev = sync;
            if (prev >= 0 && UNSAFE.compareAndSwapInt(this, SYNC, prev, prev + 1)) {
                return; // the only return point is after increment
            }
            elapse();
        }
    }

    public void exitRead() {
        int prev;
        for (; ; ) {
            prev = sync;
            if (prev > 0 && UNSAFE.compareAndSwapInt(this, SYNC, prev, prev - 1)) {
                return; // the only return point is after decrement
            }
            elapse();
        }
    }

    public void enterWrite() {
        for (; ; ) {
            if (UNSAFE.compareAndSwapInt(this, SYNC, 0, -1)) {
                return; // the only return point is after switching
            }
            elapse();
        }
    }

    public void exitWrite() {
        for (; ; ) {
            if (UNSAFE.compareAndSwapInt(this, SYNC, -1, 0)) {
                return; // the only return point is after switching
            }
            elapse();
        }
    }

    // elapse a number of cycles
    void elapse() {
        for (int i = 0; i < cycle; i++) ;
    }

}

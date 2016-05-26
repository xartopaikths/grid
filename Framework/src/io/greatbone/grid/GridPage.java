package io.greatbone.grid;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An abstract data page, conceptually containing data entries belonged.
 *
 * @param <R> type of record
 */
public abstract class GridPage<R extends GridData<R>> extends GridShard {

    // TODO marks of insertion timestamp for records

    // the parent dataset
    final GridDataCache<R> parent;

    // the page id
    final String id;

    // for the key-generating algorithm
    final AtomicInteger serial;

    GridPage(final GridDataCache<R> parent, String id) {
        this.parent = parent;
        this.id = id;
        this.serial = new AtomicInteger(0);
    }

    public String id() {
        return id;
    }

    public abstract R get(String key);

    public abstract R put(String key, R data);

    public abstract R search(Critera<R> filter);

    /**
     * create a key aocrdding to the rule specific to this partion
     *
     * @return
     */
    protected String newKey() {
        return null;
    }

//    int meet(K akey) {
//        int cmp = akey.compareTo(id);
//        if (cmp >= 0 && akey.startsWith(id)) {
//            return 0;
//        }
//        return cmp;
//    }

}
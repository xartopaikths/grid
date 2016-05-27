package io.greatbone.sample;

import io.greatbone.grid.*;

/**
 * A demanding task posted by someone.
 */
public class Task extends GridData<Task> {

    //
    // COLUMNS

    static final KEY ID = new KEY(18); // jxnc00020001

    static final KEY SHOPID = new KEY(18); // jxnc00003e

    static final SHORT STATUS = new SHORT();

    static final KEYSET ORDERS = new KEYSET(12, 50);

    static final DECIMAL ASK = new DECIMAL(2); // ask price from seller

    static final DECIMAL BID = new DECIMAL(2); // bid price from deliverer

    static final DECIMAL X = new DECIMAL(2);

    static final DECIMAL Y = new DECIMAL(2);


    public String getId() {
        return ID.getValue(this);
    }

    public void setId(String v) {
        ID.putValue(this, v);
    }


    //
    // SCHEMA

    @Override
    protected GridSchema<Task> schema() {
        return SCHEMA;
    }

    static final GridSchema<Task> SCHEMA = new GridSchema<>(Task.class);

}



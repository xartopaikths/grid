package greatbone.sample;

import greatbone.framework.grid.*;

/**
 * A devliery order, created from scatch, or derived from a number of purchase orders
 */
public class Delivery extends GridData<Delivery> {

    //
    // COLUMNS

    static final KEY ID = new KEY(18); // jxnc00020001

    static final KEY SHOPID = new KEY(18); // JXNC00003E

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
    protected GridSchema<Delivery> schema() {
        return SCHEMA;
    }

    static final GridSchema<Delivery> SCHEMA = new GridSchema<>(Delivery.class);

}



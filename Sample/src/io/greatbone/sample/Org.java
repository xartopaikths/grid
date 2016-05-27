package io.greatbone.sample;

import io.greatbone.grid.*;
import io.greatbone.web.WebPrincipal;
import io.greatbone.web.WebSpace;

/**
 * A shop that takes orders and issues delivery tasks.
 */
@Storage
public class Org extends GridData<Org> implements WebPrincipal, WebSpace {

    // COLUMNS

    static final KEY ID = new KEY(8); // jxnc003f

    static final STRING NAME = new STRING(10);

    static final STRING CREDENTIAL = new STRING(10);

    static final STRING ADDRESS = new STRING(16);

    static final DECIMAL X = new DECIMAL(2);

    static final DECIMAL Y = new DECIMAL(2);

    static final STOCK[] STOCKS = {
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
    };

    @Override
    public String space() {
        return ID.getValue(this);
    }

    @Override
    public String getName() {
        return ID.getValue(this);
    }

    @Override
    public String getCredential() {
        return CREDENTIAL.getValue(this);
    }

    @Override
    public boolean check(String scope, int roles) {
        return false;
    }

    @Override
    protected GridSchema<Org> schema() {
        return SCHEMA;
    }

    static final GridSchema<Org> SCHEMA = new GridSchema<>(Org.class);

}

class STOCK extends STRUCT {

    static final STRING CATEGORY = new STRING(12);

    final STRING ITEM = new STRING(12);

    final DECIMAL PRICE = new DECIMAL(2);

}





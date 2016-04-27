package greatbone.sample;

import greatbone.framework.grid.*;
import greatbone.framework.web.Principal;
import greatbone.framework.web.Space;

/**
 * A shop that takes order and issues delivery tasks.
 */
public class Shop extends GridData<Shop> implements Principal, Space {

    // COLUMNS

    static final KEY ID = new KEY(10); // JXNC00003F

    static final STRING NAME = new STRING(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING ADDRESS = new STRING(12);

    static final DECIMAL X = new DECIMAL(2);

    static final DECIMAL Y = new DECIMAL(2);

    static final STOCK[] STOCKS = {
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
            new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(), new STOCK(),
    };

    @Override
    public String spaceid() {
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
    public int roles() {
        return 0;
    }

    @Override
    protected GridSchema<Shop> schema() {
        return SCHEMA;
    }

    static final GridSchema<Shop> SCHEMA = new GridSchema<>(Shop.class);

}

class STOCK extends STRUCT {

    static final STRING CATEGORY = new STRING(12);

    final STRING ITEM = new STRING(12);

    final DECIMAL PRICE = new DECIMAL(2);


}




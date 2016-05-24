package greatbone.sample;

import greatbone.framework.grid.*;
import greatbone.framework.web.WebPrincipal;
import greatbone.framework.web.WebSpace;

/**
 * A shop that takes orders and issues delivery tasks.
 */
@Storage
public class Shop extends GridRecord<Shop> implements WebPrincipal, WebSpace {

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





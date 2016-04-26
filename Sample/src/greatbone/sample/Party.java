package greatbone.sample;

import greatbone.framework.grid.*;
import greatbone.framework.web.Principal;
import greatbone.framework.web.Space;

/**
 * A shop or other type of organizational unit.
 */
public class Party extends GridData<Party> implements Principal, Space {

    // COLUMNS

    static final KEY ID = new KEY(10);

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
    protected GridSchema<Party> schema() {
        return SCHEMA;
    }

    static final GridSchema<Party> SCHEMA = new GridSchema<>(Party.class);

}

class STOCK extends STRUCT {

    static final STRING CATEGORY = new STRING(12);

    final STRING ITEM = new STRING(12);

    final DECIMAL PRICE = new DECIMAL(2);


}





package greatbone.sample;

import greatbone.framework.grid.*;
import greatbone.framework.web.WebPrincipal;
import greatbone.framework.web.WebZone;

/**
 * A shop that takes orders and issues delivery tasks.
 */
public class Client extends GridData<Client> implements WebPrincipal, WebZone {

    // COLUMNS

    static final KEY ID = new KEY(10); // JXNC00003F

    static final STRING NAME = new STRING(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING ADDRESS = new STRING(12);

    static final DECIMAL X = new DECIMAL(2);

    static final DECIMAL Y = new DECIMAL(2);

    static final KEYSET ORDERS = new KEYSET(2,12);

    @Override
    public String zoner() {
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
    protected GridSchema<Client> schema() {
        return SCHEMA;
    }

    static final GridSchema<Client> SCHEMA = new GridSchema<>(Client.class);

}





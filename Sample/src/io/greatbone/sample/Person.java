package io.greatbone.sample;

import io.greatbone.grid.*;
import io.greatbone.web.WebPrincipal;
import io.greatbone.web.WebSpace;

/**
 * A shop that takes orders and issues delivery tasks.
 */
public class Person extends GridData<Person> implements WebPrincipal, WebSpace {

    // COLUMNS

    static final KEY LOGIN = new KEY(10); // JXNC00003F

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(10);

    static final KEY NUM = new KEY(10);

    static final STRING ADDRESS = new STRING(16);

    static final DECIMAL X = new DECIMAL(2); // present X position

    static final DECIMAL Y = new DECIMAL(2); // present Y position

    static final KEYSET ORDERS = new KEYSET(10, 20);

    @Override
    public String space() {
        return LOGIN.getValue(this);
    }

    @Override
    public String getName() {
        return LOGIN.getValue(this);
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
    protected GridSchema<Person> schema() {
        return SCHEMA;
    }

    static final GridSchema<Person> SCHEMA = new GridSchema<>(Person.class);

}





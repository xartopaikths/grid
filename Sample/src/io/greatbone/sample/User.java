package io.greatbone.sample;

import io.greatbone.grid.GridData;
import io.greatbone.grid.GridSchema;
import io.greatbone.grid.KEY;
import io.greatbone.grid.STRING;
import io.greatbone.web.WebPrincipal;

/**
 * An administrative mamaber of the platform that can be either a management staff or an agent.
 */
public class User extends GridData<User> implements WebPrincipal {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(12);

    //
    // ACCESSORS

    public int getID() {
        return 0;
    }

    public int compareLogin(String v) {
        return ID.tryValue(this, v);
    }

    public int compareCredential(String v) {
        return -1;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getCredential() {
        return null;
    }

    @Override
    public int roles() {
        return 0;
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<User> schema() {
        return SCHEMA;
    }

    static final GridSchema<User> SCHEMA = new GridSchema<>(User.class);

}

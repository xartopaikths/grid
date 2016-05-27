package io.greatbone.sample;

import io.greatbone.grid.*;
import io.greatbone.web.WebPrincipal;

/**
 * An administrative mamaber of the platform that can be either a management staff or an agent.
 */
public class Staffer extends GridData<Staffer> implements WebPrincipal {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(12);

    static final PERM[] PERMS = new PERM[]{
            new PERM(), new PERM(), new PERM(), new PERM(), new PERM(), new PERM(), new PERM(), new PERM()
    };


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
    public boolean check(String scope, int roles) {
        return false;
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<Staffer> schema() {
        return SCHEMA;
    }

    static final GridSchema<Staffer> SCHEMA = new GridSchema<>(Staffer.class);

}

/**
 */
class PERM extends STRUCT {

    final STRING SPACE = new STRING(12);

    final SHORT ROLES = new SHORT();

}





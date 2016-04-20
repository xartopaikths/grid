package greatbone.sample;

import greatbone.framework.grid.GridData;
import greatbone.framework.grid.GridSchema;
import greatbone.framework.grid.KEY;
import greatbone.framework.grid.STRING;
import greatbone.framework.web.Principal;
import greatbone.framework.web.Space;

/**
 * A shop or other type of organizational unit.
 */
public class Org extends GridData<Org> implements Principal, Space {

    // COLUMNS

    static final KEY ID = new KEY(10);

    static final STRING NAME = new STRING(12);

    static final STRING PASSWORD = new STRING(12);

    @Override
    public String spaceid() {
        return ID.get(this);
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

    @Override
    protected GridSchema<Org> schema() {
        return SCHEMA;
    }

    static final GridSchema<Org> SCHEMA = new GridSchema<>(Org.class);

}



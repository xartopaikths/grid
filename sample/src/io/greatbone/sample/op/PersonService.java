package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebParent;
import io.greatbone.web.WebService;

/**
 * The order management handler.
 */
public class PersonService extends WebService {

    public PersonService(WebHost host, WebParent parent) {
        super(host, parent);

    }

    @Override
    public void _(WebContext wc) {
    }

}

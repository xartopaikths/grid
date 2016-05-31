package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebActivity;
import io.greatbone.web.WebHost;

/**
 * The order management handler.
 */
public class PersonsActivity extends WebActivity {


    public PersonsActivity(WebHost host, WebActivity parent) {
        super(host, parent);

    }

    @Override
    public void default_(WebContext wc) {
    }

}

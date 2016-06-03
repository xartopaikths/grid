package io.greatbone.sample.op;

import io.greatbone.sample.Org;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebService;
import io.greatbone.web.WebServiceHub;

/**
 */
public class OpServiceHub extends WebServiceHub<Org> {

    OpServiceHub(WebHost host) {
        super(host);
    }

    @Override
    protected Org resolve(String zone) {
        return null;
    }

    @Override
    public WebService locateSub(String key, WebContext wc) {
        return null;
    }

    @Override
    public void Get(WebContext<Org> wc) throws Exception {
        super.Get(wc);
    }

}

package io.greatbone.sample.op;

import io.greatbone.sample.Org;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebService;
import io.greatbone.web.WebServiceHub;

/**
 */
public class OrgServiceHub extends WebServiceHub<Org> {

    public OrgServiceHub(WebHost host) {
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
    public void main(WebContext<Org> wc) throws Exception {
    }

}

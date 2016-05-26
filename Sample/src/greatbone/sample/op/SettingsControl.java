package greatbone.sample.op;

import greatbone.framework.web.WebSpace;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebVHost;

/**
 * The menu management handler.
 */
public class SettingsControl extends WebControl {

    public SettingsControl(WebVHost host, WebControl parent) {
        super(host, parent);
    }

    @Override
    public void default_(WebContext exch) {
        WebSpace shopid = exch.space();

    }

    public void Post(WebContext exch) {
        WebSpace shopid = exch.space();

    }

}

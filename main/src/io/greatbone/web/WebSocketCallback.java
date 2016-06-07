package io.greatbone.web;

/**
 */
public interface WebSocketCallback<Z extends WebZone> {

    void onCreate(WebContext<Z> wc);

    void onMsg(WebContext<Z> wc);

    void onClose(WebContext<Z> wc);

}

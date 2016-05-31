package io.greatbone.web;

import io.greatbone.Out;

import java.io.IOException;

/**
 */
@SuppressWarnings("unchecked")
public abstract class WebPrint<O extends Out<O>> implements Out<O> {

    // the wrapped output object
    WebContext wctx;

    protected abstract String ctype();

    protected abstract void print() throws IOException;

    @Override
    public O $(boolean v) throws IOException {
        wctx.$(v);
        return (O) this;
    }

    @Override
    public O $(short v) throws IOException {
        wctx.$(v);
        return (O) this;
    }

    @Override
    public O $(int v) throws IOException {
        wctx.$(v);
        return (O) this;
    }

    @Override
    public O $(long v) throws IOException {
        wctx.$(v);
        return (O) this;
    }

    @Override
    public O $(CharSequence v) throws IOException {
        wctx.$(v);
        return (O) this;
    }

}

package io.greatbone.sample;

import io.greatbone.web.WebPrint;

import java.io.IOException;

/**
 */
public class JSON extends WebPrint<JSON> {

    @Override
    protected String ctype() {
        return "application/json";
    }

    @Override
    public void print() throws IOException {

    }

}

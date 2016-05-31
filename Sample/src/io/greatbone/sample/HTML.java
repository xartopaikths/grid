package io.greatbone.sample;

import io.greatbone.web.WebPrint;

import java.io.IOException;

/**
 */
public abstract class HTML extends WebPrint<HTML> {

    @Override
    protected String ctype() {
        return "text/html; charset=UTF-8";
    }

    @Override
    public void print() throws IOException {
        $("<!DOCTYPE HTML>");
        $("<html>");
        $("<head>");
        $("<link rel=\"stylesheet\" type=\"text/css\" href=\"/jqm.structure.min.css\" media=\"screen\" />");
        $("<link rel=\"stylesheet\" type=\"text/css\" href=\"/jqm.theme.min.css\" media=\"screen\" />");
        $("<script type=\"text/javascript\"  src=\"jqm.min.js\"></script>");
        $("</head>");

        body();

        $("<body>");
        $("</body>");
        $("</html>");
    }

    protected void body() throws IOException {

        $("<a class=\"button\" href=\"#\">An Active Button</a>\n" +
                "<button class=\"button button-inactive\">An Active Button</button>");

        $("<div class=\"button-group\">\n" +
                "  <a class=\"secondary button\">View</a>\n" +
                "  <a class=\"success button\">Edit</a>\n" +
                "  <a class=\"warning button\">Share</a>\n" +
                "  <a class=\"alert button\">Delete</a>\n" +
                "</div>");

    }

    public void $esc(char c) {

    }

    public void $esc(CharSequence cs) {

    }

}

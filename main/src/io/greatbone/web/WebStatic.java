package io.greatbone.web;

/**
 * A static resource that resides under the RES folder or one of its decendent folder.
 */
class WebStatic {

    final String key;

    // content type
    final String ctype;

    // content byte array
    final byte[] content;

    public WebStatic(String key, byte[] content) {
        this.key = key;
        final int dot = key.lastIndexOf('.');
        final String extension = key.substring(dot + 1);
        this.ctype = WebUtility.getMimeType(extension);
        this.content = content;
    }

    final String key() {
        return key;
    }

    final String ctype() {
        return ctype;
    }

    final int length() {
        return content.length;
    }

    final byte[] content() {
        return content;
    }

}

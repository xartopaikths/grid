package io.greatbone.web;

import io.greatbone.util.Roll;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * The decoded name/value pairs as encoded in <em>www-form-urlencoded</em> format.
 */
public class FormData {

    final Roll<String, Object> params = new Roll<>(16);

    // parse body
    public FormData(ByteBuf buf) {
    }

    // parse query
    public FormData(String str) {
        final int len = str.length();
        String name = null;
        int pos = 0; // beginning of each parse
        char c;
        int i;
        for (i = pos; i < len; i++) {
            c = str.charAt(i);
            if (c == '=' && name == null) {
                if (pos != i) {
                    name = str.substring(pos, i);
                }
                pos = i + 1; // adjust the beginning pos
            } else if (c == '&' || c == ';') {
                if (name == null && pos != i) {
                    addParam(str.substring(pos, i), ""); // empty value
                } else if (name != null) {
                    if (!addParam(name, decodeComponent(str.substring(pos, i), null))) {
                        return;
                    }
                    name = null;
                }
                pos = i + 1;
            }
        }

        if (pos != i) {  // Are there characters we haven't dealt with?
            if (name == null) {     // Yes and we haven't seen any `='.
//                addParam(params, decodeComponent(s.substring(pos, i), charset), "");
            } else {                // Yes and this must be the last value.
//                addParam(params, name, decodeComponent(s.substring(pos, i), charset));
            }
        } else if (name != null) {  // Have we seen a name without value?
//            addParam(params, name, "");
        }
    }

    @SuppressWarnings("unchecked")
    boolean addParam(String name, String value) {
        Object obj = params.get(name);
        if (obj == null) {
            params.put(name, value);
        } else if (obj instanceof String) { // has a previous value
            ArrayList<String> lst = new ArrayList<>(8);
            lst.add((String) obj);
            lst.add(value);
            params.put(name, lst);
        } else { // already an arraylist
            ((ArrayList<String>) obj).add(value);
        }
        return true;
    }

    public static String decodeComponent(final String s) {
        return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
    }

    public static String decodeComponent(final String s, final Charset charset) {
        if (s == null) {
            return "";
        }
        final int size = s.length();
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            final char c = s.charAt(i);
            if (c == '%' || c == '+') {
                modified = true;
                break;
            }
        }
        if (!modified) {
            return s;
        }
        final byte[] buf = new byte[size];
        int pos = 0;  // position in `buf'.
        for (int i = 0; i < size; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '+':
                    buf[pos++] = ' ';  // "+" -> " "
                    break;
                case '%':
                    if (i == size - 1) {
                        throw new IllegalArgumentException("unterminated escape sequence at end of string: " + s);
                    }
                    c = s.charAt(++i);
                    if (c == '%') {
                        buf[pos++] = '%';  // "%%" -> "%"
                        break;
                    }
                    if (i == size - 1) {
                        throw new IllegalArgumentException("partial escape sequence at end of string: " + s);
                    }
                    c = decodeHexNibble(c);
                    final char c2 = decodeHexNibble(s.charAt(++i));
                    if (c == Character.MAX_VALUE || c2 == Character.MAX_VALUE) {
                        throw new IllegalArgumentException(
                                "invalid escape sequence `%" + s.charAt(i - 1) + s.charAt(i) + "' at index " + (i - 2) + " of: " + s);
                    }
                    c = (char) (c * 16 + c2);
                    // Fall through.
                default:
                    buf[pos++] = (byte) c;
                    break;
            }
        }
        return new String(buf, 0, pos, charset);
    }

    private static char decodeHexNibble(final char c) {
        if ('0' <= c && c <= '9') {
            return (char) (c - '0');
        } else if ('a' <= c && c <= 'f') {
            return (char) (c - 'a' + 10);
        } else if ('A' <= c && c <= 'F') {
            return (char) (c - 'A' + 10);
        } else {
            return Character.MAX_VALUE;
        }
    }

    public String getString(String name) {
        return (String) params.get(name);
    }

    public int getInt(String name) {
        int n = -1;
        Object obj = params.get(name);
        if (obj != null) {
            try {
                n = Integer.parseInt((String) obj, 10);
            } catch (NumberFormatException e) {
            }
        }
        return n;
    }

    @SuppressWarnings("unchecked")
    public String[] getStrings(String name) {
        Object obj = params.get(name);
        if (obj == null) {
            return null;
        } else if (obj instanceof ArrayList) {
            List<String> lst = (List<String>) obj;
            return lst.toArray(new String[lst.size()]);
        } else {
            return new String[]{(String) obj};
        }
    }

    @SuppressWarnings("unchecked")
    public int[] getInts(String name) {
        Object obj = params.get(name);
        if (obj == null) {
            return null;
        } else if (obj instanceof ArrayList) {
            List<String> lst = (List<String>) obj;
            int len = lst.size();
            int[] ret = new int[len];
            for (int i = 0; i < len; i++) {
                String v = lst.get(i);
                int n = -1;
                try {
                    n = Integer.parseInt(v, 10);
                } catch (NumberFormatException e) {
                }
                ret[i] = n;
            }
            return ret;
        } else { // only one value
            String v = (String) obj;
            int n = -1;
            try {
                n = Integer.parseInt(v, 10);
            } catch (NumberFormatException e) {
            }
            return new int[]{(n)};
        }
    }

}

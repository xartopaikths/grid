package io.greatbone.web;

/**
 * A web zone is a virtual folder that may be dynamically resolved.
 */
public interface WebZone {

    String key();

    WebSocketCallback doer();

}

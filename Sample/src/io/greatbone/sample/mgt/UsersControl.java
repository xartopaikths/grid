package io.greatbone.sample.mgt;

import io.greatbone.db.DbContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVirtualHost;
import io.greatbone.sample.Staffer;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The management of platform users and agents
 */
public class UsersControl extends WebControl implements Runnable {

    final ConcurrentHashMap<Integer, Staffer> cache = new ConcurrentHashMap<>();

    public UsersControl(WebVirtualHost host, WebControl parent) {
        super(host, parent);
    }

    public Staffer findGuest(int id) {
        Staffer obj = cache.get(id);
        if (obj == null) {
            // it's OK to load from database, but may not be used
            try (DbContext conn = new DbContext()) {
                obj = conn.queryobj("SELECT * FROM guests WHERE id = ?", null, null);
            } catch (SQLException e) {
            }
            Staffer prev = cache.putIfAbsent(id, obj);
            if (prev != null) {
                obj = prev;
            }
        }
        return obj;
    }

    @Override
    public void run() {
        // TODO evict expired cache items
    }

}

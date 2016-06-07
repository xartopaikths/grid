package io.greatbone.util;

import io.greatbone.db.PreparedStatementWrap;
import io.greatbone.db.ResultSetWrap;

/**
 */
public interface Cacheable {

    void load(ResultSetWrap rs);

    void save(PreparedStatementWrap ps);

}

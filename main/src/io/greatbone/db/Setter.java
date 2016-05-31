package io.greatbone.db;

import java.sql.SQLException;

/**
 */
public interface Setter {

    void set(PreparedStatementWrap wrap) throws SQLException;

}

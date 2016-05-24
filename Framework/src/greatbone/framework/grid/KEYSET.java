package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * string array
 */
public class KEYSET extends GridColumn<String[]> {

    // number of characters for each element
    int chars;

    // max number of elements
    int count;

    public KEYSET(int chars, int count) {
        this.chars = chars;
        this.count = count;
    }

    @Override
    String dbtype() {
        return "VARCHAR[" + size() + "]";
    }

    @Override
    public int size() {
        return count * chars;
    }

    String getValueAsString(GridRecord dat) {
        return null;
    }

    public String[] getValue(GridRecord dat) {
        return null;
    }

    public int tryValue(GridRecord dat, String[] v) {
        return -1;
    }

    public void putValue(GridRecord dat, String[] v) {
    }

    void putValueAsString(GridRecord dat, String v) {
    }

    @Override
    void load(GridRecord dat, ResultSet rs) throws SQLException {
        putValueAsString(dat, rs.getString(ordinal));
    }

    @Override
    void param(GridRecord dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(ordinal, getValueAsString(dat));
    }

}


package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A character string encoded in ASCII
 */
public class KEY extends GridColumn<String> {

    // maxinum number of characters
    final int length;

    public KEY(int length) {
        this.length = length;
    }

    public String getValue(GridRecord dat) {
        return dat.getAscii(offset, length);
    }

    public void putValue(GridRecord dat, String v) {
        dat.putAscii(offset, v, length);
    }

    public int tryValue(GridRecord dat, String v) {
        return -1;
    }

    @Override
    int size() {
        return length;
    }

    @Override
    String dbtype() {
        return "CHAR(" + size() + ")";
    }

    @Override
    void load(GridRecord dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getString(ordinal));
    }

    @Override
    void param(GridRecord dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(ordinal, getValue(dat));
    }

}

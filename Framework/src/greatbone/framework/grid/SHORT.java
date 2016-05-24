package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class SHORT extends GridColumn<Short> {

    public int tryValue(GridRecord dat, short v) {
        return -1;
    }

    public short getValue(GridRecord dat) {
        return dat.getShort(offset);
    }

    public void putValue(GridRecord dat, short v) {
        dat.putShort(offset, v);
    }

    @Override
    public final int size() {
        return 2;
    }

    @Override
    String dbtype() {
        return "SMALLINT";
    }

    @Override
    void load(GridRecord dat, ResultSet rs) throws SQLException {

    }

    @Override
    void param(GridRecord dat, PreparedStatement pstmt) throws SQLException {

    }

}

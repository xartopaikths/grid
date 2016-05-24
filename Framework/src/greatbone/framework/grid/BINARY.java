package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class BINARY extends GridColumn<byte[]> {

    final int length;

    public BINARY(byte kbytes) {
        this.length = kbytes * 1024;
    }

    public byte[] get(GridRecord dat) {
//        return dat.getString(offset);
        return null;
    }

    public void put(GridRecord dat, byte[] v) {
//        dat.putString(offset, v, len);
    }


    @Override
    public int size() {
        return length;
    }

    @Override
    String dbtype() {
        return "BYTEA";
    }

    @Override
    void load(GridRecord dat, ResultSet rs) throws SQLException {

    }

    @Override
    void param(GridRecord dat, PreparedStatement pstmt) throws SQLException {

    }

}

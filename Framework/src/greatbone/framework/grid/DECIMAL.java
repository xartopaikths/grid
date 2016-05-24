package greatbone.framework.grid;

import greatbone.framework.Decimal;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class DECIMAL extends GridColumn<Decimal> {

    int precision;

    public DECIMAL(int precision) {
        this.precision = precision;
    }

    public int tryValue(GridRecord dat, Decimal v) {
        return -1;
    }

    public Decimal getValue(GridRecord dat) {
        return null;
    }

    public void putValue(GridRecord dat, Decimal v) {

    }

    public void putValue(GridRecord dat, BigDecimal v) {

    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    String dbtype() {
        return "DECIMAL(8," + precision + ")";
    }

    @Override
    void load(GridRecord dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getBigDecimal(ordinal));
    }

    public int add(GridRecord data, int a) {
        return 0;
    }

    public int addAndSet(GridRecord data, int a) {
        return 0;
    }

    @Override
    void param(GridRecord dat, PreparedStatement pstmt) throws SQLException {

    }

}

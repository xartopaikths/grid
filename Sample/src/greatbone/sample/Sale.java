package greatbone.sample;

import greatbone.framework.Decimal;
import greatbone.framework.grid.*;

/**
 * A sales order or transaction.
 */
public class Sale extends GridData<Sale> {

    //
    // COLUMNS

    static final KEY ID = new KEY(18); // JXNC2016040500007A

    static final KEY SHOPID = new KEY(18); // JXNC00003E

    static final KEY GUESTID = new KEY(18); // JXNC13308060502

    static final TIMESTAMP DATE = new TIMESTAMP();

    static final STRING NAME = new STRING(12);

    static final INT OPS = new INT();

    static final DETAIL[] DETAILS = {
            new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(),
            new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(),
    };

    static final DECIMAL TOTAL = new DECIMAL(2);


    public String getId() {
        return ID.getValue(this);
    }

    public void setId(String v) {
        ID.putValue(this, v);
    }

    public String getName() {
        return NAME.getValue(this);
    }

    public void setName(String v) {
        NAME.putValue(this, v);
    }

    public short getQty(int index) {
        return DETAILS[index].QTY.getValue(this);
    }

    public void setQty(int index, short v) {
        DETAILS[index].QTY.putValue(this, v);
    }

    public Decimal getPrice(int index) {
        return DETAILS[index].PRICE.getValue(this);
    }

    public void setPrice(int index, Decimal v) {
        DETAILS[index].PRICE.putValue(this, v);
    }


    //
    // SCHEMA

    @Override
    protected GridSchema<Sale> schema() {
        return SCHEMA;
    }

    static final GridSchema<Sale> SCHEMA = new GridSchema<>(Sale.class);

}

/**
 */
class DETAIL extends STRUCT {

    final STRING NAME = new STRING(12);

    final DECIMAL PRICE = new DECIMAL(2);

    final SHORT QTY = new SHORT();

}



package greatbone.sample;

import greatbone.framework.grid.CachePolicy;
import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
@CachePolicy
public class TRANSACTS extends GridDataSet<Transact> {

    public TRANSACTS(GridUtility grid) {
        super(grid, 12);
    }

    protected void load(String arg) {
    }

    protected Class<Transact> getDataClass() {
        return Transact.class;
    }

}

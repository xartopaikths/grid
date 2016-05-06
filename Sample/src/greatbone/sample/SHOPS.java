package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Table;

/**
 * All shop participants of the business operation.
 */
@Table
public class SHOPS extends GridDataSet<Shop> {

    public SHOPS(GridUtility grid) {
        super(grid, 12);
    }

}

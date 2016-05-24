package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridRecordCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Table;

/**
 * All shop participants of the business operation.
 */
@Table
@Copy
public class SHOPS extends GridRecordCache<Shop> {

    public SHOPS(GridUtility grid) {
        super(grid, 12);
    }

}

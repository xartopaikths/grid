package greatbone.sample;

import greatbone.framework.grid.CachePolicy;
import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
@CachePolicy
public class ITEMS extends GridDataSet<Item> {

    public ITEMS(GridUtility grid) {
        super(grid, 12);
    }

}

package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridRecordCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Storage;

/**
 */
@Storage
@Copy
public class TRANSACTS extends GridRecordCache<Transact> {

    public TRANSACTS(GridUtility grid) {
        super(grid, 12);
    }

}

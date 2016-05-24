package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridRecordCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Table;

/**
 *
 */
@Table
@Copy
public class USERS extends GridRecordCache<User> {

    public USERS(GridUtility grid) {
        super(grid, 12);
    }

}

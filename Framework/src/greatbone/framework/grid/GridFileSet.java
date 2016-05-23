package greatbone.framework.grid;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * cache of underlying local file system files. off-heap memory allocation
 */
public abstract class GridFileSet extends GridSet {

    public GridFileSet(GridUtility grid, int folders) {
        super(grid);

        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("grid.fileset:type=FileSet,name=" + name);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

    }

    public String name() {
        return name;
    }

    @Override
    public void flush() {

    }


}

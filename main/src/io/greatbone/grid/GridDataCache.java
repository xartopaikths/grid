package io.greatbone.grid;

import io.greatbone.db.DbContext;
import io.greatbone.util.Roll;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A data collection of particular data class.
 * <p/>
 * pertain to a database table, act as a hash map
 * <p/>
 * consists of a number of partitions, predefined through config.xml
 * <p/>
 * setup during environment initialization
 */
public abstract class GridDataCache<D extends GridData<D>> extends GridCache<GridPage<D>> implements GridCacheMBean {

    // the record schema
    final GridSchema<D> schema;

    // annotated cache policy, can be null
    final Copy cachepol;

    @SuppressWarnings("unchecked")
    protected GridDataCache(GridUtility grid, int inipages) {
        super(grid);

        Class<D> datc = (Class<D>) typearg(0); // resolve the data class by type parameter
        this.schema = grid.getSchema(datc);
        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("grid.dataset:type=DataSet,name=" + name);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // prepare page table
        this.cachepol = getClass().getAnnotation(Copy.class);

    }

    // resolve a type argument along the inheritance hierarchy
    final Class typearg(int ordinal) {
        // gather along the inheritence hierarchy
        Deque<Class> que = new LinkedList<Class>();
        for (Class c = getClass(); c != GridDataCache.class; c = c.getSuperclass()) {
            que.addFirst(c);
        }
        for (Class c : que) {
            Type t = ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()[ordinal];
            if (t instanceof Class) {
                return (Class) t;
            } else {
                String var = ((TypeVariable) t).getName();
                Type[] cts = c.getTypeParameters();
                int p = -1;
                for (int i = 0; i < cts.length; i++) { // locate the var name in class' parameter list
                    if (((TypeVariable) cts[i]).getName().equals(var)) {
                        p = i;
                        break;
                    }
                }
                if (p == -1) {
                    throw new GridSchemaException("type variable");
                } else {
                    ordinal = p;
                }
            }
        }
        return null;
    }

    public String key() {
        return name;
    }

    @Override
    public void flush() {

    }

    @Override
    public void reload() {

    }

    public D newData() {
        return schema.instantiate();
    }

    //
    // PAGE OPERATIONS

    String select(String condition) {
        Roll<String, GridColumn> cols = schema.columns;
        StringBuilder sb = new StringBuilder("SELECT ");
        for (int i = 0; i < cols.count(); i++) {
            GridColumn col = cols.get(i);
            sb.append(col.key);
        }
        sb.append(" FROM ");
        sb.append(name);
        sb.append(" WHERE ");
        sb.append(condition);
        return sb.toString();
    }

    String update() {
        Roll<String, GridColumn> cols = schema.columns;
        StringBuilder sb = new StringBuilder("UPDATE ").append(name).append(" SET ");
        for (int i = 0; i < cols.count(); i++) {
            GridColumn col = cols.get(i);
            sb.append(col.key).append("=?");
        }
        return sb.toString();
    }

    public String CREATE() {
        return schema.getCreateTableCommand(name);
    }

    protected void load() {

        // compose sql statement
        StringBuilder sql = new StringBuilder(schema.select);
        sql.append(" FROM ").append(name);

        String likes;
        if (shardsSpec != null) {
            for (int i = 0; i < shardsSpec.size(); i++) {
                String con = shardsSpec.get(i);
                sql.append(schema.keycol.key).append(" LIKE ").append(con);
                if (i != shardsSpec.size() - 1) {
                    sql.append(" OR ");
                }
            }
            likes = sql.toString();
        } else {
            likes = null;
        }

        sql.append("WHERE ").append(likes);

        String filter;
        if (config != null && !(filter = config.getAttribute("filter")).isEmpty())
            sql.append(" AND (").append(filter).append(")");

        try (DbContext dc = new DbContext()) {
            dc.query(sql.toString(), null, wrap -> {

//                wrap.
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void loadwith(ResultSet rs) throws SQLException {
        // create a data object with one record buffer
        D dat = schema.instantiate();

        while (rs.next()) {
            // input data from result set
            dat.assign(rs);
            // put to native primary store
            put(dat);
        }
    }


    /**
     * Gets a single specified data entry.
     *
     * @param recordKey the data entry to find
     * @return a data object containing a single entry, or null
     */
    public D get(String recordKey) {
        // locate the page
        GridPage<D> shard = locateShard(recordKey);
        if (shard != null) {
            return shard.get(recordKey);
        }
        return null;
    }

    /**
     * Gets a number of data entries specified by an array of keys. The gets will execute in parallel.
     *
     * @param keys data entries to find
     * @return an merged data object, or null
     */
    public D get(String... keys) {
        List<GridGet<D>> tasks = null;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            GridPage<D> page = locateShard(key);
            if (page != null) {
                if (tasks == null) tasks = new ArrayList<>(keys.length); // lazy creation of task list
                tasks.add(new GridGet<>(page, key));
            }
        }
        if (tasks != null) {
            try {
                GridGet.invokeAll(tasks);
                // harvest the results
                D merge = null;
                for (int i = 0; i < tasks.size(); i++) {
                    D res = tasks.get(i).result;
                    if (res != null) {
                        if (merge == null) {
                            merge = res;
                        } else {
                            merge.add(res);
                        }
                    }
                }
                return merge;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public D get(Critera<D> d) {
        return null;
    }

    // no autogen of key
    public void put(D record) {
        String recordKey = record.getKey();
        GridPage<D> shard = locateShard(recordKey);
        if (shard != null) {
            shard.put(null, record);
        }
    }

    public void forEach(Critera<D> condition) {

    }

    public void clear() {

    }

    public void close() {

    }

    public boolean isClosed() {
        return false;
    }

}

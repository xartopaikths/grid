package greatbone.framework.grid;

import java.io.Serializable;

/**
 */
public interface Updater<T extends GridRecord> extends Serializable {

    void update(T cur);

}

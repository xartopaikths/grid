package greatbone.framework.grid;

import java.io.Serializable;

/**
 * A filtering condition evaluated on a data object.
 */
public interface Critera<D extends GridRecord<D>> extends Serializable {

    boolean test(D value);

}

package io.greatbone.grid;

import java.lang.annotation.*;

/**
 * For direct conversion of data (as josn part) in a distributed query
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Storage {

    String name() default "";

    /**
     * Whether or not to load from the underlying database if a cached entry missed of a read operation.
     */
    boolean load() default false;

    /**
     * The number of seconds after that an update will be saved to underlying database
     */
    int save() default -1;


}

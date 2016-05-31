package io.greatbone.grid;

import java.lang.annotation.*;

/**
 * CachePolicy specifies cache attributes for a dataset. It can read though, write through, write behind
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Copy {

    /**
     * whether to make a backup copy on the next node.
     */
    boolean value() default false;

}

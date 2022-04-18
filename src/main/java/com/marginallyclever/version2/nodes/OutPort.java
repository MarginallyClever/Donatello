package com.marginallyclever.version2.nodes;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OutPorts.class)
public @interface OutPort {
    String name() default "";
    Class<?> type() default Object.class;
    boolean optional() default false;
}

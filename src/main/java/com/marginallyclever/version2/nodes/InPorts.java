package com.marginallyclever.version2.nodes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InPorts {
    InPort[] value();
}

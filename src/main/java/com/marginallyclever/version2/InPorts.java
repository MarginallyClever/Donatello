package com.marginallyclever.version2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InPorts {
    InPort[] value();
}

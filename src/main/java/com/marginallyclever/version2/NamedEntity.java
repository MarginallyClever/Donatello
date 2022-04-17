package com.marginallyclever.version2;

/**
 * A named entity with a unique ID, good for serializing.
 */
public interface NamedEntity {

    String getName();

    String getUniqueID();
}

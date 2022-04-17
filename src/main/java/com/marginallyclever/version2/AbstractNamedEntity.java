package com.marginallyclever.version2;

import java.util.UUID;

/**
 * Implementation of {@link NamedEntity} interface.
 */
public abstract class AbstractNamedEntity implements NamedEntity {
    private final String name;
    private final String uniqueID = UUID.randomUUID().toString();

    public AbstractNamedEntity() {
        this.name = this.getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }
}

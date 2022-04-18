package com.marginallyclever.version2;

import java.io.Serializable;
import java.util.UUID;

/**
 * Implementation of {@link NamedEntity} interface.
 */
public abstract class AbstractNamedEntity implements NamedEntity, Serializable {
    private static final long serialVersionUID = -898552709829394586L;

    private String name;
    private String uniqueID;

    public AbstractNamedEntity() {
        super();
        this.name = this.getClass().getSimpleName();
        this.uniqueID = UUID.randomUUID().toString();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}

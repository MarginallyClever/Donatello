package com.marginallyclever.version2;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public abstract class Dock implements Serializable {
    @Serial
    private static final long serialVersionUID = 4623327973698656441L;

    private String name;
    private NamedEntity owner;
    private Class<?> type;

    private final Point connectionPoint = new Point();

    public Dock(String name, Class<?> type, NamedEntity owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public NamedEntity getOwner() {
        return owner;
    }

    public String getUniqueAddress() {
        return getOwner().getUniqueID()+'.'+getName();
    }

    @Override
    public String toString() {
        return "Dock: "+getName()+" ("+getType().getSimpleName()+")";
    }

    public Point getConnectionPoint() {
        return connectionPoint;
    }
}

package com.marginallyclever.version2;

public abstract class Dock {
    private final String name;
    private final Node owner;
    private final Class<?> type;

    public Dock(String name, Class<?> type, Node owner) {
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

    public Node getOwner() {
        return owner;
    }

    public String getUniqueAddress() {
        return getOwner().getUniqueID()+'.'+getName();
    }

    @Override
    public String toString() {
        return "Dock: "+getName()+" ("+getType().getSimpleName()+")";
    }
}

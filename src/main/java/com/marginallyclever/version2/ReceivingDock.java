package com.marginallyclever.version2;

public class ReceivingDock extends Dock {
    private Connection from;

    public ReceivingDock(String name, Class<?> type, AbstractNode owner) {
        super(name,type,owner);
    }

    public Connection getFrom() {
        return from;
    }

    public void setFrom(Connection from) {
        this.from = from;
    }

    public boolean isConnected() {
        return from!=null;
    }

    public Packet getPacket() {
        if(!isConnected()) return null;
        return from.getPacket();
    }

    public boolean hasPacket() {
        if(!isConnected()) return false;
        return from.hasPacket();
    }

    @Override
    public String toString() {
        return "ReceivingDock{" +
                "name='" + getName() + '\'' +
                (from==null? "" : ", from=" + from.getUniqueID()) +
                '}';
    }
}
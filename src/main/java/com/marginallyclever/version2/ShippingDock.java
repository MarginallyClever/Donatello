package com.marginallyclever.version2;

import java.util.ArrayList;
import java.util.List;

public class ShippingDock extends Dock {
    private final List<Connection> to = new ArrayList<>();

    public ShippingDock(String name, Class<?> type, Node owner) {
        super(name,type,owner);
    }

    public void addConnection(Connection c) {
        to.add(c);
    }

    public List<Connection> getConnections() {
        return to;
    }

    public void sendPacket(Packet p) {
        if(hasPacket()) throw new RuntimeException("output full!");

        for(Connection c : to) {
            c.addPacket(p);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String add = "";
        for(Connection c : to) {
            result.append(add).append(c.getUniqueID());
            add=", ";
        }
        return "ShippingDock{" +
                "name='" + getName() + '\'' +
                ", to=[" + result + ']' +
                '}';
    }

    public boolean hasPacket() {
        if(to.size()==0) return false;
        for(Connection c : to) {
            if(c.hasPacket()) return true;
        }
        return false;
    }
}
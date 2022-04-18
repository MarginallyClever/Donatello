package com.marginallyclever.version2;

import java.beans.Transient;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

public class Connection implements Serializable {
    private static final long serialVersionUID = 8466086057130123820L;

    private final String uniqueID = UUID.randomUUID().toString();

    private transient Deque<Packet<?>> queue = new LinkedList<>();

    private ShippingDock from;

    private ReceivingDock to;

    public Connection(ShippingDock source,ReceivingDock destination) {
        this.from = source;
        this.to = destination;

        if(!destination.getType().isAssignableFrom(source.getType())) {
            throw new InvalidParameterException("source cannot be assigned to destination.");
        }

        source.addConnection(this);
        destination.setFrom(this);
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public ShippingDock getFrom() {
        return from;
    }

    public ReceivingDock getTo() {
        return to;
    }

    public void setFrom(ShippingDock from) {
        this.from = from;
    }

    public void setTo(ReceivingDock to) {
        this.to = to;
    }

    public void addPacket(Packet<?> packet) {
        queue.addLast(packet);
    }

    public Packet<?> getPacket() {
        return queue.pollFirst();
    }

    public boolean hasPacket() {
        return !queue.isEmpty();
    }

    public void clear() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }

    public boolean isValid() {
        return (from!=null) && (to!=null);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "from='" + from.getUniqueAddress() + '\'' +
                ", to='" + to.getUniqueAddress() + '\'' +
                '}';
    }
}

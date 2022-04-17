package com.marginallyclever.version2;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

public class Connection {
    private final String uniqueID = UUID.randomUUID().toString();

    private Deque<Packet<?>> queue = new LinkedList<>();
    private ShippingDock from;
    private ReceivingDock to;

    public Connection(ShippingDock from,ReceivingDock to) {
        this.from = from;
        this.to = to;

        from.addConnection(this);
        to.setFrom(this);
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

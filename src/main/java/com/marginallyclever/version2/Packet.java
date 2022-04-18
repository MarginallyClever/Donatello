package com.marginallyclever.version2;

/**
 * A packet is an agnostic container for data.
 * @param <T> the type of data in the packet.
 */
public class Packet<T> {
    public T data;

    public Packet(T data) {
        this.data = data;
    }
}

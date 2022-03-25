package com.marginallyclever.donatello;

/**
 * Classes which implement {@link UpdateClockListener} can subscribe to an {@link UpdateClock} to receive
 * {@link #updateClockEvent()} at the scheduled interval.
 * @author Dan Royer
 * @since 2022-03-19
 */
public interface UpdateClockListener {
    void updateClockEvent();
}

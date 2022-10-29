package com.marginallyclever.donatello;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * UpdateClock notifies all listeners at the scheduled interval.
 * @author Dan Royer
 * @since 2022-03-19
 */
public class UpdateClock extends TimerTask {
    private final Timer myTimer = new Timer("myTimer");
    private final List<UpdateClockListener> listeners = new ArrayList<>();
    private final int frequency;

    private final ReentrantLock myLock = new ReentrantLock();

    public UpdateClock(int hz) {
        super();
        this.frequency = hz;
        myTimer.schedule(this,0,frequency);
    }

    public void addListener(UpdateClockListener ear) {
        listeners.add(ear);
    }

    public void removeListener(UpdateClockListener ear) {
        listeners.remove(ear);
    }

    @Override
    public void run() {
        lock();
        for( UpdateClockListener ear : listeners ) {
            ear.updateClockEvent();
        }
        unlock();
    }

    public void stop() {
        myTimer.cancel();
    }

    public void lock() {
        myLock.lock();
    }

    public void unlock() {
        myLock.unlock();
    }
}

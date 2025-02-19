package com.marginallyclever.donatello.select;

/**
 * A SelectEvent is fired when a Select changes value.
 * @author Dan Royer
 * @since 7.50.2
 */
public class SelectEvent {
    private final Select source;
    private final Object oldValue;
    private final Object newValue;

    public SelectEvent(Select source,Object oldValue,Object newValue) {
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Select getSource() {
        return source;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}

package com.marginallyclever.donatello;

/**
 * This is container for a String.  EditNodePanel looks for Filename and then knows to use a file chooser.
 */
public class Filename {
    private String value;

    public Filename(String value) {
        super();
        set(value);
    }

    public Filename() {}

    public String get() {
        return value;
    }

    public void set(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.marginallyclever.donatello.graphview;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphViewSettings {
    public static int DRAW_BACKGROUND = 0;
    public static int DRAW_CURSOR = 1;
    public static int DRAW_ORIGIN = 2;

    private final Map<Integer,Boolean> state = new HashMap<>();

    public GraphViewSettings() {
        super();
    }

    public void reset() {
        set(DRAW_BACKGROUND,false);
        set(DRAW_CURSOR,false);
        set(DRAW_ORIGIN,false);
    }

    public boolean get(int key) {
        return state.getOrDefault(key,false);
    }

    public void set(int key,boolean value) {
        state.put(key,value);
    }

    public void save() {
        throw new RuntimeException("Not implemented");
    }

    public void load() throws IOException {
        throw new RuntimeException("Not implemented");
    }
}

package com.marginallyclever.donatello.graphview;

import com.marginallyclever.nodegraphcore.Dock;
import com.marginallyclever.nodegraphcore.Node;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphViewSettings {
    /**
     * The default {@link Node} background color.
     */
    public static final Color NODE_COLOR_BACKGROUND = Color.WHITE;
    /**
     * The default {@link Node} border color.
     */
    public static final Color NODE_COLOR_BORDER = Color.BLACK;
    /**
     * The default {@link Node} internal border between {@link Dock}s.
     */
    public static final Color NODE_COLOR_INTERNAL_BORDER = Color.DARK_GRAY;
    /**
     * The default {@link JPanel} background color.
     */
    public static final Color PANEL_COLOR_BACKGROUND = Color.LIGHT_GRAY;
    /**
     * The default grid color.
     */
    public static final Color PANEL_GRID_COLOR = Color.GRAY;
    /**
     * size of the grid squares, in pixels.
     */
    public static int GRID_SIZE=20;
    /**
     * The default {@link Node} font color.
     */
    public static final Color NODE_COLOR_FONT_CLEAN = Color.BLACK;
    /**
     * The default {@link Node} font color for variables when <pre>getIsDirty()</pre>. is true.
     */
    public static final Color NODE_COLOR_FONT_DIRTY = Color.RED;
    /**
     * The default {@link Node} tile bar font color
     */
    public static final Color NODE_COLOR_TITLE_FONT = Color.WHITE;
    /**
     * The default {@link Node} tile bar background color
     */
    public static final Color NODE_COLOR_TITLE_BACKGROUND = Color.BLACK;
    /**
     * The default {@link Node} female connection point color.
     */
    public static final Color CONNECTION_POINT_COLOR = Color.LIGHT_GRAY;
    /**
     * The default {@link Node} male connection point color.
     */
    public static final Color CONNECTION_COLOR = Color.BLUE;

    /**
     * The default {@link Node} outer border radius.
     */
    public static final int CORNER_RADIUS = 5;

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

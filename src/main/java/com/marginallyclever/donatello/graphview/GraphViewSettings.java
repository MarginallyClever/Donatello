package com.marginallyclever.donatello.graphview;

import com.marginallyclever.nodegraphcore.Dock;
import com.marginallyclever.nodegraphcore.Node;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the settings for the {@link GraphViewPanel} class.
 */
public class GraphViewSettings {
    /**
     * The default {@link Node} background color.
     */
    private Color nodeColorBackground = Color.WHITE;
    /**
     * The default {@link Node} border color.
     */
    private Color nodeColorBorder = Color.BLACK;
    /**
     * The default {@link Node} internal border between {@link Dock}s.
     */
    private Color nodeColorInternalBorder = Color.DARK_GRAY;
    /**
     * The default {@link JPanel} background color.
     */
    private Color panelColorBackground = Color.LIGHT_GRAY;
    /**
     * The default grid color.
     */
    private Color panelGridColor = Color.GRAY;
    /**
     * size of the grid squares, in pixels.
     */
    private int gridSize =20;
    /**
     * The default {@link Node} font color.
     */
    private Color nodeColorFontClean = Color.BLACK;
    /**
     * The default {@link Node} font color for variables when <pre>getIsDirty()</pre>. is true.
     */
    private Color nodeColorFontDirty = Color.RED;
    /**
     * The default {@link Node} tile bar font color
     */
    private Color nodeColorTitleFont = Color.WHITE;
    /**
     * The default {@link Node} tile bar background color
     */
    private Color nodeColorTitleBackground = Color.BLACK;
    /**
     * The default {@link Node} female connection point color.
     */
    private Color connectionPointColor = Color.LIGHT_GRAY;
    /**
     * The default {@link Node} male connection point color.
     */
    private Color connectionColor = Color.BLUE;

    /**
     * The default {@link Node} outer border radius.
     */
    private int cornerRadius = 5;

    private static int drawBackgroundGrid = 0;
    private static int DRAW_CURSOR = 1;
    private static int DRAW_ORIGIN = 2;

    private final Map<Integer,Boolean> state = new HashMap<>();

    public GraphViewSettings() {
        super();
    }

    public void reset() {
        set(drawBackgroundGrid,false);
        set(DRAW_CURSOR,false);
        set(DRAW_ORIGIN,false);
    }

    public boolean get(int key) {
        return state.getOrDefault(key,false);
    }

    public void set(int key,boolean value) {
        state.put(key,value);
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("nodeColorBackground",nodeColorBackground.getRGB());
        json.put("nodeColorBorder",nodeColorBorder.getRGB());
        json.put("nodeColorInternalBorder",nodeColorInternalBorder.getRGB());
        json.put("panelColorBackground",panelColorBackground.getRGB());
        json.put("panelGridColor",panelGridColor.getRGB());
        json.put("gridSize",gridSize);
        json.put("nodeColorFontClean",nodeColorFontClean.getRGB());
        json.put("nodeColorFontDirty",nodeColorFontDirty.getRGB());
        json.put("nodeColorTitleFont",nodeColorTitleFont.getRGB());
        json.put("nodeColorTitleBackground",nodeColorTitleBackground.getRGB());
        json.put("connectionPointColor",connectionPointColor.getRGB());
        json.put("connectionColor",connectionColor.getRGB());
        json.put("cornerRadius",cornerRadius);

        json.put("drawBackground",get(drawBackgroundGrid));
        json.put("drawCursor",get(DRAW_CURSOR));
        json.put("drawOrigin",get(DRAW_ORIGIN));

        return json;
    }

    public void fromJSON(JSONObject json) throws IOException {
        nodeColorBackground = new Color(json.optInt("nodeColorBackground",nodeColorBackground.getRGB()));
        nodeColorBorder = new Color(json.optInt("nodeColorBorder",nodeColorBorder.getRGB()));
        nodeColorInternalBorder = new Color(json.optInt("nodeColorInternalBorder",nodeColorInternalBorder.getRGB()));
        panelColorBackground = new Color(json.optInt("panelColorBackground",panelColorBackground.getRGB()));
        panelGridColor = new Color(json.optInt("panelGridColor",panelGridColor.getRGB()));
        gridSize = json.optInt("gridSize",gridSize);
        nodeColorFontClean = new Color(json.optInt("nodeColorFontClean",nodeColorFontClean.getRGB()));
        nodeColorFontDirty = new Color(json.optInt("nodeColorFontDirty",nodeColorFontDirty.getRGB()));
        nodeColorTitleFont = new Color(json.optInt("nodeColorTitleFont",nodeColorTitleFont.getRGB()));
        nodeColorTitleBackground = new Color(json.optInt("nodeColorTitleBackground",nodeColorTitleBackground.getRGB()));
        connectionPointColor = new Color(json.optInt("connectionPointColor",connectionPointColor.getRGB()));
        connectionColor = new Color(json.optInt("connectionColor",connectionColor.getRGB()));
        cornerRadius = json.optInt("cornerRadius",cornerRadius);

        setDrawBackground(json.optBoolean("drawBackground", getDrawBackgroundGrid()));
        setDrawCursor(json.optBoolean("drawCursor",getDrawCursor()));
        setDrawOrigin(json.optBoolean("drawOrigin",getDrawOrigin()));
    }


    public Color getNodeColorBackground() {
        return nodeColorBackground;
    }

    public void setNodeColorBackground(Color color) {
        this.nodeColorBackground = color;
    }

    public Color getNodeColorBorder() {
        return nodeColorBorder;
    }

    public void setNodeColorBorder(Color color) {
        this.nodeColorBorder = color;
    }

    public Color getNodeColorInternalBorder() {
        return nodeColorInternalBorder;
    }

    public void setNodeColorInternalBorder(Color color) {
        this.nodeColorInternalBorder = color;
    }

    public Color getPanelColorBackground() {
        return panelColorBackground;
    }

    public void setPanelColorBackground(Color color) {
        this.panelColorBackground = color;
    }

    public Color getPanelGridColor() {
        return panelGridColor;
    }

    public void setPanelGridColor(Color color) {
        this.panelGridColor = color;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int GRID_SIZE) {
        this.gridSize = GRID_SIZE;
    }

    public Color getNodeColorFontClean() {
        return nodeColorFontClean;
    }

    public void setNodeColorFontClean(Color color) {
        this.nodeColorFontClean = color;
    }

    public Color getNodeColorFontDirty() {
        return nodeColorFontDirty;
    }

    public void setNodeColorFontDirty(Color color) {
        this.nodeColorFontDirty = color;
    }

    public Color getNodeColorTitleFont() {
        return nodeColorTitleFont;
    }

    public void setNodeColorTitleFont(Color color) {
        this.nodeColorTitleFont = color;
    }

    public Color getNodeColorTitleBackground() {
        return nodeColorTitleBackground;
    }

    public void setNodeColorTitleBackground(Color color) {
        this.nodeColorTitleBackground = color;
    }

    public Color getConnectionPointColor() {
        return connectionPointColor;
    }

    public void setConnectionPointColor(Color color) {
        this.connectionPointColor = color;
    }

    public Color getConnectionColor() {
        return connectionColor;
    }

    public void setConnectionColor(Color color) {
        this.connectionColor = color;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int radius) {
        if( radius < 0 ) throw new IllegalArgumentException("radius must be >=0");
        this.cornerRadius = radius;
    }

    public boolean getDrawBackgroundGrid() {
        return get(drawBackgroundGrid);
    }

    public void setDrawBackground(boolean drawBackgroundGrid) {
        set(GraphViewSettings.drawBackgroundGrid, drawBackgroundGrid);
    }

    public boolean getDrawCursor() {
        return get(DRAW_CURSOR);
    }

    public void setDrawCursor(boolean drawCursor) {
        set(GraphViewSettings.DRAW_CURSOR, drawCursor);
    }

    public boolean getDrawOrigin() {
        return get(DRAW_ORIGIN);
    }

    public void setDrawOrigin(boolean drawOrigin) {
        set(GraphViewSettings.DRAW_ORIGIN, drawOrigin);
    }
}

package com.marginallyclever.donatello;

import com.marginallyclever.donatello.graphview.GraphViewSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.io.IOException;

public class GraphViewSettingsTest {
    @Test
    public void testToFromJSON() throws IOException {
        GraphViewSettings a = new GraphViewSettings();
        a.setNodeColorBackground(Color.CYAN);
        a.setNodeColorBorder(Color.MAGENTA);
        a.setNodeColorInternalBorder(Color.DARK_GRAY);
        a.setPanelColorBackground(Color.LIGHT_GRAY);
        a.setPanelGridColor(Color.GRAY);
        a.setGridSize(10);
        a.setNodeColorFontClean(Color.BLACK);
        a.setNodeColorFontDirty(Color.RED);
        a.setNodeColorTitleFont(Color.WHITE);
        a.setNodeColorTitleBackground(Color.BLACK);
        a.setConnectionPointColor(Color.LIGHT_GRAY);
        a.setConnectionColor(Color.BLUE);
        a.setCornerRadius(5);
        a.setDrawBackground(true);
        a.setDrawCursor(true);
        a.setDrawOrigin(true);

        GraphViewSettings b = new GraphViewSettings();
        b.fromJSON(a.toJSON());

        Assertions.assertEquals(a.getNodeColorBackground(), b.getNodeColorBackground());
        Assertions.assertEquals(a.getNodeColorBorder(), b.getNodeColorBorder());
        Assertions.assertEquals(a.getNodeColorInternalBorder(), b.getNodeColorInternalBorder());
        Assertions.assertEquals(a.getPanelColorBackground(), b.getPanelColorBackground());
        Assertions.assertEquals(a.getPanelGridColor(), b.getPanelGridColor());
        Assertions.assertEquals(a.getGridSize(), b.getGridSize());
        Assertions.assertEquals(a.getNodeColorFontClean(), b.getNodeColorFontClean());
        Assertions.assertEquals(a.getNodeColorFontDirty(), b.getNodeColorFontDirty());
        Assertions.assertEquals(a.getNodeColorTitleFont(), b.getNodeColorTitleFont());
        Assertions.assertEquals(a.getNodeColorTitleBackground(), b.getNodeColorTitleBackground());
        Assertions.assertEquals(a.getConnectionPointColor(), b.getConnectionPointColor());
        Assertions.assertEquals(a.getConnectionColor(), b.getConnectionColor());
        Assertions.assertEquals(a.getCornerRadius(), b.getCornerRadius());
        Assertions.assertEquals(a.getDrawBackgroundGrid(), b.getDrawBackgroundGrid());
        Assertions.assertEquals(a.getDrawCursor(), b.getDrawCursor());
        Assertions.assertEquals(a.getDrawOrigin(), b.getDrawOrigin());
    }
}
package com.marginallyclever.donatello.graphview;

import com.marginallyclever.donatello.PanelHelper;

import javax.swing.*;
import java.awt.*;

/**
 * provides an edit interface for the {@link GraphViewSettings} class.
 */
public class GraphViewSettingsPanel extends JPanel {
    public GraphViewSettingsPanel() {
        this(new GraphViewSettings());
    }

    public GraphViewSettingsPanel(GraphViewSettings settings) {
        super(new GridBagLayout());

        GridBagConstraints gbc = PanelHelper.createGBC();

        PanelHelper.addColorChooser(this,"Panel background",settings::getPanelColorBackground,settings::setPanelColorBackground,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Panel grid",settings::getPanelGridColor,settings::setPanelGridColor,gbc);
        gbc.gridy++;
        var gs = PanelHelper.addNumberFieldInt("Grid size",settings.getGridSize());
        gs.addPropertyChangeListener("value",(e)->settings.setGridSize((int)gs.getValue()));
        PanelHelper.addLabelAndComponent(this,"Grid size",gs,gbc);
        gbc.gridy++;

        PanelHelper.addColorChooser(this,"Node background",settings::getNodeColorBackground,settings::setNodeColorBackground,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node border",settings::getNodeColorBorder,settings::setNodeColorBorder,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node internal border",settings::getNodeColorInternalBorder,settings::setNodeColorInternalBorder,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node font clean",settings::getNodeColorFontClean,settings::setNodeColorFontClean,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node font dirty",settings::getNodeColorFontDirty,settings::setNodeColorFontDirty,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node title font",settings::getNodeColorTitleFont,settings::setNodeColorTitleFont,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Node title background",settings::getNodeColorTitleBackground,settings::setNodeColorTitleBackground,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Connection point",settings::getConnectionPointColor,settings::setConnectionPointColor,gbc);
        gbc.gridy++;
        PanelHelper.addColorChooser(this,"Connection color",settings::getConnectionColor,settings::setConnectionColor,gbc);
        gbc.gridy++;

        var cr = PanelHelper.addNumberFieldInt("Corner radius",settings.getCornerRadius());
        gs.addPropertyChangeListener("value",(e)->settings.setCornerRadius((int)cr.getValue()));
        PanelHelper.addLabelAndComponent(this,"Grid size",gs,gbc);
        gbc.gridy++;



    }

}
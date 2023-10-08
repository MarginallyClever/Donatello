package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.Donatello;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Uses the {@link Donatello#printAll(Graphics)} to generate a {@link BufferedImage} and then saves that to
 * a default path.
 * TODO add a file selection dialog?
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphPrintAction extends AbstractAction {
    /**
     * The editor being affected.
     */
    private final Donatello editor;

    /**
     * The default save file.
     */
    public static final String SAVE_PATH = "saved.png";


    /**
     * Constructor for subclasses to call.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphPrintAction(String name, Donatello editor) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage awtImage = new BufferedImage(editor.getWidth(), editor.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = awtImage.getGraphics();
        editor.printAll(g);
/*
        if(popupBar.isVisible()) {
            g.translate(popupPoint.x, popupPoint.y);
            popupBar.printAll(g);
            g.translate(-popupPoint.x, -popupPoint.y);
        }
 */
        // TODO file selection dialog here
        File outputFile = new File(SAVE_PATH);
        String extension = SAVE_PATH.substring(SAVE_PATH.lastIndexOf(".")+1);

        try {
            ImageIO.write(awtImage, extension, outputFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

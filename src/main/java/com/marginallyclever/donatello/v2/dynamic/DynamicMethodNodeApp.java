package com.marginallyclever.donatello.v2.dynamic;

import javax.swing.*;
import java.awt.*;

/**
 * The main application class.
 */
public class DynamicMethodNodeApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame
            JFrame frame = new JFrame("Java Reflection Query");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add the SearchPanel to the frame
            frame.getContentPane().add(new ClassSearchPanel());

            // Display the frame
            frame.setMinimumSize(new Dimension(400, 300));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
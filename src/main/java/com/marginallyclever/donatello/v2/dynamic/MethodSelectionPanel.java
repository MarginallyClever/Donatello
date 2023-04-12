package com.marginallyclever.donatello.v2.dynamic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 * A panel that allows the user to select a method from a class.
 */
public class MethodSelectionPanel extends JPanel {
    private final JComboBox<String> methodComboBox;
    private final String className;

    private DynamicMethodNodePanel dynamicMethodNodePanel;

    public MethodSelectionPanel(String className) {
        setLayout(new BorderLayout());
        this.className = className;

        // Get the list of method names from the class (customize as needed)
        String[] methodNames = getMethodNames(className);

        methodComboBox = new JComboBox<>(methodNames);
        methodComboBox.addActionListener(new MethodComboBoxListener());

        setBorder(BorderFactory.createTitledBorder("Method"));
        add(methodComboBox, BorderLayout.NORTH);
        updateSelection();
    }

    private String[] getMethodNames(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods(); // Get all public methods, including inherited ones

            String[] methodNames = new String[methods.length];
            for (int i = 0; i < methods.length; i++) {
                methodNames[i] = methods[i].getName();
            }
            return methodNames;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private class MethodComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateSelection();
        }
    }

    private void updateSelection() {
        String selectedMethod = (String) methodComboBox.getSelectedItem();
        if (selectedMethod != null) {
            try {
                System.out.println("Found method: " + selectedMethod);
                // Replace the center part of the panel with DynamicMethodNodePanel
                DynamicMethodNode node = new DynamicMethodNode(className, selectedMethod);

                if(dynamicMethodNodePanel!=null) {
                    remove(dynamicMethodNodePanel);
                }
                dynamicMethodNodePanel = new DynamicMethodNodePanel(node);
                add(dynamicMethodNodePanel, BorderLayout.CENTER);

                revalidate();
                repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}


package com.marginallyclever.donatello.v2.dynamic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel that can be used to invoke a method dynamically at runtime. The method can be static or non-static.
 */
public class DynamicMethodNodePanel extends JPanel {
    private final DynamicMethodNode node;
    private JLabel outputLabel;
    private JLabel exceptionMessageLabel;
    private JButton dismissButton;
    private JPanel exceptionPanel;

    public DynamicMethodNodePanel(DynamicMethodNode node) {
        this.node = node;
        setLayout(new BorderLayout());

        // Add input and output panels
        createInputPanel();
        createOutputPanel();
        add(createExceptionNotificationPanel(), BorderLayout.SOUTH);
    }

    private JPanel createExceptionNotificationPanel() {
        // Initialize exception message label and dismiss button
        exceptionMessageLabel = new JLabel();
        exceptionMessageLabel.setForeground(Color.RED);
        dismissButton = new JButton("Dismiss");
        dismissButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideExceptionMessage();
            }
        });

        // Add the exception message label and dismiss button to the panel
        exceptionPanel = new JPanel(new BorderLayout());
        exceptionPanel.setBorder(BorderFactory.createTitledBorder("Exception"));
        exceptionPanel.add(exceptionMessageLabel, BorderLayout.CENTER);
        exceptionPanel.add(dismissButton, BorderLayout.SOUTH);

        // Initially hide the exception message and dismiss button
        hideExceptionMessage();

        return exceptionPanel;
    }

    private void createInputPanel() {
        JPanel inputPanelWrapper = new JPanel();
        inputPanelWrapper.setLayout(new BoxLayout(inputPanelWrapper, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createTitledBorder("Inputs"));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        Class<?>[] parameterTypes = node.getMethod().getParameterTypes();
        JTextField[] inputFields = new JTextField[parameterTypes.length];

        if(parameterTypes.length == 0) return;

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            JLabel label = new JLabel("Input " + (i + 1) + " (" + parameterType.getSimpleName() + "):");
            JTextField textField = new JTextField(20);
            inputFields[i] = textField;

            JPanel fieldPanel = new JPanel(new BorderLayout());
            fieldPanel.add(label, BorderLayout.WEST);
            fieldPanel.add(textField, BorderLayout.CENTER);

            inputPanel.add(fieldPanel);
        }

        JButton processButton = new JButton("Run");

        processButton.addActionListener(e -> {
            try {
                Object[] inputs = new Object[inputFields.length];
                for (int i = 0; i < inputFields.length; i++) {
                    // Assuming all inputs are of type String for simplicity; customize for other types as needed
                    inputs[i] = convertStringToType(parameterTypes[i],inputFields[i].getText());
                }

                node.setInput(inputs);
                node.process();
            } catch (Exception ex) {
                onException(ex);
            }

            // Update output panel with the results
            outputLabel.setText(node.getOutput().toString());
        });

        inputPanelWrapper.add(inputPanel);
        inputPanelWrapper.add(processButton);

        add(inputPanelWrapper, BorderLayout.NORTH);
    }

    private void createOutputPanel() {
        if(node.getMethod().getReturnType() == void.class) return;

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output"));
        outputLabel = new JLabel("N/A");
        outputPanel.add(outputLabel, BorderLayout.CENTER);

        add(outputPanel, BorderLayout.CENTER);
    }

    public static Object convertStringToType(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Short.class || type == short.class) {
            return Short.parseShort(value);
        } else if (type == Byte.class || type == byte.class) {
            return Byte.parseByte(value);
        } else if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, value);
        } else {
            throw new UnsupportedOperationException("Conversion not supported for type: " + type);
        }
    }

    private void showExceptionMessage(String message) {
        exceptionMessageLabel.setText(message);
        exceptionMessageLabel.setVisible(true);
        dismissButton.setVisible(true);
        exceptionPanel.setVisible(true);
    }

    private void hideExceptionMessage() {
        exceptionPanel.setVisible(false);
    }

    // Call this method when an exception occurs during the process
    public void onException(Exception e) {
        showExceptionMessage(e.getMessage());
    }
}
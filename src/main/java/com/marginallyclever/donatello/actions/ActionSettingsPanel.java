package com.marginallyclever.donatello.actions;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A panel that displays the settings for each Action in the application.
 */
public class ActionSettingsPanel extends JPanel {
    private final ArrayList<Action> list = ActionRegistry.getAll();

    public ActionSettingsPanel() {
        super();
        setLayout(new GridBagLayout());

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;

        for( var action : list ) {
            addLineItem(action,c);
            c.gridy++;
        }

        checkForCollisions(list);
    }

    private void addLineItem(Action action,GridBagConstraints c) {
        var name = (String)action.getValue(Action.NAME);

        var icon = (Icon)action.getValue(Action.SMALL_ICON);
        // if icon is null, use a blank icon
        if (icon == null) {
            icon = new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
        }

        // get the accelerator key for the action.
        KeyStroke ks = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        String readable = ks != null ? toHumanReadable(ks) : "";
        var field = new JTextField(readable);

        KeyStroke defaultKs = ActionRegistry.getDefaultAccelerator(action);
        JButton resetButton = new JButton("");
        // use icons8-reset-16.png as the icon for the reset button.
        resetButton.setIcon( new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-reset-16.png"))));
        updateResetButton(resetButton, defaultKs, ks);
        resetButton.addActionListener(e->{
            // reset the action's accelerator key to the default
            action.putValue(Action.ACCELERATOR_KEY, defaultKs);
            field.setText(toHumanReadable(defaultKs));
            field.setForeground(Color.BLACK); // reset the color to default
            resetButton.setEnabled(false);
            checkForCollisions(list);
        });

        // put it together
        c.gridx=0;
        c.weightx=0.5;
        this.add(new JLabel(name, icon, SwingConstants.LEFT),c);
        c.gridx++;
        this.add(field,c);
        c.weightx=0.0;
        c.gridx++;
        this.add(resetButton,c);

        // in a perfect world, catch any key combination typed in the field that is a valid accelerator and
        // record that as the new accelerator key for the action.
        // in reality, let the user type in the field and then validate it.
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate();
            }

            void validate() {
                String text = field.getText();
                if (text == null || text.trim().isEmpty()) {
                    action.putValue(Action.ACCELERATOR_KEY, null);
                    checkForCollisions(list);
                    return;
                }
                KeyStroke newKeyStroke = KeyStroke.getKeyStroke(text);
                // invalid key stroke
                if(newKeyStroke == null) {
                    field.setForeground(Color.RED);
                    return;
                }
                // valid key stroke.
                action.putValue(Action.ACCELERATOR_KEY, newKeyStroke);
                updateResetButton(resetButton, defaultKs, newKeyStroke);
                checkForCollisions(list);
            }
        });
    }

    private void updateResetButton(JButton resetButton, KeyStroke defaultKs, KeyStroke ks) {
        resetButton.setEnabled( defaultKs != null && !defaultKs.equals(ks) );
    }

    private void checkForCollisions(ArrayList<Action> list) {
        // get the default jtextfield background color.
        Color defaultColor = Color.BLACK; // TODO get the default color from the UIManager
        Color problemColor = Color.ORANGE;  // TODO get the problem color from the UIManager

        // reset all fields to default color
        for (Component comp : getComponents()) {
            if (comp instanceof JTextField f2) {
                f2.setForeground(defaultColor);
            }
        }

        // compare all actions to each other
        for( int i=0; i<list.size(); ++i ) {
            var action1 = list.get(i);
            var ks1 = (KeyStroke) action1.getValue(Action.ACCELERATOR_KEY);
            String text1 = ks1 != null ? toHumanReadable(ks1) : "";
            if(text1==null || text1.trim().isEmpty()) continue;

            for( int j=i+1; j<list.size(); ++j ) {
                var action2 = list.get(j);
                var ks2 = (KeyStroke) action2.getValue(Action.ACCELERATOR_KEY);
                String text2 = ks2 != null ? toHumanReadable(ks2) : "";
                if(text2==null || text2.trim().isEmpty()) continue;

                // if two actions have the same accelerator key, mark them as a collision.
                if( text1.equals(text2) ) {
                    // set the fields to red
                    for (Component comp : getComponents()) {
                        if (comp instanceof JTextField f2 && f2.getText().equals(text1)) {
                            f2.setForeground(problemColor);
                        }
                    }
                }
            }
        }
    }

    /**
     * Convert a {@link KeyStroke} to a human readable string that matches the format used in
     * {@link KeyStroke#getKeyStroke(String)}
     * @param ks the KeyStroke to convert
     * @return a human readable string representation of the {@link KeyStroke}
     */
    public String toHumanReadable(KeyStroke ks) {
        if (ks == null) return "";
        StringBuilder sb = new StringBuilder();
        int modifiers = ks.getModifiers();
        if (modifiers != 0) {
            sb.append(InputEvent.getModifiersExText(modifiers).toLowerCase().replace("+"," ")).append(" ");
        }
        sb.append(KeyEvent.getKeyText(ks.getKeyCode()));
        return sb.toString();
    }
}

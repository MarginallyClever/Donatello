package com.marginallyclever.donatello.actions;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A panel that displays the settings for each Action in the application.
 */
public class ActionSettingsPanel extends JPanel {
    public ActionSettingsPanel() {
        super();
        setLayout(new GridLayout(0,2));

        var list = ActionRegistry.getAll();
        for( var action : list ) {
            var name = (String)action.getValue(Action.NAME);
            var icon = (Icon)action.getValue(Action.SMALL_ICON);
            // if icon is null, use a blank icon
            if (icon == null) {
                icon = new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
            }

            KeyStroke ks = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
            String readable = ks != null ? toHumanReadable(ks) : "";
            this.add(new JLabel(name, icon, SwingConstants.LEFT));
            var field = new JTextField(readable);
            this.add(field);

            // catch any key combination entered in the field that is a valid accelerator
            // record that as the new accelerator key for the action.
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
                    checkForCollisions(list);
                }
            });

            checkForCollisions(list);
        }
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
                // check if the text matches the other action's accelerator key
                if( text1.equals(text2) ) {
                    // match!
                    System.out.println("Collision found: " + text1 + " and " + text2);
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

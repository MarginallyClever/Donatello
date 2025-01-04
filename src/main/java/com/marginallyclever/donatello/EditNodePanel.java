package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.ColorAtPoint;
import com.marginallyclever.nodegraphcore.dock.Input;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.dock.Dock;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Swing UI allowing a user to edit an existing {@link Node}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class EditNodePanel extends JPanel {
    /**
     * The {@link Node} being edited.
     */
    private final Node node;
    /**
     * The edit field for the label (nickname) of the {@link Node}.
     */
    private final JTextField labelField = new JTextField();
    /**
     * The fields being edited.
     */
    private final ArrayList<JComponent> fields = new ArrayList<>();

    /**
     * The Constructor for subclasses to call.
     * @param node the {@link Node} to edit.
     */
    public EditNodePanel(Node node) {
        super();
        this.node=node;
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weightx=1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(1,1,1,1);

        addReadOnlyField(c,"Type",node.getName());
        c.gridy++;
        addReadOnlyField(c,"ID",node.getUniqueID());
        c.gridy++;
        addLabelField(c);
        c.gridy++;

        for(int i=0;i<node.getNumVariables();++i) {
            addVariableField(node.getVariable(i),c);
            c.gridy++;
        }
    }

    private void addVariableField(Dock<?> dock,GridBagConstraints c) {
        if(dock instanceof Input<?> input) {
            if (Number.class.isAssignableFrom(dock.getType())) {
                addTextField(input, c);
            } else if (dock.getType().equals(String.class)) {
                addTextField(input, c);
            } else if (dock.getType().equals(Boolean.class)) {
                addBooleanField(input, c);
            } else {
                addReadOnlyField(c, dock.getName(), dock.getTypeName());
            }
        } else {
            addReadOnlyField(c, dock.getName(), dock.getTypeName());
        }
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link Dock} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addTextField(Input<?> variable, GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(variable.getName()),c);

        Object v = variable.getValue();
        String output = v==null? "" : v.toString();
        JTextField textField = new JTextField(output);
        fields.add(textField);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        this.add(textField,c);
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link Dock} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addBooleanField(Input<?> variable, GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(variable.getName()),c);

        boolean v = (Boolean)variable.getValue();
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(v);
        fields.add(checkBox);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        this.add(checkBox,c);
    }

    /**
     * Adds the node 'label' field to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addLabelField(GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel("Label"),c);

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        labelField.setText(node.getLabel());
        this.add(labelField,c);
    }

    /**
     * Adds one read-only label/value pair to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     * @param name the label
     * @param value the value
     */
    private void addReadOnlyField(GridBagConstraints c,String name,String value) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(name),c);

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        JLabel v = new JLabel(value);
        v.setEnabled(false);
        this.add(v,c);
    }

    /**
     * Displays an edit dialog for a given node.  Returns a copy of the node with any alterations, if any.
     * @param subject the node to edit.
     * @param frame the parent frame.
     */
    public static void runAsDialog(Node subject,Frame frame) {
        EditNodePanel panel = new EditNodePanel(subject);
        if(JOptionPane.showConfirmDialog(frame,panel,"Edit "+subject.getName(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            subject.setLabel(panel.getLabel());
            readAllFields(subject,panel);
        }
    }

    private static void readAllFields(Node subject, EditNodePanel panel) {
        int j=0;
        for(int i=0;i<subject.getNumVariables();++i) {
            Dock<?> variable = subject.getVariable(i);
            if(variable instanceof Input) {
                Input r = (Input)variable;
                if (variable.getType().equals(Number.class)) {
                    panel.readTextField(j++, r);
                } else if (variable.getType().equals(String.class)) {
                    panel.readTextField(j++, r);
                }  else if (variable.getType().equals(Boolean.class)) {
                    panel.readBooleanField(j++, r);
                } else {
                    // TODO ???
                }
            }
        }
    }

    private void readBooleanField(int index, Input<?> variable) {
        JCheckBox f = (JCheckBox)fields.get(index);
        if(f==null) {
            // TODO ???
            return;
        }

        variable.setValue(f.isSelected());
    }

    private void readTextField(int index, Input<?> variable) {
        JTextField f = (JTextField)fields.get(index);
        if(f==null) {
            // TODO ???
            return;
        }

        if(variable.getType().equals(Number.class)) {
            variable.setValue(Double.parseDouble(f.getText()));
        } else if(variable.getType().equals(String.class)) {
            variable.setValue(f.getText());
        } else {
            // TODO ???
        }
    }

    /**
     * Returns the value of the label field
     * @return the value of the label field
     */
    private String getLabel() {
        return labelField.getText();
    }

    /**
     * main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // a test case
        Node node = new ColorAtPoint();
        EditNodePanel.runAsDialog(node,null);
    }
}

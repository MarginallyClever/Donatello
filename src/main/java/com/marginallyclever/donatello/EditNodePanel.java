package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.ColorAtPoint;
import com.marginallyclever.nodegraphcore.port.Input;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Swing UI allowing a user to edit an existing {@link Node}.
 * @author Dan Royer
 * @since 2022-02-23
 */
public class EditNodePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(EditNodePanel.class);
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
     * static so that the file chooser dialog remembers the last directory used.
     */
    private static final JFileChooser chooser = new JFileChooser();

    /**
     * The Constructor for subclasses to call.
     * @param node the {@link Node} to edit.
     */
    public EditNodePanel(Node node) {
        super();
        this.node = node;
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

    private void addVariableField(Port<?> port, GridBagConstraints c) {
        if(port instanceof Output<?> output) {
            addReadOnlyField(c, output.getName(), output.getTypeName());
        } else if(port instanceof Input<?> input) {
            var type = input.getType();
                 if(Filename.class.isAssignableFrom(type)) addFilenameField(input, c);
            else if(Number.class.isAssignableFrom(type  )) addTextField(input, c);
            else if(String.class.isAssignableFrom(type  )) addTextField(input, c);
            else if(Boolean.class.isAssignableFrom(type )) addBooleanField(input, c);
            else if(Color.class.isAssignableFrom(type   )) addColorField(input, c);
            else addReadOnlyField(c, input.getName(), input.getTypeName());
        }
    }

    /**
     * Show a file chooser dialog to select a file.
     * @param variable
     * @param c
     */
    private void addFilenameField(Input<?> variable, GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(variable.getName()),c);

        String v = ((Filename)variable.getValue()).get();
        JTextField textField = new JTextField(v);
        textField.setColumns(20);
        fields.add(textField);
        JButton button = new JButton("...");
        button.addActionListener(e -> {
            chooser.setDialogTitle("Select a file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(true);
            if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                textField.setText(chooser.getSelectedFile().getAbsolutePath());
                variable.setValue(new Filename(chooser.getSelectedFile().getAbsolutePath()));
            }
        });
        textField.setEditable(false);
        JPanel container = new JPanel(new BorderLayout());
        container.add(textField,BorderLayout.CENTER);
        container.add(button,BorderLayout.EAST);

        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        this.add(container,c);
    }

    private void addColorField(Input<?> variable, GridBagConstraints c) {
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;
        this.add(new JLabel(variable.getName()),c);

        Color v = (Color)variable.getValue();
        JButton button = new JButton("Color");
        button.setBackground(v);
        button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null,"Choose a color",v);
            if(newColor!=null) {
                button.setBackground(newColor);
                variable.setValue(newColor);
            }
        });
        fields.add(button);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx=1;
        this.add(button,c);
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link Port} to add.
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
     * @param variable the {@link Port} to add.
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
            panel.readAllFields();
        }
    }

    /**
     * Transfer the values from the edit panel to the node.
     */
    private void readAllFields() {
        int j=0;
        var size = fields.size();

        for(int i=0;i<node.getNumVariables();++i) {
            // in case the panel has more fields than the node (somehow?)
            if(j >= size) continue;

            // only care about Input ports
            Port<?> port = node.getVariable(i);
            if(!(port instanceof Input<?> input)) continue;

            var type = input.getType();
                 if(type.equals(Number.class )) readTextField(j++, input);
            else if(type.equals(String.class )) readTextField(j++, input);
            else if(type.equals(Filename.class)) readTextField(j++, input);
            else if(type.equals(Boolean.class)) readBooleanField(j++, input);
            else if(type.equals(Color.class  )) readColorField(j++, input);
            else logger.warn("readAllFields {} unknown type {}", port.getName(), port.getTypeName());
        }
    }

    private void readColorField(int index, Input<?> variable) {
        var f = (JButton)fields.get(index);
        if(f==null) {
            // TODO ???
            return;
        }
        variable.setValue(f.getBackground());
    }

    private void readBooleanField(int index, Input<?> variable) {
        var f = (JCheckBox)fields.get(index);
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

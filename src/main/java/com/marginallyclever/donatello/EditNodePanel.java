package com.marginallyclever.donatello;

import com.marginallyclever.donatello.nodes.images.ColorAtPoint;
import com.marginallyclever.donatello.select.Select;
import com.marginallyclever.donatello.select.SelectBoolean;
import com.marginallyclever.donatello.select.SelectTextField;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Input;
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
    private final JTextField labelField = new JTextField(20);
    /**
     * The fields being edited (old way).
     */
    private final ArrayList<JComponent> fields = new ArrayList<>();

    /**
     * static so that the file chooser dialog remembers the last directory used.
     */
    private static final JFileChooser chooser = new JFileChooser();
    private final Graph graph;

    /**
     * The Constructor for subclasses to call.
     * @param node the {@link Node} to edit.
     */
    public EditNodePanel(Node node,Graph graph) {
        super(new GridBagLayout());
        this.node = node;
        this.graph = graph;

        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weightx=1;
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.insets = new Insets(1,1,1,1);

        addReadOnlyField(c,"ID",node.getUniqueID());
        c.gridy++;
        addLabelField(c);
        c.gridy++;

        for(int i = 0; i<node.getNumPorts(); ++i) {
            addVariableField(node.getPort(i),c);
            c.gridy++;
        }
    }

    private void addVariableField(Port<?> port, GridBagConstraints c) {
        if(port instanceof SwingProvider swingProvider) {
            Select component = swingProvider.getSwingComponent(this);
            if(component == null) return;
            component.attach(this,c);
            setComponentReadOnly(component, port);
            return;
        }
        // no SwingProvider, do it the old way.
        var type = port.getType();
             if(Number.class.isAssignableFrom(type)) addTextField(port, c);
        else if(String.class.isAssignableFrom(type)) addTextField(port, c);
        else if(Boolean.class.isAssignableFrom(type)) addBooleanField(port, c);
        else addReadOnlyField(c, port.getName(), port.getTypeName());
    }

    private void setComponentReadOnly(Select component, Port port) {
        if(port instanceof Output<?>) {
            // controlled by this node's update()
            component.setReadOnly(true);
        } else if(graph!=null && graph.isPortConnected(port)) {
            // controlled by an upstream value
            component.setReadOnly(true);
        }
    }
    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link Port} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addTextField(Port<?> variable, GridBagConstraints c) {
        SelectTextField field = new SelectTextField(variable.getName(),variable.getName(),variable.getValue().toString());
        field.addSelectListener( evt -> {
            variable.setValue(evt.getNewValue());
        });
        field.attach(this,c);
        setComponentReadOnly(field, variable);
    }

    /**
     * Adds one variable to the panel as a label/text field pair.
     * @param variable the {@link Port} to add.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addBooleanField(Port<?> variable, GridBagConstraints c) {
        SelectBoolean field = new SelectBoolean(variable.getName(),variable.getName(),(Boolean)variable.getValue());
        field.addSelectListener( evt -> {
            variable.setValue(evt.getNewValue());
        });
        field.attach(this,c);
        setComponentReadOnly(field, variable);
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
        SelectTextField field = new SelectTextField(name,name,value);
        field.setReadOnly(true);
        field.attach(this,c);
    }

    /**
     * Displays an edit dialog for a given node.  Returns a copy of the node with any alterations, if any.
     * @param subject the node to edit.
     * @param frame the parent frame.
     */
    public static void runAsDialog(Node subject, Frame frame, Graph graph) {
        EditNodePanel panel = new EditNodePanel(subject,graph);
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

        for(int i = 0; i<node.getNumPorts(); ++i) {
            // in case the panel has more fields than the node (somehow?)
            if(j >= size) continue;

            // only care about Input ports
            if(!(node.getPort(i) instanceof Input<?> input)) continue;

            var type = input.getType();
                 if(Number.class.isAssignableFrom(type)) readTextField(j++, input);
            else if(Filename.class.isAssignableFrom(type)) readTextField(j++, input);
            else if(String.class.isAssignableFrom(type)) readTextField(j++, input);
            else if(Boolean.class.isAssignableFrom(type)) readBooleanField(j++, input);
            else if(Color.class.isAssignableFrom(type)) readColorField(j++, input);
            else logger.warn("readAllFields {} unknown type {}", input.getName(), input.getTypeName());
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
        EditNodePanel.runAsDialog(node,null,null);
    }
}

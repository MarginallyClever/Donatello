package com.marginallyclever.donatello;

import com.marginallyclever.donatello.select.*;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.nodegraphcore.Node;
import com.marginallyclever.nodegraphcore.port.Output;
import com.marginallyclever.nodegraphcore.port.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

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

        addReadOnlyField(c,"ID",node.getUniqueID());
        c.gridy++;
        addLabelField(c,node);
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
            maybeSetComponentReadOnly(component, port);
        } else {
            // no SwingProvider, do it the old way.
            runOldTypeFactory(port, c);
        }
    }

    /**
     * Adds one variable to the panel as a label/text field pair, based on the {@link Port}.
     * @param port the {@link Port} to look at
     * @param c {@link GridBagConstraints} for placement.
     */
    private void runOldTypeFactory(Port<?> port, GridBagConstraints c) {
        var type = port.getType();

        Select field;
             if(Double .class.isAssignableFrom(type)) field = new SelectDouble   (port.getName(), port.getName(), Locale.getDefault(), (double)port.getValue());
        else if(Integer.class.isAssignableFrom(type)) field = new SelectInteger  (port.getName(), port.getName(), Locale.getDefault(), (int)port.getValue());
        else if(Number .class.isAssignableFrom(type)) field = new SelectDouble   (port.getName(), port.getName(), Locale.getDefault(), ((Number)port.getValue()).doubleValue());
        else if(String .class.isAssignableFrom(type)) field = new SelectTextField(port.getName(), port.getName(), port.getValue().toString());
        else if(Boolean.class.isAssignableFrom(type)) field = new SelectBoolean  (port.getName(), port.getName(), (Boolean)port.getValue());
        else if(Color  .class.isAssignableFrom(type)) field = new SelectColor    (port.getName(), port.getName(), (Color)port.getValue(),this);
        else field = addReadOnlyField(c, port.getName(), port.getTypeName());

        field.addSelectListener( evt -> port.setValue(evt.getNewValue()) );
        field.attach(this,c);
        maybeSetComponentReadOnly(field, port);
    }

    /**
     * If the port is an output or connected, set the component to read-only.
     * @param component the component to set.
     * @param port the port to check.
     */
    private void maybeSetComponentReadOnly(Select component, Port<?> port) {
        if(port instanceof Output<?>) {
            // controlled by this node's update()
            component.setReadOnly(true);
        } else if(graph!=null && graph.isPortConnected(port)) {
            // controlled by an upstream value
            component.setReadOnly(true);
        }
    }

    /**
     * Adds the node 'label' field to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     */
    private void addLabelField(GridBagConstraints c, Node node) {
        var field = new SelectTextField("Label", "label", node.getLabel());
        field.addSelectListener( evt -> {
            node.setLabel(field.getText());
        } );
        field.attach(this,c);
    }

    /**
     * Adds one read-only label/value pair to the edit panel.
     * @param c {@link GridBagConstraints} for placement.
     * @param name the label
     * @param value the value
     */
    private Select addReadOnlyField(GridBagConstraints c,String name,String value) {
        SelectTextField field = new SelectTextField(name,name,value);
        field.setReadOnly(true);
        field.attach(this,c);
        return field;
    }

    /**
     * Displays an edit dialog for a given node.  Returns a copy of the node with any alterations, if any.
     * @param subject the node to edit.
     * @param frame the parent frame.
     */
    public static void runAsDialog(Node subject, Frame frame, Graph graph) {
        var before = subject.toJSON();
        EditNodePanel panel = new EditNodePanel(subject,graph);
        if(JOptionPane.showConfirmDialog(frame,panel,"Edit "+subject.getName(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            // user cancelled, restore the node to its original state
            subject.fromJSON(before);
        }
    }
}

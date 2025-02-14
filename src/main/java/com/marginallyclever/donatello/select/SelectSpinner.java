package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link SelectSpinner} is a {@link Select} that uses a {@link JSpinner} to select a value.
 */
public class SelectSpinner extends Select {
    private final JSpinner field;
    private final JLabel label;

    public SelectSpinner(String internalName, String labelKey, int min, int max, int value) {
        super(internalName);

        List<Integer> list = new ArrayList<>();
        for (int i = min; i<= max; i++) {
            list.add(i);
        }
        field = new JSpinner(new SpinnerListModel(list));
        field.setName(internalName+".field");

        Dimension d = field.getPreferredSize();
        d.width = 50;
        field.setPreferredSize(d);
        field.setValue(value);

        label = createLabel(labelKey);
    }

    @Override
    public void attach(JComponent panel, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx=0;
        panel.add(label,gbc);
        gbc.gridx=1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(field,gbc);
    }

    @Override
    public void setReadOnly(boolean state) {
        field.setEnabled(!state);
    }

    public int getValue() {
        return (int) field.getValue();
    }

    public void setValue(int v) {
        field.setValue(v);
    }
}

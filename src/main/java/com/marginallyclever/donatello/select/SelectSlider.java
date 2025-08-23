package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.*;

public class SelectSlider extends Select {
	private final JLabel label;
	private final JSlider field = new JSlider();
	private final JLabel value;
	private final JPanel panel2 = new JPanel(new BorderLayout());

	public SelectSlider(String internalName,String labelKey,int top,int bottom,int defaultValue) {
		super(internalName);

		value = new JLabel("0",JLabel.TRAILING);
		Dimension dim = new Dimension(30,1);
		value.setMinimumSize(dim);
		value.setPreferredSize(dim);
		value.setMaximumSize(dim);

		field.setName(internalName+".field");
		field.setMaximum(top);
		field.setMinimum(bottom);
		field.setMinorTickSpacing(1);
		field.addChangeListener(e -> {
			int n = field.getValue();
			value.setText(Integer.toString(n));
			if(field.getValueIsAdjusting()) return;
			fireSelectEvent(null,n);
		});
		field.setValue(defaultValue);

		label = createLabel(labelKey);

		panel2.add(field,BorderLayout.CENTER);
		panel2.add(value,BorderLayout.LINE_END);
	}

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx=0;
		panel.add(label,gbc);
		gbc.gridx=1;
		gbc.anchor = GridBagConstraints.LINE_END;
		panel.add(panel2,gbc);
	}

	@Override
	public void setReadOnly(boolean state) {
		field.setEnabled(!state);
	}

	public int getValue() {
		return field.getValue();
	}
	
	public void setValue(int v) {
		field.setValue(v);
	}

	public void setTop(int top) {
		field.setMaximum(top);
	}

	public void setBottom(int bottom) {
		field.setMinimum(bottom);
	}

	public int getTop() {
		return field.getMaximum();
	}

	public int getBottom() {
		return field.getMinimum();
	}
}

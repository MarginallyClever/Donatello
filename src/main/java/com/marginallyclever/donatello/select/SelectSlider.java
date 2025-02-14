package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.*;

public class SelectSlider extends Select {
	private final JSlider field = new JSlider();
	private final JLabel label;
	private final JPanel panel2;

	public SelectSlider(String internalName,String labelKey,int top,int bottom,int defaultValue) {
		super(internalName);

		JLabel value = new JLabel("0",JLabel.TRAILING);

		field.setName(internalName+".field");
		field.setMaximum(top);
		field.setMinimum(bottom);
		field.setMinorTickSpacing(1);
		field.addChangeListener((e) -> {
	        int n = field.getValue();
	        value.setText(Integer.toString(n));
	        
			if(field.getValueIsAdjusting()) return;
			
			fireSelectEvent(null,n);
		});
		field.setValue(defaultValue);

		Dimension dim = new Dimension(30,1);
		value.setMinimumSize(dim);
		value.setPreferredSize(dim);
		value.setMaximumSize(dim);

		label = createLabel(labelKey);
		panel2 = new JPanel(new BorderLayout());
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
}

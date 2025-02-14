package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A JCheckBox that sets itself up to format true/false. 
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectBoolean extends Select {
	private final JLabel label;
	private final JCheckBox field;

	/**
	 * Create a new SelectBoolean
	 * @param internalName the name of this SelectBoolean, used for debugging
	 * @param labelKey the key to use for the label, visible to users
	 * @param defaultValue the default value
	 */
	public SelectBoolean(String internalName,String labelKey,boolean defaultValue) {
		super(internalName);
		label = createLabel(labelKey);
		field = createField(defaultValue);
		field.setName(internalName+".field");
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

	private JCheckBox createField(boolean arg0) {
		JCheckBox field = new JCheckBox();
		field.setName(getName()+".field");
		field.setSelected(arg0);
		field.setBorder(new EmptyBorder(0,0,0,0));
		field.addItemListener((e)-> {
			boolean newValue = field.isSelected();
			boolean oldValue = !newValue;
			fireSelectEvent(oldValue, newValue);
		});
		return field;
	}
	
	public boolean isSelected() {
		return field.isSelected();
	}

	public void setSelected(boolean b) {
		field.setSelected(b);
	}	

}

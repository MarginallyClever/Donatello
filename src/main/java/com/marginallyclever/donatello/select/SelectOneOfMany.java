package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectOneOfMany extends Select {
	private final JComboBox<String> field = new JComboBox<>();
	private final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)field.getModel();
	private final JLabel label;

	public SelectOneOfMany(String internalName,String labelKey) {
		super(internalName);
		field.setName(internalName+".field");
		field.addActionListener((e)-> fireSelectEvent(null, field.getSelectedIndex()) );
		label = createLabel(labelKey);
	}
	
	public SelectOneOfMany(String internalName,String labelKey,String[] options,int defaultValue) {
		this(internalName,labelKey);
		
		setNewList(options);
		field.setSelectedIndex(defaultValue);
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
	
	public void removeAll() {
		model.removeAllElements();
	}
	
	public void addItem(String s) {
		model.addElement(s);
	}
	
	public void removeItem(String s) {
		model.removeElement(s);
	}
	
	public void setReadOnly() {
		field.setEditable(false);
	}
	
	public String getSelectedItem() {
		return (String)field.getSelectedItem();
	}
	
	public int getSelectedIndex() {
		return field.getSelectedIndex();
	}

	public void setSelectedIndex(int index) {
		field.setSelectedIndex(index);
		field.repaint();// Some times it need it ! but why ? normaly the swing events listener take care of that ...
	}

	public void setNewList(String[] list) {
		model.removeAllElements();
		model.addAll(Arrays.asList(list));
	}
}

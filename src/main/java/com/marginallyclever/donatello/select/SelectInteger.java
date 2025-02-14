package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * A JFormattedTextField that sets itself up to format integers. 
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectInteger extends Select {
	private final JFormattedTextField field = new JFormattedTextField();
	private final JLabel label;
	private int value;

	public SelectInteger(String internalName,String labelKey,Locale locale,int defaultValue) {
		super(internalName);
		
		value = defaultValue;

		field.setName(internalName+".field");
		createAndAttachFormatter(locale);
		Dimension d = field.getPreferredSize();
		d.width = 100;
		field.setPreferredSize(d);
		field.setMinimumSize(d);
		field.setHorizontalAlignment(JTextField.RIGHT);
		field.setValue(defaultValue);
		field.setColumns(20);
		field.getDocument().addDocumentListener(new DelayedDocumentValidator(field,newValue-> {
			if (value != newValue) {
				int oldValue = value;
				value = newValue.intValue();
				fireSelectEvent(oldValue, value);
			}
		}));

		label = createLabel(labelKey);
	}

	public SelectInteger(String internalName,String labelKey,Locale locale) {
		this(internalName,labelKey,locale,0);
	}
	
	public SelectInteger(String internalName,String labelKey,int defaultValue) {
		this(internalName,labelKey,Locale.getDefault(),defaultValue);
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
	
	protected void createAndAttachFormatter(Locale locale) {
		NumberFormat numberFormat = NumberFormat.getIntegerInstance(locale);
		numberFormat.setGroupingUsed(false);

		field.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(numberFormat)));
	}
	
	public void setReadOnly() {
		field.setEditable(false);
	}
	
	/**
	 * @return last valid integer typed into field.
	 */
	public int getValue() {
		return value;
	}
	
	public void setValue(int arg0) {
		field.setText(Integer.toString(arg0));
	}
}

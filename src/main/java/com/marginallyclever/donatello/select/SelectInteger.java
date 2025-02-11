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
	private JFormattedTextField field;
	private int value;

	public SelectInteger(String internalName,String labelKey,Locale locale,int defaultValue) {
		super(internalName);
		
		value = defaultValue;

		JLabel label = new JLabel(labelKey, JLabel.LEADING);

		field = new JFormattedTextField();
		createAndAttachFormatter(locale);
		Dimension d = field.getPreferredSize();
		d.width = 100;
		field.setPreferredSize(d);
		field.setMinimumSize(d);
		field.setValue(defaultValue);
		field.setHorizontalAlignment(JTextField.RIGHT);
		field.getDocument().addDocumentListener(new DelayedDocumentValidator(field,newValue-> {
			if (value != newValue) {
				double oldValue = value;
				value = newValue.intValue();
				fireSelectEvent(oldValue, newValue);
			}
		}));

		this.add(label,BorderLayout.LINE_START);
		this.add(field,BorderLayout.LINE_END);
	}

	public SelectInteger(String internalName,String labelKey,Locale locale) {
		this(internalName,labelKey,locale,0);
	}
	
	public SelectInteger(String internalName,String labelKey,int defaultValue) {
		this(internalName,labelKey,Locale.getDefault(),defaultValue);
	}
	
	public SelectInteger(String internalName) {
		super(internalName);
		createAndAttachFormatter(Locale.getDefault());
	}
	
	protected void createAndAttachFormatter(Locale locale) {
		NumberFormat nFloat = NumberFormat.getIntegerInstance(locale);
		nFloat.setGroupingUsed(false);
		
		NumberFormatter nff = new NumberFormatter(nFloat);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(nff);
		field.setFormatterFactory(factory);
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

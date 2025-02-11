package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Timer;


/**
 * A JFormattedTextField that sets itself up to format floating point numbers.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectDouble extends Select {
	private final JTextField field = new JTextField();
	private double value;
	private Timer timer;

	public SelectDouble(String internalName,String labelKey, Locale locale, double defaultValue) {
		super(internalName);

		value = defaultValue;

		JLabel label = new JLabel(labelKey, JLabel.LEADING);
		label.setName(internalName+".label");

		field.setName(internalName+".field");
		Dimension d = field.getPreferredSize();
		d.width = 100;
		field.setPreferredSize(d);
		field.setMinimumSize(d);
		field.setHorizontalAlignment(JTextField.RIGHT);
		setValue(defaultValue);

		field.getDocument().addDocumentListener(new DelayedDocumentValidator(field,newValue-> {
			if (value != newValue) {
				double oldValue = value;
				value = newValue;
				fireSelectEvent(oldValue, newValue);
			}
		}));

		this.add(label, BorderLayout.LINE_START);
		this.add(field, BorderLayout.LINE_END);
	}

	public SelectDouble(String internalName,String labelKey, Locale locale) {
		this(internalName,labelKey, locale, 0);
	}

	public SelectDouble(String internalName,String labelKey, double defaultValue) {
		this(internalName,labelKey, Locale.getDefault(), defaultValue);
	}

	public SelectDouble(String internalName,String labelKey) {
		this(internalName,labelKey, Locale.getDefault(), 0);
	}

	protected SelectDouble(String internalName) {
		this(internalName,"", Locale.getDefault(), 0);
	}

	public void setReadOnly() {
		field.setEditable(false);
	}

	public void setReadOnly(boolean state) {
		field.setEditable(!state);
	}

	// @return last valid value typed into field
	public double getValue() {
		return value;
	}

	/**
	 * Set the value visible in the field.  Do not fire a property change event.
	 * @param newValue the new value to display
     */
	public void setValue(double newValue) {
		field.setText(String.format(Locale.US,"%.3f", newValue));
	}
	
	public String getText() {
		return field.getText();
	}
}

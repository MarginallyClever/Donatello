package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;


/**
 * A JFormattedTextField that sets itself up to format floating point numbers.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectDouble extends Select {
	private final JFormattedTextField field = new JFormattedTextField();
	private final JLabel label;
	private double value;
	private Timer timer;

	public SelectDouble(String internalName,String labelKey, Locale locale, double defaultValue) {
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
				double oldValue = value;
				value = newValue;
				fireSelectEvent(oldValue, value);
			}
		}));

		label = createLabel(labelKey);
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

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx=0;
		panel.add(label,gbc);
		gbc.gridx=1;
		gbc.anchor = GridBagConstraints.LINE_END;
		panel.add(field,gbc);
	}

	protected void createAndAttachFormatter(Locale locale) {
		NumberFormat nFloat = NumberFormat.getNumberInstance(locale);
		nFloat.setGroupingUsed(false);

		field.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(nFloat)));
	}

	@Override
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

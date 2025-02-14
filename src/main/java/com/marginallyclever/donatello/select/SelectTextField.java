package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * A short text input.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectTextField extends Select {
	private final JTextField field;
	private final JLabel label;

	public SelectTextField(String internalName, String labelKey, String defaultText) {
		super(internalName);
		//this.setBorder(BorderFactory.createLineBorder(Color.RED));

		field = new JTextField(defaultText,20);
		field.setName(internalName+".field");
		Dimension d = field.getPreferredSize();
		d.width = 100;
		field.setPreferredSize(d);
		field.setMinimumSize(d);
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				validate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				validate();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				validate();
			}
			
			void validate() {
				fireSelectEvent(null,field.getText());
			}
		});

		label = createLabel(labelKey);
	}

	@Override
	public void setReadOnly(boolean state) {
		field.setEnabled(!state);
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

	public String getText() {
		return field.getText();
	}
	
	public void setText(String str) {
		field.setText(str);
	}

	public boolean isEditable() {
		return field.isEditable();
	}

	public void setEditable(boolean b) {
		field.setEditable(b);
	}

	public boolean getDragEnabled() {
		return field.getDragEnabled();
	}

	public void setDragEnabled(boolean b) {
		field.setDragEnabled(b);
	}
}

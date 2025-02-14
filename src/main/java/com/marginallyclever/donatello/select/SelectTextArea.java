package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * A text input dialog with some limited formatting options.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectTextArea extends Select {
	private final JTextArea field;
	private final JLabel label;
	private final JScrollPane pane;

	public SelectTextArea(String internalName,String labelKey,String defaultText) {
		super(internalName);
		//this.setBorder(BorderFactory.createLineBorder(Color.RED));

		field = new JTextArea(defaultText, 4, 20);
		field.setName(internalName+".field");
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		field.setBorder(BorderFactory.createLoweredBevelBorder());
		field.setFont(UIManager.getFont("Label.font"));
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
				fireSelectEvent(null, field.getText());
			}
		});

		pane = new JScrollPane(field);
		pane.setPreferredSize(new Dimension(200, 150));

		label = createLabel(labelKey);
	}

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.gridx=0;
		gbc.gridwidth=2;
		panel.add(label,gbc);
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(pane,gbc);
		gbc.gridwidth=1;
	}

	@Override
	public void setReadOnly(boolean state) {
		field.setEnabled(!state);
	}

	public String getText() {
		return field.getText();
	}
	
	public void setText(String str) {
		field.setText(str);
	}

	public void setLineWrap(boolean wrap) {
		field.setLineWrap(wrap);
	}

	public boolean getLineWrap() {
		return field.getLineWrap();
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

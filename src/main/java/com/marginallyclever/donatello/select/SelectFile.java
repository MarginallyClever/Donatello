package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * A file selection dialog
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectFile extends Select {
	private final JTextField field;
	private final JLabel label;
	private final JPanel panel2;
	private FileFilter filter = null;
	private JFileChooser choose = new JFileChooser();
	private final Component parentComponent;

	public SelectFile(String internalName,String labelKey,String defaultValue,Component parentComponent) {
		super(internalName);
		this.parentComponent = parentComponent;

		field = new JTextField(defaultValue, 16);
		field.setName(internalName+".field");
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
		//field.setBorder(new LineBorder(Color.BLACK));

		JButton chooseButton = new JButton("...");
		chooseButton.addActionListener(e -> field.setText(selectFile(field.getText())));
		
		panel2 = new JPanel(new BorderLayout());
		panel2.add(field,BorderLayout.CENTER);
		panel2.add(chooseButton,BorderLayout.LINE_END);

		label = createLabel(labelKey);
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
	
	public String getText() {
		return field.getText();
	}
	
	private String selectFile(String cancelValue) {
		choose.setFileFilter(filter);
		choose.setCurrentDirectory(new File(cancelValue));
		int returnVal = choose.showDialog(parentComponent,null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = choose.getSelectedFile();
			return file.getAbsolutePath();
		} else {
			return cancelValue;
		}
	}
	
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

	/**
	 * Will notify observers that the value has changed.
	 * @param string the new value
	 */
	public void setText(String string) {
		field.setText(string);
	}

	public void setPathOnly() {
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	public void setFileOnly() {
		choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	public void setFileChooser(JFileChooser fileChooser) {
		if(fileChooser==null) throw new NullPointerException("fileChooser cannot be null");
		choose = fileChooser;
	}

	/**
	 * @param isSave true for save dialog, false for load dialog.  Default is false.
	 */
	public void setDialogType(boolean isSave) {
		choose.setDialogType(isSave ? JFileChooser.SAVE_DIALOG : JFileChooser.OPEN_DIALOG);
	}
}

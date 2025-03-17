package com.marginallyclever.donatello.select;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;



/**
 * A button that does nothing until you attach an observer.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectButton extends Select {
	private final List<ActionListener> actionListenerList = new ArrayList<>();
	private final JButton button;

	public SelectButton(String internalName,AbstractAction action) {
		super(internalName);
		button = new JButton(action);
		button.setName(internalName+".button");
	}

	public SelectButton(String internalName, String labelText) {
		super(internalName);
		
		button = new JButton(labelText);
		button.addActionListener((e) -> {
			fireActionEvent();
		});
	}

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx=0;
		gbc.gridwidth=2;
		panel.add(button,gbc);
		gbc.gridwidth=1;
	}

	@Override
	public void setReadOnly(boolean state) {
		button.setEnabled(!state);
	}

	public void doClick() {
		if(button!=null) button.doClick();
	}
	
	public void setText(String label) {
		if(button!=null) button.setText(label);
	}
	
	public void setEnabled(boolean b) {
		if(button!=null) button.setEnabled(b);
	}

	public void setForeground(Color fg) {
		if(button!=null) button.setForeground(fg);
	}

	public void addActionListener(ActionListener l) {
		actionListenerList.add(l);
	}

	public void removeActionListener(ActionListener l) {
		actionListenerList.remove(l);
	}

	public void fireActionEvent() {
		for(ActionListener l : actionListenerList) {
			l.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,getName()));
		}
	}

	public JButton getButton() {
		return button;
	}
}

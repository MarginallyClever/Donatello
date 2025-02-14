package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;

/**
 * Base class for all Select.  A Select is a UI panel item the user can control.
 * This system provides consistent look and behavior across all elements in the app.
 * @author Dan Royer
 * @since 7.24.0
 */
public abstract class Select {
	private final EventListenerList listeners = new EventListenerList();
	private String name;

	protected Select(String name) {
		super();
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addSelectListener(SelectListener listener) {
		listeners.add(SelectListener.class,listener);
	}

	public void removeSelectListener(SelectListener listener) {
		listeners.remove(SelectListener.class,listener);
	}

	protected void fireSelectEvent(Object oldValue,Object newValue) {
		try {
			SelectEvent evt = new SelectEvent(this, oldValue, newValue);
			for (SelectListener listener : listeners.getListeners(SelectListener.class)) {
				listener.selectEvent(evt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected JLabel createLabel(String labelKey) {
		JLabel label = new JLabel(labelKey, JLabel.LEADING);
		label.setName(getName()+".label");
		return label;
	}

	/**
	 *
	 * @param panel the parent component
	 * @param gbc the GridBagConstraints for this component
	 */
	abstract public void attach(JComponent panel, GridBagConstraints gbc);

    abstract public void setReadOnly(boolean b);
}

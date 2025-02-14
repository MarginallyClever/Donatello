package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A container for all Select elements, to facilitate formatting as a group.
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectPanel extends JPanel {
	private final JPanel interiorPanel = new JPanel();
	private final GridBagConstraints gbc = new GridBagConstraints();

	public SelectPanel() {
		super(new BorderLayout());
		add(interiorPanel,BorderLayout.PAGE_START);

		interiorPanel.setBorder(new EmptyBorder(5,5,5,5));
		interiorPanel.setLayout(new GridBagLayout());
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx=1;
		gbc.weighty=0;
	}
	
	public void add(Select c) {
		c.attach(interiorPanel,gbc);
		gbc.gridy++;
	}

	public void clear() {
		interiorPanel.removeAll();
	}
}

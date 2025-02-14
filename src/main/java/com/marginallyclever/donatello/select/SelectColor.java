package com.marginallyclever.donatello.select;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * A color selection dialog
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectColor extends Select {
	private final JLabel label;
	private final BackgroundPaintedButton chooseButton;
	
	/**
	 * @param parentComponent a component (JFrame, JPanel) that owns the color selection dialog
	 * @param labelValue
	 * @param defaultValue
	 */
	public SelectColor(String internalName,String labelValue,Color defaultValue,final Component parentComponent) {
		super(internalName);

		chooseButton = new BackgroundPaintedButton("");
		chooseButton.setOpaque(true);
		chooseButton.setMinimumSize(new Dimension(80,20));
		chooseButton.setMaximumSize(chooseButton.getMinimumSize());
		chooseButton.setPreferredSize(chooseButton.getMinimumSize());
		chooseButton.setSize(chooseButton.getMinimumSize());
		chooseButton.setBackground(defaultValue);
		chooseButton.setBorder(new LineBorder(Color.BLACK));
		chooseButton.addActionListener(e -> {
			Color c = JColorChooser.showDialog(parentComponent, labelValue, chooseButton.getBackground());
			if ( c != null ){
				chooseButton.setBackground(c);
				fireSelectEvent(null,c);
			}
		});
		chooseButton.setName(internalName+".button");

		label = createLabel(labelValue);
	}

	@Override
	public void setReadOnly(boolean state) {
		chooseButton.setEnabled(!state);
	}

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx=0;
		panel.add(label,gbc);
		gbc.gridx=1;
		gbc.anchor = GridBagConstraints.LINE_END;
		panel.add(chooseButton,gbc);
	}

	public Color getColor() {
		return chooseButton.getBackground();
	}
	
	public void setColor(Color c) {
		chooseButton.setBackground(c);
	}
}

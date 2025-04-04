package com.marginallyclever.donatello.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Read only JEditorPane with an HyperlinkListener for text/html, contening or not, multiple html link.
 * With clickable link (Desktop.BROWSE) and ToolTips when hovered over.
 * 
 * @author Dan Royer
 * @since 7.24.0
 */
public class SelectReadOnlyText extends Select {
	private static final Logger logger = LoggerFactory.getLogger(SelectReadOnlyText.class);
	private final JEditorPane jEditorPane;

	public SelectReadOnlyText(String internalName,String labelKey) {
		super(internalName);

		jEditorPane = createJEditorPaneWithHyperlinkListenerAndToolTipsForDesktopBrowse("<html>" + labelKey + "</html>");
		jEditorPane.setName(internalName+".field");
	}

	@Override
	public void attach(JComponent panel, GridBagConstraints gbc) {
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx=0;
		gbc.gridwidth = 2;
		panel.add(jEditorPane,gbc);
		gbc.gridwidth = 1;
	}

	@Override
	public void setReadOnly(boolean state) {}
	
	/**
	 * Create a JEditorPane not editable for text/html contente, with an HyperLinkListener to Desktop Browse (when clicked) and show a ToolTips with the URL hovered.
	 * @param sToSetAsTextToTheHtmlEditorPane
	 * @return 
	 */
	public static JEditorPane createJEditorPaneWithHyperlinkListenerAndToolTipsForDesktopBrowse(String sToSetAsTextToTheHtmlEditorPane) {
		JEditorPane createdJEditorPane =  new JEditorPane();
		createdJEditorPane.setEditable(false);
		createdJEditorPane.setOpaque(false);
		createdJEditorPane.setContentType("text/html");
		createdJEditorPane.setText(sToSetAsTextToTheHtmlEditorPane);
		createdJEditorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		createdJEditorPane.setFont(UIManager.getFont("Label.font"));
		createdJEditorPane.addHyperlinkListener((HyperlinkEvent hyperlinkEvent) -> reactToHyperlink(hyperlinkEvent,createdJEditorPane));
		return createdJEditorPane;
	}

	private static void reactToHyperlink(HyperlinkEvent hyperlinkEvent,JEditorPane createdJEditorPane) {
		HyperlinkEvent.EventType eventType = hyperlinkEvent.getEventType();
		if(eventType == HyperlinkEvent.EventType.ACTIVATED) {
			if (Desktop.isDesktopSupported()) {
				try {
					URI u = hyperlinkEvent.getURL().toURI();
					Desktop desktop = Desktop.getDesktop();
					if (desktop.isSupported(Desktop.Action.BROWSE)) {
						logger.debug("Desktop.Action.BROWSE {}", u);
						desktop.browse(u);
					} else {
						logger.error("Desktop.Action.BROWSE not supported. Cant browse {}", u);
					}
				} catch (IOException | URISyntaxException e) {
					logger.error("Failed to open the browser to the url", e);
				}
			} else {
				logger.error("Desktop not supported. Cant browse {}", hyperlinkEvent.getURL());
			}
		} else if(eventType == HyperlinkEvent.EventType.ENTERED) {
			if (!(hyperlinkEvent.getURL() == null || hyperlinkEvent.getURL().toString().isEmpty())) {
				createdJEditorPane.setToolTipText(hyperlinkEvent.getURL().toExternalForm());

				// TODO should be set at the starting of the application
				ToolTipManager.sharedInstance().setInitialDelay(0);
				ToolTipManager.sharedInstance().setDismissDelay(5000);
			}
		} else if(eventType == HyperlinkEvent.EventType.EXITED) {
			createdJEditorPane.setToolTipText(null);// null to turn off the tooltips.
		}
	}
}

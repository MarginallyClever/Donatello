package com.marginallyclever.donatello;

import com.marginallyclever.donatello.actions.BrowseURLAction;
import com.marginallyclever.nodegraphcore.DAO4JSONFactory;
import com.marginallyclever.nodegraphcore.NodeFactory;
import com.marginallyclever.nodegraphcore.ServiceLoaderHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class DonatelloApp {
    /**
     * Main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(DonatelloApp::new);
    }

    public DonatelloApp() {
        FileHelper.createDirectoryIfMissing(FileHelper.getExtensionPath());
        ServiceLoaderHelper.addAllPathFiles(FileHelper.getExtensionPath());

        NodeFactory.loadRegistries();
        DAO4JSONFactory.loadRegistries();

        Donatello.setLookAndFeel();

        PropertiesHelper.showProperties();
        PropertiesHelper.listAllNodes();
        PropertiesHelper.listAllDAO();

        Donatello donatello = new Donatello();

        JFrame frame = new JFrame("Donatello");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,800));
        frame.setLocationRelativeTo(null);
        frame.add(donatello);
        setupMenuBar(donatello, frame);
        frame.setVisible(true);

        addDoubleClickToEdit(donatello);
    }

    private void addDoubleClickToEdit(Donatello donatello) {
        // Add double-click functionality
        donatello.getPaintArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    Point cursorPosition = e.getPoint();
                    donatello.editNodeAt(cursorPosition); // Call edit method
                }
            }
        });
    }

    private void setupMenuBar(Donatello donatello,JFrame topFrame) {
        JMenuBar menuBar = new JMenuBar();
        topFrame.setJMenuBar(menuBar);

        menuBar.add(donatello.getUndoRedoMenu());
        menuBar.add(getHelpMenu());
    }

    private JMenu getHelpMenu() {
        JMenu menu = new JMenu("Help");

        BrowseURLAction showLog = new BrowseURLAction("Open log file",FileHelper.convertToFileURL(FileHelper.getLogFile()));
        BrowseURLAction update = new BrowseURLAction("Check for updates","https://github.com/MarginallyClever/GraphCore/releases");
        BrowseURLAction problem = new BrowseURLAction("I have a problem...","https://github.com/MarginallyClever/GraphCore/issues");
        BrowseURLAction drink = new BrowseURLAction("Buy me a drink","https://www.paypal.com/donate/?hosted_button_id=Y3VZ66ZFNUWJE");
        BrowseURLAction community = new BrowseURLAction("Join the community","https://discord.gg/TbNHKz6rpy");
        BrowseURLAction idea = new BrowseURLAction("I have an idea!","https://github.com/MarginallyClever/GraphCore/issues");

        community.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-discord-16.png"))));
        drink.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-cocktail-16.png"))));
        update.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-newspaper-16.png"))));
        problem.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-telephone-16.png"))));
        idea.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-light-bulb-16.png"))));
        problem.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull( DonatelloApp.class.getResource("/com/marginallyclever/donatello/icons8-bug-16.png"))));

        menu.add(community);
        menu.add(drink);
        menu.add(update);
        menu.addSeparator();
        menu.add(idea);
        menu.add(problem);
        menu.addSeparator();
        menu.add(showLog);

        return menu;
    }
}

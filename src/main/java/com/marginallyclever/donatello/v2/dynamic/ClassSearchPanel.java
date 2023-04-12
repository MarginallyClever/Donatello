package com.marginallyclever.donatello.v2.dynamic;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A panel that can be used to search for a class and invoke a method dynamically at runtime. The method can be static
 * or non-static.
 */
public class ClassSearchPanel extends JPanel {
    private final JTextField searchBar;
    private final JList<String> searchResults;
    private final DefaultListModel<String> searchResultsModel;
    private final JScrollPane searchResultsScrollPane;

    private final Set<String> classNames;

    private MethodSelectionPanel methodSelectionPanel;

    public ClassSearchPanel() {
        setLayout(new BorderLayout());

        // Get the list of visible packages and classes
        classNames = ClassPathScanner.getVisiblePackagesAndClasses();
        ArrayList<String> names = new ArrayList<>(classNames);
        Collections.sort(names);
        names.forEach(System.out::println);

        searchBar = new JTextField(30);
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent documentEvent) {
                updateSearchResults();
            }

            public void insertUpdate(DocumentEvent documentEvent) {
                updateSearchResults();
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                updateSearchResults();
            }
        });
        searchBar.addKeyListener(new SearchBarKeyListener());
        searchBar.setFocusTraversalKeysEnabled(false);

        searchResultsModel = new DefaultListModel<>();
        searchResultsModel.setSize(15);
        searchResults = new JList<>(searchResultsModel);
        searchResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResults.addKeyListener(new SearchResultKeyListener());
        searchResults.addMouseListener(new SearchResultMouseListener());
        searchResults.setVisibleRowCount(5);
        searchResults.setVisible(true);

        searchResultsScrollPane = new JScrollPane(searchResults);
        searchResultsScrollPane.setVisible(false);

        setBorder(BorderFactory.createTitledBorder("Class"));
        add(searchBar, BorderLayout.NORTH);
        add(searchResultsScrollPane, BorderLayout.CENTER);

        searchBar.setText("java.lang.String");
    }

    private void updateSearchResults() {
        String searchText = searchBar.getText().trim();
        searchResultsModel.clear();

        if (searchText.isEmpty()) {
            searchResultsScrollPane.setVisible(false);
            return;
        }

        // Perform the search and get the class names based on the search text
        List<String> closeMatches = new ArrayList<>();
        int matchCount = 0;
        for (String className : classNames) {
            if (className.startsWith(searchText)) {
                int firstDotIndex = className.indexOf('.', searchText.length());
                if (firstDotIndex != -1) {
                    className = className.substring(0, firstDotIndex);
                }
                if(!closeMatches.contains(className)) {
                    closeMatches.add(className);
                }
            }
        }

        Collections.sort(closeMatches);
        for(String className : closeMatches) {
            searchResultsModel.addElement(className);
            matchCount++;
        }

        searchResultsScrollPane.setVisible(!searchResultsModel.isEmpty());
    }

    private class SearchBarKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                updateItemFound();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                searchResults.requestFocus();
            }
        }
    }

    private void updateItemFound() {
        if (!searchBar.getText().trim().isEmpty()) {
            String className = searchBar.getText().trim();
            if(classExists(className)) {
                System.out.println("Found class "+className);
                searchResultsScrollPane.setVisible(false);

                // Replace the center part of the panel with MethodSelectionPanel
                if(methodSelectionPanel!=null) {
                    remove(methodSelectionPanel);
                }
                methodSelectionPanel = new MethodSelectionPanel(className);
                add(methodSelectionPanel, BorderLayout.CENTER);
            }
            revalidate();
            repaint();
        } else if(methodSelectionPanel!=null) {
            methodSelectionPanel.setVisible(false);
        }
    }

    private boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private class SearchResultKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                changeSearchBarItemSelected();
            } else if (e.getKeyCode() == KeyEvent.VK_UP && searchResults.getSelectedIndex() == 0) {
                searchBar.requestFocus();
            }
        }
    }

    private class SearchResultMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                changeSearchBarItemSelected();
            }
        }
    }

    private void changeSearchBarItemSelected() {
        String className = searchResults.getSelectedValue();
        searchBar.setText(className);
        searchResultsScrollPane.setVisible(false);
        updateItemFound();
    }
}

package com.marginallyclever.donatello.search;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class SearchBar extends JPanel {
    /**
     * The search field
     */
    private final JTextField search = new JTextField();
    private final JToggleButton caseSensitive = new JToggleButton("Aa");
    private final JToggleButton regularExpression = new JToggleButton(".*");
    private static String searchValue = "";
    private final ArrayList<SearchListener> listeners = new ArrayList<>();

    public SearchBar() {
        super(new BorderLayout());

        this.add(new JLabel(" \uD83D\uDD0D "), BorderLayout.WEST);
        this.add(search, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new BorderLayout());
        buttons.add(caseSensitive, BorderLayout.WEST);
        buttons.add(regularExpression, BorderLayout.EAST);
        this.add(buttons, BorderLayout.EAST);

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchFor(search.getText());
            }
        });

        caseSensitive.addActionListener((e)->searchFor(search.getText()));
        search.setText(searchValue);
    }

    protected void searchFor(String text) {
        searchValue = text;
        listeners.forEach(l -> l.searchFor(text,caseSensitive.isSelected(),regularExpression.isSelected()) );
    }

    public String getText() {
        return search.getText();
    }

    public void setText(String text) {
        search.setText(text);
    }

    public boolean getCaseSensitive() {
        return caseSensitive.isSelected();
    }

    public void addSearchListener(SearchListener listener) {
        listeners.add(listener);
    }

    public void removeSearchListener(SearchListener listener) {
        listeners.remove(listener);
    }
}

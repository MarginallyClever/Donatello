package com.marginallyclever.donatello;

import com.marginallyclever.donatello.actions.GraphSaveAsAction;
import com.marginallyclever.donatello.actions.undoable.GraphLoadAction;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * {@link RecentFilesMenu} is a menu that keeps track of recently loaded files.
 */
public class RecentFilesMenu extends JMenu {
    private final Preferences preferences;
    private static final int MAX_RECENT_FILES = 10;
    private final List<String> recentFiles = new ArrayList<>();
    private GraphSaveAsAction saveScene;
    private final Donatello editor;

    public RecentFilesMenu(Preferences preferences, Donatello editor) {
        this(preferences,"Recent Files",editor);
    }

    public RecentFilesMenu(Preferences preferences, String title, Donatello editor) {
        super(title);
        this.preferences = preferences;
        this.editor = editor;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadFromPreferences();
        updateRecentFilesMenu();
    }

    @Override
    public void removeNotify() {
        saveToPreferences();
        super.removeNotify();
    }

    /**
     * Loads the list of recent files from preferences.
     * Loads at most {@link RecentFilesMenu#MAX_RECENT_FILES} items.
     */
    public void loadFromPreferences() {
        int count = preferences.getInt("recentFiles.count", MAX_RECENT_FILES);
        for (int i = 0; i < count; i++) {
            String filePath = preferences.get("recentFiles." + i, "");
            if (!filePath.isEmpty()) {
                recentFiles.remove(filePath);
                recentFiles.add(filePath);
            }
        }
    }

    /**
     * Saves the list of recent files to preferences.
     * Saves at most {@link RecentFilesMenu#MAX_RECENT_FILES} items.
     */
    public void saveToPreferences() {
        preferences.putInt("recentFiles.count", recentFiles.size());
        int count = Math.min(recentFiles.size(), MAX_RECENT_FILES);
        for (int i = 0; i < count; i++) {
            preferences.put("recentFiles." + i, recentFiles.get(i));
        }

        // remove any extra entries
        for (int i = count; preferences.get("recentFiles." + i, null) != null; i++) {
            preferences.remove("recentFiles." + i);
        }
    }

    private void updateRecentFilesMenu() {
        removeAll();
        int index=0;
        for (String filePath : recentFiles) {
            var load = new GraphLoadAction(this,filePath,editor);
            load.setSaveScene(saveScene);
            JMenuItem menuItem = new JMenuItem(load);
            add(menuItem);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + index, InputEvent.CTRL_DOWN_MASK));

            ++index;
        }
        setVisible(!recentFiles.isEmpty());
        revalidate();
        repaint();
    }

    public void removePath(String filePath) {
        recentFiles.remove(filePath);
        saveToPreferences();
        updateRecentFilesMenu();
    }

    /**
     * Adds a path to the list of recent files.  If the path already exists in the list, it will be moved to the head of the list.
     * @param filePath the path to add.
     */
    public void addPath(String filePath) {
        // remove the path if it already exists
        recentFiles.remove(filePath);
        // add to the top of the list.
        recentFiles.add(0,filePath);
        trimList();
        saveToPreferences();
        updateRecentFilesMenu();
    }

    private void trimList() {
        while(recentFiles.size()> MAX_RECENT_FILES) {
            recentFiles.remove(recentFiles.size()-1);
        }
    }

    /**
     * Clears the list of recent files.  To commit the change, call {@link #saveToPreferences()}.
     */
    public void clear() {
        recentFiles.clear();
        updateRecentFilesMenu();
    }

    public String getMostRecentLoad() {
        return recentFiles.get(0);
    }

    public void setSaveScene(GraphSaveAsAction saveScene) {
        this.saveScene = saveScene;
    }
}

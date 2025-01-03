package com.marginallyclever.donatello.actions;

import com.marginallyclever.donatello.RecentFilesMenu;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Launches a "select file to save" dialog and attempts to save the {@link Graph} to disk.
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphSaveAsAction extends AbstractAction {
    private String lastLoadedPath;

    /**
     * The editor being affected.
     */
    private final Donatello editor;

    private final RecentFilesMenu menu;
    /**
     * The file chooser remembers the last path.
     */
    private final JFileChooser fc = new JFileChooser();

    /**
     * Constructor for subclasses to call.
     * @param menu the menu to update with the new file.
     * @param name the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphSaveAsAction(RecentFilesMenu menu, String name, Donatello editor) {
        super(name);
        this.menu = menu;
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // save as dialog
        fc.setFileFilter(Donatello.FILE_FILTER);
        if (fc.showSaveDialog(SwingUtilities.getWindowAncestor(editor)) == JFileChooser.APPROVE_OPTION) {
            String name = addExtensionIfNeeded(fc.getSelectedFile().getAbsolutePath());
            lastLoadedPath = name;
            commitSave(name);
        }
    }

    public String addExtensionIfNeeded(String filename) {
        int last = filename.lastIndexOf(".");
        String[] extensions = Donatello.FILE_FILTER.getExtensions();
        if(last == -1) {
            // no extension at all
            return filename + "." + extensions[0];
        }

        String end = filename.substring(last+1);
        for(String ext : extensions) {
            // has valid extension
            if(end.equals(ext)) return filename;
        }
        // no matching extension
        return filename + "." + extensions[0];
    }

    private void commitSave(String absolutePath) {
        try(BufferedWriter w = new BufferedWriter(new FileWriter(absolutePath))) {
            w.write(editor.getGraph().toJSON().toString());
            if(menu!=null) menu.addPath(absolutePath);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor),e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void setPath(String absolutePath) {
        lastLoadedPath = absolutePath;
        putValue(Action.SHORT_DESCRIPTION,"Save "+absolutePath);
    }
}

package com.marginallyclever.donatello.actions.undoable;

import com.marginallyclever.donatello.RecentFilesMenu;
import com.marginallyclever.donatello.actions.GraphSaveAsAction;
import com.marginallyclever.nodegraphcore.Graph;
import com.marginallyclever.donatello.Donatello;
import com.marginallyclever.donatello.edits.GraphPasteEdit;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * <p>Launches a "select file to open" dialog and attempts to load the {@link Graph} from disk.</p>
 * <p>Use {@link GraphLoadAction#commitLoad} to load a file without a dialog.</p>
 * @author Dan Royer
 * @since 2022-02-21
 */
public class GraphLoadAction extends AbstractAction {
    private static final Logger logger = LoggerFactory.getLogger(GraphLoadAction.class);

    private final Donatello editor;
    private final RecentFilesMenu menu;

    /**
     * The file chooser remembers the last path.
     */
    private final JFileChooser chooser = new JFileChooser();
    private final String filePath;

    private GraphSaveAsAction saveScene;

    /**
     * Constructor for subclasses to call.
     * @param filePath the name of this action visible on buttons and menu items.
     * @param editor the editor affected by this Action.
     */
    public GraphLoadAction(RecentFilesMenu menu, String filePath, Donatello editor) {
        super();
        this.menu = menu;
        this.editor = editor;
        this.filePath = filePath;

        if(filePath==null || filePath.isEmpty()) {
            putValue(Action.NAME,"Load...");
        } else {
            int maxLength = 60;
            String shortName = filePath.length() > maxLength
                    ? "..." + filePath.substring(filePath.length()-maxLength - 3)
                    : filePath;
            putValue(Action.NAME,shortName);
        }
        putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull("/com/marginallyclever/donatello/icons8-load-16.png")));
        putValue(SHORT_DESCRIPTION,"Load a graph from a file.  Completely replaces the current graph.");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File src = (filePath != null)
                ? new File(filePath)
                : runFileDialog((Component)e.getSource());
        if( src == null) return;
        try {
            commitLoad(src);
        }
        catch(Exception ex) {
            logger.error("Failed to load graph. ",ex);
            JOptionPane.showMessageDialog((Component) e.getSource(),
                    "Error loading file.  " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        // TODO UndoSystem.reset();
    }

    private File runFileDialog(Component source) {
        if( chooser == null ) throw new InvalidParameterException("file chooser cannot be null");
        chooser.setFileFilter(Donatello.FILE_FILTER);
        JFrame parentFrame = (JFrame)SwingUtilities.getWindowAncestor(source);
        if (chooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;  // cancelled
    }

    public void commitLoad(File selectedFile) {
        if(!selectedFile.exists()) {
            menu.removePath(selectedFile.getAbsolutePath());
            throw new InvalidParameterException("File does not exist");
        }

        StringBuilder jsonString = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)))) {
            String inputStr;
            while ((inputStr = reader.readLine()) != null) {
                jsonString.append(inputStr);
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editor), e.getLocalizedMessage());
            e.printStackTrace();
        }

        Graph newModel = new Graph();
        newModel.fromJSON(new JSONObject(jsonString.toString()));
        editor.addEdit(new GraphPasteEdit((String)this.getValue(Action.NAME),editor,newModel,new Point()));

        if(saveScene!=null) {
            saveScene.setPath(selectedFile.getAbsolutePath());
            saveScene.setEnabled(true);
        }
        if(menu!=null) menu.addPath(selectedFile.getAbsolutePath());
    }

    public void setSaveScene(GraphSaveAsAction saveScene) {
        this.saveScene = saveScene;
    }
}
package com.marginallyclever.donatello;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.marginallyclever.donatello.actions.*;
import com.marginallyclever.donatello.actions.undoable.*;
import com.marginallyclever.donatello.contextsensitivetools.ConnectionEditTool;
import com.marginallyclever.donatello.contextsensitivetools.ContextSensitiveTool;
import com.marginallyclever.donatello.contextsensitivetools.NodeMoveTool;
import com.marginallyclever.donatello.contextsensitivetools.RectangleSelectTool;
import com.marginallyclever.donatello.graphview.GraphViewListener;
import com.marginallyclever.donatello.graphview.GraphViewPanel;
import com.marginallyclever.donatello.graphview.GraphViewSettings;
import com.marginallyclever.donatello.graphview.GraphViewSettingsPanel;
import com.marginallyclever.nodegraphcore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * {@link Donatello} is a Graphic User Interface to edit a {@link Graph}.
 * @author Dan Royer
 * @since 2022-02-01
 */
public class Donatello extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(Donatello.class);
    /**
     * Used by save and load actions
      */
    public static final FileNameExtensionFilter FILE_FILTER = new FileNameExtensionFilter("Node Graph","graph");

    private static final Color COLOR_SELECTED_NODE_FIRST = Color.CYAN;
    private static final Color COLOR_SELECTED_NODE = Color.GREEN;
    private static final Color COLOR_SELECTED_NODE_LAST = Color.YELLOW;

    private static final Color COLOR_CONNECTION_EXTERNAL_INBOUND = Color.decode("#FFFF00");
    private static final Color COLOR_CONNECTION_INTERNAL = Color.decode("#FF00FF");
    private static final Color COLOR_CONNECTION_EXTERNAL_OUTBOUND = Color.decode("#00FFFF");

    /**
     * The {@link Graph} to edit.
     */
    public final Graph graph;

    /**
     * The panel into which the {@link Graph} will be painted.
     */
    private final GraphViewPanel paintArea;

    /**
     * The currently selected nodes for group operations
     */
    private final List<Node> selectedNodes = new ArrayList<>();

    /**
     * Store copied nodes in this buffer.  Could be a user-space file instead.
     */
    private final Graph copiedGraph = new Graph();

    /**
     * Manages undo/redo in the editor.
     */
    private final UndoManager undoManager = UnifiedUndoManager.getInstance();

    /**
     * declared here so that it can be referenced by the RedoAction.
     */
    private final UndoAction undoAction = new UndoAction(undoManager);

    /**
     * declared here so that it can be referenced by the UndoAction.
     */
    private final RedoAction redoAction = new RedoAction(undoManager);

    private final UndoHandler undoHandler = new UndoHandler(undoManager, undoAction, redoAction);

    /**
     * The toolBar is where the user can switch between tools.
     */
    private final JToolBar toolBar = new JToolBar();

    /**
     * Status information at the bottom of the editor.
     */
    private final JLabel statusBar = new JLabel();

    /**
     * The popupBar appears when the user right clicks in the paintArea.  It contains all actions that affect one or
     * more {@link Node}s within the model.
     */
    private final JPopupMenu popupBar = new JPopupMenu();

    /**
     * The list of actions registered in the editor.  This list is used for calls to
     * {@link #updateActionEnableStatus()}.
     */
    private final List<AbstractAction> actions = new ArrayList<>();

    /**
     * The list of context-sensitive tools, only one of which can be active at any time.
     */
    private final List<ContextSensitiveTool> tools = new ArrayList<>();

    /**
     * The active tool from the list of tools.
     */
    private ContextSensitiveTool activeTool;

    /**
     * cursor position when the popup menu happened.
     */
    private final Point popupPoint = new Point();

    private final UpdateClock updateClock = new UpdateClock(1000/30);

    /**
     * If true, the graph will update automatically.
     */
    private boolean keepGoing = false;

    private final RecentFilesMenu recentFilesMenu = new RecentFilesMenu(Preferences.userNodeForPackage(GraphLoadAction.class),this);
    private final ThreadPoolScheduler threadPoolScheduler = new ThreadPoolScheduler();

    private final NodeFactoryPanel nodeFactoryPanel = new NodeFactoryPanel();

    private final EventListenerList listeners = new EventListenerList();

    public Donatello() {
        this(new Graph());
    }

    public Donatello(Graph graph) {
        super(new BorderLayout());
        this.graph = graph;

        paintArea = new GraphViewPanel(graph);
        paintArea.loadSettings();

        this.add(toolBar, BorderLayout.NORTH);
        this.add(paintArea, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.SOUTH);

        setupTools();
        setupPaintArea();
        attachMouse();
        setupToolBar();
        setupToolBar2();

        setSelectedNodes(null);
        setupClock();
        connectNodeFactory(nodeFactoryPanel);
    }

    public void connectNodeFactory(NodeFactoryPanel nfp) {
        nfp.addListener(e->{
            var p = this.getPopupPoint();
            if(p!=null) p = this.getPaintArea().transformScreenToWorldPoint(p);
            else p = this.getPaintArea().getCameraPosition();

            var add = new NodeAddAction("Add",this,nfp);
            add.commitAdd(e,p);
        });
    }

    private void setupClock() {
        updateClock.addListener(()->{
            if(!keepGoing) return;

            threadPoolScheduler.update();
            //graph.update();

            if(!threadPoolScheduler.isIdle()) return;

            // submit dirty nodes to the thread pool.
            for(Node n : graph.getNodes()) {
                if(n.isDirty() && !threadPoolScheduler.hasQueued(n)) {
                    logger.debug("idle submit {}", n.getName());
                    threadPoolScheduler.submit(n);
                }
            }
        });
    }

    public boolean getKeepGoing() {
        return keepGoing;
    }

    public void setKeepGoing(boolean keepGoing) {
        this.keepGoing = keepGoing;
    }

    /**
     * Sets up the editor as a {@link GraphViewListener} so that it can add editor-specific decorations to the
     * painted nodes.
     */
    private void setupPaintArea() {
        paintArea.addViewListener((g,e)->{
            highlightSelectedNodes(g);
            activeTool.paint(g);
        });
        paintArea.repaint();
    }

    /**
     * Paints Node boundaries in a highlighted color.
     * @param g the {@link Graphics} context
     */
    private void highlightSelectedNodes(Graphics g) {
        if(selectedNodes.isEmpty()) return;

        ArrayList<Connection> in = new ArrayList<>();
        ArrayList<Connection> out = new ArrayList<>();

        final int size = selectedNodes.size();
        for(int i=0;i<size;++i) {
            Node n = selectedNodes.get(i);

            if(i==0) g.setColor(COLOR_SELECTED_NODE_FIRST);
            else if(i==size-1) g.setColor(COLOR_SELECTED_NODE_LAST);
            else g.setColor(COLOR_SELECTED_NODE);

            paintArea.paintNodeBorder(g, n);

            for( Connection c : graph.getConnections() ) {
                if(c.getTo()==n) in.add(c);
                if(c.getFrom()==n) out.add(c);
            }
        }
        ArrayList<Connection> both = new ArrayList<>(in);
        both.retainAll(out);
        in.removeAll(both);
        out.removeAll(both);

        g.setColor(COLOR_CONNECTION_EXTERNAL_INBOUND);
        for( Connection c : in ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(COLOR_CONNECTION_INTERNAL);
        for( Connection c : both ) {
            paintArea.paintConnection(g,c);
        }
        g.setColor(COLOR_CONNECTION_EXTERNAL_OUTBOUND);
        for( Connection c : out ) {
            paintArea.paintConnection(g,c);
        }
    }

    public void setupMenuBar() {
        JFrame topFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
        if(topFrame==null) return;

        JMenuBar menuBar = new JMenuBar();
        topFrame.setJMenuBar(menuBar);

        menuBar.add(setupNodeMenu());
        menuBar.add(setupHelpMenu());
    }

    private void setupTools() {
        RectangleSelectTool rectangleSelectTool = new RectangleSelectTool(this);
        NodeMoveTool moveTool = new NodeMoveTool(this);
        ConnectionEditTool connectionEditTool = new ConnectionEditTool(this,"Add connection","Remove connection");
        tools.add(connectionEditTool);
        tools.add(moveTool);
        tools.add(rectangleSelectTool);

        swapTool(tools.getFirst());
    }

    private JMenu setupHelpMenu() {
        JMenu menu = new JMenu("Help");

        BrowseURLAction showLog = new BrowseURLAction("Open log file",FileHelper.convertToFileURL(FileHelper.getLogFile()));
        BrowseURLAction update = new BrowseURLAction("Check for updates","https://github.com/MarginallyClever/Donatello/releases");
        BrowseURLAction problem = new BrowseURLAction("I have a problem...","https://github.com/MarginallyClever/Donatello/issues");
        BrowseURLAction drink = new BrowseURLAction("Buy me a drink","https://www.paypal.com/donate/?hosted_button_id=Y3VZ66ZFNUWJE");
        BrowseURLAction community = new BrowseURLAction("Join the community","https://discord.gg/TbNHKz6rpy");
        BrowseURLAction idea = new BrowseURLAction("I have an idea!","https://github.com/MarginallyClever/Donatello/issues");

        community.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-discord-16.png"))));
        drink.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-cocktail-16.png"))));
        update.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-newspaper-16.png"))));
        problem.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-telephone-16.png"))));
        idea.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-light-bulb-16.png"))));
        problem.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-bug-16.png"))));

        ActionRegistry.register("help > showLog",showLog);
        ActionRegistry.register("help > update",update);
        ActionRegistry.register("help > problem",problem);
        ActionRegistry.register("help > drink",drink);
        ActionRegistry.register("help > community",community);
        ActionRegistry.register("help > idea",idea);

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

    /**
     * @param key a {@link GraphViewSettings} key
     * @param newValue the new value for that setting
     */
    private void changeViewSetting(int key,boolean newValue) {
        paintArea.getSettings().set(key,newValue);
        paintArea.repaint();
    }

    private void setupToolBar() {
        addPreferencesButton(toolBar);
        toolBar.addSeparator();

        setupGraphMenu(toolBar);
        toolBar.addSeparator();

        ButtonGroup clockGroup = new ButtonGroup();

        GraphUpdateAction graphUpdateAction = new GraphUpdateAction("Step",this);
        graphUpdateAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
        graphUpdateAction.putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-end-16.png"))));
        JToggleButton graphUpdateButton = new JToggleButton(graphUpdateAction);

        PlayAction playAction = new PlayAction("Play",this, graphUpdateAction);
        JToggleButton playButton = new JToggleButton(playAction);
        playAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));

        AbstractAction resetAction = new AbstractAction("Reset") {
            @Override
            public void actionPerformed(ActionEvent e) {
                getGraph().reset();
            }
        };
        JToggleButton resetButton = new JToggleButton(resetAction);
        resetAction.putValue(Action.SHORT_DESCRIPTION,"Reset the graph to its initial state.");
        resetAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        resetAction.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-rewind-16.png"))));

        clockGroup.add(graphUpdateButton);
        clockGroup.add(playButton);
        clockGroup.add(resetButton);

        toolBar.add(graphUpdateButton);
        toolBar.add(playButton);
        toolBar.add(resetButton);

        ActionRegistry.register("toolbar > graphUpdate", graphUpdateAction);
        ActionRegistry.register("toolbar > play", playAction);
        ActionRegistry.register("toolbar > reset", resetAction);
    }

    private void addPreferencesButton(JToolBar toolBar) {
        var openSettingsAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSettingsPanel(e);
            }
        };
        openSettingsAction.putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-settings-16.png"))));
        openSettingsAction.putValue(Action.SHORT_DESCRIPTION,"Preferences");
        toolBar.add(new JButton(openSettingsAction));
    }

    private void createSettingsPanel(ActionEvent e) {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setPreferredSize(new Dimension(400,400));
        ActionSettingsPanel actionSettingsPanel = new ActionSettingsPanel();

        GraphViewSettingsPanel gvSettingsPanel = new GraphViewSettingsPanel(paintArea.getSettings());

        tabbedPane.add("Actions", new JScrollPane(actionSettingsPanel));
        tabbedPane.add("View", new JScrollPane(gvSettingsPanel));

        // put in a modal dialog
        var root = SwingUtilities.getWindowAncestor((Component)e.getSource());
        int result = JOptionPane.showConfirmDialog(root, tabbedPane, "Preferences", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result==JOptionPane.OK_OPTION)
            paintArea.saveSettings();
        else paintArea.loadSettings();
        paintArea.repaint();
    }

    private void setupToolBar2() {
        NodeCopyAction nodeCopyAction = new NodeCopyAction("Copy",this);
        NodePasteAction nodePasteAction = new NodePasteAction("Paste",this);
        NodeDeleteAction nodeDeleteAction = new NodeDeleteAction("Delete",this);
        NodeCutAction nodeCutAction = new NodeCutAction("Cut", nodeDeleteAction, nodeCopyAction);
        NodeAddAction nodeAddAction = new NodeAddAction("Add",this,nodeFactoryPanel);
        NodeEditAction editNodesAction = new NodeEditAction("Edit",this);
        ForciblyUpdateNodesAction forciblyUpdateNodesAction = new ForciblyUpdateNodesAction("Force update",this);
        GraphFoldAction graphFoldAction = new GraphFoldAction("Fold",this, nodeCutAction);
        GraphUnfoldAction graphUnfoldAction = new GraphUnfoldAction("Unfold",this);
        NodeIsolateAction nodeIsolateAction = new NodeIsolateAction("Isolate",this);
        SelectAllAction selectAllAction = new SelectAllAction("Select all",this);
        SelectionGrowAction selectionGrowAction = new SelectionGrowAction("Grow selection",this);
        SelectionShrinkAction selectionShrinkAction = new SelectionShrinkAction("Shrink selection",this);
        SelectionInvertAction selectionInvertAction = new SelectionInvertAction("Invert selection",this);
        SelectShortestPathAction selectShortestPathAction = new SelectShortestPathAction("Select shortest path",this);
        ZoomToFitSelectedAction zoomToFitSelectedAction = new ZoomToFitSelectedAction("Pan and zoom to selected",this);
        GraphStraightenAction graphStraightenAction = new GraphStraightenAction("Straighten",this);
        GraphOrganizeAction graphOrganizeAction = new GraphOrganizeAction("Organize",this);

        addActionItem(graphStraightenAction,"Straighten","/com/marginallyclever/donatello/icons8-square-ruler-16.png","");
        addActionItem(selectShortestPathAction,"Select shortest path","/com/marginallyclever/donatello/icons8-path-16.png","");
        addActionItem(forciblyUpdateNodesAction,"Force update","/com/marginallyclever/donatello/icons8-update-16.png","");
        addActionItem(zoomToFitSelectedAction,"Fit on screen","/com/marginallyclever/donatello/icons8-zoom-to-fit-16.png","control F");
        addActionItem(nodeCopyAction,"Copy","/com/marginallyclever/donatello/icons8-copy-16.png","control C");
        addActionItem(nodePasteAction,"Paste","/com/marginallyclever/donatello/icons8-paste-16.png","control V");
        addActionItem(nodeDeleteAction,"Delete","/com/marginallyclever/donatello/icons8-delete-16.png","delete");
        addActionItem(nodeCutAction,"Cut","/com/marginallyclever/donatello/icons8-cut-16.png","control X");
        addActionItem(nodeAddAction,"Add","/com/marginallyclever/donatello/icons8-add-16.png","control +");
        addActionItem(editNodesAction,"Edit","/com/marginallyclever/donatello/icons8-edit-16.png","control E");
        addActionItem(graphFoldAction,"Fold","/com/marginallyclever/donatello/icons8-edit-16.png","control {");
        addActionItem(graphUnfoldAction,"Unfold","/com/marginallyclever/donatello/icons8-edit-16.png","control }");
        addActionItem(nodeIsolateAction,"Isolate","/com/marginallyclever/donatello/icons8-separate-16.png","control I");
        addActionItem(selectAllAction,"Select all","/com/marginallyclever/donatello/icons8-select-all-16.png","control A");
        addActionItem(selectionGrowAction,"Grow selection","/com/marginallyclever/donatello/icons8-expand-16.png","control +");
        addActionItem(selectionShrinkAction,"Shrink selection","/com/marginallyclever/donatello/icons8-collapse-16.png","control -");
        addActionItem(selectionInvertAction,"Invert selection","/com/marginallyclever/donatello/icons8-invert-16.png","control shift I");
        addActionItem(graphOrganizeAction,"Organize","/com/marginallyclever/donatello/icons8-sorting-16.png","control O");

        toolBar.addSeparator();
        toolBar.add(selectAllAction);
        toolBar.add(selectionGrowAction);
        toolBar.add(selectionShrinkAction);
        toolBar.add(selectionInvertAction);
        toolBar.add(selectShortestPathAction);
        toolBar.add(zoomToFitSelectedAction);
        toolBar.addSeparator();
        toolBar.add(graphStraightenAction);
        toolBar.add(graphOrganizeAction);

        popupBar.add(nodeAddAction);
        popupBar.add(editNodesAction);
        popupBar.add(forciblyUpdateNodesAction);
        popupBar.addSeparator();
        popupBar.add(graphFoldAction);
        popupBar.add(graphUnfoldAction);
        popupBar.add(nodeIsolateAction);
        popupBar.addSeparator();
        popupBar.add(nodeCopyAction);
        popupBar.add(nodeCutAction);
        popupBar.add(nodePasteAction);
        popupBar.addSeparator();
        popupBar.add(nodeDeleteAction);
    }

    private void addActionItem(AbstractAction action,String name,String iconFile,String accelerator) {
        action.putValue(Action.SHORT_DESCRIPTION,name);
        action.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource(iconFile))));
        action.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(accelerator));
        ActionRegistry.register("action > "+name,action);
        actions.add(action);
    }

    /**
     * Populates the toolBar with actions and assigns accelerator keys.
     */
    private void setupGraphMenu(JToolBar toolBar) {
        GraphNewAction graphNewAction = new GraphNewAction("New",this);
        graphNewAction.putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-new-16.png"))));
        actions.add(graphNewAction);
        toolBar.add(graphNewAction);
        graphNewAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        ActionRegistry.register("toolbar > graphNew", graphNewAction);

        GraphLoadAction graphLoadAction = new GraphLoadAction(recentFilesMenu,null,this);
        graphLoadAction.putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-load-16.png"))));
        actions.add(graphLoadAction);
        toolBar.add(graphLoadAction);
        graphLoadAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        ActionRegistry.register("toolbar > graphLoad", graphLoadAction);

        //toolBar.add(recentFilesMenu);

        GraphSaveAsAction graphSaveAsAction = new GraphSaveAsAction(recentFilesMenu,"Save",this);
        graphSaveAsAction.putValue(Action.SMALL_ICON,new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-save-16.png"))));
        actions.add(graphSaveAsAction);
        toolBar.add(graphSaveAsAction);
        graphSaveAsAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        ActionRegistry.register("toolbar > graphSaveAs", graphSaveAsAction);
    }

    /**
     * Populates the popupBar and the node menu with actions and assigns accelerator keys.
     */
    private JMenu setupNodeMenu() {
        JMenu menu = new JMenu("Node");

        menu.add(undoAction);
        menu.add(redoAction);

        undoAction.setActionRedo(redoAction);
        redoAction.setActionUndo(undoAction);

        undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        undoAction.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-undo-16.png"))));
        redoAction.putValue(Action.SMALL_ICON, new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/marginallyclever/donatello/icons8-redo-16.png"))));

        actions.add(undoAction);
        actions.add(redoAction);

        return menu;
    }

    public void swapTool(ContextSensitiveTool tool) {
        if(tool==activeTool) return;
        deactivateCurrentTool();
        activeTool = tool;
        activateCurrentTool();
    }

    private void deactivateCurrentTool() {
        if(activeTool == null) return;
        activeTool.detachKeyboardAdapter();
        activeTool.detachMouseAdapter();
    }

    private void activateCurrentTool() {
        if(activeTool == null) return;
        activeTool.attachKeyboardAdapter();
        activeTool.attachMouseAdapter();
    }

    /**
     * Respond to popup menu requests
     */
    private void attachMouse() {
        paintArea.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                var p = paintArea.transformScreenToWorldPoint(e.getPoint());
                setStatusBar(p);
                setToolTipText(p);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = paintArea.transformScreenToWorldPoint(e.getPoint());
                checkToolContext(p);
                setStatusBar(p);
                setToolTipText(p);
            }
        });
        paintArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    popupPoint.setLocation(e.getPoint());
                    popupBar.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        });
    }

    private void setStatusBar(Point transformMousePoint) {
        StringBuilder sb = new StringBuilder();
        var camPos = getPaintArea().getCameraPosition();
        sb.append(activeTool.getName())
                .append(" (").append(transformMousePoint.x).append(",").append(transformMousePoint.y).append(")")
                .append(" [").append(camPos.x).append(",").append(camPos.y).append("]");
        statusBar.setText(sb.toString());
    }

    private void setToolTipText(Point transformMousePoint) {
        // tool tip text is the name of the type of node under the cursor, if any
        Node node = getGraph().getNodeAt(transformMousePoint);
        paintArea.setToolTipText(node == null ? null : node.getName());
    }

    private void checkToolContext(Point point) {
        if(activeTool != null && !activeTool.isActive()) {
            for (ContextSensitiveTool tool : tools) {
                if (tool.isCorrectContext(point)) {
                    swapTool(tool);
                    return;
                }
            }
        }
    }

    /**
     * Move all selected nodes some relative cartesian amount.
     * @param dx the x-axis amount.
     * @param dy the y-axis amount.
     */
    public void moveSelectedNodes(int dx, int dy) {
        for(Node n : selectedNodes) {
            n.moveRelative(dx,dy);
        }
    }

    /**
     * Sets the list of selected nodes to one item.
     * @param n the new selected node.
     */
    public void setSelectedNode(Node n) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(n!=null) nodes.add(n);
        setSelectedNodes(nodes);
    }

    /**
     * Sets the list of selected nodes.
     * @param list the new list of selected nodes.
     */
    public void setSelectedNodes(List<Node> list) {
        selectedNodes.clear();
        if (list != null) selectedNodes.addAll(list);
        updateActionEnableStatus();
        paintArea.repaint();
        fireSelectionChange();
    }

    /**
     * Returns all selected nodes.  To change the selected nodes do not edit this list.  Instead,
     * call {@link Donatello#setSelectedNodes(List)} or {@link #setSelectedNode(Node)}.
     * @return all selected nodes.
     */
    public List<Node> getSelectedNodes() {
        return selectedNodes;
    }

    /**
     * All Actions have the tools to check for themselves if they are active.
     */
    private void updateActionEnableStatus() {
        for(AbstractAction a : actions) {
            if(a instanceof EditorAction) {
                ((EditorAction)a).updateEnableStatus();
            }
        }
    }

    /**
     * Returns the graph being edited.
     * @return the graph being edited.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns the cursor location when the popup began.
     * @return the cursor location when the popup began.
     */
    public Point getPopupPoint() {
        return popupPoint;
    }

    /**
     * Store a copy of some part of the graph for later.
     * @param graph the graph to set as copied.
     */
    public void setCopiedGraph(Graph graph) {
        copiedGraph.clear();
        copiedGraph.add(graph);
    }

    /**
     * Returns the stored graph marked as copied.
     * @return the stored graph marked as copied.
     */
    public Graph getCopiedGraph() {
        return copiedGraph;
    }

    /**
     * Clears the internal graph and resets everything.
     */
    public void clear() {
        graph.clear();
        activeTool.restart();
        setSelectedNode(null);
        repaint();
    }

    public GraphViewPanel getPaintArea() {
        return paintArea;
    }

    public static void setLookAndFeel() {
        FlatLaf.registerCustomDefaultsSource( Donatello.class.getPackageName() );
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.debug("System look and feel could not be set.",e);
        }
    }

    public void addEdit(UndoableEdit undoableEdit) {
        undoHandler.undoableEditHappened(new UndoableEditEvent(this,undoableEdit));
    }

    public void lockClock() {
        updateClock.lock();
    }

    public void unlockClock() {
        updateClock.unlock();
    }


    /**
     * Main entry point.  Good for independent test.
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Exception {
        FileHelper.createDirectoryIfMissing(FileHelper.getExtensionPath());
        ServiceLoaderHelper.addAllPathFiles(FileHelper.getExtensionPath());

        NodeFactory.loadRegistries();
        DAO4JSONFactory.loadRegistries();

        Donatello.setLookAndFeel();

        PropertiesHelper.showProperties();
        PropertiesHelper.listAllNodes();
        PropertiesHelper.listAllDAO();

        Donatello panel = new Donatello();

        JFrame frame = new JFrame("Donatello");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,800));
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        panel.setupMenuBar();
        frame.setVisible(true);
    }

    public void submit(Node node) {
        threadPoolScheduler.submit(node);
    }

    public NodeFactoryPanel getNodeFactoryPanel() {
        return nodeFactoryPanel;
    }

    private void fireSelectionChange() {
        for(var recipient : listeners.getListeners(DonatelloSelectionListener.class)) {
            recipient.selectionChanged(this);
        }
    }

    public void addSelectionListener(DonatelloSelectionListener listener) {
        listeners.add(DonatelloSelectionListener.class, listener);
    }

    public void removeSelectionListener(DonatelloSelectionListener listener) {
        listeners.remove(DonatelloSelectionListener.class, listener);
    }
}

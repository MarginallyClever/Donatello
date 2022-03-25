/**
 * donatello contains Swing-based {@link com.marginallyclever.nodegraphcore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyclever.nodegraphcore.NodeGraph}s.
 */
module com.marginallyclever.donatello {
    requires com.marginallyclever.nodegraphcore;

    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires ch.qos.logback.core;

    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
        com.marginallyclever.donatello.SwingRegistry;

    uses com.marginallyclever.nodegraphcore.DAORegistry;
    provides com.marginallyclever.nodegraphcore.DAORegistry with
        com.marginallyclever.donatello.SwingRegistry;
}
import com.marginallyclever.donatello.DonatelloRegistry;

/**
 * donatello contains Swing-based {@link com.marginallyclever.nodegraphcore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyclever.nodegraphcore.NodeGraph}s.
 */
module com.marginallyclever.donatello {
    requires com.marginallyclever.nodegraphcore;

    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires logback.core;

    exports com.marginallyclever.donatello to logback.core;
    exports com.marginallyclever.donatello.nodes to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.color to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.images to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.images.blend to com.marginallyclever.nodegraphcore;

    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
            com.marginallyclever.donatello.DonatelloRegistry;

    uses com.marginallyclever.nodegraphcore.DAORegistry;
    provides com.marginallyclever.nodegraphcore.DAORegistry with
            com.marginallyclever.donatello.DonatelloRegistry;
}
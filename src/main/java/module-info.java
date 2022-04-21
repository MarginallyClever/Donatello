import com.marginallyclever.donatello.DonatelloRegistry;

/**
 * donatello contains Swing-based {@link com.marginallyclever.version2.Node}s and all Swing-based tools for
 * editing {@link com.marginallyclever.version2.Graph}s.
 */
module com.marginallyclever.donatello {
    requires java.desktop;
    requires org.slf4j;
    requires logback.core;
    requires org.reflections;

    exports com.marginallyclever.donatello to logback.core;

    // must export these packages for nodegraphcore to access the classes within.
    exports com.marginallyclever.donatello.nodes to com.marginallyclever.version2;
    exports com.marginallyclever.donatello.nodes.images.blend.color to com.marginallyclever.version2;
    exports com.marginallyclever.donatello.nodes.images to com.marginallyclever.version2;
    exports com.marginallyclever.donatello.search to logback.core;
    exports com.marginallyclever.donatello.bezier to logback.core;

    uses com.marginallyclever.version2.NodeRegistry;
    provides com.marginallyclever.version2.NodeRegistry with DonatelloRegistry, com.marginallyclever.version2.DefaultRegistry;

}
import com.marginallyclever.donatello.DonatelloRegistry;

/**
 * donatello contains Swing-based {@link com.marginallyclever.nodegraphcore.Node}s and all Swing-based tools for
 * editing {@link com.marginallyclever.nodegraphcore.Graph}s.
 */
module com.marginallyclever.donatello {
    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires ch.qos.logback.core;
    requires com.marginallyclever.nodegraphcore;
    requires io.github.classgraph;
    requires webcam.capture;
    requires org.reflections;
    requires com.formdev.flatlaf;
    requires java.prefs;

    exports com.marginallyclever.donatello;

    // must export these packages for nodegraphcore to access the classes within.
    exports com.marginallyclever.donatello.nodes to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.color to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.images to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.nodes.images.blend to com.marginallyclever.nodegraphcore;
    exports com.marginallyclever.donatello.search to ch.qos.logback.core;
    exports com.marginallyclever.donatello.bezier to ch.qos.logback.core;
    exports com.marginallyclever.donatello.graphview to ch.qos.logback.core;

    // A Java module that wants to implement a service interface from a service interface module must:
    // - Require the service interface module in its own module descriptor.
    // - Implement the service interface with a Java class.
    // - Declare the service interface implementation in its module descriptor.
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
            com.marginallyclever.donatello.DonatelloRegistry;

    provides com.marginallyclever.nodegraphcore.DAORegistry with
            com.marginallyclever.donatello.DonatelloRegistry;

    // In order to use the service, the client module must declare in its module descriptor that it uses the service.
    // http://tutorials.jenkov.com/java/modules.html
    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    uses com.marginallyclever.nodegraphcore.DAORegistry;
}
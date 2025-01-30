module com.marginallyclever.donatello {
    requires java.desktop;
    requires org.json;
    requires org.slf4j;
    requires ch.qos.logback.core;
    requires com.marginallyclever.nodegraphcore;
    requires io.github.classgraph;
    requires org.reflections;
    requires com.formdev.flatlaf;
    requires java.prefs;

    exports com.marginallyclever.donatello;
    exports com.marginallyclever.donatello.graphview;

    // must export these packages for nodegraphcore to access the classes within.
    exports com.marginallyclever.donatello.nodes;
    exports com.marginallyclever.donatello.nodes.color;
    exports com.marginallyclever.donatello.nodes.images;
    exports com.marginallyclever.donatello.nodes.images.blend;
    exports com.marginallyclever.donatello.search;
    exports com.marginallyclever.donatello.bezier;
    exports com.marginallyclever.donatello.actions.undoable;

    // A Java module that wants to implement a service interface from a service interface module must:
    // - Require the service interface module in its own module descriptor.
    // - Implement the service interface with a Java class.
    // - Declare the service interface implementation in its module descriptor.
    // In order to use the service, the client module must declare in its module descriptor that it uses the service.
    // http://tutorials.jenkov.com/java/modules.html
    uses com.marginallyclever.nodegraphcore.NodeRegistry;
    provides com.marginallyclever.nodegraphcore.NodeRegistry with
            com.marginallyclever.donatello.DonatelloRegistry;

    uses com.marginallyclever.nodegraphcore.DAORegistry;
    provides com.marginallyclever.nodegraphcore.DAORegistry with
            com.marginallyclever.donatello.DonatelloRegistry;
}
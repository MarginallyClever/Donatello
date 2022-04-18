package com.marginallyclever.version2;

import java.io.*;

/**
 * Parses and builds a {@link Graph} from an {@link InputStream}.
 */
public class GraphReader {
    /**
     *
     * @param inputStream the source
     * @return the {@link Graph} in the {@link InputStream}.
     * @throws IOException if the reading the stream failed.
     * @throws ClassNotFoundException if one of the subclasses could not be instantiated.
     */
    public Graph parse(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        Graph graph = (Graph)in.readObject();

        return graph;
    }
}

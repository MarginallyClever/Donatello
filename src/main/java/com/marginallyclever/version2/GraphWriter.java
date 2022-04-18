package com.marginallyclever.version2;

import java.io.*;

/**
 * Writes a {@link Graph} to an {@link OutputStream}.
 */
public class GraphWriter {
    /**
     *
     * @param subject the {@link Graph} to write.
     * @param outputStream the target for writing.
     * @throws IOException if a write failure occurs.
     */
    public void write(Graph subject, OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(subject);
        out.flush();
    }
}

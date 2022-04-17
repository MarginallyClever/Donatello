package com.marginallyclever.version2;

import java.io.*;

public class GraphWriter {
    public void write(Graph subject, OutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.append(subject.toString());
    }
}

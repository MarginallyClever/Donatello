package com.marginallyclever.donatello;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenient methods for searching files in a directory and finding the user's donatello/extensions path.
 * @author Dan Royer
 * @since 2022-03-??
 */
public class FileHelper {
    /**
     * Attempts to create a directory if it does not exist.
     * @param dir the desired path
     */
    public static void createDirectoryIfMissing(String dir) {
        File directory = new File(dir);
        if(!directory.exists()) directory.mkdirs();
    }

    /**
     * Returns the extension path ~/Donatello/extensions/
     * @return the extension path ~/Donatello/extensions/
     */
    public static String getExtensionPath() {
        String sep = FileSystems.getDefault().getSeparator();
        return getWorkPath() + "extensions" + sep;
    }

    /**
     * Returns the path ~/Donatello/
     * @return the path ~/Donatello/
     */
    public static String getWorkPath() {
        String sep = FileSystems.getDefault().getSeparator();
        return System.getProperty("user.home") + sep + "Donatello" + sep;
    }

    /**
     * Returns the path ~/Donatello/Donatello.log
     * @return the path ~/Donatello/Donatello.log
     */
    public static String getLogFile() {
        String sep = FileSystems.getDefault().getSeparator();
        return getWorkPath() + sep + "Donatello.log";
    }

    /**
     * Convert from a filename to a file URL.
     */
    public static String convertToFileURL( String filename ) {
        // On JDK 1.2 and later, simplify this to:
        // "path = file.toURL().toString()".
        String path = new File( filename ).getAbsolutePath();
        if ( File.separatorChar != '/' ) {
            path = path.replace ( File.separatorChar, '/' );
        }
        if ( !path.startsWith ( "/" ) ) {
            path = "/" + path;
        }
        return "file:" + path;
    }
}

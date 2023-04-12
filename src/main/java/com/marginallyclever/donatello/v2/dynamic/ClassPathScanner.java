package com.marginallyclever.donatello.v2.dynamic;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.HashSet;
import java.util.Set;

/**
 * Scans the classpath for all classes and packages.
 */
public class ClassPathScanner {
    public static Set<String> getVisiblePackagesAndClasses() {
        Set<String> classNames = new HashSet<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableSystemJarsAndModules()
                .enableAllInfo()
                .acceptPackages("java", "javax", "com.marginallyclever") //, "com", "org", "jdk", "sun"
                .scan()) {

            classNames.addAll(scanResult.getAllClasses().getNames());
        }

        return classNames;
    }
}

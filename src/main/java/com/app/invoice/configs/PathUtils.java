package com.app.invoice.configs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    public static Path validateAndCreatePath(String baseDir, String... subpaths) throws IOException {
        Path basePath = Paths.get(baseDir).normalize().toAbsolutePath();
        Path resolvedPath = basePath;
        
        for (String subpath : subpaths) {
            resolvedPath = resolvedPath.resolve(subpath).normalize();
        }
        
        if (!resolvedPath.startsWith(basePath)) {
            throw new IOException("Invalid path traversal detected");
        }
        
        return resolvedPath;
    }
}
package com.app.invoice.configs.tenants;

public class DatabaseContext {
    private static final ThreadLocal<String> currentDatabase = new ThreadLocal<>();

    public static void setCurrentDatabase(String database) {
        currentDatabase.set(database);
    }

    public static String getCurrentDatabase() {
        return currentDatabase.get();
    }

    public static void clear() {
        currentDatabase.remove();
    }
}

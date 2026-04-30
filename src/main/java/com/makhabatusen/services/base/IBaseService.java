package com.makhabatusen.services.base;

public interface IBaseService {
    default void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    default void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}

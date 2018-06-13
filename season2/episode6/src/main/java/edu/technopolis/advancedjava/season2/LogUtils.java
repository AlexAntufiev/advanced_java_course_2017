package edu.technopolis.advancedjava.season2;

import java.io.IOException;

/**
 * Логирование исключений.
 */
public class LogUtils {
    public static void logException(String s, IOException e) {
        System.err.println(s);
        e.printStackTrace();
    }

    public static void log(String s) {
        System.out.println(s);
    }
}

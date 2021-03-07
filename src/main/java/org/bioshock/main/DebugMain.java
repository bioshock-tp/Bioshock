package org.bioshock.main;

public class DebugMain {
    public static void main(String[] args) {
        App.logger.info("App started in debug mode");
        Debug.main(args);
    }
}

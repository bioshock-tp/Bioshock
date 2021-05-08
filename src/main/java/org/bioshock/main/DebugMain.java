package org.bioshock.main;

/**
 * 
 * Class which calls Debug.main
 *
 */
public class DebugMain {
    public static void main(String[] args) {
        App.logger.info("App started in debug mode");
        Debug.main(args);
    }
}

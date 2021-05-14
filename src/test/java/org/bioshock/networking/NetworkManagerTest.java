package org.bioshock.networking;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class NetworkManagerTest {
    @Test
    public void highScoreTest() {
        Assertions.assertTrue(
            NetworkManager.requestHighScores(),
            "Request failed"
        );

        Assertions.assertFalse(
            NetworkManager.getHighScores().isEmpty(),
            "Received JSON was empty"
        );
    }
}

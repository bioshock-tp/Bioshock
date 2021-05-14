package org.bioshock.networking;

import org.bioshock.networking.Message.ClientInput;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;

public class MessageTest {
    private static final int PLAYER_NUMBER = 0;
    private static final String UUID = "foo";
    private static final String NAME = "bar";

    private static final int X = 10;
    private static final int Y = 20;

    private static final int AI_X = 15;
    private static final int AI_Y = 25;
    private static final int[][] AI_COORDINATES = new int[][] {{AI_X, AI_Y}};

    private static final String MESSAGE = "Hello World";

    private static final ClientInput INPUT = new ClientInput(
        X, Y, AI_COORDINATES, MESSAGE
    );

    private static final boolean DEAD = true;

    private static String serialised;


    @Test
    @Order(1)
    public void serialiseTest() {
        serialised = Message.serialise(
            new Message(PLAYER_NUMBER, UUID, NAME, INPUT, DEAD)
        );

        Assertions.assertNotNull(serialised, "Serialise returned null");

        Assertions.assertFalse(
            serialised.isEmpty(),
            "Serialised message was empty"
        );
    }


    @Test
    @Order(2)
    public void deserialiseTest() {
        serialiseTest();
        Assertions.assertNotNull(serialised, "Serialisation failed");

        Message deserialised = Message.deserialise(serialised);

        Assertions.assertNotNull(deserialised, "Deserialise returned null");

        Assertions.assertAll(
            () -> Assertions.assertEquals(
                PLAYER_NUMBER,
                deserialised.playerNumber,
                "Player number deserialised incorrectly"
            ),
            () -> Assertions.assertEquals(
                UUID,
                deserialised.uuid,
                "UUID deserialised incorrectly"
            ),
            () -> Assertions.assertEquals(
                NAME,
                deserialised.name,
                "Name deserialised incorrectly"
            ),
            () -> compareInput(deserialised.input),
            () -> Assertions.assertEquals(
                DEAD,
                deserialised.dead,
                "Dead deserialised incorrectly"
            )
        );
    }


    private void compareInput(ClientInput input) {
        Assertions.assertAll(
            () -> Assertions.assertEquals(
                X,
                input.x,
                "x deserialised incorrectly"
            ),
            () -> Assertions.assertEquals(
                Y,
                input.y,
                "y deserialised incorrectly"
            ),
            () -> Assertions.assertArrayEquals(
                AI_COORDINATES,
                input.aiCoords,
                "AI coordinates deserialised incorrectly"
            )
        );
    }
}

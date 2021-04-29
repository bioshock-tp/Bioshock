package org.bioshock.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import org.bioshock.main.App;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The number of the player within the lobby */
    int playerNumber;

    /** The unique ID of the player */
    String uuid;

    /** The player's chosen name */
    String name;

    /** The current state of the player */
    ClientInput input;

    /** True if the player is dead */
    boolean dead;

    private Message() {}

    /**
     * Constructs a new message to send to web socket server
     * @param playerNumber Should be -1 outside of lobby, otherwise should be
     * position of player in join queue
     * @param uuid Unique ID of player sending message
     * @param name Name of player
     * @param input A ClientInput object containing states of player and AI
     * @param dead True if sending player is dead
     */
    Message(
        int playerNumber,
        String uuid,
        String name,
        ClientInput input,
        boolean dead
    ) {
        this.playerNumber = playerNumber;
        this.name = name;
        this.uuid = uuid;
        this.input = input;
        this.dead = dead;
    }


    static class ClientInput implements Serializable {
        private static final long serialVersionUID = 1L;

        /** x coordinate of player */
        final int x;

        /** y coordinate of player */
        final int y;

        /** Coordinates of seeker(s) */
        final int[][] aiCoords;

        /** Chat message */
        final String message;

        /**
         * Information to send in message containing player position
         * @param x coordinate of player
         * @param y coordinate of player
         * @param aiCoords coordinates of seekers}
         * @param message Chat message
         */
        ClientInput(int x, int y, int[][] aiCoords, String message) {
            this.x = x;
            this.y = y;

            this.aiCoords = aiCoords;

            this.message = message;
        }

        @Override
        public String toString() {
            return String.format(
                "ClientInput{x=%d, y=%d, aiCoordinates=%s, message=%s}",
                x, y, aiCoords, message
            );
        }
    }


    /**
     * Returns a message to send when a player joins a lobby
     * @param playerNumber The players number within the lobby
     * @param uuid of the player
     * @param name of the player
     * @return A message used within the lobby of a networked game
     */
    static Message inLobby(int playerNumber, String uuid, String name) {
        return new Message(playerNumber, uuid, name, null, false);
    }


    /**
     * Returns a message to send every game tick to the server containing
     * information of the associated player
     * @param uuid of the player
     * @param name of the player
     * @param input A {@link ClientInput} from the player
     * @param dead true if the player is dead
     * @return
     */
    static Message sendInputState(
        String uuid,
        String name,
        ClientInput input,
        boolean dead
    ) {
        return new Message(-1, uuid, name, input, dead);
    }


    /**
     * Serialises a message object
     * @param message to serialise
     * @return The serialised object
     */
    public static String serialise(Message message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            App.logger.error("Error serialising {} ", message, e);

            if (message.equals(new Message())) {
                App.logger.fatal(
                    "Too much recursion serialising empty messages"
                );
                App.exit(-1);
            } else {
                return serialise(new Message());
            }
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    /**
     * De-serialises a message object
     * @param string to de-serialise
     * @return The {@link Message} object
     */
    public static Message deserialise(String string) {
        byte[] data = Base64.getDecoder().decode(string);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        Message message;
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            message = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            App.logger.error("Error deserialising {} ", string, e);
            return new Message();
        }

        return message;
    }


    @Override
    public String toString() {
        return String.format(
            "Message{Player Number %d, UUID %s, %s, %b}",
            playerNumber,
            uuid,
            input,
            dead
        );
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (dead ? 1231 : 1237);
        result = prime * result + ((input == null) ? 0 : input.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + playerNumber;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (dead != other.dead)
            return false;
        if (input == null) {
            if (other.input != null)
                return false;
        } else if (!input.equals(other.input))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (playerNumber != other.playerNumber)
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }
}

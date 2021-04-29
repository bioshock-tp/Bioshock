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

    int playerNumber;
    String uuid;
    String name;
    ClientInput input;
    boolean dead;

    private Message() {}

    /**
     * Constructs a new message to send to web socket server
     * @param playerNumber Should be -1 outside of lobby, otherwise should be
     * position of player in join queue
     * @param uuid Unique ID of player sending message
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

        final int x;
        final int y;

        final int[][] aiCoords;

        final String message;

        /**
         * Information to send in message containing player POS / DIR
         * @param x Coordinate X of player
         * @param y Coordinate Y of player
         * @param aiX Coordinate X of AI
         * @param aiY Coordinate Y of AI
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

    static Message inLobby(int playerNumber, String uuid, String name) {
        return new Message(playerNumber, uuid, name, null, false);
    }

    static Message sendInputState(
        String uuid,
        String name,
        ClientInput input,
        boolean dead
    ) {
        return new Message(-1, uuid, name, input, dead);
    }

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

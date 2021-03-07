package org.bioshock.engine.networking;

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
    String UUID;
    ClientInput input;

    Message() {}

    /**
     * Constructs a new message to send to web socket server
     * @param playerNumber Should be -1 outside of lobby, otherwise should be
     *     position of player in join queue
     * @param UUID Unique ID of player sending message
     * @param input A ClientInput object containing states of player and AI
     */
    Message(int playerNumber, String UUID, ClientInput input) {
        this.playerNumber = playerNumber;
        this.UUID = UUID;
        this.input = input;
    }

    static class ClientInput implements Serializable {
        private static final long serialVersionUID = 1L;

        final int x;
        final int y;

        final double aiX;
        final double aiY;

        /**
         * Information to send in message containing player POS/DIR
         * @param x Coordinate X of player
         * @param y Coordinate Y of player
         * @param aiX Coordinate X of AI
         * @param aiY Coordinate Y of AI
         */
        ClientInput(int x, int y, double aiX, double aiY) {
            this.x = x;
            this.y = y;

            this.aiX = aiX;
            this.aiY = aiY;
        }

        @Override
        public String toString() {
            return String.format("ClientInput{x=%d, y=%d, aiX=%f, aiY=%f}", x, y, aiX, aiY);
        }

        /**
         * Automatically generated
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(aiX);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(aiY);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        /**
         * Automatically generated, checks if fields are same, if not
         * identical reference
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            ClientInput other = (ClientInput) obj;
            if (Double.doubleToLongBits(aiX) != Double.doubleToLongBits(other.aiX))
                return false;
            if (Double.doubleToLongBits(aiY) != Double.doubleToLongBits(other.aiY))
                return false;
            if (x != other.x)
                return false;

            return y != other.y;
        }
    }

    static Message inLobby(int playerNumber, String UUID) {
        return new Message(playerNumber, UUID, null);
    }

    static Message sendInputState(String UUID, ClientInput input) {
        return new Message(-1, UUID, input);
    }

    public static String serialise(Message message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            App.logger.error(
                "Error serialising message {}:\n {}",
                message,
                e.getMessage()
            );

            if (message.equals(new Message())) {
                App.logger.fatal(
                    "Too much recursion serialising empty messages"
                );
                App.exit();
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
            App.logger.error(
                "Error deserialising string {}:\n{}",
                string,
                e.getMessage()
            );
            return new Message();
        }

        return message;
    }

    @Override
    public String toString() {
        return String.format(
            "Message{Player Number %d, UUID %s, %s}",
            playerNumber,
            UUID,
            input
        );
    }

    /**
     * Automatically generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((UUID == null) ? 0 : UUID.hashCode());
        result = prime * result + ((input == null) ? 0 : input.hashCode());
        result = prime * result + playerNumber;
        return result;
    }

    /**
     * Automatically generated, checks if fields are same, if not
     * identical reference
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (UUID == null) {
            if (other.UUID != null)
                return false;
        } else if (!UUID.equals(other.UUID))
            return false;
        if (input == null) {
            if (other.input != null)
                return false;
        } else if (!input.equals(other.input))
            return false;
        if (playerNumber != other.playerNumber)
            return false;
        return true;
    }
}

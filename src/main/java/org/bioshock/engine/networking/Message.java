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

    String uuid;
    ClientInput input;

    Message() {}

    Message(String uuid, ClientInput input) {
        this.uuid = uuid;
        this.input = input;
    }

    static class ClientInput implements Serializable {
        private static final long serialVersionUID = 1L;

        final int x;
        final int y;

        final double aiX;
        final double aiY;

        ClientInput(int x, int y, double aiX, double aiY) {
            this.x = x;
            this.y = y;

            this.aiX = aiX;
            this.aiY = aiY;
        }

        @Override
        public String toString() {
            return String.format("ClientInput{x=%d, y=%d, aiX=%d, aiY=%d}", x, y, aiX, aiY);
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

    static Message inLobby(String uuid) {
        return new Message(uuid, null);
    }

    static Message sendInputState(String uuid, ClientInput input) {
        return new Message(uuid, input);
    }

    public static String serialise(Message message) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(message);
        } catch (IOException e) {
            App.logger.error(
                "Error serializing message {}:\n {}",
                message,
                e.getMessage()
            );

            Message emptyMessage = new Message();
            if (message.equals(emptyMessage)) {
                App.logger.error("Tried to serialize empty message");
                App.exit();
            } else {
                return serialise(emptyMessage);
            }
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Message deserialise(String string) {
        byte[] data = Base64.getDecoder().decode(string);
        ByteArrayInputStream baos = new ByteArrayInputStream(data);

        Message message;
        try (ObjectInputStream ois = new ObjectInputStream(baos)) {
            message = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            App.logger.error(
                "Error deserializing string {}:\n{}",
                string,
                e.getMessage()
            );
            return new Message();
        }

        return message;
    }

    /**
     * Automatically generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((input == null) ? 0 : input.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
        if (input == null) {
            if (other.input != null)
                return false;
        } else if (!input.equals(other.input))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Message{UUID %s, %s}", uuid, input);
    }
}

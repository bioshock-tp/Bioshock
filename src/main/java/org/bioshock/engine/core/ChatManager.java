package org.bioshock.engine.core;

import org.bioshock.engine.input.InputManager;
import org.bioshock.entities.EntityManager;
import org.bioshock.main.App;
import org.bioshock.networking.NetworkManager;
import org.bioshock.scenes.GameScene;
import org.bioshock.scenes.MainGame;
import org.bioshock.scenes.SceneManager;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


/**
 * A class that is used in managing text chat on the client side
 */
public class ChatManager {
    /**
     * The regex for valid chat messages
     */
    private static final String CHAT_REGEX = "[a-zA-Z0-9 ]*";

    /**
     * Time in seconds for chat to stay on screen before starting to fade
     */
    private static final double CHAT_FADE_DELAY = 5;

    /**
     * Time in seconds for chat fade animation
     */
    private static final double CHAT_FADE_LENGTH = 3;

    /**
     * Chat container node
     */
    private static VBox chat;

    /**
     * Text input node
     */
    private static TextField textField = new TextField();

    /**
     * List of messages in chat
     */
    private static ObservableList<String> messageList =
        FXCollections.observableArrayList();

    /**
     * Node to display {@link #messageList}
     */
    private static ListView<String> messages = new ListView<>(messageList);

    /**
     * True if currently typing chat message
     */
    private static BooleanProperty acceptingInput =
        new SimpleBooleanProperty(false);

    /**
     * Name of local player
     */
    private static String hiderName;

    /**
     * Timeline used for fade
     */
    private static Timeline fadeTimeline = new Timeline();


	/**
	 * ChatManager is a static class
	 */
    private ChatManager() {}

    public static void initialise() {
        acceptingInput.bind(textField.disableProperty().not());

        /* Add users name to input field */
        hiderName = EntityManager.getCurrentPlayer().getName();
        textField.setText(hiderName + ": ");
        textField.positionCaret(textField.getLength());

        /* Allows user to only input valid text, and not remove their name */
        textField.setTextFormatter(new TextFormatter<>(value -> {
            if (
                value.isContentChange()
                && checkValid(value.getControlNewText())
            ) {
                return value;
            } else {
                return null;
            }
        }));

        MainGame mainGame = SceneManager.getMainGame();

        /* Reduce space between chat and scoreboard */
        Insets oldInsets = BorderPane.getMargin(mainGame.getScoreboard());
        Insets newInsets = new Insets(
            oldInsets.getTop(),
            oldInsets.getRight(),
            oldInsets.getBottom(),
            50
        );
        BorderPane.setMargin(mainGame.getScoreboard(), newInsets);

        /* Add chat to pane */
        double height = GameScene.getGameScreen().getHeight();
        chat = new VBox(messages, textField);
        messages.setPrefHeight(height / 2);
        BorderPane.setMargin(chat, new Insets(height / 2 - 100, 0, 100, 100));
        mainGame.getBorderPane().setLeft(chat);

        /* Add styling to chat */
        chat.getStyleClass().add("chat");
        messages.getStyleClass().add("chat");

        /* Default chat to inactive */
        setActive(false);

        messages.setSelectionModel(null);
        messages.setCellFactory(e -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                setMinWidth(e.getWidth());
                setMaxWidth(e.getWidth());
                setPrefWidth(e.getWidth());

                setWrapText(true);

                setText(item);
            }
        });

        /* Removes consumption of key events */
        messages.setDisable(true);

        textField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (
                e.getCode() == KeyCode.W
                || e.getCode() == KeyCode.A
                || e.getCode() == KeyCode.S
                || e.getCode() == KeyCode.D
            ) {
                chat.fireEvent(e.copyFor(chat, chat));
            }
        });

        textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if (
                e.getCode() == KeyCode.W
                || e.getCode() == KeyCode.A
                || e.getCode() == KeyCode.S
                || e.getCode() == KeyCode.D
            ) {
                chat.fireEvent(e.copyFor(chat, chat));
            }
        });

        /* Remove transparency due to being disabled */
        messages.setOpacity(1);

        /* Invisible by default */
        chat.setOpacity(0);

        /* Makes chat fade after a delay */
        fadeTimeline = new Timeline(
            new KeyFrame(
                Duration.seconds(CHAT_FADE_LENGTH),
                new KeyValue(chat.opacityProperty(), 0)
            )
        );
        fadeTimeline.setDelay(Duration.seconds(CHAT_FADE_DELAY));

        /* Make chat active when enter key is pressed */
        InputManager.onPress(KeyCode.ENTER, () -> {
            if (inChat()) {
                sendMessage();
            } else {
                setActive(true);
            }
        });

        /* Allows exiting chat by pressing escape */
        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) setActive(false);
        });
    }


    /**
     * Checks if given string is valid content for the {@link #textField}
     * @param string To compare to {@link #CHAT_REGEX}
     * @return True if valid string (matches {@link #CHAT_REGEX})
     */
    private static boolean checkValid(String string) {
        return string.matches(hiderName + ": " + CHAT_REGEX);
    }


    /**
     * Removes the message from the {@link #textField} and passes it to
     * {@link NetworkManager#addMessage(String)}
     */
    public static void sendMessage() {
        if (textField.getLength() > hiderName.length() + 2) {
            if (App.isNetworked()) {
                NetworkManager.addMessage(textField.getText());
            } else {
                incomingMessage(textField.getText());
            }
            textField.deleteText(
                hiderName.length() + 2,
                textField.getLength()
            );
        }
    }


    /**
     * Displays a message in chat
     * @param message The message to display in chat
     */
    public static void incomingMessage(String message) {
        messageList.add(message);
        messages.scrollTo(messageList.size());
        showChat(true);
        setActive(false);
    }


    /**
     * @param show True if chat should be shown
     */
    private static void showChat(boolean show) {
        if (show) {
            fadeTimeline.stop();
            chat.setOpacity(1);
        } else {
            textField.setOpacity(1);
            fadeTimeline.playFromStart();
        }
    }


    /**
     * @param active True if chat should be shown and accept input
     */
    public static void setActive(boolean active) {
        EntityManager.getCurrentPlayer().getMovement().pauseMovement(active);
        textField.setDisable(!active);
        textField.requestFocus();
        showChat(active);
    }


    /**
     * @return True if currently typing message
     */
    public static boolean inChat() {
        return acceptingInput.get();
    }
}

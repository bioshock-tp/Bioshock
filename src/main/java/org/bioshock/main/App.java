package org.bioshock.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bioshock.gui.MainController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class App extends Application{
	public static final Logger logger = LogManager.getLogger(App.class);
	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException, URISyntaxException {

//		prefs = Preferences.userRoot().node(this.getClass().getName());
//		playMusic(prefs.getBoolean("musicOn", true));

		scene = new Scene(loadFXML("main"));
		stage.setScene(scene);
		stage.show();
	}

	private static Parent loadFXML(String fxml) throws IOException {
		URL location = MainController.class.getResource(fxml + ".fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		return fxmlLoader.load();
	}

	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		WindowManager.initialize(primaryStage);
//		SceneManager.initialize(primaryStage, new LoadingScreen());
//        InputManager.initialize();
//        InputManager.onPressListener(KeyCode.C, () -> App.logger.debug(SceneManager.getScene()));
//
//		primaryStage.setScene(SceneManager.getScene());
//		primaryStage.show();
//
//		GameLoop loop = new GameLoop();
//		loop.start();
//	}
	
	public static void main(String[] args) {
		launch();
	}
}

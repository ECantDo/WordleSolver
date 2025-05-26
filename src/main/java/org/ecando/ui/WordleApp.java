package org.ecando.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class WordleApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app_icon.png")));

		Pane root = new Pane();
		Scene scene = new Scene(root, 600, 600);
		stage.setTitle("Wordle Solver");
		stage.setScene(scene);
		stage.show();
	}
}

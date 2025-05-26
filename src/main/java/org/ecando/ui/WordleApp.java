package org.ecando.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class WordleApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// Load in icon
		var img = getClass().getResourceAsStream("/icons/app_icon.png");
		// Check icon exists?
		if (img != null)
			// Apply icon since it exists
			stage.getIcons().add(new Image(img));
		else
			System.err.println("Image icon found to be null.");

		// Other stuff
		Pane root = new Pane();
		Scene scene = new Scene(root, 600, 600);
		stage.setTitle("Wordle Solver");
		stage.setScene(scene);
		stage.show();
	}
}

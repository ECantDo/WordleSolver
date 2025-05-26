package org.ecando.ui;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import javafx.geometry.Insets;

import org.ecando.ui.LetterButton.Colors;


public class WordleApp extends Application {

	//==================================================================================================================
	// VARS METHODS
	//==================================================================================================================

	private final Pane rootPane = new Pane();

	private int currentRowIndex = 0;
	private int currentInputIndex = 0;
	private final int ROWS = 6;
	private final int COLS = 5;

	private final LetterButton[] letterButtons = new LetterButton[ROWS * COLS];


	//==================================================================================================================
	// RUN METHOD
	//==================================================================================================================
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

		// Buttons
		this.setupButtons();
		//
		Scene scene = new Scene(rootPane, 600, 600);
		scene.setOnKeyPressed(this::handleKeyInput);


		stage.setTitle("Wordle Solver");
		stage.setScene(scene);
		stage.show();
		scene.getRoot().requestFocus();
	}

	//==================================================================================================================
	// TYPING METHODS
	//==================================================================================================================

	private void handleKeyInput(KeyEvent event) {
		String key = event.getText().toUpperCase();

		if (event.getCode() == KeyCode.BACK_SPACE && currentInputIndex > 0) {
			int idx = currentRowIndex * COLS + --currentInputIndex;
			letterButtons[idx].setLetter('\0');
			letterButtons[idx].setColor(Colors.DEFAULT);
			if (currentInputIndex < 0)
				currentInputIndex = 0;
			return;
		}

		if (!key.matches("[A-Z]") || currentInputIndex >= COLS) return;

		int idx = currentRowIndex * COLS + currentInputIndex;
		if (idx >= 0 && idx < letterButtons.length) {
			letterButtons[idx].setLetter(key.charAt(0));
			currentInputIndex++;

			if (currentInputIndex > COLS) {
				currentInputIndex = COLS;
			}
		}

	}


	//==================================================================================================================
	// BUTTON METHODS
	//==================================================================================================================
	private void setupButtons() {
		VBox verticalLayout = new VBox(10);
		verticalLayout.setPadding(new Insets(10));

		// Reset Button
		Button resetButton = new Button("Reset");
		resetButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-font-color: black;" +
				" -fx-background-color: #e57373; ");
		resetButton.setPrefSize(80, 30);

		resetButton.setOnAction(e -> {
			this.resetLetterButtons();
		});

		verticalLayout.getChildren().add(resetButton);


		// Word Buttons
		HBox[] rows = new HBox[ROWS];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new HBox(2);

			for (int j = 0; j < COLS; j++) {
				LetterButton btn = new LetterButton('\0');
				btn.setOnAction(e -> {
					btn.cycleColor();
				});
				rows[i].getChildren().add(btn);
				letterButtons[j + COLS * i] = btn;
			}
			verticalLayout.getChildren().add(rows[i]);
		}

		// Attach things to the pane
		rootPane.getChildren().add(verticalLayout);
	}

	private void resetLetterButtons() {
		this.currentInputIndex = 0;
		this.currentRowIndex = 0;

		for (LetterButton button : letterButtons) {
			if (button == null)
				continue;

			button.setLetter('\0');
			button.setColor(Colors.DEFAULT);
		}
	}
}

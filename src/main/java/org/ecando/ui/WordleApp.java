package org.ecando.ui;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

	private final LetterButton[][] letterButtons = new LetterButton[ROWS][COLS];

	private boolean rowNeedsReset = true;

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
			letterButtons[currentRowIndex][--currentInputIndex].setLetter('\0');
			letterButtons[currentRowIndex][currentInputIndex].setColor(Colors.DEFAULT);
			if (currentInputIndex < 0)
				currentInputIndex = 0;
			return;
		}

		if (event.getCode() == KeyCode.ENTER && currentRowIndex < ROWS - 1) {
			currentRowIndex++;
			currentInputIndex = 0;
			rowNeedsReset = true;
			return;
		}

		if (!key.matches("[A-Z]")) return;

		if (rowNeedsReset) {
			for (int i = 0; i < COLS; i++) {
				LetterButton btn = letterButtons[currentRowIndex][i];
				btn.setLetter('\0');
				btn.setColor(Colors.DEFAULT);
			}
			rowNeedsReset = false;
		}

		if (currentRowIndex >= 0 && currentRowIndex < ROWS && currentInputIndex < COLS) {
			letterButtons[currentRowIndex][currentInputIndex].setLetter(key.charAt(0));
			currentInputIndex++;
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
		resetButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;" +
				" -fx-background-color: #e57373; ");
		resetButton.setPrefSize(80, 30);

		resetButton.setOnAction(e -> {
			this.resetLetterButtons();
		});

		verticalLayout.getChildren().add(resetButton);


		// Word Buttons
		HBox[] rows = new HBox[ROWS];
		for (int i = 0; i < rows.length; i++) {
			final int rowIdx = i;
			rows[i] = new HBox(2);

			for (int j = 0; j < COLS; j++) {
				LetterButton btn = new LetterButton('\0');
				btn.setOnAction(createLetterButtonHandler(btn, rowIdx));

				rows[i].getChildren().add(btn);
				letterButtons[i][j] = btn;
			}

			// Attach to screen
			verticalLayout.getChildren().add(rows[i]);
		}

		// Attach things to the pane
		rootPane.getChildren().add(verticalLayout);
	}

	private void resetLetterButtons() {
		this.currentInputIndex = 0;
		this.currentRowIndex = 0;

		for (LetterButton[] buttonRow : letterButtons) {
			for (LetterButton button : buttonRow) {
				if (button == null)
					continue;

				button.setLetter('\0');
				button.setColor(Colors.DEFAULT);
			}
		}
	}

	private void activateRow(int rowIdx) {
		currentRowIndex = rowIdx;
		currentInputIndex = 0;
		rootPane.requestFocus();
		rowNeedsReset = true;
	}

	private EventHandler<ActionEvent> createLetterButtonHandler(LetterButton btn, int rowIdx) {
		return e -> {
			btn.cycleColor();
			activateRow(rowIdx);
		};
	}

	//==================================================================================================================
	// GET OUTPUTS
	//==================================================================================================================

	/**
	 * Returns all words in each row, as a String array.
	 *
	 * @return Each element will be one rows word. Null if the word is incomplete.
	 */
	public String[] getWords() {
		String[] strings = new String[ROWS];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = getWord(i);
		}

		return strings;
	}

	/**
	 * Returns the word that makes up a row.
	 *
	 * @param rowIndex The row index to get the word from.
	 * @return A string of said word. Null if the word is incomplete.
	 */
	public String getWord(int rowIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < COLS; i++) {
			LetterButton btn = letterButtons[rowIndex][i];

			char l = btn.getLetter();
			if (l == '\0') return null;
			sb.append(l);
		}

		return sb.toString();
	}
}

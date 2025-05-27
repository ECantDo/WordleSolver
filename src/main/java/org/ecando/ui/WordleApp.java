package org.ecando.ui;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import javafx.geometry.Insets;

import org.ecando.FindWords;
import org.ecando.GuessScore;
import org.ecando.ui.LetterButton.Colors;

import java.util.List;


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

	private final ObservableList<String> wordList = FXCollections.observableArrayList();
	private ListView<String> wordListView;

	private final ObservableList<GuessScore> guessList = FXCollections.observableArrayList();
	private ListView<GuessScore> guessListView;

	//==================================================================================================================
	// RUN METHOD
	//==================================================================================================================
	@Override
	public void start(Stage stage) {
		// Load in icon
		var img = getClass().getResourceAsStream("/icons/app_icon.png");
		// Check icon exists?
		if (img != null)
			// Apply icon since it exists
			stage.getIcons().add(new Image(img));
		else
			System.err.println("Image icon found to be null.");

		// Buttons
		HBox buttonOutputPlane = new HBox(10);
		buttonOutputPlane.getChildren().add(this.setupButtons());

		{
			VBox possibleWordPane = new VBox(10);

			Label possibleWordPaneLabel = new Label("Possible Words");
			possibleWordPaneLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			possibleWordPaneLabel.setPrefHeight(40);


			wordListView = new ListView<>(wordList);
			wordListView.setPrefSize(200, 472);
			wordListView.setStyle("-fx-font-size: 16px;");
			possibleWordPane.getChildren().addAll(possibleWordPaneLabel, wordListView);

			buttonOutputPlane.getChildren().add(possibleWordPane);
		}

		{
			VBox decentGuessPane = new VBox(10);

			Label possibleWordPaneLabel = new Label("Decent Guesses");
			possibleWordPaneLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			possibleWordPaneLabel.setPrefHeight(40);


			guessListView = new ListView<>(guessList);
			guessListView.setPrefSize(200, 472);

			guessListView.setCellFactory(listView -> new ListCell<>() {
				@Override
				protected void updateItem(GuessScore item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setGraphic(null);
						setText(null);
					} else {
						//create the bar
						double normalizedScore = Math.min(1.0, item.score / 20.0); //  todo; change the max
						String barColor = item.score <= 2 ? "#4caf50" : item.score <= 5 ? "#ffc107" : "#e57373";

						Label barLabel = new Label(String.valueOf(item.score));
						barLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
						barLabel.setMinWidth(30);
						barLabel.setMaxWidth(30);
						barLabel.setPrefHeight(20);
						barLabel.setStyle("-fx-background-color: " + barColor + "; -fx-alignment: center; -fx-text-fill: white;");

						Label label = new Label(item.word);
						label.setStyle("-fx-font-size: 16px;");

						HBox hbox = new HBox(5, barLabel, label);
						setGraphic(hbox);
					}
				}
			});

			decentGuessPane.getChildren().addAll(possibleWordPaneLabel, guessListView);

			buttonOutputPlane.getChildren().add(decentGuessPane);
		}


		// Attach everything to the root pane
		rootPane.getChildren().add(buttonOutputPlane);
		//
		Scene scene = new Scene(rootPane, 807, 535);
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

		// If clicked on, and backspace is pressed, clear... or just backspace on index 0
		if (event.getCode() == KeyCode.BACK_SPACE && currentInputIndex == 0) {
			for (int i = 0; i < COLS; i++) {
				LetterButton btn = letterButtons[currentRowIndex][i];
				btn.setLetter('\0');
				btn.setColor(Colors.DEFAULT);
			}
			return;
		}
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
			LetterButton btn = letterButtons[currentRowIndex][currentInputIndex];

			btn.setLetter(key.charAt(0));
			btn.setColor(Colors.INCORRECT);
			currentInputIndex++;
		}

	}

	//==================================================================================================================
	// BUTTON METHODS
	//==================================================================================================================
	private Pane setupButtons() {
		VBox verticalLayout = new VBox(10);
		verticalLayout.setPadding(new Insets(10));

		// Control Buttons
		//==============================================================================================================
		HBox controlButtons = new HBox(5);
		// Reset Button
		Button resetButton = new Button("Reset");
		resetButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;" +
				" -fx-background-color: #e57373; ");
		resetButton.setPrefSize(80, 30);
		resetButton.setOnAction(e -> {
			this.wordList.clear();
			this.resetLetterButtons();
			this.guessList.clear();
		});
		controlButtons.getChildren().add(resetButton);

		// Solve button
		Button solveButton = new Button("Solve");
		solveButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;" +
				" -fx-background-color: #4caf50;");
		solveButton.setPrefSize(80, 30);
		solveButton.setOnAction(e -> {
			List<String> possibleWords = FindWords.findWords(this);
			wordList.setAll(possibleWords);
			guessList.setAll(FindWords.rankGuesses(possibleWords));
		});
		controlButtons.getChildren().add(solveButton);

		// Add control buttons to vertical layout
		verticalLayout.getChildren().add(controlButtons);


		// Word Buttons
		//==============================================================================================================
		HBox[] rows = new HBox[ROWS];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new HBox(2);

			for (int j = 0; j < COLS; j++) {
				LetterButton btn = new LetterButton('\0');
				btn.setOnAction(createLetterButtonHandler(btn, i));

				rows[i].getChildren().add(btn);
				letterButtons[i][j] = btn;
			}

			// Attach to screen
			//==============================================================================================================
			verticalLayout.getChildren().add(rows[i]);
		}

		return verticalLayout;
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

		return sb.toString().toLowerCase();
	}

	/**
	 * Returns the color states for all letters in all rows.
	 *
	 * @return A 2D array representing the color of each button in each row.
	 */
	public Colors[][] getColors() {
		Colors[][] colors = new Colors[ROWS][];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = getWordColors(i);
		}

		return colors;
	}

	/**
	 * Returns the color states for all letters in a specific row.
	 *
	 * @param rowIndex The index of the row (0-based).
	 * @return An array of Colors for the specified row.
	 */
	public Colors[] getWordColors(int rowIndex) {
		if (letterButtons[rowIndex] == null)
			return null;

		Colors[] colors = new Colors[COLS];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = letterButtons[rowIndex][i].getColor();
		}
		return colors;
	}
}

package org.ecando.ui;

import javafx.scene.control.Button;

public class LetterButton extends Button {

	//==================================================================================================================
	// CONSTRUCTORS
	//==================================================================================================================
	public LetterButton() {
		this(' ');
	}

	public LetterButton(char c) {
		super();
		// Square button
		setPrefSize(50, 50);
		// Make letter visible
		setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// Center text
		setFocusTraversable(true);

		// Apply initial letter
		setLetter(c);

		color = Colors.DEFAULT;
	}

	//==================================================================================================================
	// VARIABLE & ENUMS
	//==================================================================================================================
	public enum Colors {
		DEFAULT,
		CORRECT,
		INCORRECT,
		MISPLACED,
	}

	private Colors color;

	//==================================================================================================================
	// LETTER METHODS
	//==================================================================================================================

	/**
	 * Set the displayed character in the button.
	 *
	 * @param letter The letter to set.
	 */
	public void setLetter(char letter) {
		if (Character.isLetter(letter))
			setText(String.valueOf(Character.toUpperCase(letter)));
		else
			setText(" ");
	}

	/**
	 * Get the letter currently displayed. \0 if none.
	 *
	 * @return Character.
	 */
	public char getLetter() {
		String text = getText();
		// Check if text exists & length is 1; if yes, return letter, otherwise nothing
		return (text != null && text.length() == 1) ? text.charAt(0) : '\0';
	}

	//==================================================================================================================
	// COLOR METHODS
	//==================================================================================================================
	public void cycleColor() {
		switch (this.color) {
			case DEFAULT, INCORRECT -> this.color = Colors.CORRECT;
			case CORRECT -> this.color = Colors.MISPLACED;
			case MISPLACED -> this.color = Colors.INCORRECT;
			default -> this.color = Colors.DEFAULT;
		}
		updateColor();
	}

	public void setColor(Colors color) {
		this.color = color;
		this.updateColor();
	}

	private void updateColor() {
		String style = "-fx-font-size: 24px; -fx-font-weight: bold;";

		switch (this.color) {
			case CORRECT:
				style += " -fx-background-color: #6aaa64; -fx-text-fill: white;";
				break;
			case INCORRECT:
				style += " -fx-background-color: #787c7e; -fx-text-fill: white;";
				break;
			case MISPLACED:
				style += " -fx-background-color: #c9b458; -fx-text-fill: white;";
				break;
			case DEFAULT:
			default:
				style += " -fx-background-color: #d3d6da; -fx-text-fill: black;";
				break;

		}

		this.setStyle(style);

	}
}

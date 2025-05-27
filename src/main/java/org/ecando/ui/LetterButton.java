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
		setPrefSize(70, 70);
		// Make letter visible
		setStyle(DEFAULT_FX_STR);

		// Center text
		setFocusTraversable(true);

		// Apply initial letter
		setLetter(c);

		color = Colors.DEFAULT;
		updateColor();
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

	private final String DEFAULT_FX_STR = "-fx-font-size: 27px; -fx-font-weight: bold;";

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
		String style = DEFAULT_FX_STR;

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

	public Colors getColor() {
		return this.color;
	}
}

package org.ecando.ui;

import javafx.scene.control.Button;

public class LetterButton extends Button {

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
	}

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
}

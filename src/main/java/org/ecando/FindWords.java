package org.ecando;

import org.ecando.ui.LetterButton;
import org.ecando.ui.WordleApp;

import java.util.ArrayList;
import java.util.Arrays;

public class FindWords {

	private static final String[] words = ReadInWords.readInWords();

	public static ArrayList<String> findWords(WordleApp app) {
		LetterButton.Colors[][] colors = app.getColors();
		String[] words = app.getWords();

		StringBuilder disallowedLettersBuilder = new StringBuilder();
		char[] requiredOrder = new char[5];
		String[] orderedDisallowedLetters = new String[5];
		Arrays.fill(orderedDisallowedLetters, "");

		for (int i = 0; i < words.length; i++) {
			if (words[i] == null)
				continue;

			char[] letters = words[i].toCharArray();
			for (int j = 0; j < letters.length; j++) {
				switch (colors[i][j]) {
					case INCORRECT:
						disallowedLettersBuilder.append(letters[j]);
						break;
					case CORRECT:
						if (requiredOrder[j] == '\0')
							requiredOrder[j] = letters[j];
						break;
					case MISPLACED:
						if (!orderedDisallowedLetters[j].contains(String.valueOf(letters[j])))
							orderedDisallowedLetters[j] += letters[j];
						break;
				}

			}
		}

		String disallowedLetters = disallowedLettersBuilder.toString();

		System.out.println("Dissallowed Letters = " + disallowedLetters +
				"\nRequired Order = " + Arrays.toString(requiredOrder) +
				"\nOrdered Disallowed Letters = " + Arrays.toString(orderedDisallowedLetters));

		return null;
	}
}

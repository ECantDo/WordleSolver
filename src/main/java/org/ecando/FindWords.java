package org.ecando;

import org.ecando.ui.LetterButton;
import org.ecando.ui.WordleApp;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FindWords {

	private static final String[] words = ReadInWords.readInWords();

	public static ArrayList<String> findWords(WordleApp app) {
		LetterButton.Colors[][] colors = app.getColors();
		String[] words = app.getWords();

		// TODO: Implement system for duplicate letters
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

//		System.out.println("Dissallowed Letters = " + disallowedLetters +
//				"\nRequired Order = " + Arrays.toString(requiredOrder) +
//				"\nOrdered Disallowed Letters = " + Arrays.toString(orderedDisallowedLetters));

		ArrayList<String> output = filterWords(requiredOrder, orderedDisallowedLetters, disallowedLetters);
//		System.out.println(output);
		return output;
	}

	private static String findRequiredLetters(char[] chars, String[] strings) {
		HashSet<Character> seen = new HashSet<>();
		StringBuilder result = new StringBuilder();
		for (char c : chars)
			if (c != '\0' && seen.add(c))
				result.append(c);

		for (String str : strings)
			for (char c : str.toCharArray())
				if (seen.add(c))
					result.append(c);

		return result.toString();
	}

	private static ArrayList<String> filterWords(char[] requiredOrder, String[] orderedDisallowedLetters,
	                                             String disallowedLetters) {
		ArrayList<String> possibleWords = new ArrayList<>();
		String requiredLetters = findRequiredLetters(requiredOrder, orderedDisallowedLetters);

		String bannedLetters = "";
		for (char c : disallowedLetters.toCharArray()) {
			boolean valid = true;
			for (char l : requiredLetters.toCharArray()) {
				if (c == l) {
					valid = false;
					break;
				}
			}
			if (valid)
				bannedLetters += c;
		}

		for (String word : FindWords.words) {
			boolean wordPossible = true;
			for (char c : requiredLetters.toCharArray()) {
				boolean contains = false;
				for (char l : word.toCharArray()) {
					if (c == l) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					wordPossible = false;
					break;
				}

			}

			if (!wordPossible)
				continue;

			for (int i = 0; i < word.length() && wordPossible; i++) {
				char letter = word.charAt(i);
				if (requiredOrder[i] != '\0' && letter != requiredOrder[i]) {
					wordPossible = false;
					break;
				}

				boolean possible = true;
				for (char c : orderedDisallowedLetters[i].toCharArray()) {
					if (letter == c) {
						possible = false;
						break;
					}
				}
				if (!possible) {
					wordPossible = false;
					break;
				}

				// TODO: Fix letter counts.  Currently ignores that possibility
				for (char c : bannedLetters.toCharArray()) {
					if (letter == c) {
						wordPossible = false;
						break;
					}
				}
			}

			if (!wordPossible)
				continue;

			possibleWords.add(word);

		}
		return possibleWords;
	}
}

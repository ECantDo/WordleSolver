package org.ecando;

import org.ecando.ui.LetterButton;
import org.ecando.ui.WordleApp;

import java.util.*;

public class FindWords {


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

		return filterWords(requiredOrder, orderedDisallowedLetters, disallowedLetters);
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

		for (String word : ReadInWords.getPossibleWords()) {
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

	/**
	 * Ranks guesses by how well they partition the possible answers. Based on possible guesses being the full range
	 * of possibilities.
	 *
	 * @param possibleAnswers List of words that could be the correct answer.
	 * @return A list of guesses ranked by how well they cut down the answer pool.
	 */
	public static List<GuessScore> rankGuesses(List<String> possibleAnswers) {
		return rankGuesses(possibleAnswers, List.of(ReadInWords.getPossibleWords()));
	}

	/**
	 * Ranks guesses by how well they partition the possible answers.
	 *
	 * @param possibleAnswers List of words that could be the correct answer.
	 * @param allGuesses      List of all allowed guess words.
	 * @return A list of guesses ranked by how well they cut down the answer pool.
	 */
	public static List<GuessScore> rankGuesses(List<String> possibleAnswers, List<String> allGuesses) {
		if (possibleAnswers.size() == 1) {
			// Only one possible answer; return it as the best guess with score 0
			String onlyAnswer = possibleAnswers.getFirst();
			return List.of(new GuessScore(onlyAnswer, 0));
		}


		Map<String, Integer> guessScores = new HashMap<>();

		for (String guess : allGuesses) {
			Map<String, Integer> patternCounts = new HashMap<>();

			for (String answer : possibleAnswers) {
				String feedback = getFeedbackPattern(guess, answer);
				patternCounts.put(feedback, patternCounts.getOrDefault(feedback, 0) + 1);
			}

			// Score the guess by the size of the largest pattern bucket (minimize worst-case)
			int worstCaseSize = patternCounts.values().stream().max(Integer::compare).orElse(0);
			guessScores.put(guess, worstCaseSize);
		}

		// Sort guesses by how small the worst-case group is
		Set<String> possibleSet = new HashSet<>(possibleAnswers);

		return guessScores.entrySet().stream()
				.sorted(Comparator
						.comparingInt(Map.Entry<String, Integer>::getValue)
						.thenComparing(e -> !possibleSet.contains(e.getKey())))  // true last, so in-possible comes first
				.limit(50)
				.map(e -> new GuessScore(e.getKey(), e.getValue()))
				.toList();
	}

	private static String getFeedbackPattern(String guess, String answer) {
		char[] pattern = new char[guess.length()];
		boolean[] used = new boolean[guess.length()];

		// 1. Green pass
		for (int i = 0; i < guess.length(); i++) {
			if (guess.charAt(i) == answer.charAt(i)) {
				pattern[i] = 'G';
				used[i] = true;
			}
		}

		// 2. Yellow & Black pass
		for (int i = 0; i < guess.length(); i++) {
			if (pattern[i] == 'G') continue;

			char g = guess.charAt(i);
			boolean found = false;
			for (int j = 0; j < answer.length(); j++) {
				if (!used[j] && g == answer.charAt(j)) {
					found = true;
					used[j] = true;
					break;
				}
			}

			pattern[i] = found ? 'Y' : 'B';
		}

		return new String(pattern);
	}


}

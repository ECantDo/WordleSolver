package org.ecando;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadInWords {

	private static String[] possibleWords;// = ReadInWords.readInPossibleWords();
	private static GuessScore[] blankSolveWords;

	public static String[] getPossibleWords() {
		if (possibleWords == null)
			possibleWords = readInPossibleWords();
		return possibleWords;
	}

	public static GuessScore[] getBlankSolveWords() {
		if (blankSolveWords == null)
			blankSolveWords = readInBlankSolve();
		return blankSolveWords;
	}


	/**
	 * Reads in words from a text file, where each new line contains a single word.
	 * Assumes a default location with this function for my personal computer.
	 *
	 * @return Returns a String array containing all the words, without the new line characters at the end.
	 */
	private static String[] readInPossibleWords() {
		return readInPossibleWords("Words-5Letters.txt");
	}

	/**
	 * Reads in words from a text file, where each new line contains a single word.
	 *
	 * @param fileName Path to the file that contains the words.
	 * @return Returns a String array containing all the words, without the new line characters at the end.
	 */
	private static String[] readInPossibleWords(String fileName) {
		if (fileName == null)
			throw new RuntimeException("fileName cannot be null");

		// Use getResourceAsStream to read file from resources folder (inside jar or classes dir)
		InputStream is = ReadInWords.class.getResourceAsStream(fileName.startsWith("/") ? fileName : "/" + fileName);
		if (is == null) {
			throw new RuntimeException("Resource not found: " + fileName);
		}

		Scanner wordScanner = new Scanner(is);
		ArrayList<String> words = new ArrayList<>();
		while (wordScanner.hasNextLine()) {
			String word = wordScanner.nextLine();
			words.add(word.toLowerCase().strip());
		}
		wordScanner.close();

		return words.toArray(new String[0]);
	}

	private static GuessScore[] readInBlankSolve() {
		String fileName = "/BlankSolve.csv";
		GuessScore[] words = new GuessScore[50];
		int i = 0;
		InputStream is = ReadInWords.class.getResourceAsStream(fileName);

		if (is == null)
			throw new RuntimeException("Resource not found: " + fileName);

		Scanner wordScanner = new Scanner(is);
		while (wordScanner.hasNextLine()) {
			String line = wordScanner.nextLine();
			String word = line.substring(0, 5);
			int score = Integer.parseInt(line.substring(6));
			words[i++] = new GuessScore(word, score);
		}
		return words;
	}
}

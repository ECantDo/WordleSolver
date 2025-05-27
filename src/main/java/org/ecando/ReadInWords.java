package org.ecando;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadInWords {

	/**
	 * Reads in words from a text file, where each new line contains a single word.
	 * Assumes a default location with this function for my personal computer.
	 *
	 * @return Returns a String array containing all the words, without the new line characters at the end.
	 */
	public static String[] readInWords() {
		return readInWords("Words-5Letters.txt");
	}

	/**
	 * Reads in words from a text file, where each new line contains a single word.
	 *
	 * @param fileName Path to the file that contains the words.
	 * @return Returns a String array containing all the words, without the new line characters at the end.
	 */
	public static String[] readInWords(String fileName) {
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
}

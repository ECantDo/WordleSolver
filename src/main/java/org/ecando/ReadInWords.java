package org.ecando;

import java.io.File;
import java.io.FileNotFoundException;
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
		return readInWords("E:\\Data Files\\words\\Words-5Letters.txt");
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

		File wordFile = new File(fileName);
		Scanner wordScanner;
		try {
			wordScanner = new Scanner(wordFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		ArrayList<String> words = new ArrayList<>();
		while (wordScanner.hasNext()) {
			String word = wordScanner.nextLine();
			words.add(word.toLowerCase().strip());
		}

		return words.toArray(new String[0]);
	}
}

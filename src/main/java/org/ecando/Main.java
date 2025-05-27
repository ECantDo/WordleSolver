package org.ecando;

import org.ecando.ui.WordleApp;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
//		System.out.println(Arrays.toString(ReadInWords.readInWords()));
		WordleApp.launch(WordleApp.class, args);
	}
}
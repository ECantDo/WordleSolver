package org.ecando;

public class GuessScore {
	public final String word;
	public final int score;

	public GuessScore(String word, int score) {
		this.word = word;
		this.score = score;
	}

	@Override
	public String toString() {
		return word;
	}
}


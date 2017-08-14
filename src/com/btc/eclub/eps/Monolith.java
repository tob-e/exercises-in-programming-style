package com.btc.eclub.eps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Monolith {

	public static void main(String[] args) throws Exception {
		// the global list of [word, frequency] pairs
		List<String> words = new ArrayList<>();
		List<Integer> freqs = new ArrayList<>();

		// the list of stop words
		List<String> stopWords = Arrays.asList(Files.readAllLines(Paths.get("stop_words.txt")).get(0).split(","));

		// iterate through the file one line at a time
		for (String line : Files.readAllLines(Paths.get(args[0]))) {
			int startChar = -1;
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);

				if (startChar == -1) {
					if (Character.isAlphabetic(c) || Character.isDigit(c)) {
						// We found the start of a word
						startChar = i;
					}
				} else {
					if (!(Character.isAlphabetic(c) || Character.isDigit(c))) {
						// We found the end of a word. Process it
						boolean found = false;
						String word = line.substring(startChar, i).toLowerCase();

						// Ignore stop words
						if (!stopWords.contains(word)) {
							int pairIndex = 0;
							// Let's see if it already exists
							for (int j = 0; j < words.size(); j++) {
								if (words.get(j).equals(word)) {
									freqs.set(j, freqs.get(j) + 1);
									found = true;
									break;
								}
								pairIndex++;
							}
							if (!found) {
								words.add(word);
								freqs.add(1);
							} else if (words.size() > 1) {
								// We may need to reorder
								for (int n = pairIndex; n >= 0; n--) {
									if (freqs.get(pairIndex) > freqs.get(n)) {
										// swap
										int f = freqs.get(pairIndex);
										freqs.set(pairIndex, freqs.get(n));
										freqs.set(n, f);
										String w = words.get(pairIndex);
										words.set(pairIndex, words.get(n));
										words.set(n, w);
										pairIndex = n;
									}
								}
							}
						}
						startChar = -1;
					}
				}
			}
		}

		for (int i = 0; i < 25 && i < words.size(); i++) {
			System.out.println(words.get(i) + " - " + freqs.get(i));
		}
	}
}

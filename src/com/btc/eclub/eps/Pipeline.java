package com.btc.eclub.eps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Pipeline {

	public static void main(String[] args) throws IOException {
		printSorted(countWords(removeStopWords(extractWords(removeSpecialChars(readLines(args[0]))))));
	}
	
	private static void printSorted(Map<String, Integer> wordMap) {
		wordMap.entrySet().stream() // 
		.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) //
		.limit(25) // 
		.forEach(System.out::println);;
	}

	public static Map<String, Integer> countWords(List<String> words){
		Map<String, Integer> wordFrequencies = new HashMap<>();
		for (String word : words) {
			int increment = 1;
			if (wordFrequencies.containsKey(word)) {
				increment += wordFrequencies.get(word);
			}
			wordFrequencies.put(word, increment);
		}
		return wordFrequencies;
	}

	private static List<String> removeStopWords(List<String> words) throws IOException {
		List<String> stopWords = Arrays.asList(Files.readAllLines(Paths.get("stop_words.txt")).get(0).split(","));
		return words.stream().filter(word -> !stopWords.contains(word)).collect(Collectors.toList());
	}
	
	public static List<String> extractWords(List<String> lines) throws IOException{
		List<String> words = new ArrayList<>();
		for (String line : lines) {
			for (String word : line.split(" ")) {
				word = word.trim();
				if(word.length() > 0) {
					words.add(word.toLowerCase());	
				}
			}
		}
		return words;
	}
	
	public static List<String> removeSpecialChars(List<String> lines) throws IOException{
		List<String> cleanLines = new ArrayList<>();
		for (String line : lines) {
			cleanLines.add(line.replaceAll("[^a-zA-Z0-9 ]",""));
		}
		return cleanLines; 
	}
	
	public static List<String> readLines(String inputFile) throws IOException{
		return Files.readAllLines(Paths.get(inputFile));
	}
	

	
}

package com.btc.eclub.eps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Spreadsheet {

	static class Cell<T> {
		final Supplier<T> callable;
		T value;

		Cell(Supplier<T> callable) {
			this.callable = callable;
		}

		T get() {
			if (value == null)
				compute();
			return value;
		}

		void compute() {
			value = callable.get();
		}
	}

	public static void main(String[] args) throws Exception {
		// load data for the first two cells
		String allWordsString = new String(Files.readAllBytes(Paths.get(args[0]))).toLowerCase();
		List<String> allWordsValue = Arrays.asList(allWordsString.split("[^a-z]+"));

		String stopWordsString = new String(Files.readAllBytes(Paths.get("stop_words.txt"))).toLowerCase();
		List<String> stopWordsValue = Arrays.asList(stopWordsString.split(","));

		// define the cells
		Cell<List<String>> allWords = new Cell<>(() -> allWordsValue);
		Cell<List<String>> stopWords = new Cell<>(() -> stopWordsValue);

		Cell<List<String>> nonStopWords = new Cell<>(
				() -> allWords.get().stream().filter(x -> !stopWords.get().contains(x)).collect(Collectors.toList()));

		Cell<List<String>> uniqueWords = new Cell<>(
				() -> nonStopWords.get().stream().distinct().collect(Collectors.toList()));

		Cell<List<Entry<String, Long>>> counts = new Cell<>(() -> uniqueWords.get().stream()
				.map(u -> new SimpleEntry<>(u, nonStopWords.get().stream().filter(x -> x.equals(u)).count()))
				.collect(Collectors.toList()));

		Cell<List<Entry<String, Long>>> sorted = new Cell<>(() -> counts.get().stream()
				.sorted((a, b) -> b.getValue().compareTo(a.getValue())).limit(25).collect(Collectors.toList()));

		// compute all cell's values
		Arrays.asList(allWords, stopWords, nonStopWords, uniqueWords, counts, sorted).forEach(Cell::compute);

		// output the result
		sorted.get().forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
	}
}

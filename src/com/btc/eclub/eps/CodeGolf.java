package com.btc.eclub.eps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CodeGolf {

	public static void main(String[] args) throws Exception {
		List<String> s = Arrays.asList(Files.readAllLines(Paths.get("stop_words.txt")).get(0).split(","));
		String[] l = new String(Files.readAllBytes(Paths.get(args[0]))).split("[^a-zA-Z]+");
		Predicate<String> f = x -> !s.contains(x.toLowerCase());
		Arrays.stream(l) //
				.filter(f) //
				.distinct() //
				.map(u -> new SimpleEntry<>(u, Arrays.stream(l).filter(f).filter(x -> x.equals(u)).count())) //
				.sorted((a, b) -> b.getValue().compareTo(a.getValue())) //
				.limit(25) //
				.forEach(System.out::println);
	}
}

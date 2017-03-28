package main;

import algorithm.TextRank;
import algorithm.unweighted.UnweightedTextRank;
import algorithm.weighted.WeightedTextRank;

public class Main {

	public static void main(String[] args) {
		UnweightedTextRank tr = new UnweightedTextRank();
		System.out.println("Unweighted algorithm");
		tr.extractKeywordsFromText("data/news.txt", 1);
		
		System.out.println();
		
		WeightedTextRank tr2 = new WeightedTextRank();
		System.out.println("Weighted algorithm, window size: 5");
		tr2.extractKeywordsFromText("data/java.txt", 5);

	}

}

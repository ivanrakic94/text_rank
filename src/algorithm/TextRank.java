package algorithm;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Edge;
import model.Node;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;


public abstract class TextRank {
	
	public UndirectedGraph<Node, Edge> graph = new UndirectedSparseGraph<Node, Edge>();
	public static final double d = 0.85;
	
	public abstract void connectVertices(List<String> words, int windowSize);
	public abstract void calculateVerticesScore();
	public abstract double calculateScore(Node n, List<Node> visited);
	
	public void extractKeywordsFromText(String path, int windowSize) {
		Document text = new Document(readText(path));
		List<Sentence> senteces = text.sentences();
		
		List<String> words = returnWords(senteces);
		List<String> filteredWords = addWordsToGraph(senteces);
		System.out.println(words);
		System.out.println(graph.toString());
		
		connectVertices(filteredWords, windowSize);
		System.out.println(graph.getVertexCount());
		System.out.println(graph.getEdgeCount());
		
		calculateVerticesScore();
		
		Node[] vertices = rankVertices();
		
		List<String> keywords = extractKeywords(vertices);
		
		List<String> mergedKeywords = mergeKeywords(keywords, filteredWords);
		
		removeMergedKeywords(keywords, mergedKeywords);
		
		displayKeywords(keywords, mergedKeywords);
	}
	
	private void removeMergedKeywords(List<String> keywords, List<String> mergedKeywords) {
		for (String s : mergedKeywords) {
			String[] words = s.split(" ");
			keywords.remove(words[0]);
			keywords.remove(words[1]);
		}
	}

	private void displayKeywords(List<String> keywords, List<String> mergedKeywords) {
		System.out.println("Keywords for given text:");
		for (String s : mergedKeywords) {
			System.out.println(s);
		}
		for (String s : keywords) {
			System.out.println(s);
		}
		
	}

	private List<String> mergeKeywords(List<String> keywords, List<String> words) {
		List<String> mergedKeywords = new ArrayList<String>();
		for (int i = 0; i < words.size() - 1; i++) {
			if (keywords.contains(words.get(i)) && keywords.contains(words.get(i + 1)) && !mergedKeywords.contains(words.get(i) + " " + words.get(i + 1))) {
					mergedKeywords.add(words.get(i) + " " + words.get(i + 1));
			}
		}
		return mergedKeywords;
		
	}

	private List<String> extractKeywords(Node[] vertices) {
		int numKeyWords = Math.min(15, graph.getVertexCount() / 3);
		List<String> keywords = new ArrayList<String>();
		for (int i = 0; i < numKeyWords; i++) {
			keywords.add(vertices[i].getValue());
		}
		
		return keywords;
	}

	private Node[] rankVertices() {
		Node[] vertices = graph.getVertices().toArray(new Node[graph.getVertexCount()]);
		
		Arrays.sort(vertices);
		
		return vertices;
	}


	private List<String> addWordsToGraph(List<Sentence> senteces) {
		List<String> filteredWords = new ArrayList<String>();
		
		List<String> allowedPos = new ArrayList<String>();
		allowedPos.add("JJ");
		allowedPos.add("NN");
		allowedPos.add("NNP");
		allowedPos.add("NNPS");
		allowedPos.add("NNS");
		
		for (Sentence s : senteces) {
			List<String> posTags = s.posTags();
			for (int i = 0; i < posTags.size(); i++) {
				if (allowedPos.contains(posTags.get(i))) {
					boolean f = true;
					for (Node n : graph.getVertices()) {
						if (s.lemma(i).equals(n.getValue())) {
							f = false;
						}
					}
					if (f) {
						graph.addVertex(new Node(s.lemma(i)));
						filteredWords.add(s.lemma(i));
					}
				}
			}
		}
		
		return filteredWords;
	}

	private List<String> returnWords(List<Sentence> senteces) {
		List<String> words = new ArrayList<String>();
		
		for (Sentence s : senteces) {
			for (String w : s.lemmas()) {
					words.add(w);
			}
		}
		
		return words;
	}

	private String readText(String path) {
		BufferedReader in;
		String text = "";
		try {
			in = new BufferedReader(new FileReader(path));
			
			while (true) {
				String line = in.readLine();
				if (line == null) {
					in.close();
					break;
				}
				text += line + " ";
			}
			
			return text;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}

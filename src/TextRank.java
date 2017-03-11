import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;


public class TextRank {
	
	static UndirectedGraph<Node, Edge> graph = new UndirectedSparseGraph<Node, Edge>();
	public static final double d = 0.85;

	public static void main(String[] args) {
		
			Document text = new Document(readText("data/news.txt"));
			List<Sentence> senteces = text.sentences();
			
			List<String> words = returnWords(senteces);
			addWordsToGraph(senteces);
			
			connectVertices(words);
			
			calculateVerticesScore();
			
			Node[] vertices = rankVertices();
			
			List<String> keywords = extractKeywords(vertices);
			
			List<String> mergedKeywords = mergeKeywords(keywords, words);
			
			removeMergedKeywords(keywords, mergedKeywords);
			
			displayKeywords(keywords, mergedKeywords);
			
	}
	
	private static void removeMergedKeywords(List<String> keywords, List<String> mergedKeywords) {
		for (String s : mergedKeywords) {
			String[] words = s.split(" ");
			keywords.remove(words[0]);
			keywords.remove(words[1]);
		}
	}

	private static void displayKeywords(List<String> keywords, List<String> mergedKeywords) {
		System.out.println("Keywords for given text:");
		for (String s : mergedKeywords) {
			System.out.println(s);
		}
		for (String s : keywords) {
			System.out.println(s);
		}
		
	}

	private static List<String> mergeKeywords(List<String> keywords, List<String> words) {
		List<String> mergedKeywords = new ArrayList<String>();
		for (int i = 0; i < words.size() - 1; i++) {
			if (keywords.contains(words.get(i)) && keywords.contains(words.get(i + 1)) && !mergedKeywords.contains(words.get(i) + " " + words.get(i + 1))) {
					mergedKeywords.add(words.get(i) + " " + words.get(i + 1));
			}
		}
		return mergedKeywords;
		
	}

	private static List<String> extractKeywords(Node[] vertices) {
		int numKeyWords = Math.min(15, graph.getVertexCount() / 3);
		List<String> keywords = new ArrayList<String>();
		for (int i = 0; i < numKeyWords; i++) {
			keywords.add(vertices[i].getValue());
		}
		
		return keywords;
	}

	private static Node[] rankVertices() {
		Node[] vertices = graph.getVertices().toArray(new Node[graph.getVertexCount()]);
		
		for (int i = 0; i < vertices.length; i++) {
			for (int j = 0; j < vertices.length; j++) {
				if (vertices[j].getScore() < vertices[i].getScore()) {
					Node p = vertices[i];
					vertices[i] = vertices[j];
					vertices[j] = p;
				}
			}
		}
		
		return vertices;
	}

	private static void calculateVerticesScore() {
		for (Node v : graph.getVertices()) {
			List<Node> visited = new ArrayList<Node>();
			v.setScore(calculateScore(v, graph, visited));
		}
		
	}

	private static void connectVertices(List<String> words) {
		for (int i = 0; i < words.size()-1; i++) {
			String current = words.get(i);
			String next = words.get(i+1);
			Node source = null, target = null;
			for (Node v : graph.getVertices()) {
				if (v.getValue().equals(current)) source = v;
				else if (v.getValue().equals(next)) target = v;
				if (source!= null & target != null) {
					if (graph.findEdge(source, target) == null)
						graph.addEdge(new Edge(1), source, target);
					break;
				}
			}
		}
		
	}

	private static void addWordsToGraph(List<Sentence> senteces) {
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
					}
				}
			}
		}
		
	}

	private static List<String> returnWords(List<Sentence> senteces) {
		List<String> words = new ArrayList<String>();
		
		for (Sentence s : senteces) {
			for (String w : s.lemmas()) {
					words.add(w);
			}
		}
		
		return words;
	}

	private static String readText(String path) {
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

	private static double calculateScore(Node n, Graph<Node, Edge> g, List<Node> visited) {
			if (visited.contains(n)) {
				return n.getScore();
			} else {
				visited.add(n);
				double sum = 0;
				for (Node next : g.getPredecessors(n)) {
					sum += calculateScore(next, g, visited) / g.getSuccessorCount(next);
				}
				return (1 - d) + d * sum;
			}
	}

}

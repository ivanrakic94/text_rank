package algorithm.unweighted;
import java.util.ArrayList;
import java.util.List;

import algorithm.TextRank;
import model.Edge;
import model.Node;
import edu.uci.ics.jung.graph.Graph;


public class UnweightedTextRank extends TextRank {
	
	
	
	@Override
	public void connectVertices(List<String> words, int windowSize) {
		for (int i = 0; i < words.size()-1; i++) {
			String current = words.get(i);
			String next = words.get(i+1);
			Node source = null, target = null;
			for (Node v : graph.getVertices()) {
				if (v.getValue().equals(current)) source = v;
				else if (v.getValue().equals(next)) target = v;
				if (source!= null && target != null) {
					if (graph.findEdge(source, target) == null)
						graph.addEdge(new Edge(1), source, target);
					break;
				}
			}
		}
	}

	@Override
	public void calculateVerticesScore() {
		for (Node v : graph.getVertices()) {
			List<Node> visited = new ArrayList<Node>();
			v.setScore(calculateScore(v, visited));
		}
		
	}

	@Override
	public double calculateScore(Node n, List<Node> visited) {
		if (visited.contains(n)) {
			return n.getScore();
		} else {
			visited.add(n);
			double sum = 0;
			for (Node next : graph.getPredecessors(n)) {
				sum += calculateScore(next, visited) / graph.getSuccessorCount(next);
			}
			return (1 - d) + d * sum;
		}
		
	}

}

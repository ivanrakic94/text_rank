package algorithm.unweighted;
import java.util.ArrayList;
import java.util.List;

import algorithm.TextRank;
import model.Edge;
import model.Node;


public class UnweightedTextRank extends TextRank {
	
	
	
	@Override
	public void connectVertices(List<String> words, int windowSize) {
		for (int i = 0; i < words.size()-1; i++) {
			Node source = nodesMap.get(words.get(i));
			if (source == null) continue;
			
			for (int j = i + 1; j <= Math.min(words.size() - 1, i + windowSize); j++) {
				Node target = nodesMap.get(words.get(j));	
				if (target == null) continue;
				
				if (graph.findEdge(source, target) == null) 
					graph.addEdge(new Edge(1), source, target);
			
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
			for (Node previous : graph.getPredecessors(n)) {
				sum += calculateScore(previous, visited) / graph.getSuccessorCount(previous);
			}
			return (1 - d) + d * sum;
		}
		
	}

}

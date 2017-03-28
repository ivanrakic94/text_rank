package algorithm.weighted;

import java.util.ArrayList;
import java.util.List;

import algorithm.TextRank;
import model.Edge;
import model.Node;
import edu.uci.ics.jung.graph.Graph;


public class WeightedTextRank extends TextRank{
	
	

	@Override
	public void connectVertices(List<String> words, int windowSize) {
		for (int i = 0; i < words.size()-1; i++) {
			String current = words.get(i);
			for (int j = i + 1; j <= Math.min(words.size() - 1, i + windowSize); j++) {
				String next = words.get(j);
				Node source = null, target = null;
				for (Node v : graph.getVertices()) {
					if (v.getValue().equals(current)) source = v;
					else if (v.getValue().equals(next)) target = v;
					if (source != null && target != null) {
						if (graph.findEdge(source, target) == null) {
							graph.addEdge(new Edge(1), source, target);
						} else {
							Edge e = graph.findEdge(source, target);
							e.setDistance(e.getDistance() + 1);
						}
						break;
					}
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
			for (Node previous : graph.getPredecessors(n)) {
				sum += (graph.findEdge(previous, n).getDistance()) * calculateScore(previous, visited) / calculateSuccessorWeights(previous);
			}
				return (1 - d) + d * sum;
		}
	}
	
	private double calculateSuccessorWeights(Node previous) {
		double sum = 0;
		for (Node n : graph.getSuccessors(previous)) {
			sum += (graph.findEdge(previous, n).getDistance());
		}
		
		return sum;
	}
	
}



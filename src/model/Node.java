package model;

public class Node implements Comparable<Node> {
	
	private String value;
	private double score;

	public Node() {
	}
	
	public Node(String value) {
		this.value = value;
	}
	
	public Node(String value, double score) {
		this.value = value;
		this.score = score;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return value+", score: "+score;
	}
	
	@Override
	public boolean equals(Object n) {
		if (value.equals(((Node) n).getValue())) {
			return true;
		}
		return false;
	}

	public int compareTo(Node node) {
		if (this.score < node.getScore()) {
			return 1;
		} else if (this.score > node.getScore()) {
			return -1;
		}
		
		return 0;
	}

}

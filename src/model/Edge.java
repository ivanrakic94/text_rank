package model;

public class Edge {
	
	private double weight;

	public Edge(int weight) {
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return weight+"";
	}

}

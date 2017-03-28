package model;

public class Edge {
	
	private double distance;

	public Edge(int distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return distance+"";
	}

}

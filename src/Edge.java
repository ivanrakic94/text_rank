
public class Edge {
	
	private int distance;

	public Edge(int distance) {
		super();
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return distance+"";
	}

}

package mapEnums;

public enum Terrain {
	Grass(1), 
	Water(10000), 
	Mountain(2);

	private final int cost;

	Terrain(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}
}

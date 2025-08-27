package moveManager.algorithm;

import mapData.Position;




public class Node implements Comparable<Node>{
    private final Position position;
    private int distance;

    public Node(Position position, int distance) {
        this.position = position;
        this.distance = distance;
    }

    public Position getPosition() {
        return position;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }
}

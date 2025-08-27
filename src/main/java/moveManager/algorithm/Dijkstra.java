package moveManager.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import moveManager.Movement;


public class Dijkstra {
    private final HashMap<Position, Field> map;

    public Dijkstra(HashMap<Position, Field> map) {
        this.map = map;
    }

    public Map<Position, List<Position>> calculateShortestPathsWithSteps(Position start) {
        Map<Position, Integer> distances = new HashMap<>();
        Map<Position, Position> previous = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        Set<Position> visited = new HashSet<>();

        for (Position pos : map.keySet()) {
            distances.put(pos, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        priorityQueue.add(new Node(start, 0));

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            Position current = currentNode.getPosition();

            if (visited.contains(current)) continue;

            visited.add(current);

            for (Node neighbor : getNeighbors(current)) {
                Position neighborPos = neighbor.getPosition();
                int newDist = distances.get(current) + neighbor.getDistance();

                if (newDist < distances.get(neighborPos)) {
                    distances.put(neighborPos, newDist);
                    previous.put(neighborPos, current);
                    priorityQueue.add(new Node(neighborPos, newDist));
                }
            }
        }

       
        Map<Position, List<Position>> shortestPaths = new HashMap<>();
        for (Position target : distances.keySet()) {
            if (distances.get(target) < Integer.MAX_VALUE) {
                List<Position> path = reconstructPath(target, previous);
                shortestPaths.put(target, path);
            }
        }

        return shortestPaths;
    }

    private List<Position> reconstructPath(Position target, Map<Position, Position> previous) {
        List<Position> path = new ArrayList<>();
        for (Position at = target; at != null; at = previous.get(at)) {
            path.add(0, at); 
        }
        return path;
    }

    private List<Node> getNeighbors(Position current) {
        List<Node> neighbors = new ArrayList<>();

        for (Movement movement : Movement.values()) {
            Position neighborPos = movement.apply(current);
            if (map.containsKey(neighborPos) && !map.get(neighborPos).getTerrain().equals(Terrain.Water)) {
                Field neighborField = map.get(neighborPos);
                int moveCost = calculateMoveCost(map.get(current), neighborField);
                neighbors.add(new Node(neighborPos, moveCost));
            }
        }

        return neighbors;
    }

    private int calculateMoveCost(Field from, Field to) {
        if (to.getTerrain() == Terrain.Water) return Integer.MAX_VALUE; 
        return from.getTerrain().getCost() + to.getTerrain().getCost();
    }
}

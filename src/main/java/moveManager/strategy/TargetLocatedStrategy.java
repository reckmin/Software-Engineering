package moveManager.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import exceptions.TargetNotSetException;
import mapData.Field;
import mapData.Position;
import moveManager.algorithm.Dijkstra;

public class TargetLocatedStrategy implements MovementStrategy {
	private final HashMap<Position, Field> mapOfFields;
	private final Dijkstra dijkstra;

	private Optional<Position> target;
	private List<Map.Entry<Position, Field>> path;
	
	public TargetLocatedStrategy(HashMap<Position, Field> mapOfFields) {
		this.mapOfFields = mapOfFields;
		this.dijkstra = new Dijkstra(mapOfFields);
	
		this.path = new ArrayList<>();
	}
 
	public void setTarget(Optional<Position> target) {
		this.target = target; 
	}
 

	@Override
	public List<Entry<Position, Field>> calculatePath(Position start) {
		if(!path.isEmpty()) {
			if(path.get(0).getKey().equals(start)) {
				path.removeFirst();
			}
			return path;
		}
		
		if(target.isEmpty()) throw new TargetNotSetException("Target is not determined!");
		path.clear();

		Map<Position, List<Position>> shortestPaths = dijkstra.calculateShortestPathsWithSteps(start);
		List<Position> steps = shortestPaths.getOrDefault(target.get(), new ArrayList<>());

		for (Position step : steps) {
			Field field = mapOfFields.get(step);
			path.add(Map.entry(step, field));
		}
		
		if(path.get(0).getKey().equals(start)) {
			path.removeFirst();
		}
		return path;
	}

}

package moveManager.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import moveManager.algorithm.Dijkstra;

// calculatePath  Created by prompt in ChatGPT 4.0 

public class TargetNotLocatedStrategy implements MovementStrategy {

	private static final Logger logger = LoggerFactory.getLogger(TargetNotLocatedStrategy.class);
	private HashMap<Position, Field> mapOfFields;
	private final HashMap<Position, Field> allGrassFields = new HashMap<Position, Field>();
	private final Dijkstra dijkstra;

	private List<Map.Entry<Position, Field>> pathOfFields = new ArrayList<>();

	public TargetNotLocatedStrategy(HashMap<Position, Field> mapOfFields) {
		this.mapOfFields = mapOfFields;
		this.dijkstra = new Dijkstra(mapOfFields);
		determineAllallGrassFields(mapOfFields);
	}

	@Override
	public List<Entry<Position, Field>> calculatePath(Position start) {
		if (!pathOfFields.isEmpty()) {
			if (pathOfFields.get(0).getKey().equals(start)) {
				pathOfFields.removeFirst();
			}
			return pathOfFields;
		}
 
		List<Map.Entry<Position, Field>> path = new ArrayList<>();
		if (allGrassFields.isEmpty())
			return path;

		Set<Position> visited = new HashSet<>();
		Position current = start;

		logger.info("mapfOfFields {}", mapOfFields.keySet().toString());
		logger.info("all grass fields {}", allGrassFields.keySet().toString());

		while (visited.size() < allGrassFields.size()) {
			Position next = null;
			int minDist = Integer.MAX_VALUE;

			Map<Position, List<Position>> shortestPaths = dijkstra.calculateShortestPathsWithSteps(current);
			for (Position target : allGrassFields.keySet()) {
				if (!visited.contains(target) && shortestPaths.containsKey(target)) {
					List<Position> candidatePath = shortestPaths.get(target);
					int candidateDist = candidatePath.size();

					if (candidateDist < minDist) {
						next = target;
						minDist = candidateDist;
					}
				}
			}

			if (next != null) {
				List<Position> stepsToNext = shortestPaths.get(next);
				for (Position step : stepsToNext) {

					if (path.isEmpty() || !path.get(path.size() - 1).getKey().equals(step)) {
						Field field = mapOfFields.get(step);
						path.add(Map.entry(step, field));
					}
				}
				visited.add(next);
				current = next;
			} else {
				break;
			}
		}

		if (path.get(0).getKey().equals(start)) {
			path.removeFirst();
		}
		pathOfFields = path;
		
		logger.debug("Visited positions: {}", visited);
		logger.debug("Remaining grass fields: {}", allGrassFields.keySet().stream()
		        .filter(pos -> !visited.contains(pos))
		        .toList());
		
		return path;
	}

	private void determineAllallGrassFields(HashMap<Position, Field> fields) {
		if (allGrassFields.isEmpty()) {
			for (Map.Entry<Position, Field> entry : fields.entrySet()) {
				Position position = entry.getKey();
				Field field = entry.getValue();

				if (field.getTerrain().equals(Terrain.Grass)) {
					allGrassFields.put(position, field);
				}
			}
		}
	}

}

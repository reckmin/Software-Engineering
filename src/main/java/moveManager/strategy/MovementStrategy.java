package moveManager.strategy;


import java.util.List;
import java.util.Map;
import mapData.Field;
import mapData.Position;

public interface MovementStrategy {
    
    public List<Map.Entry<Position, Field>> calculatePath(Position position);

}

import java.util.*;
public interface Location extends Entity{
    Optional<Entity> findNearest(WorldModel world, Point pos, List<Class> kinds);

    Optional<Entity> nearestEntity(List<Entity> entities, Point pos);

    Point nextPosition(WorldModel world, Point destPos);

    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}

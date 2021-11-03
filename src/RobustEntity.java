import processing.core.PImage;

import java.util.*;
public abstract class RobustEntity extends ActivityEntity{


    public RobustEntity(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public Optional<Entity> findNearest(WorldModel world, Point pos, List<Class> kinds){
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : world.getEntities()) {
                if (entity.getClass() == kind){
                    ofType.add(entity);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos){
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity entity : entities) {
                int otherDistance = entity.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = entity;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    abstract Point nextPosition(WorldModel world, Point destPos);

    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}

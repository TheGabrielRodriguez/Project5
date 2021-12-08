import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Gyrados extends CreateEntity{

    public PathingStrategy path = new AStarPathingStrategy();

    public Gyrados(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class, PokeTree.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
        else{
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }

    }

    public Point nextPosition( //entity  //check creates and order of variables in the parameters, then check constructors
                               WorldModel world, Point destPos)
    {
        List<Point> subsequentPath;
        subsequentPath = path.computePath(this.getPosition(), destPos, point -> world.withinBounds(point) && (!world.isOccupied(point) ||(world.getOccupancyCell(point).getClass() == Stump.class ||(world.getOccupancyCell(point).getClass() == Fire.class))), (p1,p2) -> p1.adjacent(p2), PathingStrategy.CARDINAL_NEIGHBORS);
        if (subsequentPath.size() == 0)  //if no path the computePath call will return 0 and the size of the List of Points will be zero; if so return current position
            return getPosition();
        return subsequentPath.get(0);
    }



    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            ((Green)target).deductHealth(1);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());
            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}


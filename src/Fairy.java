import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Fairy extends RobustEntity
{
    public PathingStrategy path = new AStarPathingStrategy();

    public Fairy(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public void executeActivity(  //Entity  //Execute* are all Entity, make non static get rid of parameter Entity as well
                                  // look at which one we are dotting the most, using the objects (classes) data
                                  WorldModel world,  // has to be one of these 4 parameters
                                  // find whose data we are using
                                  ImageStore imageStore,
                                  EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                this.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                ActivityEntity sapling = (ActivityEntity) Factory.createSapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(world.getSaplingKey()));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }




    public Point nextPosition(WorldModel world, Point destPos)
    {
        List<Point> subsequentPath;
        subsequentPath = path.computePath(this.getPosition(), destPos, point -> world.withinBounds(point) && !world.isOccupied(point), (p1,p2) -> p1.adjacent(p2), PathingStrategy.CARDINAL_NEIGHBORS);
        if (subsequentPath.size() == 0)  //if no path the computePath call will return 0 and the size of the List of Points will be zero; if so return current position
            return getPosition();
        return subsequentPath.get(0);   //return first element0 in List of Points
    }


    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

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




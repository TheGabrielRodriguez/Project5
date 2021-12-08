import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeNotFull extends RobustEntity
{
    private final int resourceLimit;
    private int resourceCount;
    public PathingStrategy path = new AStarPathingStrategy();

    public DudeNotFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }





    private boolean transformNotFull( // entity

                                     WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Factory.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.resourceLimit,
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((DudeFull) miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }




    public Point nextPosition( //entity  //check creates and order of variables in the parameters, then check constructors
                                          WorldModel world, Point destPos)
    {
        List<Point> subsequentPath;
        subsequentPath = path.computePath(this.getPosition(), destPos, point -> world.withinBounds(point) && (!world.isOccupied(point) ||(world.getOccupancyCell(point).getClass() == Stump.class)), (p1,p2) -> p1.adjacent(p2), PathingStrategy.CARDINAL_NEIGHBORS);
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
            this.resourceCount += 1;
            ((Green)target).deductHealth(1);

            return true;
        }
        else {
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




import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeFull extends RobustEntity
{
    private final int resourceLimit;
    public PathingStrategy path = new AStarPathingStrategy();

    public DudeFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }





    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = this.findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this,nextPos);
            }
            return false;
        }
    }




    private void transformFull( // use entity data a lot, gets dotted into a lot
                                       //make all instance variables private, purposefully leave things in function if they belong
                                      // move function, make non static, get rid of parameter, make a getter
                                      WorldModel world,
                                      EventScheduler scheduler,
                                      ImageStore imageStore)
    {
        Entity miner = Factory.createDudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.resourceLimit,
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((DudeNotFull)miner).scheduleActions(scheduler, world, imageStore);
    }



    public Point nextPosition( //rename to just nextposition as already in dudefull
                                          WorldModel world, Point destPos)
    {
        List<Point> subsequentPath;
        subsequentPath = path.computePath(this.getPosition(), destPos, point -> world.withinBounds(point) && (!world.isOccupied(point) ||(world.getOccupancyCell(point).getClass() == Stump.class)), (p1,p2) -> p1.adjacent(p2), PathingStrategy.CARDINAL_NEIGHBORS);
        if (subsequentPath.size() == 0)  //if no path the computePath call will return 0 and the size of the List of Points will be zero; if so return current position
            return getPosition();
        return subsequentPath.get(0);   //return first element0 in List of Points

}}




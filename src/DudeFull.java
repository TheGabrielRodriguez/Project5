import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeFull extends RobustEntity
{
    private final int resourceLimit;

    public DudeFull(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int resourceLimit) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }





    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = this.findNearest(world, super.getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(target,nextPos);
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
        Entity miner = Factory.createDudeFull(super.getId(),
                super.getPosition(), super.getActionPeriod(),
                super.getAnimationPeriod(),
                this.resourceLimit,
                super.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((RobustEntity)miner).scheduleActions(scheduler, world, imageStore);
    }



    public Point nextPosition( //rename to just nextposition as already in dudefull
                                          WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }
        return newPos;
    }

}




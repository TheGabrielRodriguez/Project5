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
                    super.getActionPeriod());
        }
    }





    private boolean transformNotFull( // entity

                                     WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Factory.createDudeNotFull(super.getId(),
                    super.getPosition(), super.getActionPeriod(),
                    super.getAnimationPeriod(),
                    this.resourceLimit,
                    super.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            this.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }




    public Point nextPosition( //entity
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



    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition())) {
            this.resourceCount += 1;
            ((Green)target).deductHealth(1);

            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
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




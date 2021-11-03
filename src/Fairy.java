import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Fairy extends RobustEntity
{
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
                Fairy sapling = (Fairy) Factory.createSapling("sapling_" + super.getId(), tgtPos,
                        imageStore.getImageList(world.getSaplingKey()));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
    }






    public Point nextPosition( //entity bc moving entity data a lot, using specific fairies data a lot so entity and not worldmodel
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
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

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




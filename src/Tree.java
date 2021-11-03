import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Tree extends Green
{

    private static final String STUMP_KEY = "stump";

    public Tree(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }


    public boolean transformTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
        {
            if (super.getHealth() <= 0) {
                Tree stump = (Tree) Factory.createStump(super.getId(),
                        super.getPosition(),
                        imageStore.getImageList(STUMP_KEY));

                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(stump);
                stump.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
        }
    @Override
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) { return transformTree(world, scheduler, imageStore); }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

}




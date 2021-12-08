import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Tree extends Green
{

    private static final String STUMP_KEY = "stump";
    private static final String POKETREE_KEY = "Tree";

    public Tree(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }


    private boolean transformTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
        {
            if (this.getHealth() <= 0) {
                Entity stump = Factory.createStump(this.getId(),
                        this.getPosition(),
                        imageStore.getImageList(STUMP_KEY));

                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(stump);
                return true;
            }

            return false;
        }
    public void transformToPokeTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {

        Entity poketree = Factory.createPokeTree(this.getId(),
                this.getPosition(),
                this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getHealth(),
                imageStore.getImageList(POKETREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(poketree);

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
                    this.getActionPeriod());
        }
    }

}




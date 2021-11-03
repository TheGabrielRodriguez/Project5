import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling extends Green {
    private final int healthLimit;


    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;


    private static final String STUMP_KEY = "stump";

    public Sapling(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health, int healthLimit) {
        super(id, position, images, animationPeriod, actionPeriod, health);
        this.healthLimit = healthLimit;
    }


    //getters;


    public int getHealthLimit() {
        return healthLimit;
    }


    //setters;



    public boolean transformSapling(
            Sapling entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (super.getHealth() <= 0) {
            Sapling stump = (Sapling) Factory.createStump(super.getId(),
                    super.getPosition(),
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        } else if (super.getHealth() >= this.getHealthLimit()) {
            Sapling tree = (Sapling) Factory.createTree("tree_" + super.getId(),
                    super.getPosition(),
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList(world.getTreeKey()));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }



    @Override
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {return transformSapling(this, world, scheduler, imageStore); }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        super.setHealth();
        if (!transformPlant(world, scheduler, imageStore))  //Refer statically until move transformplant to entity then change based on where transformplant goes
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }
}





import processing.core.PImage;

import java.util.*;

public class Factory {

    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time

    public static Action createAnimationAction(Entity entity, int repeatCount) { // Action
        return new Animation(entity,
                repeatCount);
    }

    public static Action createActivityAction(  //
                                                Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public static Entity createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House(id, position, images);
    }

    public static Entity createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images,
                animationPeriod);
    }

    public static Entity createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, images,
                animationPeriod, actionPeriod, health);
    }

    public static Entity createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Entity createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public static Entity createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images,
                animationPeriod, actionPeriod);
    }

    // need resource count, though it always starts at 0
    public static Entity createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull(id, position, images, animationPeriod, actionPeriod,resourceLimit,
                0);
    }

    // don't technically need resource count ... full
    public static Entity createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, position, images, animationPeriod,
                actionPeriod, resourceLimit);
    }
    public static Entity createCharizard(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images) {
        return new Charizard(id, position, images, animationPeriod,
                actionPeriod);
    }

    public static Entity createPokeTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new PokeTree(id, position, images,
                animationPeriod, actionPeriod, health);
    }

}

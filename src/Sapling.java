import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling implements Entity, Green, RobustEntity,AnimateEntity
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;
    private final int healthLimit;
    private int health;

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;


    private static final String STUMP_KEY = "stump";


    public Sapling(

            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;

    }


    //getters;

    public String getId(){
        return id;
    }
    public Point getPosition(){
        return position;
    }
    public int getHealth(){
        return health;
    }
    public int getHealthLimit(){
        return healthLimit;
    }

    //setters;
    public void setPosition(Point position){
        this.position = position;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!transformPlant(world, scheduler, imageStore))  //Refer statically until move transformplant to entity then change based on where transformplant goes
        {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }



    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Functions.createAnimationAction(this, 0),
                getAnimationPeriod());


        }




    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);

        }




    private boolean transformSapling(
            Sapling entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Sapling stump = (Sapling) Functions.createStump(entity.id,
                    entity.position,
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (this.getHealth()>= this.getHealthLimit())
        {
            Sapling tree = (Sapling) Functions.createTree("tree_" + entity.id,
                    entity.position,
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



    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage(){
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }



    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {return transformSapling(this, world, scheduler, imageStore); }
    public List<PImage> getImages(){ return images; }
    public int getImageIndex(){ return imageIndex; }
    public int getActionPeriod() { return actionPeriod; }
}




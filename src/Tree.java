import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Tree implements Entity, Green, AnimateEntity, RobustEntity
{

    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;
    private int health;


    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;


    private static final String STUMP_KEY = "stump";


    public Tree(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;

    }


    //getters;
    public String getId(){
        return id;
    }
    public Point getPosition(){
        return position;
    }
    public int getHealth(){ return health; }

    //setters;
    public void setPosition(Point position){
        this.position = position;
    }




    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

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


        private boolean transformTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
        {
            if (this.health <= 0) {
                Tree stump = (Tree) Functions.createStump(this.id,
                        this.position,
                        imageStore.getImageList(STUMP_KEY));

                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(stump);
                stump.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
        }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) { return transformTree(world, scheduler, imageStore); }

    public int getAnimationPeriod() {
            return this.animationPeriod;

    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
    public List<PImage> getImages(){ return images; }
    public int getImageIndex(){ return imageIndex; }
    public int getActionPeriod() { return actionPeriod; }

}




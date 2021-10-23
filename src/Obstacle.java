import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Obstacle implements Entity, AnimateEntity
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;
    private final int animationPeriod;
    private final int actionPeriod;




    public Obstacle(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod
            )
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
        this.actionPeriod = actionPeriod;



    }


    //getters;
    public String getId(){
        return id;
    }
    public Point getPosition(){
        return position;
    }

    //setters;
    public void setPosition(Point position){
        this.position = position;
    }

    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);

        }
    public int getAnimationPeriod() {//never called? but pretty certain this should be in here given that the createObstacle in Functions initializes animationperiod;
        return this.animationPeriod;
    }
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public List<PImage> getImages(){ return images; }
    public int getImageIndex(){ return imageIndex; }
    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                actionPeriod);
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                getAnimationPeriod());
    }
    }




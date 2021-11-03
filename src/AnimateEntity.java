import processing.core.PImage;

import java.util.List;

public abstract class AnimateEntity extends Entity{
    private int animationPeriod;

    public AnimateEntity(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }


    public int getAnimationPeriod(){
        return this.animationPeriod;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                getAnimationPeriod());
    }
}


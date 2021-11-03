import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends AnimateEntity {
    private int actionPeriod;

    public ActivityEntity(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public void nextImage(){ super.setNextImage(); }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        super.scheduleActions(scheduler,world, imageStore);
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.getActionPeriod());

    }
    public int getActionPeriod(){return actionPeriod; }
    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}

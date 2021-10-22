import processing.core.PImage;
public interface RobustEntity extends Entity{
    int getActionPeriod();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}

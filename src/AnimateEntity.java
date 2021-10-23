import processing.core.PImage;
public interface AnimateEntity extends Entity{
    void nextImage();
    int getAnimationPeriod();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}

import processing.core.PImage;
public interface AnimateEntity extends Entity{
    void nextImage();
    int getAnimationPeriod();
}

import processing.core.PImage;

import java.util.List;

public abstract class CreateEntity extends RobustEntity{
    public CreateEntity(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }
}

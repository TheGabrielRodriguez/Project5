import processing.core.PImage;

import java.util.List;

public abstract class Green extends ActivityEntity {
    private int health;

    public Green(String id, Point position, List<PImage> images, int animationPeriod, int actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }


    abstract boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
    public void deductHealth(int given){
        health = health - given;
    }
    public int getHealth(){
        return health;
    }
    public void setHealth(){health ++; }
    //not have animationperiod and action in constructor, uml
}

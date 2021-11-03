import java.util.List;
import processing.core.PImage;

public abstract class Entity {

    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;


    public Entity(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
    }
    public PImage getCurrentImage(){return images.get(this.imageIndex);}
    public String getId(){return this.id;}
    public List<PImage> getImages(){return images;}
    public Point getPosition(){return position;}
    public void setPosition(Point position){
        this.position = position;
    }
    public void setNextImage(){this.imageIndex = (this.imageIndex + 1) % this.images.size();}
}

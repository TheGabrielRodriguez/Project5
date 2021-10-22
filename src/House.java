import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class House implements Entity
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private final int imageIndex;


    public House(
            String id,
            Point position,
            List<PImage> images
            )
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


    //getters;
    public String getId(){
        return id;
    }
    public Point getPosition(){
        return position;
    }
    public PImage getCurrentImage() { return images.get(this.imageIndex); }
    public List<PImage> getImages(){ return images; }
    public int getImageIndex(){ return imageIndex; }

    //setters;
    public void setPosition(Point position){
        this.position = position;
    }


}




import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Stump implements Entity
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private final int imageIndex;



    public Stump(
            String id,
            Point position,
            List<PImage> images)
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


    //setters;
    public void setPosition(Point position){
        this.position = position;
    }




    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);

        }

    public List<PImage> getImages(){ return images; }
    public int getImageIndex(){ return imageIndex; }



}




import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    public String id;
    public List<PImage> images;
    public int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public  PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
        return images.get(this.imageIndex);

    }

}

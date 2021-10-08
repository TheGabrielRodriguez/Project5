import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    private final String id;
    private final List<PImage> images;
    private int imageIndex;  //never called in background and initialized in entity, make private and getter or leave public?

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
        return images.get(this.imageIndex);

    }

}

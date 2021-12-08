import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public class Fire extends Background
{

    public Fire(String id, List<PImage> images) {
        super(id, images);
    }

    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
        return super.getCurrentImage();
    }
}

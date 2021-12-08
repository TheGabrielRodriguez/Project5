import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public class Fire extends AnimateEntity
{

    public Fire(String id, Point position, List<PImage> images, int animationPeriod) {
        super(id, position, images,animationPeriod);
    }

}

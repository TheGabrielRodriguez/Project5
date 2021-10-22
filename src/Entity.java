import java.util.List;
import processing.core.PImage;

public interface Entity {
    PImage getCurrentImage();
    String getId();
    List<PImage> getImages();
    Point getPosition();
    void setPosition(Point position);
    int getImageIndex();
}

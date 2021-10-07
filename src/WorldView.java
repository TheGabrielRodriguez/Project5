import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView
{
    public PApplet screen;
    public WorldModel world;
    public int tileWidth;
    public int tileHeight;
    public Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public void shiftView(int colDelta, int rowDelta) { //worldView, using worldviews data a lot
        int newCol = Functions.clamp(this.viewport.col + colDelta, 0,
                this.world.numCols - this.viewport.numCols);
        int newRow = Functions.clamp(this.viewport.row + rowDelta, 0,
                this.world.numRows - this.viewport.numRows);

        this.viewport.shift(newCol, newRow);
    }

    public void drawBackground() {  //Worldview, uses a lot of data from worldview objects
        for (int row = 0; row < this.viewport.numRows; row++) {
            for (int col = 0; col < this.viewport.numCols; col++) {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image =
                        this.world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    public void drawEntities(){  // worldview, using much more of worldview data than entities object(Singular isntance)
        for (Entity entity : this.world.entities) {
            Point pos = entity.position;

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
                this.screen.image(entity.getCurrentImage(),  //trouble with this one
                        viewPoint.x * this.tileWidth,
                        viewPoint.y * this.tileHeight);
            }
        }
    }

    public void drawViewport() { //worldview maybe? uses worldview data but might be background or something
        this.drawBackground();
        this.drawEntities();
    }
}

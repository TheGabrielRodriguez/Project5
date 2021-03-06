import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);

        Optional<Entity> entityOptional = world.getOccupant(pressed);

        String tl = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() - 1) + " " + (pressed.getY() - 1);
        String t = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + (pressed.getY() - 1);
        String tr = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() + 1) + " " + (pressed.getY() - 1);
        String l = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() - 1) + " " + pressed.getY();
        String r = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() + 1) + " " + pressed.getY();
        String bl = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() - 1) + " " + (pressed.getY() + 1);
        String b = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + (pressed.getY() + 1);
        String br = "bg bg_" + pressed.getX() + "_" + pressed.getY() + " " + (pressed.getX() + 1) + " " + (pressed.getY() + 1);

        if (entityOptional.isEmpty()) {
            String line = "charizard charizard_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + pressed.getY() + " 784 100";
            String[] properties = line.split("\\s");
            world.parseCharizard(properties, imageStore, scheduler);

            //top left
            String[] tlp = tl.split("\\s");
            world.parseFire(tlp, imageStore);

            //top
            String[] tp = t.split("\\s");
            world.parseFire(tp, imageStore);

            //top right
            String[] trp = tr.split("\\s");
            world.parseFire(trp, imageStore);

            //left
            String[] lp = l.split("\\s");
            world.parseFire(lp, imageStore);

            //right
            String[] rp = r.split("\\s");
            world.parseFire(rp, imageStore);

            //bottom left
            String[] blp = bl.split("\\s");
            world.parseFire(blp, imageStore);

            //bottom
            String[] bp = b.split("\\s");
            world.parseFire(bp, imageStore);

            //bottom right
            String[] brp = br.split("\\s");
            world.parseFire(brp, imageStore);
        }

        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
//            System.out.println(entity.getId() + ": " + entity.getClass());
            if  (entity instanceof Green){
                if(entity.getClass() == Tree.class){
                    world.removeEntity(entity);
                    scheduler.unscheduleAllEvents(entity);
                    String line = "Tree Tree_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + pressed.getY() + " 501 1289 2";
                    String[] properties = line.split("\\s");
                    world.parsePokeTree(properties, imageStore, scheduler);
                } else {
                    System.out.println(entity.getId() + ": " + entity.getClass() + " : " + ((Green) entity).getHealth());
                }
            }

            if (entity instanceof Charizard) {
                //top left
                String[] tlp = tl.split("\\s");
                world.parseFirt(tlp, imageStore);

                //top
                String[] tp = t.split("\\s");
                world.parseFirt(tp, imageStore);

                //top right
                String[] trp = tr.split("\\s");
                world.parseFirt(trp, imageStore);

                //left
                String[] lp = l.split("\\s");
                world.parseFirt(lp, imageStore);

                //right
                String[] rp = r.split("\\s");
                world.parseFirt(rp, imageStore);

                //bottom left
                String[] blp = bl.split("\\s");
                world.parseFirt(blp, imageStore);

                //bottom
                String[] bp = b.split("\\s");
                world.parseFirt(bp, imageStore);

                //bottom right
                String[] brp = br.split("\\s");
                world.parseFirt(brp, imageStore);
            }


            if (entity.getClass() == Obstacle.class) {
                String line = "mag mag_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + pressed.getY() + " 784 100";
                String[] properties = line.split("\\s");
                world.parseMag(properties, imageStore, scheduler);

            }
            if (entity.getClass() == Magikarp.class){
                world.removeEntity(entity);
                scheduler.unscheduleAllEvents(entity);
                String line = "Gyrados gyrados_" + pressed.getX() + "_" + pressed.getY() + " " + pressed.getX() + " " + pressed.getY() + " 784 100";
                String[] properties = line.split("\\s");
                world.parseGyrados(properties, imageStore, scheduler);
            }
        }
    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof AnimateEntity){
                ((AnimateEntity)entity).scheduleActions(scheduler, world, imageStore);
                }

            }
        }


    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}

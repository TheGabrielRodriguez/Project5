import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {  // make init vars private and then make getters/make correction to errors as they come up
    private final int numRows;
    private final int numCols;
    private final Background background[][];



    private final Entity occupancy[][];
    private final Set<Entity> entities;

    //getters
    public int getNumRows(){
        return numRows;
    }
    public int getNumCols(){
        return numCols;
    }

    public Set<Entity> getEntities(){
        return entities;
    }
    public String getTreeKey(){
        return TREE_KEY;
    }
    public String getSaplingKey(){
        return SAPLING_KEY;
    }

    //end of getters



    private static final int PROPERTY_KEY = 0;

    private static final String SAPLING_KEY = "sapling";

    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000;
    private static final int SAPLING_NUM_PROPERTIES = 4;
    private static final int SAPLING_ID = 1;
    private static final int SAPLING_COL = 2;
    private static final int SAPLING_ROW = 3;
    private static final int SAPLING_HEALTH = 4;



    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String FIRE_KEY = "flame";
    private static final int FIRE_NUM_PROPERTIES = 5;
    private static final int FIRE_ANIMATION_PERIOD = 4;
    private static final int FIRE_ID = 1;
    private static final int FIRE_COL = 2;
    private static final int FIRE_ROW = 3;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 5;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;
    private static final int OBSTACLE_ANIMATION_PERIOD = 4;

    private static final String MAGIKARP_KEY = "mag";
    private static final int MAGIKARP_NUM_PROPERTIES = 6;
    private static final int MAGIKARP_ID = 1;
    private static final int MAGIKARP_COL = 2;
    private static final int MAGIKARP_ROW = 3;
    private static final int MAGIKARP_ACTION_PERIOD = 4;
    private static final int MAGIKARP_ANIMATION_PERIOD = 5;

    private static final String GYRADOS_KEY = "Gyrados";
    private static final int GYRADOS_NUM_PROPERTIES = 6;
    private static final int GYRADOS_ID = 1;
    private static final int GYRADOS_COL = 2;
    private static final int GYRADOS_ROW = 3;
    private static final int GYRADOS_ACTION_PERIOD = 4;
    private static final int GYRADOS_ANIMATION_PERIOD = 5;

    private static final String DUDE_KEY = "dude";
    private static final int DUDE_NUM_PROPERTIES = 7;
    private static final int DUDE_ID = 1;
    private static final int DUDE_COL = 2;
    private static final int DUDE_ROW = 3;
    private static final int DUDE_LIMIT = 4;
    private static final int DUDE_ACTION_PERIOD = 5;
    private static final int DUDE_ANIMATION_PERIOD = 6;

    private static final String CHARIZARD_KEY = "charizard";
    private static final int CHARIZARD_NUM_PROPERTIES = 6;
    private static final int CHARIZARD_ID = 1;
    private static final int CHARIZARD_COL = 2;
    private static final int CHARIZARD_ROW = 3;
    private static final int CHARIZARD_ACTION_PERIOD = 4;
    private static final int CHARIZARD_ANIMATION_PERIOD = 5;

    private static final String HOUSE_KEY = "house";
    private static final int HOUSE_NUM_PROPERTIES = 4;
    private static final int HOUSE_ID = 1;
    private static final int HOUSE_COL = 2;
    private static final int HOUSE_ROW = 3;

    private static final String FAIRY_KEY = "fairy";
    private static final int FAIRY_NUM_PROPERTIES = 6;
    private static final int FAIRY_ID = 1;
    private static final int FAIRY_COL = 2;
    private static final int FAIRY_ROW = 3;
    private static final int FAIRY_ANIMATION_PERIOD = 4;
    private static final int FAIRY_ACTION_PERIOD = 5;



    private static final String TREE_KEY = "tree";
    private static final int TREE_NUM_PROPERTIES = 7;
    private static final int TREE_ID = 1;
    private static final int TREE_COL = 2;
    private static final int TREE_ROW = 3;
    private static final int TREE_ANIMATION_PERIOD = 4;
    private static final int TREE_ACTION_PERIOD = 5;
    private static final int TREE_HEALTH = 6;

//    private static final String POKETREE_KEY = "Tree";
//    private static final int POKETREE_NUM_PROPERTIES = 7;
//    private static final int POKETREE_ID = 1;
//    private static final int POKETREE_COL = 2;
//    private static final int POKETREE_ROW = 3;
//    private static final int POKETREE_ANIMATION_PERIOD = 4;
//    private static final int POKETREE_ACTION_PERIOD = 5;
//    private static final int POKETREE_HEALTH = 6;






    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public boolean withinBounds(Point pos) {  // worldmodel
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }

    private void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }

    private void setBackground(Point pos, Background background) {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

//    private void setFireCell(Point pos, Fire background) {
//        this.background[pos.getY()][pos.getX()] = background;
//    }
//
//    private void setFire(Point pos, Fire background) {
//        if (this.withinBounds(pos)) {
//            this.setFireCell(pos, background);
//        }
//    }


    private Background getBackgroundCell(Point pos) { //world
        return this.background[pos.getY()][pos.getX()];
    }



    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());  //create instance of entity or background in order to access getCurrentImage?
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && getOccupancyCell(pos) != null;
    }



    private void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }



    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public void tryAddEntity(Entity entity) {
        if (entity.getClass() == Magikarp.class){
            addEntity(entity);
        } else if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }else {
            addEntity(entity);
        }
    }

    public void addEntity(Entity entity) { //worldmodel holds grid of world and where everything is located
        // entity is a single dude, so put into worldmodel bc that is where we add and remove things
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public Entity getOccupancyCell(Point pos) { //world
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public void load(
            Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private boolean processLine(
            String line, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return this.parseBackground(properties, imageStore);
                case FIRE_KEY:
                    return this.parseFire(properties, imageStore);
                case DUDE_KEY:
                    return this.parseDude(properties, imageStore);
                case OBSTACLE_KEY:
                    return this.parseObstacle(properties, imageStore);
                case FAIRY_KEY:
                    return this.parseFairy(properties, imageStore);
                case HOUSE_KEY:
                    return this.parseHouse(properties, imageStore);
                case TREE_KEY:
                    return this.parseTree(properties, imageStore);
                case SAPLING_KEY:
                    return this.parseSapling(properties, imageStore);
            }
        }

        return false;
    }

    private boolean parseBackground(String[] properties, ImageStore imageStore) {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            this.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }
    public boolean parseFire(String[] properties, ImageStore imageStore) {
        if (properties.length == FIRE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FIRE_COL]),
                    Integer.parseInt(properties[FIRE_ROW]));
            Entity entity = Factory.createObstacle(properties[FIRE_ID], pt,
                    Integer.parseInt(properties[FIRE_ANIMATION_PERIOD]),
                    imageStore.getImageList(FIRE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseSapling(String[] properties, ImageStore imageStore) {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            Entity entity = new Sapling(id, pt, imageStore.getImageList(SAPLING_KEY),
                    SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, SAPLING_HEALTH_LIMIT);
            this.tryAddEntity(entity);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;


    }
    public boolean parseDude(String[] properties, ImageStore imageStore)
    {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            Entity entity = Factory.createDudeNotFull(properties[DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    imageStore.getImageList(DUDE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }


    private boolean parseFairy(String[] properties, ImageStore imageStore)
    {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            Entity entity = Factory.createFairy(properties[FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(FAIRY_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }


    private boolean parseTree(String[] properties, ImageStore imageStore)
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Entity entity = Factory.createTree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList(TREE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    private boolean parseObstacle(String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Factory.createObstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseCharizard(String[] properties, ImageStore imageStore, EventScheduler scheduler)
    {
        if (properties.length == CHARIZARD_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[CHARIZARD_COL]),
                    Integer.parseInt(properties[CHARIZARD_ROW]));
            Entity entity = Factory.createCharizard(properties[CHARIZARD_ID],
                    pt,
                    Integer.parseInt(properties[CHARIZARD_ACTION_PERIOD]),
                    Integer.parseInt(properties[CHARIZARD_ANIMATION_PERIOD]),
                    imageStore.getImageList(CHARIZARD_KEY));
            this.tryAddEntity(entity);
            ((Charizard) entity).executeActivity(this, imageStore, scheduler);
            scheduler.scheduleEvent(entity,
                    Factory.createAnimationAction(entity, 0),
                    ((AnimateEntity) entity).getAnimationPeriod());
        }

        return properties.length == CHARIZARD_NUM_PROPERTIES;
    }

    public boolean parseMag(String[] properties, ImageStore imageStore, EventScheduler scheduler)
    {
        if (properties.length == MAGIKARP_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[MAGIKARP_COL]),
                    Integer.parseInt(properties[MAGIKARP_ROW]));
            Entity entity = Factory.createMagikarp(properties[MAGIKARP_ID], pt,
                    Integer.parseInt(properties[MAGIKARP_ACTION_PERIOD]),
                    Integer.parseInt(properties[MAGIKARP_ANIMATION_PERIOD]),
                    imageStore.getImageList(MAGIKARP_KEY));
            this.tryAddEntity(entity);
            scheduler.scheduleEvent(entity,
                    Factory.createAnimationAction(entity, 0),
                    ((AnimateEntity) entity).getAnimationPeriod());
        }

        return properties.length == MAGIKARP_NUM_PROPERTIES;
    }

    public boolean parseGyrados(String[] properties, ImageStore imageStore, EventScheduler scheduler)
    {
        if (properties.length == GYRADOS_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[GYRADOS_COL]),
                    Integer.parseInt(properties[GYRADOS_ROW]));
            Entity entity = Factory.createGyrados(properties[GYRADOS_ID], pt,
                    Integer.parseInt(properties[GYRADOS_ACTION_PERIOD]),
                    Integer.parseInt(properties[GYRADOS_ANIMATION_PERIOD]),
                    imageStore.getImageList(GYRADOS_KEY));
            this.tryAddEntity(entity);
            scheduler.scheduleEvent(entity,
                    Factory.createAnimationAction(entity, 0),
                    ((AnimateEntity) entity).getAnimationPeriod());
        }

        return properties.length == MAGIKARP_NUM_PROPERTIES;
    }

    private boolean parseHouse(String[] properties, ImageStore imageStore)
    {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = Factory.createHouse(properties[HOUSE_ID], pt,
                    imageStore.getImageList(HOUSE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }

    private void removeEntityAt(Point pos) {  //World bc removing things from that
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public void moveEntity(Entity entity, Point pos) {  //Entity
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }


    public void removeEntity(Entity entity) {//Entity?
        this.removeEntityAt(entity.getPosition());
    }



}
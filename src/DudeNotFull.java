import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeNotFull implements Entity, RobustEntity,AnimateEntity,Location
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;
    

    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod
            )
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;


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






    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                findNearest(world, this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }



    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Factory.createAnimationAction(this, 0),
                this.animationPeriod);

        }

    private boolean transformNotFull( // entity

                                     WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Factory.createDudeNotFull(this.id,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.resourceLimit,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            this.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }






    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);
        }





    public Point nextPosition( //entity
                                          WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;

    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition())) {
            this.resourceCount += 1;
            ((Green)target).deducteHealth(1);

            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }



    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity entity : entities) {
                int otherDistance = this.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = entity;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(WorldModel world, Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : world.getEntities()) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }

    public int getActionPeriod() { return actionPeriod; }
    public List<PImage> getImages() { return images; }
    public int getImageIndex() { return imageIndex; }

}




import processing.core.PImage;

import java.util.*;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeFull implements Entity, AnimateEntity,RobustEntity,Location
{
    private final String id;
    private Point position; //make a getter, then make a setter if it gets set
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private final int actionPeriod;
    private final int animationPeriod;



    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
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


    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity entity : entities) {
                int otherDistance = entity.getPosition().distanceSquared(pos);

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
                if (entity.getClass() == kind){
                    ofType.add(entity);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = this.findNearest(world, this.position, new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() &&
                this.moveTo(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(target,nextPos);
            }
            return false;
        }
    }



    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {           scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        this.animationPeriod);

        }


    private void transformFull( // use entity data a lot, gets dotted into a lot
                                       //make all instance variables private, purposefully leave things in function if they belong
                                      // move function, make non static, get rid of parameter, make a getter
                                      WorldModel world,
                                      EventScheduler scheduler,
                                      ImageStore imageStore)
    {
        Entity miner = Factory.createDudeFull(this.id,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.resourceLimit,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((RobustEntity)miner).scheduleActions(scheduler, world, imageStore);
    }


    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);

        }




    public Point nextPosition( //rename to just nextposition as already in dudefull
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

    public int getAnimationPeriod() { return this.animationPeriod;}

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }



    public int getResourceLimit() { return resourceLimit; }
    public int getActionPeriod() { return actionPeriod; }
    public List<PImage> getImages() { return images; }
    public int getImageIndex() { return imageIndex; }
}




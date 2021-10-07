import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Entity
{
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;
    public int health;
    public int healthLimit;

    public Entity(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }



    public void executeSaplingActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!transformPlant(world, scheduler, imageStore))  //Refer statically until move transformplant to entity then change based on where transformplant goes
        {
            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeTreeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeFairyActivity(  //Entity  //Execute* are all Entity, make non static get rid of parameter Entity as well
                                               // look at which one we are dotting the most, using the objects (classes) data
                                              WorldModel world,  // has to be one of these 4 parameters
                                              // find whose data we are using
                                              ImageStore imageStore,
                                              EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                Functions.findNearest(world, this.position, new ArrayList<>(Collections.singletonList(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (moveToFairy(this, world, fairyTarget.get(), scheduler)) {
                Entity sapling = Functions.createSapling("sapling_" + this.id, tgtPos,
                        Functions.getImageList(imageStore, Functions.SAPLING_KEY));

                Functions.addEntity(world, sapling);
                scheduleActions(sapling, scheduler, world, imageStore);
            }
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Functions.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void executeDudeNotFullActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                Functions.findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (!target.isPresent() || !moveToNotFull(this, world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeDudeFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                Functions.findNearest(world, this.position, new ArrayList<>(Arrays.asList(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && moveToFull(this, world,
                fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {
        switch (this.kind) {// if switch then focus on that classes data
            case DUDE_FULL:
                scheduler.scheduleEvent(this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                scheduler.scheduleEvent(this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case OBSTACLE:
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case FAIRY:
                scheduler.scheduleEvent(this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case SAPLING:
                scheduler.scheduleEvent(this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case TREE:
                scheduler.scheduleEvent(this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this,
                        Functions.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            default:
        }


        }
    public boolean transformNotFull( // entity

                                     WorldModel world,
                                     EventScheduler scheduler,
                                     ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Functions.createDudeFull(this.id,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.resourceLimit,
                    this.images);

            Functions.removeEntity(world, this);
            EventScheduler.unscheduleAllEvents(scheduler, this);

            Functions.addEntity(world, miner);
            this.scheduleActions(miner, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void transformFull( // use entity data a lot, gets dotted into a lot
                                       //make all instance variables private, purposefully leave things in function if they belong
                                      // move function, make non static, get rid of parameter, make a getter
                                      WorldModel world,
                                      EventScheduler scheduler,
                                      ImageStore imageStore)
    {
        Entity miner = Functions.createDudeNotFull(this.id,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.resourceLimit,
                this.images);

        Functions.removeEntity(world, this);
        EventScheduler.unscheduleAllEvents(scheduler, this);

        Functions.addEntity(world, miner);
        scheduleActions(miner, scheduler, world, imageStore);
    }

    public Point nextPositionFairy( //entity bc moving entity data a lot, using specific fairies data a lot so entity and not worldmodel
                                           WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || Functions.isOccupied(world, newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || Functions.isOccupied(world, newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public Point nextPositionDude( //entity
                                          WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || Functions.isOccupied(world, newPos) && Functions.getOccupancyCell(world, newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || Functions.isOccupied(world, newPos) &&  Functions.getOccupancyCell(world, newPos).kind != EntityKind.STUMP) {
                newPos = this.position;
            }
        }

        return newPos;
    }


    public PImage getCurrentImage() { // get rid of static and get rid of parameter
        // put it into both entity and background
            return images.get(this.imageIndex);

        }


    public boolean transformPlant(
                                          WorldModel world,
                                          EventScheduler scheduler,
                                          ImageStore imageStore)
    {
        if (this.kind == EntityKind.TREE)
        {
            return transformTree(world, scheduler, imageStore);
        }
        else if (this.kind == EntityKind.SAPLING)
        {
            return transformSapling(this, world, scheduler, imageStore);
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("transformPlant not supported for %s", this));
        }
    }

    public boolean transformTree(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.health <= 0) {
            Entity stump = Functions.createStump(this.id,
                    this.position,
                    Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, this);
            EventScheduler.unscheduleAllEvents(scheduler, this);

            Functions.addEntity(world, stump);
            scheduleActions(stump, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean transformSapling(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (entity.health <= 0) {
            Entity stump = Functions.createStump(entity.id,
                    entity.position,
                    Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, entity);
            EventScheduler.unscheduleAllEvents(scheduler, entity);

            Functions.addEntity(world, stump);
            scheduleActions(stump, scheduler, world, imageStore);

            return true;
        }
        else if (entity.health >= entity.healthLimit)
        {
            Entity tree = Functions.createTree("tree_" + entity.id,
                    entity.position,
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    Functions.getImageList(imageStore, Functions.TREE_KEY));

            Functions.removeEntity(world, entity);
            EventScheduler.unscheduleAllEvents(scheduler, entity);

            Functions.addEntity(world, tree);
            scheduleActions(tree, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public static boolean moveToFairy(
            Entity fairy,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(fairy.position, target.position)) {
            Functions.removeEntity(world, target);
            EventScheduler.unscheduleAllEvents(scheduler, target);
            return true;
        }
        else {
            Point nextPos = nextPositionFairy(fairy, world, target.position);

            if (!fairy.position.equals(nextPos)) {
                Optional<Entity> occupant = Functions.getOccupant(world, nextPos);
                if (occupant.isPresent()) {
                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
                }

                Functions.moveEntity(world, fairy, nextPos);
            }
            return false;
        }
    }

    public static boolean moveToNotFull(
            Entity dude,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(dude.position, target.position)) {
            dude.resourceCount += 1;
            target.health--;
            return true;
        }
        else {
            Point nextPos = nextPositionDude(dude, world, target.position);

            if (!dude.position.equals(nextPos)) {
                Optional<Entity> occupant = Functions.getOccupant(world, nextPos);
                if (occupant.isPresent()) {
                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
                }

                Functions.moveEntity(world, dude, nextPos);
            }
            return false;
        }
    }

    public static boolean moveToFull(
            Entity dude,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(dude.position, target.position)) {
            return true;
        }
        else {
            Point nextPos = nextPositionDude(dude, world, target.position);

            if (!dude.position.equals(nextPos)) {
                Optional<Entity> occupant = Functions.getOccupant(world, nextPos);
                if (occupant.isPresent()) {
                    EventScheduler.unscheduleAllEvents(scheduler, occupant.get());
                }

                Functions.moveEntity(world, dude, nextPos);
            }
            return false;
        }
    }

    public static Point nextPositionFairy( //entity bc moving entity data a lot, using specific fairies data a lot so entity and not worldmodel
                                           Entity entity, WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - entity.position.x);
        Point newPos = new Point(entity.position.x + horiz, entity.position.y);

        if (horiz == 0 || Functions.isOccupied(world, newPos)) {
            int vert = Integer.signum(destPos.y - entity.position.y);
            newPos = new Point(entity.position.x, entity.position.y + vert);

            if (vert == 0 || Functions.isOccupied(world, newPos)) {
                newPos = entity.position;
            }
        }

        return newPos;
    }

    public static Point nextPositionDude( //entity
                                          Entity entity, WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - entity.position.x);
        Point newPos = new Point(entity.position.x + horiz, entity.position.y);

        if (horiz == 0 || Functions.isOccupied(world, newPos) && Functions.getOccupancyCell(world, newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - entity.position.y);
            newPos = new Point(entity.position.x, entity.position.y + vert);

            if (vert == 0 || Functions.isOccupied(world, newPos) &&  Functions.getOccupancyCell(world, newPos).kind != EntityKind.STUMP) {
                newPos = entity.position;
            }
        }

        return newPos;
    }

    public int getAnimationPeriod() {
        switch (this.kind) {
            case DUDE_FULL:
            case DUDE_NOT_FULL:
            case OBSTACLE:
            case FAIRY:
            case SAPLING:
            case TREE:
                return this.animationPeriod;
          default:
              throw new UnsupportedOperationException(
                       String.format("getAnimationPeriod not supported for %s",
                               this.kind));
      }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public static Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.position.distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
}




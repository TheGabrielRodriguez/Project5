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
        if (!Functions.transformPlant(this, world, scheduler, imageStore))  //Refer statically until move transformplant to entity then change based on where transformplant goes
        {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeTreeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!Functions.transformPlant(this, world, scheduler, imageStore)) {

            Functions.scheduleEvent(scheduler, this,
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

            if (Functions.moveToFairy(this, world, fairyTarget.get(), scheduler)) {
                Entity sapling = Functions.createSapling("sapling_" + this.id, tgtPos,
                        Functions.getImageList(imageStore, Functions.SAPLING_KEY));

                Functions.addEntity(world, sapling);
                scheduleActions(sapling, scheduler, world, imageStore);
            }
        }

        Functions.scheduleEvent(scheduler, this,
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

        if (!target.isPresent() || !Functions.moveToNotFull(this, world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            Functions.scheduleEvent(scheduler, this,
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

        if (fullTarget.isPresent() && Functions.moveToFull(this, world,
                fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void scheduleActions( //entity bc we have a switch function that calls entities data

                                 Entity miner, EventScheduler scheduler,
                                 WorldModel world,
                                 ImageStore imageStore)
    {
        switch (this.kind) {// if switch then focus on that classes data
            case DUDE_FULL:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
                break;

            case DUDE_NOT_FULL:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
                break;

            case OBSTACLE:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
                break;

            case FAIRY:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
                break;

            case SAPLING:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
                break;

            case TREE:
                Functions.scheduleEvent(scheduler, this,
                        Functions.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                Functions.scheduleEvent(scheduler, this,
                        Functions.createAnimationAction(this, 0),
                        Functions.getAnimationPeriod(this));
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
            Functions.unscheduleAllEvents(scheduler, this);

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
        Functions.unscheduleAllEvents(scheduler, this);

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
        else if (entity.kind == EntityKind.SAPLING)
        {
            return transformSapling(entity, world, scheduler, imageStore);
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("transformPlant not supported for %s", entity));
        }
    }
    }




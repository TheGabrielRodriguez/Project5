/**
 * An action that can be taken by an entity
 */
public final class Action
{
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) { // action class (swtich)
       switch (this.kind) {
           case ACTIVITY:
               executeActivityAction(scheduler);
                break;

           case ANIMATION:
                executeAnimationAction(scheduler);
               break;
        }
   }

    public void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                Functions.createAnimationAction(this.entity,
                    Math.max(this.repeatCount - 1,0)),
                this.entity.getAnimationPeriod());
        }
    }

    public void executeActivityAction(EventScheduler scheduler)
    {
        switch (this.entity.kind) {
            case SAPLING:
                entity.executeSaplingActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case TREE:
                entity.executeTreeActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case FAIRY:
                entity.executeFairyActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case DUDE_NOT_FULL:
                entity.executeDudeNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case DUDE_FULL:
                entity.executeDudeFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        this.entity.kind));
        }
    }
}

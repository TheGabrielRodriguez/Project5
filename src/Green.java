public interface Green{
    boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
    void deductHealth(int given);
}
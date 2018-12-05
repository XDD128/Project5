import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends AbstractMiner
{

    private int resourceCount;

    public MinerNotFull( String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod)
    {

        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> notFullTarget = world.findNearest( this.getPosition(),
                Ore.class);
        //change above to Ore.class
        if (!notFullTarget.isPresent() ||
                !moveTo( world, notFullTarget.get(), scheduler) ||
                !transformNotFull( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }



    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= getResourceLimit()) {
            transform(world,scheduler,imageStore);
            return true;
        }

        return false;
    }



    public void _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        resourceCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }


}
import processing.core.PImage;

import java.util.List;

public abstract class AbstractMiner extends AbstractMovingEntity{

    private String id;
    private int resourceLimit;
    public AbstractMiner(String id, Point position,
                         List<PImage> images, int resourceLimit,
                         int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }
    public int getResourceLimit() {return resourceLimit;}
    public void transform( WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore) {
        //Change Entity to ActiveEntity
        if (getClass() == MinerFull.class)
        {MinerNotFull miner = getPosition().createMinerNotFull(id, resourceLimit,
                getActionPeriod(), getAnimationPeriod(),
                getImages());
        world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions( scheduler, world, imageStore);}
        else
        {MinerFull miner = getPosition().createMinerFull(id, resourceLimit,
                    getActionPeriod(), getAnimationPeriod(),
                    getImages());
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions( scheduler, world, imageStore);}

    }
    public void _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {return;}

    public boolean getOccupance(WorldModel world, Point newPos){
        return world.isOccupied(newPos);
    }
}
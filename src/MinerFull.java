import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends AbstractMiner{

    public MinerFull( String id, Point position,
                List<PImage> images, int resourceLimit,
                int actionPeriod, int animationPeriod)
    {

        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);


    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> fullTarget = world.findNearest( getPosition(),
                Blacksmith.class);
        //change above to Blacksmith.class
        if (fullTarget.isPresent() &&
                moveTo( world, fullTarget.get(), scheduler))
        {
            transform( world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent( this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }







}
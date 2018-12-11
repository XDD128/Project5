import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDestroyer extends AbstractMovingEntity
{
    private final String QUAKE_KEY = "quake";


    public AbstractDestroyer ( Point position,
                     List<PImage> images,
                     int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);

    }




    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> target = getNearestTarget(world);
        //change above to Vein.class
        long nextPeriod = this.getActionPeriod();

        if (target.isPresent())
        {                                   //changed position to .getPosition()
            Point tgtPos = target.get().getPosition();

            if (moveTo( world, target.get(), scheduler))
            {   //change Entity to ActiveEntity
                Quake quake = tgtPos.createQuake(
                        imageStore.getImageList( QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }
    public void _moveTo(WorldModel world, Entity target, EventScheduler scheduler){

        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);

    }


    public boolean getOccupance(WorldModel world, Point newPos){
        Optional<Entity> occupant = world.getOccupant(newPos);
        return (occupant.isPresent());
    }

    protected abstract Optional<Entity> getNearestTarget(WorldModel world);
}
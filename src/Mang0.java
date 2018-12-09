import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Mang0 extends AbstractMovingEntity
{
    private final String QUAKE_KEY = "quake";



    public Mang0 ( Point position,
                     List<PImage> images,
                     int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);

    }




    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> Mang0Target = world.findNearest(
                this.getPosition(), SmashBall.class);
        //change above to Vein.class
        long nextPeriod = this.getActionPeriod();

        if (Mang0Target.isPresent())
        {                                   //changed position to .getPosition()
            Point tgtPos = Mang0Target.get().getPosition();

            if (moveTo( world, Mang0Target.get(), scheduler))
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
        return (occupant.isPresent() && !(occupant.get().getClass() == Ore.class));
    }
}
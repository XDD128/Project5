import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SmashBall extends AbstractMovingEntity
{
    private final String QUAKE_KEY = "quake";



    public SmashBall ( Point position,
                     List<PImage> images,
                     int actionPeriod, int animationPeriod)
    {

        super(position,images,actionPeriod, animationPeriod);
        this.strategy = new RandomPathingStrategy();
        this.SELECTED_NEIGHBORS = PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS;
    }




    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> ballTarget = world.findNearest(
                this.getPosition(), MinerNotFull.class);
        //change above to Vein.class
        long nextPeriod = this.getActionPeriod();

        if (ballTarget.isPresent())
        {                                   //changed position to .getPosition()
            Point tgtPos = ballTarget.get().getPosition();

            if (moveTo( world, ballTarget.get(), scheduler))
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
        return (occupant.isPresent() && !(occupant.get().getClass() == OreBlob.class));
    }
}
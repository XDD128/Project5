import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Mang0 extends AbstractDestroyer
{
    private final String QUAKE_KEY = "quake";



    public Mang0 ( Point position,
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
            if (getPosition().adjacent( target.get().getPosition())) {

                ((SmashBall)target.get()).damage(1);

                System.out.println("Miner hit Smash Ball to: " + (((SmashBall)target.get()).getHealth()));

                if (((SmashBall)target.get()).getHealth() == 0) {
                    {
                        System.out.println("THE MASTER HAS AWAKENED");
                        Master hand = new Master(getPosition(), imageStore.getImageList("master"),
                                getActionPeriod(), getAnimationPeriod());

                        world.removeEntity(this);
                        scheduler.unscheduleAllEvents(this);

                    world.removeEntity(target.get());
                    scheduler.unscheduleAllEvents(target.get());

                    world.addEntity(hand);
                    hand.scheduleActions(scheduler, world, imageStore);
                }
            }
                }else {
                Point nextPos = nextPosition( world, target.get().getPosition());

                if (!getPosition().equals(nextPos)) {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent()) {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
        }
            }

            }


        scheduler.scheduleEvent( this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    protected Optional<Entity> getNearestTarget(WorldModel world)
    {
        return world.findNearest(
                this.getPosition(), SmashBall.class);
    }

    public void _moveTo(WorldModel world, Entity target, EventScheduler scheduler){
        return;
    }


}
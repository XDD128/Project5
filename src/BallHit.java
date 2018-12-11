import processing.core.PImage;

import java.util.List;

public class BallHit extends AbstractMovingEntity
{

    private final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private int health;
    public BallHit( Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod, int health)
    {

        super(position, images, actionPeriod, animationPeriod);
        this.health = health;
    }



    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        SmashBall weakenedSmashBall = new SmashBall(getPosition(), imageStore.getImageList("ball"), 100, 20, health);
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);


        world.addEntity(weakenedSmashBall);
        weakenedSmashBall.scheduleActions( scheduler, world, imageStore);
    }

    public void scheduleActions( EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());

    }
    public void _moveTo(WorldModel world, Entity target, EventScheduler scheduler) {return;}
    public boolean getOccupance(WorldModel world, Point newPos){
        return world.isOccupied(newPos);
    }
}